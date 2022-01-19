package com.example.security;

import com.example.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class UserDatailsImpl implements UserDetails {

    private Account account;

    public UserDatailsImpl(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("USER");
        return Arrays.asList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return account.getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return account.getEmail();
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
