package ca.uhn.fhir.sl.cache.caffeine;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.uhn.fhir.sl.cache.CacheFactory;
import org.junit.jupiter.api.Test;

public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
        assertNotNull(CacheFactory.build(1000));
    }
}
