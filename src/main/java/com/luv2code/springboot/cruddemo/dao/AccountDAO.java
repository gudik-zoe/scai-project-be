package com.luv2code.springboot.cruddemo.dao;

import java.util.List;

import javax.security.auth.login.AccountException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.ImageUrl;

public interface AccountDAO {

	public List<AccountBasicData> getPeopleYouMayKnow(int accountId);

	public AccountData findById(int accountId);

	public Account save(Account account) throws CustomeException;

	public void deleteById(int theId);

	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception;



	public AccountBasicData getAccountBasicData(int accountId);

	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo)throws Exception;

	public int getAccountByPostId(int postId);

	public boolean checkIfEmailIsNotTaken(String email);

	public Account updateAccount(Account account , int idAccount) throws AccountException;

	public AccountBasicData getTheLoggedInUser(int accountIdFromToken);

	public Account updateEmail(int accountId , String email , String password);

	public Account updatePassword(int accountId , String oldPassword , String newPassword);

	public AccountData getLoggedInUserFullData(int accountIdFromToken);

	public List<AccountBasicData> getMyFriends(int accountId);

	public ResponseEntity<Account> login(Account user);

	public List<AccountBasicData> getAllUsers();

	public List<String> getAccountPhotos(int accountId);


}
