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
import static org.assertj.core.api.Assertions.fail;


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

		assertThat(bundle.getTypeElement().getValueAsEnum()).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("http://foo/Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.PUT);
	}

	@Test
	public void testNewPrimitive() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IPrimitiveType<Date> datePrimitive = builder.newPrimitive("instant", myCheckDate);
		assertThat(datePrimitive).isNotNull();
		assertThat(datePrimitive.getValue()).isEqualTo(myCheckDate);
	}

	@Test
	public void testSettingBundleFields() {
		String uuid = UUID.randomUUID().toString();

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		try {
			builder.setBundleField("id", uuid);
			fail("");		} catch (NullPointerException e) {
			assertThat(e.getMessage()).isEqualTo("Unable to find field id");

		}

		try {
		builder.setMetaField("lastUpdated", builder.newPrimitive("instant", myCheckDate));
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("This method may only be called for FHIR version DSTU3 and above");
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

		assertThat(bundle.getTypeElement().getValueAsEnum()).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("http://foo/Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient?active=true");
		assertThat(bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.PUT);
	}

	@Test
	public void testSearchHandling() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IBase entry = builder.addEntry();
		assertThat(entry).isNotNull();

		try {
			builder.addSearch(entry);
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("This method may only be called for FHIR version DSTU3 and above");
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

		assertThat(bundle.getTypeElement().getValueAsEnum()).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isNull();
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient");
		assertThat(bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.POST);
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

		assertThat(bundle.getTypeElement().getValueAsEnum()).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(2);

		//Check the IBaseresource style entry
		assertThat(bundle.getEntry().get(0).getResource()).isNull();
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.DELETE);

		//Check the resourcetype + id style entry.
		assertThat(bundle.getEntry().get(1).getResource()).isNull();
		assertThat(bundle.getEntry().get(1).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(1).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.DELETE);


	}

	@Test
	public void testAddEntryCreateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getTypeElement().getValueAsEnum()).isEqualTo(BundleTypeEnum.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isNull();
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient");
		assertThat(bundle.getEntry().get(0).getRequest().getIfNoneExist()).isEqualTo("Patient?active=true");
		assertThat(bundle.getEntry().get(0).getRequest().getMethodElement().getValueAsEnum()).isEqualTo(HTTPVerbEnum.POST);
	}

}
