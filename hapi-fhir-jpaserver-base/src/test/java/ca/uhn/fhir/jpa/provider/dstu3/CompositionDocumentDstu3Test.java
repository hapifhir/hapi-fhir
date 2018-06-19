package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Encounter.EncounterStatus;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.junit.*;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CompositionDocumentDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompositionDocumentDstu3Test.class);
	private String orgId;
	private String patId;
	private List<String> myObsIds;
	private String encId;
	private String listId;
	private String compId;

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
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

		Encounter enc = new Encounter();
		enc.setStatus(EncounterStatus.ARRIVED);
		enc.getSubject().setReference(patId);
		enc.getServiceProvider().setReference(orgId);
		encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless().getValue();

		ListResource listResource = new ListResource();

		ArrayList<Observation> myObs = new ArrayList<>();
		myObsIds = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(patId);
			obs.setStatus(ObservationStatus.FINAL);
			String obsId = ourClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(obs)));
			myObs.add(obs);
			myObsIds.add(obsId);
		}

		listId = ourClient.create().resource(listResource).execute().getId().toUnqualifiedVersionless().getValue();

		Composition composition = new Composition();
		composition.setSubject(new Reference(patId));
		composition.addSection().addEntry(new Reference(patId));
		composition.addSection().addEntry(new Reference(orgId));
		composition.addSection().addEntry(new Reference(encId));
		composition.addSection().addEntry(new Reference(listId));

		for (String obsId : myObsIds) {
			composition.addSection().addEntry(new Reference(obsId));
		}
		compId = ourClient.create().resource(composition).execute().getId().toUnqualifiedVersionless().getValue();
	}

	@Test
	public void testDocumentBundleReturnedCorrect() throws Exception {
	//	myDaoConfig.setEverythingIncludesFetchPageSize(1);

		String theUrl = ourServerBase + "/" + compId + "/$document?_format=json";
		System.out.println(theUrl);
		Bundle bundle = fetchBundle(theUrl, EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new TreeSet<String>();
		for (BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);
		assertThat(actual, hasItem(patId));
		assertThat(actual, hasItem(orgId));
		assertThat(actual, hasItem(encId));
		assertThat(actual, hasItem(listId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
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
