package com.luv2code.springboot.cruddemo.service;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.jpa.CommentLikeJpaRepo;

@Service
public class CommentLikesServiceImpl implements CommentLikesService {

	@Autowired
	private CommentService commentService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PostService postService;

	@Autowired
	private CommentLikeJpaRepo commentLikeJpaRepo;

	public CommentLikesServiceImpl() {

	}

	@Override
	public CommentLike addCommentLike(int accountId, int commentId, boolean userCommentLike) {
		Comment theCommentToLike = commentService.getCommentById(commentId);
		if (userCommentLike) {
			CommentLike commentLike = commentLikeJpaRepo.addUserLike(accountId, commentId);
			if (commentLike != null) {
				commentLikeJpaRepo.delete(commentLike);
				return null;
			} else {
				CommentLike commentLikeToAdd = new CommentLike(commentId, accountId, null);
				commentLikeJpaRepo.save(commentLikeToAdd);
				if (theCommentToLike.getCommentCreatorId() != null) {	
					Post post = postService.findPostByPostId(theCommentToLike.getRelatedPostId());
					notificationService.addNotification(notificationService.createNot(accountId, post, "liked your comment"));
				}
				return commentLikeToAdd;
			}
		} else {
			Post theRelatedPost = postService.findPostByPostId(theCommentToLike.getRelatedPostId());
			CommentLike commentLike = commentLikeJpaRepo.addPageLike(theRelatedPost.getPageCreatorId(), commentId);
			if (commentLike != null) {
				commentLikeJpaRepo.delete(commentLike);
				return null;
			} else {
				CommentLike commentLikeToAdd = new CommentLike(commentId, null, theRelatedPost.getPageCreatorId());
				commentLikeJpaRepo.save(commentLikeToAdd);
				return commentLikeToAdd;

			}
		}

	}

}
