package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Post;

public interface PostsDAO {

	public List<Post> findPostByAccountId(int accountId , int loggedInUserId);

	public Post savePost(int theId, Post thePost);

	public Post findPostByPostId(int theId);

	public void deletePostById(int theId);

	public List<Post> getHomePagePosts(int accountId);

	public Post updatePost(Post post);

	public Post resharePost(int accountId, int idPost, String extraText , boolean isPublic);

	public Post postOnWall(int accountId, Post thePost);

	public List<Post> getAllPosts();


}
