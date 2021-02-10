package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.service.CommentService;
import com.luv2code.utility.IdExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class CommentRestController {
	@Autowired
	private CommentService commentService;

	public CommentRestController() {

	}

	@PostMapping("comments/idAccount/{postId}/{userComment}")
	public Comment addComment(@RequestHeader("Authorization") String authHeader, @RequestBody String commentText,
			@PathVariable int postId, @PathVariable boolean userComment) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return commentService.addComment(idExtractor.getIdFromToken(), commentText, postId, userComment);
	}

	@PutMapping("comments/idAccount/{commentId}")
	public Comment updateComment(@RequestHeader("Authorization") String authHeader, @PathVariable int commentId,
			@RequestBody String newCommentText) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return commentService.updateComment(commentId, newCommentText, idExtractor.getIdFromToken());
	}

	@DeleteMapping("comments/{commentId}")
	public void deleteComment(@PathVariable int commentId, @RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		commentService.deleteComment(commentId, idExtractor.getIdFromToken());

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
