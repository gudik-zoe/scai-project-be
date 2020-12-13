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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.springboot.cruddemo.service.MessagesService;
import com.luv2code.utility.IdExtractor;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessagesRestController {

	private MessagesService messageService;
	private EntityManager entityManager;

	@Autowired
	public MessagesRestController(MessagesService theMessagesService, EntityManager theEntityManager) {
		this.messageService = theMessagesService;
		this.entityManager = theEntityManager;
	}

	@GetMapping("/messages/myMessages")
	private Integer getMyMessages(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.getMyMessages(idExtractor.getIdFromToken());
	}

	@GetMapping("/messages/receivedFrom/accountId/{senderId}")
	private List<Message> getMessagesReceivedFrom(@RequestHeader("Authorization") String authHeader,
			@PathVariable int senderId) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.getMessagesReceivedFrom(idExtractor.getIdFromToken(), senderId);
	}

	@PostMapping("/messages/sendMessage")
	private Message sendMessage(@RequestBody Message theMessage) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account sender = currentSession.get(Account.class, theMessage.getIdSender());
		Account receiver = currentSession.get(Account.class, theMessage.getIdSender());
		if (sender == null || receiver == null) {
			throw new CustomeException("user not found");
		} else if (theMessage.getMessage().isBlank()) {
			throw new CustomeException("cannot send an empty message");
		} else {
			return messageService.sendMessage(theMessage);
		}
	}

	@GetMapping("/messages/unseenMessages/{account2Id}")
	private Integer checkForUnseenMessages(@RequestHeader("Authorization") String authHeader , @PathVariable int account2Id) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.unSeenMessagesFrom(idExtractor.getIdFromToken() , account2Id);

	}
	
	@PutMapping("/messages/seen")
	private void messageSeen(@RequestBody int user2Id , @RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		
		messageService.messageSeen(idExtractor.getIdFromToken() , user2Id);

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
