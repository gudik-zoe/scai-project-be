package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;

import com.luv2code.exception.error.handling.CustomeException;

import com.luv2code.springboot.cruddemo.entity.Relationship;

@Repository
public class RelationshipDAOHibernateImpl implements RelationshipDAO {

	private EntityManager entityManager;

	@Autowired
	public RelationshipDAOHibernateImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;

	}

	@Override
	public Relationship createRelation(int user1Id, int user2Id) {
		Session currentSession = entityManager.unwrap(Session.class);

		Query<Relationship> theQuery = currentSession
				.createQuery("from Relationship where (user_one_id=" + user1Id + "and user_two_id=" + user2Id + ")or ("
						+ "user_one_id= " + user2Id + "and user_two_id=" + user1Id + ")", Relationship.class);

		List<Relationship> theList = theQuery.getResultList();
		if (theList.size() != 0) {
			throw new CustomeException("there is already a relation between those users");
		} else if (user1Id == user2Id) {
			throw new CustomeException("i know you'r feeling lonely but don't add urself add others");
		} else {
			Relationship theRelation = new Relationship();
			theRelation.setUserOneId(user1Id);
			theRelation.setUserTwoId(user2Id);
			theRelation.setStatus(0);
			currentSession.save(theRelation);
			return theRelation;
		}
	}

	@Override
	public Integer getStatus(int user1Id, int user2Id) {
		Session currentSession = entityManager.unwrap(Session.class);
		if (user1Id == user2Id) {
			Relationship selfRelationship = new Relationship();
			selfRelationship.setStatus(1);
			return selfRelationship.getStatus();
		}
		Query<Relationship> theQuery = currentSession
				.createQuery("from Relationship where (user_one_id=" + user1Id + "and user_two_id=" + user2Id + " )or ("
						+ "user_one_id= " + user2Id + "and user_two_id=" + user1Id + ")", Relationship.class);
		try {
			Relationship theRelation = theQuery.getSingleResult();
			return theRelation.getStatus();
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Relationship checkRelation(int user1Id, int user2Id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Relationship> theQuery = currentSession
				.createQuery("from Relationship where (user_one_id=" + user1Id + "and user_two_id=" + user2Id + " )or ("
						+ "user_one_id= " + user2Id + "and user_two_id=" + user1Id + ")", Relationship.class);
		try {
			Relationship theRelation = theQuery.getSingleResult();
			return theRelation;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Relationship respondToFriendRequest(int relationshipId, int status) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Relationship> theQuery = currentSession
				.createQuery("from Relationship where id_relationship=" + relationshipId, Relationship.class);

		Relationship theRelation = theQuery.getSingleResult();

		theRelation.setStatus(status);
		if (status == 2) {
			currentSession.delete(theRelation);
		} else {
			currentSession.update(theRelation);
		}
		return theRelation;
	}

	@Override
	public List<Relationship> getFriendRequests(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Relationship> theQuery = currentSession.createQuery(
				"from Relationship where user_two_id=" + accountId + "and status =" + 0, Relationship.class);
		List<Relationship> theRelations = theQuery.getResultList();

		return theRelations;
	}

	@Override
	public void cancelFriendRequest(int accountId, int userTwoId) {
		Session currentSession = entityManager.unwrap(Session.class);
		@SuppressWarnings("unchecked")
		Query<Relationship> theQuery = currentSession
				.createQuery(
						"from Relationship where (user_one_id=" + accountId + "and user_two_id=" + userTwoId + " )or ("
								+ "user_one_id= " + userTwoId + "and user_two_id=" + accountId + ")",
						Relationship.class);
		Relationship theRelation;
		try {
			theRelation = theQuery.getSingleResult();
			currentSession.delete(theRelation);
		} catch (Exception e) {
			throw new CustomeException("this relation doesn't exist");
		}

	}

	@Override
	public List<Relationship> getMyFriends(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Relationship> theQuery = currentSession.createQuery("from Relationship where user_one_id =" + accountId
				+ "and  status=" + 1 + "or user_two_id=" + accountId + "and status =" + 1, Relationship.class);
		List<Relationship> friends = theQuery.getResultList();
		return friends;
	}

}
