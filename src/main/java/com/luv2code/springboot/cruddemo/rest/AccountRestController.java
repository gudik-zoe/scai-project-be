package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.IdExtractor;
import com.luv2code.utility.ImageUrl;
import io.swagger.annotations.*;
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
	private AccountService  accountService;

	public AccountRestController() {

	}

	Logger logger = LoggerFactory.getLogger(AccountRestController.class);

	@ApiOperation(value = "login an account " , notes = "check for the account by mail then check the password : doesn't token in header")
	@ApiResponses(value={
			@ApiResponse(code = 200 , message = "Successful" , response = Account.class) ,
			@ApiResponse(code = 400 , message = "Bad Request " , response = CustomeException.class) ,
			@ApiResponse(code = 404 , message = "not found " , response = CustomeException.class) ,
			@ApiResponse(code = 500 , message = "Internal Server Error")
	})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "access-token" , value = "access-token" , required = true , dataType = "sting" , paramType="header")
	})
	@PostMapping("/login")
	public ResponseEntity<Account> login(@ApiParam(name = "Account Object")@RequestBody Account user) throws AccountException {
		return accountService.login(user);
	}


	@ApiOperation(value="find all users" , notes = "no need for any parameter " , response = Account.class)
	@GetMapping("/allUsers")
	public List<Account> getAllUsers() {
		return accountService.getAllUsers();
	}


	@ApiOperation(value="get people you may know " , notes = "checks for  users in db with which u don't the status of friend " , response = AccountBasicData.class)
	@GetMapping("/account/peopleYouMayKnow")
	public List<AccountBasicData> getPeopleYouMayKnow(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getPeopleYouMayKnow(idExtractor.getIdFromToken());
	}
	@ApiOperation(value="get a user's friends  " , notes = "checks for users in db with which u have the status of friend " , response = AccountBasicData.class)
	@GetMapping("/account/friends")
	public List<AccountBasicData> getMyFriends(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getMyFriends(idExtractor.getIdFromToken());
	}

	@ApiOperation(value="get a user fullData by id  " , response = Account.class)
	@GetMapping("/accounts/{accountId}")
	public Account getAccountById(@ApiParam(value = "account id"  , required = true) @PathVariable Integer accountId)  {
		 	return accountService.findById(accountId);
	}

	@ApiOperation(value="get the basic data of the Logged user" , response = AccountBasicData.class)
	@GetMapping("/accounts/idAccount/getLoggedInUserBasicData")
	public AccountBasicData getLoggedInUserData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserBasicData(idExtractor.getIdFromToken());

	}

	@ApiOperation(value="get Logged user FullData " , response = AccountData.class)
	@GetMapping("/accounts/idAccount/getLoggedInUserFullData")
	public AccountData getLoggedInUserFullData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserFullData(idExtractor.getIdFromToken());

	}


	@ApiOperation(value="get a user basicData " , response = AccountBasicData.class)
	@GetMapping("/accounts/details/{accountId}")
	public AccountBasicData getAccountBasicData(@ApiParam(name = "accountId" , required = true )@PathVariable int accountId) throws AccountException {
		return accountService.getAccountBasicData(accountId);
	}

	@ApiOperation(value="create a user" , notes = "check's in db for a similar mail if not it adds the user", response = Account.class)
	@PostMapping("/signUp")
	public Account addAccount(@ApiParam(name = "account object" , required = true) @RequestBody Account theAccount) throws CustomeException, AccountException {
		return accountService.save(theAccount);
	}


	@ApiOperation(value="update an account" , response = Account.class)
	@PutMapping("/accounts/updateAccount")
	public Account updateAccount(@RequestHeader("Authorization") String authHeader, @ApiParam(name = "account object" , required = true) @RequestBody Account theNewAccount) throws AccountException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccount(theNewAccount , idExtractor.getIdFromToken());
	}

	@ApiOperation(value="update an account profile photo" , response = ImageUrl.class)
	@PutMapping("/accounts/profilePhoto/accountId")
	public ImageUrl updateAccountProfilePhoto(@RequestHeader("Authorization") String authHeader,
		@ApiParam(name = "image" )	@RequestPart("image") MultipartFile newPhoto) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountProfilePhoto(idExtractor.getIdFromToken(), newPhoto);
	}


	@ApiOperation(value="update an account cover photo" , response = ImageUrl.class)
	@PutMapping("/accounts/coverPhoto/accountId")
	public ImageUrl updateAccountCoverPhoto(@RequestHeader("Authorization") String authHeader,
											@ApiParam(name = "image" , required = true  , type = "MultipartFile")	@RequestPart("image") MultipartFile newCoverPhoto) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountCoverPhoto(idExtractor.getIdFromToken(), newCoverPhoto);

	}

	@ApiOperation(value="update an account email" , response = Account.class)
	@PutMapping("/accounts/updateEmail")
	public Account updateAccountEmail(@RequestHeader("Authorization") String authHeader,
		@ApiParam(name="new Email" , required = true , type = "string")	@RequestPart(value = "newEmail", required = true) String newEmail,
									  @ApiParam(name="password" , required = true ,type = "string")	@RequestPart(value = "password", required = true) String password) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateEmail(idExtractor.getIdFromToken(), newEmail, password);
	}

	@ApiOperation(value="update an account password" , response = Account.class)
	@PutMapping("/accounts/updatePassword")
	public Account updateAccountPass(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name="old password" , required = true )@RequestPart(value = "oldPassword", required = true) String oldPassword,
									 @ApiParam(name="new password" , required = true )@RequestPart(value = "newPassword", required = true) String newPassword) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updatePassword(idExtractor.getIdFromToken(), oldPassword, newPassword);

	}

	@ApiOperation(value="get mutual friends for a user " , response = AccountBasicData.class)
	@GetMapping("account/mutualFriends/{accountId}")
	public List<AccountBasicData> getMutualFriends(@PathVariable int accountId) {
		return accountService.getMyFriends(accountId);
	}

	@ApiOperation(value="get user Photos " , response = String.class)
	@GetMapping("account/photos/{accountId}")
	public List<String> getAccountPhotos(@ApiParam(name="accountId" , required = true)@PathVariable int accountId) {
		return accountService.getAccountPhotos(accountId);
	}

	@ApiOperation(value="deleting an account" )
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
