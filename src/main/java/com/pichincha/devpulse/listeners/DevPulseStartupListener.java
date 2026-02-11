package com.pichincha.devpulse.listeners;

import com.intellij.ide.AppLifecycleListener;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.service.MessageService;
import com.pichincha.devpulse.service.NotificationService;
import com.pichincha.devpulse.service.TelemetryService;

import java.util.List;

public class DevPulseStartupListener implements AppLifecycleListener {
  @Override
  public void appFrameCreated(List<String> commandLineArgs) {
    TelemetryService.getInstance().trackPluginStartup();
    
    MessageService messageService = MessageService.getInstance();
    NotificationService notificationService = NotificationService.getInstance();
    
    List<Message> unreadMessages = messageService.getUnreadMessages();
    
    if (!unreadMessages.isEmpty()) {
      notificationService.showInfoNotification(
          "DevPulse BP",
          "Tienes " + unreadMessages.size() + " mensaje(s) sin leer"
      );
    }
  }
}
