package com.luv2code.springboot.cruddemo.rest;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.service.CommentLikesService;
import com.luv2code.springboot.cruddemo.service.PageService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentLikesRestController {

	private CommentLikesService commentLikesService;

	private RelationshipService relationshipService;

	private EntityManager entityManager;

	private PageService pageService;

	@Autowired
	public CommentLikesRestController(CommentLikesService theCommentLikesService,
			RelationshipService theRelationshipService, EntityManager theEntityManager, PageService thePageService) {
		commentLikesService = theCommentLikesService;
		relationshipService = theRelationshipService;
		entityManager = theEntityManager;
		pageService = thePageService;
	}

	@PostMapping("/commentLikes/idAccount/{commentId}/{userCommentLike}")
	public CommentLike addLikeToAComment(@RequestHeader("Authorization") String authHeader, @PathVariable int commentId,
			@PathVariable boolean userCommentLike) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Comment theCommentToLike = currentSession.get(Comment.class, commentId);
		if (!userCommentLike) {
			Post thePost = currentSession.get(Post.class, theCommentToLike.getRelatedPostId());
			return commentLikesService.addLikeAsPage(idExtractor.getIdFromToken(), thePost.getPageCreatorId(),
					commentId);
		} else {
			return commentLikesService.addLikeAsUser(idExtractor.getIdFromToken(), commentId);
		}
	}

	@GetMapping("/commentLikes/likesNumber/{commentId}")
	public int getCommentLikesById(@PathVariable int commentId) {

		return commentLikesService.getCommentLikesById(commentId);
	}

	@GetMapping("/commentLikes/likers/{commentId}")
	public List<AccountBasicData> getCommentLikers(@PathVariable int commentId) {

		return commentLikesService.getCommentLikers(commentId);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
