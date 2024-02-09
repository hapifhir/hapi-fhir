package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
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

import static org.assertj.core.api.Assertions.assertThat;


public class BundleBuilderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BundleBuilderR4Test.class);
	private FhirContext myFhirContext = FhirContext.forR4Cached();
	private Date myCheckDate;

	@BeforeEach
	public void initDate() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(2021, 0, 0);
		myCheckDate = cal.getTime();
	}

	@Test
	public void testAddEntryPatch() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionFhirPatchEntry(new IdType("http://foo/Patient/123"), patch);

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patch);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("http://foo/Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PATCH);

	}

	@Test
	public void testAddEntryPatchConditional() {
		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));

		BundleBuilder builder = new BundleBuilder(myFhirContext);
		builder.addTransactionFhirPatchEntry(patch).conditional("Patient?identifier=http://foo|123");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patch);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo(null);
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient?identifier=http://foo|123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PATCH);

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

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("http://foo/Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PUT);
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
		builder
			.setBundleField("type", "searchset")
			.setBundleField("id", uuid)
			.setMetaField("lastUpdated", builder.newPrimitive("instant", myCheckDate));

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.SEARCHSET);
		assertThat(bundle.getId()).isEqualTo(uuid);
		assertThat(bundle.getMeta().getLastUpdated()).isEqualTo(myCheckDate);
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

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo("http://foo/Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient?active=true");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.PUT);
	}

	@Test
	public void testSearchHandling() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);
		IBase entry = builder.addEntry();
		assertThat(entry).isNotNull();

		IBase search = builder.addSearch(entry);
		assertThat(entry).isNotNull();

		builder.setSearchField(search, "mode", "match");
		builder.setSearchField(search, "score", builder.newPrimitive("decimal", BigDecimal.ONE));

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getSearch()).isNotNull();
		assertThat(bundle.getEntry().get(0).getSearch().getMode()).isEqualTo(Bundle.SearchEntryMode.MATCH);
		assertThat(bundle.getEntry().get(0).getSearch().getScore()).isEqualTo(BigDecimal.ONE);
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

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo(null);
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
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

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(2);

		//Check the IBaseresource style entry
		assertThat(bundle.getEntry().get(0).getResource()).isNull();
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.DELETE);

		//Check the resourcetype + id style entry.
		assertThat(bundle.getEntry().get(1).getResource()).isNull();
		assertThat(bundle.getEntry().get(1).getRequest().getUrl()).isEqualTo("Patient/123");
		assertThat(bundle.getEntry().get(1).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.DELETE);



	}

	@Test
	public void testAddEntryCreateConditional() {
		BundleBuilder builder = new BundleBuilder(myFhirContext);

		Patient patient = new Patient();
		patient.setActive(true);
		builder.addTransactionCreateEntry(patient).conditional("Patient?active=true");

		Bundle bundle = (Bundle) builder.getBundle();
		ourLog.debug("Bundle:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));

		assertThat(bundle.getType()).isEqualTo(Bundle.BundleType.TRANSACTION);
		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource()).isSameAs(patient);
		assertThat(bundle.getEntry().get(0).getFullUrl()).isEqualTo(null);
		assertThat(bundle.getEntry().get(0).getRequest().getUrl()).isEqualTo("Patient");
		assertThat(bundle.getEntry().get(0).getRequest().getIfNoneExist()).isEqualTo("Patient?active=true");
		assertThat(bundle.getEntry().get(0).getRequest().getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
	}

}
