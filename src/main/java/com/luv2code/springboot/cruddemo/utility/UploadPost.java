package com.luv2code.springboot.cruddemo.utility;

import org.springframework.web.multipart.MultipartFile;

public class UploadPost {

    private String text;

    private MultipartFile photo;

    private int creatorId;

    public UploadPost(){

    }

    public UploadPost(String text, MultipartFile photo, int creatorId) {
        this.text = text;
        this.photo = photo;
        this.creatorId = creatorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }
}
