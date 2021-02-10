package com.luv2code.springboot.cruddemo.rest;


import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.CommentLike;
import com.luv2code.springboot.cruddemo.service.CommentLikesService;
import com.luv2code.utility.IdExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
