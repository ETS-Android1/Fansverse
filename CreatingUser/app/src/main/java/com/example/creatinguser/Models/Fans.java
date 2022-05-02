package com.example.creatinguser.Models;

public class Fans {
    String page_id;
    String userID;
    String userImage;
    String username;

    public Fans() {
    }

    public Fans(String page_id, String userID, String userImage, String username) {
        this.page_id = page_id;
        this.userID = userID;
        this.userImage = userImage;
        this.username = username;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
