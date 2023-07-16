//package com.example.backendapi.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthProvider implements AuthenticationProvider {
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
//            System.out.println("Authenticated.....1.2.3");
//            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
//        } else {
//            throw new BadCredentialsException("Invalid username or password");
//        }
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class); // same method is below
//        //return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
