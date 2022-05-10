package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BundleBuilderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(BundleBuilderTest.class);
	private FhirContext myFhirContext = FhirContext.forR4();
	private Date myCheckDate;

	@BeforeEach
	public void initDate() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2021, 0, 0);
		myCheckDate = cal.getTime();
	}

	@Test
	public void testAddEntryUpdate() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);
		builder.addTransactionUpdateEntry(patient);

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
	public void testNewPrimitive() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IPrimitiveType<Date> datePrimitive = builder.newPrimitive("instant", myCheckDate);
		assertNotNull(datePrimitive);
		assertEquals(myCheckDate, datePrimitive.getValue());
	}

	@Test
	public void testSettingBundleFields() {
		String uuid = UUID.randomUUID().toString();

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder
			.setBundleField("type", "searchset")
			.setBundleField("id", uuid)
			.setMetaField("lastUpdated", builder.newPrimitive("instant", myCheckDate));

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.SEARCHSET, bundle.getType());
		assertEquals(uuid, bundle.getId());
		assertEquals(myCheckDate, bundle.getMeta().getLastUpdated());
	}


	@Test
	public void testAddEntryUpdateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);
		builder.addTransactionUpdateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(1, bundle.getEntry().size());
		assertSame(patient, bundle.getEntry().get(0).getResource());
		assertEquals("http://foo/Patient/123", bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?active=true", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.PUT, bundle.getEntry().get(0).getRequest().getMethod());
	}

	@Test
	public void testSearchHandling() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IBase entry = builder.addEntry();
		assertNotNull(entry);

		IBase search = builder.addSearch(entry);
		assertNotNull(entry);

		builder.setSearchField(search, "mode", "match");
		builder.setSearchField(search, "score", builder.newPrimitive("decimal", BigDecimal.ONE));

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(1, bundle.getEntry().size());
		assertNotNull(bundle.getEntry().get(0).getSearch());
		assertEquals(Bundle.SearchEntryMode.MATCH, bundle.getEntry().get(0).getSearch().getMode());
		assertEquals(BigDecimal.ONE, bundle.getEntry().get(0).getSearch().getScore());
	}

	@Test
	public void testAddToEntry() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		IBase entry = builder.addEntry();

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addToEntry(entry, "resource", patient);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(1, bundle.getEntry().size());
	}

	@Test
	public void testAddEntryCreate() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(1, bundle.getEntry().size());
		assertSame(patient, bundle.getEntry().get(0).getResource());
		assertEquals(null, bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.POST, bundle.getEntry().get(0).getRequest().getMethod());
	}

	@Test
	public void testAddEntryDelete() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		patient.setId("123");
		builder.addTransactionDeleteEntry(patient);
		builder.addTransactionDeleteEntry("Patient", "123");
		Bundle bundle = (Bundle) builder.getBundle();

		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(2, bundle.getEntry().size());

		//Check the IBaseresource style entry
		assertNull(bundle.getEntry().get(0).getResource());
		assertEquals("Patient/123", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.DELETE, bundle.getEntry().get(0).getRequest().getMethod());

		//Check the resourcetype + id style entry.
		assertNull(bundle.getEntry().get(1).getResource());
		assertEquals("Patient/123", bundle.getEntry().get(1).getRequest().getUrl());
		assertEquals(Bundle.HTTPVerb.DELETE, bundle.getEntry().get(1).getRequest().getMethod());



	}

	@Test
	public void testAddEntryCreateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.info("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(Bundle.BundleType.TRANSACTION, bundle.getType());
		assertEquals(1, bundle.getEntry().size());
		assertSame(patient, bundle.getEntry().get(0).getResource());
		assertEquals(null, bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals("Patient?active=true", bundle.getEntry().get(0).getRequest().getIfNoneExist());
		assertEquals(Bundle.HTTPVerb.POST, bundle.getEntry().get(0).getRequest().getMethod());
	}

}
