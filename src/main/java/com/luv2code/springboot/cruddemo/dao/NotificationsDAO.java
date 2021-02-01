package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.utility.NotificationDetails;

public interface NotificationsDAO {

	public List<Notification> getMyNotification(int accountId);

	public Notification addNotification(Notification theNotification);

	public void deleteNotification();

	public void notificationHasBeenSeen(int notId);

	public void allNotificationSeen(int accountId);

	public List<Notification> loadMore(int accountId , Integer notId);

	public NotificationDetails getNotDetails(int accountId);
	

}
