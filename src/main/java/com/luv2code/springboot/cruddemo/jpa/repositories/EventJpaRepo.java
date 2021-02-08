package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Event;

@Repository
public interface EventJpaRepo extends JpaRepository<Event, Integer> {

	@Query("from Event as e where e.eventCreatorId =:accountId ")
	public List<Event> getMyEvents(@Param(value = "accountId") int accountId);

}
