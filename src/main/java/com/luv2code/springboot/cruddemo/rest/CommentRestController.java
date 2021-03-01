package com.luv2code.springboot.cruddemo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.springboot.cruddemo.entity.Comment;
import com.luv2code.springboot.cruddemo.service.CommentService;
import com.luv2code.springboot.cruddemo.utility.IdExtractor;

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

//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler
//	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
//		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error occured",
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}
}
