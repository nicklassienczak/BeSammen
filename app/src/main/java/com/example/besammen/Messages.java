package com.example.besammen;

//Arne
//This is a model class that represents a message in the specific chat

public class Messages {

    private String message, senderId, currentTime;
    private long timeStamp;

    //The empty constructor is required by firebase to map the documents retrieved from the database to objects of the BeSammenModel class.
    public Messages() {

    }

    //This constructor is used to pair the parameters to the values
    public Messages(String message, String senderId, String currentTime, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.currentTime = currentTime;
        this.timeStamp = timeStamp;
    }

    //Getters and setters
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
