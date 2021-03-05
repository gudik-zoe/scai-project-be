package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.CommentJpaRepo;
import com.luv2code.springboot.cruddemo.dto.PageBasicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentJpaRepo commentJpaRepo;

	@Autowired
	private PostService postService;

	@Autowired
	private PageService pageService;

	@Autowired
	private RelationshipService relationshipService;

	@Autowired
	private NotificationService notificationService;

	public CommentServiceImpl() {

	}

	@Override
	public Comment addComment(int accountId, String commentText, int postId, boolean userComment) {
		Post theRelatedPost = postService.findPostByPostId(postId);
		Comment theComment = new Comment();
		if (userComment) {
			if (relationshipService.getStatus(accountId, theRelatedPost.getPostCreatorId()) != 1
					|| theRelatedPost.getStatus() == 2) {
				throw new NotFoundException("cannot comment on friend's post that is not your friend");
			} else {
				theComment = new Comment(commentText, theRelatedPost.getIdPost(), new Date(System.currentTimeMillis()),
						accountId, null);
				if (theRelatedPost.getPageCreatorId() == null
						&& !theComment.getCommentCreatorId().equals(theRelatedPost.getPostCreatorId())) {
					if (theRelatedPost.getPostCreatorId() != null && theRelatedPost.getPostCreatorId() != accountId) {
						notificationService.addNotification(
								notificationService.createNot(accountId, theRelatedPost, "commented on your post"));
					}
				}
			}

		} else {
			theComment = new Comment(commentText, theRelatedPost.getIdPost(), new Date(System.currentTimeMillis()),
					null, theRelatedPost.getPageCreatorId());

		}
		commentJpaRepo.save(theComment);
		return theComment;

	}

	@Override
	public void deleteComment(int commentId, int accountId) {
		Comment theComment = getCommentById(commentId);
		if (theComment.getCommentCreatorId() != null && theComment.getCommentCreatorId() == accountId) {
			commentJpaRepo.delete(theComment);
		} else if (theComment.getPageCreatorId() != null) {
			PageBasicData thePage = pageService.getPageBasicDataById(theComment.getPageCreatorId());
			if (thePage.getPageCreatorId() == accountId) {
				commentJpaRepo.delete(theComment);
			} else {
				throw new NotFoundException("comment is not yours ");
			}
		}
	}

	@Override
	public Comment updateComment(int commentId, String commentText, int accountId) {
		Comment theComment = getCommentById(commentId);
		if (theComment.getCommentCreatorId() != null) {
			if (theComment.getCommentCreatorId() == accountId) {
				theComment.setText(commentText);
			} else {
				throw new NotFoundException("cannot update a comment that is not yours");
			}
		} else {
			PageBasicData thePage = pageService.getPageBasicDataById(theComment.getPageCreatorId());
			if (thePage.getPageCreatorId() == accountId) {
				theComment.setText(commentText);
			} else {
				throw new NotFoundException("page is not yours");
			}
		}
		commentJpaRepo.save(theComment);
		return theComment;
	}

	@Override
	public Comment getCommentById(int commentId) {
		Optional<Comment> result = commentJpaRepo.findById(commentId);
		Comment theComment = null;
		if (result.isPresent()) {
			theComment = result.get();
		} else {
			throw new NotFoundException("no such id for a comment");
		}
		return theComment;
	}

}
