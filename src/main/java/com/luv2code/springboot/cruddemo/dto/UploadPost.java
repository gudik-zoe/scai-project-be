package com.luv2code.springboot.cruddemo.dto;

public class UploadPost {

    private String text;

//    private MultipartFile photo;

    private int creatorId;

    public UploadPost(){

    }

    public UploadPost(String text, int creatorId) {
        this.text = text;
//        this.photo = photo;
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

//    public MultipartFile getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(MultipartFile photo) {
//        this.photo = photo;
//    }
}
