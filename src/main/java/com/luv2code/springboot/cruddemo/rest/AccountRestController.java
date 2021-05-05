package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.dto.*;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.service.AccountService;
import com.luv2code.springboot.cruddemo.service.EmailSender;
import com.luv2code.springboot.cruddemo.utility.IdExtractor;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class AccountRestController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private EmailSender emailSender;



	public AccountRestController() {

	}

	@ApiOperation(value = "login an account ", notes = "check for the account by mail then check the password : doesn't token in header")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful", response = Account.class),
			@ApiResponse(code = 400, message = "Bad Request ", response = NotFoundException.class),
			@ApiResponse(code = 404, message = "not found ", response = NotFoundException.class),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "access-token", value = "access-token", required = false, dataType = "sting", paramType = "header") })

	@PostMapping("/login")
	public ResponseEntity<Account> login(@ApiParam(name = "Account Object") @RequestBody LoginDTO loginDto) {
		return accountService.login(loginDto);
	}

	@ApiOperation(value = "find all users", notes = "no need for any parameter ", response = Account.class)
	@GetMapping("/allUsers")
	public List<Account> getAllUsers() {
		return accountService.getAllUsers();
	}

	@GetMapping("/greeting")
	public String greeting() {
		return "hello world";
	}

	@ApiOperation(value = "get people you may know ", notes = "checks for  users in db with which u don't the status of friend ", response = AccountBasicData.class)
	@GetMapping("/account/peopleYouMayKnow")
	public List<AccountBasicData> getPeopleYouMayKnow(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getPeopleYouMayKnow(idExtractor.getIdFromToken());
	}

	@ApiOperation(value = "get a user's friends  ", notes = "checks for users in db with which u have the status of friend ", response = AccountBasicData.class)
	@GetMapping("/account/friends")
	public List<AccountBasicData> getMyFriends(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getMyFriends(idExtractor.getIdFromToken());
	}

	@ApiOperation(value = "get a user fullData by id  ", response = Account.class)
	@GetMapping("/accounts/{accountId}")
	public AccountData getAccountById(@ApiParam(value = "account id", required = true) @PathVariable Integer accountId) {
		return accountService.getLoggedInUserFullData(accountId);
	}

	@ApiOperation(value = "get the basic data of the Logged user", response = AccountBasicData.class)
	@GetMapping("/accounts/idAccount/getLoggedInUserBasicData")
	public AccountBasicData getLoggedInUserData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserBasicData(idExtractor.getIdFromToken());

	}

	@ApiOperation(value = "get Logged user FullData ", response = AccountData.class)
	@GetMapping("/accounts/idAccount/getLoggedInUserFullData")
	public AccountData getLoggedInUserFullData(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.getLoggedInUserFullData(idExtractor.getIdFromToken());

	}

	@ApiOperation(value = "get a user basicData ", response = AccountBasicData.class)
	@GetMapping("/accounts/details/{accountId}")
	public AccountBasicData getAccountBasicData(
			@ApiParam(name = "accountId", required = true) @PathVariable int accountId) throws AccountException {
		return accountService.getAccountBasicData(accountId);
	}

	@ApiOperation(value = "create a user", notes = "check's in db for a similar mail if not it adds the user", response = Account.class)
	@PostMapping("/signUp")
	public boolean addAccount(@ApiParam(name = "account object", required = true) @RequestBody Account theAccount)
			throws NotFoundException, AccountException {
		return accountService.save(theAccount);
	}

	@ApiOperation(value = "update an account", response = Account.class)
	@PutMapping("/accounts/updateAccount")
	public Account updateAccount(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name = "account object", required = true) @RequestBody Account theNewAccount)
			throws AccountException {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccount(theNewAccount, idExtractor.getIdFromToken());
	}

	@ApiOperation(value = "update an account profile photo", response = ImageUrl.class)
	@PutMapping("/accounts/profilePhoto/accountId")
	public ImageUrl updateAccountProfilePhoto(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name = "image") @RequestPart("image") MultipartFile newPhoto) throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountProfilePhoto(idExtractor.getIdFromToken(), newPhoto);
	}

	@ApiOperation(value = "update an account cover photo", response = ImageUrl.class)
	@PutMapping("/accounts/coverPhoto/accountId")
	public ImageUrl updateAccountCoverPhoto(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name = "image", required = true, type = "MultipartFile") @RequestPart("image") MultipartFile newCoverPhoto)
			throws Exception {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateAccountCoverPhoto(idExtractor.getIdFromToken(), newCoverPhoto);

	}

	@ApiOperation(value = "update an account email", response = Account.class)
	@PutMapping("/accounts/updateEmail")
	public Account updateAccountEmail(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name = "new Email", required = true, type = "string") @RequestPart(value = "newEmail", required = true) String newEmail,
			@ApiParam(name = "password", required = true, type = "string") @RequestPart(value = "password", required = true) String password) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updateEmail(idExtractor.getIdFromToken(), newEmail, password);
	}

	@ApiOperation(value = "update an account password", response = Account.class)
	@PutMapping("/accounts/updatePassword")
	public Account updateAccountPass(@RequestHeader("Authorization") String authHeader,
			@ApiParam(name = "old password", required = true) @RequestPart(value = "oldPassword", required = true) String oldPassword,
			@ApiParam(name = "new password", required = true) @RequestPart(value = "newPassword", required = true) String newPassword) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		return accountService.updatePassword(idExtractor.getIdFromToken(), oldPassword, newPassword , null);

	}

	@ApiOperation(value = "get mutual friends for a user ", response = AccountBasicData.class)
	@GetMapping("account/mutualFriends/{accountId}")
	public List<AccountBasicData> getMutualFriends(@PathVariable int accountId) {
		return accountService.getMyFriends(accountId);
	}

	@ApiOperation(value = "get user Photos ", response = String.class)
	@GetMapping("account/photos/{accountId}")
	public List<String> getAccountPhotos(@ApiParam(name = "accountId", required = true) @PathVariable int accountId) {
		return accountService.getAccountPhotos(accountId);
	}

	@ApiOperation(value = "deleting an account")
	@DeleteMapping("accounts/accountId")
	public void deleteAccount(@RequestHeader("Authorization") String authHeader) {
		IdExtractor idExtractor = new IdExtractor(authHeader);
		accountService.deleteById(idExtractor.getIdFromToken());
	}

	@ApiOperation(value = "resetting the password")
	@PostMapping("/resetPassword")
	public Account resetPassword(@RequestBody String email) throws IOException, MessagingException {

			return emailSender.sendEmail(email);


	}

	@ApiOperation(value = "checking  the temporary password")
	@PostMapping("/checkTempPassword")
	public boolean checkTempPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws ParseException {
		System.out.println(resetPasswordDTO.getTempPassword() + " " + resetPasswordDTO.getNewPassword() + " " + resetPasswordDTO.getConfirmNewPassword());
		try{
			return	accountService.checkTempPassword(resetPasswordDTO);
		}catch (Exception e){
			System.out.println(e);
			throw e;
		}


	}

}
