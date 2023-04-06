package com.epam.esm.giftcertificates.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestFilePaths {

    public final String PATH_TO_GIFT_CERTIFICATE = "json/GiftCertificate.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_TAG_NAME = "json/GiftCertificateWithBlankTagName.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_NAME = "json/GiftCertificateWithBlankName.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_BLANK_DESCRIPTION =
        "json/GiftCertificateWithBlankDescription.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_PRICE_LESS_THAN_ZERO_DOT_ONE =
        "json/GiftCertificateWithPriceLessThanZeroDotZeroOne.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_PRICE_SCALE_MORE_THAN_TWO =
        "json/GiftCertificateWithPriceScaleMoreThanTwo.json";
    public final String PATH_TO_GIFT_CERTIFICATE_WITH_DURATION_LESS_THAN_ONE =
        "json/GiftCertificateWithDurationLessThanOne.json";
    public final String PATH_TO_GIFT_CERTIFICATE_SORTING_PARAMETERS =
        "json/GiftCertificateSortingParameters.json";
    public final String PATH_TO_TAG_NAMES = "json/TagNames.json";
    public final String PATH_TO_EMPTY_TAG_NAMES = "json/EmptyTagNames.json";
    public final String PATH_TO_UPDATE_GIFT_CERTIFICATE = "json/UpdateGiftCertificate.json";
    public final String PATH_TO_UPDATE_GIFT_CERTIFICATE_WITH_EMPTY_TAGS_SET =
        "json/UpdateGiftCertificateWithEmptyTagsSet.json";

    public final String PATH_TO_GIFT_CERTIFICATE_NAMES = "json/GiftCertificateNames.json";
    public final String PATH_TO_EMPTY_GIFT_CERTIFICATE_NAMES = "json/EmptyGiftCertificateNames.json";

    public final String PATH_TO_TAG = "json/Tag.json";
    public final String PATH_TO_TAG_WITH_BLANK_TAG_NAME = "json/TagWithBlankTagName.json";
}
