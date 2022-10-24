package org.hl7.fhir.cache.caffeine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.cache.CacheFactory;
import org.junit.jupiter.api.Test;

public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
        assertNotNull(CacheFactory.build(1000));
    }
}
