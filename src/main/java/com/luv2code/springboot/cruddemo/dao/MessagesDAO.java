package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Message;

public interface MessagesDAO {
	

	public Integer getMyMessages(int accountId);
	
	public Message sendMessage( Message theMessage);

	public List<Message> getMessagesReceivedFrom(int accountId , int senderId);

	public Integer unSeenMessagesFrom(int accountId , int account2Id);

	public void messageSeen(int user1Id , int user2Id);

}
