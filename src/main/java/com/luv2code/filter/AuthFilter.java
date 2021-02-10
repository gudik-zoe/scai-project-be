package com.luv2code.filter;

import com.luv2code.exception.error.handling.CustomeException;
import com.luv2code.exception.error.handling.ErrorResponse;
import com.luv2code.springboot.cruddemo.rest.AccountRestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(AccountRestController.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException,CustomeException , ServletException
		 {
		// ignore OPTION method calls
		if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
			chain.doFilter(request, response);
			return;
		}

		// ignore all permitted routes
		if (request.getRequestURI().startsWith("/files") || "/api/login".equalsIgnoreCase(request.getRequestURI())
				|| "/api/signUp".equalsIgnoreCase(request.getRequestURI())
				|| request.getRequestURI().startsWith("/chat") || request.getRequestURI().startsWith("/") ) {
			chain.doFilter(request, response);
			return;
		}

		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			throw new CustomeException("token not found");
		}
		String token = header.replace("Bearer ", "");
		try {
			Jwts.parser().setSigningKey("ciao").parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException ex){
        	 request.setAttribute("expired", ex.getMessage());
        	 final String expiredMsg = (String) request.getAttribute("expired");
        	 final String msg = (expiredMsg != null) ? expiredMsg : "Unauthorized";
        	 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
        }
		chain.doFilter(request, response);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleCustomeException(CustomeException exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handlePostException(Exception exc) {
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "unknown error occured",
				System.currentTimeMillis());
		logger.error(error.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

}
