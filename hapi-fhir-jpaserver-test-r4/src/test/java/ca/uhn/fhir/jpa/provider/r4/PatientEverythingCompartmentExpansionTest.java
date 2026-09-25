package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientEverythingCompartmentExpansionTest extends BaseResourceProviderR4Test {

	@Test
	public void patientEverything_shouldReturnMedication_whenMedicationAdministrationExistsThatRefersToMedicationAndPatient() throws Exception {

		Patient patient = new Patient();
		String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToPatient = new Reference();
		referenceToPatient.setReference(patientId);

		Medication medication = new Medication();
		String medicationId = myClient.create().resource(medication).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToMedication = new Reference();
		referenceToMedication.setReference(medicationId);

		MedicationAdministration medicationAdministration = new MedicationAdministration();
		medicationAdministration.setSubject(referenceToPatient);
		medicationAdministration.setMedication(referenceToMedication);
		String medicationAdministrationId = myClient.create().resource(medicationAdministration).execute().getId().toUnqualifiedVersionless().getValue();

		Bundle bundle = fetchBundle("/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual).contains(patientId);
		assertThat(actual).contains(medicationId);
		assertThat(actual).contains(medicationAdministrationId);
	}

	@Test
	public void patientEverything_shouldReturnOrganization_whenPatientRefersToItAsManagingOrganization() throws Exception {

		Organization organization = new Organization();
		String organizationId = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToOrganization = new Reference();
		referenceToOrganization.setReference(organizationId);

		Patient patient = new Patient();
		patient.setManagingOrganization(referenceToOrganization);
		String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Bundle bundle = fetchBundle("/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual).contains(patientId);
		assertThat(actual).contains(organizationId);
	}

	@Test
	public void patientEverything_shouldReturnOrganization_whenPatientRefersToItAsGeneralPractitioner() throws Exception {

		Organization organization = new Organization();
		String organizationId = myClient.create().resource(organization).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToOrganization = new Reference();
		referenceToOrganization.setReference(organizationId);

		Patient patient = new Patient();
		patient.setGeneralPractitioner(List.of(referenceToOrganization));
		String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Bundle bundle = fetchBundle("/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual).contains(patientId);
		assertThat(actual).contains(organizationId);
	}

	@Test
	public void patientEverything_shouldReturnPractitioner_whenPatientRefersToItAsGeneralPractitioner() throws Exception {

		Practitioner practitioner = new Practitioner();
		String practitionerId = myClient.create().resource(practitioner).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToPractitioner = new Reference();
		referenceToPractitioner.setReference(practitionerId);

		Patient patient = new Patient();
		patient.setGeneralPractitioner(List.of(referenceToPractitioner));
		String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Bundle bundle = fetchBundle("/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual).contains(patientId);
		assertThat(actual).contains(practitionerId);
	}

	@Test
	public void patientEverything_shouldReturnDevice_whenDeviceRefersToPatient() throws Exception {

		Patient patient = new Patient();
		String patientId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();
		Reference referenceToPatient = new Reference();
		referenceToPatient.setReference(patientId);

		Device device = new Device();
		device.setPatient(referenceToPatient);
		String deviceId = myClient.create().resource(device).execute().getId().toUnqualifiedVersionless().getValue();


		Bundle bundle = fetchBundle("/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual).contains(patientId);
		assertThat(actual).contains(deviceId);
	}


	/**
	 * @param thePath the path below the server base, e.g. {@literal "/Patient/123/$everything"}
	 */
	private Bundle fetchBundle(String thePath, EncodingEnum theEncoding) {
		HttpTestResponse resp = myServer.fhirRequest(thePath).get();
		assertEquals(theEncoding.getResourceContentTypeNonLegacy(), resp.getContentType());
		return theEncoding.newParser(myFhirContext).parseResource(Bundle.class, resp.getBody());
	}

}

