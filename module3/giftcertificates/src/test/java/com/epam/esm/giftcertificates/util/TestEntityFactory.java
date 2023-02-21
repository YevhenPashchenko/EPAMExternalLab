package com.epam.esm.giftcertificates.util;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Person;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.integration.constant.TestEntityFieldValues;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@UtilityClass
public class TestEntityFactory {

    public GiftCertificateDto createDefaultGiftCertificateDto() {
        var giftCertificate = new GiftCertificateDto();
        giftCertificate.setId(1L);
        giftCertificate.setName(TestEntityFieldValues.NAME);
        giftCertificate.setDescription(TestEntityFieldValues.DESCRIPTION);
        giftCertificate.setPrice(TestEntityFieldValues.PRICE);
        giftCertificate.setDuration(TestEntityFieldValues.DURATION);
        return giftCertificate;
    }

    public GiftCertificate createDefaultGiftCertificate() {
        var giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName(TestEntityFieldValues.NAME);
        giftCertificate.setDescription(TestEntityFieldValues.DESCRIPTION);
        giftCertificate.setPrice(TestEntityFieldValues.PRICE);
        giftCertificate.setDuration(TestEntityFieldValues.DURATION);
        return giftCertificate;
    }

    public GiftCertificate createGiftCertificate(String name, String description, BigDecimal price, int duration,
        Set<Tag> tags) {
        var giftCertificate = new GiftCertificate();
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

    public PersonDto createDefaultPersonDto() {
        var person = new PersonDto();
        person.setId(1L);
        person.setEmail(TestEntityFieldValues.EMAIL);
        person.setPassword(TestEntityFieldValues.PASSWORD);
        return person;
    }

    public Person createDefaultPerson() {
        var person = new Person();
        person.setId(1L);
        person.setEmail(TestEntityFieldValues.EMAIL);
        person.setPassword(TestEntityFieldValues.PASSWORD);
        return person;
    }

    public Person createPerson(String email, String password) {
        var person = new Person();
        person.setEmail(email);
        person.setPassword(password);
        return person;
    }

    public ReceiptDto createDefaultReceiptDto() {
        var receipt = new ReceiptDto();
        receipt.setId(1L);
        receipt.setTotalCost(BigDecimal.ONE);
        return receipt;
    }

    public Receipt createDefaultReceipt() {
        var receipt = new Receipt();
        receipt.setId(1L);
        receipt.setTotalCost(BigDecimal.ONE);
        return receipt;
    }

    public Receipt createReceipt(BigDecimal totalCost, Person person, List<GiftCertificate> giftCertificates) {
        var receipt = new Receipt();
        receipt.setTotalCost(totalCost);
        receipt.setPerson(person);
        receipt.setGiftCertificates(giftCertificates);
        return receipt;
    }

    public TagDto createDefaultTagDto() {
        var tag = new TagDto();
        tag.setId(1L);
        tag.setName(TestEntityFieldValues.FIRST_TAG_NAME);
        return tag;
    }

    public Tag createDefaultTag() {
        var tag = new Tag();
        tag.setId(1L);
        tag.setName(TestEntityFieldValues.FIRST_TAG_NAME);
        return tag;
    }

    public Tag createTag(String tagName) {
        var tag = new Tag();
        tag.setName(tagName);
        return tag;
    }
}
