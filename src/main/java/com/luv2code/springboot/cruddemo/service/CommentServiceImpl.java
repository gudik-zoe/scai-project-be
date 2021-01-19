package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springboot.cruddemo.dao.CommentDAO;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Post;
@Service
public class CommentServiceImpl implements CommentService {
	
	private CommentDAO commentDAO;
	
	
	@Autowired
	public CommentServiceImpl (CommentDAO theCommentDAO) {
		commentDAO = theCommentDAO;
	}

	@Override
	@Transactional
	public List<Comment> getCommentsByAccountId(int id) {
		return commentDAO.getCommentsByAccountId(id);
	}

	@Override
	@Transactional
	public List<Comment> getCommentsByPostId(int id) {
		return commentDAO.getCommentsByPostId(id);
	}

	@Override
	@Transactional
	public List<Comment> getAllComments() {
		
		return commentDAO.getAllComments();
	}

	@Override
	@Transactional
	public Comment addComment(int accountId,  String comment , Post post) {
	
		return commentDAO.addComment(accountId  , comment ,post);
	}

	@Override
	@Transactional
	public void deleteComment(int commentId , int accountId) {
		commentDAO.deleteComment(commentId , accountId);

	}

	@Override
	@Transactional
	public Comment updateComment(int commentId , String commentText , int accountId) {
		return commentDAO.updateComment(commentId , commentText , accountId);
	}

	

}
