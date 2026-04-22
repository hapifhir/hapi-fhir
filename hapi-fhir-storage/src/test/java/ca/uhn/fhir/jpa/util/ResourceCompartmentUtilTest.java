package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorR4;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.bouncycastle.util.Strings;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ResourceCompartmentUtilTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	private ISearchParamExtractor mySearchParamExtractor;

	@BeforeEach
	public void beforeEach() {
		ISearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(myFhirContext);
		mySearchParamExtractor = new SearchParamExtractorR4(new StorageSettings(), new PartitionSettings(), myFhirContext, searchParamRegistry);
	}

	@Test
	void getResourceCompartment() {
		Observation resource = new Observation().setSubject(new Reference("Patient/P01"));

		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition("Observation");
		List<RuntimeSearchParam> myCompartmentSearchParams = ResourceCompartmentUtil.getPatientCompartmentSearchParams(resourceDef);

		Optional<String> oCompartment = ResourceCompartmentUtil.getResourceCompartment("Patient",
			resource, myCompartmentSearchParams, mySearchParamExtractor);

		assertThat(oCompartment).isPresent();
		assertThat(oCompartment).contains("P01");
	}

	@Test
	void getPatientCompartmentSearchParams() {
		RuntimeResourceDefinition runtimeResourceDefinition = myFhirContext.getResourceDefinition("Observation");

		List<RuntimeSearchParam> result = ResourceCompartmentUtil.getPatientCompartmentSearchParams(runtimeResourceDefinition);

		assertThat(result.stream().map(RuntimeSearchParam::getName).toList()).containsExactlyInAnyOrder("performer", "subject");
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
		false , Observation , performer subject
		true  , Observation , performer patient subject
		false , Encounter   , patient
		true  , Encounter   , patient subject
		false , Coverage    , beneficiary payor policy-holder subscriber
		true  , Coverage    , beneficiary payor policy-holder subscriber
		""")
	void getPatientCompartmentSearchParams_IncludeSupersets(boolean theIncludeSupersets, String theResourceType, String theExpectedParamNames) {
		RuntimeResourceDefinition runtimeResourceDefinition = myFhirContext.getResourceDefinition(theResourceType);

		List<RuntimeSearchParam> result = ResourceCompartmentUtil.getPatientCompartmentSearchParams(runtimeResourceDefinition, theIncludeSupersets);

		// Verify
		List<String> paramNames = result.stream().map(RuntimeSearchParam::getName).toList();
		String[] expected = theExpectedParamNames.split(" ");
		assertThat(paramNames).containsExactlyInAnyOrder(expected);
	}

	@Nested
	public class TestGetPatientCompartmentIdentity {
		@Test
		void whenNoPatientCompartmentsReturnsEmpty() {
			ValueSet resource = new ValueSet();

			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(resource, myFhirContext, mySearchParamExtractor);

			assertTrue(result.isEmpty());
		}

		@Test
		void whenPatientResource_andNoId_throws() {
			// No ID assigned to the patient
			Patient resource = new Patient();

			assertThatThrownBy(() -> ResourceCompartmentUtil.getPatientCompartmentIdentity(resource, myFhirContext, mySearchParamExtractor))
				.isInstanceOf(MethodNotAllowedException.class)
				.hasMessageStartingWith(Msg.code(2475) + "Patient resource IDs must be client-assigned");
		}

		@Test
		void whenPatientResource_whichHasId_returnsId() {
			Resource resource = new Patient().setId("Patient/Abc");

			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(resource, myFhirContext, mySearchParamExtractor);

			assertThat(result).isPresent();
			assertThat(result).contains("Abc");
		}

		@Test
		void whenNoPatientResource_returnsPatientCompartment() {
			Observation resource = new Observation().setSubject(new Reference("Patient/P01"));

			// execute
			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(resource, myFhirContext, mySearchParamExtractor);

			// Verify
			assertThat(result).isPresent();
			assertThat(result).contains("P01");
		}

		@Test
		void nullResource_shouldNotThrowNPE() {
			// The input resource may be null when mass ingestion is enabled.
			Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(null, myFhirContext, mySearchParamExtractor);
			assertThat(result).isEmpty();
		}
	}

}
