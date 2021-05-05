package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Relationship;

import java.util.List;

public interface RelationshipService {

	public Relationship createRelation(int user1Id , int user2Id);
	
	public Relationship checkRelation(int user1Id , int user2Id);
	
	public Relationship respondToFriendRequest(int accountId , int relationshipId, int status);
	
	public List<Relationship> getFriendRequests(int accountId);
	
	public void cancelFriendRequest(int accountIdAccount , int userTwoId);
	
	public List<Relationship> getMyFriends(int accountId);
	
	public Integer getStatus(int user1Id , int user2Id);
	
	public Relationship getRelationshipById(int relationId);
	
	
}
