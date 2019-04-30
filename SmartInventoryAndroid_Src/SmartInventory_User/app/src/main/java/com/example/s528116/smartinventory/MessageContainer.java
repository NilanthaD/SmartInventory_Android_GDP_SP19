package com.example.s528116.smartinventory;

import android.widget.TextView;

import java.util.Date;

public class MessageContainer {
    private String title, from, to, messageDocId, message;
    private Date composeDate;
    public MessageContainer(String title, String from, String to, Date composeDate, String messageDocId, String message){
        this.title = title;
        this. from = from;
        this.to = to;
        this.composeDate = composeDate;
        this.messageDocId = messageDocId;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessageDocId() {
        return messageDocId;
    }

    public Date getComposeDate() {
        return composeDate;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
