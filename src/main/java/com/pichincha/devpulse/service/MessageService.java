package com.pichincha.devpulse.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.model.MessageType;
import com.pichincha.devpulse.model.Priority;
import com.pichincha.devpulse.settings.DevPulseSettings;
import com.pichincha.devpulse.util.LocalDateTimeAdapter;
import com.intellij.util.concurrency.AppExecutorUtil;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageService {
  private static final Logger LOG = Logger.getInstance(MessageService.class);
  private final List<Message> messages = new ArrayList<>();
  private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();
  private final Gson gson;
  private final File storageFile;
  private EventSource sseEventSource;
  private int retryCount = 0;
  private boolean isConnecting = false;
  private ScheduledFuture<?> periodicRefreshFuture;

  public interface MessageListener {
    void onMessagesUpdated();
  }

  public MessageService() {
    this.gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .setPrettyPrinting()
        .create();

    String userHome = System.getProperty("user.home");
    File devPulseDir = new File(userHome, ".devpulse");
    if (!devPulseDir.exists()) {
      devPulseDir.mkdirs();
    }
    this.storageFile = new File(devPulseDir, "messages.json");

    loadMessages();
    refreshMessages(); // Initial sync on startup

    // This is the "Reactive" engine: It stays connected waiting for pushes from Kafka (via SSE bridge)
    startRealTimeListener();
    
    // Fallback: Periodic refresh every 5 minutes to ensure synchronization
    startPeriodicRefresh();
  }

  private void startPeriodicRefresh() {
    if (periodicRefreshFuture != null && !periodicRefreshFuture.isCancelled()) {
      periodicRefreshFuture.cancel(false);
    }
    
    periodicRefreshFuture = AppExecutorUtil.getAppScheduledExecutorService()
        .scheduleWithFixedDelay(this::refreshMessages, 5, 5, TimeUnit.MINUTES);
    
    LOG.info("Started periodic refresh task (every 5 minutes)");
  }

  public static MessageService getInstance() {
    return ApplicationManager.getApplication().getService(MessageService.class);
  }

  private void loadMessages() {
    if (!storageFile.exists()) {
      return;
    }

    try (FileReader reader = new FileReader(storageFile)) {
      Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
      List<Message> loaded = gson.fromJson(reader, listType);
      if (loaded != null) {
        messages.clear();
        messages.addAll(loaded);
        LOG.info("Loaded " + messages.size() + " messages from storage");
      }
    } catch (IOException e) {
      LOG.error("Failed to load messages", e);
    }
  }

  private void saveMessages() {
    try (FileWriter writer = new FileWriter(storageFile)) {
      gson.toJson(messages, writer);
      LOG.info("Saved " + messages.size() + " messages to storage");
    } catch (IOException e) {
      LOG.error("Failed to save messages", e);
    }
  }

  public List<Message> getAllMessages() {
    return new ArrayList<>(messages);
  }

  public List<Message> getUnreadMessages() {
    return messages.stream()
        .filter(m -> !m.isRead())
        .collect(Collectors.toList());
  }

  public Message getMessageById(String id) {
    return messages.stream()
        .filter(m -> m.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public void markAsRead(String messageId) {
    Message message = getMessageById(messageId);
    if (message != null && !message.isRead()) {
      message.setRead(true);
      saveMessages();

      TelemetryService telemetry = TelemetryService.getInstance();
      telemetry.trackMessageRead(messageId);
    }
  }

  public void addMessage(Message message) {
    messages.add(0, message);
    saveMessages();
    notifyListeners();
  }

  public void addMessageListener(MessageListener listener) {
    listeners.add(listener);
  }

  public void removeMessageListener(MessageListener listener) {
    listeners.remove(listener);
  }

  private void notifyListeners() {
    for (MessageListener listener : listeners) {
      listener.onMessagesUpdated();
    }
  }

  public void refreshMessages() {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    String apiUrl = settings.getApiUrl();

    if (apiUrl == null || apiUrl.isEmpty()) {
      LOG.warn("API URL not configured, skipping refresh");
      return;
    }

    LOG.info("Refreshing messages from " + apiUrl);

    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url(apiUrl + "/api/v1/messages")
        .build();

    ApplicationManager.getApplication().executeOnPooledThread(() -> {
      try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
          LOG.error("Failed to fetch messages: " + response.code());
          return;
        }

        String body = response.body().string();
        Type listType = new TypeToken<ArrayList<Message>>(){}.getType();
        List<Message> newMessages = gson.fromJson(body, listType);

        ApplicationManager.getApplication().invokeLater(() -> {
          boolean hasNew = false;
          for (Message msg : newMessages) {
            if (messages.stream().noneMatch(m -> m.getId().equals(msg.getId()))) {
              messages.add(0, msg);
              hasNew = true;
            }
          }

          if (hasNew) {
            saveMessages();
            notifyListeners();
            NotificationService.getInstance().showInfoNotification(
                "DevPulse BP",
                "Se han recibido nuevas pildoras de conocimiento"
            );
          }
        });
      } catch (IOException e) {
        LOG.error("Error refreshing messages", e);
      }
    });
  }


  private void startRealTimeListener() {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    String apiUrl = settings.getApiUrl();

    // If API URL is not configured, we do nothing
    if (apiUrl == null || apiUrl.isEmpty()) {
      return;
    }

    // REAL REACTIVE IMPLEMENTATION using OkHttp SSE
    OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS) // Infinite timeout for stream
        .retryOnConnectionFailure(true)
        .build();

    Request request = new Request.Builder()
        .url(apiUrl + "/api/v1/messages/stream")
        .header("Accept", "text/event-stream")
        .build();

    EventSourceListener listener = new EventSourceListener() {
      @Override
      public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
        LOG.info("SSE Connection opened successfully. Now in REACTIVE mode.");
        retryCount = 0; // Reset retry count on success
        isConnecting = false;
      }

      @Override
      public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
        LOG.info("Instant message received from Kafka via SSE: " + data);
        try {
          Message message = gson.fromJson(data, Message.class);
          ApplicationManager.getApplication().invokeLater(() -> {
            addMessage(message);
            NotificationService.getInstance().showMessage(message);
          });
        } catch (Exception e) {
          LOG.error("Failed to parse reactive message", e);
        }
      }

      @Override
      public void onClosed(@NotNull EventSource eventSource) {
        LOG.warn("SSE Connection closed.");
        isConnecting = false;
        scheduleReconnection();
      }

      @Override
      public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        LOG.error("SSE Connection error: " + (t != null ? t.getMessage() : "Unknown error"));
        isConnecting = false;
        scheduleReconnection();
      }
    };

    isConnecting = true;
    this.sseEventSource = EventSources.createFactory(client).newEventSource(request, listener);
  }

  private void scheduleReconnection() {
    if (isConnecting) return;

    // Exponential backoff: 2, 4, 8, 16, 32, max 60 seconds
    long delaySeconds = (long) Math.min(Math.pow(2, retryCount), 60);
    retryCount++;

    LOG.info("Scheduling SSE reconnection in " + delaySeconds + " seconds (Attempt " + retryCount + ")");

    ApplicationManager.getApplication().executeOnPooledThread(() -> {
      try {
        Thread.sleep(delaySeconds * 1000);
        startRealTimeListener();
      } catch (InterruptedException e) {
        LOG.error("Reconnection delay interrupted", e);
      }
    });
  }

  public int getUnreadCount() {
    return (int) messages.stream().filter(m -> !m.isRead()).count();
  }
}