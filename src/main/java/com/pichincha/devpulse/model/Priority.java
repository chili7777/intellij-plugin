package com.pichincha.devpulse.model;

public enum Priority {
  LOW("Baja", 1),
  NORMAL("Normal", 2),
  HIGH("Alta", 3),
  CRITICAL("Crítica", 4);

  private final String displayName;
  private final int level;

  Priority(String displayName, int level) {
    this.displayName = displayName;
    this.level = level;
  }

  public String getDisplayName() {
    return displayName;
  }

  public static Priority fromDisplayName(String displayName) {
    for (Priority priority : values()) {
      if (priority.displayName.equals(displayName)) {
        return priority;
      }
    }
    return NORMAL;
  }

  public int getLevel() {
    return level;
  }

  public static Priority fromLevel(int level) {
    for (Priority priority : values()) {
      if (priority.level == level) {
        return priority;
      }
    }
    return NORMAL;
  }
}
