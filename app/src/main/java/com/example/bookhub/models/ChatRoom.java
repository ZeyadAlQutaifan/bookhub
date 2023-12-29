package com.example.bookhub.models;

public class ChatRoom {
    private String senderId ;
    private String receiverId ;
    private long creationDate ;

    public ChatRoom() {
    }

    public ChatRoom(String senderId, String receiverId, long creationDate) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.creationDate = creationDate;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
