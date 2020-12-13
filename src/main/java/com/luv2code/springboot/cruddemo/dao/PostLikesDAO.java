package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.PostLike;

public interface PostLikesDAO {

	public int getPostLikesById(int postId);

	public PostLike addLike(int accountId, Post post);

	public List<Integer> getPostLikers(int postId);

}
