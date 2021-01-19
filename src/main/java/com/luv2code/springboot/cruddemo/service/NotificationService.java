package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Notification;

public interface NotificationService {
	
	public List<Notification> getMyNotification(int accountId);
	
	public Notification addNotification(Notification theNotification);
	
	public void deleteNotification();
	
	public void notificationHasBeenSeen(int notId);
	
	public void allNotificationSeen(int accountId);
	
	public List<Notification> loadMore(int accountId , Integer notId);

}
