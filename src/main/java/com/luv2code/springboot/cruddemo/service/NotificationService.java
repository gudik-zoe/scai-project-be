package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.dto.NotificationDetails;

public interface NotificationService {

	public List<Notification> getMyNotification(int accountId);

	public Notification addNotification(Notification theNotification);

	public void notificationHasBeenSeen(int notId);

	public void allNotificationSeen(int accountId);

	public List<Notification> loadMore(int accountId, Integer notId);

	public NotificationDetails getNotDetails(int accountId);
	
	public Notification getNotById(int notId);
	
	public Notification createNot(int accountId , Post post , String action );

}
