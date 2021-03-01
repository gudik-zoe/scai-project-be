package com.luv2code.springboot.cruddemo.utility;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncryptor {
	public String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	public boolean checkPass(String plainPassword, String hashedPassword) {
		if (BCrypt.checkpw(plainPassword, hashedPassword)) {
			return true;
		} else {
			return false;
		}
	}

}
