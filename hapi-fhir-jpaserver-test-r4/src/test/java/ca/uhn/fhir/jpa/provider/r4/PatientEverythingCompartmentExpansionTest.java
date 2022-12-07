package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SuppressWarnings("Duplicates")
public class PatientEverythingCompartmentExpansionTest extends BaseResourceProviderR4Test {

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setEverythingIncludesFetchPageSize(new DaoConfig().getEverythingIncludesFetchPageSize());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myDaoConfig.setAllowMultipleDelete(true);
	}

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

		Bundle bundle = fetchBundle(myServerBase + "/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual, hasItem(patientId));
		assertThat(actual, hasItem(medicationId));
		assertThat(actual, hasItem(medicationAdministrationId));
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

		Bundle bundle = fetchBundle(myClient.getServerBase() + "/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual, hasItem(patientId));
		assertThat(actual, hasItem(organizationId));
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

		Bundle bundle = fetchBundle(myClient.getServerBase() + "/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual, hasItem(patientId));
		assertThat(actual, hasItem(organizationId));
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

		Bundle bundle = fetchBundle(myClient.getServerBase() + "/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(actual, hasItem(patientId));
		assertThat(actual, hasItem(practitionerId));
	}

	private Bundle fetchBundle(String theUrl, EncodingEnum theEncoding) throws IOException {
		Bundle bundle;
		HttpGet get = new HttpGet(theUrl);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(theEncoding.getResourceContentTypeNonLegacy(), resp.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
			bundle = theEncoding.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		} finally {
			IOUtils.closeQuietly(resp);
		}

		return bundle;
	}

}

