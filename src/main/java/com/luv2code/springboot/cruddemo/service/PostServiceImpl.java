package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.exceptions.BadRequestException;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.PostJpaRepo;
import com.luv2code.springboot.cruddemo.dto.AccountBasicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostJpaRepo postRepoJpa;

	@Autowired
	private AccountService accountService;

	@Autowired
	private PageService pageService;

	@Autowired
	private RelationshipService relationshipService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private NotificationService notificationService;

	public PostServiceImpl() {

	}

	@Override
	public List<Post> findPostByAccountId(int accountId, int loggedInUserId) {
		List<Post> posts = new ArrayList<Post>();
		if (accountId == loggedInUserId) {
			posts = postRepoJpa.getMyPostsAndPostedOnMyWAll(accountId);
			return posts;
		} else {
			Integer relationshipStatus = relationshipService.getStatus(accountId, loggedInUserId);
			if (relationshipStatus == null || relationshipStatus != 1) {
				posts = postRepoJpa.getPostsOfNonFriend(accountId);
			} else {
				posts = postRepoJpa.getPostsOfAFriend(accountId);
			}
		}
		return posts;
	}

	@Override
	public Post savePost(int accountId, String text, MultipartFile image, String postOption) throws Exception {
		Post thePost = new Post();
		String theImage = null;
		if (image != null) {
			theImage = storageService.pushImage(image).getImageUrl();
		}
		thePost.setImage(theImage);
		if (postOption.equals("public")) {
			thePost.setStatus(0);
		} else if (postOption.equals("just-friends")) {
			thePost.setStatus(1);
		} else {
			thePost.setStatus(2);
		}
		thePost.setDate(new Date(System.currentTimeMillis()));
		thePost.setText(text);
		thePost.setPostCreatorId(accountId);
		postRepoJpa.save(thePost);

		return thePost;

	}

	@Override
	public void deletePostById(int accountId, int postId) {
		Post post = findPostByPostId(postId);
		if (post.getPostCreatorId() != null && post.getPostCreatorId() != accountId) {
			throw new BadRequestException("post is not yours");
		} else {
			postRepoJpa.deleteById(postId);
		}
	}

	@Override
	public List<Post> getHomePagePosts(int accountId) {
		List<Post> totalPosts = new ArrayList<Post>();
		List<Integer> likedPagesIds = pageService.getMyLikedPages(accountId);
		List<AccountBasicData> myFriends = accountService.getMyFriends(accountId);
		List<Integer> myFriendsIds = new ArrayList<Integer>();
		if (myFriends.size() > 0) {
			for (AccountBasicData friend : myFriends) {
				myFriendsIds.add(friend.getIdAccount());
			}
		}
		totalPosts.addAll(postRepoJpa.getMyPosts(accountId));
		if (myFriendsIds.size() > 0) {

			for (Integer id : myFriendsIds) {
				totalPosts.addAll(postRepoJpa.getFriendsPosts(id));
			}
		}
		if (likedPagesIds.size() > 0) {
			for (Integer pageId : likedPagesIds) {
				totalPosts.addAll(postRepoJpa.getPagesPosts(pageId));
			}
		}
		return totalPosts;
	}

	@Override
	public Post findPostByPostId(int theId) {
		Optional<Post> result = postRepoJpa.findById(theId);
		Post thePost = null;
		if (result.isPresent()) {
			thePost = result.get();
		} else {
			throw new NotFoundException("no such id for a post");
		}
		return thePost;
	}

	@Override
	public Post updatePost(int accountId, int postId, MultipartFile image, boolean postWithImage, String newText)
			throws Exception {
		Post OriginalPost = findPostByPostId(postId);
		if (!newText.equals(OriginalPost.getText())) {
			OriginalPost.setText(newText);
		}
		if (postWithImage && image != null) {
			OriginalPost.setImage(storageService.pushImage(image).getImageUrl());
		} else if (!postWithImage) {
			OriginalPost.setImage(null);
		}
		postRepoJpa.save(OriginalPost);
		return OriginalPost;

	}

	@Override
	public Post resharePost(int accountId, int idPost, String extraText, String postOption) {
		Post thePost = new Post();
		Post OriginalPost = findPostByPostId(idPost);
		if (OriginalPost.getPostCreatorId() != null
				&& relationshipService.getStatus(accountId, OriginalPost.getPostCreatorId()) != 1) {
			throw new NotFoundException("cannot share a post to a user that is not ur friend");
		} else if (OriginalPost.getPageCreatorId() != null
				&& !pageService.checkIfPageLikedByAccount(accountId, OriginalPost.getPageCreatorId())) {
			throw new NotFoundException("cannot share a page's post that u didn't like");
		} else {
			thePost.setExtraText(extraText);
			if (postOption.equals("public")) {
				thePost.setStatus(0);
			} else if (postOption.equals("just-friends")) {
				thePost.setStatus(1);
			} else {
				thePost.setStatus(2);
			}
			thePost.setPostOriginalId(idPost);
			thePost.setPostCreatorId(accountId);
			thePost.setDate(new Date(System.currentTimeMillis()));
			postRepoJpa.save(thePost);
			if (OriginalPost.getPostCreatorId() != null) {
				Notification sharedUrPost = new Notification(accountId, OriginalPost.getPostCreatorId(),
						"shared your post", new Date(System.currentTimeMillis()), thePost.getIdPost(), false);
				notificationService
						.addNotification(notificationService.createNot(accountId, OriginalPost, "shared your post"));
			}
		}
		return thePost;

	}

	@Override
	public Post postOnWall(int accountId, int postedOnUserId, MultipartFile image, String text) throws Exception {
		Post thePost = new Post();
		if ( relationshipService.getStatus(accountId, postedOnUserId) != 1) {
			throw new NotFoundException("cannot add a post on a user's wall that is not ur friend");
		} else {
			thePost.setPostedOn(postedOnUserId);
			thePost.setText(text);
			thePost.setDate(new Date(System.currentTimeMillis()));
			if (image != null) {
				thePost.setImage(storageService.pushImage(image).getImageUrl());
			} else {
				thePost.setImage(null);
			}
			thePost.setPostCreatorId(accountId);
			thePost.setStatus(1);
			postRepoJpa.save(thePost);
			notificationService
					.addNotification(notificationService.createNot(accountId, thePost, "posted on your wall"));
			return thePost;
		}
	}

	@Override
	public List<String> getAccountPhotos(int accountId) {
		List<Post> posts = postRepoJpa.getPhotos(accountId);
		List<String> photos = new ArrayList<String>();
		for (Post post : posts) {
			photos.add(post.getImage());
		}
		return photos;
	}

}
