package com.tochie.Traskit.controller;

import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.exception.ErrorResponse;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    UserDetails username;


    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }


    @GetMapping("/admin/adminProfile")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> adminProfile() {

        ResponseEntity<BaseResponse> jwtSession = validatedUserSession();
        if (jwtSession != null) return jwtSession;

        BaseResponse response = new ResponseData();
        response.setCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setDescription("Welcome to Admin Profile, " + username.getUsername());
      return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private ResponseEntity<BaseResponse> validatedUserSession() {
        BaseResponse response;
        if(request.getAttribute("user") == null){
            response = ErrorResponse.ErrorResponseBuilder.anErrorResponse().build(ResponseCodeEnum.FAILED_AUTHENTICATION);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else {
            username = (UserDetails) request.getAttribute("user");
        }
        return null;
    }


}
