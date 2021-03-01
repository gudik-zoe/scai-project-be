package com.luv2code.springboot.cruddemo.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Notification;

@Repository
public interface NotRepoPaging extends PagingAndSortingRepository<Notification, Integer> {
	@Query("from Notification as n where n.notReceiver=:accountId order by n.date desc")
	Page<Notification> findFirst5(Pageable pageable, @Param(value = "accountId") int accountId);

	@Query("from Notification as n where n.notReceiver=:accountId and n.idNotification < :notId order by date desc ")
	Page<Notification> loadMore(Pageable next, @Param(value = "accountId") int accountId,
			@Param(value = "notId") Integer notId);
}
