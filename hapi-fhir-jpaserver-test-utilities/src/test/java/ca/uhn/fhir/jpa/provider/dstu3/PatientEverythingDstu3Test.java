package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setEverythingIncludesFetchPageSize(new DaoConfig().getEverythingIncludesFetchPageSize());
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);

		myOrg = new Organization();
		myOrg.setName("an org");
		myOrgId = ourClient.create().resource(myOrg).execute().getId().toUnqualifiedVersionless().getValue();
		myOrg.setId(myOrgId);
		ourLog.info("OrgId: {}", myOrgId);

		myPatient = new Patient();
		myPatient.getManagingOrganization().setReference(myOrgId);
		myPatientId = ourClient.create().resource(myPatient).execute().getId().toUnqualifiedVersionless().getValue();
		myPatient.setId(myPatientId);

		Patient patient2 = new Patient();
		patient2.getManagingOrganization().setReference(myOrgId);
		myWrongPatId = ourClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReference(myPatientId);
		enc1.getServiceProvider().setReference(myOrgId);
		encId1 = ourClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReference(myPatientId);
		enc2.getServiceProvider().setReference(myOrgId);
		encId2 = ourClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter wrongEnc1 = new Encounter();
		wrongEnc1.setStatus(EncounterStatus.ARRIVED);
		wrongEnc1.getSubject().setReference(myWrongPatId);
		wrongEnc1.getServiceProvider().setReference(myOrgId);
		myWrongEnc1 = ourClient.create().resource(wrongEnc1).execute().getId().toUnqualifiedVersionless().getValue();

		myObsIds = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientId);
			obs.setStatus(ObservationStatus.FINAL);
			String obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			myObsIds.add(obsId);
		}

	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingReturnsCorrectResources() throws Exception {
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);
		
		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);
		
		assertThat(actual, hasItem(myPatientId));
		assertThat(actual, hasItem(encId1));
		assertThat(actual, hasItem(encId2));
		assertThat(actual, hasItem(myOrgId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
		assertThat(actual, not(hasItem(myWrongPatId)));
		assertThat(actual, not(hasItem(myWrongEnc1)));
	}

	@Test
	public void testEverythingHandlesCircularReferences() throws Exception {
		Patient linkedPatient1 = new Patient();
		linkedPatient1.addLink().setOther(new Reference(myPatientId));
		String linkedPatient1Id = ourClient.create().resource(linkedPatient1).execute().getId().toUnqualifiedVersionless().getValue();

		Patient linkedPatient2 = new Patient();
		linkedPatient2.addLink().setOther(new Reference(linkedPatient1Id));
		String linkedPatient2Id = ourClient.create().resource(linkedPatient2).execute().getId().toUnqualifiedVersionless().getValue();

		myPatient.addLink().setOther(new Reference(linkedPatient2Id));
		ourClient.update().resource(myPatient).execute();

		Bundle bundle = fetchBundle(ourServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);

		assertThat(actual, hasItem(myPatientId));
		assertThat(actual, hasItem(linkedPatient1Id));
		assertThat(actual, hasItem(linkedPatient2Id));
		assertThat(actual, hasItem(encId1));
		assertThat(actual, hasItem(encId2));
		assertThat(actual, hasItem(myOrgId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
		assertThat(actual, not(hasItem(myWrongPatId)));
		assertThat(actual, not(hasItem(myWrongEnc1)));

	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingReturnsCorrectResourcesSmallPage() throws Exception {
		myDaoConfig.setEverythingIncludesFetchPageSize(1);
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + myPatientId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);
		
		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);
		
		assertThat(actual, hasItem(myPatientId));
		assertThat(actual, hasItem(encId1));
		assertThat(actual, hasItem(encId2));
		assertThat(actual, hasItem(myOrgId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
		assertThat(actual, not(hasItem(myWrongPatId)));
		assertThat(actual, not(hasItem(myWrongEnc1)));
	}
	
	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingJson() throws Exception {
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + myPatientId + "/$everything?_format=json&_count=1", EncodingEnum.JSON);
		
		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl(), containsString("_format=json"));
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);
		
		assertNotNull(bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl(), containsString("_format=json"));
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.JSON);
	}

	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingXml() throws Exception {
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + myPatientId + "/$everything?_format=xml&_count=1", EncodingEnum.XML);
		
		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl(), containsString("_format=xml"));
		bundle = fetchBundle(bundle.getLink("next").getUrl(), EncodingEnum.XML);

		assertNotNull(bundle.getLink("next").getUrl());
		ourLog.info("Next link: {}", bundle.getLink("next").getUrl());
		assertThat(bundle.getLink("next").getUrl(), containsString("_format=xml"));
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
