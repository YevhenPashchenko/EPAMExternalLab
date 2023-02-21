package com.epam.esm.giftcertificates.mapper;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.PersonDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.Person;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityDtoMapper {

    /**
     * Converts {@link GiftCertificateDto} object to {@link GiftCertificate} object.
     *
     * @param giftCertificateDto object to convert.
     * @return converted object.
     * @see GiftCertificate
     * @see GiftCertificateDto
     */
    GiftCertificate giftCertificateDtoToGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Converts {@link GiftCertificate} object to {@link GiftCertificateDto} object.
     *
     * @param giftCertificate object to convert.
     * @return converted object.
     * @see GiftCertificate
     * @see GiftCertificateDto
     */
    GiftCertificateDto giftCertificateToGiftCertificateDto(GiftCertificate giftCertificate);

    /**
     * Update {@link GiftCertificate} object fields value from not null {@link GiftCertificateDto} object fields value.
     *
     * @param giftCertificate             converted object.
     * @param giftCertificateDtoForUpdate object to convert.
     * @see GiftCertificate
     * @see GiftCertificateDtoForUpdate
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGiftCertificateFromGiftCertificateDto(@MappingTarget GiftCertificate giftCertificate,
        GiftCertificateDtoForUpdate giftCertificateDtoForUpdate);

    /**
     * Converts {@link TagDto} object to {@link Tag} object.
     *
     * @param tagDto object to convert.
     * @return converted object.
     * @see Tag
     * @see TagDto
     */
    Tag tagDtoToTag(TagDto tagDto);

    /**
     * Converts {@link Tag} object to {@link TagDto} object.
     *
     * @param tag object to convert.
     * @return converted object.
     * @see Tag
     * @see TagDto
     */
    TagDto tagToTagDto(Tag tag);

    /**
     * Converts {@link Person} object to {@link PersonDto} object.
     *
     * @param person object to convert.
     * @return converted object.
     * @see Person
     * @see PersonDto
     */
    PersonDto personToPersonDto(Person person);

    /**
     * Converts {@link Receipt} object to {@link ReceiptDto} object.
     *
     * @param recipe object to convert.
     * @return converted object.
     * @see Receipt
     * @see ReceiptDto
     */
    ReceiptDto receiptToReceiptDto(Receipt recipe);
}
