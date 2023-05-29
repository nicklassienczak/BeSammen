package com.example.besammen;


import android.app.Activity;

public class Messages extends Activity {

    private String message, senderId, currentTime;
    private long timeStamp;

    public Messages() {

    }

    public Messages(String message, String senderId, String currentTime, long timeStamp) {
        this.message = message;
        this.senderId = senderId;
        this.currentTime = currentTime;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
