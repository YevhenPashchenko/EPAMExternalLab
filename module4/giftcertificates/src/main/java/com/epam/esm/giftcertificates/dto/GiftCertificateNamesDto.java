package com.epam.esm.giftcertificates.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class GiftCertificateNamesDto {

    @NotEmpty(message = "list of gift certificate names must not be empty")
    private List<String> giftCertificateNames = new ArrayList<>();
}
