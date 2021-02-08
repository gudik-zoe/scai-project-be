//package com.luv2code.springboot.cruddemo.dao;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//
//import org.hibernate.Session;
//import org.hibernate.query.Query;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import com.luv2code.exception.error.handling.CustomeException;
//import com.luv2code.springboot.cruddemo.entity.Comment;
//import com.luv2code.springboot.cruddemo.entity.CommentLike;
//import com.luv2code.springboot.cruddemo.entity.Notification;
//import com.luv2code.springboot.cruddemo.entity.Page;
//import com.luv2code.springboot.cruddemo.service.AccountService;
//import com.luv2code.springboot.cruddemo.service.NotificationService;
//import com.luv2code.springboot.cruddemo.service.PageService;
//import com.luv2code.utility.AccountBasicData;
//
//@Repository
//public class CommentLikesDAOHibernateImpl implements CommentLikesDAO {
//
//	private EntityManager entityManager;
//
//	private AccountService accountService;
//
//	private NotificationService notificationService;
//
//	private PageService pageService;
//
//	@Autowired
//	public CommentLikesDAOHibernateImpl(EntityManager theEntityManager, AccountService theAccountService,
//			NotificationService theNotificationService, PageService thePageService) {
//		entityManager = theEntityManager;
//		accountService = theAccountService;
//		notificationService = theNotificationService;
//		pageService = thePageService;
//	}
//
//	@Override
//	public int getCommentLikesById(int commentId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<CommentLike> theQuery = currentSession
//				.createQuery("from CommentLike where related_comment_id=" + commentId, CommentLike.class);
//		List<CommentLike> list = theQuery.getResultList();
//
//		return list.size();
//	}
//
//	@Override
//	public CommentLike addLikeAsUser(int accountId, int commentId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Comment theCommentToLike = currentSession.get(Comment.class, commentId);
//		if (theCommentToLike.getCommentCreatorId() != null && theCommentToLike.getCommentCreatorId() == accountId) {
//			throw new CustomeException("cannot like ur own comment");
//		} else if (theCommentToLike.getPageCreatorId() != null
//				&& !pageService.checkIfPageLikedByAccount(accountId, theCommentToLike.getPageCreatorId())) {
//			throw new CustomeException("cannot like a page's comment that you are not following");
//		}
//		Query<CommentLike> theQuery = currentSession.createQuery("from CommentLike where related_comment_id = "
//				+ commentId + " and comment_like_creator_id = " + accountId, CommentLike.class);
//
//		try {
//			CommentLike thecommentLike = theQuery.getSingleResult();
//			currentSession.delete(thecommentLike);
//			return null;
//		} catch (Exception e) {
//			CommentLike theNewLike = new CommentLike(commentId, accountId, null);
//			currentSession.save(theNewLike);
//			if (theCommentToLike.getPageCreatorId() == null) {
//				Notification theNotification = new Notification(accountId, theCommentToLike.getCommentCreatorId(),
//						"liked your comment", new Date(System.currentTimeMillis()), theCommentToLike.getRelatedPostId(),
//						false);
//				notificationService.addNotification(theNotification);
//			}
//			return theNewLike;
//
//		}
//
//	}
//
//	@Override
//	public List<AccountBasicData> getCommentLikers(int commentId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<CommentLike> theQuery = currentSession
//				.createQuery("from CommentLike where related_comment_id=" + commentId, CommentLike.class);
//		List<CommentLike> theList = theQuery.getResultList();
//		List<AccountBasicData> likers = new ArrayList<>();
//		for (CommentLike liker : theList) {
//			likers.add(accountService.getAccountBasicData(liker.getCommentLikeCreatorId()));
//		}
//
//		return likers;
//	}
//
//	@Override
//	public CommentLike addLikeAsPage(int accountId, int pageId, int commentId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Page thePage = currentSession.get(Page.class, pageId);
//		if (accountId == thePage.getPageCreatorId()) {
//			Query<CommentLike> theQuery = currentSession.createQuery(
//					"from CommentLike where page_creator_id=" + pageId + "and related_comment_id = " + commentId,
//					CommentLike.class);
//			try {
//				CommentLike theLikedComment = theQuery.getSingleResult();
//				currentSession.delete(theLikedComment);
//				return null;
//			} catch (Exception e) {
//				CommentLike theCommentLike = new CommentLike(commentId, null, pageId);
//				currentSession.save(theCommentLike);
//				return theCommentLike;
//			}
//		} else {
//			throw new CustomeException("page is not yours");
//		}
//	}
//
//}
