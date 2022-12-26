package com.epam.esm.giftcertificates.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class GiftCertificateDTO implements Serializable {

    private List<TagDTO> tags = new ArrayList<>();

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;

    private String sortByName;
    private String sortByCreateDate;
    private String sortByLastUpdateDate;
}
