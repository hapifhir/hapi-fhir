package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.test.junit.StringToIntegerListArgumentConverter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PatientIdPartitionInterceptorTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final PartitionSettings myPartitionSettings = new PartitionSettings();
	private PatientIdPartitionInterceptor mySvc;
	private MatchUrlService myMatchUrlSvc;

	@Mock
	private ISearchParamExtractor mySearchParamExtractor;

	@Mock
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	void beforeEach() {
		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
		StorageSettings storageSettings = new StorageSettings();
		ISearchParamExtractor searchParamExtractor = new SearchParamExtractorR4(storageSettings, myPartitionSettings, myFhirContext, searchParamRegistry);
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, searchParamExtractor, myPartitionSettings, myDaoRegistry);

		myMatchUrlSvc = new MatchUrlService(myFhirContext, new FhirContextSearchParamRegistry(myFhirContext));
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
		Patient/123                          | 3690
		http://base/Patient/123              | 3690
		Patient/123/_history/1               | 3690
		http://base/Patient/123/_history/1   | 3690
		""")
	void testCreate_ValidCompartment(String theValue, int theExpectedPartitionId) {
		// Setup
		Observation createDetails = new Observation();
		createDetails.setSubject(new Reference(theValue));

		// Test
		RequestPartitionId actual = mySvc.identifyForCreate(createDetails, new ServletRequestDetails());

		// Verify
		assertFalse(actual.isAllPartitions());
		assertThat(actual.getPartitionIds()).containsExactly(theExpectedPartitionId);
	}

	@Test
	void testCreate_MultipleCompartments() {
		// Setup
		Observation createDetails = new Observation();
		createDetails.addPerformer(new Reference("Patient/1"));
		createDetails.addPerformer(new Reference("Patient/2"));

		// Test
		assertThatThrownBy(() -> mySvc.identifyForCreate(createDetails, new ServletRequestDetails()))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Policy does not allow resource of type \"Observation\" to be created in multiple Patient compartments: Patient/1, Patient/2");
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
		Observation?patient=123                                                                    | 3690
		Observation?patient=123,456                                                                | 3690,6669
		Observation?patient=Patient/123                                                            | 3690
		Observation?patient=Patient/123,Patient/456                                                | 3690,6669
		Observation?patient=http://base/Patient/123                                                | 3690
		Observation?patient=http://base/Patient/123,http://base/Patient/456                        | 3690,6669
		Observation?patient=Patient/123/_history/1                                                 | 3690
		Observation?patient=Patient/123/_history/1,Patient/456/_history/1                          | 3690,6669
		Observation?patient=http://base/Patient/123/_history/1                                     | 3690
		Observation?patient=http://base/Patient/123/_history/1,http://base/Patient/456/_history/1  | 3690,6669
		Patient?_id=123                                                                            | 3690
		Patient?_id=123,456                                                                        | 3690,6669
		Patient?_id=Patient/123                                                                    | 3690
		Patient?_id=Patient/123,Patient/456                                                        | 3690,6669
		Patient?_id=http://base/Patient/123                                                        | 3690
		Patient?_id=http://base/Patient/123,http://base/Patient/456                                | 3690,6669
		Patient?_id=Patient/123/_history/1                                                         | 3690
		Patient?_id=Patient/123/_history/1,Patient/456/_history/1                                  | 3690,6669
		Patient?_id=http://base/Patient/123/_history/1                                             | 3690
		Patient?_id=http://base/Patient/123/_history/1,http://base/Patient/456/_history/1          | 3690,6669
		""")
	void testSearch_ValidCompartment(String theValue, @ConvertWith(StringToIntegerListArgumentConverter.class) List<Integer> theExpectedPartitionId) {
		// Setup
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		// Test
		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		// Verify
		assertFalse(actual.isAllPartitions());
		assertThat(actual.getPartitionIds()).containsExactly(theExpectedPartitionId.toArray(Integer[]::new));
	}

	@Test
	void testHistoryInstance_Patient_ResolvesPartition() {
		// Test
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forHistory(
			"Patient", new IdType("Patient/p1"));

		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		// Verify
		int expectedPartitionId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("p1");
		assertThat(actual.getPartitionIds()).containsExactly(expectedPartitionId);
	}

	@Test
	// Generated by claude-sonnet-4-6
	void testIdentifyForRead_NullRestOperationType_ThrowsIllegalArgumentException() {
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forGeneric(new SystemRequestDetails());

		assertThatThrownBy(() -> mySvc.identifyForRead(readDetails, new SystemRequestDetails()))
				.isInstanceOf(NullPointerException.class);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		Encounter?patient=http://foo/fhir/NamingSystem/id-pathhosp-mrn|123
		Encounter?subject=http://foo/fhir/NamingSystem/id-pathhosp-mrn|123
		Encounter?patient=http://patient|123
		Encounter?subject=http://patient|123
		Encounter?patient=123|456
		Encounter?subject=123|456
		""")
	void testSearch_InvalidParameter(String theValue) {
		// Test
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		// Verify
		assertTrue(actual.isAllPartitions());
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		# No policies allowed for Patient resource
		Patient     , MANDATORY_SINGLE_COMPARTMENT      , Can not provide a resource type policy for resource type 'Patient'
		
		# Patient compartment members
		Observation , ALWAYS_USE_DEFAULT_PARTITION      ,
		Observation , MANDATORY_SINGLE_COMPARTMENT      ,
		Observation , OPTIONAL_SINGLE_COMPARTMENT       ,
		Observation , NON_UNIQUE_COMPARTMENT_IN_DEFAULT ,
		Observation , ALWAYS_USE_PARTITION_ID/1         ,
		
		# Non patient compartment members
		Location , ALWAYS_USE_DEFAULT_PARTITION         ,
		Location , MANDATORY_SINGLE_COMPARTMENT         , Resource type 'Location' is not a Patient Compartment resource type, can not apply policy:
		Location , OPTIONAL_SINGLE_COMPARTMENT          , Resource type 'Location' is not a Patient Compartment resource type, can not apply policy:
		Location , NON_UNIQUE_COMPARTMENT_IN_DEFAULT    , Resource type 'Location' is not a Patient Compartment resource type, can not apply policy:
		Location , ALWAYS_USE_PARTITION_ID/1            ,
		
		# Non partitionable
		ValueSet , ALWAYS_USE_DEFAULT_PARTITION         , Can not provide a resource type policy for non-partitionable resource type: ValueSet
		ValueSet , MANDATORY_SINGLE_COMPARTMENT         , Can not provide a resource type policy for non-partitionable resource type: ValueSet
		ValueSet , OPTIONAL_SINGLE_COMPARTMENT          , Can not provide a resource type policy for non-partitionable resource type: ValueSet
		ValueSet , NON_UNIQUE_COMPARTMENT_IN_DEFAULT    , Can not provide a resource type policy for non-partitionable resource type: ValueSet
		ValueSet , ALWAYS_USE_PARTITION_ID/1            , Can not provide a resource type policy for non-partitionable resource type: ValueSet
		""")
	void testPolicyVerification(String theResourceType, String thePolicy, String theExpectedFailureIfAny) {
		Map<String, ResourceCompartmentStoragePolicy> policies = Map.of(theResourceType, ResourceCompartmentStoragePolicy.parse(thePolicy));

		PatientIdPartitionInterceptor interceptor = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		if (isNotBlank(theExpectedFailureIfAny)) {
			assertThatThrownBy(() -> interceptor.setResourceTypePolicies(policies))
				.isInstanceOf(ConfigurationException.class)
				.hasMessageContaining(theExpectedFailureIfAny);

		} else {
			assertDoesNotThrow(() -> interceptor.setResourceTypePolicies(policies));
		}
	}

	@Nested
	class PrimaryPatientCompartmentExtension {

		private Observation createObservationInMultipleCompartments() {
			Observation obs = new Observation();
			obs.addPerformer(new Reference("Patient/1"));
			obs.addPerformer(new Reference("Patient/2"));
			return obs;
		}

		private void addPrimaryCompartmentExtension(Observation theObs, String thePatientReference) {
			theObs.addExtension()
				.setUrl("http://hapifhir.io/fhir/StructureDefinition/primary-patient-compartment")
				.setValue(new Reference(thePatientReference));
		}

		@ParameterizedTest
		@CsvSource({"MANDATORY_SINGLE_COMPARTMENT", "OPTIONAL_SINGLE_COMPARTMENT"})
		void testCreate_multipleCompartments_withExtension_resolvesToExtensionPatient(String thePolicy) {
			mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));

			Observation obs = createObservationInMultipleCompartments();
			addPrimaryCompartmentExtension(obs, "Patient/1");

			RequestPartitionId actual = mySvc.identifyForCreate(obs, new ServletRequestDetails());

			int patient1PartitionId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("1");
			int patient2PartitionId = PatientIdPartitionInterceptor.defaultPartitionAlgorithm("2");
			// Guard: ensure the two patients hash to different partitions, otherwise the test proves nothing
			assertThat(patient1PartitionId).isNotEqualTo(patient2PartitionId);
			assertFalse(actual.isAllPartitions());
			assertThat(actual.getPartitionIds()).containsExactly(patient1PartitionId);
		}

		@ParameterizedTest
		@CsvSource({"MANDATORY_SINGLE_COMPARTMENT", "OPTIONAL_SINGLE_COMPARTMENT"})
		void testCreate_multipleCompartments_withExtension_patientNotInCompartmentList(String thePolicy) {
			mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));

			Observation obs = createObservationInMultipleCompartments();
			addPrimaryCompartmentExtension(obs, "Patient/999");

			assertThatThrownBy(() -> mySvc.identifyForCreate(obs, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage("HAPI-2897: Extension http://hapifhir.io/fhir/StructureDefinition/primary-patient-compartment references Patient/999 which is not a compartment of this resource. Compartments are: Patient/1, Patient/2");
		}

		@ParameterizedTest
		@CsvSource({"MANDATORY_SINGLE_COMPARTMENT", "OPTIONAL_SINGLE_COMPARTMENT"})
		void testCreate_multipleCompartments_withExtension_nonPatientResourceType(String thePolicy) {
			mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));

			Observation obs = createObservationInMultipleCompartments();
			addPrimaryCompartmentExtension(obs, "Observation/123");

			assertThatThrownBy(() -> mySvc.identifyForCreate(obs, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage("HAPI-2895: Extension http://hapifhir.io/fhir/StructureDefinition/primary-patient-compartment must reference a Patient resource, but found: Observation/123");
		}

		@ParameterizedTest
		@CsvSource({"MANDATORY_SINGLE_COMPARTMENT", "OPTIONAL_SINGLE_COMPARTMENT"})
		void testCreate_multipleCompartments_withExtension_nonReferenceValue(String thePolicy) {
			mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));

			Observation obs = createObservationInMultipleCompartments();
			obs.addExtension()
				.setUrl("http://hapifhir.io/fhir/StructureDefinition/primary-patient-compartment")
				.setValue(new StringType("Patient/1"));

			assertThatThrownBy(() -> mySvc.identifyForCreate(obs, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage("HAPI-2894: Extension http://hapifhir.io/fhir/StructureDefinition/primary-patient-compartment must have a Reference value");
		}

		@ParameterizedTest
		@CsvSource({"NON_UNIQUE_COMPARTMENT_IN_DEFAULT", "ALWAYS_USE_DEFAULT_PARTITION"})
		void testCreate_multipleCompartments_withDefaultPartitionPolicy_extensionIgnored(String thePolicy) {
			mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));

			Observation obs = createObservationInMultipleCompartments();
			addPrimaryCompartmentExtension(obs, "Patient/1");

			RequestPartitionId actual = mySvc.identifyForCreate(obs, new ServletRequestDetails());

			assertThat(actual.getPartitionIds()).containsExactly(myPartitionSettings.getDefaultPartitionId());
		}
	}


}
