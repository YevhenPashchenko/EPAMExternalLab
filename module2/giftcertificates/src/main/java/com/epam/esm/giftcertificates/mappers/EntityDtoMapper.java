package com.epam.esm.giftcertificates.mappers;

import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;
import com.epam.esm.giftcertificates.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityDtoMapper {

    /**
     * Method that converts {@link GiftCertificateDto} to {@link GiftCertificate}.
     * @param giftCertificateDto {@link GiftCertificateDto}.
     * @return {@link GiftCertificate}.
     */
    GiftCertificate giftCertificateDtoToGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Method that converts {@link GiftCertificate} to {@link GiftCertificateDto}.
     * @param giftCertificate {@link GiftCertificate}.
     * @return {@link GiftCertificateDto}.
     */
    GiftCertificateDto giftCertificateToGiftCertificateDto(GiftCertificate giftCertificate);

    /**
     * Method that converts {@link GiftCertificateToTagDto} to {@link GiftCertificateToTag}.
     * @param giftCertificateToTagDto {@link GiftCertificateToTagDto}.
     * @return {@link GiftCertificateToTag}.
     */
    GiftCertificateToTag giftCertificateToTagDtoToGiftCertificateToTag(GiftCertificateToTagDto giftCertificateToTagDto);

    /**
     * Method that converts {@link TagDto} to {@link Tag}.
     * @param tagDto {@link TagDto}.
     * @return {@link Tag}.
     */
    Tag tagDtoToTag(TagDto tagDto);

    /**
     * Method that converts {@link Tag} to {@link TagDto}.
     * @param tag {@link Tag}.
     * @return {@link TagDto}.
     */
    TagDto tagToTagDto(Tag tag);

    /**
     * Method that converts {@link GiftCertificateSortingParametersDto} to {@link GiftCertificateSortingParameters}.
     * @param giftCertificateSortingParametersDto {@link GiftCertificateSortingParametersDto}.
     * @return {@link GiftCertificateSortingParameters}.
     */
    GiftCertificateSortingParameters giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(GiftCertificateSortingParametersDto giftCertificateSortingParametersDto);
}
