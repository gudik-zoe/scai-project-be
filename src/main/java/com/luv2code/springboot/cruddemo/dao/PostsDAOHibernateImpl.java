package com.luv2code.springboot.cruddemo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Page;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.springboot.cruddemo.service.NotificationService;
import com.luv2code.springboot.cruddemo.service.PageService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.AccountBasicData;

@Repository
public class PostsDAOHibernateImpl implements PostsDAO {

	private EntityManager entityManager;

	private PageService pageService;

	private NotificationService notificationService;

	private AccountService accountservice;

	private RelationshipService relatopnshipService;

	private StorageService storageService;

	@Autowired
	public PostsDAOHibernateImpl(EntityManager theEntityManager, PageService thePageService,
			NotificationService theNotificationService, AccountService theAccountService,
			RelationshipService theRelationshipService, StorageService theStorageService) {
		entityManager = theEntityManager;
		pageService = thePageService;
		notificationService = theNotificationService;
		accountservice = theAccountService;
		relatopnshipService = theRelationshipService;
		storageService = theStorageService;

	}

	@Override
	public List<Post> getAllPosts() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession.createQuery("from Post", Post.class);
		List<Post> allPosts = theQuery.getResultList();
		return allPosts;
	}

	@Override
	public List<Post> getHomePagePosts(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		List<AccountBasicData> myFriends = accountservice.getMyFriends(accountId);
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> likedPagesIds = pageService.getMyLikedPages(accountId);
		Query<Post> theQuery = null;

		List<Post> thePosts = new ArrayList<Post>();

		if (myFriends != null && myFriends.size() > 0) {
			for (AccountBasicData friend : myFriends) {
				ids.add(friend.getIdAccount());
			}
			ids.add(accountId);

			for (Integer id : ids) {

				theQuery = currentSession.createQuery("from Post where posted_on =" + null + " and post_creator_id = "
						+ id + " and status != " + 2 + " order by date DESC", Post.class);
				thePosts.addAll(theQuery.getResultList());

			}

		} else {
			theQuery = currentSession.createQuery("from Post where posted_on = " + null + " and status !=" + 2
					+ " and post_creator_id = " + accountId + " order by date desc", Post.class);
			try {
				thePosts = theQuery.getResultList();
			} catch (Exception e) {
				return null;
			}
		}
		if (likedPagesIds.size() > 0) {
			List<Post> pagePosts = new ArrayList<Post>();
			for (Integer id : likedPagesIds) {
				theQuery = currentSession.createQuery("from Post where page_creator_id = " + id, Post.class);
				pagePosts = theQuery.getResultList();
				thePosts.addAll(pagePosts);
			}
		}

		return thePosts;
	}

	@Override
	public List<Post> findPostByAccountId(int accountId, int loggedInUserId) {
		Session currentSession = entityManager.unwrap(Session.class);
		List<Post> thePosts = new ArrayList<Post>();
		if (accountId == loggedInUserId) {
			Query<Post> theQuery = currentSession.createQuery("from Post where post_creator_id=" + accountId
					+ " or posted_on = " + loggedInUserId + " order by date DESC", Post.class);
			thePosts = theQuery.getResultList();
		} else {
			Integer relationshipStatus = relatopnshipService.getStatus(accountId, loggedInUserId);
			if (relationshipStatus == null || relationshipStatus != 1) {
				Query<Post> theQuery = currentSession.createQuery(
						"from Post where post_creator_id=" + accountId + " and status = " + 0 + " order by date DESC",
						Post.class);
				thePosts = theQuery.getResultList();
			} else if (relationshipStatus == 1) {
				Query<Post> theQuery = currentSession.createQuery("from Post where post_creator_id=" + accountId
						+ " and status != " + 2 + " or posted_on = " + accountId + "order by date DESC", Post.class);
				thePosts = theQuery.getResultList();
			}
		}
		return thePosts;
	}

	public Post findPostByPostId(int theId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post thePost = currentSession.get(Post.class, theId);
		return thePost;

	}

	@Override
	public Post savePost(int accountId, Post post) {
		Session currentSession = entityManager.unwrap(Session.class);
		post.setPostCreatorId(accountId);
		currentSession.save(post);
		return post;
	}

	@Override
	public Post postOnWall(int accountId, Post thePost) {
		Session currentSession = entityManager.unwrap(Session.class);
		thePost.setPostCreatorId(accountId);
		if (thePost.getPostedOn() != null) {
			Account theRequestedAccount = currentSession.get(Account.class, thePost.getPostedOn());
			if (theRequestedAccount != null) {
				thePost.setStatus(1);
				currentSession.save(thePost);
				Notification WroteOnYourPost = new Notification(accountId, thePost.getPostedOn(), "posted on your wall",
						new Date(System.currentTimeMillis()), thePost.getIdPost(), false);
				notificationService.addNotification(WroteOnYourPost);
			} else {
				throw new CustomeException("this user doesn't exist");
			}
		}
		return thePost;

	}

	@Override
	public Post updatePost(Post post, MultipartFile image, boolean postWithImage, String newText) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		if (image != null) {
			post.setImage(storageService.pushImage(image).getImageUrl());
		} else if (!postWithImage && image == null) {
			post.setImage(null);
		}
		if (!newText.trim().equals("")) {
			post.setText(newText);
		} else {
			throw new CustomeException("cannot add an empty text");
		}
//		Post theOldPost = currentSession.get(Post.class, updatedPost.getIdPost());
		currentSession.update(post);
		return post;
	}

	@Override
	public void deletePostById(int accountId, int postId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post thePost = currentSession.get(Post.class, postId);
		Page thePage = new Page();
		if (thePost.getPageCreatorId() != null) {
			thePage = currentSession.get(Page.class, thePost.getPageCreatorId());
		}
		if (thePost.getPostCreatorId() != null && thePost.getPostCreatorId() != accountId) {
			throw new CustomeException("you cannot delete a post that is not yours");
		} else if (thePost.getPostCreatorId() == null && accountId != thePage.getPageCreatorId()) {
			throw new CustomeException("you cannot delete a post from a page  that is not yours");
		} else {
			currentSession.delete(thePost);
		}
	}

	@Override
	public Post resharePost(int accountId, int idPost, String extraText, Integer status) {
		Session currentSession = entityManager.unwrap(Session.class);
		Post theOriginalPost = currentSession.get(Post.class, idPost);
		Post resharedPost = new Post();
		resharedPost.setPostCreatorId(accountId);
		resharedPost.setPostOriginalId(idPost);
		resharedPost.setStatus(status);
		resharedPost.setDate(new Date(System.currentTimeMillis()));
		resharedPost.setExtraText(extraText);
		currentSession.save(resharedPost);

		if (theOriginalPost.getPageCreatorId() == null) {
			Notification shareNotification = new Notification(accountId, theOriginalPost.getPostCreatorId(),
					"shared your post", new Date(System.currentTimeMillis()), idPost, false);
			notificationService.addNotification(shareNotification);
		}
		System.out.println("this is a page's post we don want notification");
		return resharedPost;

	}

}
