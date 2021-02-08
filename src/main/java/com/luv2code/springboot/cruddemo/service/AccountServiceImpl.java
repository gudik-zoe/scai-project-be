package com.luv2code.springboot.cruddemo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.AccountException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.jpa.repositories.AccountJpaRepo;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.ImageUrl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountJpaRepo accountRepoJpa;

	@Autowired
	private StorageService storageService;

	@Autowired
	private RelationshipService relationshipService;

	@Autowired
	private PostService postService;
	
	
	public AccountServiceImpl() {

	}

	@Override
	public List<AccountBasicData> getPeopleYouMayKnow(int accountId) {
		List<Account> accounts = new ArrayList<Account>();
		List<AccountBasicData> peopleYouMayKnow = new ArrayList<AccountBasicData>();
		accounts = accountRepoJpa.findPeopleYouMayKnow(accountId);
		for (Account account : accounts) {
			if (relationshipService.getStatus(accountId, account.getIdAccount()) == null
					|| relationshipService.getStatus(accountId, account.getIdAccount()) != 1) {
				AccountBasicData personYouMayKnow = new AccountBasicData(account.getFirstName(), account.getLastName(),
						account.getProfilePhoto(), account.getIdAccount(), account.getCoverPhoto());
				peopleYouMayKnow.add(personYouMayKnow);
			}
		}
		return peopleYouMayKnow;
	}

	@Override
	public Account save(Account account) throws CustomeException {
		String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher mat = emailPattern.matcher(account.getEmail());
		if (!mat.matches()) {
			throw new CustomeException("enter a valid email");
		} else if (!account.getPassword().matches(passwordPattern)) {
			throw new CustomeException(
					"a valid password password should contains at least one letter in upperCase + one digit + one alphanumeric character -/*?");
		} else if (accountRepoJpa.findByEmail(account.getEmail()) == null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			account.setPassword(encoder.encode(account.getPassword()));
			return accountRepoJpa.save(account);
		} else {
			throw new CustomeException("email already exist");
		}

	}

	@Override
	public void deleteById(int theId) {
		accountRepoJpa.deleteById(theId);
	}

	@Override
	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception {
		ImageUrl imgUrl = storageService.pushImage(photo);
		if (imgUrl == null) {
			throw new CustomeException("no image was provided");
		} else {
			Account theAccount = findById(accountId);
			theAccount.setProfilePhoto(imgUrl.getImageUrl());
			return imgUrl;
		}
	}

	@Override
	public AccountBasicData getAccountBasicData(int accountId) {
		Account account = findById(accountId);
		AccountBasicData theAccount = new AccountBasicData(account.getFirstName(), account.getLastName(),
				account.getProfilePhoto(), account.getIdAccount(), account.getCoverPhoto());
		return theAccount;
	}

	@Override
	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo) throws Exception {
		ImageUrl imgUrl = storageService.pushImage(photo);
		if (imgUrl == null) {
			throw new CustomeException("no image was provided");
		} else {
			Account theAccount = findById(accountId);
			theAccount.setCoverPhoto(imgUrl.getImageUrl());
			return imgUrl;
		}

	}

	@Override
	public Account updateAccount(Account account) throws AccountException {
		return accountRepoJpa.save(account);
	}

	@Override
	public AccountBasicData getLoggedInUserBasicData(int accountIdFromToken) {
		Account account = findById(accountIdFromToken);
		AccountBasicData theAccount = new AccountBasicData(account.getFirstName(), account.getLastName(),
				account.getProfilePhoto(), account.getIdAccount(), account.getCoverPhoto());
		return theAccount;
	}

	@Override
	public Account updateEmail(int accountId, String email, String password) {
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Account theAccount = findById(accountId);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean check = encoder.matches(password, theAccount.getPassword());
		Matcher mat = emailPattern.matcher(email);
		if (check && mat.matches() && accountRepoJpa.findByEmail(email) == null) {
			theAccount.setEmail(email);
			return accountRepoJpa.save(theAccount);
		} else {
			throw new CustomeException("enter a valid email or this email already exist");
		}
	}

	@Override
	public Account updatePassword(int accountId, String oldPassword, String newPassword) {
		String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		if (!newPassword.matches(passwordPattern)) {
			throw new CustomeException(
					" password  should contains at least one letter in upperCase + one digit + one alphanumeric character -/*?\");");
		} else {
			Account theAccount = findById(accountId);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			boolean check = encoder.matches(oldPassword, theAccount.getPassword());
			if (check && !oldPassword.equals(newPassword)) {
				theAccount.setPassword(encoder.encode(newPassword));
				accountRepoJpa.save(theAccount);
				return theAccount;
			} else {
				throw new CustomeException("incorrect password or u didn't make any changes to ur currentPassword");
			}
		}

	}

	@Override
	public AccountData getLoggedInUserFullData(int accountIdFromToken) {
		Account account = findById(accountIdFromToken);
		AccountData theAccount = new AccountData(account.getFirstName(), account.getLastName(),
				account.getProfilePhoto(), account.getIdAccount(), account.getCoverPhoto(), account.getLivesIn(),
				account.getEmail(), account.getWentTo(), account.getStudy());
		return theAccount;
	}

	@Override
	public List<AccountBasicData> getMyFriends(int accountId) {
		List<Relationship> relationships = relationshipService.getMyFriends(accountId);
		List<AccountBasicData> friends = new ArrayList<AccountBasicData>();
		Account theAccount = null;
		for (Relationship relationship : relationships) {
			Optional<Account> result = accountRepoJpa
					.findById(relationship.getUserOneId() == accountId ? relationship.getUserTwoId()
							: relationship.getUserOneId());
			if (result.isPresent()) {
				theAccount = result.get();
				friends.add(new AccountBasicData(theAccount.getFirstName(), theAccount.getLastName(),
						theAccount.getProfilePhoto(), theAccount.getIdAccount(), theAccount.getCoverPhoto()));
			}
		}
		return friends;
	}

	@Override
	public ResponseEntity<Account> login(Account user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Account theAccount = accountRepoJpa.findByEmail(user.getEmail());
		boolean check = encoder.matches(user.getPassword(), theAccount.getPassword());
		if (theAccount != null && check) {
			HttpHeaders headers = new HttpHeaders();
			HashMap<String, Object> addedValues = new HashMap<String, Object>();
			addedValues.put("userid", theAccount.getIdAccount());
			String token = Jwts.builder().addClaims(addedValues).setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
					.signWith(SignatureAlgorithm.HS512, "ciao").compact();
			headers.add("Authorization", "Bearer " + token);
			return ResponseEntity.ok().headers(headers).build();
		} else {
			throw new CustomeException("invalid credentials");
		}
	}

	@Override
	public List<Account> getAllUsers() {
		return accountRepoJpa.findAll();
	}

	@Override
	public List<String> getAccountPhotos(int accountId) {
		Account theAccount = findById(accountId);
		List<String> photos = postService.getAccountPhotos(accountId);
		photos.add(theAccount.getCoverPhoto());
		photos.add(theAccount.getProfilePhoto());
		return photos;
	}

	@Override
	public Account findById(int accountId) {
		Optional<Account> result = accountRepoJpa.findById(accountId);
		Account theAccount = null;
		if (result.isPresent()) {
			theAccount = result.get();
		} else {
			throw new CustomeException("no such id for an account");
		}
		return theAccount;
	}

}
