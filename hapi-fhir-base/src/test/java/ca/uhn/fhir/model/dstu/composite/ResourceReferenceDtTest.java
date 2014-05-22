package ca.uhn.fhir.model.dstu.composite;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class ResourceReferenceDtTest {

	private static FhirContext ourCtx;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceReferenceDtTest.class);

	@Test
	public void testParseValueAbsolute() {
	}


	@BeforeClass
	public static void beforeClass() {
		ourCtx = new FhirContext();
	}

}
