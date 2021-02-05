package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Relationship;


@Repository
public interface RelationshipJpa extends JpaRepository<Relationship, Integer>  {
	
	@Query("from Relationship as r where r.userOneId=:accountId and r.status = 1 or r.userTwoId=:accountId and status = 1")
	public List<Relationship> getFriends(@Param(value="accountId")int accountId);

}
