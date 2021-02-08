package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.EventReact;

@Repository
public interface EventReactJpaRepo extends JpaRepository<EventReact, Integer> {
	
	@Query("from EventReact as e where e.eventReactCreatorId=:accountId")
	public List<EventReact> getMyLinkedEvents(@Param(value="accountId")int accountId);
	
	
	@Query("from EventReact as e where e.relatedEventId=:idEvent and e.eventReactCreatorId=:accountId")
	public EventReact getEventReact(@Param(value="accountId")int accountId, @Param(value="idEvent") int idEvent);

}
