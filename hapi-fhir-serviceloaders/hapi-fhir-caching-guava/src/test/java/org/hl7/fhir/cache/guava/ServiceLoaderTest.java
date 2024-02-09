package ca.uhn.fhir.sl.cache.guava;

import ca.uhn.fhir.sl.cache.CacheFactory;
import org.junit.jupiter.api.Test;

public class ServiceLoaderTest {
    @Test
    void loaderIsAvailable() {
			assertThat(CacheFactory.build(1000)).isNotNull();
    }
}
