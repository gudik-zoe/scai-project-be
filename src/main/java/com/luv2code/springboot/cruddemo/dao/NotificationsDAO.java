package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Notification;

public interface NotificationsDAO {

	public List<Notification> getMyNotification(int accountId);

	public Notification addNotification(Notification theNotification);

	public void deleteNotification();

	public void notificationHasBeenSeen(int notId);
	

}
