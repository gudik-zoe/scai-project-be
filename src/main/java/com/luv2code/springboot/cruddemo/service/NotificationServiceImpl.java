package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.dao.NotificationsDAO;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.utility.NotificationDetails;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
	
private NotificationsDAO notificationsDAO;
	
	@Autowired
	public NotificationServiceImpl (NotificationsDAO theNotificationsDAO) {
		notificationsDAO = theNotificationsDAO;
	}
	

	@Override
	public List<Notification> getMyNotification(int accountId) {
		return notificationsDAO.getMyNotification(accountId);
	}

	@Override
	public Notification addNotification(Notification theNotification) {
		 return notificationsDAO.addNotification(theNotification);
	}

	@Override
	public void deleteNotification() {
		 notificationsDAO.deleteNotification();

	}


	@Override
	public void notificationHasBeenSeen(int notId) {
		notificationsDAO.notificationHasBeenSeen(notId);
		
	}


	@Override
	public void allNotificationSeen(int accountId) {
		notificationsDAO.allNotificationSeen(accountId);
		
	}


	@Override
	public List<Notification> loadMore(int accountId , Integer notId) {
		return notificationsDAO.loadMore(accountId , notId);
	}


	@Override
	public NotificationDetails getNotDetails(int accountId) {
		return notificationsDAO.getNotDetails(accountId);
	}

}
