package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.RelationshipDAO;
import com.luv2code.springboot.cruddemo.entity.Relationship;

@Service
public class RelationshipServiceImpl implements RelationshipService {

	private RelationshipDAO relationshipDAO;

	public RelationshipServiceImpl(RelationshipDAO theRelationshipDAO) {
		relationshipDAO = theRelationshipDAO;
	}

	@Override
	@Transactional
	public Relationship createRelation(int user1Id, int user2Id) {
		return relationshipDAO.createRelation(user1Id, user2Id);
	}

	@Override
	@Transactional
	public Relationship checkRelation(int user1Id, int user2Id) {
		return relationshipDAO.checkRelation(user1Id, user2Id);
	}

	@Override
	@Transactional
	public Relationship respondToFriendRequest(int relationshipId, int status) {
		return relationshipDAO.respondToFriendRequest(relationshipId, status);
	}

	@Override
	@Transactional
	public List<Relationship> getFriendRequests(int accountId) {
		return relationshipDAO.getFriendRequests(accountId);
	}

	@Override
	@Transactional
	public void cancelFriendRequest(int accountIdAccount, int userTwoId) {
		relationshipDAO.cancelFriendRequest(accountIdAccount, userTwoId);

	}

	@Override
	@Transactional
	public List<Relationship> getMyFriends(int accountId) {

		return relationshipDAO.getMyFriends(accountId);
	}

	@Override
	public Relationship getStatus(int user1Id, int user2Id) {
		return relationshipDAO.getStatus(user1Id ,user2Id );
	}

	

}
