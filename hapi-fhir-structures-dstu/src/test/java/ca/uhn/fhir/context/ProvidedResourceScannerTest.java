package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.ProvidesResources;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.util.TestUtil;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;

public class ProvidedResourceScannerTest extends TestCase {
	@Test
	public void testScannerShouldAddProvidedResources() {
		FhirContext ctx = FhirContext.forDstu1();
		assertEquals(CustomPatient.class, ctx.getElementDefinition(CustomPatient.class).getImplementingClass());
		assertEquals(Patient.class, ctx.getResourceDefinition("Patient").getImplementingClass());

		ProvidedResourceScanner scanner = new ProvidedResourceScanner(ctx);
		scanner.scanForProvidedResources(new TestResourceProviderB());

		assertNotNull(ctx.getElementDefinition(CustomPatient.class));
	}

	@ResourceDef(name = "Patient", id = "CustomPatient")
	public static class CustomPatient extends Patient {
	}

	@ProvidesResources(resources = CustomObservation.class)
	public static class TestResourceProviderA {
	}

	@ProvidesResources(resources = { CustomPatient.class, ResourceWithExtensionsA.class })
	public static class TestResourceProviderB {
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}