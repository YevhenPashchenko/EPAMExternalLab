package com.epam.esm.authorization.service;

import com.epam.esm.authorization.dto.AuthorizationCodeDto;
import com.epam.esm.authorization.dto.LoginDto;
import com.epam.esm.authorization.entity.Client;

/**
 * Provides method for authorization.
 *
 * @author Yevhen Pashchenko
 */
public interface AuthorizationService {

    /**
     * Returns {@link AuthorizationCodeDto} object for {@link Client} from given {@code clientId}.
     *
     * @param clientId {@code clientId}.
     * @return {@link AuthorizationCodeDto} object.
     */
    AuthorizationCodeDto getTokenForLogin(String clientId);

    /**
     * Returns {@link AuthorizationCodeDto} object from given {@link LoginDto} object.
     *
     * @param login {@link LoginDto} object.
     * @return {@link AuthorizationCodeDto} object.
     */
    AuthorizationCodeDto login(LoginDto login);
}
