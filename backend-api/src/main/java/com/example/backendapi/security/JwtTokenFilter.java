package com.example.backendapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//
//@Component
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            //Exract JWT token from the request header
//            String jwtToken = extractJwt(request);
//            if (jwtToken != null && jwtTokenProvider.validateToken(jwtToken)) {
//                //Token is valid, set the authentication in the security context
//                Authentication authentication = jwtTokenProvider.getAu;
//            }
//        }
//    }
//
//    private String extractJwt(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//

//}
