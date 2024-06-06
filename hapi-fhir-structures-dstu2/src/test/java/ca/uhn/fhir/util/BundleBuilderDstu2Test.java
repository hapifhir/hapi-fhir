package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;


public class BundleBuilderDstu2Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BundleBuilderDstu2Test.class);
	private FhirContext myFhirContext = FhirContext.forDstu2Cached();
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
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(BundleTypeEnum.TRANSACTION, bundle.getTypeElement().getValueAsEnum());
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertEquals("http://foo/Patient/123", bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient/123", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(HTTPVerbEnum.PUT, bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum());
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
		try {
			builder.setBundleField("id", uuid);
			fail();		} catch (NullPointerException e) {
			assertEquals("Unable to find field id", e.getMessage());

		}

		try {
		builder.setMetaField("lastUpdated", builder.newPrimitive("instant", myCheckDate));
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("This method may only be called for FHIR version DSTU3 and above", e.getMessage());
		}

	}


	@Test
	public void testAddEntryUpdateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setId("http://foo/Patient/123");
		patient.setActive(true);
		builder.addTransactionUpdateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(BundleTypeEnum.TRANSACTION, bundle.getTypeElement().getValueAsEnum());
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertEquals("http://foo/Patient/123", bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient?active=true", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(HTTPVerbEnum.PUT, bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum());
	}

	@Test
	public void testSearchHandling() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IBase entry = builder.addEntry();
		assertNotNull(entry);

		try {
			builder.addSearch(entry);
			fail();		} catch (IllegalArgumentException e) {
			assertEquals("This method may only be called for FHIR version DSTU3 and above", e.getMessage());
		}
	}

	@Test
	public void testAddToEntry() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		IBase entry = builder.addEntry();

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addToEntry(entry, "resource", patient);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getEntry()).hasSize(1);
	}

	@Test
	public void testAddEntryCreate() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(BundleTypeEnum.TRANSACTION, bundle.getTypeElement().getValueAsEnum());
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertNull(bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(HTTPVerbEnum.POST, bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum());
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

		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(BundleTypeEnum.TRANSACTION, bundle.getTypeElement().getValueAsEnum());
		assertThat(bundle.getEntry()).hasSize(2);

		//Check the IBaseresource style entry
		assertNull(bundle.getEntry().get(0).getResource());
		assertEquals("Patient/123", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals(HTTPVerbEnum.DELETE, bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum());

		//Check the resourcetype + id style entry.
		assertNull(bundle.getEntry().get(1).getResource());
		assertEquals("Patient/123", bundle.getEntry().get(1).getRequest().getUrl());
		assertEquals(HTTPVerbEnum.DELETE, bundle.getEntry().get(1).getRequest().getMethodElement().getValueAsEnum());


	}

	@Test
	public void testAddEntryCreateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertEquals(BundleTypeEnum.TRANSACTION, bundle.getTypeElement().getValueAsEnum());
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertNull(bundle.getEntry().get(0).getFullUrl());
		assertEquals("Patient", bundle.getEntry().get(0).getRequest().getUrl());
		assertEquals("Patient?active=true", bundle.getEntry().get(0).getRequest().getIfNoneExist());
		assertEquals(HTTPVerbEnum.POST, bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum());
	}

}
