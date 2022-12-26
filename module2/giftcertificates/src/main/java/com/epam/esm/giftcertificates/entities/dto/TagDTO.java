package com.epam.esm.giftcertificates.entities.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TagDTO implements Serializable {

    private long id;
    private String name;
}
