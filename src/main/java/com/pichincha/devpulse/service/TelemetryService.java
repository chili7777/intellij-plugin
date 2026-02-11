package com.pichincha.devpulse.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.pichincha.devpulse.settings.DevPulseSettings;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TelemetryService {
  private static final Logger LOG = Logger.getInstance(TelemetryService.class);

  public static TelemetryService getInstance() {
    return ApplicationManager.getApplication().getService(TelemetryService.class);
  }

  public void trackPluginInstalled() {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    if (!settings.isEnableTelemetry()) {
      return;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("event", "plugin_installed");
    data.put("version", settings.getPluginVersion());
    data.put("userEmail", settings.getUserEmail());
    data.put("timestamp", LocalDateTime.now().toString());

    sendTelemetry(data);
  }

  public void trackMessageRead(String messageId) {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    if (!settings.isEnableTelemetry()) {
      return;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("event", "message_read");
    data.put("messageId", messageId);
    data.put("userEmail", settings.getUserEmail());
    data.put("timestamp", LocalDateTime.now().toString());

    sendTelemetry(data);
  }

  public void trackPluginStartup() {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    if (!settings.isEnableTelemetry()) {
      return;
    }

    Map<String, Object> data = new HashMap<>();
    data.put("event", "plugin_startup");
    data.put("version", settings.getPluginVersion());
    data.put("userEmail", settings.getUserEmail());
    data.put("timestamp", LocalDateTime.now().toString());

    sendTelemetry(data);
  }

  private void sendTelemetry(Map<String, Object> data) {
    LOG.info("Telemetry (mock): " + data);
  }
}
