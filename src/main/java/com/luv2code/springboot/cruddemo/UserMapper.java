package com.luv2code.springboot.cruddemo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.luv2code.springboot.cruddemo.entity.Account;

public class UserMapper implements RowMapper<Account> {

	@Override
	public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
		Account user = new Account();
		user.setIdAccount(rs.getInt("id_account"));
		user.setPassword(rs.getString("password"));
		user.setEmail(rs.getString("email"));
		return user;
	}

}
