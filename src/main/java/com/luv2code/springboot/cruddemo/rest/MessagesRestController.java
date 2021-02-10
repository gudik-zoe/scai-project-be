package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.entity.Message;
import com.luv2code.springboot.cruddemo.service.MessagesService;
import com.luv2code.utility.IdExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class MessagesRestController {

	@Autowired
	private MessagesService messageService;

	public MessagesRestController() {

	}

	@GetMapping("/messages/myMessages")
	private Integer getMyMessages(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.getMyMessages(idExtractor.getIdFromToken());
	}

	@GetMapping("/messages/receivedFrom/accountId/{senderId}")
	private List<Message> getConversation(@RequestHeader("Authorization") String authHeader,
			@PathVariable int senderId) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.getConversation(idExtractor.getIdFromToken(), senderId);
	}

	@PostMapping("/messages/sendMessage")
	private Message sendMessage(@RequestBody Message theMessage) {
		return messageService.sendMessage(theMessage);

	}

	@GetMapping("/messages/unseenMessages/{account2Id}")
	private Integer checkForUnseenMessages(@RequestHeader("Authorization") String authHeader,
			@PathVariable int account2Id) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return messageService.unSeenMessagesFrom(idExtractor.getIdFromToken(), account2Id);

	}

	@PutMapping("/messages/seen")
	private void messageSeen(@RequestBody int user2Id, @RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		messageService.messageSeen(idExtractor.getIdFromToken(), user2Id);

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
//		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
//				System.currentTimeMillis());
//		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//	}

}
