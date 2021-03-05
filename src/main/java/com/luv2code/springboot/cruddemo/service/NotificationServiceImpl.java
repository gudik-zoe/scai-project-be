package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.NotRepoPaging;
import com.luv2code.springboot.cruddemo.jpa.NotificationJpaRepo;
import com.luv2code.springboot.cruddemo.dto.NotificationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
	@Autowired
	private NotificationJpaRepo notificationJpaRepo;

	@Autowired
	private NotRepoPaging notRepoPaging;

	public NotificationServiceImpl() {

	}

	@Override
	public List<Notification> getMyNotification(int accountId) {
		Pageable first5 = PageRequest.of(0, 5);
		Page<Notification> nots = notRepoPaging.findFirst5(first5, accountId);
		List<Notification> myfirst = nots.getContent();
		return myfirst;
	}

	@Override
	public List<Notification> loadMore(int accountId, Integer notId) {

		Pageable next = PageRequest.of(0, 5);
		Page<Notification> nots = notRepoPaging.loadMore(next, accountId, notId);
		List<Notification> myNext = nots.getContent();
		return myNext;
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
		notificationJpaRepo.save(theNotification);

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
			throw new NotFoundException("no such id for a notification");
		}
		return theNot;
	}

	@Override
	public Notification createNot(int accountId, Post post, String action) {
		return new Notification(accountId, post.getPostCreatorId() == accountId ? post.getPostedOn() : post.getPostCreatorId(), action, new Date(System.currentTimeMillis()),
				post.getIdPost(), false);
	}

}
