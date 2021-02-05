package com.luv2code.springboot.cruddemo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.dao.PostRepoJpa;
import com.luv2code.springboot.cruddemo.dao.PostsDAO;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.utility.AccountBasicData;

@Service
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostsDAO postsDAO;
	
	
	@Autowired
	private PostRepoJpa postRepoJpa;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private PageService pageService;
	
	
	public PostServiceImpl() {
		
	}

	@Override
	@Transactional
	public List<Post> findPostByAccountId(int accountId , int loggedInUserId) {
		return postsDAO.findPostByAccountId(accountId , loggedInUserId );
	}

	@Override
	@Transactional
	public Post savePost(int theId, Post thePost) {

		return postsDAO.savePost(theId, thePost);

	}

	@Override
	@Transactional
	public void deletePostById(int accountId , int postId) {
		postsDAO.deletePostById(accountId , postId);
	}

	@Override
	public List<Post> getHomePagePosts(int accountId) {
		List<Post> totalPosts = new ArrayList<Post>();
		List<Integer> likedPagesIds = pageService.getMyLikedPages(accountId);
		List<AccountBasicData> myFriends = accountService.getMyFriends(accountId);
		List<Integer> myFriendsIds = new ArrayList<Integer>();
		if(myFriends.size()>0) {
			for(AccountBasicData friend:myFriends) {
				myFriendsIds.add(friend.getIdAccount());
			}
		}
		totalPosts.addAll(postRepoJpa.getMyPosts(accountId));
		if(myFriendsIds.size() > 0) {
			
		for(Integer id:myFriendsIds) {
			totalPosts.addAll(postRepoJpa.getFriendsPosts(id));
		}
		}
		if(likedPagesIds.size() > 0) {
			for(Integer pageId:likedPagesIds) {
				totalPosts.addAll(postRepoJpa.getPagesPosts(pageId));
			}
		}
		return totalPosts;
		
		
		
		
//		Query<Post> theQuery = null;
//
//		List<Post> thePosts = new ArrayList<Post>();
//
//		if (myFriends != null && myFriends.size() > 0) {
//			for (AccountBasicData friend : myFriends) {
//				ids.add(friend.getIdAccount());
//			}
//			ids.add(accountId);
//
//			for (Integer id : ids) {
//
//				theQuery = currentSession.createQuery("from Post where posted_on =" + null + " and post_creator_id = "
//						+ id + " and status != " + 2 + " order by date DESC", Post.class);
//				thePosts.addAll(theQuery.getResultList());
//
//			}
//
//		} else {
//			theQuery = currentSession.createQuery("from Post where posted_on = " + null + " and status !=" + 2
//					+ " and post_creator_id = " + accountId + " order by date desc", Post.class);
//			try {
//				thePosts = theQuery.getResultList();
//			} catch (Exception e) {
//				return null;
//			}
//		}
//		if (likedPagesIds.size() > 0) {
//			List<Post> pagePosts = new ArrayList<Post>();
//			for (Integer id : likedPagesIds) {
//				theQuery = currentSession.createQuery("from Post where page_creator_id = " + id, Post.class);
//				pagePosts = theQuery.getResultList();
//				thePosts.addAll(pagePosts);
//			}
//		}
//
//		return thePosts;
//		return postsDAO.getHomePagePosts(accountId);
//		return null;
	}

	@Override
	@Transactional
	public Post findPostByPostId(int theId) {

		return postsDAO.findPostByPostId(theId);
	}

	@Override
	@Transactional
	public Post updatePost(Post post , MultipartFile image , boolean postWithImage , String newText) throws Exception {
		return postsDAO.updatePost(post , image , postWithImage , newText);

	}

	@Override
	@Transactional
	public Post resharePost(int accountId, int idPost, String extraText , Integer status) {

		return postsDAO.resharePost(accountId, idPost,  extraText , status);

	}

	@Override
	@Transactional
	public Post postOnWall(int accountId, Post thePost) {
		return postsDAO.postOnWall(accountId , thePost);
	}

	

	@Override
	public List<String> getAccountPhotos(int accountId) {
		List<Post> posts = postRepoJpa.getPhotos(accountId);
		List<String> photos = new ArrayList<String>();
		for(Post post:posts) {
			photos.add(post.getImage());
		}
		return photos;
	}



}
