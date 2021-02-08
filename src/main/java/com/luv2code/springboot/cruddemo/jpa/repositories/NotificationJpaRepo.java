package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Notification;

@Repository
public interface NotificationJpaRepo extends JpaRepository<Notification, Integer> {

	@Query("from Notification as n where n.notReceiver=:accountId order by n.date desc")
	public List<Notification> getMyNotifications(@Param(value="accountId")int accountId);

	
	@Query("from Notification as n where n.notReceiver=:accountId and n.seen = false ")
	public List<Notification> getMyUnseenNots(@Param(value="accountId")int accountId);

	

}
