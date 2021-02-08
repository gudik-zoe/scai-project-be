package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.CommentLike;

public interface CommentLikesService {

	public CommentLike addCommentLike(int accountId, int commentId, boolean userCommentLike);

}
