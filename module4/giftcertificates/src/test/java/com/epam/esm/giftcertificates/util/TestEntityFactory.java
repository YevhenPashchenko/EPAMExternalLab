package com.epam.esm.giftcertificates.util;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateNamesDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.handler.exception.ReceiptTotalCostCalculationException;
import com.epam.esm.giftcertificates.integration.constant.TestFilePaths;
import com.epam.esm.giftcertificates.integration.reader.JsonReader;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class TestEntityFactory {

    private final String GIFT_CERTIFICATE_NAME = "name1";
    private final String GIFT_CERTIFICATE_DESCRIPTION = "description1";
    private final BigDecimal PRICE = BigDecimal.valueOf(250.50);
    private final Integer DURATION = 15;
    private final JsonReader jsonReader = new JsonReader();

    public GiftCertificateDto createDefaultGiftCertificateDto() {
        var giftCertificate = new GiftCertificateDto();
        giftCertificate.setName(GIFT_CERTIFICATE_NAME);
        giftCertificate.setDescription(GIFT_CERTIFICATE_DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        return giftCertificate;
    }

    public GiftCertificate createDefaultGiftCertificate() {
        var giftCertificate = new GiftCertificate();
        giftCertificate.setName(GIFT_CERTIFICATE_NAME);
        giftCertificate.setDescription(GIFT_CERTIFICATE_DESCRIPTION);
        giftCertificate.setPrice(PRICE);
        giftCertificate.setDuration(DURATION);
        return giftCertificate;
    }

    public GiftCertificateSortingParametersDto createDefaultGiftCertificateSortingParametersDto() {
        var giftCertificate = new GiftCertificateSortingParametersDto();
        try {
            giftCertificate.setSortBy(
                jsonReader.readJsonFile(TestFilePaths.PATH_TO_GIFT_CERTIFICATE_SORTING_PARAMETERS).get("sort-by"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return giftCertificate;
    }

    public GiftCertificateNamesDto createDefaultGiftCertificateNamesDto(List<String> giftCertificateNames) {
        var names = new GiftCertificateNamesDto();
        names.setGiftCertificateNames(giftCertificateNames);
        return names;
    }

    public Receipt createDefaultReceipt(List<GiftCertificate> giftCertificates, String email) {
        var receipt = new Receipt();
        receipt.setGiftCertificates(giftCertificates);
        receipt.setTotalCost(receipt.getGiftCertificates()
            .stream()
            .map(GiftCertificate::getPrice)
            .reduce(BigDecimal::add)
            .orElseThrow(ReceiptTotalCostCalculationException::new));
        receipt.setEmail(email);
        return receipt;
    }

    public ReceiptDto createDefaultReceiptDto(List<GiftCertificateDto> giftCertificates) {
        var receipt = new ReceiptDto();
        receipt.setGiftCertificates(giftCertificates);
        receipt.setTotalCost(receipt.getGiftCertificates()
            .stream()
            .map(GiftCertificateDto::getPrice)
            .reduce(BigDecimal::add)
            .orElseThrow(ReceiptTotalCostCalculationException::new));
        return receipt;
    }

    public TagDto createDefaultTagDto(String name) {
        var tag = new TagDto();
        tag.setName(name);
        return tag;
    }

    public Tag createDefaultTag(String name) {
        var tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
