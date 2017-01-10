package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Bundle.HTTPVerb;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderInterceptorDstu3Test extends BaseResourceProviderDstu3Test {

	private IServerInterceptor myServerInterceptor;
	private IServerInterceptor myDaoInterceptor;

	@Override
	@After
	public void after() throws Exception {
		super.after();

		myDaoConfig.getInterceptors().remove(myDaoInterceptor);
		ourRestServer.unregisterInterceptor(myServerInterceptor);
	}

	@Override
	public void before() throws Exception {
		super.before();
		
		myServerInterceptor = mock(IServerInterceptor.class);
		myDaoInterceptor = mock(IServerInterceptor.class);

		when(myServerInterceptor.handleException(any(RequestDetails.class), any(BaseServerResponseException.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPostProcessed(any(RequestDetails.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.incomingRequestPreProcessed(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class))).thenReturn(true);
		when(myServerInterceptor.outgoingResponse(any(RequestDetails.class), any(IBaseResource.class), any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(true);
		
		myDaoConfig.getInterceptors().add(myDaoInterceptor);
		ourRestServer.registerInterceptor(myServerInterceptor);
		
//		ourRestServer.registerInterceptor(new InterceptorAdapter() {
//			@Override
//			public void incomingRequestPreHandled(RestOperationTypeEnum theOperation, ActionRequestDetails theProcessedRequest) {
//				super.incomingRequestPreHandled(theOperation, theProcessedRequest);
//			}});
		
	}
	
	
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderInterceptorDstu3Test.class);

	@Test
	public void testCreateResource() throws IOException {
		String methodName = "testCreateResource";

		Patient pt = new Patient();
		pt.addName().setFamily(methodName);
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(pt);

		HttpPost post = new HttpPost(ourServerBase + "/Patient");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was: {}", resp);
			assertEquals(201, response.getStatusLine().getStatusCode());
			String newIdString = response.getFirstHeader(Constants.HEADER_LOCATION_LC).getValue();
			assertThat(newIdString, startsWith(ourServerBase + "/Patient/"));
		} finally {
			response.close();
		}

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getValue());
		assertEquals("Patient", ardCaptor.getValue().getResourceType());
		assertNotNull(ardCaptor.getValue().getResource());
		
		ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myDaoInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
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
		
		String resource = myFhirCtx.newXmlParser().encodeResourceToString(bundle);

		HttpPost post = new HttpPost(ourServerBase + "/");
		post.setEntity(new StringEntity(resource, ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse response = ourHttpClient.execute(post);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
		}

		ArgumentCaptor<ActionRequestDetails> ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		ArgumentCaptor<RestOperationTypeEnum> opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myServerInterceptor, times(1)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getValue());
		assertNotNull(ardCaptor.getValue().getResource());
		
		ardCaptor = ArgumentCaptor.forClass(ActionRequestDetails.class);
		opTypeCaptor = ArgumentCaptor.forClass(RestOperationTypeEnum.class);
		verify(myDaoInterceptor, times(2)).incomingRequestPreHandled(opTypeCaptor.capture(), ardCaptor.capture());
		assertEquals(RestOperationTypeEnum.TRANSACTION, opTypeCaptor.getAllValues().get(0));
		assertEquals("Bundle", ardCaptor.getAllValues().get(0).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(0).getResource());
		assertEquals(RestOperationTypeEnum.CREATE, opTypeCaptor.getAllValues().get(1));
		assertEquals("Patient", ardCaptor.getAllValues().get(1).getResourceType());
		assertNotNull(ardCaptor.getAllValues().get(1).getResource());
	}

	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
