package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r5.model.Address;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class JsonParserSummaryModeR5Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonParserSummaryModeR5Test.class);

	private static final FhirContext ourCtx = FhirContext.forR5Cached();

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.addPhoto().setTitle("green");
		patient.getMaritalStatus().addCoding().setCode("D");

		ourLog.debug(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded).contains("Patient");
		assertThat(encoded).containsSubsequence("\"tag\"", "\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM_R4 + "\",", "\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"");
		assertThat(encoded).doesNotContain("THE DIV");
		assertThat(encoded).contains("family");
		assertThat(encoded).doesNotContain("maritalStatus");
	}

	@Test
	public void testEncodeSummary2() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().setFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded).contains("Patient");
		assertThat(encoded).containsSubsequence("\"tag\"", "\"system\": \"foo\",", "\"code\": \"bar\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_R4 + "\"",
			"\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\"");
		assertThat(encoded).doesNotContain("THE DIV");
		assertThat(encoded).contains("family");
		assertThat(encoded).doesNotContain("maritalStatus");
	}

	/**
	 * We specifically include extensions on CapabilityStatment even in
	 * summary mode, since this is behaviour that people depend on
	 */
	@Test
	public void testEncodeSummaryCapabilityStatementExtensions() {
		CapabilityStatement cs = createCapabilityStatementWithExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"rest\"");
		assertThat(encoded).contains("http://foo");
		assertThat(encoded).contains("bar");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
	}

	/**
	 * We specifically include extensions on CapabilityStatment even in
	 * summary mode, since this is behaviour that people depend on
	 */
	@Test
	public void testEncodeSummaryCapabilityStatementExtensions_ExplicitlyExcludeExtensions() {
		CapabilityStatement cs = createCapabilityStatementWithExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		parser.setPrettyPrint(true);
		parser.setDontEncodeElements(
			"CapabilityStatement.version.extension",
			"CapabilityStatement.rest.security.extension"
		);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"rest\"");
		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://goo");
		assertThat(encoded).doesNotContain("ber");
	}


	@Test
	public void testDontIncludeExtensions() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"id\": \"1\"");
		assertThat(encoded).contains("\"versionId\": \"1\"");
		assertThat(encoded).contains("\"city\": \"CITY\"");
		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://goo");
		assertThat(encoded).doesNotContain("ber");
		assertThat(encoded).doesNotContain("http://fog");
		assertThat(encoded).doesNotContain("baz");
		assertThat(encoded).doesNotContain("Married to work");
	}

	@Test
	public void testForceInclude() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setEncodeElements("Patient.maritalStatus", "Patient.address.city.extension");
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"id\": \"1\"");
		assertThat(encoded).contains("\"versionId\": \"1\"");
		assertThat(encoded).contains("\"city\": \"CITY\"");
		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://fog");
		assertThat(encoded).doesNotContain("baz");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
		assertThat(encoded).contains("Married to work");
	}

	@Test
	public void testForceInclude_UsingStar() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setEncodeElements("*.maritalStatus", "*.address.city.extension");
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"id\": \"1\"");
		assertThat(encoded).contains("\"versionId\": \"1\"");
		assertThat(encoded).contains("\"city\": \"CITY\"");
		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://fog");
		assertThat(encoded).doesNotContain("baz");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
		assertThat(encoded).contains("Married to work");
	}

	@Test
	public void testForceInclude_ViaDefaultConfig() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		FhirContext ctx = FhirContext.forR5();
		ctx.getParserOptions().setEncodeElementsForSummaryMode("Patient.maritalStatus", "Patient.address.city.extension");
		ctx.getParserOptions().setDontEncodeElementsForSummaryMode("Patient.id");

		IParser parser = ctx.newJsonParser();
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).doesNotContain("\"id\": \"1\"");
		assertThat(encoded).contains("\"versionId\": \"1\"");
		assertThat(encoded).contains("\"city\": \"CITY\"");
		assertThat(encoded).doesNotContain("http://foo");
		assertThat(encoded).doesNotContain("bar");
		assertThat(encoded).doesNotContain("http://fog");
		assertThat(encoded).doesNotContain("baz");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
		assertThat(encoded).contains("Married to work");
	}

	@Test
	public void testParserOptionsDontIncludeForSummaryModeDoesntApplyIfNotUsingSummaryMode() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		FhirContext ctx = FhirContext.forR5();
		ctx.getParserOptions().setEncodeElementsForSummaryMode("Patient.maritalStatus", "Patient.address.city.extension");
		ctx.getParserOptions().setDontEncodeElementsForSummaryMode("Patient.id");

		IParser parser = ctx.newJsonParser();
		parser.setSummaryMode(false);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded).contains("\"id\": \"1\"");
		assertThat(encoded).contains("\"versionId\": \"1\"");
		assertThat(encoded).contains("\"city\": \"CITY\"");
		assertThat(encoded).contains("http://foo");
		assertThat(encoded).contains("bar");
		assertThat(encoded).contains("http://fog");
		assertThat(encoded).contains("baz");
		assertThat(encoded).contains("http://goo");
		assertThat(encoded).contains("ber");
		assertThat(encoded).contains("Married to work");
	}

	private static @Nonnull CapabilityStatement createCapabilityStatementWithExtensions() {
		CapabilityStatement cs = new CapabilityStatement();
		CapabilityStatement.CapabilityStatementRestComponent rest = cs.addRest();
		rest.setMode(CapabilityStatement.RestfulCapabilityMode.CLIENT);
		rest.getSecurity()
			.addExtension()
			.setUrl("http://foo")
			.setValue(new StringType("bar"));

		cs.getVersionElement().addExtension()
			.setUrl("http://goo")
			.setValue(new StringType("ber"));
		return cs;
	}

	private static @Nonnull Patient createPatientWithVariousFieldsAndExtensions() {
		Patient retVal = new Patient();
		retVal.setId("Patient/1/_history/1");
		retVal.getMaritalStatus().setText("Married to work");
		retVal.addExtension()
			.setUrl("http://fog")
			.setValue(new StringType("baz"));
		Address address = retVal.addAddress();
		address.setCity("CITY");
		address
			.addExtension()
			.setUrl("http://foo")
			.setValue(new StringType("bar"));
		address.getCityElement().addExtension()
			.setUrl("http://goo")
			.setValue(new StringType("ber"));
		return retVal;
	}

}
