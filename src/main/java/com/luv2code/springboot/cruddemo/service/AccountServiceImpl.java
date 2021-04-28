package com.luv2code.springboot.cruddemo.service;

import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.entity.Profile;
import com.luv2code.springboot.cruddemo.entity.Relationship;
import com.luv2code.springboot.cruddemo.exceptions.BadRequestException;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.jpa.AccountJpaRepo;
import com.luv2code.springboot.cruddemo.jpa.ProfileJpaRepo;
import com.luv2code.springboot.cruddemo.mappers.AccountDataMapper;
import com.luv2code.springboot.cruddemo.mappers.AccountDataMapperImpl;
import com.luv2code.springboot.cruddemo.mappers.AccountMapper;
import com.luv2code.springboot.cruddemo.mappers.AccountMapperImpl;
import com.luv2code.springboot.cruddemo.dto.AccountBasicData;
import com.luv2code.springboot.cruddemo.dto.AccountData;
import com.luv2code.springboot.cruddemo.dto.ImageUrl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Autowired
	private ProfileJpaRepo profileJpaRepo;

	private static AccountMapper  accountBasicDataMapper = new AccountMapperImpl();

	private static AccountDataMapper accountDataMapper = new AccountDataMapperImpl();

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
				AccountBasicData personYouMayKnow = accountBasicDataMapper.toAccountBasicData(account);
				peopleYouMayKnow.add(personYouMayKnow);
			}
		}
		return peopleYouMayKnow;
	}

	@Override
	public boolean save(Account account) throws NotFoundException {
		String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher mat = emailPattern.matcher(account.getEmail());
		if (!mat.matches()) {
			throw new NotFoundException("enter a valid email");
		} else if (!account.getPassword().matches(passwordPattern)) {
			throw new NotFoundException(
					"a valid password password should contains at least one letter in upperCase + one digit + one alphanumeric character -/*?");
		} else if (accountRepoJpa.findByEmail(account.getEmail()) == null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			account.setPassword(encoder.encode(account.getPassword()));
			 accountRepoJpa.save(account);
			 profileJpaRepo.save(new Profile(account.getIdAccount()));
			 return true;
		} else {
			throw new NotFoundException("email already exist");
		}

	}

	@Override
	public void deleteById(int theId) {
		profileJpaRepo.deleteById(theId);
	}

	@Override
	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception {
		ImageUrl imgUrl = storageService.pushImage(photo);
		if (imgUrl == null) {
			throw new NotFoundException("no image was provided");
		} else {
			Account theAccount = findById(accountId);
			theAccount.setProfilePhoto(imgUrl.getImageUrl());
			accountRepoJpa.save(theAccount);
			return imgUrl;
		}
	}

	@Override
	public AccountBasicData getAccountBasicData(int accountId) {
		Account account = findById(accountId);
		return accountBasicDataMapper.toAccountBasicData(account);

	}

	@Override
	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo) throws Exception {
		ImageUrl imgUrl = storageService.pushImage(photo);
		if (imgUrl == null) {
			throw new NotFoundException("no image was provided");
		} else {
			Account theAccount = findById(accountId);
			theAccount.setCoverPhoto(imgUrl.getImageUrl());
			accountRepoJpa.save(theAccount);
			return imgUrl;
		}

	}

	@Override
	public Account updateAccount(Account account, int accountId) throws AccountException {
		account.setIdAccount(accountId);
		return accountRepoJpa.save(account);
	}

	@Override
	public AccountBasicData getLoggedInUserBasicData(int accountIdFromToken) {
		Account account = findById(accountIdFromToken);
	return accountBasicDataMapper.toAccountBasicData(account);
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
			throw new NotFoundException("enter a valid email or this email already exist");
		}
	}

	@Override
	public Account updatePassword(int accountId, String oldPassword, String newPassword) {
		String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
		if (!newPassword.matches(passwordPattern)) {
			throw new NotFoundException(
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
				throw new NotFoundException("incorrect password or u didn't make any changes to ur currentPassword");
			}
		}

	}

	@Override
	public AccountData getLoggedInUserFullData(int accountIdFromToken) {
		Account account = findById(accountIdFromToken);
		return accountDataMapper.toAccountData(account);
	}

	@Override
	public List<AccountBasicData> getMyFriends(int accountId) {
		List<Relationship> relationships = relationshipService.getMyFriends(accountId);
		List<AccountBasicData> friends = new ArrayList<AccountBasicData>();
		Account theAccount = null;
		for (Relationship relationship : relationships) {
			 theAccount = findById(relationship.getUserOneId() == accountId ? relationship.getUserTwoId()
					 : relationship.getUserOneId());
				friends.add(accountBasicDataMapper.toAccountBasicData(theAccount));
		}
		return friends;
	}

	@Override
	public ResponseEntity<Account> login(Account user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Account theAccount = findByEmail(user.getEmail());
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
			throw new BadRequestException("invalid credentials");
		}

	}

	public Account findByEmail(String email) {
		Account theAccount = accountRepoJpa.findByEmail(email);
		if (theAccount == null) {
			throw new BadRequestException("invalid credentials");
		}
		return theAccount;
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
	public boolean checkTempPassword(String password) {
			Account theAccount = accountRepoJpa.getAccountByTempPassword(password);
			if(theAccount != null){
				System.out.println(new Date(System.currentTimeMillis()).getMinutes() - theAccount.getTemporaryPasswordExpiryDate().getMinutes());
				return true;
			}
		return false;
	}

	@Override
	public Account findById(int accountId) throws NotFoundException{
		Optional<Account> result = accountRepoJpa.findById(accountId);
		Account theAccount = null;
		if (result.isPresent()) {
			theAccount = result.get();
		} else {
			throw new NotFoundException("no such id for an account");
		}
		return theAccount;
	}

	public Profile getProfileById(int accountId) throws NotFoundException{
		Optional<Profile> result = profileJpaRepo.findById(accountId);
		Profile theAccount = null;
		if (result.isPresent()) {
			theAccount = result.get();
		} else {
			throw new NotFoundException("no such id for an account");
		}
		return theAccount;
	}

}
