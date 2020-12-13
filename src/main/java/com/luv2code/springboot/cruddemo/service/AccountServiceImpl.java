package com.luv2code.springboot.cruddemo.service;

import java.util.List;

import javax.security.auth.login.AccountException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.springboot.cruddemo.dao.AccountDAO;
import com.luv2code.springboot.cruddemo.entity.Account;
import com.luv2code.utility.AccountBasicData;
import com.luv2code.utility.AccountData;
import com.luv2code.utility.ImageUrl;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountDAO accountDAO;

	@Autowired
	public AccountServiceImpl(AccountDAO theAccountDAO) {
		accountDAO = theAccountDAO;
	}

	@Override
	@Transactional
	public List<AccountBasicData> getPeopleYouMayKnow(int accountId) {
		return accountDAO.getPeopleYouMayKnow(accountId);
	}

	@Override
	@Transactional
	public AccountData findById(int accountId) {
		return accountDAO.findById(accountId);
	}

	@Override 
	@Transactional
	public Account save(Account account) throws CustomeException {
		return accountDAO.save(account);

	}

	@Override
	@Transactional
	public void deleteById(int theId) {
		accountDAO.deleteById(theId);

	}

	@Override
	@Transactional
	public ImageUrl updateAccountProfilePhoto(int accountId, MultipartFile photo) throws Exception {
		return accountDAO.updateAccountProfilePhoto(accountId, photo);

	}

	@Override
	@Transactional
	public AccountBasicData getAccountBasicData(int accountId) {
		return accountDAO.getAccountBasicData(accountId);
	}

	@Override
	@Transactional
	public ImageUrl updateAccountCoverPhoto(int accountId, MultipartFile photo) throws Exception {
		 return accountDAO.updateAccountCoverPhoto(accountId , photo);
		
	}

	@Override
	@Transactional
	public int getAccountByPostId(int postId) {
		return  accountDAO.getAccountByPostId(postId);
	}

	@Override
	@Transactional
	public boolean checkIfEmailIsNotTaken(String email) {
		return accountDAO.checkIfEmailIsNotTaken(email);
	}

	@Override
	@Transactional
	public Account updateAccount(Account account , int idAccount)throws AccountException {
		return accountDAO.updateAccount(account , idAccount);
	}

	@Override
	@Transactional
	public AccountBasicData getLoggedInUserBasicData(int accountIdFromToken) {
		return accountDAO.getTheLoggedInUser(accountIdFromToken);
	}

	@Override
	@Transactional
	public Account updateEmail(int accountId ,String email , String password) {
		return accountDAO.updateEmail(accountId , email , password);
	}

	@Override
	@Transactional
	public Account updatePassword(int accountId , String oldPassword , String newPassword) {
		return accountDAO.updatePassword(accountId , oldPassword , newPassword);
	}


	@Override
	@Transactional
	public AccountData getLoggedInUserFullData(int accountIdFromToken) {
		return accountDAO.getLoggedInUserFullData(accountIdFromToken);
	}

	@Override
	@Transactional
	public List<AccountBasicData> getMyFriends(int accountId) {
		return accountDAO.getMyFriends(accountId);
	}

	@Override
	@Transactional
	public ResponseEntity<Account> login(Account user) {
		return accountDAO.login(user);
	}

	@Override
	@Transactional
	public List<AccountBasicData> getAllUsers() {
		return accountDAO.getAllUsers();
	}


}
