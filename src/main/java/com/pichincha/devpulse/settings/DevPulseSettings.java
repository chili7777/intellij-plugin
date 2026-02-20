package com.pichincha.devpulse.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
    name = "DevPulseSettings",
    storages = @Storage("devpulse.xml")
)
public class DevPulseSettings implements PersistentStateComponent<DevPulseSettings> {
  public String apiUrl = "http://localhost:8080";
  public String userEmail = "";
  public boolean enableNotifications = true;
  public boolean enableTelemetry = true;
  public String pluginVersion = "1.0.0";
  public long lastSyncTimestamp = 0;

  public static DevPulseSettings getInstance() {
    return ApplicationManager.getApplication().getService(DevPulseSettings.class);
  }

  @Nullable
  @Override
  public DevPulseSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull DevPulseSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public String getApiUrl() {
    return apiUrl;
  }

  public void setApiUrl(String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public boolean isEnableNotifications() {
    return enableNotifications;
  }

  public void setEnableNotifications(boolean enableNotifications) {
    this.enableNotifications = enableNotifications;
  }

  public boolean isEnableTelemetry() {
    return enableTelemetry;
  }

  public void setEnableTelemetry(boolean enableTelemetry) {
    this.enableTelemetry = enableTelemetry;
  }

  public String getPluginVersion() {
    return pluginVersion;
  }

  public void setPluginVersion(String pluginVersion) {
    this.pluginVersion = pluginVersion;
  }

  public long getLastSyncTimestamp() {
    return lastSyncTimestamp;
  }

  public void setLastSyncTimestamp(long lastSyncTimestamp) {
    this.lastSyncTimestamp = lastSyncTimestamp;
  }
}