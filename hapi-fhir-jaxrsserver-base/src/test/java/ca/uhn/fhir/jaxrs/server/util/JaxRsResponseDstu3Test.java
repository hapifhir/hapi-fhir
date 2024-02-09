package ca.uhn.fhir.jaxrs.server.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import jakarta.ws.rs.core.Response;

import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class JaxRsResponseDstu3Test {
	
	private JaxRsResponse response;
	private JaxRsRequest request;
	private Bundle bundle;
	private Set<SummaryEnum> theSummaryMode;

	@BeforeEach
	public void setUp() throws URISyntaxException {
		request = new JaxRsRequestDstu3Test().createRequestDetails();
		this.response = (JaxRsResponse) request.getResponse();
		bundle = getSinglePatientResource();
		theSummaryMode = Collections.<SummaryEnum>emptySet();
	}
	
	@Test
	public void testGetResponseWriterNoZipNoBrowser() throws IOException {
		boolean theRequestIsBrowser = false;
		boolean respondGzip = false;
		Set<SummaryEnum> theSummaryMode = Collections.<SummaryEnum>emptySet();
        boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), bundle, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, request);
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo(Constants.CT_FHIR_JSON_NEW + Constants.CHARSET_UTF8_CTSUFFIX);
		assertThat(result.getEntity().toString()).contains("Patient");
		assertThat(result.getEntity().toString()).contains("15");
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
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo(contentType);
		assertThat((byte[]) result.getEntity()).containsExactly(content);
	}
	
	@Test
	public void testSendAttachmentResponseNoContent() throws IOException {
		boolean theRequestIsBrowser = true;
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		binary.setContent(new byte[]{});
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo(null);
		assertThat(result.getEntity()).isEqualTo(null);
	}
	
	@Test
	public void testSendAttachmentResponseEmptyContent() throws IOException {
		boolean theRequestIsBrowser = true;
		boolean respondGzip = true;
		IBaseBinary binary = new Binary();
		boolean theAddContentLocationHeader = false;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), binary, theSummaryMode, 200, theAddContentLocationHeader, respondGzip, this.request);
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getEntity().toString()).isEqualTo("{\n" +
			"  \"resourceType\": \"Binary\"\n" +
			"}");
	}

	@Test
	public void testReturnResponse() throws IOException {
		IdType theId = new IdType(15L);
		int operationStatus = 200;
		boolean allowPrefer = true;
		String resourceName = "Patient";
		MethodOutcome methodOutcome = new MethodOutcome(theId);
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), createPatient(), theSummaryMode, 200, addContentLocationHeader, respondGzip, this.request);
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo(Constants.CT_FHIR_JSON_NEW + "; charset=UTF-8");
		assertThat(result.getEntity().toString()).contains("resourceType\": \"Patient");
		assertThat(result.getEntity().toString()).contains("15");
		
	}
	
	@Test
	public void testReturnResponseAsXml() throws IOException {
		IdType theId = new IdType(15L);
		int operationStatus = 200;
		boolean allowPrefer = true;
		String resourceName = "Patient";
		MethodOutcome methodOutcome = new MethodOutcome(theId);
		response.getRequestDetails().addParameter(Constants.PARAM_FORMAT, new String[]{Constants.CT_XML});
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), createPatient(), theSummaryMode, 200, addContentLocationHeader, respondGzip, this.request);
		assertThat(result.getStatus()).isEqualTo(200);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo("application/fhir+xml; charset=UTF-8");
		assertThat(result.getEntity().toString()).contains("<Patient");
		assertThat(result.getEntity().toString()).contains("15");
	}
	
	@Test
	public void testNoOutcomeXml() throws IOException {
		response.getRequestDetails().addParameter(Constants.PARAM_FORMAT, new String[]{Constants.CT_XML});
		boolean addContentLocationHeader = true;
		boolean respondGzip = true;
		Response result = (Response) RestfulServerUtils.streamResponseAsResource(request.getServer(), null, theSummaryMode, 204, addContentLocationHeader, respondGzip, this.request);
		assertThat(result.getStatus()).isEqualTo(204);
		assertThat(result.getHeaderString(Constants.HEADER_CONTENT_TYPE)).isEqualTo(null);
	}

	private Bundle getSinglePatientResource() {
		Patient theResource = createPatient();
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(theResource);
		return bundle;
	}

	private Patient createPatient() {
		Patient theResource = new Patient();
		theResource.setId(new IdType(15L));
		return theResource;
	}	
	
}
