package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
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
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.test.junit.StringToIntegerListArgumentConverter;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
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

	@Test
	void testSetResourceTypePolicies_UnknownResourceType_ThrowsConfigurationException() {
		Map<String, ResourceCompartmentStoragePolicy> policies = Map.of(
				"NotARealResourceType", ResourceCompartmentStoragePolicy.alwaysUseDefaultPartition());

		assertThatThrownBy(() -> mySvc.setResourceTypePolicies(policies))
				.isInstanceOf(ConfigurationException.class)
				.hasMessageContaining(Msg.code(2866))
				.hasMessageContaining("NotARealResourceType")
				.hasMessageContaining("is not a valid resource type");
	}

	/**
	 * Chained search params on subject/patient (e.g. subject.gender=female)
	 * should be tolerated in patient compartment mode. The partition is determined by the base
	 * reference; the chained param does not contribute to partition selection and must be skipped.
	 */
	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
		Encounter?subject=Patient/abc                          | 6354
		Encounter?subject=Patient/abc&subject.gender=female    | 6354
		Encounter?subject=Patient/abc&subject.gender=male      | 6354
		Observation?patient=Patient/123&patient.gender=female  | 3690
		Observation?patient.gender=female&patient=Patient/123  | 3690
		""")
	void testSearch_ChainedParamAlongsideDirect_ReturnsPartitionFromDirectRef(String theValue, @ConvertWith(StringToIntegerListArgumentConverter.class) List<Integer> theExpectedPartitionId) {
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		assertFalse(actual.isAllPartitions());
		assertThat(actual.getPartitionIds()).containsExactly(theExpectedPartitionId.toArray(Integer[]::new));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		Encounter?subject.identifier=http://patient|1
		Encounter?subject.identifier=http://patient|1&subject.gender=female
		Encounter?subject.gender=male&subject.identifier=http://patient|1
		""")
	void testSearch_ChainedParamWithNonResolvedParameter_throwsException(String theValue) {
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		assertThatThrownBy(() -> mySvc.identifyForRead(readDetails, new ServletRequestDetails()))
			.isInstanceOf(MethodNotAllowedException.class)
			.hasMessageContaining(Msg.code(2928));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		Patient?_id:text=foo
		Patient?_id:exact=123
		Patient?_id:contains=partial
		""")
	void testSearch_UnsupportedModifierOnIdParam_throwsMethodNotAllowed(String theValue) {
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		assertThatThrownBy(() -> mySvc.identifyForRead(readDetails, new ServletRequestDetails()))
			.isInstanceOf(MethodNotAllowedException.class)
			.hasMessageContaining(Msg.code(1322));
	}

	@Nested
	class PatientCompartmentExtension {

		private Observation createObservationInMultipleCompartments() {
			Observation obs = new Observation();
			obs.addPerformer(new Reference("Patient/1"));
			obs.addPerformer(new Reference("Patient/2"));
			return obs;
		}

		private void addCompartmentExtension(DomainResource theResource, String theValue) {
			theResource.addExtension()
				.setUrl("http://hapifhir.io/fhir/StructureDefinition/patient-compartment")
				.setValue(new StringType(theValue));
		}

		@Test
		void testCreate_extensionOnPatient_rejected() {
			Patient patient = new Patient();
			patient.setId("PAT-A");
			addCompartmentExtension(patient, "Patient/PAT-A");

			assertThatThrownBy(() -> mySvc.identifyForCreate(patient, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2908")
				.hasMessageContaining("is not applicable to Patient resources");
		}

		@Test
		void testCreate_extensionOnNonCompartmentResource_rejected() {
			// Organization is not a patient-compartment resource type
			Organization org = new Organization();
			addCompartmentExtension(org, "Patient/1");

			assertThatThrownBy(() -> mySvc.identifyForCreate(org, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2909")
				.hasMessageContaining("is not applicable to resource type 'Organization'");
		}

		@Test
		void testCreate_withNonStringValue_throwsError() {
			Observation obs = createObservationInMultipleCompartments();
			obs.addExtension()
				.setUrl("http://hapifhir.io/fhir/StructureDefinition/patient-compartment")
				.setValue(new Reference("Patient/1"));

			assertThatThrownBy(() -> mySvc.identifyForCreate(obs, new ServletRequestDetails()))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessageContaining("HAPI-2894");
		}

		@ParameterizedTest
		@CsvSource(delimiter = '|', useHeadersInDisplayName = true, textBlock = """
			policy                            | extensionValue   | expectedPartition | expectedErrorCode

			# Patient/<id> routes to specified patient's partition
			# (an empty policy '' falls back to MANDATORY default for patient-compartment resources)
			MANDATORY_SINGLE_COMPARTMENT      | Patient/1        | PATIENT1          |
			OPTIONAL_SINGLE_COMPARTMENT       | Patient/1        | PATIENT1          |
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | Patient/1        | PATIENT1          |
			''                                | Patient/1        | PATIENT1          |

			# NONE routes to default partition; rejected under MANDATORY (which is also the default) policy
			OPTIONAL_SINGLE_COMPARTMENT       | NONE             | DEFAULT           |
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | NONE             | DEFAULT           |
			MANDATORY_SINGLE_COMPARTMENT      | NONE             |                   | HAPI-2895
			''                                | NONE             |                   | HAPI-2895

			# ALWAYS_USE_* policies ignores the extension
			ALWAYS_USE_DEFAULT_PARTITION      | Patient/1        | DEFAULT           |
			ALWAYS_USE_PARTITION_ID/5         | Patient/1        | 5                 |

			# Input validation error cases, validation runs before any policy-aware branch,
			# so each invalid value must be rejected under every compartment-based policy
			# (including the empty '' policy which falls back to the MANDATORY default)

			# Patient ID is not in the resource's compartment list (resource only has Patient/1, Patient/2)
			MANDATORY_SINGLE_COMPARTMENT      | Patient/999      |                   | HAPI-2897
			OPTIONAL_SINGLE_COMPARTMENT       | Patient/999      |                   | HAPI-2897
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | Patient/999      |                   | HAPI-2897
			''                                | Patient/999      |                   | HAPI-2897

			# "Patient/" prefix with no ID part — fails Patient/<id> parsing
			MANDATORY_SINGLE_COMPARTMENT      | Patient/         |                   | HAPI-2896
			OPTIONAL_SINGLE_COMPARTMENT       | Patient/         |                   | HAPI-2896
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | Patient/         |                   | HAPI-2896
			''                                | Patient/         |                   | HAPI-2896

			# Value is neither "NONE" nor a "Patient/<id>"
			MANDATORY_SINGLE_COMPARTMENT      | SomethingInvalid |                   | HAPI-2896
			OPTIONAL_SINGLE_COMPARTMENT       | SomethingInvalid |                   | HAPI-2896
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | SomethingInvalid |                   | HAPI-2896
			''                                | SomethingInvalid |                   | HAPI-2896

			# Reference is to a non-Patient resource type
			MANDATORY_SINGLE_COMPARTMENT      | Observation/1    |                   | HAPI-2896
			OPTIONAL_SINGLE_COMPARTMENT       | Observation/1    |                   | HAPI-2896
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | Observation/1    |                   | HAPI-2896
			''                                | Observation/1    |                   | HAPI-2896

			# Empty string — doesn't match "NONE" and fails Patient/<id> parsing ('' is JUnit CSV syntax for empty)
			MANDATORY_SINGLE_COMPARTMENT      | ''               |                   | HAPI-2896
			OPTIONAL_SINGLE_COMPARTMENT       | ''               |                   | HAPI-2896
			NON_UNIQUE_COMPARTMENT_IN_DEFAULT | ''               |                   | HAPI-2896
			''                                | ''               |                   | HAPI-2896
			""")
		void testCreate_extensionOnCompartmentResource(String thePolicy, String theExtensionValue, String theExpectedPartition, String theExpectedErrorCode) {
			if (isNotBlank(thePolicy)) {
				mySvc.setResourceTypePolicies(Map.of("Observation", ResourceCompartmentStoragePolicy.parse(thePolicy)));
			}

			Observation obs = createObservationInMultipleCompartments();
			addCompartmentExtension(obs, theExtensionValue);

			if (isNotBlank(theExpectedErrorCode)) {
				assertThatThrownBy(() -> mySvc.identifyForCreate(obs, new ServletRequestDetails()))
					.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining(theExpectedErrorCode);
			} else {
				RequestPartitionId actual = mySvc.identifyForCreate(obs, new ServletRequestDetails());
				assertThat(actual.getPartitionIds()).containsExactly(resolveExpectedPartitionId(theExpectedPartition));
			}
		}

		private Integer resolveExpectedPartitionId(String theExpected) {
			return switch (theExpected) {
				case "PATIENT1" -> PatientIdPartitionInterceptor.defaultPartitionAlgorithm("1");
				case "DEFAULT" -> myPartitionSettings.getDefaultPartitionId();
				default -> Integer.parseInt(theExpected);
			};
		}
	}

}
