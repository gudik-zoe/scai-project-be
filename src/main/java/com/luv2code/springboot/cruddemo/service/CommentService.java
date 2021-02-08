package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Comment;

public interface CommentService {

	public Comment addComment(int accountId, String commentText, int postId, boolean userComment);

	public Comment updateComment(int commentId, String commentText, int accountId);

	public void deleteComment(int commentId, int accountId);

	public Comment getCommentById(int commentId);

}
