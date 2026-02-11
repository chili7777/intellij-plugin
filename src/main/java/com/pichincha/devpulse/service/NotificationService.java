package com.pichincha.devpulse.service;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.pichincha.devpulse.model.Message;
import com.pichincha.devpulse.model.Priority;
import com.pichincha.devpulse.settings.DevPulseSettings;

public class NotificationService {
  private static final Logger LOG = Logger.getInstance(NotificationService.class);
  private static final String NOTIFICATION_GROUP_ID = "DevPulse Notifications";

  public static NotificationService getInstance() {
    return ApplicationManager.getApplication().getService(NotificationService.class);
  }

  public void showMessage(Message message) {
    DevPulseSettings settings = DevPulseSettings.getInstance();
    if (!settings.isEnableNotifications()) {
      LOG.info("Notifications disabled, skipping message: " + message.getTitle());
      return;
    }

    NotificationType type = getNotificationType(message.getPriority());
    
    Notification notification = NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(
            message.getTitle(),
            message.getContent(),
            type
        );

    if (message.getLink() != null && !message.getLink().isEmpty()) {
      notification.addAction(new com.intellij.notification.NotificationAction("Ver más") {
        @Override
        public void actionPerformed(com.intellij.openapi.actionSystem.AnActionEvent e, Notification notification) {
          com.intellij.ide.BrowserUtil.browse(message.getLink());
          MessageService.getInstance().markAsRead(message.getId());
          notification.expire();
        }
      });
    }

    notification.addAction(new com.intellij.notification.NotificationAction("Marcar como leído") {
      @Override
      public void actionPerformed(com.intellij.openapi.actionSystem.AnActionEvent e, Notification notification) {
        MessageService.getInstance().markAsRead(message.getId());
        notification.expire();
      }
    });

    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    if (projects.length > 0) {
      Notifications.Bus.notify(notification, projects[0]);
    } else {
      Notifications.Bus.notify(notification);
    }

    LOG.info("Displayed notification: " + message.getTitle());
  }

  private NotificationType getNotificationType(Priority priority) {
    return switch (priority) {
      case CRITICAL -> NotificationType.ERROR;
      case HIGH -> NotificationType.WARNING;
      default -> NotificationType.INFORMATION;
    };
  }

  public void showInfoNotification(String title, String content) {
    Notification notification = NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(title, content, NotificationType.INFORMATION);
    
    Notifications.Bus.notify(notification);
  }

  public void showErrorNotification(String title, String content) {
    Notification notification = NotificationGroupManager.getInstance()
        .getNotificationGroup(NOTIFICATION_GROUP_ID)
        .createNotification(title, content, NotificationType.ERROR);
    
    Notifications.Bus.notify(notification);
  }
}
