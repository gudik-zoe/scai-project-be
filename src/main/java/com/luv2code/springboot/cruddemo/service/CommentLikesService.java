package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.utility.AccountBasicData;



public interface CommentLikesService {
	
	public int getCommentLikesById(int commentId);
	
	public CommentLike addLikeAsUser(int accountId , int commentId);
	
	public CommentLike addLikeAsPage(int accountId, int pageId, int commentId);

	public List<AccountBasicData> getCommentLikers(int commentId);
	

}
