package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.service.NotificationService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.NotificationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class NotificationRestController {

	@Autowired
	private NotificationService notificationService;


	public NotificationRestController() {

	}

	@GetMapping("/notification/getNotification/accountId")
	public List<Notification> getAccountNotifications(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return notificationService.getMyNotification(idExtractor.getIdFromToken());
	}

	@GetMapping("notification/notificationDetails")
	public NotificationDetails getMyNotsDetails(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return notificationService.getNotDetails(idExtractor.getIdFromToken());
	}

	@PostMapping("/loadMore")
	public List<Notification> loadMore(@RequestHeader("Authorization") String authHeader, @RequestBody Integer ids) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return notificationService.loadMore(idExtractor.getIdFromToken(), ids);
	}

	@PostMapping("/notification/addNotification")
	public Notification addNotification(@RequestBody Notification theNotification) {
		return notificationService.addNotification(theNotification);
	}

	@PutMapping("/notification/notificationSeen/{notId}")
	public void notHasBeenSeen(@PathVariable int notId) {
		notificationService.notificationHasBeenSeen(notId);
	}

	@PutMapping("/notification/allNotification/seen")
	public void allNotificationSeen(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		notificationService.allNotificationSeen(idExtractor.getIdFromToken());
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
