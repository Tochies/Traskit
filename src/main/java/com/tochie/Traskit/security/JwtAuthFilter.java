package com.tochie.Traskit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tochie.Traskit.enums.ResponseCodeEnum;
import com.tochie.Traskit.exception.ErrorResponse;
import com.tochie.Traskit.exception.GenericException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, GenericException {

        String token = jwtService.resolveToken(request);

        if (token != null && jwtService.validateToken(token)) {
            Authentication auth = jwtService.getAuthentication(response, request);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } else {

            SecurityContextHolder.clearContext();

        }


        filterChain.doFilter(request, response);
    }

    private void handleJWTException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(ResponseCodeEnum.FAILED_AUTHENTICATION.getCode());
        errorResponse.setDescription("Failed authentication");

        PrintWriter writer = response.getWriter();
        new ObjectMapper().writeValue(writer, errorResponse);
        writer.flush();
    }

}
