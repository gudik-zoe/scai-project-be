package com.luv2code.springboot.cruddemo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.service.CommentLikesService;
import com.luv2code.springboot.cruddemo.utility.IdExtractor;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class CommentLikesRestController {

	@Autowired
	private CommentLikesService commentLikesService;

	public CommentLikesRestController() {

	}

	@PostMapping("/commentLikes/idAccount/{commentId}/{userCommentLike}")
	public CommentLike addLikeToAComment(@RequestHeader("Authorization") String authHeader, @PathVariable int commentId,
			@PathVariable boolean userCommentLike) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return commentLikesService.addCommentLike(idExtractor.getIdFromToken(), commentId, userCommentLike);
	}
//
//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleException(CustomeException exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
//				System.currentTimeMillis());
//
//		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//
//	}
//
//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}
}
