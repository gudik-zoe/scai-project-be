package com.luv2code.springboot.cruddemo.service;

import java.util.List;


import com.luv2code.springboot.cruddemo.entity.Post;

public interface PostService {

	public List<Post> findPostByAccountId(int accountId , int loggedInUserId);
	
	public Post findPostByPostId(int theId);
	
	public Post savePost(int theId , Post thePost);
	
	public Post postOnWall(int accountId , Post thePost);
	
	public Post resharePost (int accountId , int idPost , String extraText , Integer status);
	
	public Post updatePost(Post post);
	
	public void deletePostById (int theId);

	public List<Post> getHomePagePosts(int accountId);
	
	public List<Post> getAllPosts();
	
	

}
