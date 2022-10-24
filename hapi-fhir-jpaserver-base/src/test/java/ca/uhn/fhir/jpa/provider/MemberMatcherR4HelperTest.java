package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.r4.IConsentExtensionProvider;
import ca.uhn.fhir.jpa.provider.r4.MemberMatcherR4Helper;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONSENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberMatcherR4HelperTest {

	@Spy
	private final FhirContext myFhirContext = FhirContext.forR4();
	@Mock
	private IFhirResourceDao<Coverage> myCoverageDao;
	@Mock
	private IFhirResourceDao<Patient> myPatientDao;
	@Mock
	private IFhirResourceDao<Consent> myConsentDao;

	private MemberMatcherR4Helper myHelper;

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
		when(myCoverageDao.search(isA(SearchParameterMap.class))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.singletonList(myMatchedCoverage));

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch);

		assertEquals(Optional.of(myMatchedCoverage), result);
		verify(myCoverageDao).search(mySearchParameterMapCaptor.capture());
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
		when(myCoverageDao.search(isA(SearchParameterMap.class))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(
			Collections.emptyList(), Collections.singletonList(myMatchedCoverage));

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch);

		assertEquals(Optional.of(myMatchedCoverage), result);
		verify(myCoverageDao, times(2)).search(mySearchParameterMapCaptor.capture());
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
		when(myCoverageDao.search(any(SearchParameterMap.class))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.emptyList(), Collections.emptyList());

		Optional<Coverage> result = myHelper.findMatchingCoverage(myCoverageToMatch);

		assertFalse(result.isPresent());
	}


	@Test
	void buildSuccessReturnParameters() {
		Patient patient = new Patient();
		Coverage coverage = new Coverage();
		Consent consent = new Consent();

		Parameters result = myHelper.buildSuccessReturnParameters(patient, coverage, consent);

		assertEquals(PARAM_MEMBER_PATIENT, result.getParameter().get(0).getName());
		assertEquals(patient, result.getParameter().get(0).getResource());

		assertEquals(PARAM_NEW_COVERAGE, result.getParameter().get(1).getName());
		assertEquals(coverage, result.getParameter().get(1).getResource());

		assertEquals(PARAM_CONSENT, result.getParameter().get(2).getName());
		assertEquals(consent, result.getParameter().get(2).getResource());
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

	@Test
	void TestAddIdentifierToConsent() {
		Consent consent = new Consent();
		myHelper.addIdentifierToConsent(consent);
		assertEquals(1, consent.getIdentifier().size());
		assertEquals(myTestedHelper.CONSENT_IDENTIFIER_CODE_SYSTEM, consent.getIdentifier().get(0).getSystem());
		assertNotNull(consent.getIdentifier().get(0).getValue());
	}

	@Nested
	public class TestGetBeneficiaryPatient {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private Coverage coverage;


		@Test
		void noBeneficiaryOrBeneficiaryTargetReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(null);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithNoIdentifierReturnsEmpty() {
			when(coverage.getBeneficiary()).thenReturn(null);
			when(coverage.getBeneficiaryTarget()).thenReturn(new Patient());

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithIdentifierReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(patient);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceResourceReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(patient);

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceNoResourceOrReferenceReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(new Reference());

			Optional<Patient> result = myHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryReferenceReferenceReturnsReadPatient() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(null);
			when(coverage.getBeneficiary().getReference()).thenReturn("patient-id");

			myHelper.getBeneficiaryPatient(coverage);

			verify(myPatientDao).read(new IdDt("patient-id"));
		}

	}

	@Nested
	public class TestValidPatientMember {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private Coverage coverage;
		private Patient patient;

		@Test
		void noPatientFoundFromContractReturnsFalse() {
			boolean result = myHelper.validPatientMember(null, patient);
			assertFalse(result);
		}

		@Test
		void noPatientFoundFromPatientMemberReturnsFalse() {
			boolean result = myHelper.validPatientMember(patient, null);
			assertFalse(result);
		}

		@Test
		void noMatchingFamilyNameReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "2020-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Smith", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch);
			assertFalse(result);
		}


		@Test
		void noMatchingBirthdayReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "1990-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Person", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch);
			assertFalse(result);
		}

		@Test
		void noMatchingFieldsReturnsFalse() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "1990-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Smith", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(new Patient().setId("B123")));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch);
			assertFalse(result);
		}

		@Test
		void patientMatchingReturnTrue() {
			Patient patientFromMemberMatch = getPatientWithNoIDParm("Person", "2020-01-01");
			Patient patientFromContractFound = getPatientWithIDParm("A123", "Person", "2020-01-01");
			when(myPatientDao.search(any(SearchParameterMap.class))).thenAnswer(t -> {
				IBundleProvider provider = new SimpleBundleProvider(Collections.singletonList(patientFromContractFound));
				return provider;
			});
			boolean result = myHelper.validPatientMember(patientFromContractFound, patientFromMemberMatch);
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

	@Test
	void TestUpdateConsentPatientAndPerformer() {
		Consent consent = getConsent("#sensitive");
		Patient patient = (Patient)new Patient().setId("Patient/123");
		myHelper.updateConsentPatientAndPerformer(consent, patient);
		String patientRef = patient.getIdElement().toUnqualifiedVersionless().getValue();
		assertEquals(patientRef, consent.getPatient().getReference());
		assertEquals(patientRef, consent.getPerformer().get(0).getReference());
	}

	@Nested
	public class TestValidvalidConsentDataAccess {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private Coverage coverage;
		private Patient patient;
		private Consent consent;

		@Test
		void noConsentProfileFoundReturnsFalse() {
			consent = new Consent();
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void noDataAccessValueProvidedReturnsFalse() {
			consent = getConsent("");
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void wrongDataAccessValueProvidedReturnsFalse() {
			consent = getConsent("#access_data");
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void regularDataAccessWithRegularNotAllowedReturnsFalse() {
			consent = getConsent("#regular");
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void regularDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent("#regular");
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void sensitiveDataAccessAllowedReturnsTrue() {
			consent = getConsent("#sensitive");
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleSensitivePolicyDataAccessAllowedReturnsTrue() {
			consent = getConsent("#sensitive");
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleRegularPolicyDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent("#regular");
			consent.addPolicy(constructConsentPolicyComponent("#regular"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}

		@Test
		void multipleMixedPolicyDataAccessWithRegularNotAllowedReturnsFalse() {
			consent = getConsent("#regular");
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertFalse(result);
		}

		@Test
		void multipleMixedPolicyDataAccessWithRegularAllowedReturnsTrue() {
			myHelper.setRegularFilterSupported(true);
			consent = getConsent("#regular");
			consent.addPolicy(constructConsentPolicyComponent("#sensitive"));
			boolean result = myHelper.validConsentDataAccess(consent);
			assertTrue(result);
		}
	}

	private Consent getConsent(String uriAccess) {
		Consent consent = new Consent().addPolicy(constructConsentPolicyComponent(uriAccess));
		return consent;
	}

	private Consent.ConsentPolicyComponent constructConsentPolicyComponent(String uriAccess) {
		String uri = "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition-hrex-consent.html";
		return new Consent.ConsentPolicyComponent().setUri(uri + uriAccess);
	}


	@Nested
	public class MemberMatchWithoutConsentProvider {

		@Test
		public void addClientIdAsExtensionToConsentIfAvailable_noProvider_doesNothing() {
			// setup
			Consent consent = new Consent();

			// test
			myHelper.addClientIdAsExtensionToConsentIfAvailable(consent);

			// verify
			verify(myConsentDao, Mockito.never())
				.create(any(Consent.class));
		}
	}

	@Nested
	public class MemberMatchWithConsentProvider {
		@Mock
		private IConsentExtensionProvider myExtensionProvider;

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
			Consent consent = new Consent();
			consent.setId("Consent/RED");
			Extension ext = new Extension();
			ext.setUrl("http://example.com");
			ext.setValue(new StringType("value"));

			// when
			when(myExtensionProvider.getConsentExtension(any(IBaseResource.class)))
				.thenReturn(Collections.singleton(ext));

			// test
			myHelper.addClientIdAsExtensionToConsentIfAvailable(consent);

			// verify
			ArgumentCaptor<Consent> consentArgumentCaptor = ArgumentCaptor.forClass(Consent.class);
			verify(myConsentDao).create(consentArgumentCaptor.capture());
			Consent saved = consentArgumentCaptor.getValue();
			assertEquals(consent.getId(), saved.getId());
			assertNotNull(saved.getExtension());
			assertEquals(1, saved.getExtension().size());
			Extension savedExt = saved.getExtension().get(0);
			assertEquals(ext.getUrl(), savedExt.getUrl());
			assertEquals(ext.getValue(), savedExt.getValue());
		}
	}
}
