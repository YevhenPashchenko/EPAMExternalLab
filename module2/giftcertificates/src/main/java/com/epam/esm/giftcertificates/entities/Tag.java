package com.epam.esm.giftcertificates.entities;

import lombok.Data;

import java.io.Serializable;

@Data
public class Tag implements Serializable {

    private long id;
    private String name;
}
