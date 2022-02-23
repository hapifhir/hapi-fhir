package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SchemaBaseValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import ca.uhn.fhir.validation.schematron.SchematronBaseValidator;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.conformance.ProfileUtilities;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;
import org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus;
import org.hl7.fhir.dstu3.model.EligibilityResponse.BenefitComponent;
import org.hl7.fhir.dstu3.model.Narrative.NarrativeStatus;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceValidatorDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceValidatorDstu3Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}",
				new Object[]{index, next.getSeverity(), next.getLocationString(), next.getMessage()});
			index++;

			if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	/**
	 * See issue #50
	 */
	@Test()
	public void testOutOfBoundsDate() {
		Patient p = new Patient();
		p.setBirthDateElement(new DateType("2000-12-31"));

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
			assertEquals(Msg.code(1851) + "DataFormatException at [[row,col {unknown-source}]: [2,4]]: " + Msg.code(1821) + "[element=\"birthDate\"] Invalid attribute value \"2000-15-31\": Invalid date/time format: \"2000-15-31\"", e.getMessage());
		}
	}

	@Test
	@Disabled
	public void testValidateCareTeamProfile() {

		CareTeam careTeam = new CareTeam();
		careTeam
			.addParticipant()
			.setMember(new Reference("Practitioner/1647bbb2-3b12-43cc-923c-a475f817e881"))
			.setOnBehalfOf(new Reference("Organization/5859a28f-01e7-42d8-a8ba-48b31679a828"));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(careTeam);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));


		ValidationResult result = val.validateWithResult(encoded);
		String resultString = parser.setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertEquals(2, ((OperationOutcome) result.toOperationOutcome()).getIssue().size());
		assertThat(resultString, StringContains.containsString("cvc-pattern-valid"));

	}

	@Test
	public void testValidateCareTeamXsd() {

		CareTeam careTeam = new CareTeam();
		careTeam
			.addParticipant()
			.setMember(new Reference("http://example.com/Practitioner/1647bbb2-3b12-43cc-923c-a475f817e881"))
			.setOnBehalfOf(new Reference("Organization/5859a28f-01e7-42d8-a8ba-48b31679a828"));

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = parser.encodeResourceToString(careTeam);

		ourLog.info(encoded);

		FhirValidator val = ourCtx.newValidator();

		ValidationResult result = val.validateWithResult(encoded);
		String resultString = parser.setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertThat(resultString, containsString("No issues detected during validation"));

	}

	@Test
	public void testValidateCodeableConceptContainingOnlyBadCode() {
		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://hl7.org/fhir/v3/MaritalStatus").setCode("FOO");

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult output = val.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);

		assertEquals("None of the codings provided are in the value set 'Marital Status Codes' (http://hl7.org/fhir/ValueSet/marital-status), and a coding should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable) (codes = http://hl7.org/fhir/v3/MaritalStatus#FOO)", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.WARNING, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateCodeableConceptContainingOnlyGoodCode() {
		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://hl7.org/fhir/v3/MaritalStatus").setCode("M");

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult output = val.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(0, all.size());
		assertEquals(0, output.getMessages().size());
	}

	@Test
	@Disabled
	public void testValidateProfileWithExtension() throws IOException, FHIRException {
		PrePopulatedValidationSupport valSupport = new PrePopulatedValidationSupport(ourCtx);
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ourCtx);
		ValidationSupportChain support = new ValidationSupportChain(valSupport, defaultSupport);

		// Prepopulate SDs
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/dstu3/myconsent-profile.xml"));
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/dstu3/myconsent-ext.xml"));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(support));

		Consent input = ourCtx.newJsonParser().parseResource(Consent.class, IOUtils.toString(ResourceValidatorDstu3Test.class.getResourceAsStream("/dstu3/myconsent-resource.json")));

		ValidationResult output = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(0, all.size());
		assertEquals(0, output.getMessages().size());

	}

	private StructureDefinition loadStructureDefinition(DefaultProfileValidationSupport theDefaultValSupport, String theResName) throws IOException, FHIRException {
		StructureDefinition derived = ourCtx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(ResourceValidatorDstu3Test.class.getResourceAsStream(theResName)));
		StructureDefinition base = (StructureDefinition) theDefaultValSupport.fetchStructureDefinition(derived.getBaseDefinition());
		Validate.notNull(base);

		IWorkerContext worker = new HapiWorkerContext(ourCtx, theDefaultValSupport);
		List<ValidationMessage> issues = new ArrayList<>();
		ProfileUtilities profileUtilities = new ProfileUtilities(worker, issues, null);
		profileUtilities.generateSnapshot(base, derived, "", "");

		return derived;
	}


	@Test
	@Disabled
	public void testValidateDifferentPropertyButSameStartsWithPath() throws Exception {

		EligibilityResponse fhirObj = new EligibilityResponse();
		BenefitComponent benComp = fhirObj.addInsurance().addBenefitBalance().addFinancial();
		// Test between .benefit[x] and benefitUsed[x]
//		benComp.setBenefitUsed(new UnsignedIntType(2));

		String input = ourCtx.newXmlParser().encodeResourceToString(fhirObj);

		FhirValidator validator = ourCtx.newValidator();
		validator.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = validator.validateWithResult(input);
		// we should get some results, not an exception
		assertEquals(4, result.getMessages().size());
	}

	@Test
	public void testValidateJsonNumericId() {
		String input = "{\"resourceType\": \"Patient\",\n" +
			"  \"id\": 123,\n" +
			"  \"meta\": {\n" +
			"    \"versionId\": \"29\",\n" +
			"    \"lastUpdated\": \"2015-12-22T19:53:11.000Z\"\n" +
			"  },\n" +
			"  \"communication\": {\n" +
			"    \"language\": {\n" +
			"      \"coding\": [\n" +
			"        {\n" +
			"          \"system\": \"urn:ietf:bcp:47\",\n" +
			"          \"code\": \"hi\",\n" +
			"          \"display\": \"Hindi\",\n" +
			"          \"userSelected\": false\n" +
			"        }],\n" +
			"      \"text\": \"Hindi\"\n" +
			"    },\n" +
			"    \"preferred\": true\n" +
			"  }\n" +
			"}";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));
		ValidationResult output = val.validateWithResult(input);

		OperationOutcome operationOutcome = (OperationOutcome) output.toOperationOutcome();
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Error parsing JSON: the primitive value must be a string"));

	}

	/**
	 * Per email from Jon Zammit
	 */
	@Test
	@Disabled
	public void testValidateQuestionnaire() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/questionnaire_jon_z_20160506.xml"), StandardCharsets.UTF_8);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(input);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String ooencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(ooencoded);

		assertTrue(result.isSuccessful());
	}

	/**
	 * TODO: re-enable this
	 */
	@Test
	public void testValidateQuestionnaireWithCanonicalUrl() {
		String input = "{\n" +
			"  \"resourceType\": \"Questionnaire\",\n" +
			"  \"url\": \"http://some.example.url\",\n" +
			"  \"status\": \"active\",\n" +
			"  \"subjectType\": [\n" +
			"    \"Patient\"\n" +
			"  ],\n" +
			"  \"item\": [\n" +
			"    {\n" +
			"      \"linkId\": \"example-question\",\n" +
			"      \"text\": \"Is the sky blue?\",\n" +
			"      \"type\": \"choice\",\n" +
			"      \"options\": {\n" +
			"        \"reference\": \"http://loinc.org/vs/LL3044-6\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";

		Questionnaire q = new Questionnaire();
		q = ourCtx.newJsonParser().parseResource(Questionnaire.class, input);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(q);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);

	}

	/**
	 * Make sure that the elements that appear in all resources (meta, language, extension, etc) all appear in the correct order
	 */
	@Test
	public void testValidateResourceWithResourceElements() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDate(new Date());
		patient.setId("123");
		patient.getText().setDivAsString("<div lang=\"en\">FOO</div>");
		patient.getText().setStatus(NarrativeStatus.GENERATED);
		patient.getLanguageElement().setValue("en");
		patient.addExtension().setUrl("http://foo").setValue(new StringType("MOD"));
		patient.getMeta().setLastUpdated(new Date());

		List<Reference> conditions = new ArrayList<Reference>();
		Condition condition = new Condition();
		condition.getSubject().setReference("Patient/123");
		condition.addBodySite().setText("BODY SITE");
		condition.getCode().setText("CODE");
		condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
		condition.setVerificationStatus(ConditionVerificationStatus.CONFIRMED);
		conditions.add(new Reference(condition));
		patient.setCondition(conditions);
		patient.addIdentifier().setSystem("http://foo").setValue("123");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(encoded);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String ooencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(ooencoded);

		assertTrue(result.isSuccessful());

		assertThat(ooencoded, containsString("Unknown extension http://foo"));
	}

	/**
	 * See https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium= email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsJson() {
		PatientProfileDstu3 myPatient = new PatientProfileDstu3();
		myPatient.setId("1");
		myPatient.setColorPrimary(new CodeableConcept().addCoding(new Coding().setSystem("http://example.com#animalColor").setCode("furry-grey")));
		myPatient.setColorSecondary(new CodeableConcept().addCoding(new Coding().setSystem("http://example.com#animalColor").setCode("furry-white")));
		myPatient.setOwningOrganization(new Reference("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().setFamily("FamilyName");
		myPatient.addExtension().setUrl("http://foo.com/example").setValue(new StringType("String Extension"));

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true);
		String messageString = p.encodeResourceToString(myPatient);
//		ourLog.info(messageString);

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

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new SchemaBaseValidator(ourCtx));
		val.registerValidatorModule(new SchematronBaseValidator(ourCtx));
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(messageString);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	/**
	 * See https://groups.google.com/d/msgid/hapi-fhir/a266083f-6454-4cf0-a431-c6500f052bea%40googlegroups.com?utm_medium= email&utm_source=footer
	 */
	@Test
	public void testValidateWithExtensionsXml() {
		PatientProfileDstu3 myPatient = new PatientProfileDstu3();
		myPatient.setId("1");
		myPatient.setColorPrimary(new CodeableConcept().addCoding(new Coding().setSystem("http://example.com#animalColor").setCode("furry-grey")));
		myPatient.setColorSecondary(new CodeableConcept().addCoding(new Coding().setSystem("http://example.com#animalColor").setCode("furry-white")));
		myPatient.setOwningOrganization(new Reference("Organization/2.25.79433498044103547197447759549862032393"));
		myPatient.addName().setFamily("FamilyName");
		myPatient.addExtension().setUrl("http://foo.com/example").setValue(new StringType("String Extension"));

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
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
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(messageString);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());

		assertThat(messageString, containsString("valueReference"));
		assertThat(messageString, not(containsString("valueResource")));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@ResourceDef(name = "Patient")
	public static class TestPatientFor327 extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "testCondition")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "http://testCondition", definedLocally = true, isModifier = false)
		private List<Reference> testConditions = null;

		public List<Reference> getConditions() {
			return this.testConditions;
		}

		public void setCondition(List<Reference> ref) {
			this.testConditions = ref;
		}
	}

}
