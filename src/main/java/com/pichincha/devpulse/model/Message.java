package com.pichincha.devpulse.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
  private String id;
  private String title;
  private String content;
  private MessageType type;
  private Priority priority;
  private LocalDateTime createdAt;
  private LocalDateTime scheduledFor;
  private String link;
  private String codeExample;
  private boolean read;
  private LocalDateTime readAt;

  public Message() {
  }

  public Message(String id, String title, String content, MessageType type, Priority priority) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.type = type;
    this.priority = priority;
    this.createdAt = LocalDateTime.now();
    this.read = false;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public Priority getPriority() {
    return priority;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getScheduledFor() {
    return scheduledFor;
  }

  public void setScheduledFor(LocalDateTime scheduledFor) {
    this.scheduledFor = scheduledFor;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getCodeExample() {
    return codeExample;
  }

  public void setCodeExample(String codeExample) {
    this.codeExample = codeExample;
  }

  public boolean isRead() {
    return read;
  }

  public void setRead(boolean read) {
    this.read = read;
    if (read && this.readAt == null) {
      this.readAt = LocalDateTime.now();
    }
  }

  public LocalDateTime getReadAt() {
    return readAt;
  }

  public void setReadAt(LocalDateTime readAt) {
    this.readAt = readAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Message{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", type=" + type +
        ", priority=" + priority +
        ", read=" + read +
        '}';
  }
}
