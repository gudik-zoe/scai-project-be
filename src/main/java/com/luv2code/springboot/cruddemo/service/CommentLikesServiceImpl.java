package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.CommentLikesDAO;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.utility.AccountBasicData;


@Service
public class CommentLikesServiceImpl implements CommentLikesService {
	
		private CommentLikesDAO commentLikesDAO;
	
		@Autowired
		public CommentLikesServiceImpl (CommentLikesDAO theCommentLikesDAO) {
			commentLikesDAO = theCommentLikesDAO;
		}
		
		
	@Override
	@Transactional
	public int getCommentLikesById(int commentId) {
		return commentLikesDAO.getCommentLikesById(commentId);
	}
	

	@Override
	@Transactional
	public CommentLike addLike(int accountId, int commentId) {
		return commentLikesDAO.addLike(accountId , commentId);

	}


	@Override
	public List<AccountBasicData> getCommentLikers(int commentId) {
		return commentLikesDAO.getCommentLikers(commentId);
	}


}
