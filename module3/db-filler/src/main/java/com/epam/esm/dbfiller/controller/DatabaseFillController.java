package com.epam.esm.dbfiller.controller;

import com.epam.esm.dbfiller.filler.DatabaseFiller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fill")
@RequiredArgsConstructor
public class DatabaseFillController {

    private final DatabaseFiller databaseFiller;

    @GetMapping
    public void fill() {
        databaseFiller.fillTags();
        databaseFiller.fillPersons();
        databaseFiller.fillGiftCertificates();
        databaseFiller.fillReceipts();
    }
}
