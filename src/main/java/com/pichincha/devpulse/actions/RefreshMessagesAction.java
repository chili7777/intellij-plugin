package com.pichincha.devpulse.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.pichincha.devpulse.service.MessageService;
import com.pichincha.devpulse.service.NotificationService;
import org.jetbrains.annotations.NotNull;

public class RefreshMessagesAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    MessageService messageService = MessageService.getInstance();
    NotificationService notificationService = NotificationService.getInstance();
    
    messageService.refreshMessages();
    
    notificationService.showInfoNotification(
        "DevPulse BP",
        "Mensajes actualizados correctamente"
    );
  }
}
