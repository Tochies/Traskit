package com.tochie.Traskit.service;

import com.tochie.Traskit.dto.LoginDTO;
import com.tochie.Traskit.dto.SignUpDTO;
import com.tochie.Traskit.dto.apiresponse.ResponseData;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.model.Role;
import com.tochie.Traskit.model.User;
import com.tochie.Traskit.pojo.UserObject;
import com.tochie.Traskit.repository.RoleRepository;
import com.tochie.Traskit.repository.UserRepository;
import com.tochie.Traskit.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAuth userAuth;


    public ResponseData addUser(SignUpDTO signUpDto){

        ResponseData responseData = new ResponseData();
        responseData.assignResponseCode(ResponseCodeEnum.INVALID_REQUEST);


        // add check for username exists in a DB
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            responseData.assignResponseCode(ResponseCodeEnum.FAILED_AUTHENTICATION.getCode(), "Username is already taken!");
            return responseData;
        }

        // add check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            responseData.assignResponseCode(ResponseCodeEnum.FAILED_AUTHENTICATION.getCode(), "Email is already taken!");
            return responseData;
        }

        createNewUser(signUpDto);

        responseData.assignResponseCode(ResponseCodeEnum.SUCCESS.getCode(), "User registered successfully");

        return responseData;
    }

    private void createNewUser(SignUpDTO signUpDto) {
        // create user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
    }


    public ResponseData loginUser(LoginDTO loginDTO){

        ResponseData responseData = new ResponseData();
        responseData.assignResponseCode(ResponseCodeEnum.INVALID_REQUEST);

        UserDetails userDetails ;

        try {
            userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsernameOrEmail());
        } catch (UsernameNotFoundException e){

            responseData.assignResponseCode(ResponseCodeEnum.USER_NOT_FOUND);
            return responseData;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userAuth.authenticateUser(loginDTO.getUsernameOrEmail());

        responseData.assignResponseCode(ResponseCodeEnum.SUCCESS.getCode(), "User log in successful");

        return responseData;
    }


}
