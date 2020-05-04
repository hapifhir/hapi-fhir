package ca.uhn.fhir.jpa.provider.r4;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.junit.*;

import com.google.common.base.Charsets;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.TestUtil;

@SuppressWarnings("Duplicates")
public class PatientEverythingR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PatientEverythingR4Test.class);
	private String orgId;
	private String patId;
	private String encId1;
	private String encId2;
	private ArrayList<String> myObsIds;
	private String myWrongPatId;
	private String myWrongEnc1;

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setEverythingIncludesFetchPageSize(new DaoConfig().getEverythingIncludesFetchPageSize());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		
		Organization org = new Organization();
		org.setName("an org");
		orgId = ourClient.create().resource(org).execute().getId().toUnqualifiedVersionless().getValue();
		ourLog.info("OrgId: {}", orgId);

		Patient patient = new Patient();
		patient.getManagingOrganization().setReference(orgId);
		patId = ourClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Patient patient2 = new Patient();
		patient2.getManagingOrganization().setReference(orgId);
		myWrongPatId = ourClient.create().resource(patient2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.setStatus(EncounterStatus.CANCELLED);
		enc1.getSubject().setReference(patId);
		enc1.getServiceProvider().setReference(orgId);
		encId1 = ourClient.create().resource(enc1).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.setStatus(EncounterStatus.ARRIVED);
		enc2.getSubject().setReference(patId);
		enc2.getServiceProvider().setReference(orgId);
		encId2 = ourClient.create().resource(enc2).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter wrongEnc1 = new Encounter();
		wrongEnc1.setStatus(EncounterStatus.ARRIVED);
		wrongEnc1.getSubject().setReference(myWrongPatId);
		wrongEnc1.getServiceProvider().setReference(orgId);
		myWrongEnc1 = ourClient.create().resource(wrongEnc1).execute().getId().toUnqualifiedVersionless().getValue();

		myObsIds = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(patId);
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
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + patId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);
		
		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);
		
		assertThat(actual, hasItem(patId));
		assertThat(actual, hasItem(encId1));
		assertThat(actual, hasItem(encId2));
		assertThat(actual, hasItem(orgId));
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
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + patId + "/$everything?_format=json&_count=100", EncodingEnum.JSON);
		
		assertNull(bundle.getLink("next"));
		
		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		
		ourLog.info("Found IDs: {}", actual);
		
		assertThat(actual, hasItem(patId));
		assertThat(actual, hasItem(encId1));
		assertThat(actual, hasItem(encId2));
		assertThat(actual, hasItem(orgId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
		assertThat(actual, not(hasItem(myWrongPatId)));
		assertThat(actual, not(hasItem(myWrongEnc1)));
	}
	
	/**
	 * See #674
	 */
	@Test
	public void testEverythingPagesWithCorrectEncodingJson() throws Exception {
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + patId + "/$everything?_format=json&_count=1", EncodingEnum.JSON);
		
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
		
		Bundle bundle = fetchBundle(ourServerBase + "/" + patId + "/$everything?_format=xml&_count=1", EncodingEnum.XML);
		
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
			bundle = theEncoding.newParser(myFhirCtx).parseResource(Bundle.class, IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8));
		} finally {
			IOUtils.closeQuietly(resp);
		}
		
		return bundle;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
