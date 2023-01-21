package com.epam.esm.giftcertificates.mapper;

import com.epam.esm.giftcertificates.entity.GiftCertificateSortingParameters;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.GiftCertificateToTag;
import com.epam.esm.giftcertificates.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityDtoMapper {

    /**
     * Converts {@link GiftCertificateDto} object to {@link GiftCertificate} object.
     * @param giftCertificateDto {@link GiftCertificateDto} object.
     * @return {@link GiftCertificate} object.
     */
    GiftCertificate giftCertificateDtoToGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Converts {@link GiftCertificate} object to {@link GiftCertificateDto} object.
     * @param giftCertificate {@link GiftCertificate} object.
     * @return {@link GiftCertificateDto} object.
     */
    GiftCertificateDto giftCertificateToGiftCertificateDto(GiftCertificate giftCertificate);

    /**
     * Converts {@link GiftCertificateToTagDto} object to {@link GiftCertificateToTag} object.
     * @param giftCertificateToTagDto {@link GiftCertificateToTagDto} object.
     * @return {@link GiftCertificateToTag} object.
     */
    GiftCertificateToTag giftCertificateToTagDtoToGiftCertificateToTag(GiftCertificateToTagDto giftCertificateToTagDto);

    /**
     * Converts {@link TagDto} object to {@link Tag} object.
     * @param tagDto {@link TagDto} object.
     * @return {@link Tag} object.
     */
    Tag tagDtoToTag(TagDto tagDto);

    /**
     * Converts {@link Tag} object to {@link TagDto} object.
     * @param tag {@link Tag} object.
     * @return {@link TagDto} object.
     */
    TagDto tagToTagDto(Tag tag);

    /**
     * Converts {@link GiftCertificateSortingParametersDto} object to {@link GiftCertificateSortingParameters} object.
     * @param giftCertificateSortingParametersDto {@link GiftCertificateSortingParametersDto} object.
     * @return {@link GiftCertificateSortingParameters} object.
     */
    GiftCertificateSortingParameters giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(GiftCertificateSortingParametersDto giftCertificateSortingParametersDto);
}
