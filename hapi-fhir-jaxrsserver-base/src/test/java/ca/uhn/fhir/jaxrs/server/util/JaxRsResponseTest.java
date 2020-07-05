package ca.uhn.fhir.jaxrs.server.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class JaxRsResponseTest {

	private JaxRsResponse response;
	private JaxRsRequest request;
	private Set<SummaryEnum> theSummaryMode;

	@BeforeEach
	public void setUp() throws URISyntaxException {
		request = new JaxRsRequestTest().createRequestDetails();
		this.response = (JaxRsResponse) request.getResponse();
		theSummaryMode = Collections.<SummaryEnum> emptySet();
	}

	@Test
	public void testSendAttachmentResponse() throws IOException {
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
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		binary.setContent(new byte[] {});
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals(null, result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertEquals(null, result.getEntity());
	}

	@Test
	public void testSendAttachmentResponseEmptyContent() throws IOException {
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
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		// Response result = response.returnResponse(outcome, operationStatus, allowPrefer, methodOutcome, resourceName);
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), createPatient(), theSummaryMode, 200, addContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals("application/json+fhir; charset=UTF-8", result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		System.out.println(result.getEntity().toString());
		assertTrue(result.getEntity().toString().contains("resourceType\": \"Patient"));
		assertTrue(result.getEntity().toString().contains("15"));

	}

	@Test
	public void testReturnResponseAsXml() throws IOException {
		response.getRequestDetails().addParameter(Constants.PARAM_FORMAT, new String[] { Constants.CT_XML });
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), createPatient(), theSummaryMode, 200, addContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals("application/xml+fhir; charset=UTF-8", result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
		assertTrue(result.getEntity().toString().contains("<Patient"));
		assertTrue(result.getEntity().toString().contains("15"));
	}

	@Test
	public void testNoOutcomeXml() throws IOException {
		response.getRequestDetails().addParameter(Constants.PARAM_FORMAT, new String[] { Constants.CT_XML });
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), createPatient(), theSummaryMode, 200, addContentLocationHeader, respondGzip, this.request);
		assertEquals(200, result.getStatus());
		assertEquals("application/xml+fhir; charset=UTF-8", result.getHeaderString(Constants.HEADER_CONTENT_TYPE));
	}

	@Test
	public void addMultipleHeaderValues() throws IOException {
		response.addHeader("Authorization", "Basic");
		response.addHeader("Authorization", "Bearer");
		response.addHeader("Cache-Control", "no-cache, no-store");

		final IBaseBinary binary = new Binary();
		binary.setContentType("abc");
		binary.setContent(new byte[] { 1 });
		final Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, false, false, this.request);

		assertThat(result.getHeaders().get("Authorization"), Matchers.contains("Basic", "Bearer"));
		assertThat(result.getHeaders().get("Cache-Control"), Matchers.contains("no-cache, no-store"));
	}

	private Patient createPatient() {
		Patient theResource = new Patient();
		theResource.setId(new IdDt(15L));
		return theResource;
	}
}
