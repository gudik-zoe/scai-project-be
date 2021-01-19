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
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.PostLike;
import com.luv2code.springboot.cruddemo.service.PostLikesService;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostLikesRestController {

	private PostLikesService postLikesService;

	private EntityManager entityManager;

	private RelationshipService relationshipService;

	@Autowired
	public PostLikesRestController(PostLikesService thePostLikesService, EntityManager theEntityManager,
			RelationshipService theRelatinoRelationshipService) {
		postLikesService = thePostLikesService;
		entityManager = theEntityManager;
		relationshipService = theRelatinoRelationshipService;
	}

	@PostMapping("/postLikes/accountId/{idPost}")
	private PostLike addLike(@RequestHeader("Authorization") String authHeader, @PathVariable int idPost
			) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Post thelikedPost = currentSession.get(Post.class, idPost);
		if (thelikedPost == null) {
			throw new CustomeException("cannot add a like to a post that doesn't exist");
		}if(thelikedPost.getPageCreatorId() != null) {
			return postLikesService.addLike(idExtractor.getIdFromToken(), thelikedPost);
		}
		else {
			Integer theRelationshipStatus = relationshipService.getStatus(idExtractor.getIdFromToken(),
					thelikedPost.getPostCreatorId());
			if (theRelationshipStatus == null || theRelationshipStatus != 1 ) {
				throw new CustomeException("cannot like a user's post that is not ur friend");
			}else if ( thelikedPost.getStatus() == 2 && idExtractor.getIdFromToken() !=  thelikedPost.getPostCreatorId() ) {
				throw new CustomeException("cannot like a private post unless it's yours");
			}else {		
				return postLikesService.addLike(idExtractor.getIdFromToken(), thelikedPost);
			}
		}

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeEsception(CustomeException exc) {
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

	@GetMapping("/postLikes/likers/{postId}")
	private List<Integer> getLikersByPostId(@PathVariable int postId) {
		return postLikesService.getPostLikers(postId);
	}

	@GetMapping("/postLikes/likesNumber/{postId}")
	private int getPostLikes(@PathVariable int postId) {
		return postLikesService.getPostLikesById(postId);
	}

}
