package com.abhishek.onlineshop.Domain;

import android.widget.TextView;

public class NotificationDomain {
    private String title;
    private String message;
    private String date;
    public NotificationDomain() {

    }

    public NotificationDomain(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
