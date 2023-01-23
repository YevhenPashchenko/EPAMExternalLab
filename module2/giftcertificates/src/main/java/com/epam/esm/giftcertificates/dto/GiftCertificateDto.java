package com.epam.esm.giftcertificates.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GiftCertificateDto {

    private List<TagDto> tags = new ArrayList<>();

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;
}
