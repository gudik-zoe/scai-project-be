package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;

@Repository
public class NotificationsDAOHibernateImpl implements NotificationsDAO {
	private EntityManager entityManager;

	@Autowired
	public NotificationsDAOHibernateImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public List<Notification> getMyNotification(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Notification> theQuery = currentSession.createQuery(
				"from Notification where not_receiver=" + accountId + " order by id_notification DESC",
				Notification.class);
		List<Notification> theNotificationList = theQuery.getResultList();
		return theNotificationList;
	}

	@Override
	public Notification addNotification(Notification theNotification) {
		Session currentSession = entityManager.unwrap(Session.class);

	
			currentSession.save(theNotification);

			return theNotification;
		}
	

	@Override
	public void deleteNotification() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificationHasBeenSeen(int notId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Notification> theQuery = currentSession.createQuery("from Notification where id_notification =" + notId,
				Notification.class);
		Notification theNot;
		try {
			theNot = theQuery.getSingleResult();
		} catch (Exception e) {
			throw new CustomeException("this  notification doesn't exist");
		}
		theNot.setSeen(true);
		currentSession.saveOrUpdate(theNot);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
