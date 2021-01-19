package com.luv2code.springboot.cruddemo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.springboot.cruddemo.service.NotificationService;
import com.luv2code.utility.AccountBasicData;

@Repository
public class CommentLikesDAOHibernateImpl implements CommentLikesDAO {

	private EntityManager entityManager;

	private AccountService accountService;

	private NotificationService notificationService;

	@Autowired
	public CommentLikesDAOHibernateImpl(EntityManager theEntityManager, AccountService theAccountService,
			NotificationService theNotificationService) {
		entityManager = theEntityManager;
		accountService = theAccountService;
		notificationService = theNotificationService;
	}

	@Override
	public int getCommentLikesById(int commentId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<CommentLike> theQuery = currentSession
				.createQuery("from CommentLike where related_comment_id=" + commentId, CommentLike.class);
		List<CommentLike> list = theQuery.getResultList();

		return list.size();
	}

	@Override
	public CommentLike addLike(int accountId, int commentId) {
		Session currentSession = entityManager.unwrap(Session.class);

		Query<CommentLike> theQuery = currentSession.createQuery("from CommentLike where related_comment_id="
				+ commentId + " and comment_like_creator_id = " + accountId + " or page_creator_id != " + null,
				CommentLike.class);
		try {
			CommentLike thecommentLike = theQuery.getSingleResult();
			currentSession.delete(thecommentLike);
			return null;
		} catch (Exception e) {
			CommentLike theNewLike = new CommentLike();
			Comment theLikedComment = currentSession.get(Comment.class, commentId);
			if (theLikedComment.getPageCreatorId() != null) {
				CommentLike theLike = new CommentLike(commentId, accountId, null);
				currentSession.save(theLike);
				return theLike;
			} else {

				Post relatedPost = currentSession.get(Post.class, theLikedComment.getRelatedPostId());
				if (theLikedComment.getCommentCreatorId() == accountId) {
					throw new CustomeException("cannot like ur own comment");
				} else {
					theNewLike.setCommentLikeCreatorId(accountId);
					theNewLike.setRelatedCommentId(commentId);
					currentSession.save(theNewLike);
					Notification theNotification = new Notification(accountId, theLikedComment.getCommentCreatorId(),
							"liked your comment", new Date(System.currentTimeMillis()), relatedPost.getIdPost(), false);
					notificationService.addNotification(theNotification);
					return theNewLike;
				}

			}
		}

	}

	@Override
	public List<AccountBasicData> getCommentLikers(int commentId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<CommentLike> theQuery = currentSession
				.createQuery("from CommentLike where related_comment_id=" + commentId, CommentLike.class);
		List<CommentLike> theList = theQuery.getResultList();
		List<AccountBasicData> likers = new ArrayList<>();
		for (CommentLike liker : theList) {
			likers.add(accountService.getAccountBasicData(liker.getCommentLikeCreatorId()));
		}

		return likers;
	}

}
