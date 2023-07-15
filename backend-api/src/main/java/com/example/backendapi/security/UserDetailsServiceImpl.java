package com.example.backendapi.security;

import com.example.backendapi.domain.model.Owner;
import com.example.backendapi.domain.model.Role;
import com.example.backendapi.domain.repo.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private OwnerRepo ownerRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Load user data from ownerRepo or database
        Owner user = ownerRepo.findOwnerByOwnerEmail(username); // .orElseThrow()....

        // Create a list of GrantedAuthority objects based on the user's roles
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority(Role.ROLE_OWNER),
                new SimpleGrantedAuthority(Role.ROLE_ADMIN),
                new SimpleGrantedAuthority(Role.ROLE_USER)
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getOwnerEmail())
                .password(user.getPassword())
                .authorities(Role.ROLE_OWNER)
                .build();
    }
}
