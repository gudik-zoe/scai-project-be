package com.luv2code.utility;

public class UploadPost<T> {
	private String text;
	
	private T image;
	
	public UploadPost() {
	
	}

	public UploadPost(String text, T image) {
		this.text = text;
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public T getImage() {
		return image;
	}

	public void setImage(T image) {
		this.image = image;
	}
	
	
}
