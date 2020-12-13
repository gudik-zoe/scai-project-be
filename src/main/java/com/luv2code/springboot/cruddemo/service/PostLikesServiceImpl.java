package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.PostLikesDAO;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.PostLike;
@Service
public class PostLikesServiceImpl implements PostLikesService { 
	
	private PostLikesDAO postLikesDAO;
	
	@Autowired
	public PostLikesServiceImpl (PostLikesDAO thePostLikesDAO) {
		postLikesDAO = thePostLikesDAO;
	}
	

	@Override
	@Transactional
	public int getPostLikesById(int postId) {
		return postLikesDAO.getPostLikesById(postId);
	}

	@Override
	@Transactional
	public PostLike addLike(int accountId, Post post) {
		return postLikesDAO.addLike(accountId, post );
	}

	@Override
	@Transactional
	public List<Integer> getPostLikers(int postId) {
		return postLikesDAO.getPostLikers(postId);
	}

}
