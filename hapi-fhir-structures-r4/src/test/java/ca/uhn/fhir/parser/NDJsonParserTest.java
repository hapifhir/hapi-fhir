package ca.uhn.fhir.parser;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.TestUtil;

import org.hl7.fhir.r4.model.Patient;

public class NDJsonParserTest {
	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(NDJsonParserTest.class);

	private String toNDJson(IBaseResource bundle) throws DataFormatException {
		IParser p = ourCtx.newNDJsonParser();
		return p.encodeResourceToString(bundle);
	}

	private IBaseResource fromNDJson(String ndjson) throws DataFormatException {
		IParser p = ourCtx.newNDJsonParser();
		return p.parseResource(ndjson);
	} 

	private boolean fhirResourcesEqual(IBaseResource expected, IBaseResource actual) {
		// I would prefer to use, e.g., EqualsBuilder to do this instead.
		String encoded_expected = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expected);
		String encoded_actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(actual);

		ourLog.info("Expected: {}", encoded_expected);
 		ourLog.info("Actual  : {}", encoded_actual);

		return encoded_expected.equals(encoded_actual);
	}

	@Test
	public void testSinglePatientEncodeDecode() {
		BundleBuilder myBuilder = new BundleBuilder(ourCtx);

		Patient p = new Patient();
		p.setId("Patient/P1");
		myBuilder.addCollectionEntry(p);
		IBaseResource myBundle = myBuilder.getBundle();
		IBaseResource responseBundle = fromNDJson(toNDJson(myBundle));

		assertTrue(fhirResourcesEqual(myBundle, responseBundle));
	}

	@Test
	public void testEmptyBundleEncodeDecode() {
		BundleBuilder myBuilder = new BundleBuilder(ourCtx);

		myBuilder.setType("collection");
		IBaseResource myBundle = myBuilder.getBundle();
		IBaseResource responseBundle = fromNDJson(toNDJson(myBundle));

		assertTrue(fhirResourcesEqual(myBundle, responseBundle));
	}

	@Test
	public void testThreePatientEncodeDecode() {
		BundleBuilder myBuilder = new BundleBuilder(ourCtx);

		Patient p = new Patient();
		p.setId("Patient/P1");
		myBuilder.addCollectionEntry(p);
		p = new Patient();
		p.setId("Patient/P2");
		myBuilder.addCollectionEntry(p);
		p = new Patient();
		p.setId("Patient/P3");
		myBuilder.addCollectionEntry(p);

		IBaseResource myBundle = myBuilder.getBundle();
		IBaseResource responseBundle = fromNDJson(toNDJson(myBundle));

		assertTrue(fhirResourcesEqual(myBundle, responseBundle));
	}

	@Test
	public void testHasNewlinesEncodeDecode() {
		BundleBuilder myBuilder = new BundleBuilder(ourCtx);

		Patient p = new Patient();
		p.setId("Patient/P1");
		p.addAddress().setText("1 Place Street\r\nOn Earth");
		myBuilder.addCollectionEntry(p);
		IBaseResource myBundle = myBuilder.getBundle();
		IBaseResource responseBundle = fromNDJson(toNDJson(myBundle));

		assertTrue(fhirResourcesEqual(myBundle, responseBundle));
	}

	@Test
	public void testOnlyEncodesBundles() {
		Patient p = new Patient();
		p.setId("Patient/P1");
		assertThrows(IllegalArgumentException.class,
		             ()->{toNDJson(p);});
	}

	@Test
	public void testOnlyDecodesBundles() {
		BundleBuilder myBuilder = new BundleBuilder(ourCtx);

		Patient p = new Patient();
		p.setId("Patient/P1");
		myBuilder.addCollectionEntry(p);
		IBaseResource myBundle = myBuilder.getBundle();
 		String myBundleJson = toNDJson(myBundle);
		IParser parser = ourCtx.newNDJsonParser();
		assertThrows(DataFormatException.class,
		             ()->{parser.parseResource(Patient.class, myBundleJson);});
	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
