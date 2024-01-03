package com.example.demo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ProblemDetailTemplate problemDetailTemplate;

    RestResponseEntityExceptionHandler(ProblemDetailTemplate problemDetailTemplate) {
        this.problemDetailTemplate = problemDetailTemplate;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.UNAUTHORIZED, exception.getMessage(), "Authentication Failure");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.FORBIDDEN, exception.getMessage(), "Not Authorised");
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.FORBIDDEN, exception.getMessage(), "Invalid JWT Signature");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.FORBIDDEN, exception.getMessage(), "Expired JWT");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.BAD_REQUEST, exception.getMessage(), "Data Integrity Violation");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUsernameNotFoundException(Exception exception, WebRequest request) {
        return problemDetailTemplate.template
                (HttpStatus.NOT_FOUND, exception.getMessage(), "Username not found.");
    }
}