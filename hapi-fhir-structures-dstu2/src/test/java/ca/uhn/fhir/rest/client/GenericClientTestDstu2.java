package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class GenericClientTestDstu2 {
	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;


	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2();
	}

	
	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	
	@Test
	public void testSearchByString() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Bundle response = client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("james"))
				.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient?name=james", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntries().get(0).getResource().getClass());

	}

	@Test
	public void testDeleteConditional() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
//		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_TEXT + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		
		client.delete().resourceById(new IdDt("Patient/123")).execute();
		assertEquals("DELETE", capt.getAllValues().get(idx).getMethod());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(idx).getURI().toString());
		idx++;
		
		client.delete().resourceConditionalByUrl("Patient?name=foo").execute();
		assertEquals("DELETE", capt.getAllValues().get(idx).getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.delete().resourceConditionalByType("Patient").where(Patient.NAME.matches().value("foo")).execute();
		assertEquals("DELETE", capt.getAllValues().get(idx).getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	@Test
	public void testCreateConditional() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		
		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");
		
		client.create().resource(p).conditionalByUrl("Patient?name=foo").execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		client.create().resource(p).conditional().where(Patient.NAME.matches().value("foo")).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_IF_NONE_EXIST).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

	}


	@Test
	public void testUpdateConditional() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(""), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		
		Patient p = new Patient();
		p.addName().addFamily("FOOFAMILY");
		
		client.update().resource(p).conditionalByUrl("Patient?name=foo").execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditionalByUrl("Patient?name=foo").execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.update().resource(p).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo&address=AAA%5C%7CBBB", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		client.update().resource(ourCtx.newXmlParser().encodeResourceToString(p)).conditional().where(Patient.NAME.matches().value("foo")).and(Patient.ADDRESS.matches().value("AAA|BBB")).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("http://example.com/fhir/Patient?name=foo&address=AAA%5C%7CBBB", capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), "UTF-8");
		return body;
	}

}
