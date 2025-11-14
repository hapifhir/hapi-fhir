// Created by claude-sonnet-4-5
package ca.uhn.fhir.batch2.jobs.merge;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceLinkServiceFactoryTest {

	private ResourceLinkServiceFactory myFactory;
	private PatientNativeLinkService myPatientService;
	private ExtensionBasedLinkService myExtensionService;

	@BeforeEach
	void setUp() {
		myPatientService = new PatientNativeLinkService();
		myExtensionService = new ExtensionBasedLinkService();
		myFactory = new ResourceLinkServiceFactory(myPatientService, myExtensionService);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Patient", "patient", "PATIENT", "PaTiEnT"})
	void getServiceForResourceType_shouldReturnPatientServiceForPatientCaseInsensitive(String theResourceType) {
		// When
		IResourceLinkService service = myFactory.getServiceForResourceType(theResourceType);

		// Then
		assertThat(service).isSameAs(myPatientService);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Observation", "Practitioner", "Organization"})
	void getServiceForResourceType_shouldReturnExtensionServiceForNonPatientTypes(String theResourceType) {
		// When
		IResourceLinkService service = myFactory.getServiceForResourceType(theResourceType);

		// Then
		assertThat(service).isSameAs(myExtensionService);
	}

	@Test
	void getServiceForResource_shouldReturnPatientServiceForPatientResource() {
		// Given
		Patient patient = new Patient();

		// When
		IResourceLinkService service = myFactory.getServiceForResource(patient);

		// Then
		assertThat(service).isSameAs(myPatientService);
	}

	@ParameterizedTest
	@MethodSource("provideNonPatientResources")
	void getServiceForResource_shouldReturnExtensionServiceForNonPatientResources(IBaseResource theResource) {
		// When
		IResourceLinkService service = myFactory.getServiceForResource(theResource);

		// Then
		assertThat(service).isSameAs(myExtensionService);
	}

	static Stream<IBaseResource> provideNonPatientResources() {
		return Stream.of(new Observation(), new Practitioner(), new Organization());
	}
}
