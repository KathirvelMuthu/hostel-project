package com.hostel.model;

public class Message {
    private int id;
    private String senderId;
    private String receiverId;
    private String content;
    private String timestamp;
    private String senderName; // Transient for display

    public Message() {}

    public Message(int id, String senderId, String receiverId, String content, String timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(int id, String senderId, String receiverId, String content, String timestamp, String senderName) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
        this.senderName = senderName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
}
