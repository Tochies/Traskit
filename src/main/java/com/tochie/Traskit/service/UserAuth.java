package com.tochie.Traskit.service;

import com.tochie.Traskit.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuth {

    @Autowired
    private JwtService jwtService;

    @Autowired
    HttpServletResponse response;


    public void authenticateUser(String username){
        String jwt = "Bearer ".concat(jwtService.generateToken(username));
        response.addHeader("Authorization" , jwt);
    }


}
