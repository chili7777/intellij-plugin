package com.pichincha.devpulse.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.model.MessageType;
import com.pichincha.devpulse.model.Priority;
import com.pichincha.devpulse.util.LocalDateTimeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MessageService {
  private static final Logger LOG = Logger.getInstance(MessageService.class);
  private final List<Message> messages = new ArrayList<>();
  private final Gson gson;
  private final File storageFile;

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
    if (messages.isEmpty()) {
      initializeMockMessages();
    }
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

  private void initializeMockMessages() {
    Message msg1 = new Message(
        UUID.randomUUID().toString(),
        "Bienvenido a DevPulse BP",
        "Este es el nuevo canal de comunicación técnica para desarrolladores. " +
        "Aquí recibirás píldoras informativas, lineamientos, documentación y alertas importantes.",
        MessageType.INFORMATIVE,
        Priority.NORMAL
    );
    msg1.setLink("https://confluence.pichincha.com/devpulse");

    Message msg2 = new Message(
        UUID.randomUUID().toString(),
        "Nuevo Lineamiento: Uso de Lombok",
        "Se ha actualizado el lineamiento de uso de Lombok en proyectos Spring Boot. " +
        "Todos los DTOs y entidades deben usar anotaciones Lombok para reducir boilerplate.",
        MessageType.GUIDELINE,
        Priority.HIGH
    );
    msg2.setLink("https://confluence.pichincha.com/lineamientos/lombok");
    msg2.setCodeExample("@Getter\n@Setter\n@Builder(toBuilder = true)\npublic class UserDto {\n  private String id;\n  private String name;\n}");

    Message msg3 = new Message(
        UUID.randomUUID().toString(),
        "Alerta: Vulnerabilidad Crítica en Log4j",
        "Se ha detectado una vulnerabilidad crítica (CVE-2021-44228) en Log4j. " +
        "Actualizar inmediatamente a la versión 2.17.1 o superior.",
        MessageType.ALERT,
        Priority.CRITICAL
    );
    msg3.setLink("https://confluence.pichincha.com/security/log4j-vulnerability");

    Message msg4 = new Message(
        UUID.randomUUID().toString(),
        "Píldora: Manejo de Excepciones en Spring",
        "Aprende las mejores prácticas para el manejo de excepciones en Spring Boot usando @ControllerAdvice.",
        MessageType.TRAINING_PILL,
        Priority.NORMAL
    );
    msg4.setLink("https://confluence.pichincha.com/training/exception-handling");
    msg4.setCodeExample("@ControllerAdvice\npublic class GlobalExceptionHandler {\n  @ExceptionHandler(ResourceNotFoundException.class)\n  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {\n    return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));\n  }\n}");

    messages.add(msg1);
    messages.add(msg2);
    messages.add(msg3);
    messages.add(msg4);
    
    saveMessages();
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
  }

  public void refreshMessages() {
    LOG.info("Refreshing messages from server (mock)");
  }

  public int getUnreadCount() {
    return (int) messages.stream().filter(m -> !m.isRead()).count();
  }
}
