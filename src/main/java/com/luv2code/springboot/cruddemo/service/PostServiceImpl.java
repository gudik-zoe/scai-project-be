package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.PostsDAO;
import com.luv2code.springboot.cruddemo.entity.Post;

@Service
public class PostServiceImpl implements PostService {

	private PostsDAO postsDAO;

	public PostServiceImpl(PostsDAO thePostsDAO) {
		postsDAO = thePostsDAO;
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
	public void deletePostById(int theId) {

		postsDAO.deletePostById(theId);

	}

	@Override
	@Transactional
	public List<Post> getHomePagePosts(int accountId) {
		return postsDAO.getHomePagePosts(accountId);
	}

	@Override
	@Transactional
	public Post findPostByPostId(int theId) {

		return postsDAO.findPostByPostId(theId);
	}

	@Override
	@Transactional
	public Post updatePost(Post post) {
		return postsDAO.updatePost(post);

	}

	@Override
	@Transactional
	public Post resharePost(int accountId, int idPost, String extraText , boolean isPublic) {

		return postsDAO.resharePost(accountId, idPost,  extraText , isPublic);

	}

	@Override
	@Transactional
	public Post postOnWall(int accountId, Post thePost) {
		return postsDAO.postOnWall(accountId , thePost);
	}

	@Override
	@Transactional
	public List<Post> getAllPosts() {
		return postsDAO.getAllPosts();
	}



}
