package com.tochie.Traskit.exception;

import com.tochie.Traskit.enums.Constants;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(value = {AuthException.class})
    public ResponseEntity<Object> handleGenericException(AuthException e){

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withDescription(e.getMessage())
                .withResponseCode(ResponseCodeEnum.FAILED_AUTHENTICATION.getCode())
                .build();
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e){

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withDescription("Username/password invalid")
                .withResponseCode(ResponseCodeEnum.FAILED_AUTHENTICATION.getCode())
                .build();
        return new ResponseEntity<>(errorResponse, badRequest);
    }



    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e){

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withDescription(e.getMessage())
                .withResponseCode(ResponseCodeEnum.SERVICE_FAILURE.getCode())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<Object> handleCustomException(CustomException e){

        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withErrorCategory(e.getErrorCategory()).withDescription(e.getMessage())
                .withResponseCode(e.getCode())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getAllErrors();


        String errorMessage = !allErrors.isEmpty() ? allErrors.get(0).getDefaultMessage() :
                "An error occurred while processing request. Please check your request data";


        ErrorResponse errorResponse = ErrorResponse.ErrorResponseBuilder.anErrorResponse()
                .withErrorCategory(Constants.VALIDATION_ERROR).withDescription(errorMessage)
                .withResponseCode(ResponseCodeEnum.VALIDATION_ERROR.getCode())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
