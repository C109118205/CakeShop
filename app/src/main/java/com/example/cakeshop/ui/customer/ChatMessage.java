package com.example.cakeshop.ui.customer;

import android.os.Message;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.PropertyName;

import java.util.Date;

public class ChatMessage {
    @PropertyName("message")
    public String message;
    @PropertyName("sender")
    public String sender;

//    public long timestamp;

    public ChatMessage() {
        // 空的建構函式是必需的
    }
    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(){
        this.message = message;
    }
    public String getSender(){
        return sender;
    }
    public void setSender(){
        this.sender =sender;
    }
}
