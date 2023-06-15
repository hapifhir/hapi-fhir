package ca.uhn.fhir.sl.cache.guava;

import org.junit.jupiter.api.Test;

import ca.uhn.fhir.sl.cache.CacheFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
        assertNotNull(CacheFactory.build(1000));
    }
}
