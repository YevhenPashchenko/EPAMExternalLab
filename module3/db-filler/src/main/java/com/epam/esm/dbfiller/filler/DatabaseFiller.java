package com.epam.esm.dbfiller.filler;

import com.epam.esm.dbfiller.entity.GiftCertificate;
import com.epam.esm.dbfiller.entity.Person;
import com.epam.esm.dbfiller.entity.Receipt;
import com.epam.esm.dbfiller.entity.Tag;
import com.epam.esm.dbfiller.service.GiftCertificateService;
import com.epam.esm.dbfiller.service.PersonService;
import com.epam.esm.dbfiller.service.ReceiptService;
import com.epam.esm.dbfiller.service.TagService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseFiller {

    private final TagService tagService;
    private final PersonService personService;
    private final GiftCertificateService giftCertificateService;
    private final ReceiptService receiptService;
    private final Random random = new Random();
    private static final int LEFT_LIMIT = 1;

    public void fillTags() {
        var listOfTagName = List.of("sport", "health", "house", "food", "work");
        var tags = new ArrayList<Tag>();
        listOfTagName.forEach(tagName -> {
            for (var i = 1; i <= 200; i++) {
                var tag = new Tag();
                tag.setName(tagName + i);
                tags.add(tag);
            }
        });
        tagService.createTags(tags);
    }

    public void fillPersons() {
        var email = "@mail.com";
        var password = "password";
        var persons = new ArrayList<Person>();
        for (int i = 1; i <= 1000; i++) {
            var person = new Person();
            person.setEmail("person" + i + email);
            person.setPassword(password + i);
            persons.add(person);
        }
        personService.createPersons(persons);
    }

    public void fillGiftCertificates() {
        var listOfNames = List.of("Gift", "Certificate", "Anniversary gift", "Special gift", "Birthday gift");
        var descriptions = List.of("sport", "health", "house", "food", "work");
        var priceRightLimit = 1000F;
        var durationRightLimit = 180;
        var giftCertificates = new ArrayList<GiftCertificate>();
        listOfNames.forEach(name -> descriptions.forEach(description -> {
            for (int i = 1; i <= 400; i++) {
                var generatedPrice = LEFT_LIMIT + random.nextFloat() * (priceRightLimit - LEFT_LIMIT);
                var giftCertificate = new GiftCertificate();
                giftCertificate.setName(name + i);
                giftCertificate.setDescription(name + " of " + description + i);
                giftCertificate.setPrice(BigDecimal.valueOf(generatedPrice));
                giftCertificate.setDuration(generateRandomIntegerNumber(durationRightLimit));
                giftCertificate.setTags(getTags());
                giftCertificates.add(giftCertificate);
            }
        }));
        giftCertificateService.createGiftCertificate(giftCertificates);
    }

    private int generateRandomIntegerNumber(int rightLimit) {
        return LEFT_LIMIT + (int) (random.nextFloat() * (rightLimit - LEFT_LIMIT));
    }

    private Set<Tag> getTags() {
        var tagsCountRightLimit = 10;
        var tagIdRightLimit = 1000L;
        var tags = new HashSet<Tag>();
        for (int j = 0; j < generateRandomIntegerNumber(tagsCountRightLimit); j++) {
            tags.add(tagService.getTagById(generateRandomLongNumber(tagIdRightLimit)));
        }
        return tags;
    }

    private Long generateRandomLongNumber(Long rightLimit) {
        return LEFT_LIMIT + (long) (Math.random() * (rightLimit - LEFT_LIMIT));
    }

    public void fillReceipts() {
        var receipts = new ArrayList<Receipt>();
        for (int i = 1; i <= 1000; i++) {
            var receiptsCountRightLimit = 50;
            var person = personService.getPersonById((long) i);
            for (int j = 0; j < generateRandomIntegerNumber(receiptsCountRightLimit); j++) {
                var receipt = new Receipt();
                receipt.setPerson(person);
                receipt.setGiftCertificates(getGiftCertificates());
                receipt.setTotalCost(
                    receipt.getGiftCertificates().stream().map(GiftCertificate::getPrice).reduce(BigDecimal::add)
                        .orElseThrow(RuntimeException::new));
                receipts.add(receipt);
            }
        }
        receiptService.createReceipt(receipts);
    }

    private List<GiftCertificate> getGiftCertificates() {
        var giftCertificatesCountRightLimit = 50;
        var giftCertificateIdRightLimit = 10000L;
        var giftCertificateDuplicatesCountRightLimit = 10;
        var giftCertificates = new ArrayList<GiftCertificate>();
        for (int j = 0; j < generateRandomIntegerNumber(giftCertificatesCountRightLimit); j++) {
            var giftCertificate =
                giftCertificateService.getGiftCertificateById(generateRandomLongNumber(giftCertificateIdRightLimit));
            for (int k = 0; k < generateRandomIntegerNumber(giftCertificateDuplicatesCountRightLimit); k++) {
                giftCertificates.add(giftCertificate);
            }
        }
        return giftCertificates;
    }
}
