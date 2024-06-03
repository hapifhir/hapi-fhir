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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;

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

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM_R4 + "\",", "\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\""));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
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

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"foo\",", "\"code\": \"bar\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_R4 + "\"",
			"\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\""));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
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

		assertThat(encoded, (containsString("\"rest\"")));
		assertThat(encoded, (containsString("http://foo")));
		assertThat(encoded, (containsString("bar")));
		assertThat(encoded, (containsString("http://goo")));
		assertThat(encoded, (containsString("ber")));
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

		assertThat(encoded, (containsString("\"rest\"")));
		assertThat(encoded, not(containsString("http://foo")));
		assertThat(encoded, not(containsString("bar")));
		assertThat(encoded, not(containsString("http://goo")));
		assertThat(encoded, not(containsString("ber")));
	}


	@Test
	public void testDontIncludeExtensions() {
		Patient cs = createPatientWithVariousFieldsAndExtensions();

		IParser parser = ourCtx.newJsonParser();
		parser.setSummaryMode(true);
		parser.setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(cs);
		ourLog.info(encoded);

		assertThat(encoded, containsString("\"id\": \"1\""));
		assertThat(encoded, containsString("\"versionId\": \"1\""));
		assertThat(encoded, containsString("\"city\": \"CITY\""));
		assertThat(encoded, not(containsString("http://foo")));
		assertThat(encoded, not(containsString("bar")));
		assertThat(encoded, not(containsString("http://goo")));
		assertThat(encoded, not(containsString("ber")));
		assertThat(encoded, not(containsString("http://fog")));
		assertThat(encoded, not(containsString("baz")));
		assertThat(encoded, not(containsString("Married to work")));
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

		assertThat(encoded, containsString("\"id\": \"1\""));
		assertThat(encoded, containsString("\"versionId\": \"1\""));
		assertThat(encoded, containsString("\"city\": \"CITY\""));
		assertThat(encoded, not(containsString("http://foo")));
		assertThat(encoded, not(containsString("bar")));
		assertThat(encoded, not(containsString("http://fog")));
		assertThat(encoded, not(containsString("baz")));
		assertThat(encoded, containsString("http://goo"));
		assertThat(encoded, containsString("ber"));
		assertThat(encoded, containsString("Married to work"));
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

		assertThat(encoded, containsString("\"id\": \"1\""));
		assertThat(encoded, containsString("\"versionId\": \"1\""));
		assertThat(encoded, containsString("\"city\": \"CITY\""));
		assertThat(encoded, not(containsString("http://foo")));
		assertThat(encoded, not(containsString("bar")));
		assertThat(encoded, not(containsString("http://fog")));
		assertThat(encoded, not(containsString("baz")));
		assertThat(encoded, containsString("http://goo"));
		assertThat(encoded, containsString("ber"));
		assertThat(encoded, containsString("Married to work"));
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

		assertThat(encoded, not(containsString("\"id\": \"1\"")));
		assertThat(encoded, containsString("\"versionId\": \"1\""));
		assertThat(encoded, containsString("\"city\": \"CITY\""));
		assertThat(encoded, not(containsString("http://foo")));
		assertThat(encoded, not(containsString("bar")));
		assertThat(encoded, not(containsString("http://fog")));
		assertThat(encoded, not(containsString("baz")));
		assertThat(encoded, containsString("http://goo"));
		assertThat(encoded, containsString("ber"));
		assertThat(encoded, containsString("Married to work"));
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

		assertThat(encoded, containsString("\"id\": \"1\""));
		assertThat(encoded, containsString("\"versionId\": \"1\""));
		assertThat(encoded, containsString("\"city\": \"CITY\""));
		assertThat(encoded, containsString("http://foo"));
		assertThat(encoded, containsString("bar"));
		assertThat(encoded, containsString("http://fog"));
		assertThat(encoded, containsString("baz"));
		assertThat(encoded, containsString("http://goo"));
		assertThat(encoded, containsString("ber"));
		assertThat(encoded, containsString("Married to work"));
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
