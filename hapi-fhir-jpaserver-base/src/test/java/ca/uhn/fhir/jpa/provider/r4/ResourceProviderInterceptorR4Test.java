package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.interceptor.PerformanceTracingLoggingInterceptor;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.interceptor.ServerOperationInterceptorAdapter;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ResourceProviderInterceptorR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInterceptorR4Test.class);
	private IServerOperationInterceptor myDaoInterceptor;

	private IServerOperationInterceptor myServerInterceptor;
	private List<Object> myInterceptors = new ArrayList<>();
	@Mock
	private IAnonymousInterceptor myHook;
	@Captor
	private ArgumentCaptor<HookParams> myParamsCaptor;

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		ourRestServer.unregisterInterceptor(myServerInterceptor);

		myInterceptorRegistry.unregisterInterceptors(myInterceptors);
		myInterceptors.clear();
	}

	@Override
	public void before() throws Exception {
		super.before();

		myServerInterceptor = mock(IServerOperationInterceptor.class);
		myDaoInterceptor = mock(IServerOperationInterceptor.class);

		resetServerInterceptor();

		ourRestServer.registerInterceptor(myServerInterceptor);

		ourRestServer.registerInterceptor(new InterceptorAdapter() {
			@Override
			public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
				super.incomingRequestPreHandled(theOperation, theProcessedRequest);
			}
		});

	}

	private void resetServerInterceptor() throws ServletException, IOException {
		reset(myServerInterceptor);
		reset(myDaoInterceptor);
		when(myServerInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(ResponseDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
	}

	@Test
	public void testPerfInterceptors() throws InterruptedException {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(15, 100));
		for (int i = 0; i < 30; i++) {
			Patient p = new Patient();
			p.addName().setFamily("FAM" + i);
			ourLog.info("About to create patient");
			myPatientDao.create(p);
		}

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_FAILED, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, interceptor);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, interceptor);
		myInterceptors.add(interceptor);

		myInterceptors.add(new PerformanceTracingLoggingInterceptor());

		ourLog.info("About to perform search...");

		Bundle results = ourClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();

		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED), myParamsCaptor.capture());
		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FAILED), myParamsCaptor.capture());

		SearchRuntimeDetails details = myParamsCaptor.getAllValues().get(0).get(SearchRuntimeDetails.class);
		assertEquals(SearchStatusEnum.PASSCMPLET, details.getSearchStatus());

		// Load the next (and final) page
		reset(interceptor);
		results = ourClient.loadPage().next(results).execute();
		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED), myParamsCaptor.capture());
		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE), myParamsCaptor.capture());
		verify(interceptor, times(0)).invoke(eq(Pointcut.JPA_PERFTRACE_SEARCH_FAILED), myParamsCaptor.capture());

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
		resetServerInterceptor();
		entry.getRequest().setIfNoneExist("Patient?name=" + methodName);
		transaction(bundle);

		/*
		 * Server Interceptor
		 */

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));

		verify(myServerInterceptor, times(1)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		verify(myServerInterceptor, times(0)).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));
		verify(myServerInterceptor, times(0)).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

		/*
		 * DAO Interceptor
		 */

		verify(myDaoInterceptor, times(0)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		verify(myDaoInterceptor, times(0)).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));
		verify(myDaoInterceptor, times(0)).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

	}

	@Test
	public void testCreateResource() throws IOException {
		String methodName = "testCreateResource";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		verify(myServerInterceptor, times(0)).incomingRequestPreHandled(any(RestOperationTypeEnum.class), any(ActionRequestDetails.class));
		verify(myDaoInterceptor, times(0)).incomingRequestPreHandled(any(RestOperationTypeEnum.class), any(ActionRequestDetails.class));

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
		}

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getValue());
		assertEquals("Patient", ardCaptor.getValue().getResourceType());
		assertNotNull(ardCaptor.getValue().getResource());

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

		transaction(bundle);

		/*
		 * Server Interceptor
		 */

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(2)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));
		assertEquals(null, ardCaptor.getAllValues().get(0).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(0).getResource());
		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getAllValues().get(1));
		assertEquals("Patient", ardCaptor.getAllValues().get(1).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(1).getResource());

		ArgumentCaptor<RequestDetails> rdCaptor = ArgumentCaptor.forClass(RequestDetails.class);
		ArgumentCaptor<HttpServletRequest> srCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
		ArgumentCaptor<HttpServletResponse> sRespCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
		verify(myServerInterceptor, times(1)).incomingRequestPostProcessed(rdCaptor.capture(), srCaptor.capture(), sRespCaptor.capture());

	}

	@Test
	public void testCreateReflexResourceTheHardWay() {
		ServerOperationInterceptorAdapter interceptor = new ReflexInterceptor();

		ourRestServer.registerInterceptor(interceptor);
		try {

			Patient p = new Patient();
			p.setActive(true);
			IIdType pid = ourClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

			Bundle observations = ourClient
				.search()
				.forResource("Observation")
				.where(Observation.SUBJECT.hasId(pid))
				.returnBundle(Bundle.class)
				.execute();
			assertEquals(1, observations.getEntry().size());
			ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(observations));

		} finally {
			ourRestServer.unregisterInterceptor(interceptor);
		}
	}


	@Test
	public void testCreateResourceWithVersionedReference() throws IOException, ServletException {
		String methodName = "testCreateResourceWithVersionedReference";

		Organization org = new Organization();
		org.setName("orgName");
		IIdType orgId = ourClient.create().resource(org).execute().getId().toUnqualified();
		assertNotNull(orgId.getVersionIdPartAsLong());

		resetServerInterceptor();

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		pt.setManagingOrganization(new Reference(orgId));

		IParser parser = myFhirCtx.newXmlParser();
		parser.setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		parser.setPrettyPrint(true);
		String resource = parser.encodeResourceToString(pt);

		ourLog.info(resource);

		verify(myServerInterceptor, times(0)).incomingRequestPreHandled(any(RestOperationTypeEnum.class), any(ActionRequestDetails.class));
		verify(myDaoInterceptor, times(0)).incomingRequestPreHandled(any(RestOperationTypeEnum.class), any(ActionRequestDetails.class));

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
		}

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());

		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getValue());
		assertEquals("Patient", ardCaptor.getValue().getResourceType());
		assertNotNull(ardCaptor.getValue().getResource());

		Patient patient;
		patient = (Patient) ardCaptor.getAllValues().get(0).getResource();
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
		resetServerInterceptor();
		entry.getRequest().setIfNoneExist("Patient?name=" + methodName);
		transaction(bundle);

		/*
		 * Server Interceptor
		 */

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(2)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));
		assertEquals(RestOperationTypeEnum.UPDATE, opTypeCaptor.getAllValues().get(1));

		verify(myServerInterceptor, times(1)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		verify(myServerInterceptor, times(0)).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));
		verify(myServerInterceptor, times(0)).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

		/*
		 * DAO Interceptor
		 */

		verify(myDaoInterceptor, times(0)).incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class));
		verify(myDaoInterceptor, times(0)).resourceCreated(any(RequestDetails.class), any(IBaseResource.class));
		verify(myDaoInterceptor, times(0)).resourceUpdated(any(RequestDetails.class), any(IBaseResource.class), any(IBaseResource.class));

	}


	private void transaction(Bundle theBundle) throws IOException {
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(theBundle);
		HttpPost post = new HttpPost(ourServerBase + "/");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
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

				ourClient.create().resource(observation).execute();
			}
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static void verifyDaoInterceptor(IServerInterceptor theDaoInterceptor) {
		ArgumentCaptor<ActionRequestDetails> ardCaptor;
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor;
		ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(theDaoInterceptor, atLeast(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
//		boolean good = false;
//		for (int i = 0; i < opTypeCaptor.getAllValues().size(); i++) {
//			if (RestOperationTypeEnum.CREATE.equals(opTypeCaptor.getAllValues().get(i))) {
//				if ("Patient".equals(ardCaptor.getValue().getResourceType())) {
//					if (ardCaptor.getValue().getResource() != null) {
//						good = true;
//					}
//				}
//			}
//		}
//		assertTrue(good);
	}

}
