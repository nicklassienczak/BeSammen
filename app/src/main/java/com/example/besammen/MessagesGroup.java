package com.example.besammen;


public class MessagesGroup {

    private String message, senderId, currentTime, senderName, receiverName;
    private long timeStamp;

    public MessagesGroup() {

    }

    public MessagesGroup(String message, String senderId, String currentTime, String senderName, String receiverName, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.currentTime = currentTime;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
