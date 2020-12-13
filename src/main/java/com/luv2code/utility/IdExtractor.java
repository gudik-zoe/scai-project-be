package com.luv2code.utility;

import io.jsonwebtoken.Jwts;

public class IdExtractor {

	private String token;

	public IdExtractor(String token) {
		this.token = token;
	}

	public IdExtractor() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIdFromToken() {
		String token = getToken().replace("Bearer ", "");
		Integer idAccount = (Integer) Jwts.parser().setSigningKey("ciao").parseClaimsJws(token).getBody().get("userid");
		return idAccount;
	}

}
