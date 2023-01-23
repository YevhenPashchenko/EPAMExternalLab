package com.epam.esm.giftcertificates.integration.reader;

import java.io.IOException;

public interface IntegrationTestFileReader<T> {

    T read(String path) throws IOException;
}
