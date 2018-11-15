package com.example.demo.security;

import com.example.demo.service.NtUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NtUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private NtUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.debug(">> NtUserDetailsAuthenticationProvider.additionalAuthenticationChecks userDetails={}, authentication={}", userDetails, authentication);
        if (authentication.getCredentials() == null || userDetails.getPassword() == null) {
            throw new BadCredentialsException("Credentials may not be null.");
        }
        if (!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        log.debug("<< NtUserDetailsAuthenticationProvider.additionalAuthenticationChecks");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.debug(">> NtUserDetailsAuthenticationProvider.retrieveUser username={}, authentication={}", username, authentication);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        log.debug("< NtUserDetailsAuthenticationProvider.retrieveUser userDetails={}", userDetails);
        return userDetails;
    }
}
