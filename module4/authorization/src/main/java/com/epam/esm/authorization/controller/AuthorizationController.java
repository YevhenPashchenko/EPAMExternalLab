package com.epam.esm.authorization.controller;

import com.epam.esm.authorization.dto.AuthorizationCodeDto;
import com.epam.esm.authorization.dto.LoginDto;
import com.epam.esm.authorization.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorizationCodeDto login(@Valid @RequestBody LoginDto login) {
        return authorizationService.login(login);
    }
}
