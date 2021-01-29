package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Post;

public interface CommentDAO {

	public List<Comment> getCommentsByAccountId(int id);

	public List<Comment> getCommentsByPostId(int id);

	public List<Comment> getAllComments();

	public Comment addComment(int accountId,   String comment ,Post post , boolean userComment);

	public void deleteComment(int commentId , int accountId);

	public Comment updateComment(int commentId , String CommentText , int accountId);

}
