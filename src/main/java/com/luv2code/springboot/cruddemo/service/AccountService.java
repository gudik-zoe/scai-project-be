package com.luv2code.springboot.cruddemo.service;


import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.utility.AccountBasicData;
import com.luv2code.springboot.cruddemo.utility.AccountData;
import com.luv2code.springboot.cruddemo.utility.ImageUrl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.util.List;



public interface AccountService {

	public List<Account> getAllUsers();

	public List<AccountBasicData> getPeopleYouMayKnow(int accountId);

	public List<AccountBasicData> getMyFriends(int accountId);

	public Account findById(int accountId);

	public AccountBasicData getLoggedInUserBasicData(int accountIdFromToken);

	public AccountData getLoggedInUserFullData(int accountIdFromToken);

	public boolean save(Account account) throws NotFoundException;

	public Account updateAccount(Account account, int accountId) throws AccountException;

	public void deleteById(int theId);

	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception;

	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo) throws Exception;

	public AccountBasicData getAccountBasicData(int accountId);

	public Account updateEmail(int accountId, String email, String password);

	public Account updatePassword(int accountId, String oldPassword, String newPassword);

	public ResponseEntity<Account> login(Account user);

	public List<String> getAccountPhotos(int accountId);

}
