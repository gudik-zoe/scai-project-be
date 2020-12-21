package com.luv2code.springboot.cruddemo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Post;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.service.RelationshipService;
import com.luv2code.springboot.cruddemo.service.StorageService;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.ImageUrl;

import java.util.Date;
import java.util.HashMap;
import org.springframework.http.HttpHeaders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Repository
public class AccountDAOHibernateImpl implements AccountDAO {

	private EntityManager entityManager;

	private StorageService storageService;

	private RelationshipService relationshipService;

	@Autowired
	public AccountDAOHibernateImpl(EntityManager theEntityManager, StorageService theStorageService,
			RelationshipService theRelationshipService) {
		entityManager = theEntityManager;
		storageService = theStorageService;
		relationshipService = theRelationshipService;

	}

	@Override
	public ResponseEntity<Account> login(Account user) throws CustomeException{
		Session currentSession = entityManager.unwrap(Session.class);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String selectQuery = "FROM Account as account WHERE account.email = :emailParam";
		Query<Account> theQuery = currentSession.createQuery(selectQuery);
		theQuery.setParameter("emailParam", user.getEmail());
		Account theResult;
		try {
		 theResult =  theQuery.getSingleResult();
		}catch (Exception e) {
			throw new CustomeException("invalid credentials");
		}
		boolean check = encoder.matches(user.getPassword(), theResult.getPassword());
		if (theResult == null || !check) {
			throw new CustomeException("invalid credentials");
		} else {
			HttpHeaders headers = new HttpHeaders();
			HashMap<String, Object> addedValues = new HashMap<String, Object>();
			addedValues.put("userid", theResult.getIdAccount());
			String token = Jwts.builder().addClaims(addedValues).setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
					.signWith(SignatureAlgorithm.HS512, "ciao").compact();
			headers.add("Authorization", "Bearer " + token);
			return ResponseEntity.ok().headers(headers).build();
			}
	}

	@Override
	public List<AccountBasicData> getAllUsers() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Account> theQuery = currentSession.createQuery("from Account", Account.class);
		List<Account> users = theQuery.getResultList();
		List<AccountBasicData> allUsers = new ArrayList<AccountBasicData>();
		for (Account user : users) {
			AccountBasicData theUser = new AccountBasicData(user.getFirstName(), user.getLastName(),
					user.getProfilePhoto(), user.getIdAccount(), user.getCoverPhoto());
			allUsers.add(theUser);
		}
		return allUsers;
	}

	@Override
	public List<AccountBasicData> getPeopleYouMayKnow(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Account> theQuery = currentSession.createQuery("from Account where id_account != " + accountId,
				Account.class);
		List<Account> theAccounts = theQuery.getResultList();
		List<AccountBasicData> peopleYouMayKnow = new ArrayList<AccountBasicData>();
		for (Account account : theAccounts) {
			if (relationshipService.getStatus(accountId, account.getIdAccount()) == null || relationshipService.getStatus(accountId, account.getIdAccount()) !=  1) {
				AccountBasicData personYouMayKnow = new AccountBasicData(account.getFirstName(), account.getLastName(),
						account.getProfilePhoto(), account.getIdAccount(), account.getCoverPhoto());
				peopleYouMayKnow.add(personYouMayKnow);
			}
		}
		return peopleYouMayKnow;
	}

	@Override
	public List<AccountBasicData> getMyFriends(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Relationship> theQuery = currentSession.createQuery("from Relationship where user_one_id = " + accountId + " and status = " + 1 + " or  user_two_id = "+ accountId + " and status = " + 1 , 
				Relationship.class);
		List<Relationship> theRelations = theQuery.getResultList();
		List<AccountBasicData> myFriends = new ArrayList<AccountBasicData>();
		for (Relationship relation : theRelations) {
			if(accountId == relation.getUserOneId()) {
				AccountBasicData theAccount = getAccountBasicData(relation.getUserTwoId());
				myFriends.add(theAccount);
			}
			else {
				AccountBasicData theAccount = getAccountBasicData(relation.getUserOneId());
				myFriends.add(theAccount);
			}
		}
		return myFriends;

	}
	

	@Override
	public AccountData findById(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, accountId);
		AccountData accountdata = new AccountData(theAccount.getFirstName(), theAccount.getLastName(),
				theAccount.getProfilePhoto(), theAccount.getIdAccount(), theAccount.getCoverPhoto(),
				theAccount.getLivesIn(), theAccount.getEmail(), theAccount.getWentTo(), theAccount.getStudy());
		return accountdata;
	}

	@Override
	public AccountBasicData getTheLoggedInUser(int accountIdFromToken) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, accountIdFromToken);
		AccountBasicData accountBasicData = new AccountBasicData(theAccount.getFirstName(), theAccount.getLastName(),
				theAccount.getProfilePhoto(), theAccount.getIdAccount(), theAccount.getCoverPhoto());
		return accountBasicData;
	}

	@Override
	public Account save(Account theAccount) throws CustomeException {
		Session currentSession = entityManager.unwrap(Session.class);
		if (checkIfEmailIsNotTaken(theAccount.getEmail())) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			theAccount.setPassword(encoder.encode(theAccount.getPassword()));
			currentSession.saveOrUpdate(theAccount);
			return theAccount;
		} else {
			throw new CustomeException("this mail already exist");
		}
	}

	@Override
	public void deleteById(int theId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, theId);
		if (theAccount == null) {
			throw new CustomeException("this account doesn't exist");
		} else {
			currentSession.delete(theAccount);
		}
	}

	@Override
	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		ImageUrl img = storageService.pushImage(photo);
		Account account = currentSession.get(Account.class, accountId);
		account.setProfilePhoto(img.getImageUrl());
		currentSession.update(account);
		return img;

	}

	@Override
	public AccountBasicData getAccountBasicData(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theRequestedAccount = currentSession.get(Account.class, accountId);

		AccountBasicData account = new AccountBasicData();
		account.setIdAccount(theRequestedAccount.getIdAccount());
		account.setFirstName(theRequestedAccount.getFirstName());
		account.setLastName(theRequestedAccount.getLastName());
		account.setProfilePhoto(theRequestedAccount.getProfilePhoto());
		return account;
	}

	@Override
	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo) throws Exception {
		Session currentSession = entityManager.unwrap(Session.class);
		ImageUrl img = storageService.pushImage(photo);
		Account account = currentSession.get(Account.class, accountId);
		account.setCoverPhoto(img.getImageUrl());
		currentSession.update(account);
		return img;

	}

	@Override
	public int getAccountByPostId(int postId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession.createQuery("from Post where id_post =" + postId, Post.class);
		Post post = theQuery.getSingleResult();
		int accountId = post.getPostCreatorId();
		return accountId;
	}

	@Override
	public boolean checkIfEmailIsNotTaken(String email) {
		Session currentSession = entityManager.unwrap(Session.class);
		String selectQuery = "FROM Account as account WHERE account.email = :emailParam";
		Query theQuery = currentSession.createQuery(selectQuery);
		theQuery.setParameter("emailParam", email);
		@SuppressWarnings("unchecked")
		List<Account> theResult = theQuery.getResultList();
		if (theResult.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Account updateAccount(Account theNewAccount, int idAccount) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, idAccount);
		theAccount.setFirstName(theNewAccount.getFirstName());
		theAccount.setLastName(theNewAccount.getLastName());
		theAccount.setStudy(theNewAccount.getStudy());
		theAccount.setWentTo(theNewAccount.getWentTo());
		theAccount.setLivesIn(theNewAccount.getLivesIn());
		currentSession.update(theAccount);
		return theNewAccount;

	}

	@Override
	public Account updateEmail(int accountId, String newEmail , String password) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, accountId);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean check = encoder.matches(password, theAccount.getPassword());
		if(!check) {
			throw new CustomeException("invalid password");
		}
		if (checkIfEmailIsNotTaken(newEmail) && check && !newEmail.equals(theAccount.getEmail())) {
			theAccount.setEmail(newEmail);
			currentSession.update(theAccount);
		} else {
			throw new CustomeException("this email is already taken ");
		}
		return theAccount;
	}

	@Override
	public Account updatePassword(int accountId, String oldPassword , String newPassword) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, accountId);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean check = encoder.matches(oldPassword, theAccount.getPassword());
		if(check && !oldPassword.equals(newPassword)) {
			theAccount.setPassword(encoder.encode(newPassword));
			currentSession.update(theAccount);			
			return theAccount;
		}else {
			throw new CustomeException("incorrect password or u didn't make any changes to ur currentPassword");
		}
	}

	@Override
	public AccountData getLoggedInUserFullData(int accountIdFromToken) {
		Session currentSession = entityManager.unwrap(Session.class);
		Account theAccount = currentSession.get(Account.class, accountIdFromToken);
		AccountData accountdata = new AccountData(theAccount.getFirstName(), theAccount.getLastName(),
				theAccount.getProfilePhoto(), theAccount.getIdAccount(), theAccount.getCoverPhoto(),
				theAccount.getLivesIn(), theAccount.getEmail(), theAccount.getWentTo(), theAccount.getStudy());
		return accountdata;
	}
	
	@Override
	public List<String> getAccountPhotos(int accountId) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Post> theQuery = currentSession.createQuery("from Post where post_creator_id = " + accountId + " and status != " + 2, Post.class);
		List<Post> myPosts = theQuery.getResultList();
		List<String> myPhotos = new ArrayList<String>();
		Account theAccount = currentSession.get(Account.class, accountId);
		myPhotos.add(theAccount.getCoverPhoto());
		myPhotos.add(theAccount.getProfilePhoto());
		for(Post post:myPosts) {
			if(post.getImage() != null ) {
				myPhotos.add(post.getImage());
			}
		}	
		return myPhotos;
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
