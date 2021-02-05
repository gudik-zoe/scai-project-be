package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.entity.Post;

public interface PostsDAO {

	public List<Post> findPostByAccountId(int accountId , int loggedInUserId);

	public Post savePost(int theId, Post thePost);

	public Post findPostByPostId(int theId);

	public void deletePostById(int accountId , int postId);

	public List<Post> getHomePagePosts(int accountId);

	public Post updatePost(Post post , MultipartFile image , boolean postWithImage , String newText) throws Exception;

	public Post resharePost(int accountId, int idPost, String extraText , Integer status);

	public Post postOnWall(int accountId, Post thePost);



}
