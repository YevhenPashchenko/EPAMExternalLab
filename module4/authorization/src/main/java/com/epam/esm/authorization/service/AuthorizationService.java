package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.AuthorizationCodeDto;
import com.epam.esm.authorization.dto.LoginDto;

/**
 * Provides method for authorization.
 *
 * @author Yevhen Pashchenko
 */
public interface AuthorizationService {

    AuthorizationCodeDto login(LoginDto login);
}
