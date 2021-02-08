package com.luv2code.springboot.cruddemo.rest;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RelationshipRestController {

	@Autowired
	private RelationshipService relationshipService;

	public RelationshipRestController() {

	}

	@PostMapping("/relation/accountId/{user2Id}")
	public Relationship createRelation(@RequestHeader("Authorization") String authHeader, @PathVariable int user2Id) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return relationshipService.createRelation(idExtractor.getIdFromToken(), user2Id);

	}

	@GetMapping("/relation/getRelation/accountId/{user2Id}")
	public Relationship checkRelation(@RequestHeader("Authorization") String authHeader, @PathVariable int user2Id) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return relationshipService.checkRelation(idExtractor.getIdFromToken(), user2Id);

	}

	@GetMapping("relation/getStatus/{user2Id}")
	public Integer getStatus(@RequestHeader("Authorization") String authHeader, @PathVariable int user2Id) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return relationshipService.getStatus(idExtractor.getIdFromToken(), user2Id);
	}

	@GetMapping("/relation/getMyFriends")
	public List<Relationship> getMyFriends(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return relationshipService.getMyFriends(idExtractor.getIdFromToken());
	}

	@GetMapping("/relation/getFriendRequests/accountId")
	public List<Relationship> getFriendRequests(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return relationshipService.getFriendRequests(idExtractor.getIdFromToken());
	}

	@PutMapping("/relation/answerRequest/{relationshipId}/{status}")
	public Relationship answerRequest(@PathVariable int relationshipId, @PathVariable int status,
			@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);

		return relationshipService.respondToFriendRequest(idExtractor.getIdFromToken(), relationshipId, status);
	}

	@DeleteMapping("/relation/deleteRequest/accountId/{userTwoId}")
	public void cancelFriendRequest(@RequestHeader("Authorization") String authHeader, @PathVariable int userTwoId) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		relationshipService.cancelFriendRequest(idExtractor.getIdFromToken(), userTwoId);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
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
