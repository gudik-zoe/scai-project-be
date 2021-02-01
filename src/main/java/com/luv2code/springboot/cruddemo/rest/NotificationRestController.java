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
import com.luv2code.springboot.cruddemo.entity.Notification;
import com.luv2code.springboot.cruddemo.service.NotificationService;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.NotificationDetails;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationRestController {

	private NotificationService notificationService;

	private EntityManager entityManager;

	@Autowired
	public NotificationRestController(NotificationService theNotificationService, EntityManager theEntityManager) {
		this.notificationService = theNotificationService;
		this.entityManager = theEntityManager;
	}

	@GetMapping("/notification/getNotification/accountId")
	public List<Notification> getAccountNotifications(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		Session currentSession = entityManager.unwrap(Session.class);
		Account theRequestedAccount = currentSession.get(Account.class, idExtractor.getIdFromToken());
		if (theRequestedAccount == null) {
			throw new CustomeException("account not found");
		} else {
			return notificationService.getMyNotification(idExtractor.getIdFromToken());
		}
	}
	
	@GetMapping("notification/notificationDetails")
	public NotificationDetails getMyNotsDetails(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return notificationService.getNotDetails(idExtractor.getIdFromToken());
	}
	
	@PostMapping("/loadMore")
	public List<Notification> loadMore( @RequestHeader("Authorization") String authHeader , @RequestBody Integer ids){
		IdExtractor idExtractor = new IdExtractor(authHeader);	
		return notificationService.loadMore(idExtractor.getIdFromToken(), ids);
	}

	@PostMapping("/notification/addNotification")
	public Notification addNotification(@RequestBody Notification theNotification) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account notSender = currentSession.get(Account.class, theNotification.getNotCreator());
		Account notReceiver = currentSession.get(Account.class, theNotification.getNotReceiver());
		if (notSender == null || notReceiver == null) {
			throw new CustomeException("user not found");
		} else {
			return notificationService.addNotification(theNotification);
		}
	}

	@PutMapping("/notification/notificationSeen/{notId}")
	public void notHasBeenSeen(@PathVariable int notId) {
		notificationService.notificationHasBeenSeen(notId);
	}
	
	@PutMapping("/notification/allNotification/seen")
	public void notHasBeenSeen(@RequestHeader("Authorization") String authHeader) {
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
