package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Post;

public interface CommentService {
	
	public List<Comment> getCommentsByAccountId(int id);
	
	public List<Comment> getCommentsByPostId(int id);
	
	public List<Comment> getAllComments();
	
	public Comment addComment(int accountId, String comment , Post post);
	
	public Comment updateComment(int commentId , String commentText , int accountId);
	
	public void deleteComment(int commentId , int accountId);
	
	

}
