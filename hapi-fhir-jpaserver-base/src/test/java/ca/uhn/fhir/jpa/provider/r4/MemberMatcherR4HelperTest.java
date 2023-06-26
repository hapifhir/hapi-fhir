package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.test.util.LogbackCaptureTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_IDENTIFIER;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberMatcherR4HelperTest {

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension((Logger) MemberMatcherR4Helper.ourLog, Level.TRACE);
	@Spy
	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@Mock
	private IFhirResourceDao<Coverage> myCoverageDao;
	@Mock
	private IFhirResourceDao<Patient> myPatientDao;
	@Mock
	private IFhirResourceDao<Consent> myConsentDao;

	private MemberMatcherR4Helper myHelper;
	RequestDetails myRequestDetails = new SystemRequestDetails();

	@BeforeEach
	public void before() {
		myHelper = new MemberMatcherR4Helper(
			myFhirContext,
			myCoverageDao,
			myPatientDao,
			myConsentDao,
			null // extension provider
		);
	}

	@Mock private Coverage myCoverageToMatch;
	@Mock private IBundleProvider myBundleProvider;

	private final Coverage myMatchedCoverage = new Coverage()
		.setBeneficiary(new Reference("Patient/123"));
	private final Identifier myMatchingIdentifier = new Identifier()
		.setSystem("identifier-system").setValue("identifier-value");

	@Captor
	ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@Test
	void findMatchingCoverageMatchByIdReturnsMatched() {
		when(myCoverageToMatch.getId()).thenReturn("cvg-to-match-id");
		when(myCoverageDao.search(isA(SearchParameterMap.class), same(myRequestDetails))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.singletonList(myMatchedCoverage));

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch, myRequestDetails);

		assertEquals(Optional.of(myMatchedCoverage), result);
		verify(myCoverageDao).search(mySearchParameterMapCaptor.capture(), same(myRequestDetails));
		SearchParameterMap spMap = mySearchParameterMapCaptor.getValue();
		assertTrue(spMap.containsKey("_id"));
		List<List<IQueryParameterType>> listListParams = spMap.get("_id");
		assertEquals(1, listListParams.size());
		assertEquals(1, listListParams.get(0).size());
		IQueryParameterType param = listListParams.get(0).get(0);
		assertEquals("cvg-to-match-id", param.getValueAsQueryToken(myFhirContext));
	}


	@Test
	void findMatchingCoverageMatchByIdentifierReturnsMatched() {
		when(myCoverageToMatch.getId()).thenReturn("non-matching-id");
		when(myCoverageToMatch.getIdentifier()).thenReturn(Collections.singletonList(myMatchingIdentifier));
		when(myCoverageDao.search(isA(SearchParameterMap.class), same(myRequestDetails))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(
			Collections.emptyList(), Collections.singletonList(myMatchedCoverage));

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch, myRequestDetails);

		assertEquals(Optional.of(myMatchedCoverage), result);
		verify(myCoverageDao, times(2)).search(mySearchParameterMapCaptor.capture(), same(myRequestDetails));
		List<SearchParameterMap> spMap = mySearchParameterMapCaptor.getAllValues();
		assertTrue(spMap.get(0).containsKey("_id"));
		assertTrue(spMap.get(1).containsKey("identifier"));
		List<List<IQueryParameterType>> listListParams = spMap.get(1).get("identifier");
		assertEquals(1, listListParams.size());
		assertEquals(1, listListParams.get(0).size());
		IQueryParameterType param = listListParams.get(0).get(0);
		assertEquals(myMatchingIdentifier.getSystem() + "|" + myMatchingIdentifier.getValue(),
			param.getValueAsQueryToken(myFhirContext));
	}


	@Test
	void findMatchingCoverageNoMatchReturnsEmpty() {
		when(myCoverageToMatch.getId()).thenReturn("non-matching-id");
		when(myCoverageToMatch.getIdentifier()).thenReturn(Collections.singletonList(myMatchingIdentifier));
		when(myCoverageDao.search(any(SearchParameterMap.class), same(myRequestDetails))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.emptyList(), Collections.emptyList());

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch, myRequestDetails);

		assertFalse(result.isPresent());
	}


	@Test
	void buildSuccessReturnParameters() {
		Identifier identifier = new Identifier();
		CodeableConcept identifierType = new CodeableConcept();
		identifierType.addCoding(new Coding("", "MB", ""));
		identifier.setType(identifierType);
		Patient patient = new Patient();
		Coverage coverage = new Coverage();
		Consent consent = new Consent();
		patient.addIdentifier(identifier);

		Parameters result = myHelper.buildSuccessReturnParameters(patient, coverage, consent);

		assertEquals(PARAM_MEMBER_PATIENT, result.getParameter().get(0).getName());
		assertEquals(patient, result.getParameter().get(0).getResource());

		assertEquals(PARAM_NEW_COVERAGE, result.getParameter().get(1).getName());
		assertEquals(coverage, result.getParameter().get(1).getResource());

		assertEquals(PARAM_CONSENT, result.getParameter().get(2).getName());
		assertEquals(consent, result.getParameter().get(2).getResource());

		assertEquals(PARAM_MEMBER_IDENTIFIER, result.getParameter().get(3).getName());
		assertEquals(identifier, result.getParameter().get(3).getValue());
	}

	@Test
	void buildNotSuccessReturnParameters_IncorrectPatientIdentifier() {
		Identifier identifier = new Identifier();
		Patient patient = new Patient();
		Coverage coverage = new Coverage();
		Consent consent = new Consent();
		patient.addIdentifier(identifier);

		try {
			myHelper.buildSuccessReturnParameters(patient, coverage, consent);
		} catch (Exception e) {
			assertThat(e.getMessage(), startsWith(Msg.code(2219)));
		}
	}

	@Test
	void addMemberIdentifierToMemberPatient() {
		Identifier originalIdentifier = new Identifier()
			.setSystem("original-identifier-system").setValue("original-identifier-value");

		Identifier newIdentifier = new Identifier()
			.setSystem("new-identifier-system").setValue("new-identifier-value");

		Patient patient = new Patient().setIdentifier(Lists.newArrayList(originalIdentifier));

		myHelper.addMemberIdentifierToMemberPatient(patient, newIdentifier);

		assertEquals(2, patient.getIdentifier().size());

		assertEquals("original-identifier-system", patient.getIdentifier().get(0).getSystem());
		assertEquals("original-identifier-value", patient.getIdentifier().get(0).getValue());

		assertEquals("new-identifier-system", patient.getIdentifier().get(1).getSystem());
		assertEquals("new-identifier-value", patient.getIdentifier().get(1).getValue());
	}

	/**
	 * Testing multiple scenarios for getting patient resource from coverage's plan beneficiary
	 */
	@Nested
	public class TestGetBeneficiaryPatient {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private Coverage coverage;


		@Test
		void noBeneficiaryOrBeneficiaryTargetReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(null);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithNoIdentifierReturnsEmpty() {
			when(coverage.getBeneficiary()).thenReturn(null);
			when(coverage.getBeneficiaryTarget()).thenReturn(new Patient());

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithIdentifierReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(patient);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceResourceReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(patient);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceNoResourceOrReferenceReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(new Reference());

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryReferenceReferenceReturnsReadPatient() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(null);
			when(coverage.getBeneficiary().getReference()).thenReturn("patient-id");

			myHelper.getBeneficiaryPatient(coverage, myRequestDetails);

			verify(myPatientDao).read(new IdDt("patient-id"), myRequestDetails);
		}
	}

	/**
	 * Testing multiple scenarios for validity of Patient Member parameter
	 */
	@Nested
	public class TestValidPatientMember {

		private final Patient patient = new Patient();

		@Test
		void noPatientFoundFromContractReturnsFalse() {
			boolean result = myHelper.validPatientMember(null, patient, myRequestDetails);
			assertFalse(result);
		}

		@Test
		void noPatientFoundFromPatientMemberReturnsFalse() {
			boolean result = myHelper.validPatientMember(patient, null, myRequestDetails);
			assertFalse(result);
		}

		@Test
		void noMatchingFamilyNameReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "2020-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Smith", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class), same(myRequestDetails))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch, myRequestDetails);
			assertFalse(result);
		}


		@Test
		void noMatchingBirthdayReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "1990-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Person", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class), same(myRequestDetails))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch, myRequestDetails);
			assertFalse(result);
		}

		@Test
		void noMatchingFieldsReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "1990-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Smith", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class), same(myRequestDetails))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch, myRequestDetails);
			assertFalse(result);
		}

		@Test
		void patientMatchingReturnTrue() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "2020-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Person", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class), same(myRequestDetails))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(patientFromContractFound));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch, myRequestDetails);
			assertTrue(result);
		}

		private Patient getPatientWithNoIDParm(String familyName, String birthdate) {
			Patient patient = new Patient().setName(Lists.newArrayList(new HumanName()
					.setUse(HumanName.NameUse.OFFICIAL).setFamily(familyName)))
				.setBirthDateElement(new DateType(birthdate));
			return patient;
		}

		private Patient getPatientWithIDParm(String id, String familyName, String birthdate) {
			Patient patient = getPatientWithNoIDParm(familyName, birthdate);
			patient.setId(id);
			return patient;
		}

	}

	/**
	 * Testing multiple scenarios for consent's policy data that is defined in
	 * https://build.fhir.org/ig/HL7/davinci-ehrx/StructureDefinition-hrex-consent.html#notes
	 */
	@Nested
	public class TestValidvalidConsentDataAccess {

		private Consent consent;

		@Test
		void noConsentProfileFoundReturnsFalse() {
			consent = new Consent();
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void noDataAccessValueProvidedReturnsFalse() {
			consent = getConsent();
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void wrongDataAccessValueProvidedReturnsFalse() {
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#access_data"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void regularDataAccessWithRegularNotAllowedReturnsFalse() {
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void regularDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void sensitiveDataAccessAllowedReturnsTrue() {
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleSensitivePolicyDataAccessAllowedReturnsTrue() {
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleRegularPolicyDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleMixedPolicyDataAccessWithRegularNotAllowedReturnsFalse() {
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void multipleMixedPolicyDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}
	}

	private Consent getConsent() {
		Consent consent = new Consent();
		consent.getPerformer().add(new Reference("Patient/1"));
		return consent;
	}

	private Consent.ConsentPolicyComponent constructConsentPolicyComponent(String uriAccess) {
		String uri = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition-hrex-consent.html";
		return new Consent.ConsentPolicyComponent().setUri(uri + uriAccess);
	}

	private Patient createPatientForMemberMatchUpdate(boolean addIdentifier) {
		Patient patient = new Patient();
		patient.setId("Patient/RED");
		Identifier identifier = new Identifier();
		if (addIdentifier) {
			CodeableConcept identifierType = new CodeableConcept();
			identifierType.addCoding(new Coding("", "MB", ""));
			identifier.setType(identifierType);
		}
		identifier.setValue("RED-Patient");
		patient.addIdentifier(identifier);

		return patient;
	}

	@Nested
	public class MemberMatchWithoutConsentProvider {
		@Captor
		private ArgumentCaptor<Consent> myDaoCaptor;

		@Test
		public void updateConsentForMemberMatch_noProvider_addsIdentifierUpdatePatientButNotExtensionAndSaves() {
			// setup
			Consent consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			Patient patient = createPatientForMemberMatchUpdate(false);
			Patient memberPatient = createPatientForMemberMatchUpdate(true);

			// test
			myHelper.updateConsentForMemberMatch(consent, patient, memberPatient, myRequestDetails);

			// verify

			verify(myConsentDao).create(myDaoCaptor.capture(), same(myRequestDetails));
			Consent saved = myDaoCaptor.getValue();
			// check consent identifier
			assertEquals(1, saved.getIdentifier().size());
			assertEquals(MemberMatcherR4Helper.CONSENT_IDENTIFIER_CODE_SYSTEM, saved.getIdentifier().get(0).getSystem());
			assertNotNull(saved.getIdentifier().get(0).getValue());
			assertEquals(saved.getIdentifier().get(0).getValue(), memberPatient.getIdentifier().get(0).getValue());

			// check consent patient info
			String patientRef = patient.getIdElement().toUnqualifiedVersionless().getValue();
			assertEquals(patientRef, saved.getPatient().getReference());
			assertEquals(patientRef, saved.getPerformer().get(0).getReference());

			assertThat(myLogCapture.getLogEvents(), empty());

		}
	}

	@Nested
	public class MemberMatchWithConsentProvider {
		@Mock
		private IConsentExtensionProvider myExtensionProvider;
		@Captor
		private ArgumentCaptor<Consent> myHookCaptor;

		@BeforeEach
		public void before() {
			myHelper = new MemberMatcherR4Helper(
				myFhirContext,
				myCoverageDao,
				myPatientDao,
				myConsentDao,
				myExtensionProvider
			);
		}

		@Test
		public void addClientIdAsExtensionToConsentIfAvailable_withProvider_addsExtensionAndSaves() {
			// setup
			Consent consent = getConsent();
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			consent.setId("Consent/RED");
			Patient patient = createPatientForMemberMatchUpdate(false);
			Patient memberPatient = createPatientForMemberMatchUpdate(true);

			// test
			myHelper.updateConsentForMemberMatch(consent, patient, memberPatient, myRequestDetails);

			// verify
			verify(myExtensionProvider).accept(myHookCaptor.capture());

			assertSame(consent, myHookCaptor.getValue());
			assertThat(myLogCapture.getLogEvents(), empty());
		}
	}
}
