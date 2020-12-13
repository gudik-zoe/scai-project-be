package com.luv2code.springboot.cruddemo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.entity.Account;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public Account getUserAuth(String email, String pass) throws CustomeException {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		Account authUser;
		try {
			authUser = jdbcTemplate.queryForObject("SELECT * from account where email = ?", new Object[] { email },
					new UserMapper());
		} catch (CustomeException e) {
			throw new CustomeException("invalid credentials");
		}

		boolean check = encoder.matches(pass, authUser.getPassword());

		if (authUser == null || !check) {
			throw new CustomeException("invalid credentials");
		} else {
			return authUser;
		}

	}
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleGenericException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "unknown error occured",
				System.currentTimeMillis());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}

}