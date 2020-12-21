package com.luv2code.springboot.cruddemo.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.service.CommentService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentRestController {

	private CommentService commentService;

	private EntityManager entityManager;

	private RelationshipService relationshipService;

	@Autowired
	public CommentRestController(CommentService theCommentService, EntityManager theEntityManager,
			RelationshipService theRelationshipService) {
		commentService = theCommentService;
		entityManager = theEntityManager;
		relationshipService = theRelationshipService;
	}

	@GetMapping("/comments")
	public List<Comment> findComments() {
		return commentService.getAllComments();
	}

	@GetMapping("/comments/postId/{idPosts}")
	public List<Comment> getCommentsByPostId(@PathVariable int idPosts) {

		return commentService.getCommentsByPostId(idPosts);
	}

	@PostMapping("comments/idAccount/{postId}")
	public Comment addComment(@RequestHeader("Authorization") String authHeader, @RequestBody String commentText,
			@PathVariable int postId) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Post theRequestedPost = currentSession.get(Post.class, postId);
		if (theRequestedPost == null) {
			throw new CustomeException("this post doesn't exist");
		} else {
			Integer theRelationshipStatus = relationshipService.getStatus(idExtractor.getIdFromToken(),
					theRequestedPost.getPostCreatorId());
			
			if (theRelationshipStatus == null || theRelationshipStatus != 1) {

				throw new CustomeException("cannot comment on a user's post that is not ur friend");

			} else if (commentText.equals("") || commentText.isEmpty() || theRequestedPost == null) {
				throw new CustomeException("cannot add an empty comment");
			}else if (theRequestedPost.getStatus() == 2 && theRequestedPost.getPostCreatorId() != idExtractor.getIdFromToken()) {
				throw new CustomeException("cannot comment to a post that is private and not yours");
			}
			else {
				return commentService.addComment(idExtractor.getIdFromToken(), commentText, theRequestedPost);
			}
		}
	}

	@PutMapping("comments/idAccount/{commentId}")
	public Comment updateComment(@RequestHeader("Authorization") String authHeader, @PathVariable int commentId,
			@RequestBody String newCommentText) {
		Session currentSession = entityManager.unwrap(Session.class);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Comment theOriginalComment = currentSession.get(Comment.class, commentId);
		if (theOriginalComment == null) {
			throw new CustomeException("this comment doesn't exist");
		} else if (newCommentText.equals("") || newCommentText.isEmpty()) {
			throw new CustomeException("cannot add an empty comment");
		} else if (idExtractor.getIdFromToken() != theOriginalComment.getCommentCreatorId()) {
			throw new CustomeException("cannot update a comment that is not yours");
		} else if (newCommentText.equals(theOriginalComment.getText())) {
			throw new CustomeException("nothing  was changed");
		} else {
			return commentService.updateComment(commentId, newCommentText);
		}
	}

	@DeleteMapping("comments/{commentId}")
	public void deleteComment(@PathVariable int commentId, @RequestHeader("Authorization") String authHeader) {
		Session currentSession = entityManager.unwrap(Session.class);
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Comment theOriginalComment = currentSession.get(Comment.class, commentId);
		if (theOriginalComment == null) {
			throw new CustomeException("this comment doesn't exist");
		} else if (idExtractor.getIdFromToken() != theOriginalComment.getCommentCreatorId()) {
			throw new CustomeException("cannot delete a comment that is not yours");
		} else {
			commentService.deleteComment(commentId);
		}
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
