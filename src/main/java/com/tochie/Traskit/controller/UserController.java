package com.tochie.Traskit.controller;

import com.tochie.Traskit.dto.LoginDTO;
import com.tochie.Traskit.dto.SignUpDTO;
import com.tochie.Traskit.dto.apiresponse.BaseResponse;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.exception.ErrorResponse;
import com.tochie.Traskit.model.Role;
import com.tochie.Traskit.model.User;
import com.tochie.Traskit.repository.RoleRepository;
import com.tochie.Traskit.repository.UserRepository;
import com.tochie.Traskit.security.JwtService;
import com.tochie.Traskit.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    UserService userService;


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO){


        return new ResponseEntity<>(userService.loginUser(loginDTO), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDto){

        return new ResponseEntity<>(userService.addUser(signUpDto), HttpStatus.OK);

    }




//    @PostMapping("/generateToken")
//    public String authenticateAndGetToken(@RequestBody LoginDTO loginDTO) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(loginDTO.getUsernameOrEmail());
//        } else {
//            throw new UsernameNotFoundException("invalid user request !");
//        }
//    }


}
