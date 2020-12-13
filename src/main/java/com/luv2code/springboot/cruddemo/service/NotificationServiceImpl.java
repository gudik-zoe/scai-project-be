package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.dao.NotificationsDAO;
import com.luv2code.springboot.cruddemo.entity.Notification;
@Service
public class NotificationServiceImpl implements NotificationService {
	
private NotificationsDAO notificationsDAO;
	
	@Autowired
	public NotificationServiceImpl (NotificationsDAO theNotificationsDAO) {
		notificationsDAO = theNotificationsDAO;
	}
	

	@Override
	@Transactional
	public List<Notification> getMyNotification(int accountId) {
		return notificationsDAO.getMyNotification(accountId);
	}

	@Override
	@Transactional
	public Notification addNotification(Notification theNotification) {
		 return notificationsDAO.addNotification(theNotification);
	}

	@Override
	@Transactional
	public void deleteNotification() {
		 notificationsDAO.deleteNotification();

	}


	@Override
	@Transactional
	public void notificationHasBeenSeen(int notId) {
		notificationsDAO.notificationHasBeenSeen(notId);
		
	}

}
