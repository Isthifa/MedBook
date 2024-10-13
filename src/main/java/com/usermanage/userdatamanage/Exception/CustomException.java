package com.usermanage.userdatamanage.Exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class CustomException {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        ProblemDetail problemDetail = null;


        if(e instanceof PassNotValidException) {
             problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getMessage());
            problemDetail.setProperty("password", "Password is not valid");
        }



        if(e instanceof AccessDeniedException) {
             problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
            problemDetail.setProperty("access denied", "Access denied");
        }

        return problemDetail;

    }
}
