package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.TestUtil;

public class SortClientTest {

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
	public void testSort() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.searchWithParam(new StringParam("hello"), new SortSpec("given"));

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient?name=hello&_sort=given", get.getURI().toString());
	}

	@Test
	public void testSortWithChain() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		client.searchWithParam(new StringParam("hello"), new SortSpec("given", SortOrderEnum.DESC, new SortSpec("family", SortOrderEnum.ASC)));

		assertEquals(HttpGet.class, capt.getValue().getClass());
		HttpGet get = (HttpGet) capt.getValue();
		assertEquals("http://foo/Patient?name=hello&_sort%3Adesc=given&_sort%3Aasc=family", get.getURI().toString());
	}

	private String createBundle() {
		return ctx.newXmlParser().encodeBundleToString(new Bundle());
	}


	private interface IClient extends IBasicClient {

		@Search(type=Patient.class)
		public List<Patient> searchWithParam(@RequiredParam(name=Patient.SP_NAME) StringParam theString, @Sort SortSpec theSort);

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
