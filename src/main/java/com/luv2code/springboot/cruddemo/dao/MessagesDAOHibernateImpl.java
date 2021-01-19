package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.springboot.cruddemo.service.StorageService;

@Repository
public class MessagesDAOHibernateImpl implements MessagesDAO {

	private EntityManager entityManager;
	
	private StorageService storageService;

	@Autowired
	public MessagesDAOHibernateImpl(EntityManager theEntityManager , StorageService theStorageService) {
		entityManager = theEntityManager;
		storageService= theStorageService;

	}

	@Override
	public Integer getMyMessages(int accountId) {

		Session currentSession = entityManager.unwrap(Session.class);
		Query<Message> theQuery = currentSession
				.createQuery("from Message where id_receiver=" + accountId + " and seen = " + false, Message.class);
		List<Message> myMessages = theQuery.getResultList();
		return myMessages.size();
	}

	@Override
	public Message sendMessage(Message theMessage) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.save(theMessage);

		return theMessage;
	}

	@Override
	public List<Message> getMessagesReceivedFrom(int accountId, int senderId) {
		Session currentSession = entityManager.unwrap(Session.class);
		
		Account theAccount = currentSession.get(Account.class, senderId);
		if(theAccount == null) {
			throw new CustomeException("this account doesn't exist");
		}else {
			
		Query<Message> theQuery = currentSession
				.createQuery(
						"from Message where (id_sender=" + senderId + "and id_receiver=" + accountId
								+ "  ) or (id_sender =" + accountId + "and id_receiver=" + senderId + ")",
						Message.class);
		List<Message> theMessages = theQuery.getResultList();

		return theMessages;
		}
	}

	@Override
	public Integer unSeenMessagesFrom(int accountId, int account2Id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Message> theQuery = currentSession.createQuery(
				"from Message where id_sender=" + account2Id + "and id_receiver=" + accountId + "and seen = " + false,
				Message.class);
		List<Message> theList = theQuery.getResultList();
		if(theList.size() > 0) {
			return theList.size();
		}else {
			return 0;
		}
	}

	@Override
	public void messageSeen(int user1Id, int user2Id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Message> theQuery = currentSession
				.createQuery("from Message where id_sender=" + user2Id + "and id_receiver=" + user1Id + " and seen = " + false , Message.class);
		List<Message> theMessages = theQuery.getResultList();
		
		for (Message message : theMessages) {
			message.setSeen(true);
			currentSession.update(message);
		}
		

	}



}
