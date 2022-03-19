package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.dao.JpaPersistedResourceValidationSupport;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.IValidatorModule;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.AopTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu3ValidateTest extends BaseJpaDstu3Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu3ValidateTest.class);
	@Autowired
	private IValidatorModule myValidatorModule;
	@Autowired
	private CachingValidationSupport myValidationSupport;
	@Autowired
	private FhirInstanceValidator myFhirInstanceValidator;
	@Autowired
	@Qualifier(JpaConfig.JPA_VALIDATION_SUPPORT)
	private IValidationSupport myPersistedResourceValidationSupport;

	@Test
	public void testValidateChangedQuestionnaire() {
		Questionnaire q = new Questionnaire();
		q.setId("QUEST");
		q.addItem().setLinkId("A").setType(Questionnaire.QuestionnaireItemType.STRING).setRequired(true);
		myQuestionnaireDao.update(q);

		try {
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
			qr.getQuestionnaire().setReference("Questionnaire/QUEST");
			qr.addItem().setLinkId("A").addAnswer().setValue(new StringType("AAA"));

			MethodOutcome results = myQuestionnaireResponseDao.validate(qr, null, null, null, null, null, null);
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(results.getOperationOutcome()));
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			fail(e.toString());
		}


		q = new Questionnaire();
		q.setId("QUEST");
		q.addItem().setLinkId("B").setType(Questionnaire.QuestionnaireItemType.STRING).setRequired(true);
		myQuestionnaireDao.update(q);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qr.getQuestionnaire().setReference("Questionnaire/QUEST");
		qr.addItem().setLinkId("A").addAnswer().setValue(new StringType("AAA"));

		MethodOutcome results = myQuestionnaireResponseDao.validate(qr, null, null, null, null, null, null);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(results.getOperationOutcome()));

		ourLog.info("Clearing cache");
		myValidationSupport.invalidateCaches();
		myFhirInstanceValidator.invalidateCaches();
		ProxyUtil.getSingletonTarget(myPersistedResourceValidationSupport, JpaPersistedResourceValidationSupport.class).clearCaches();

		try {
			myQuestionnaireResponseDao.validate(qr, null, null, null, null, null, null);
			fail();
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			// good
		}

	}


	@Test
	public void testExpandLocalCodeSystemWithExplicitCodes() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/dstu3/iar/CodeSystem-iar-citizenship-status.xml");
		myCodeSystemDao.create(cs);

		ValueSet vs = loadResourceFromClasspath(ValueSet.class, "/dstu3/iar/ValueSet-iar-citizenship-status.xml");
		myValueSetDao.create(vs);

		ValueSet expansion = myValueSetDao.expandByIdentifier("http://ccim.on.ca/fhir/iar/ValueSet/iar-citizenship-status", null);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));

		assertThat(expansion.getExpansion().getContains().stream().map(t->t.getCode()).collect(Collectors.toList()), containsInAnyOrder(
			"CDN", "PR", "TR", "REF", "UNK", "ASKU"
		));
	}


	@AfterEach
	public void after() {
		FhirInstanceValidator val = AopTestUtils.getTargetObject(myValidatorModule);
		val.setBestPracticeWarningLevel(BestPracticeWarningLevel.Warning);
	}


	@Test
	public void testValidateWithCanonicalReference() {
		FhirInstanceValidator val = AopTestUtils.getTargetObject(myValidatorModule);
		BestPracticeWarningLevel bestPracticeWarningLevel = BestPracticeWarningLevel.Ignore;
		val.setBestPracticeWarningLevel(bestPracticeWarningLevel);

		ValueSet vs = new ValueSet();
		vs.setId("MYVS");
		vs.setUrl("http://myvs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://hl7.org/fhir/administrative-gender")
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("male"))
			.addConcept(new ValueSet.ConceptReferenceComponent().setCode("female"));
		myValueSetDao.update(vs);

		Questionnaire q = new Questionnaire();
		q.setId("MYQ");
		q.setUrl("http://hl7.org/fhir/Questionnaire/myq");
		q.addItem()
			.setLinkId("LINKID")
			.setType(Questionnaire.QuestionnaireItemType.CHOICE)
			.setOptions(new Reference().setReference("ValueSet/MYVS"));
		myQuestionnaireDao.update(q);

		// Validate with matching code
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qr.setQuestionnaire(new Reference("Questionnaire/MYQ"));
		qr.addItem()
			.setLinkId("LINKID")
			.addAnswer()
			.setValue(new Coding().setSystem("http://hl7.org/fhir/administrative-gender").setCode("aaa").setDisplay("AAAA"));

		// Validate as resource
		try {
			MethodOutcome outcome = myQuestionnaireResponseDao.validate(qr, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
			fail();
		} catch (PreconditionFailedException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info(encoded);
			assertThat(encoded, containsString("is not in the options value set"));
		}

		// Validate as string
		try {
			String raw = myFhirContext.newJsonParser().encodeResourceToString(qr);
			MethodOutcome outcome = myQuestionnaireResponseDao.validate(qr, null, raw, EncodingEnum.JSON, ValidationModeEnum.CREATE, null, mySrd);
			OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
			fail();
		} catch (PreconditionFailedException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info(encoded);
			assertThat(encoded, containsString("is not in the options value set"));
		}

	}


	@Test
	public void testValidateStructureDefinition() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/sd-david-dhtest7.json"), StandardCharsets.UTF_8);
		StructureDefinition sd = myFhirContext.newJsonParser().parseResource(StructureDefinition.class, input);


		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		ourLog.info("Done validation");

		StopWatch sw = new StopWatch();
		ourLog.info("Starting validation");
		try {
			myStructureDefinitionDao.validate(sd, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			// ok
		}
		ourLog.info("Done validation in {}ms", sw.getMillis());

	}

	@Test
	public void testValidateDocument() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/document-bundle-dstu3.json"), StandardCharsets.UTF_8);
		Bundle document = myFhirContext.newJsonParser().parseResource(Bundle.class, input);


		ourLog.info("Starting validation");
		try {
			MethodOutcome outcome = myBundleDao.validate(document, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		} catch (PreconditionFailedException e) {
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
		}
		ourLog.info("Done validation");

//		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome()));
	}

	@Test
	@Disabled
	public void testValidateResourceContainingProfileDeclarationJson() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationJson";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.JSON);

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Element '.subject': minimum required = 1, but only found 0"));
		assertThat(ooString, containsString("Element encounter @ : max allowed = 0, but found 1"));
		assertThat(ooString, containsString("Element '.device': minimum required = 1, but only found 0"));
	}

	@Test
	@Disabled
	public void testValidateResourceContainingProfileDeclarationXml() throws Exception {
		String methodName = "testValidateResourceContainingProfileDeclarationXml";
		OperationOutcome outcome = doTestValidateResourceContainingProfileDeclaration(methodName, EncodingEnum.XML);

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Element '/f:Observation.subject': minimum required = 1, but only found 0"));
		assertThat(ooString, containsString("Element encounter @ /f:Observation: max allowed = 0, but found 1"));
		assertThat(ooString, containsString("Element '/f:Observation.device': minimum required = 1, but only found 0"));
	}

	private OperationOutcome doTestValidateResourceContainingProfileDeclaration(String methodName, EncodingEnum enc) throws IOException {
		Bundle vss = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/dstu3/model/valueset/valuesets.xml");
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-status"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-category"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-codes"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-methods"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-valueabsentreason"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-interpretation"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "body-site"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "referencerange-meaning"), mySrd);
		myValueSetDao.update((ValueSet) findResourceByIdInBundle(vss, "observation-relationshiptypes"), mySrd);

		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/org/hl7/fhir/dstu3/model/profile/devicemetricobservation.profile.xml");
		sd.setId(new IdType());
		sd.setUrl("http://example.com/foo/bar/" + methodName);
		myStructureDefinitionDao.create(sd, mySrd);

		Observation input = new Observation();
		input.getMeta().getProfile().add(new IdType(sd.getUrl()));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getContext().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		String encoded = null;
		MethodOutcome outcome = null;
		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		switch (enc) {
			case JSON:
				encoded = myFhirContext.newJsonParser().encodeResourceToString(input);
				try {
					myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
					fail();
				} catch (PreconditionFailedException e) {
					return (OperationOutcome) e.getOperationOutcome();
				}
				break;
			case XML:
				encoded = myFhirContext.newXmlParser().encodeResourceToString(input);
				try {
					myObservationDao.validate(input, null, encoded, EncodingEnum.XML, mode, null, mySrd);
					fail();
				} catch (PreconditionFailedException e) {
					return (OperationOutcome) e.getOperationOutcome();
				}
				break;
		}

		throw new IllegalStateException(); // shouldn't get here
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationInvalid() {
		String methodName = "testValidateResourceContainingProfileDeclarationInvalid";

		Observation input = new Observation();
		String profileUri = "http://example.com/StructureDefinition/" + methodName;
		input.getMeta().getProfile().add(new IdType(profileUri));

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getContext().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationModeEnum mode = ValidationModeEnum.CREATE;
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(input);
		try {
			// Expected to throw exception
			MethodOutcome output = myObservationDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output.getOperationOutcome()));
			fail();
		} catch (PreconditionFailedException e) {
			OperationOutcome oo = (OperationOutcome) e.getOperationOutcome();
			String outputString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
			ourLog.info(outputString);
			assertThat(outputString, containsString("Profile reference 'http://example.com/StructureDefinition/testValidateResourceContainingProfileDeclarationInvalid' has not been checked because it is unknown"));
		}
	}

	@Test
	public void testValidateForCreate() {
		String methodName = "testValidateForCreate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must not be populated"));
		}

		pat.setId("");
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.CREATE, null, mySrd);

	}

	@Test
	public void testValidateForUpdate() {
		String methodName = "testValidateForUpdate";

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must be populated"));
		}

	}

	@Test
	public void testValidateForUpdateWithContained() {
		String methodName = "testValidateForUpdate";

		Organization org = new Organization();
		org.setId("#123");

		Patient pat = new Patient();
		pat.setId("Patient/123");
		pat.addName().setFamily(methodName);
		myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);

		pat.setId("");

		try {
			myPatientDao.validate(pat, null, null, null, ValidationModeEnum.UPDATE, null, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("ID must be populated"));
		}

	}

	@Test
	public void testValidateForDelete() {
		String methodName = "testValidateForDelete";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addName().setFamily(methodName);
		pat.getManagingOrganization().setReference(orgId.getValue());
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		OperationOutcome outcome = null;
		try {
			myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			outcome = (OperationOutcome) e.getOperationOutcome();
		}

		String ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Unable to delete Organization"));

		pat.setId(patId);
		pat.getManagingOrganization().setReference("");
		myPatientDao.update(pat, mySrd);

		outcome = (OperationOutcome) myOrganizationDao.validate(null, orgId, null, null, ValidationModeEnum.DELETE, null, mySrd).getOperationOutcome();
		ooString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(ooString);
		assertThat(ooString, containsString("Ok to delete"));

	}

	private IBaseResource findResourceByIdInBundle(Bundle vss, String name) {
		IBaseResource retVal = null;
		for (BundleEntryComponent next : vss.getEntry()) {
			if (next.getResource().getIdElement().getIdPart().equals(name)) {
				retVal = next.getResource();
				break;
			}
		}
		if (retVal == null) {
			fail("Can't find VS: " + name);
		}
		return retVal;
	}

	/**
	 * Format has changed, this is out of date
	 */
	@Test
	@Disabled
	public void testValidateNewQuestionnaireFormat() throws Exception {
		String input = IOUtils.toString(FhirResourceDaoDstu3ValidateTest.class.getResourceAsStream("/questionnaire_dstu3.xml"));
		try {
			MethodOutcome results = myQuestionnaireDao.validate(null, null, input, EncodingEnum.XML, ValidationModeEnum.UPDATE, null, mySrd);
			OperationOutcome oo = (OperationOutcome) results.getOperationOutcome();
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
		} catch (PreconditionFailedException e) {
			// this is a failure of the test
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			throw e;
		}
	}

	@Test
	public void testValidateUsingDifferentialProfile() throws IOException {
		StructureDefinition sd = loadResourceFromClasspath(StructureDefinition.class, "/dstu3/profile-differential-patient-dstu3.json");
		myStructureDefinitionDao.create(sd);

		Patient p = new Patient();
		p.getText().getDiv().setValue("<div>hello</div>");
		p.getText().setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/MyPatient421");
		p.setActive(true);

		String raw = myFhirContext.newJsonParser().encodeResourceToString(p);
		try {
			MethodOutcome outcome = myPatientDao.validate(p, null, raw, EncodingEnum.JSON, null, null, mySrd);

			String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.getOperationOutcome());
			ourLog.info("OO: {}", encoded);
			assertThat(encoded, containsString("No issues detected"));
		} catch (PreconditionFailedException e) {
			// not expected, but let's log the error
			ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			fail(e.toString());
		}
	}


}
