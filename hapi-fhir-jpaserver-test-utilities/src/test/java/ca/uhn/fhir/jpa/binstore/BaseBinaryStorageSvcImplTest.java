package ca.uhn.fhir.jpa.binstore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseBinaryStorageSvcImplTest {

    private static final Logger ourLog =
            LoggerFactory.getLogger(BaseBinaryStorageSvcImplTest.class);

    @Test
    public void testNewRandomId() {
        MemoryBinaryStorageSvcImpl svc = new MemoryBinaryStorageSvcImpl();
        svc.setFhirContextForTests(FhirContext.forR4Cached());
        svc.setInterceptorBroadcasterForTests(new InterceptorService());

        String id = svc.newBlobId();
        ourLog.info(id);
        assertThat(id, matchesPattern("^[a-zA-Z0-9]{100}$"));
    }
}
