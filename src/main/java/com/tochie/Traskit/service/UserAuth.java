package com.tochie.Traskit.service;

import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.exception.ErrorResponse;
import com.tochie.Traskit.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserAuth {

    @Autowired
    private JwtService jwtService;

    @Autowired
    HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    public UserDetails userDetails;


    public void authenticateUser(String username){
        String jwt = "Bearer ".concat(jwtService.generateToken(username));
        response.addHeader("Authorization" , jwt);
    }

    public ResponseEntity<BaseResponse> validatedUserSession() {
        BaseResponse response;
        if(request.getAttribute("user") == null){
            response = ErrorResponse.ErrorResponseBuilder.anErrorResponse().build(ResponseCodeEnum.FAILED_AUTHENTICATION);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
            userDetails = (UserDetails) request.getAttribute("user");
        }
        return null;
    }


}
