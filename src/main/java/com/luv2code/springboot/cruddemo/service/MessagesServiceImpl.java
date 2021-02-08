package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.springboot.cruddemo.jpa.repositories.MessageJpaRepo;

@Service
public class MessagesServiceImpl implements MessagesService {

	@Autowired
	private MessageJpaRepo messageJpaRepo;

	public MessagesServiceImpl() {

	}

	@Override
	public Integer getMyMessages(int accountId) {
		List<Message> myUnseenMessages = messageJpaRepo.getMyUnseenMessages(accountId);
		return myUnseenMessages.size();
	}

	@Override
	public Message sendMessage(Message theMessage) {
		messageJpaRepo.save(theMessage);
		return theMessage;
	}

	@Override
	public List<Message> getConversation(int accountId, int senderId) {
		List<Message> convWithUser = messageJpaRepo.getConversation(accountId, senderId);
		return convWithUser;
	}

	@Override
	public Integer unSeenMessagesFrom(int accountId, int account2Id) {
		List<Message> unSeenMessagesFromUser = messageJpaRepo.getunSeenMessagesFromUser(accountId, account2Id);
		return unSeenMessagesFromUser.size();

	}

	@Override
	public void messageSeen(int accountId, int user2Id) {
		List<Message> theUnseenMessagesBetweenMeAndUser2Id = messageJpaRepo.getunSeenMessagesFromUser(accountId,
				user2Id);
		for (Message message : theUnseenMessagesBetweenMeAndUser2Id) {
			message.setSeen(true);
			messageJpaRepo.save(message);
		}

	}

}
