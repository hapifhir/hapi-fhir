package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.provider.r4.ResourceProviderInterceptorR4Test;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
public class ResourceProviderInterceptorDstu2Test extends BaseResourceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInterceptorDstu2Test.class);
	private IServerInterceptor myDaoInterceptor;

	private IServerInterceptor myServerInterceptor;
	private IServerOperationInterceptor myJpaServerInterceptor;

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.getInterceptors().remove(myDaoInterceptor);
		ourRestServer.unregisterInterceptor(myServerInterceptor);
		ourRestServer.unregisterInterceptor(myJpaServerInterceptor);
	}

	@Override
	public void before() throws Exception {
		super.before();

		myServerInterceptor = mock(IServerInterceptor.class);
		myDaoInterceptor = mock(IServerInterceptor.class);
		myJpaServerInterceptor = mock(IServerOperationInterceptor.class);

		when(myServerInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(ResponseDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);

		when(myJpaServerInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myJpaServerInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myJpaServerInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myJpaServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class))).thenReturn(true);
		when(myJpaServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myJpaServerInterceptor.outgoingResponse(any(RequestDetails.class), any(ResponseDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);

		myDaoConfig.getInterceptors().add(myDaoInterceptor);
		ourRestServer.registerInterceptor(myServerInterceptor);
		ourRestServer.registerInterceptor(myJpaServerInterceptor);

		ourRestServer.registerInterceptor(new InterceptorAdapter() {
			@Override
			public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
				super.incomingRequestPreHandled(theOperation, theProcessedRequest);
			}
		});

	}

	@Test
	public void testCreateResource() throws IOException {
		String methodName = "testCreateResource";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

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

		ResourceProviderInterceptorR4Test.verifyDaoInterceptor(myDaoInterceptor);
	}

	@Test
	public void testCreateResourceInTransaction() throws IOException {
		String methodName = "testCreateResourceInTransaction";

		Patient pt = new Patient();
		pt.addName().addFamily(methodName);

		Bundle bundle = new Bundle();
		bundle.setType(BundleTypeEnum.TRANSACTION);
		Entry entry = bundle.addEntry();
		entry.setFullUrl("Patient");
		entry.setResource(pt);
		entry.getRequest().setMethod(HTTPVerbEnum.POST);
		entry.getRequest().setUrl("Patient");

		String resource = myFhirCtx.newXmlParser().encodeResourceToString(bundle);

		HttpPost post = new HttpPost(ourServerBase + "/");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
		}

		/*
		 * Server Interceptor 
		 */

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(2)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals("Had types: " + opTypeCaptor.getAllValues() + " and requests: " + ardCaptor.getAllValues(), RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));
		assertNull(ardCaptor.getAllValues().get(0).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(0).getResource());
		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getAllValues().get(1));
		assertEquals("Patient", ardCaptor.getAllValues().get(1).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(1).getResource());

		ArgumentCaptor<RequestDetails> rdCaptor = ArgumentCaptor.forClass(RequestDetails.class);
		ArgumentCaptor<HttpServletRequest> srCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
		ArgumentCaptor<HttpServletResponse> sRespCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
		verify(myServerInterceptor, times(1)).incomingRequestPostProcessed(rdCaptor.capture(), srCaptor.capture(), sRespCaptor.capture());

		verify(myJpaServerInterceptor, times(1)).resourceCreated(ArgumentCaptor.forClass(RequestDetails.class).capture(), ArgumentCaptor.forClass(IBaseResource.class).capture());
		
		/*
		 * DAO Interceptor 
		 */

		ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myDaoInterceptor, atLeast(2)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals("Had types: " + opTypeCaptor.getAllValues() + " and requests: " + ardCaptor.getAllValues(), RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));
		assertEquals("Bundle", ardCaptor.getAllValues().get(0).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(0).getResource());
		assertEquals("Had types: " + opTypeCaptor.getAllValues() + " and requests: " + ardCaptor.getAllValues(), RestOperationTypeEnum.CREATE, opTypeCaptor.getAllValues().get(1));
		assertEquals("Patient", ardCaptor.getAllValues().get(1).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(1).getResource());

		rdCaptor = ArgumentCaptor.forClass(RequestDetails.class);
		srCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
		sRespCaptor = ArgumentCaptor.forClass(HttpServletResponse.class);
		verify(myDaoInterceptor, times(0)).incomingRequestPostProcessed(rdCaptor.capture(), srCaptor.capture(), sRespCaptor.capture());

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
