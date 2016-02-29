package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
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
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.Constants;


public class SearchClientDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchClientDstu3Test.class);
	private FhirContext ourCtx;
	private HttpClient ourHttpClient;
	private HttpResponse ourHttpResponse;

	@Before
	public void before() {
		ourCtx = FhirContext.forDstu3();

		ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	/**
	 * See #299
	 */
	@Test
	public void testListResponseWithSearchExtension() throws Exception {

		final String response = createBundleWithSearchExtension();
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(response), Charset.forName("UTF-8"));
			}
		});

		ILocationClient client = ourCtx.newRestfulClient(ILocationClient.class, "http://localhost:8081/hapi-fhir/fhir");

		List<Location> matches = client.getMatches(new StringParam("smith"), 100);
		assertEquals(1, matches.size());
		assertEquals("Sample Clinic", matches.get(0).getName());

		HttpGet value = (HttpGet) capt.getValue();
		assertEquals("http://localhost:8081/hapi-fhir/fhir/Location?_query=match&name=smith&_count=100", value.getURI().toString());
	}

	/**
	 * See #299
	 */
	@Test
	public void testBundleResponseWithSearchExtension() throws Exception {

		final String response = createBundleWithSearchExtension();
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(response), Charset.forName("UTF-8"));
			}
		});

		ILocationClient client = ourCtx.newRestfulClient(ILocationClient.class, "http://localhost:8081/hapi-fhir/fhir");

		Bundle matches = client.getMatchesReturnBundle(new StringParam("smith"), 100);
		
		assertEquals(1, matches.getEntry().size());
		BundleEntryComponent entry = matches.getEntry().get(0);
		assertEquals("Sample Clinic", ((Location)entry.getResource()).getName());

		List<Extension> ext = entry.getSearch().getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/algorithmic-match");
		assertEquals(1, ext.size());
		
		HttpGet value = (HttpGet) capt.getValue();
		assertEquals("http://localhost:8081/hapi-fhir/fhir/Location?_query=match&name=smith&_count=100", value.getURI().toString());
	}

	private String createBundleWithSearchExtension() {
		//@formatter:off
		final String response = "<Bundle xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"f61f6ddc-95e8-4ef9-a4cd-17c79bbb74f3\"></id>"
				+ "<meta><lastUpdated value=\"2016-02-19T12:04:02.616-05:00\"></lastUpdated></meta>"
				+ "<type value=\"searchset\"></type>"
				+ "<link><relation value=\"self\"></relation><url value=\"http://localhost:8081/hapi-fhir/fhir/Location?name=Sample+Clinic&amp;_query=match\"></url></link>"
				+ "<entry>"
				+ "<resource>"
				+ "<Location xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"1\"></id>"
				+ "<name value=\"Sample Clinic\"></name>"
				+ "</Location>"
				+ "</resource>"
				+ "<search>"
				+ "<extension url=\"http://hl7.org/fhir/StructureDefinition/algorithmic-match\">"
				+ "<valueCode value=\"probable\"></valueCode>"
				+ "</extension>"
				+ "<score value=\"0.8000000000000000444089209850062616169452667236328125\">"
				+ "</score>"
				+ "</search>"
				+ "</entry>"
				+ "</Bundle>";
		//@formatter:on
		return response;
	}

	
	
	public interface ILocationClient extends IRestfulClient {
		@Search(queryName = "match")
		public List<Location> getMatches(final @RequiredParam(name = Location.SP_NAME) StringParam name, final @Count Integer count);

		@Search(queryName = "match", type=Location.class)
		public Bundle getMatchesReturnBundle(final @RequiredParam(name = Location.SP_NAME) StringParam name, final @Count Integer count);
	}

}
