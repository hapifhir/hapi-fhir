package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.rest.api.Constants.PARAM_MEMBER_PATIENT;
import static ca.uhn.fhir.rest.api.Constants.PARAM_NEW_COVERAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberMatcherR4HelperTest {

	private final FhirContext myFhirContext = FhirContext.forR4();
	@Mock private IFhirResourceDao<Coverage> myCoverageDao;
	@Mock private IFhirResourceDao<Patient> myPatientDao;

	private MemberMatcherR4Helper myTestedHelper;

	@Mock private Coverage myCoverageToMatch;
	@Mock private Patient myPatient;
	@Mock private IBundleProvider myBundleProvider;

	private final Coverage myMatchedCoverage = new Coverage();
	private final Identifier myMatchingIdentifier = new Identifier()
		.setSystem("identifier-system").setValue("identifier-value");

	@Captor ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;

	@BeforeEach
	public void beforeEach() {
		myTestedHelper = new MemberMatcherR4Helper(myFhirContext);

		// @InjectMocks didn't work
		ReflectionTestUtils.setField(myTestedHelper, "myCoverageDao", myCoverageDao);
		ReflectionTestUtils.setField(myTestedHelper, "myPatientDao", myPatientDao);
	}


	@Test
	void findMatchingCoverageMatchByIdReturnsMatched() {
		when(myCoverageToMatch.getId()).thenReturn("cvg-to-match-id");
		when(myCoverageDao.search(isA(SearchParameterMap.class))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.singletonList(myMatchedCoverage));

		Optional<Coverage> result = myTestedHelper.findMatchingCoverage(myCoverageToMatch);

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

		Optional<Coverage> result = myTestedHelper.findMatchingCoverage(myCoverageToMatch);

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
		when(myCoverageDao.search(isA(SearchParameterMap.class))).thenReturn(myBundleProvider);
		when(myBundleProvider.getAllResources()).thenReturn(Collections.emptyList(), Collections.emptyList());

		Optional<Coverage> result = myTestedHelper.findMatchingCoverage(myCoverageToMatch);

		assertFalse(result.isPresent());
	}


	@Test
	void buildSuccessReturnParameters() {
		Patient patient = new Patient();
		Coverage coverage = new Coverage();

		Parameters result = myTestedHelper.buildSuccessReturnParameters(patient, coverage);

		assertEquals(PARAM_MEMBER_PATIENT, result.getParameter().get(0).getName());
		assertEquals(patient, result.getParameter().get(0).getResource());

		assertEquals(PARAM_NEW_COVERAGE, result.getParameter().get(1).getName());
		assertEquals(coverage, result.getParameter().get(1).getResource());
	}


	@Test
	void addMemberIdentifierToMemberPatient() {
		Identifier originalIdentifier = new Identifier()
			.setSystem("original-identifier-system").setValue("original-identifier-value");

		Identifier newIdentifier = new Identifier()
			.setSystem("new-identifier-system").setValue("new-identifier-value");

		Patient patient = new Patient().setIdentifier(Lists.newArrayList(originalIdentifier));

		myTestedHelper.addMemberIdentifierToMemberPatient(patient, newIdentifier);

		assertEquals(2, patient.getIdentifier().size());

		assertEquals("original-identifier-system", patient.getIdentifier().get(0).getSystem());
		assertEquals("original-identifier-value", patient.getIdentifier().get(0).getValue());

		assertEquals("new-identifier-system", patient.getIdentifier().get(1).getSystem());
		assertEquals("new-identifier-value", patient.getIdentifier().get(1).getValue());
	}

	@Nested
	public class TestGetBeneficiaryPatient {

		@Mock(answer = Answers.RETURNS_DEEP_STUBS)
		private Coverage coverage;


		@Test
		void noBeneficiaryOrBeneficiaryTargetReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(null);

			Optional<Patient> result = myTestedHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithNoIdentifierReturnsEmpty() {
			when(coverage.getBeneficiary()).thenReturn(null);
			when(coverage.getBeneficiaryTarget()).thenReturn(new Patient());

			Optional<Patient> result = myTestedHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryTargetWithIdentifierReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(patient);

			Optional<Patient> result = myTestedHelper.getBeneficiaryPatient(coverage);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceResourceReturnsBeneficiary() {
			Patient patient = new Patient().setIdentifier(Collections.singletonList(new Identifier()));
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(patient);

			Optional<Patient> result = myTestedHelper.getBeneficiaryPatient(coverage);

			assertTrue(result.isPresent());
			assertEquals(patient, result.get());
		}


		@Test
		void beneficiaryReferenceNoResourceOrReferenceReturnsEmpty() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary()).thenReturn(new Reference());

			Optional<Patient> result = myTestedHelper.getBeneficiaryPatient(coverage);

			assertFalse(result.isPresent());
		}


		@Test
		void beneficiaryReferenceReferenceReturnsReadPatient() {
			when(coverage.getBeneficiaryTarget()).thenReturn(null);
			when(coverage.getBeneficiary().getResource()).thenReturn(null);
			when(coverage.getBeneficiary().getReference()).thenReturn("patient-id");

			myTestedHelper.getBeneficiaryPatient(coverage);

			verify(myPatientDao).read(new IdDt("patient-id"));
		}

	}


}
