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
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.test.junit.StringToIntegerListArgumentConverter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PatientIdPartitionInterceptorTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private PatientIdPartitionInterceptor mySvc;
	private MatchUrlService myMatchUrlSvc;
	private PartitionSettings myPartitionSettings = new PartitionSettings();

	@Mock
	private ISearchParamExtractor mySearchParamExtractor;

	@Mock
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	void beforeEach() {
		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
		StorageSettings storageSettings = new StorageSettings();
		ISearchParamExtractor searchParamExtractor = new SearchParamExtractorR4(storageSettings, myPartitionSettings, myFhirContext, searchParamRegistry);
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, searchParamExtractor, myPartitionSettings);

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

		PatientIdPartitionInterceptor interceptor = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor, myPartitionSettings);
		if (isNotBlank(theExpectedFailureIfAny)) {
			assertThatThrownBy(() -> interceptor.setResourceTypePolicies(policies))
				.isInstanceOf(ConfigurationException.class)
				.hasMessageContaining(theExpectedFailureIfAny);

		} else {
			assertDoesNotThrow(() -> interceptor.setResourceTypePolicies(policies));
		}
	}


}
