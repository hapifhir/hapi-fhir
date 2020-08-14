package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


public class TransactionBuilderTest {
	private static final Logger ourLog = LoggerFactory.getLogger(TransactionBuilderTest.class);
	private FhirContext myFhirContext = FhirContext.forR4();

	@Test
	public void testAddEntryUpdate() {
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);
		builder.addUpdateEntry(patient);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(1, bundle.getEntry().size());
		assertSame(patient, bundle.getEntry().get(0).getResource());
		assertEquals("http://foo/Patient/123", bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient/123", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, bundle.getEntry().get(0).getRequest().getMethod());


	}


	@Test
	public void testAddEntryUpdateConditional() {
		TransactionBuilder builder = new TransactionBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);
		builder.addUpdateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(1, bundle.getEntry().size());
		assertSame(patient, bundle.getEntry().get(0).getResource());
		assertEquals("http://foo/Patient/123", bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?active=true", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, bundle.getEntry().get(0).getRequest().getMethod());


	}


}
