package com.epam.esm.authorization.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {

    public final String ADMIN_ROLE = "ROLE_admin";

    public final String PERSONS_READ = "SCOPE_persons.read";
    public final String PERSONS_WRITE = "SCOPE_persons.write";

    public final String CLIENTS_READ = "SCOPE_clients.read";
    public final String CLIENTS_WRITE = "SCOPE_clients.write";
}
