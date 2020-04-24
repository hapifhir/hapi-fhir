package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CompositionDocumentR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompositionDocumentR4Test.class);
	private String orgId;
	private String patId;
	private List<String> myObsIds;
	private String encId;
	private String listId;
	private String compId;
	@Captor
	private ArgumentCaptor<HookParams> myHookParamsCaptor;

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
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc.getSubject().setReference(patId);
		enc.getServiceProvider().setReference(orgId);
		encId = ourClient.create().resource(enc).execute().getId().toUnqualifiedVersionless().getValue();

		ListResource listResource = new ListResource();

		ArrayList<Observation> myObs = new ArrayList<>();
		myObsIds = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(patId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
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
	public void testDocumentBundleReturnedCorrect() throws IOException {

		String theUrl = ourServerBase + "/" + compId + "/$document?_format=json";
		Bundle bundle = fetchBundle(theUrl, EncodingEnum.JSON);

		assertNull(bundle.getLink("next"));

		Set<String> actual = new HashSet<>();
		for (Bundle.BundleEntryComponent nextEntry : bundle.getEntry()) {
			actual.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		ourLog.info("Found IDs: {}", actual);
		assertThat(actual, hasItem(compId));
		assertThat(actual, hasItem(patId));
		assertThat(actual, hasItem(orgId));
		assertThat(actual, hasItem(encId));
		assertThat(actual, hasItem(listId));
		assertThat(actual, hasItems(myObsIds.toArray(new String[0])));
	}

	@Test
	public void testInterceptorHookIsCalledForAllContents_STORAGE_PREACCESS_RESOURCES() throws IOException {

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PREACCESS_RESOURCES, interceptor);
		try {

			ourLog.info("Composition ID: {}", compId);
			List<String> returnedClasses = new ArrayList<>();
			doAnswer(t->{
				HookParams param = t.getArgument(1, HookParams.class);
				IPreResourceAccessDetails nextPreResourceAccessDetails = param.get(IPreResourceAccessDetails.class);
				for (int i = 0; i < nextPreResourceAccessDetails.size(); i++) {
					String className = nextPreResourceAccessDetails.getResource(i).getClass().getSimpleName();
					ourLog.info("* Preaccess called on {}", nextPreResourceAccessDetails.getResource(i).getIdElement().getValue());
					returnedClasses.add(className);
				}
				return null;
			}).when(interceptor).invoke(eq(Pointcut.STORAGE_PREACCESS_RESOURCES), any());

			String theUrl = ourServerBase + "/" + compId + "/$document?_format=json";
			Bundle bundle = fetchBundle(theUrl, EncodingEnum.JSON);
			for (Bundle.BundleEntryComponent next : bundle.getEntry()) {
				ourLog.info("Bundle contained: {}", next.getResource().getIdElement().getValue());
			}

			Mockito.verify(interceptor, times(2)).invoke(eq(Pointcut.STORAGE_PREACCESS_RESOURCES), myHookParamsCaptor.capture());

			ourLog.info("Returned classes: {}", returnedClasses);

			assertThat(returnedClasses, hasItem("Composition"));
			assertThat(returnedClasses, hasItem("Organization"));

		} finally {

			ourRestServer.getInterceptorService().unregisterInterceptor(interceptor);

		}
	}

	private Bundle fetchBundle(String theUrl, EncodingEnum theEncoding) throws IOException {
		Bundle bundle;
		HttpGet get = new HttpGet(theUrl);

		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String resourceString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			bundle = theEncoding.newParser(myFhirCtx).parseResource(Bundle.class, resourceString);
		}
		return bundle;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
