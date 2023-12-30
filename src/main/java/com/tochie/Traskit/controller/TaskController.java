package com.tochie.Traskit.controller;

import com.tochie.Traskit.dto.TaskCreationDTO;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.exception.ErrorResponse;
import com.tochie.Traskit.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    UserDetails userDetails;

    @Autowired
    TaskService taskService;


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
        response.setDescription("Welcome to Admin Profile, " + userDetails.getUsername());
      return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    @ResponseBody
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskCreationDTO taskCreationDTO) {

        ResponseEntity<BaseResponse> jwtSession = validatedUserSession();
        if (jwtSession != null) return jwtSession;


        return new ResponseEntity<>(taskService.createTask(taskCreationDTO, userDetails), HttpStatus.OK);
    }



    private ResponseEntity<BaseResponse> validatedUserSession() {
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
