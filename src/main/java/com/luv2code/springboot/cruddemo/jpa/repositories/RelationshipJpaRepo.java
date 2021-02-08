package com.luv2code.springboot.cruddemo.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Relationship;


@Repository
public interface RelationshipJpaRepo extends JpaRepository<Relationship, Integer>  {
	
	@Query("from Relationship as r where r.userOneId=:accountId and r.status = 1 or r.userTwoId=:accountId and status = 1")
	public List<Relationship> getFriends(@Param(value="accountId")int accountId);
	
	@Query ("from Relationship as r where r.userOneId =:user1Id and r.userTwoId=:user2Id or  r.userOneId =:user2Id and r.userTwoId=:user1Id ")
	public Relationship createRelation(@Param(value="user1Id") int user1Id, @Param (value="user2Id") int user2Id);

	@Query("from Relationship as r where r.userTwoId=:accountId and r.status = 0" )
	public List<Relationship> getFriendRequests(@Param(value="accountId")int accountId);

}
