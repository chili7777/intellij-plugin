package com.pichincha.devpulse.model;

public enum MessageType {
  INFORMATIVE("Informativo", "info"),
  GUIDELINE("Lineamiento", "guideline"),
  DOCUMENTATION("Documentación", "documentation"),
  ANNOUNCEMENT("Anuncio", "announcement"),
  ALERT("Alerta", "alert"),
  TRAINING_PILL("Píldora Educativa", "training");

  private final String displayName;
  private final String code;

  MessageType(String displayName, String code) {
    this.displayName = displayName;
    this.code = code;
  }

  public String getDisplayName() {
    return displayName;
  }

  public static MessageType fromDisplayName(String displayName) {
    for (MessageType type : values()) {
      if (type.displayName.equals(displayName)) {
        return type;
      }
    }
    return INFORMATIVE;
  }

  public String getCode() {
    return code;
  }

  public static MessageType fromCode(String code) {
    for (MessageType type : values()) {
      if (type.code.equals(code)) {
        return type;
      }
    }
    return INFORMATIVE;
  }
}
