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
//
//import org.springframework.stereotype.Repository;
//
//import com.luv2code.exception.error.handling.CustomeException;
//
//import com.luv2code.springboot.cruddemo.entity.Notification;
//import com.luv2code.springboot.cruddemo.entity.Post;
//import com.luv2code.springboot.cruddemo.entity.PostLike;
//import com.luv2code.springboot.cruddemo.service.NotificationService;
//import com.luv2code.springboot.cruddemo.service.PageService;
//
//@Repository
//public class PostLikesDAOHibernateImpl implements PostLikesDAO {
//
//	private EntityManager entityManager;
//
//	@Autowired
//	private NotificationService notificationService;
//
//	@Autowired
//	private PageService pageService;
//
//	@Autowired
//	public PostLikesDAOHibernateImpl(EntityManager theEntityManager, PageService thePageService) {
//		entityManager = theEntityManager;
//		pageService = thePageService;
//
//	}
//
//	@Override
//	public int getPostLikesById(int postId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<PostLike> theQuery = currentSession.createQuery("from PostLike where related_post_id=" + postId,
//				PostLike.class);
//		List<PostLike> theList = theQuery.getResultList();
//		return theList.size();
//	}
//
//	@Override
//	public PostLike addLike(int accountId, Post post) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<PostLike> theQuery = currentSession.createQuery(
//				"from PostLike where related_post_id=" + post.getIdPost() + "and post_like_creator_id = " + accountId,
//				PostLike.class);
//
//		PostLike theNewLike = new PostLike();
//
//		try {
//			PostLike theLike = theQuery.getSingleResult();
//			currentSession.delete(theLike);
//			return null;
//		} catch (Exception e) {
//			if (post.getPageCreatorId() != null) {
//				if (pageService.checkIfPageLikedByAccount(accountId, post.getPageCreatorId())) {
//					theNewLike.setPostLikeCreatorId(accountId);
//					theNewLike.setRelatedPostId(post.getIdPost());
//					currentSession.save(theNewLike);
//				} else {
//					throw new CustomeException("cannot like a page's post that u didn't follow");
//				}
//			}
//			theNewLike.setPostLikeCreatorId(accountId);
//			theNewLike.setRelatedPostId(post.getIdPost());
//			currentSession.save(theNewLike);
//			if (post.getPageCreatorId() == null && accountId != post.getPostCreatorId()) {
//				Notification likeNotification = new Notification(accountId, post.getPostCreatorId(), "liked your post",
//						new Date(System.currentTimeMillis()), post.getIdPost(), false);
//				notificationService.addNotification(likeNotification);
//			}
//		}
//		return theNewLike;
//
//	}
//
//	@Override
//	public List<Integer> getPostLikers(int postId) {
//		Session currentSession = entityManager.unwrap(Session.class);
//		Query<PostLike> theQuery = currentSession.createQuery("from PostLike where related_post_id=" + postId,
//				PostLike.class);
//		List<PostLike> theList = theQuery.getResultList();
//
//		List<Integer> likers = new ArrayList<Integer>();
//		for (PostLike l : theList) {
//			likers.add(l.getPostLikeCreatorId());
//		}
//
//		return likers;
//	}
//
//}
