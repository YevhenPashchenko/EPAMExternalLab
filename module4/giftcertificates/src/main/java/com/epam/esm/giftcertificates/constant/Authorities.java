package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Authorities {

    public final String ADMIN_ROLE = "ROLE_admin";
    public final String USER_ROLE = "ROLE_user";

    public final String GIFT_CERTIFICATES_READ = "SCOPE_gift-certificates.read";
    public final String GIFT_CERTIFICATES_WRITE = "SCOPE_gift-certificates.write";

    public final String RECEIPTS_READ = "SCOPE_receipts.read";
    public final String RECEIPTS_WRITE = "SCOPE_receipts.write";

    public final String TAGS_READ = "SCOPE_tags.read";
    public final String TAGS_WRITE = "SCOPE_tags.write";
}
