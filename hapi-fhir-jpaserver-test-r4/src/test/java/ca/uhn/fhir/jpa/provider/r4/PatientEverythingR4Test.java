package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("Duplicates")
public class PatientEverythingR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientEverythingR4Test.class);
	private String orgId;
	private String patId;
	private String taskId;
	private String encId1;
	private String encId2;
	private ArrayList<String> myObsIds;
	private String myWrongPatId;
	private String myWrongEnc1;

	@BeforeEach
	public void beforeDisableResultReuse() {
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setEverythingIncludesFetchPageSize(new JpaStorageSettings().getEverythingIncludesFetchPageSize());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);
		
		Organization org = new Organization();
		org.setName("an org");
		orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless().getValue();
		ourLog.info("OrgId: {}", orgId);

		Patient patient = new Patient();
		patient.getManagingOrganization().setReference(orgId);
		patId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient2 = new Patient();
		patient2.getManagingOrganization().setReference(orgId);
		myWrongPatId = myClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReference(patId);
		enc1.getServiceProvider().setReference(orgId);
		encId1 = myClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReference(patId);
		enc2.getServiceProvider().setReference(orgId);
		encId2 = myClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless().getValue();

		Task task = new Task();
		task.setStatus(Task.TaskStatus.COMPLETED);
		task.getOwner().setReference(patId);
		taskId = myClient.create().resource(task).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter wrongEnc1 = new Encounter();
		wrongEnc1.setStatus(EncounterStatus.ARRIVED);
		wrongEnc1.getSubject().setReference(myWrongPatId);
		wrongEnc1.getServiceProvider().setReference(orgId);
		myWrongEnc1 = myClient.create().resource(wrongEnc1).execute().getId().toUnqualifiedVersionless().getValue();

		myObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(patId);
			obs.setStatus(ObservationStatus.FINAL);
			String obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			myObsIds.add(obsId);
		}

	}

	@Test
	public void testEverythingWithCanonicalReferences() throws Exception {
		myStorageSettings.setAllowExternalReferences(true);

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://example.com/Organization/123"));
		String patientId = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		Observation obs = new Observation();
		obs.getSubject().setReference(patientId);
		obs.getEncounter().setReference("http://example.com/Encounter/999");
		String observationId = myObservationDao.create(obs).getId().toUnqualifiedVersionless().getValue();

		// Normal call
		Bundle bundle = fetchBundle(myServerBase + "/" + patientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);
		assertNull(bundle.getLink("next"));
		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		assertThat(actual).containsExactlyInAnyOrder(patientId, observationId);

		// Synchronous call
		HttpGet get = new HttpGet(myServerBase + "/" + patientId + "/$everything?_format=json&_count=100");
		get.addHeader(Constants.HEADER_CACHE_CONTROL, Constants.CACHE_CONTROL_NO_CACHE);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(EncodingEnum.JSON.getResourceContentTypeNonLegacy(), resp.getFirstHeader(ca.uhn.fhir.rest.api.Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
			bundle = EncodingEnum.JSON.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		}
		assertNull(bundle.getLink("next"));
		actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		assertThat(actual).containsExactlyInAnyOrder(patientId, observationId);
	}


	/**
	 * See #674
	 */
	@Test
	public void testEverythingReturnsCorrectResources() throws Exception {

		Bundle bundle = fetchBundle(myServerBase + "/" + patId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).contains(patId);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(orgId);
		assertThat(actual).contains(taskId);
		assertThat(actual).contains(myObsIds.toArray(new String[0]));
		assertThat(actual).doesNotContain(myWrongPatId);
		assertThat(actual).doesNotContain(myWrongEnc1);
	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingReturnsCorrectResourcesSmallPage() throws Exception {
		myStorageSettings.setEverythingIncludesFetchPageSize(1);
		
		Bundle bundle = fetchBundle(myServerBase + "/" + patId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).contains(patId);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(orgId);
		assertThat(actual).contains(myObsIds.toArray(new String[0]));
		assertThat(actual).doesNotContain(myWrongPatId);
		assertThat(actual).doesNotContain(myWrongEnc1);
	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingJson() throws Exception {

		Bundle bundle = fetchBundle(myServerBase + "/" + patId + "/$everything?_format=json&_count=1", EncodingEnum.JSON);

		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=json");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);

		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=json");
		fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);
	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingXml() throws Exception {

		Bundle bundle = fetchBundle(myServerBase + "/" + patId + "/$everything?_format=xml&_count=1", EncodingEnum.XML);

		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=xml");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.XML);

		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=xml");
		fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.XML);
	}

	@Test
	//See https://github.com/hapifhir/hapi-fhir/issues/3215
	public void testEverythingWithLargeResultSetDoesNotNpe() throws IOException {
		for (int i = 0; i < 500; i++) {
			Observation obs1 = new Observation();
			obs1.setSubject(new Reference(patId));
			obs1.getCode().addCoding().setCode("CODE1");
			obs1.setValue(new StringType("obsvalue1"));
			myObservationDao.create(obs1, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
		}

		Bundle bundle = fetchBundle(myServerBase + "/" + patId + "/$everything?_format=json&_count=250", EncodingEnum.JSON);
		do {
			String next = bundle.getLink("next").getUrl();

			//This used to NPE!
			bundle = fetchBundle(next, EncodingEnum.JSON);
		} while (bundle.getLink("next") != null);
	}

	/**
	 * Built to reproduce <a href="https://gitlab.com/simpatico.ai/cdr/-/issues/4940">this issue</a>
	 */
	@Test
	public void testEverythingRespectsServerDefaultPageSize() throws IOException {
		// setup
		for (int i = 0; i < 25; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily("lastn").addGiven("name");
			myPatientDao.create(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
		}

		// must be larger than myStorageSettings.getSearchPreFetchThresholds()[0] for issue to show up
		int originalPagingProviderPageSize = myPagingProvider.getDefaultPageSize();
		myPagingProvider.setDefaultPageSize(50);

		// execute
		Bundle bundle;
		try {
			bundle = fetchBundle(myServerBase + "/Patient/$everything?_format=json", EncodingEnum.JSON);
		} finally {
			// restore
			myPagingProvider.setDefaultPageSize(originalPagingProviderPageSize);
		}

		// validate
		List<Patient> bundlePatients = BundleUtil.toListOfResourcesOfType(myFhirContext, bundle, Patient.class);
		assertThat(bundlePatients).hasSize(myServer.getDefaultPageSize());
	}

	private Bundle fetchBundle(String theUrl, EncodingEnum theEncoding) throws IOException {
		Bundle bundle;
		HttpGet get = new HttpGet(theUrl);
		CloseableHttpResponse resp = ourHttpClient.execute(get);
		try {
			assertEquals(theEncoding.getResourceContentTypeNonLegacy(), resp.getFirstHeader(ca.uhn.fhir.rest.api.Constants.HEADER_CONTENT_TYPE).getValue().replaceAll(";.*", ""));
			bundle = theEncoding.newParser(myFhirContext).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		} finally {
			IOUtils.closeQuietly(resp);
		}

		return bundle;
	}

}
