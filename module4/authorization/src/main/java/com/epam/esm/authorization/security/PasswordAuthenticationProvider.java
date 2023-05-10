package com.epam.esm.authorization.security;

import com.epam.esm.authorization.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private final PersonService personService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var email = authentication.getName();
        var password = authentication.getCredentials().toString();
        var user = personService.loadUserByUsername(email);
        if (encoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Password is incorrect");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
