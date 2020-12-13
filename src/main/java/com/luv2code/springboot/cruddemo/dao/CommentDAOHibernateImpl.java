package com.luv2code.springboot.cruddemo.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.NotificationService;

@Repository
public class CommentDAOHibernateImpl implements CommentDAO {

	private EntityManager entityManager;
	@Autowired
	private NotificationService notificationService;

	@Autowired
	public CommentDAOHibernateImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;

	}

	@Override
	public List<Comment> getCommentsByAccountId(int id) {
		Session currentSession = entityManager.unwrap(Session.class);

		Query<Comment> theQuery = currentSession.createQuery("from Comment where comment_creator_id=" + id,
				Comment.class);
		List<Comment> theComments = theQuery.getResultList();
		return theComments;
	}

	@Override
	public List<Comment> getCommentsByPostId(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Comment> theQuery = currentSession.createQuery("from Comment where related_post_id=" + id, Comment.class);
		List<Comment> theComments = theQuery.getResultList();
		return theComments;
	}

	@Override
	public List<Comment> getAllComments() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Comment> theQuery = currentSession.createQuery("from Comment", Comment.class);
		List<Comment> theComments = theQuery.getResultList();
		return theComments;
	}

	@Override
	public Comment addComment(int accountId, String commentText, Post post) {
		Session currentSession = entityManager.unwrap(Session.class);

		Comment theComment = new Comment(commentText, post.getIdPost(), accountId , new Date(System.currentTimeMillis()));
		currentSession.save(theComment);
		if (theComment.getCommentCreatorId() != post.getPostCreatorId()) {
			Notification addCommentNot = new Notification(accountId, post.getPostCreatorId(), "commented on your post",  new Date(System.currentTimeMillis()),
					post.getIdPost(), false);
			notificationService.addNotification(addCommentNot);
		}
		return theComment;
	}

	@Override
	public void deleteComment(int theId) {
		Session currentSession = entityManager.unwrap(Session.class);

		Comment theComment = currentSession.get(Comment.class, theId);

		currentSession.delete(theComment);

	}

	@Override
	public Comment updateComment(int commentId, String commentText) {
		Session currentSession = entityManager.unwrap(Session.class);
		Comment theComment = currentSession.get(Comment.class, commentId);
		theComment.setText(commentText);
		currentSession.update(theComment);
		return theComment;
	}

}
