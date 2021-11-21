package com.example.social_media_chat_app.ModelClass;

public class Users {

    public String uid;
    public String name;
    public String email;
    public String imageUri;
    public String status;

    public Users() {
    }

    public String getUid() {
        return uid;
    }

    public Users(String uid, String name, String email, String imageUri, String status) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
