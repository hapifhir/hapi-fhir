package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.parser.XmlParserDstu2Test.TestPatientFor327;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;
import org.apache.commons.io.IOUtils;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceValidatorDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceValidatorDstu2Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu2();

	private FhirValidator createFhirValidator() {
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(true);
		return val;
	}

	private String logOperationOutcome(ValidationResult result) {
		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);
		return encoded;
	}

	/**
	 * See issue #50
	 */
	@Test()
	public void testOutOfBoundsDate() {
		Patient p = new Patient();
		p.setBirthDate(new DateDt("2000-12-31"));

		// Put in an invalid date
		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new StrictErrorHandler());

		String encoded = parser.setPrettyPrint(true).encodeResourceToString(p).replace("2000-12-31", "2000-15-31");
		ourLog.info(encoded);

		assertThat(encoded, StringContains.containsString("2000-15-31"));

		ValidationResult result = ourCtx.newValidator().validateWithResult(encoded);
		String resultString = parser.setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertEquals(2, ((OperationOutcome) result.toOperationOutcome()).getIssue().size());
		assertThat(resultString, StringContains.containsString("cvc-pattern-valid"));

		try {
			parser.parseResource(encoded);
			fail();
		} catch (DataFormatException e) {
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1821) + "[element=\"birthDate\"] Invalid attribute value \"2000-15-31\": " + Msg.code(1882) + "Invalid date/time format: \"2000-15-31\"", e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSchemaBundleValidator() throws IOException {
		String res = IOUtils.toString(ResourceValidatorDstu2Test.class.getResourceAsStream("/bundle-example.json"));
		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, res);

		FhirValidator val = createFhirValidator();

		ValidationResult result = val.validateWithResult(b);
		assertTrue(result.isSuccessful());

		MedicationOrder p = (MedicationOrder) b.getEntry().get(0).getResource();
		TimingDt timing = new TimingDt();
		timing.getRepeat().setDuration(123);
		timing.getRepeat().setDurationUnits((UnitsOfTimeEnum) null);
		p.getDosageInstructionFirstRep().setTiming(timing);

		result = val.validateWithResult(b);
		assertFalse(result.isSuccessful());
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.getOperationOutcome());
		ourLog.info(encoded);
		assertThat(encoded, containsString("tim-1:"));

	}

	@Test
	public void testSchemaBundleValidatorFails() throws IOException {
		String res = IOUtils.toString(ResourceValidatorDstu2Test.class.getResourceAsStream("/bundle-example.json"), StandardCharsets.UTF_8);
		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, res);


		FhirValidator val = createFhirValidator();

		ValidationResult validationResult = val.validateWithResult(b);
		assertTrue(validationResult.isSuccessful());

		MedicationOrder p = (MedicationOrder) b.getEntry().get(0).getResource();
		TimingDt timing = new TimingDt();
		timing.getRepeat().setDuration(123);
		timing.getRepeat().setDurationUnits((UnitsOfTimeEnum) null);
		p.getDosageInstructionFirstRep().setTiming(timing);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b));

		validationResult = val.validateWithResult(b);

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(validationResult.toOperationOutcome()));

		assertFalse(validationResult.isSuccessful());

		String encoded = logOperationOutcome(validationResult);
		assertThat(encoded, containsString("tim-1:"));
	}

	@Test
	public void testSchemaBundleValidatorIsSuccessful() throws IOException {
		String res = IOUtils.toString(ResourceValidatorDstu2Test.class.getResourceAsStream("/bundle-example.json"), StandardCharsets.UTF_8);
		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, res);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b));

		FhirValidator val = createFhirValidator();

		ValidationResult result = val.validateWithResult(b);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();

		assertTrue(result.isSuccessful(), result.toString());
		assertNotNull(operationOutcome);
		assertEquals(1, operationOutcome.getIssue().size());
	}

//	@Test
//	public void testValidateWithAny() {
//		Provenance prov = new Provenance();
//		prov.
//		
//		IParser p = FhirContext.forDstu2().newJsonParser().setPrettyPrint(true);
//		String messageString = p.encodeResourceToString(myPatient);
//		ourLog.info(messageString);
//
//		FhirValidator val = ourCtx.newValidator();
////		val.setValidateAgainstStandardSchema(true);
////		val.setValidateAgainstStandardSchematron(true);
//		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
//		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
//        
//		ValidationResult result = val.validateWithResult(messageString);
//
//		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
//		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
//		ourLog.info(encoded);
//
//		assertTrue(result.isSuccessful());
//	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSchemaResourceValidator() throws IOException {
		String res = IOUtils.toString(ResourceValidatorDstu2Test.class.getResourceAsStream("/patient-example-dicom.json"));
		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, res);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(false);

		ValidationResult result = val.validateWithResult(p);
		assertTrue(result.isSuccessful());

		p.getAnimal().getBreed().setText("The Breed");
		result = val.validateWithResult(p);
		assertFalse(result.isSuccessful());
		OperationOutcome operationOutcome = (OperationOutcome) result.getOperationOutcome();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals(1, operationOutcome.getIssue().size());
		assertThat(operationOutcome.getIssueFirstRep().getDetailsElement().getValue(), containsString("cvc-complex-type"));
	}

	@Test
	public void testSchematronResourceValidator() throws IOException {
		String res = IOUtils.toString(ResourceValidatorDstu2Test.class.getResourceAsStream("/patient-example-dicom.json"), StandardCharsets.UTF_8);
		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, res);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(true);

		ValidationResult validationResult = val.validateWithResult(p);
		assertTrue(validationResult.isSuccessful());

		p.getTelecomFirstRep().setValue("123-4567");
		validationResult = val.validateWithResult(p);
		assertFalse(validationResult.isSuccessful());
		OperationOutcome operationOutcome = (OperationOutcome) validationResult.toOperationOutcome();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals(1, operationOutcome.getIssue().size());
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics(), containsString("cpt-2:"));

		p.getTelecomFirstRep().setSystem(ContactPointSystemEnum.EMAIL);
		validationResult = val.validateWithResult(p);
		assertTrue(validationResult.isSuccessful());
	}

	/**
	 * Make sure that the elements that appear in all resources (meta, language, extension, etc)
	 * all appear in the correct order
	 */
	@Test
	public void testValidateResourceWithResourceElements() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);
		patient.setId("123");
		patient.getText().setDiv("<div>FOO</div>");
		patient.getText().setStatus(NarrativeStatusEnum.GENERATED);
		patient.getLanguage().setValue("en");
		patient.addUndeclaredExtension(true, "http://foo").setValue(new StringDt("MOD"));
		ResourceMetadataKeyEnum.UPDATED.put(patient, new InstantDt(new Date()));

		List<ResourceReferenceDt> conditions = new ArrayList<>();
		Condition condition = new Condition();
		condition.getPatient().setReference("Patient/123");
		condition.addBodySite().setText("BODY SITE");
		condition.getCode().setText("CODE");
		condition.setVerificationStatus(ConditionVerificationStatusEnum.CONFIRMED);
		conditions.add(new ResourceReferenceDt(condition));
		patient.setCondition(conditions);
		patient.addIdentifier().setSystem("http://foo").setValue("123");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		FhirValidator val = createFhirValidator();
		ValidationResult result = val.validateWithResult(encoded);

		String messageString = logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("No issues"));

	}

	/**
	 * See
	 * https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium=
	 * email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsJson() {
		PatientProfileDstu2 myPatient = new PatientProfileDstu2();
		myPatient.setColorPrimary(new CodeableConceptDt("http://example.com#animalColor", "furry-grey"));
		myPatient.setColorSecondary(new CodeableConceptDt("http://example.com#animalColor", "furry-white"));
		myPatient.setOwningOrganization(new ResourceReferenceDt("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().addFamily("FamilyName");
		myPatient.addUndeclaredExtension(new ExtensionDt().setUrl("http://foo.com/example").setValue(new StringDt("String Extension")));

		IParser p = FhirContext.forDstu2().newJsonParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(myPatient);
		ourLog.info(messageString);

		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"meta",
			"String Extension",
			"Organization/2.25.79433498044103547197447759549862032393",
			"furry-grey",
			"furry-white",
			"FamilyName"
		));
		assertThat(messageString, not(stringContainsInOrder(
			"extension",
			"meta"
		)));
		//@formatter:on

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));

		ValidationResult result = val.validateWithResult(messageString);

		logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	/**
	 * See
	 * https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium=
	 * email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsXml() {
		PatientProfileDstu2 myPatient = new PatientProfileDstu2();
		myPatient.setColorPrimary(new CodeableConceptDt("http://example.com#animalColor", "furry-grey"));
		myPatient.setColorSecondary(new CodeableConceptDt("http://example.com#animalColor", "furry-white"));
		myPatient.setOwningOrganization(new ResourceReferenceDt("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().addFamily("FamilyName");
		myPatient.addUndeclaredExtension(new ExtensionDt().setUrl("http://foo.com/example").setValue(new StringDt("String Extension")));

		IParser p = FhirContext.forDstu2().newXmlParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(myPatient);
		ourLog.info(messageString);

		//@formatter:off
		assertThat(messageString, stringContainsInOrder(
			"meta",
			"Organization/2.25.79433498044103547197447759549862032393",
			"furry-grey",
			"furry-white",
			"String Extension",
			"FamilyName"
		));
		assertThat(messageString, not(stringContainsInOrder(
			"extension",
			"meta"
		)));
		assertThat(messageString, containsString("url=\"http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorSecondary\""));
		assertThat(messageString, containsString("url=\"http://foo.com/example\""));
		//@formatter:on

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));

		ValidationResult result = val.validateWithResult(messageString);

		logOperationOutcome(result);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
