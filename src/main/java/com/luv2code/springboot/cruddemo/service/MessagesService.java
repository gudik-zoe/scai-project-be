package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.utility.ImageUrl;

public interface MessagesService {
	
	public Integer getMyMessages(int accountId);
	
	public Message sendMessage(Message theMessage);

	public List<Message> getMessagesReceivedFrom(int accountId , int senderId);

	public Integer  unSeenMessagesFrom(int accountId , int account2Id);
	
	public void messageSeen(int user1Id , int user2Id);
	
	

}
