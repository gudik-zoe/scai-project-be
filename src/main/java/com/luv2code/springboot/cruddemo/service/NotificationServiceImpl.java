package com.luv2code.springboot.cruddemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.jpa.repositories.NotificationJpaRepo;
import com.luv2code.utility.NotificationDetails;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationJpaRepo notificationJpaRepo;

	@Autowired
	private EntityManager entityManager;

	public NotificationServiceImpl() {

	}

	@Override
	public List<Notification> getMyNotification(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Notification> theQuery = currentSession
				.createQuery("from Notification where not_receiver=" + accountId + " order by date DESC",
						Notification.class)
				.setMaxResults(5);
		List<Notification> theNotificationList = theQuery.getResultList();
		return theNotificationList;
	}

	@Override
	public Notification addNotification(Notification theNotification) {
		notificationJpaRepo.save(theNotification);
		return theNotification;
	}

	@Override
	public void notificationHasBeenSeen(int notId) {
		Notification theNotification = getNotById(notId);
		theNotification.setSeen(true);

	}

	@Override
	public void allNotificationSeen(int accountId) {
		List<Notification> myUnseenNots = notificationJpaRepo.getMyUnseenNots(accountId);
		for (Notification not : myUnseenNots) {
			not.setSeen(true);
			notificationJpaRepo.save(not);
		}

	}

	@Override
	public List<Notification> loadMore(int accountId, Integer notId) {
		Session currentSession = entityManager.unwrap(Session.class);
		List<Notification> theRestNots = new ArrayList<Notification>();
		Query<Notification> theQuery = currentSession.createQuery("from Notification where not_receiver = " + accountId
				+ " and id_notification < " + notId + " order by date desc ", Notification.class).setMaxResults(5);
		theRestNots = theQuery.getResultList();

		return theRestNots;
	}

	@Override
	public NotificationDetails getNotDetails(int accountId) {
		List<Notification> myNotifications = notificationJpaRepo.getMyNotifications(accountId);
		Integer myNotsNumber = myNotifications.size();
		Integer unseenNots = 0;
		for (Notification notification : myNotifications) {
			if (!notification.isSeen()) {
				unseenNots++;
			}
		}
		NotificationDetails notificationDetails = new NotificationDetails(unseenNots, myNotsNumber);
		return notificationDetails;
	}

	@Override
	public Notification getNotById(int notId) {
		Optional<Notification> result = notificationJpaRepo.findById(notId);
		Notification theNot = null;
		if (result.isPresent()) {
			theNot = result.get();
		} else {
			throw new CustomeException("no such id for a notification");
		}
		return theNot;
	}

}
