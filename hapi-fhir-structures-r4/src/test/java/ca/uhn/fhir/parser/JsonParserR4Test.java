package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class JsonParserR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonParserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	private Bundle createBundleWithPatient() {
		Bundle b = new Bundle();
		b.setId("BUNDLEID");
		b.getMeta().addProfile("http://FOO");

		Patient p = new Patient();
		p.setId("PATIENTID");
		p.getMeta().addProfile("http://BAR");
		p.addName().addGiven("GIVEN");
		b.addEntry().setResource(p);
		return b;
	}

	@Test
	public void testDontStripVersions() {
		FhirContext ctx = FhirContext.forR4();
		ctx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("QuestionnaireResponse.questionnaire");

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaireElement().setValueAsString("Questionnaire/123/_history/456");

		String output = ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(qr);
		ourLog.info(output);

		assertThat(output, containsString("\"Questionnaire/123/_history/456\""));
	}

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwice() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertNotEquals(-1, idx);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	/**
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwiceWithManualIds() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		mr.setId("#MR");
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		med.setId("#MED");
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertNotEquals(-1, idx);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	/*
	 * See #814
	 */
	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwiceWithManualIdsAndManualAddition() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		mr.setId("#MR");
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		med.setId("#MED");

		Reference medRef = new Reference();
		medRef.setReference("#MED");
		md.setMedication(medRef);
		mr.setMedication(medRef);

		md.getContained().add(mr);
		md.getContained().add(med);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(md);
		ourLog.info(encoded);

		int idx = encoded.indexOf("\"Medication\"");
		assertNotEquals(-1, idx);

		idx = encoded.indexOf("\"Medication\"", idx + 1);
		assertEquals(-1, idx);

	}

	@Test
	public void testEncodeAndParseUnicodeCharacterInNarrative() {
		Patient p = new Patient();
		p.getText().getDiv().setValueAsString("<div>Copy © 1999</div>");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(encoded);

		p = (Patient) ourCtx.newJsonParser().parseResource(encoded);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">Copy &copy; 1999</div>", p.getText().getDivAsString());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalFirst() {

		Observation obs = new Observation();

		Patient pt = new Patient();
		pt.setId("#1");
		pt.addName().setFamily("FAM");
		obs.getSubject().setReference("#1");
		obs.getContained().add(pt);

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		obs.getContext().setResource(enc);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newJsonParser().parseResource(Observation.class, encoded);
		assertEquals("#1", obs.getContained().get(0).getId());
		assertEquals("#2", obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getContext().getResource();
		assertEquals(Encounter.EncounterStatus.ARRIVED, enc.getStatus());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast() {

		Observation obs = new Observation();

		Patient pt = new Patient();
		pt.addName().setFamily("FAM");
		obs.getSubject().setResource(pt);

		Encounter enc = new Encounter();
		enc.setId("#1");
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		obs.getContext().setReference("#1");
		obs.getContained().add(enc);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newJsonParser().parseResource(Observation.class, encoded);
		assertEquals("#1", obs.getContained().get(0).getId());
		assertEquals("#2", obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getContext().getResource();
		assertEquals(Encounter.EncounterStatus.ARRIVED, enc.getStatus());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast2() {

		MedicationRequest mr = new MedicationRequest();
		Practitioner pract = new Practitioner().setActive(true);
		mr.getRequester().setResource(pract);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newJsonParser().parseResource(MedicationRequest.class, encoded);

		mr.setMedication(new Reference(new Medication().setStatus(Medication.MedicationStatus.ACTIVE)));
		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newJsonParser().parseResource(MedicationRequest.class, encoded);

		assertEquals("#2", mr.getContained().get(0).getId());
		assertEquals("#1", mr.getContained().get(1).getId());

	}

	@Test
	public void testExcludeNothing() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
//		excludes.add("*.id");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, containsString("BUNDLEID"));
		assertThat(encoded, containsString("http://FOO"));
		assertThat(encoded, containsString("PATIENTID"));
		assertThat(encoded, containsString("http://BAR"));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", b.getEntry().get(0).getResource().getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeRootStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("id");
		excludes.add("meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, (containsString("PATIENTID")));
		assertThat(encoded, (containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", b.getEntry().get(0).getResource().getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeStarDotStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("*.id");
		excludes.add("*.meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, not(containsString("PATIENTID")));
		assertThat(encoded, not(containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertNotEquals("Patient/PATIENTID", b.getEntry().get(0).getResource().getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	/**
	 * Test that long JSON strings don't get broken up
	 */
	@Test
	public void testNoBreakInLongString() {
		String longString = StringUtils.leftPad("", 100000, 'A');

		Patient p = new Patient();
		p.addName().setFamily(longString);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);

		assertThat(encoded, containsString(longString));
	}

	@Test
	public void testParseAndEncodeExtensionWithValueWithExtension() {
		String input = "{\n" +
			"  \"resourceType\": \"Patient\",\n" +
			"  \"extension\": [\n" +
			"    {\n" +
			"      \"url\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/BirthWeight\",\n" +
			"      \"_valueDecimal\": {\n" +
			"        \"extension\": [\n" +
			"          {\n" +
			"            \"url\": \"http://www.hl7.org/fhir/extension-data-absent-reason.html\",\n" +
			"            \"valueCoding\": {\n" +
			"              \"system\": \"http://hl7.org/fhir/ValueSet/birthweight\",\n" +
			"              \"code\": \"Underweight\",\n" +
			"              \"userSelected\": false\n" +
			"            }\n" +
			"          }\n" +
			"        ]\n" +
			"      }\n" +
			"    }\n" +
			"  ],\n" +
			"  \"identifier\": [\n" +
			"    {\n" +
			"      \"system\": \"https://purl.org/elab/fhir/network/StructureDefinition/1/EuroPrevallStudySubjects\",\n" +
			"      \"value\": \"1\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"gender\": \"female\"\n" +
			"}";

		IParser jsonParser = ourCtx.newJsonParser();
		IParser xmlParser = ourCtx.newXmlParser();
		jsonParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));
		xmlParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));

		Patient parsed = jsonParser.parseResource(Patient.class, input);

		ourLog.info(jsonParser.setPrettyPrint(true).encodeResourceToString(parsed));
		assertThat(xmlParser.encodeResourceToString(parsed), containsString("Underweight"));
		assertThat(jsonParser.encodeResourceToString(parsed), containsString("Underweight"));

	}

	@Test
	public void testParseExtensionOnPrimitive() throws IOException {
		String input = IOUtils.toString(JsonParserR4Test.class.getResourceAsStream("/extension-on-line.txt"));
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Patient pt = parser.parseResource(Patient.class, input);

		StringType line0 = pt.getAddressFirstRep().getLine().get(0);
		assertEquals("535 Sheppard Avenue West, Unit 1907", line0.getValue());
		Extension houseNumberExt = line0.getExtensionByUrl("http://hl7.org/fhir/StructureDefinition/iso21090-ADXP-houseNumber");
		assertEquals("535", ((StringType) houseNumberExt.getValue()).getValue());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
