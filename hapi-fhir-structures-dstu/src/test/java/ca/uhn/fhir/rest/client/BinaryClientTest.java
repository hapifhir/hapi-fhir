package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.server.Constants;

public class BinaryClientTest {

	private FhirContext ctx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		ctx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);

		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testRead() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "foo/bar"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ByteArrayInputStream(new byte[] {1,2,3,4}));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		Binary resp = client.read(new IdDt("http://foo/Patient/123"));

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Binary/123", get.getURI().toString());
		
		assertEquals("foo/bar", resp.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, resp.getContent());
	}

	public static void main(String[] args) {
		
		IClient c = Mockito.mock(IClient.class, new ReturnsDeepStubs());
		
	}
	
	
	@Test
	public void testCreate() throws Exception {
		Binary res = new Binary();
		res.setContent(new byte[] { 1, 2, 3, 4 });
		res.setContentType("text/plain");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 201, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML));
		when(httpResponse.getEntity().getContent()).thenReturn(new ByteArrayInputStream(new byte[] {}));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		MethodOutcome resp = client.create(res);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo/Binary", post.getURI().toString());
		
		assertEquals("text/plain", post.getEntity().getContentType().getValue());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, IOUtils.toByteArray(post.getEntity().getContent()));

	}


	private String createBundle() {
		return ctx.newXmlParser().encodeBundleToString(new Bundle());
	}


	private interface IClient extends IBasicClient {

		@Read(type=Binary.class)
		public Binary read(@IdParam IdDt theBinary);

		@Create(type=Binary.class)
		public MethodOutcome create(@ResourceParam Binary theBinary);

	}

}
