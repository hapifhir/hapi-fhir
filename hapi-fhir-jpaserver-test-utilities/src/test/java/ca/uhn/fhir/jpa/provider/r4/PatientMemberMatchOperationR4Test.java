package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
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
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
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

	private Identifier ourExistingCoverageIdentifier;


	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setEverythingIncludesFetchPageSize(new DaoConfig().getEverythingIncludesFetchPageSize());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
	}


	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
	}


	private void createCoverageWithBeneficiary(
		boolean theAssociateBeneficiaryPatient, boolean includeBeneficiaryIdentifier) {

		Patient member = null;
		if (theAssociateBeneficiaryPatient) {
			// Patient
			member = new Patient().setName(Lists.newArrayList(new HumanName()
					.setUse(HumanName.NameUse.OFFICIAL).setFamily("Person").addGiven("Patricia").addGiven("Ann")));
			if (includeBeneficiaryIdentifier) {
				member.setIdentifier(Collections.singletonList(new Identifier()
						.setSystem(EXISTING_COVERAGE_PATIENT_IDENT_SYSTEM).setValue(EXISTING_COVERAGE_PATIENT_IDENT_VALUE)));
			}

			myClient.create().resource(member).execute().getId().toUnqualifiedVersionless().getValue();
		}

		// Coverage
		ourExistingCoverageIdentifier = new Identifier()
			.setSystem(EXISTING_COVERAGE_IDENT_SYSTEM).setValue(EXISTING_COVERAGE_IDENT_VALUE);
		Coverage ourExistingCoverage = new Coverage()
			.setStatus(Coverage.CoverageStatus.ACTIVE)
			.setIdentifier(Collections.singletonList(ourExistingCoverageIdentifier));

		if (theAssociateBeneficiaryPatient) {
			// this doesn't work
			//	myOldCoverage.setBeneficiaryTarget(patient)
			ourExistingCoverage.setBeneficiary(new Reference(member))
				.setId(EXISTING_COVERAGE_ID);
		}

		myClient.create().resource(ourExistingCoverage).execute().getId().toUnqualifiedVersionless().getValue();
	}


	@Test
	public void testMemberMatchByCoverageId() throws Exception {
		createCoverageWithBeneficiary(true, true);

		// patient doesn't participate in match
		Patient patient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

		// Old Coverage
		Coverage oldCoverage = new Coverage();
		oldCoverage.setId(EXISTING_COVERAGE_ID);  // must match field

		// New Coverage (must return unchanged)
		Coverage newCoverage = new Coverage();
		newCoverage.setId("AA87654");
		newCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

		Parameters inputParameters = buildInputParameters(patient, oldCoverage, newCoverage);
		Parameters parametersResponse = performOperation(ourServerBase + ourQuery,
			EncodingEnum.JSON, inputParameters);

		validateMemberPatient(parametersResponse);
		validateNewCoverage(parametersResponse, newCoverage);
	}


	@Test
	public void testCoverageNoBeneficiaryReturns422() throws Exception {
		createCoverageWithBeneficiary(false, false);

		// patient doesn't participate in match
		Patient patient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

		// Old Coverage
		Coverage oldCoverage = new Coverage();
		oldCoverage.setId(EXISTING_COVERAGE_ID);  // must match field

		// New Coverage (must return unchanged)
		Coverage newCoverage = new Coverage();
		newCoverage.setId("AA87654");
		newCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

		Parameters inputParameters = buildInputParameters(patient, oldCoverage, newCoverage);
		performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find beneficiary for coverage.");
	}


	@Test
	public void testCoverageBeneficiaryNoIdentifierReturns422() throws Exception {
		createCoverageWithBeneficiary(true, false);

		// patient doesn't participate in match
		Patient patient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

		// Old Coverage
		Coverage oldCoverage = new Coverage();
		oldCoverage.setId(EXISTING_COVERAGE_ID);  // must match field

		// New Coverage (must return unchanged)
		Coverage newCoverage = new Coverage();
		newCoverage.setId("AA87654");
		newCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

		Parameters inputParameters = buildInputParameters(patient, oldCoverage, newCoverage);
		performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Coverage beneficiary does not have an identifier.");
	}


	@Nested
	public class ValidateParameterErrors {
		private Patient ourPatient;
		private Coverage ourOldCoverage;
		private Coverage ourNewCoverage;

		@BeforeEach
		public void beforeValidateParameterErrors() {
			ourPatient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

			ourOldCoverage = new Coverage();
			ourOldCoverage.setId(EXISTING_COVERAGE_ID);

			ourNewCoverage = new Coverage();
			ourNewCoverage.setId("AA87654");
			ourNewCoverage.setIdentifier(Lists.newArrayList(
				new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));
		}

		@Test
		public void testInvalidPatient() throws Exception {
			Parameters inputParameters = buildInputParameters(new Patient(), ourOldCoverage, ourNewCoverage);
			performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_MEMBER_PATIENT + "\\\" is required.");
		}

		@Test
		public void testInvalidOldCoverage() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, new Coverage(), ourNewCoverage);
			performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_OLD_COVERAGE + "\\\" is required.");
		}

		@Test
		public void testInvalidNewCoverage() throws Exception {
			Parameters inputParameters = buildInputParameters(ourPatient, ourOldCoverage, new Coverage());
			performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
				"Parameter \\\"" + PARAM_NEW_COVERAGE + "\\\" is required.");
		}
	}


	@Test
	public void testMemberMatchByCoverageIdentifier() throws Exception {
		createCoverageWithBeneficiary(true, true);

		// patient doesn't participate in match
		Patient patient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

		// Old Coverage
		Coverage oldCoverage = new Coverage();
		oldCoverage.setId("9876B1");
		oldCoverage.setIdentifier(Lists.newArrayList(ourExistingCoverageIdentifier)); // must match field

		// New Coverage (must return unchanged)
		Coverage newCoverage = new Coverage();
		newCoverage.setId("AA87654");
		newCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

		Parameters inputParameters = buildInputParameters(patient, oldCoverage, newCoverage);
		Parameters parametersResponse = performOperation(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters);

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
		// Patient doesn't participate in match
		Patient patient = new Patient().setGender(Enumerations.AdministrativeGender.FEMALE);

		// Old Coverage
		Coverage oldCoverage = new Coverage();
		oldCoverage.setId("9876B1");
		oldCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://oldhealthplan.example.com").setValue("DH10001235")));

		// New Coverage
		Organization newOrg = new Organization();
		newOrg.setId("Organization/ProviderOrg1");
		newOrg.setName("New Health Plan");

		Coverage newCoverage = new Coverage();
		newCoverage.setId("AA87654");
		newCoverage.getContained().add(newOrg);
		newCoverage.setIdentifier(Lists.newArrayList(
			new Identifier().setSystem("http://newealthplan.example.com").setValue("234567")));

		Parameters inputParameters = buildInputParameters(patient, oldCoverage, newCoverage);
		performOperationExpecting422(ourServerBase + ourQuery, EncodingEnum.JSON, inputParameters,
			"Could not find coverage for member");
	}


	private Parameters buildInputParameters(Patient thePatient, Coverage theOldCoverage, Coverage theNewCoverage) {
		Parameters p = new Parameters();
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_MEMBER_PATIENT, thePatient);
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_OLD_COVERAGE, theOldCoverage);
		ParametersUtil.addParameterToParameters(this.getFhirContext(), p, PARAM_NEW_COVERAGE, theNewCoverage);
		return p;
	}


	private Parameters performOperation(String theUrl,
			EncodingEnum theEncoding, Parameters theInputParameters) throws Exception {

		HttpPost post = new HttpPost(theUrl);
		post.addHeader(Constants.HEADER_ACCEPT_ENCODING, theEncoding.toString());
		post.setEntity(new ResourceEntity(this.getFhirContext(), theInputParameters));
		ourLog.info("Request: {}", post);
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());

			return theEncoding.newParser(myFhirContext).parseResource(Parameters.class,
				IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8));
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
		//	  		"code": "UMB",
		//	  		"display": "Member Number",
		//	  		"userSelected": false
		//		}
		//	 * ]
		Coding coding = theType.getCoding().get(0);
		assertEquals("http://terminology.hl7.org/CodeSystem/v2-0203", coding.getSystem());
		assertEquals("UMB", coding.getCode());
		assertEquals("Member Number", coding.getDisplay());
		assertFalse(coding.getUserSelected());
	}



}
