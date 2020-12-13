package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.utility.AccountBasicData;
public interface CommentLikesDAO {

	public int getCommentLikesById(int commentId);

	public CommentLike addLike(int accountId, int commentId);

	public List<AccountBasicData> getCommentLikers(int commentId);

}
