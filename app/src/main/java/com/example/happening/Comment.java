package com.example.happening;

public class Comment {

    private String userName;
    private String comment;
    private String date;

    public Comment(String userName, String comment, String date) {
        this.userName = userName;
        this.comment = comment;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
