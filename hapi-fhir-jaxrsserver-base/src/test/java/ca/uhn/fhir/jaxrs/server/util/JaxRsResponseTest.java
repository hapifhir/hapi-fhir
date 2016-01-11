package ca.uhn.fhir.jaxrs.server.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.ParseAction;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class JaxRsResponseTest {
	
	private JaxRsResponse response;
	private JaxRsRequest request;
	private Bundle bundle;
	private Set<SummaryEnum> theSummaryMode;

	@Before
	public void setUp() throws URISyntaxException {
		request = new JaxRsRequestTest().createRequestDetails();
		this.response = (JaxRsResponse) request.getResponse();
		bundle = getSinglePatientResource();
		theSummaryMode = Collections.<SummaryEnum>emptySet();
	}
	
	@Test
	public void testGetResponseWriterNoZipNoBrowser() throws IOException {
		boolean theRequestIsBrowser = false;
		boolean respondGzip = false;
		Set<SummaryEnum> theSummaryMode = Collections.<SummaryEnum>emptySet();
		Response result = (Response) RestfulServerUtils.streamResponseAsBundle(request.getServer(), bundle, theSummaryMode, respondGzip, request);
		assertEquals(200, result.getStatus());
		assertEquals(Constants.CT_FHIR_JSON+Constants.CHARSET_UTF8_CTSUFFIX, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertTrue(result.getEntity().toString().contains("Patient"));
		assertTrue(result.getEntity().toString().contains("15"));
	}

	@Test
	public void testSendAttachmentResponse() throws IOException {
		boolean theRequestIsBrowser = true;
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		String contentType = "foo";
		byte[] content = new byte[] { 1, 2, 3, 4 };
		binary.setContentType(contentType);
		binary.setContent(content);
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals(contentType, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertEquals(content, result.getEntity());
	}
	
	@Test
	public void testSendAttachmentResponseNoContent() throws IOException {
		boolean theRequestIsBrowser = true;
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		binary.setContent(new byte[]{});
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals(null, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertEquals(null, result.getEntity());
	}
	
	@Test
	public void testSendAttachmentResponseEmptyContent() throws IOException {
		boolean theRequestIsBrowser = true;
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals(null, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertEquals(null, result.getEntity());
	}
	

	@Test
	public void testReturnResponse() throws IOException {
		IdDt theId = new IdDt(15L);
		ParseAction<?> outcome = ParseAction.create(createPatient());
		int operationStatus = 200;
		boolean allowPrefer = true;
		String resourceName = "Patient";
		MethodOutcome methodOutcome = new MethodOutcome(theId);
		Response result = response.returnResponse(outcome, operationStatus, allowPrefer, methodOutcome, resourceName);
		assertEquals(200, result.getStatus());
		assertEquals(Constants.CT_JSON+Constants.CHARSET_UTF8_CTSUFFIX, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		System.out.println(result.getEntity().toString());
		assertTrue(result.getEntity().toString().contains("resourceType\":\"Patient"));
		assertTrue(result.getEntity().toString().contains("15"));
		
	}
	
	@Test
	public void testReturnResponseAsXml() throws IOException {
		IdDt theId = new IdDt(15L);
		ParseAction<?> outcome = ParseAction.create(createPatient());
		int operationStatus = 200;
		boolean allowPrefer = true;
		String resourceName = "Patient";
		MethodOutcome methodOutcome = new MethodOutcome(theId);
		response.getRequestDetails().getParameters().put(Constants.PARAM_FORMAT, new String[]{Constants.CT_XML});
		Response result = response.returnResponse(outcome, operationStatus, allowPrefer, methodOutcome, resourceName);
		assertEquals(200, result.getStatus());
		assertEquals(Constants.CT_XML+Constants.CHARSET_UTF8_CTSUFFIX, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertTrue(result.getEntity().toString().contains("<Patient"));
		assertTrue(result.getEntity().toString().contains("15"));
	}
	
	@Test
	public void testNoOutcomeXml() throws IOException {
		ParseAction<?> outcome = ParseAction.create((IBaseResource) null);
		int operationStatus = Constants.STATUS_HTTP_204_NO_CONTENT;
		boolean allowPrefer = true;
		String resourceName = "Patient";
		MethodOutcome methodOutcome = new MethodOutcome(null);
		response.getRequestDetails().getParameters().put(Constants.PARAM_FORMAT, new String[]{Constants.CT_XML});
		Response result = response.returnResponse(outcome, operationStatus, allowPrefer, methodOutcome, resourceName);
		assertEquals(204, result.getStatus());
		assertEquals(Constants.CT_XML+Constants.CHARSET_UTF8_CTSUFFIX, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
	}

	private Bundle getSinglePatientResource() {
		Patient theResource = createPatient();
		Bundle bundle = Bundle.withSingleResource(theResource);
		return bundle;
	}

	private Patient createPatient() {
		Patient theResource = new Patient();
		theResource.setId(new IdDt(15L));
		return theResource;
	}	
	
}
