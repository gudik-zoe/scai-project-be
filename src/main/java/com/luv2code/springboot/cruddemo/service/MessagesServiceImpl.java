package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.MessagesDAO;
import com.luv2code.springboot.cruddemo.entity.Message;

@Service
public class MessagesServiceImpl implements MessagesService {
	
	private MessagesDAO messagesDAO;
	
	@Autowired
	public MessagesServiceImpl(MessagesDAO theMessagesDAO) {
		messagesDAO = theMessagesDAO;
	}

	@Override
	@Transactional
	public Integer getMyMessages(int accountId) {
		return messagesDAO.getMyMessages(accountId);
	}

	@Override
	@Transactional
	public Message sendMessage(Message theMessage) {
		return messagesDAO.sendMessage(theMessage);
	}

	@Override
	@Transactional
	public List<Message> getMessagesReceivedFrom(int accountId , int senderId) {
		return messagesDAO.getMessagesReceivedFrom(accountId , senderId);
	}

	@Override
	@Transactional
	public Integer unSeenMessagesFrom(int accountId , int account2Id) {
		return messagesDAO.unSeenMessagesFrom(accountId ,account2Id );
		
	}

	@Override
	@Transactional
	public void messageSeen(int user1Id , int user2Id) {
		 messagesDAO.messageSeen(user1Id , user2Id);
		
	}



}
