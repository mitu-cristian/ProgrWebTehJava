package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class ProblemDetailTemplate {
    ProblemDetail template(HttpStatus status, String exceptionMessage, String propertyMessage) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, exceptionMessage);
        problemDetail.setProperty("access_denied_reason", propertyMessage);
        return problemDetail;
    }
}
