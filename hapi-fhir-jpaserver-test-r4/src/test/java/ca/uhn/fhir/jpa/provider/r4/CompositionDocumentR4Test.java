package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

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

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);

		Organization org = new Organization();
		org.setName("an org");
		orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless().getValue();
		ourLog.info("OrgId: {}", orgId);

		Patient patient = new Patient();
		patient.getManagingOrganization().setReference(orgId);
		patId = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless().getValue();

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc.getSubject().setReference(patId);
		enc.getServiceProvider().setReference(orgId);
		encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless().getValue();

		ListResource listResource = new ListResource();

		ArrayList<Observation> myObs = new ArrayList<>();
		myObsIds = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReference(patId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			String obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless().getValue();
			listResource.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(obs)));
			myObs.add(obs);
			myObsIds.add(obsId);
		}

		listId = myClient.create().resource(listResource).execute().getId().toUnqualifiedVersionless().getValue();

		Composition composition = new Composition();
		composition.setSubject(new Reference(patId));
		composition.addSection().addEntry(new Reference(patId));
		composition.addSection().addEntry(new Reference(orgId));
		composition.addSection().addEntry(new Reference(encId));
		composition.addSection().addEntry(new Reference(listId));

		for (String obsId : myObsIds) {
			composition.addSection().addEntry(new Reference(obsId));
		}
		compId = myClient.create().resource(composition).execute().getId().toUnqualifiedVersionless().getValue();
	}

	@Test
	public void testDocumentBundleReturnedCorrect() throws IOException {

		String theUrl = ourServerBase + "/" + compId + "/$document?_format=json";
		Bundle bundle = fetchBundle(theUrl, EncodingEnum.JSON);
		//Ensure each entry has a URL.

		assertThat(bundle.getType(), is(equalTo(Bundle.BundleType.DOCUMENT)));
		bundle.getEntry().stream()
				.forEach(entry -> {
					assertThat(entry.getFullUrl(), is(equalTo(entry.getResource().getIdElement().toVersionless().toString())));
				});
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
			bundle = theEncoding.newParser(myFhirContext).parseResource(Bundle.class, resourceString);
		}
		return bundle;
	}

}
