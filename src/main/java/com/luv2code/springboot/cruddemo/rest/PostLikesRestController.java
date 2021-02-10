package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.PostLike;
import com.luv2code.springboot.cruddemo.service.PostLikesService;
import com.luv2code.utility.IdExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class PostLikesRestController {

	@Autowired
	private PostLikesService postLikesService;
	Logger logger = LoggerFactory.getLogger(AccountRestController.class);
	public PostLikesRestController() {

	}

	@PostMapping("/postLikes/accountId/{postId}")
	private PostLike addLike(@RequestHeader("Authorization") String authHeader, @PathVariable int postId)
			throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return postLikesService.addLike(idExtractor.getIdFromToken(), postId);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeEsception(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error occured",
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
