package com.example.backendapi.domain.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Data
@Setter
@Getter
@Table(name = "owner")
public class Owner implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    //@Value("${my.property.username}")
    private String ownerUserName;
    @Column(nullable = false)
    //@Value("${my.property.name}")
    private String ownerName;
    @Column(nullable = false, unique = true)
    //@Value("${my.property.email}")
    private String ownerEmail;
    @Column(nullable = false)
    //@Value("${my.property.pass}")
    private String password;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    public enum Role {
        OWNER
    }

    public Owner() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_OWNER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Override
    public String getUsername() {
        return ownerEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
