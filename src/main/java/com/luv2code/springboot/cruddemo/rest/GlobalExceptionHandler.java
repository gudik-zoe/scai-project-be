package com.luv2code.springboot.cruddemo.rest;

import com.luv2code.springboot.cruddemo.exceptions.BadRequestException;
import com.luv2code.springboot.cruddemo.exceptions.ErrorResponse;
import com.luv2code.springboot.cruddemo.exceptions.NotFoundException;
import com.luv2code.springboot.cruddemo.exceptions.UnauthorisedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;





@ControllerAdvice
public class GlobalExceptionHandler {
	
      Logger logger = LoggerFactory.getLogger("com.luv2code.springboot.cruddemo.rest");

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),
                System.currentTimeMillis());
        System.out.println("passed from not found exception");
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exc.getMessage(),
                System.currentTimeMillis());
        System.out.println("passed from bad request");
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUnauthorisedException(UnauthorisedException exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), exc.getMessage(),
                System.currentTimeMillis());
        System.out.println("passed from unauthorised");
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exc) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.getMessage(),
                System.currentTimeMillis());
        System.out.println("passed from internal server error");
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
