package com.epam.esm.authorization.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestFilePaths {

    public static final String PATH_TO_LOGIN = "json/authorization/Login.json";
    public static final String PATH_TO_LOGIN_WITH_WRONG_CLIENT_ID = "json/authorization/LoginWithWrongClientId.json";
    public static final String PATH_TO_LOGIN_WITH_CLIENT_ID_THAT_NOT_VALID =
        "json/authorization/LoginWithClientIdThatNotValid.json";
    public static final String PATH_TO_LOGIN_WITH_WRONG_EMAIL = "json/authorization/LoginWithWrongEmail.json";
    public static final String PATH_TO_LOGIN_WITH_EMAIL_THAT_NOT_VALID =
        "json/authorization/LoginWithEmailThatNotValid.json";
    public static final String PATH_TO_LOGIN_WITH_INCORRECT_PASSWORD =
        "json/authorization/LoginWithIncorrectPassword.json";
    public static final String PATH_TO_LOGIN_WITH_PASSWORD_THAT_NOT_VALID =
        "json/authorization/LoginWithPasswordThatNotValid.json";
    public static final String PATH_TO_LOGIN_WITH_NULL_REDIRECT_URI =
        "json/authorization/LoginWithNullRedirectUri.json";
    public static final String PATH_TO_LOGIN_WITH_INCORRECT_SCOPE_VALUE =
        "json/authorization/LoginWithIncorrectScopeValue.json";

    public static final String PATH_TO_CREATE_CLIENT = "json/client/CreateClient.json";
    public static final String PATH_TO_CREATE_CLIENT_WITH_CLIENT_ID_THAT_NOT_VALID =
        "json/client/CreateClientWithClientIdThatNotValid.json";
    public static final String PATH_TO_CREATE_CLIENT_WITH_CLIENT_SECRET_THAT_NOT_VALID =
        "json/client/CreateClientWithClientSecretThatNotValid.json";
    public static final String PATH_TO_CREATE_CLIENT_WITH_SCOPES_THAT_IS_NULL =
        "json/client/CreateClientWithScopesThatIsNull.json";
    public static final String PATH_TO_UPDATE_CLIENT = "json/client/UpdateClient.json";

    public static final String PATH_TO_CREATE_PERSON = "json/person/CreatePerson.json";
    public static final String PATH_TO_CREATE_PERSON_WITH_NOT_VALID_EMAIL =
        "json/person/CreatePersonWithNotValidEmail.json";
    public static final String PATH_TO_CREATE_PERSON_WITH_NOT_VALID_PASSWORD =
        "json/person/CreatePersonWithNotValidPassword.json";
    public static final String PATH_TO_PERSON_EMAIL = "json/person/PersonEmail.json";
    public static final String PATH_TO_PERSON_EMAIL_WITH_NOT_VALID_EMAIL =
        "json/person/PersonEmailWithNotValidEmail.json";
    public static final String PATH_TO_UPDATE_PERSON = "json/person/UpdatePerson.json";
    public static final String PATH_TO_UPDATE_PERSON_WITH_NOT_VALID_EMAIL =
        "json/person/UpdatePersonWithNotValidEmail.json";
    public static final String PATH_TO_UPDATE_PERSON_WITH_ENABLED_IS_NULL =
        "json/person/UpdatePersonWithEnabledIsNull.json";
    public static final String PATH_TO_UPDATE_PERSON_WITH_AUTHORITIES_IS_NULL =
        "json/person/UpdatePersonWithAuthoritiesIsNull.json";
    public static final String PATH_TO_CHANGE_PERSON_PASSWORD = "json/person/ChangePersonPassword.json";
    public static final String PATH_TO_CHANGE_PERSON_PASSWORD_WITH_NOT_VALID_OLD_PASSWORD =
        "json/person/ChangePersonPasswordWithNotValidOldPassword.json";
    public static final String PATH_TO_CHANGE_PERSON_PASSWORD_WITH_NOT_VALID_NEW_PASSWORD =
        "json/person/ChangePersonPasswordWithNotValidNewPassword.json";
}
