package com.example.demo.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private ProblemDetailTemplate problemDetailTemplate;

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

}