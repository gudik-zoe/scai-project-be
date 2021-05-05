package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.RelationshipJpaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelationshipServiceImpl implements RelationshipService {
	
	@Autowired
	private RelationshipJpaRepo relationshipJpa;

	public RelationshipServiceImpl() {

	}

	@Override
	public Relationship createRelation(int user1Id, int user2Id) {
		if (user1Id != user2Id) {
			Relationship theRelationship = relationshipJpa.createRelation(user1Id, user2Id);
			if (theRelationship == null) {
				theRelationship = new Relationship(user1Id, user2Id, 0);
				relationshipJpa.save(theRelationship);
			} else {
				throw new NotFoundException("there is already a relation");
			}
		}
		return null;
	}

	@Override
	public Relationship checkRelation(int user1Id, int user2Id) {
		Relationship theRelationship = relationshipJpa.createRelation(user1Id, user2Id);
		if (theRelationship != null) {
			return theRelationship;
		} else {
			return null;
		}

	}

	@Override
	public Relationship respondToFriendRequest(int accountId, int relationshipId, int status) {
		Relationship theRelationship = getRelationshipById(relationshipId);
		if (theRelationship.getUserTwoId() == accountId) {
			if (status == 2) {
				relationshipJpa.delete(theRelationship);
				return null;
			} else {
				theRelationship.setStatus(status);
				relationshipJpa.save(theRelationship);
				return theRelationship;
			}
		} else {
			throw new NotFoundException("cannot update a relation that is not related to you");
		}
	}

	@Override
	public List<Relationship> getFriendRequests(int accountId) {
		List<Relationship> myFriendRequests = relationshipJpa.getFriendRequests(accountId);

		return myFriendRequests;

	}

	@Override
	public void cancelFriendRequest(int accountId, int userTwoId) {
		Relationship theRelationship = relationshipJpa.createRelation(accountId, userTwoId);
		if (theRelationship != null) {
			relationshipJpa.delete(theRelationship);
		}
	}

	@Override
	public Integer getStatus(int user1Id, int user2Id) {
		if (user1Id == user2Id) {
			return 1;
		} else {

			Relationship theRelationship = relationshipJpa.createRelation(user1Id, user2Id);
			if (theRelationship != null) {
				return theRelationship.getStatus();
			} else {
				return null;
			}
		}
	}

	@Override
	public List<Relationship> getMyFriends(int accountId) {
		List<Relationship> theFriends = relationshipJpa.getFriends(accountId);
		return theFriends;
	}

	@Override
	public Relationship getRelationshipById(int relationId) {
		Optional<Relationship> result = relationshipJpa.findById(relationId);
		Relationship theRelationship = null;
		if (result.isPresent()) {
			theRelationship = result.get();
		} else {
			throw new NotFoundException("no such id for a relationship");
		}
		return theRelationship;
	}

}
