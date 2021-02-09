package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class AccountRestController {

	@Autowired
	private AccountService accountService;

	public AccountRestController() {

	}

	Logger logger = LoggerFactory.getLogger(AccountRestController.class);

	@PostMapping("/login")
	public ResponseEntity<Account> login(@RequestBody Account user) throws AccountException {
		System.out.println("hello world");
		return accountService.login(user);
	}

	@GetMapping("/allUsers")
	public List<Account> getAllUsers() {
		return accountService.getAllUsers();
	}

	@GetMapping("/account/peopleYouMayKnow")
	public List<AccountBasicData> getPeopleYouMayKnow(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getPeopleYouMayKnow(idExtractor.getIdFromToken());
	}

	@GetMapping("/account/friends")
	public List<AccountBasicData> getMyFriends(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getMyFriends(idExtractor.getIdFromToken());
	}

	@GetMapping("/accounts/{accountId}")
	public Account getAccountById(@PathVariable Integer accountId) {
		return accountService.findById(accountId);
	}

	@GetMapping("/accounts/idAccount/getLoggedInUserBasicData")
	public AccountBasicData getLoggedInUserData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserBasicData(idExtractor.getIdFromToken());

	}

	@GetMapping("/accounts/idAccount/getLoggedInUserFullData")
	public AccountData getLoggedInUserFullData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserFullData(idExtractor.getIdFromToken());

	}

	@GetMapping("/accounts/details/{accountId}")
	public AccountBasicData getAccountBasicData(@PathVariable int accountId) throws AccountException {
		return accountService.getAccountBasicData(accountId);
	}

	@PostMapping("/signUp")
	public Account addAccount(@RequestBody Account theAccount) throws CustomeException, AccountException {
		return accountService.save(theAccount);
	}

	@PutMapping("/accounts/updateAccount")
	public Account updateAccount(@RequestBody Account theNewAccount) throws AccountException {

		return accountService.updateAccount(theNewAccount);
	}

	@PutMapping("/accounts/profilePhoto/accountId")
	public ImageUrl updateAccountProfilePhoto(@RequestHeader("Authorization") String authHeader,
			@RequestParam("image") MultipartFile newPhoto) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountProfilePhoto(idExtractor.getIdFromToken(), newPhoto);
	}

	@PutMapping("/accounts/coverPhoto/accountId")
	public ImageUrl updateAccountCoverPhoto(@RequestHeader("Authorization") String authHeader,
			@RequestParam("image") MultipartFile newCoverPhoto) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountCoverPhoto(idExtractor.getIdFromToken(), newCoverPhoto);

	}

	@PutMapping("/accounts/updateEmail")
	public Account updateAccountEmail(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "newEmail", required = true) String newEmail,
			@RequestPart(value = "password", required = true) String password) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateEmail(idExtractor.getIdFromToken(), newEmail, password);
	}

	@PutMapping("/accounts/updatePassword")
	public Account updateAccountPass(@RequestHeader("Authorization") String authHeader,
			@RequestPart(value = "oldPassword", required = true) String oldPassword,
			@RequestPart(value = "newPassword", required = true) String newPassword) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updatePassword(idExtractor.getIdFromToken(), oldPassword, newPassword);

	}

	@GetMapping("account/mutualFriends/{accountId}")
	public List<AccountBasicData> getMutualFriends(@PathVariable int accountId) {
		return accountService.getMyFriends(accountId);
	}

	@GetMapping("account/photos/{accountId}")
	public List<String> getAccountPhotos(@PathVariable int accountId) {
		return accountService.getAccountPhotos(accountId);
	}

	@DeleteMapping("accounts/accountId")
	public void deleteAccount(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		accountService.deleteById(idExtractor.getIdFromToken());
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
}
