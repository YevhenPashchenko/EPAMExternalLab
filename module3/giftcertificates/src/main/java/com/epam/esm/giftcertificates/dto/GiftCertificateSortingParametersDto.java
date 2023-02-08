package com.epam.esm.giftcertificates.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class GiftCertificateSortingParametersDto {

  private String tagName;
  private String partName;
  private String partDescription;
  private JsonNode sortBy;

  public Map<String, String> getSortBy() {
    var map =
        new ObjectMapper()
            .convertValue(sortBy, new TypeReference<LinkedHashMap<String, String>>() {});
    return map != null ? map : new HashMap<>();
  }
}
