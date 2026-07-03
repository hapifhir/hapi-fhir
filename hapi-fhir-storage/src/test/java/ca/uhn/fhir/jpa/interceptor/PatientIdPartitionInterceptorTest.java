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
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.test.junit.StringToIntegerListArgumentConverter;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PatientIdPartitionInterceptorTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private final PartitionSettings myPartitionSettings = new PartitionSettings();
	private PatientIdPartitionInterceptor mySvc;
	private MatchUrlService myMatchUrlSvc;
	private ISearchParamExtractor myRealSearchParamExtractor;

	@Mock
	private ISearchParamExtractor mySearchParamExtractor;

	@Mock
	private DaoRegistry myDaoRegistry;

	@BeforeEach
	void beforeEach() {
		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
		StorageSettings storageSettings = new StorageSettings();
		myRealSearchParamExtractor = new SearchParamExtractorR4(storageSettings, myPartitionSettings, myFhirContext, searchParamRegistry);
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, myRealSearchParamExtractor, myPartitionSettings, myDaoRegistry);

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
	void testSearch_ChainedParamWithNonResolvedParameter_allPartitionsSearchNotSupported_throwsException(String theValue) {
		mySvc.setAllPartitionSearchSupported(false);

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
		Encounter?subject.identifier=http://patient|1
		Encounter?subject.identifier=http://patient|1&subject.gender=female
		Encounter?subject.gender=male&subject.identifier=http://patient|1
		""")
	void testSearch_ChainedParamWithNonResolvedParameter_allPartitionsSearchSupported_returnsAllPartitions(String theValue) {
		MatchUrlService.ResourceTypeAndSearchParameterMap parsedMatchUrl = myMatchUrlSvc.parseAndTranslateMatchUrl(theValue);
		SearchParameterMap params = parsedMatchUrl.searchParameterMap();
		String resourceType = parsedMatchUrl.resourceType();
		ReadPartitionIdRequestDetails readDetails = ReadPartitionIdRequestDetails.forSearchType(resourceType, params, null);

		// mySvc has isAllPartitionSearchSupported() == true (the base default)
		RequestPartitionId actual = mySvc.identifyForRead(readDetails, new ServletRequestDetails());

		assertTrue(actual.isAllPartitions());
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

	/**
	 * Resolution of conditional Patient references in a transaction body: after the pre-fetch has run, the
	 * {@code STORAGE_TRANSACTION_WRITE_AFTER_PREFETCH} hook rewrites a {@code "Patient?identifier=..."} body
	 * reference to a literal {@code Patient/<id>} by reusing what the pre-fetch resolved into
	 * {@link TransactionDetails#getResolvedMatchUrls()} (falling back to the match-URL cache), so that per-entry
	 * partition determination can route the resource. No live search is performed here.
	 */
	@Nested
	class ConditionalPatientReferenceResolutionInTransactionBundle {

		private static final String PATIENT_IDENTIFIER_MATCH_URL = "Patient?identifier=http://acme.org/mrn|PT00062";

		private Bundle bundleWithObservationSubjectReference(String theReferenceValue) {
			Observation obs = new Observation();
			obs.getSubject().setReference(theReferenceValue);
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionCreateEntry(obs);
			return bb.getBundleTyped();
		}

		/** The raw bundle entries, as the transaction processor passes them to the hook. */
		@SuppressWarnings("unchecked")
		private List<IBase> entriesOf(Bundle theBundle) {
			return new ArrayList<>(theBundle.getEntry());
		}

		private void fireHook(Bundle theBundle, TransactionDetails theTransactionDetails) {
			mySvc.resolveConditionalPatientReferencesAfterPrefetch(entriesOf(theBundle), theTransactionDetails);
		}

		@Test
		void testAfterPrefetch_WhenAllPartitionSearchUnsupported_ResolutionSkipped() {
			// When isAllPartitionSearchSupported() is false the hook returns early (MegaScale: the pre-fetch
			// does not resolve across partitions, so there is nothing to reuse).
			mySvc.setAllPartitionSearchSupported(false);
			Bundle bundle = bundleWithObservationSubjectReference(PATIENT_IDENTIFIER_MATCH_URL);
			Observation obs = (Observation) bundle.getEntry().get(0).getResource();

			fireHook(bundle, new TransactionDetails());

			// The conditional reference is left untouched and no resolution is attempted.
			assertThat(obs.getSubject().getReference()).isEqualTo(PATIENT_IDENTIFIER_MATCH_URL);
		}

		@Test
		void testAfterPrefetch_resolvedInForwardMap_rewritesReferenceToLiteralPatientId() {
			JpaPid pid = mock();

			Bundle bundle = bundleWithObservationSubjectReference(PATIENT_IDENTIFIER_MATCH_URL);
			Observation obs = (Observation) bundle.getEntry().get(0).getResource();

			// The pre-fetch resolved the match URL: it populates both the match-URL map and the resolved-id
			// (reverse) map, which is what the hook reads to rewrite the reference.
			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.addResolvedMatchUrl(myFhirContext, PATIENT_IDENTIFIER_MATCH_URL, pid);
			transactionDetails.addResolvedResourceId(new IdType("Patient/A"), pid);

			fireHook(bundle, transactionDetails);

			assertThat(obs.getSubject().getReference()).isEqualTo("Patient/A");
		}

		@Test
		void testAfterPrefetch_absentFromTransactionMap_leftUntouched_andCacheNotConsulted() {
			// A match URL that the pre-fetch did not resolve into getResolvedMatchUrls() (e.g. it was served from the
			// cross-transaction match-URL cache, which populates the id map instead) is left untouched. The hook does
			// NOT consult the match-URL cache. Per-entry partition determination then rejects the unroutable resource
			// with HAPI-1326 downstream, so the transaction fails cleanly rather than committing mis-routed data.
			Bundle bundle = bundleWithObservationSubjectReference(PATIENT_IDENTIFIER_MATCH_URL);
			Observation obs = (Observation) bundle.getEntry().get(0).getResource();

			fireHook(bundle, new TransactionDetails());

			assertThat(obs.getSubject().getReference()).isEqualTo(PATIENT_IDENTIFIER_MATCH_URL);
		}

		@Test
		void testAfterPrefetch_noMatch_throwsResourceNotFound() {
			// The pre-fetch marked the match URL as NOT_FOUND.
			Bundle bundle = bundleWithObservationSubjectReference(PATIENT_IDENTIFIER_MATCH_URL);
			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.addResolvedMatchUrl(myFhirContext, PATIENT_IDENTIFIER_MATCH_URL, TransactionDetails.NOT_FOUND);

			assertThatThrownBy(() -> fireHook(bundle, transactionDetails))
					.isInstanceOf(ResourceNotFoundException.class)
					.hasMessage(Msg.code(2992) + "Conditional reference \"" + PATIENT_IDENTIFIER_MATCH_URL
							+ "\" matched no Patient resources; unable to determine partition");
		}

		@Test
		void testAfterPrefetch_literalReference_isLeftUntouchedAndNotResolved() {
			Bundle bundle = bundleWithObservationSubjectReference("Patient/A");
			Observation obs = (Observation) bundle.getEntry().get(0).getResource();

			fireHook(bundle, new TransactionDetails());

			// A literal reference is not conditional, so it is left untouched and triggers no lookup.
			assertThat(obs.getSubject().getReference()).isEqualTo("Patient/A");
		}

		@Test
		void testAfterPrefetch_multipleEntries_resolvesEachConditionalReference() {
			String matchUrlA = "Patient?identifier=http://acme.org/mrn|A";
			String matchUrlB = "Patient?identifier=http://acme.org/mrn|B";
			JpaPid pidA = mock();
			JpaPid pidB = mock();

			Observation obsA = new Observation();
			obsA.getSubject().setReference(matchUrlA);
			Observation obsB = new Observation();
			obsB.getSubject().setReference(matchUrlB);
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionCreateEntry(obsA);
			bb.addTransactionCreateEntry(obsB);
			Bundle bundle = bb.getBundleTyped();

			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.addResolvedMatchUrl(myFhirContext, matchUrlA, pidA);
			transactionDetails.addResolvedMatchUrl(myFhirContext, matchUrlB, pidB);
			transactionDetails.addResolvedResourceId(new IdType("Patient/A"), pidA);
			transactionDetails.addResolvedResourceId(new IdType("Patient/B"), pidB);

			fireHook(bundle, transactionDetails);

			assertThat(obsA.getSubject().getReference()).isEqualTo("Patient/A");
			assertThat(obsB.getSubject().getReference()).isEqualTo("Patient/B");
		}

		@Test
		void testAfterPrefetch_multipleCompartmentReferencesInOneResource_eachResolvedToItsPatient() {
			// subject and performer are both Patient-compartment search params for Observation, so a
			// conditional reference on each is resolved independently to its own Patient.
			String subjectMatchUrl = "Patient?identifier=http://acme.org/mrn|A";
			String performerMatchUrl = "Patient?identifier=http://acme.org/mrn|B";
			JpaPid pidA = mock();
			JpaPid pidB = mock();

			Observation obs = new Observation();
			obs.getSubject().setReference(subjectMatchUrl);
			obs.addPerformer().setReference(performerMatchUrl);
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionCreateEntry(obs);
			Bundle bundle = bb.getBundleTyped();

			TransactionDetails transactionDetails = new TransactionDetails();
			transactionDetails.addResolvedMatchUrl(myFhirContext, subjectMatchUrl, pidA);
			transactionDetails.addResolvedMatchUrl(myFhirContext, performerMatchUrl, pidB);
			transactionDetails.addResolvedResourceId(new IdType("Patient/A"), pidA);
			transactionDetails.addResolvedResourceId(new IdType("Patient/B"), pidB);

			fireHook(bundle, transactionDetails);

			assertThat(obs.getSubject().getReference()).isEqualTo("Patient/A");
			assertThat(obs.getPerformerFirstRep().getReference()).isEqualTo("Patient/B");
		}

		@Test
		void testAfterPrefetch_nonPatientConditionalReference_isLeftUntouchedAndNotResolved() {
			String groupConditionalReference = "Group?identifier=http://acme.org/grp|G1";
			Observation obs = new Observation();
			obs.getSubject().setReference(groupConditionalReference);
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionCreateEntry(obs);

			// Only Patient conditional references are resolved; a non-Patient one triggers no lookup.
			fireHook(bb.getBundleTyped(), new TransactionDetails());

			assertThat(obs.getSubject().getReference()).isEqualTo(groupConditionalReference);
		}

		@Test
		void testAfterPrefetch_nonCompartmentPatientConditionalReference_isLeftUntouchedAndNotResolved() {
			// 'focus' is not a Patient-compartment search param for Observation, so a conditional Patient
			// reference there does not drive partition selection. The interceptor leaves it untouched; the
			// core transaction processor resolves it later at save time.
			Observation obs = new Observation();
			obs.addFocus().setReference(PATIENT_IDENTIFIER_MATCH_URL);
			BundleBuilder bb = new BundleBuilder(myFhirContext);
			bb.addTransactionCreateEntry(obs);

			fireHook(bb.getBundleTyped(), new TransactionDetails());

			assertThat(obs.getFocusFirstRep().getReference()).isEqualTo(PATIENT_IDENTIFIER_MATCH_URL);
		}
	}

}
