package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
public class RDFParserR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(RDFParserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	/*
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
	*/

	@Test
	public void testDontStripVersions() {
		FhirContext ctx = FhirContext.forR4();
		ctx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("QuestionnaireResponse.questionnaire");

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaireElement().setValueAsString("Questionnaire/123/_history/456");

		String output = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(qr);
		ourLog.info(output);

		assertThat(output, containsString("\"Questionnaire/123/_history/456\""));
	}

	@Test
	public void testDuplicateContainedResourcesNotOutputtedTwice() {
		MedicationDispense md = new MedicationDispense();

		MedicationRequest mr = new MedicationRequest();
		md.addAuthorizingPrescription().setResource(mr);

		Medication med = new Medication();
		md.setMedication(new Reference(med));
		mr.setMedication(new Reference(med));

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(md);
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

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(md);
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

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(md);
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
		String encoded = ourCtx.newRDFParser().encodeResourceToString(p);
		ourLog.info(encoded);

		p = (Patient) ourCtx.newRDFParser().parseResource(encoded);
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
		obs.getEncounter().setResource(enc);

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newRDFParser().parseResource(Observation.class, encoded);
		assertEquals("#1", obs.getContained().get(0).getId());
		assertEquals("#2", obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getEncounter().getResource();
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
		obs.getEncounter().setReference("#1");
		obs.getContained().add(enc);

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = ourCtx.newRDFParser().parseResource(Observation.class, encoded);
		assertEquals("#1", obs.getContained().get(0).getId());
		assertEquals("#2", obs.getContained().get(1).getId());

		pt = (Patient) obs.getSubject().getResource();
		assertEquals("FAM", pt.getNameFirstRep().getFamily());

		enc = (Encounter) obs.getEncounter().getResource();
		assertEquals(Encounter.EncounterStatus.ARRIVED, enc.getStatus());
	}

	@Test
	public void testEncodeResourceWithMixedManualAndAutomaticContainedResourcesLocalLast2() {
		MedicationRequest mr = new MedicationRequest();
		Practitioner pract = new Practitioner().setActive(true);
		mr.getRequester().setResource(pract);

		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newRDFParser().parseResource(MedicationRequest.class, encoded);

		mr.setMedication(new Reference(new Medication().setStatus(Medication.MedicationStatus.ACTIVE)));
		encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(mr);
		ourLog.info(encoded);
		mr = ourCtx.newRDFParser().parseResource(MedicationRequest.class, encoded);

		assertEquals("#2", mr.getContained().get(0).getId());
		assertEquals("#1", mr.getContained().get(1).getId());
	}


	/**
	 * Test that long JSON strings don't get broken up
	 */
	@Test
	public void testNoBreakInLongString() {
		String longString = StringUtils.leftPad("", 100000, 'A');

		Patient p = new Patient();
		p.addName().setFamily(longString);
		String encoded = ourCtx.newRDFParser().setPrettyPrint(true).encodeResourceToString(p);

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

		IParser jsonParser = ourCtx.newRDFParser();
		IParser xmlParser = ourCtx.newXmlParser();
		jsonParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));
		xmlParser.setDontEncodeElements(Sets.newHashSet("id", "meta"));

		Patient parsed = jsonParser.parseResource(Patient.class, input);

		ourLog.info(jsonParser.setPrettyPrint(true).encodeResourceToString(parsed));
		assertThat(xmlParser.encodeResourceToString(parsed), containsString("Underweight"));
		assertThat(jsonParser.encodeResourceToString(parsed), containsString("Underweight"));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
