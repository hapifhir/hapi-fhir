package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.time.DateUtils.MILLIS_PER_SECOND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ResourceProviderInterceptorR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInterceptorR4Test.class);
	private List<Object> myInterceptors = new ArrayList<>();
	@Mock
	private IAnonymousInterceptor myHook;
	@Captor
	private ArgumentCaptor<HookParams> myParamsCaptor;

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		ourRestServer.getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testPerfInterceptors() {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(15, 100));
		for (int i = 0; i < 30; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			ourLog.info("About to create patient");
			myPatientDao.create(p);
		}

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_FAILED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, interceptor);
		myInterceptors.add(interceptor);

		myInterceptors.add(new PerformanceTracingLoggingInterceptor());

		ourLog.info("About to perform search...");

		Bundle results = myClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FAILED), myParamsCaptor.capture());

		SearchRuntimeDetails details = myParamsCaptor.getAllValues().get(0).get(SearchRuntimeDetails.class);
		assertEquals(SearchStatusEnum.PASSCMPLET, details.getSearchStatus());

		// Load the next (and final) page
		reset(interceptor);
		results = myClient.loadPage().next(results).execute();
		assertNotNull(results);
		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, timeout(10000).times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FAILED), myParamsCaptor.capture());

	}


	@Test
	public void testCreateConditionalNoOpResourceInTransaction() throws Exception {
		String methodName = "foo";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		BundleEntryComponent entry = bundle.addEntry();
		entry.setFullUrl("Patient");
		entry.setResource(pt);
		entry.getRequest().setMethod(HTTPVerb.POST);
		entry.getRequest().setUrl("Patient");

		// Transaction time!
		transaction(bundle);

		// Do it again but with a conditional create that shouldn't actually create
		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, interceptor);

		entry.getRequest().setIfNoneExist("Patient?name=" + methodName);
		transaction(bundle);

		/*
		 * Server Interceptor
		 */

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(1)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED), myParamsCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, myParamsCaptor.getAllValues().get(0).get(RestOperationTypeEnum.class));

		verify(interceptor, times(1)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED), myParamsCaptor.capture());

	}

	@Test
	public void testCreateResource() throws IOException {
		String methodName = "testCreateResource";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirContext.newXmlParser().encodeResourceToString(pt);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
		}

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(1)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED), myParamsCaptor.capture());
		assertEquals(RestOperationTypeEnum.CREATE, myParamsCaptor.getValue().get(RestOperationTypeEnum.class));
		assertEquals("Patient", myParamsCaptor.getValue().get(RequestDetails.class).getResource().getIdElement().getResourceType());

	}

	@Test
	public void testCreateResourceInTransaction() throws IOException {
		String methodName = "testCreateResourceInTransaction";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		BundleEntryComponent entry = bundle.addEntry();
		entry.setFullUrl("Patient");
		entry.setResource(pt);
		entry.getRequest().setMethod(HTTPVerb.POST);
		entry.getRequest().setUrl("Patient");

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, interceptor);

		transaction(bundle);

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(2)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED), myParamsCaptor.capture());
		assertEquals(RestOperationTypeEnum.CREATE, myParamsCaptor.getValue().get(RestOperationTypeEnum.class));
		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(1)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED), myParamsCaptor.capture());

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(1)).invoke(eq(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED), myParamsCaptor.capture());
	}

	@Test
	public void testCreateReflexResourceTheHardWay() {
		ServerOperationInterceptorAdapter interceptor = new ReflexInterceptor();

		ourRestServer.registerInterceptor(interceptor);
		try {

			Patient p = new Patient();
			p.setActive(true);
			IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

			Bundle observations = myClient
				.search()
				.forResource("Observation")
				.where(Observation.SUBJECT.hasId(pid))
				.returnBundle(Bundle.class)
				.execute();
			assertEquals(1, observations.getEntry().size());
			ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(observations));

		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testCreateResourceWithVersionedReference() throws IOException, ServletException {
		String methodName = "testCreateResourceWithVersionedReference";

		Organization org = new Organization();
		org.setName("orgName");
		IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualified();
		assertNotNull(orgId.getVersionIdPartAsLong());

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		pt.setManagingOrganization(new Reference(orgId));

		IParser parser = myFhirContext.newXmlParser();
		parser.setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		parser.setPrettyPrint(true);
		String resource = parser.encodeResourceToString(pt);

		ourLog.info(resource);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
		}

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(1)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED), myParamsCaptor.capture());
		assertEquals(RestOperationTypeEnum.CREATE, myParamsCaptor.getValue().get(RestOperationTypeEnum.class));

		Patient patient = (Patient) myParamsCaptor.getValue().get(RequestDetails.class).getResource();
		assertEquals(orgId.getValue(), patient.getManagingOrganization().getReference());

	}

	@Test
	public void testUpdateNoOpResourceInTransaction() throws Exception {
		String methodName = "foo";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String ptId = myPatientDao.create(pt).getId().toUnqualifiedVersionless().getValue();

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.TRANSACTION);
		BundleEntryComponent entry = bundle.addEntry();
		entry.setFullUrl(ptId);
		entry.setResource(pt);
		entry.getRequest().setMethod(HTTPVerb.PUT);
		entry.getRequest().setUrl(ptId);

		// Transaction time!
		transaction(bundle);

		// Do it again but with an update that shouldn't actually create
		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, interceptor);
		ourRestServer.getInterceptorService().registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED, interceptor);

		entry.getRequest().setIfNoneExist("Patient?name=" + methodName);
		transaction(bundle);

		verify(interceptor, timeout(10 * MILLIS_PER_SECOND).times(2)).invoke(eq(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED), myParamsCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, myParamsCaptor.getAllValues().get(0).get(RestOperationTypeEnum.class));
		assertEquals(RestOperationTypeEnum.UPDATE, myParamsCaptor.getAllValues().get(1).get(RestOperationTypeEnum.class));
		verify(interceptor, times(0)).invoke(eq(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED), any());
		verify(interceptor, times(0)).invoke(eq(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED), any());

	}


	private void transaction(Bundle theBundle) throws IOException {
		String resource = myFhirContext.newXmlParser().encodeResourceToString(theBundle);
		HttpPost post = new HttpPost(ourServerBase + "/");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
		}
	}

	@Test
	public void testInterceptorExpandsSearch() {

		@Interceptor
		class SearchExpandingInterceptor {

			@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
			public void enrich(RequestDetails theRequestDetails) {

				String[] subjectValues = theRequestDetails.getParameters().get("subject");
				if (subjectValues != null) {
					for (int index = 0; index < subjectValues.length; index++) {
						String nextValue = subjectValues[index];
						if (nextValue.equals("Patient/p1")) {
							nextValue = "Patient/p1,Patient/p2";
							subjectValues[index] = nextValue;
						}
					}
				}

			}

		}

		Patient p1 = new Patient();
		p1.setId("p1");
		p1.addIdentifier().setValue("p1");
		myPatientDao.update(p1);

		Observation o1 = new Observation();
		o1.setId("o1");
		o1.getSubject().setReference("Patient/p1");
		myObservationDao.update(o1);

		Patient p2 = new Patient();
		p2.setId("p2");
		p2.addIdentifier().setValue("p2");
		myPatientDao.update(p2);

		Observation o2 = new Observation();
		o2.setId("o2");
		o2.getSubject().setReference("Patient/p2");
		myObservationDao.update(o2);

		Patient p3 = new Patient();
		p3.setId("p3");
		p3.addIdentifier().setValue("p3");
		myPatientDao.update(p3);

		Observation o3 = new Observation();
		o3.setId("o3");
		o3.getSubject().setReference("Patient/p3");
		myObservationDao.update(o3);

		SearchExpandingInterceptor interceptor = new SearchExpandingInterceptor();
		try {
			ourRestServer.registerInterceptor(interceptor);

			Bundle bundle = myClient
				.search()
				.forResource(Observation.class)
				.where(Observation.SUBJECT.hasId("Patient/p1"))
				.returnBundle(Bundle.class)
				.execute();
			List<String> ids = toUnqualifiedVersionlessIdValues(bundle);
			assertThat(ids, containsInAnyOrder("Observation/o1", "Observation/o2"));

		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}


	}

	public class ReflexInterceptor extends ServerOperationInterceptorAdapter {
		@Override
		public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
			if (theResource instanceof Patient) {
				((ServletRequestDetails) theRequest).getServletRequest().setAttribute("CREATED_PATIENT", theResource);
			}
		}

		@Override
		public void processingCompletedNormally(ServletRequestDetails theRequestDetails) {
			Patient createdPatient = (Patient) theRequestDetails.getServletRequest().getAttribute("CREATED_PATIENT");
			if (createdPatient != null) {
				Observation observation = new Observation();
				observation.setSubject(new Reference(createdPatient.getId()));

				myClient.create().resource(observation).execute();
			}
		}
	}

}
