package com.github.joehaivo.util;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public class Notifier {

  private static final NotificationGroup NOTIFICATION_GROUP =
          new NotificationGroup("IconFontViewer Notification Group", NotificationDisplayType.BALLOON, true);

  public static void notifyError(@Nullable Project project, String content) {
    NOTIFICATION_GROUP.createNotification(content, NotificationType.ERROR).notify(project);
  }

  public static void notifyInfo(@Nullable Project project, String content) {
    NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION).notify(project);
  }
}