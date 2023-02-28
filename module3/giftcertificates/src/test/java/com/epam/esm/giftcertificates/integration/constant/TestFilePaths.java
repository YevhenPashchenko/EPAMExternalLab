package com.epam.esm.giftcertificates.integration.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestFilePaths {

    public final String PATH_TO_INPUT_GIFT_CERTIFICATE = "json/input/GiftCertificate.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_TAG_NAME =
        "json/input/GiftCertificateWithBlankTagName.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_GIFT_CERTIFICATE_NAME =
        "json/input/GiftCertificateWithBlankGiftCertificateName.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_BLANK_GIFT_CERTIFICATE_DESCRIPTION =
        "json/input/GiftCertificateWithBlankGiftCertificateDescription.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_PRICE_LESS_THAN_ZERO_DOT_ONE =
        "json/input/GiftCertificateWithGiftCertificatePriceLessThanZeroDotOne.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_PRICE_SCALE_MORE_THAN_TWO =
        "json/input/GiftCertificateWithGiftCertificatePriceScaleMoreThanTwo.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_WITH_GIFT_CERTIFICATE_DURATION_LESS_THAN_ONE =
        "json/input/GiftCertificateWithGiftCertificateDurationLessThanOne.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_SORTING_PARAMETERS =
        "json/input/GiftCertificateSortingParameters.json";
    public final String PATH_TO_INPUT_GIFT_CERTIFICATE_FOR_UPDATE = "json/input/GiftCertificateForUpdate.json";
    public final String PATH_TO_INPUT_TAG = "json/input/Tag.json";
    public final String PATH_TO_INPUT_TAG_WITH_BLANK_NAME = "json/input/TagWithBlankName.json";
}
