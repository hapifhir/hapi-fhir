package ca.uhn.fhir.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;
import org.junit.jupiter.api.Test;

public class DependencyLogUtilTest {

    @Test
    public void testDependencyLogFactory() {
        IDependencyLog logger = DependencyLogFactory.createJarLogger();
        assertNotNull(logger);
        logger.logStaxImplementation(DependencyLogUtilTest.class);
    }
}
