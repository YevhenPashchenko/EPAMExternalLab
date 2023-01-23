package com.epam.esm.giftcertificates.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GiftCertificateDaoConstants {

    public final String CREATE_GIFT_CERTIFICATE_QUERY =
            "INSERT INTO gift_certificates.gift_certificate VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    public final String SELECT_ALL_GIFT_CERTIFICATES_ON_PAGE_QUERY =
            "SELECT * FROM gift_certificates.gift_certificate OFFSET ? LIMIT ?";
    public final String SELECT_GIFT_CERTIFICATE_BY_ID_QUERY =
            "SELECT * FROM gift_certificates.gift_certificate WHERE id = ?";
    public final String UPDATE_GIFT_CERTIFICATE_NAME_QUERY =
            "UPDATE gift_certificates.gift_certificate SET name = ?, last_update_date = ? WHERE id = ?";
    public final String UPDATE_GIFT_CERTIFICATE_DESCRIPTION_QUERY =
            "UPDATE gift_certificates.gift_certificate SET description = ?, last_update_date = ? WHERE id = ?";
    public final String UPDATE_GIFT_CERTIFICATE_PRICE_QUERY =
            "UPDATE gift_certificates.gift_certificate SET price = ?, last_update_date = ? WHERE id = ?";
    public final String UPDATE_GIFT_CERTIFICATE_DURATION_QUERY =
            "UPDATE gift_certificates.gift_certificate SET duration = ?, last_update_date = ? WHERE id = ?";
    public final String DELETE_GIFT_CERTIFICATE_QUERY = "DELETE FROM gift_certificates.gift_certificate WHERE id = ?";
}
