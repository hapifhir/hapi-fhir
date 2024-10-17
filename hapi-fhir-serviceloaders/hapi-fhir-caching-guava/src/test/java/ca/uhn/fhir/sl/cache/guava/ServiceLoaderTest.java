package ca.uhn.fhir.sl.cache.guava;

import ca.uhn.fhir.sl.cache.CacheFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
			assertNotNull(CacheFactory.build(1000));
    }
}
