package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.luv2code.springboot.cruddemo.entity.Post;

public interface PostService {

	public List<Post> findPostByAccountId(int accountId , int loggedInUserId);
	
	public Post findPostByPostId(int theId);
	
	public Post savePost(int accountId , String text , MultipartFile image , String postOption) throws Exception;
	
	public Post postOnWall(int accountId, int postedOnUserId , MultipartFile image , String text) throws Exception;
	
	public Post resharePost (int accountId , int idPost, String extraText, String postOption);
	
	public Post updatePost(int accountId , int postId, MultipartFile image, boolean postWithImage, String newText) throws Exception;
	
	public void deletePostById (int accountId , int postId);

	public List<Post> getHomePagePosts(int accountId);
	
	
	public List<String> getAccountPhotos(int accountId);
	
	

}
