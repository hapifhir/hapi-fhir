package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.ProvidesResources;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProvidedResourceScannerTest extends TestCase {
    @Test
    public void testScannerShouldAddProvidedResources() {
        FhirContext ctx = new FhirContext();
        assertNull(ctx.getElementDefinition(CustomPatient.class));

        ProvidedResourceScanner scanner = new ProvidedResourceScanner(ctx);
        scanner.scanForProvidedResources(new TestResourceProviderB());

        assertNotNull(ctx.getElementDefinition(CustomPatient.class));
    }

    @ProvidesResources(resources=CustomObservation.class)
    class TestResourceProviderA {
    }

    @ProvidesResources(resources={CustomPatient.class,ResourceWithExtensionsA.class})
    class TestResourceProviderB {
    }

    @ResourceDef(name = "Patient", id="CustomPatient")
    class CustomPatient extends Patient {
    }
}