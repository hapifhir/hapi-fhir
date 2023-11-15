package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.util.ParametersUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT_PATIENT_REFERENCE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT_PERFORMER_REFERENCE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT_BIRTHDATE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT_NAME;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;
import static ca.uhn.fhir.rest.api.Constants.PARAM_OLD_COVERAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SuppressWarnings("Duplicates")
public class PatientMemberMatchOperationR4Test extends BaseResourceProviderR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientMemberMatchOperationR4Test.class);

	private static final String ourQuery = "/Patient/$member-match?_format=json";
	private static final String EXISTING_COVERAGE_ID = "cov-id-123";
	private static final String EXISTING_COVERAGE_IDENT_SYSTEM = "http://centene.com/insurancePlanIds";
	private static final String EXISTING_COVERAGE_IDENT_VALUE = "U1234567890";
	private static final String EXISTING_COVERAGE_PATIENT_IDENT_SYSTEM = "http://oldhealthplan.example.com";
	private static final String EXISTING_COVERAGE_PATIENT_IDENT_VALUE = "DHU-55678";
	private static final String CONSENT_POLICY_SENSITIVE_DATA_URI = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition-hrex-consent.html#sensitive";
	private static final String CONSENT_POLICY_REGULAR_DATA_URI = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition-hrex-consent.html#regular";

	private Identifier ourExistingCoverageIdentifier;
	private Patient myPatient;
	private Coverage oldCoverage; // Old Coverage (must match field)
	private Coverage newCoverage; // New Coverage (must return unchanged)
	private Consent myConsent;

	@Autowired
	MemberMatcherR4Helper myMemberMatcherR4Helper;

	@Autowired
	MemberMatchR4ResourceProvider theMemberMatchR4ResourceProvider;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setEverythingIncludesFetchPageSize(new JpaStorageSettings().getEverythingIncludesFetchPageSize());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myServer.getRestfulServer().unregisterProvider(theMemberMatchR4ResourceProvider);
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myPatient = (Patient) new Patient().setName(Lists.newArrayList(new HumanName()
				.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person")))
			.setBirthDateElement(new DateType("2020-01-01"))
			.setId("Patient/A123");

		// Old Coverage (must match field)
		oldCoverage = (Coverage) new Coverage()
			.setId(EXISTING_COVERAGE_ID);

		// New Coverage (must return unchanged)
		newCoverage = (Coverage) new Coverage()
			.setIdentifier(Lists.newArrayList(new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")))
			.setId("AA87654");

		myConsent = new Consent()
			.setStatus(Consent.ConsentState.ACTIVE)
			.setScope(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/consentscope", "patient-privacy", null)))
			.addPolicy(new Consent.ConsentPolicyComponent().setUri(CONSENT_POLICY_SENSITIVE_DATA_URI))
			.setPatient(new Reference("Patient/A123"))
			.addPerformer(new Reference("Patient/A123"));
		myServer.getRestfulServer().registerProvider(theMemberMatchR4ResourceProvider);
		myMemberMatcherR4Helper.setRegularFilterSupported(false);
	}

	private void createCoverageWithBeneficiary(
		boolean theAssociateBeneficiaryPatient, boolean includeBeneficiaryIdentifier) {

		Patient member = new Patient();
		if (theAssociateBeneficiaryPatient) {
			// Patient
			member.setName(Lists.newArrayList(new HumanName()
				.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person")))
				.setBirthDateElement(new DateType("2020-01-01"))
				.setId("Patient/A123");
			if (includeBeneficiaryIdentifier) {
				member.setIdentifier(Collections.singletonList(new Identifier()
					.setSystem(EXISTING_COVERAGE_PATIENT_IDENT_SYSTEM).setValue(EXISTING_COVERAGE_PATIENT_IDENT_VALUE)));
			}
			member.setActive(true);
			myClient.update().resource(member).execute();
		}

		// Coverage
		ourExistingCoverageIdentifier = new Identifier()
			.setSystem(EXISTING_COVERAGE_IDENT_SYSTEM).setValue(EXISTING_COVERAGE_IDENT_VALUE);
		Coverage ourExistingCoverage = (Coverage) new Coverage()
			.setStatus(Coverage.CoverageStatus.ACTIVE)
			.setIdentifier(Collections.singletonList(ourExistingCoverageIdentifier));

		if (theAssociateBeneficiaryPatient) {
			ourExistingCoverage.setBeneficiary(new Reference(member))
				.setId(EXISTING_COVERAGE_ID);
		}

		myClient.create().resource(ourExistingCoverage).execute().getId().toUnqualifiedVersionless().getValue();
	}

	@Test
	public void testMemberMatchByCoverageId() throws Exception {
		createCoverageWithBeneficiary(true, true);

		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
	}


	@Test
	public void testCoverageNoBeneficiaryReturns422() throws Exception {
		createCoverageWithBeneficiary(false, false);

		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find beneficiary for coverage.");
	}

	@Test
	public void testCoverageBeneficiaryNoIdentifierReturns422() throws Exception {
		createCoverageWithBeneficiary(true, false);

		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Coverage beneficiary does not have an identifier.");
	}

	@Test
	public void testCoverageNoMatchingPatientFamilyNameReturns422() throws Exception {
		createCoverageWithBeneficiary(true, true);

		myPatient.setName(Lists.newArrayList(new HumanName().setUse(HumanName.NameUse.OFFICIAL).setFamily("Smith")));
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find matching patient for coverage.");
	}

	@Test
	public void testCoverageNoMatchingPatientBirthdateReturns422() throws Exception {
		createCoverageWithBeneficiary(true, false);

		myPatient.setBirthDateElement(new DateType("2000-01-01"));
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find matching patient for coverage.");
	}

	@Test
	public void testRegularContentProfileAccessWithRegularNotAllowedReturns422() throws Exception {
		createCoverageWithBeneficiary(true, true);
		myConsent.getPolicy().get(0).setUri(CONSENT_POLICY_REGULAR_DATA_URI);
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Consent policy does not match the data release segmentation capabilities.");
	}

	@Test
	public void testSensitiveContentProfileAccessWithRegularNotAllowed() throws Exception {
		createCoverageWithBeneficiary(true, true);
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
	}

	@Test
	public void testRegularContentProfileAccessWithRegularAllowed() throws Exception {
		createCoverageWithBeneficiary(true, true);
		myConsent.getPolicy().get(0).setUri(CONSENT_POLICY_REGULAR_DATA_URI);
		myMemberMatcherR4Helper.setRegularFilterSupported(true);
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
	}

	@Test
	public void testConsentReturns() throws Exception {
		createCoverageWithBeneficiary(true, true);
		myConsent.getPolicy().get(0).setUri(CONSENT_POLICY_REGULAR_DATA_URI);
		myMemberMatcherR4Helper.setRegularFilterSupported(true);
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
		validateResponseConsent(parametersResponse, myConsent);
	}

	@Test
	public void testMemberMatchByCoverageIdentifier() throws Exception {
		createCoverageWithBeneficiary(true, true);

		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
	}

	/**
	 * Validates that second resource from the response is same as the received coverage
	 */
	private void validateNewCoverage(Parameters theResponse, Coverage theOriginalCoverage) {
		List<IBase> patientList = ParametersUtil.getNamedParameters(this.getFhirContext(), theResponse, PARAM_NEW_COVERAGE);
		assertEquals(1, patientList.size());
		Coverage respCoverage = (Coverage) theResponse.getParameter().get(1).getResource();

		assertEquals("Coverage/" + theOriginalCoverage.getId(), respCoverage.getId());
		assertEquals(theOriginalCoverage.getIdentifierFirstRep().getSystem(), respCoverage.getIdentifierFirstRep().getSystem());
		assertEquals(theOriginalCoverage.getIdentifierFirstRep().getValue(), respCoverage.getIdentifierFirstRep().getValue());
	}

	private void validateConsentPatientAndPerformerRef(Patient thePatient, Consent theConsent) {
		String patientRef = thePatient.getIdElement().toUnqualifiedVersionless().getValue();
		assertEquals(patientRef, theConsent.getPatient().getReference());
		assertEquals(patientRef, theConsent.getPerformer().get(0).getReference());
	}

	private void validateMemberPatient(Parameters response) {
//		parameter MemberPatient must have a new identifier with:
//		{
//			"use": "usual",
//			"type": {
//			"coding": [
//				{
//					"system": "http://terminology.hl7.org/CodeSystem/v2-0203",
//					"code": "UMB",
//					"display": "Member Number",
//					"userSelected": false
//				}
//       	],
//				"text": "Member Number"
//			},
//			"system": COVERAGE_PATIENT_IDENT_SYSTEM,
//			"value": COVERAGE_PATIENT_IDENT_VALUE
//		}
		List<IBase> patientList = ParametersUtil.getNamedParameters(this.getFhirContext(), response, PARAM_MEMBER_PATIENT);
		assertEquals(1, patientList.size());
		Patient resultPatient = (Patient) response.getParameter().get(0).getResource();

		assertNotNull(resultPatient.getIdentifier());
		assertEquals(1, resultPatient.getIdentifier().size());
		Identifier addedIdentifier = resultPatient.getIdentifier().get(0);
		assertEquals(Identifier.IdentifierUse.USUAL, addedIdentifier.getUse());
		checkCoding(addedIdentifier.getType());
		assertEquals(EXISTING_COVERAGE_PATIENT_IDENT_SYSTEM, addedIdentifier.getSystem());
		assertEquals(EXISTING_COVERAGE_PATIENT_IDENT_VALUE, addedIdentifier.getValue());
	}

	@Test
	public void testNoCoverageMatchFound() throws Exception {
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find coverage for member");
	}

	@Test
	public void testConsentUpdatePatientAndPerformer() throws Exception {
		createCoverageWithBeneficiary(true, true);
		Parameters inputParameters = buildInputParameters(myPatient, oldCoverage, newCoverage, myConsent);
		Parameters parametersResponse = performOperation(myServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		Consent respConsent = validateResponseConsent(parametersResponse, myConsent);
		validateConsentPatientAndPerformerRef(myPatient, respConsent);
	}

	private Parameters buildInputParameters(Patient thePatient, Coverage theOldCoverage, Coverage theNewCoverage, Consent theConsent) {
		Parameters p = new Parameters();
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_MEMBER_PATIENT, thePatient);
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_OLD_COVERAGE, theOldCoverage);
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_NEW_COVERAGE, theNewCoverage);
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_CONSENT, theConsent);
		return p;
	}

	private Parameters performOperation(String theUrl,
													EncodingEnum theEncoding, Parameters theInputParameters) throws Exception {

		HttpPost post = new HttpPost(theUrl);
		post.addHeader(Constants.HEADER_ACCEPT_ENCODING, theEncoding.toString());
		post.setEntity(new ResourceEntity(this.getFhirContext(), theInputParameters));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());

			return theEncoding.newParser(myFhirContext).parseResource(Parameters.class,
				responseString);
		}
	}

	private void performOperationExpecting422(String theUrl, EncodingEnum theEncoding,
															Parameters theInputParameters, String theExpectedErrorMsg) throws Exception {

		HttpPost post = new HttpPost(theUrl);
		post.addHeader(Constants.HEADER_ACCEPT_ENCODING, theEncoding.toString());
		post.setEntity(new ResourceEntity(this.getFhirContext(), theInputParameters));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response: {}", responseString);
			assertEquals(422, response.getStatusLine().getStatusCode());
			assertThat(responseString, containsString(theExpectedErrorMsg));
		}
	}

	private void checkCoding(CodeableConcept theType) {
		// must match:
		//	 "coding": [
		//	  	{
		//	  		"system": "http://terminology.hl7.org/CodeSystem/v2-0203",
		//	  		"code": "MB",
		//	  		"display": "Member Number",
		//	  		"userSelected": false
		//		}
		//	 * ]
		Coding coding = theType.getCoding().get(0);
		assertEquals("http://terminology.hl7.org/CodeSystem/v2-0203", coding.getSystem());
		assertEquals("MB", coding.getCode());
		assertEquals("Member Number", coding.getDisplay());
		assertFalse(coding.getUserSelected());
	}

	/**
	 * Validates that consent from the response is same as the received consent with additional identifier and extension
	 */
	private Consent validateResponseConsent(Parameters theResponse, Consent theOriginalConsent) {
		List<IBase> consentList = ParametersUtil.getNamedParameters(this.getFhirContext(), theResponse, PARAM_CONSENT);
		assertEquals(1, consentList.size());
		Consent respConsent = (Consent) theResponse.getParameter().get(2).getResource();

		assertEquals(theOriginalConsent.getScope().getCodingFirstRep().getSystem(), respConsent.getScope().getCodingFirstRep().getSystem());
		assertEquals(theOriginalConsent.getScope().getCodingFirstRep().getCode(), respConsent.getScope().getCodingFirstRep().getCode());
		assertEquals(myMemberMatcherR4Helper.CONSENT_IDENTIFIER_CODE_SYSTEM, respConsent.getIdentifier().get(0).getSystem());
		assertNotNull(respConsent.getIdentifier().get(0).getValue());
		return respConsent;
	}

	@Nested
	public class ValidateParameterErrors {
		private Patient ourPatient;
		private Coverage ourOldCoverage;
		private Coverage ourNewCoverage;
		private Consent ourConsent;

		@BeforeEach
		public void beforeValidateParameterErrors() {
			ourPatient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

			ourOldCoverage = new Coverage();
			ourOldCoverage.setId(EXISTING_COVERAGE_ID);

			ourNewCoverage = new Coverage();
			ourNewCoverage.setId("AA87654");
			ourNewCoverage.setIdentifier(Lists.newArrayList(
				new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

			ourConsent = new Consent();
			ourConsent.setStatus(Consent.ConsentState.ACTIVE);
		}

		@Test
		public void testInvalidPatient() throws Exception {
			Parameters inputParameters = buildInputParameters(new Patient(), ourOldCoverage, ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_MEMBER_PATIENT + "\\\" is required.");
		}

		@Test
		public void testInvalidOldCoverage() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, new Coverage(), ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_OLD_COVERAGE + "\\\" is required.");
		}

		@Test
		public void testInvalidNewCoverage() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, new Coverage(), ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_NEW_COVERAGE + "\\\" is required.");
		}

		@Test
		public void testInvalidConsent() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, ourNewCoverage, new Consent());
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_CONSENT + "\\\" is required.");
		}

		@Test
		public void testMissingPatientFamilyName() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_MEMBER_PATIENT_NAME + "\\\" is required.");
		}

		@Test
		public void testMissingPatientBirthdate() throws Exception {
			ourPatient.setName(Lists.newArrayList(new HumanName()
				.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person")));
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_MEMBER_PATIENT_BIRTHDATE + "\\\" is required.");
		}

		@Test
		public void testMissingConsentPatientReference() throws Exception {
			ourPatient.setName(Lists.newArrayList(new HumanName()
					.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person")))
				.setBirthDateElement(new DateType("2020-01-01"));

			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_CONSENT_PATIENT_REFERENCE + "\\\" is required.");
		}

		@Test
		public void testMissingConsentPerformerReference() throws Exception {
			ourPatient.setName(Lists.newArrayList(new HumanName()
					.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person")))
				.setBirthDateElement(new DateType("2020-01-01"));

			ourConsent.setPatient(new Reference("Patient/1"));
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, ourNewCoverage, ourConsent);
			performOperationExpecting422(myServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_CONSENT_PERFORMER_REFERENCE + "\\\" is required.");
		}
	}

}
