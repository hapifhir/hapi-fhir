package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientEverythingDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientEverythingDstu3Test.class);
	private String myOrgId;
	private String myPatientId;
	private String encId1;
	private String encId2;
	private ArrayList<String> myObsIds;
	private String myWrongPatId;
	private String myWrongEnc1;
	private Organization myOrg;
	private Patient myPatient;

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
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);

		myOrg = new Organization();
		myOrg.setName("an org");
		myOrgId = myClient.create().resource(myOrg).execute().getId().toUnqualifiedVersionless().getValue();
		myOrg.setId(myOrgId);
		ourLog.info("OrgId: {}", myOrgId);

		myPatient = new Patient();
		myPatient.getManagingOrganization().setReference(myOrgId);
		myPatientId = myClient.create().resource(myPatient).execute().getId().toUnqualifiedVersionless().getValue();
		myPatient.setId(myPatientId);

		Patient patient2 = new Patient();
		patient2.getManagingOrganization().setReference(myOrgId);
		myWrongPatId = myClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReference(myPatientId);
		enc1.getServiceProvider().setReference(myOrgId);
		encId1 = myClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReference(myPatientId);
		enc2.getServiceProvider().setReference(myOrgId);
		encId2 = myClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter wrongEnc1 = new Encounter();
		wrongEnc1.setStatus(EncounterStatus.ARRIVED);
		wrongEnc1.getSubject().setReference(myWrongPatId);
		wrongEnc1.getServiceProvider().setReference(myOrgId);
		myWrongEnc1 = myClient.create().resource(wrongEnc1).execute().getId().toUnqualifiedVersionless().getValue();

		myObsIds = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientId);
			obs.setStatus(ObservationStatus.FINAL);
			String obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			myObsIds.add(obsId);
		}

	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingReturnsCorrectResources() throws Exception {
		
		Bundle bundle = fetchBundle(myServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).contains(myPatientId);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).contains(myObsIds.toArray(new String[0]));
		assertThat(actual).doesNotContain(myWrongPatId);
		assertThat(actual).doesNotContain(myWrongEnc1);
	}

	@Test
	public void testEverythingHandlesCircularReferences() throws Exception {
		CarePlan cp1 = new CarePlan();
		cp1.setSubject(new Reference(myPatientId));
		String cp1Id = myClient.create().resource(cp1).execute().getId().toUnqualifiedVersionless().getValue();

		CarePlan cp2 = new CarePlan();
		cp2.addBasedOn(new Reference(cp1Id));
		String cp2Id = myClient.create().resource(cp2).execute().getId().toUnqualifiedVersionless().getValue();

		cp1.addBasedOn(new Reference(cp2Id));
		cp1.setId(cp1Id);
		myClient.update().resource(cp1).execute();

		Bundle bundle = fetchBundle(myServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).contains(myPatientId);
		assertThat(actual).contains(cp1Id);
		assertThat(actual).contains(cp2Id);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(myOrgId);
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
		
		Bundle bundle = fetchBundle(myServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);

		assertThat(actual).contains(myPatientId);
		assertThat(actual).contains(encId1);
		assertThat(actual).contains(encId2);
		assertThat(actual).contains(myOrgId);
		assertThat(actual).contains(myObsIds.toArray(new String[0]));
		assertThat(actual).doesNotContain(myWrongPatId);
		assertThat(actual).doesNotContain(myWrongEnc1);
	}
	
	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingJson() throws Exception {
		
		Bundle bundle = fetchBundle(myServerBase + "/" + myPatientId + "/$everything?_format=json&_count=1", EncodingEnum.JSON);

		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=json");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);

		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=json");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);
	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingXml() throws Exception {
		
		Bundle bundle = fetchBundle(myServerBase + "/" + myPatientId + "/$everything?_format=xml&_count=1", EncodingEnum.XML);

		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=xml");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.XML);

		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl()).contains("_format=xml");
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.XML);
	}

	private Bundle fetchBundle(String theUrl, EncodingEnum theEncoding) throws IOException, ClientProtocolException {
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
