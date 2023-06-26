package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.Extension;
import org.hl7.fhir.dstu2016may.model.Location;
import org.hl7.fhir.dstu2016may.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchClientDstu2_1Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchClientDstu2_1Test.class);
	private FhirContext ourCtx;
	private HttpClient ourHttpClient;
	private HttpResponse ourHttpResponse;

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeEach
	public void before() {
		ourCtx = FhirContext.forDstu2_1();

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
	 * See #371
	 */
	@Test
	public void testSortForDstu3() throws Exception {

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

		ILocationClient client = ourCtx.newRestfulClient(ILocationClient.class, "http://localhost/fhir");

		int idx = 0;

		client.search(new SortSpec("param1", SortOrderEnum.ASC));
		assertEquals("http://localhost/fhir/Bundle?_sort=param1", ((HttpGet) capt.getAllValues().get(idx++)).getURI().toString());

		client.search(new SortSpec("param1", SortOrderEnum.ASC).setChain(new SortSpec("param2", SortOrderEnum.DESC)));
		assertEquals("http://localhost/fhir/Bundle?_sort=param1%2C-param2", ((HttpGet) capt.getAllValues().get(idx++)).getURI().toString());
	}

	@Test
	public void testSearchWithPrimitiveTypes() throws Exception {
		TimeZone tz = TimeZone.getDefault();
		try {
			TimeZone.setDefault(TimeZone.getTimeZone("America/Toronto"));

			Date date = new Date(23898235986L);
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			;

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

			ILocationClient client = ourCtx.newRestfulClient(ILocationClient.class, "http://localhost/fhir");

			int idx = 0;

			client.search("STRING1", new StringType("STRING2"), date, cal);
			assertEquals("http://localhost/fhir/Bundle?stringParam=STRING1&stringTypeParam=STRING2&dateParam=1970-10-04T10:23:55.986-04:00&calParam=1970-10-04T10:23:55.986-04:00",
					UrlUtil.unescape(((HttpGet) capt.getAllValues().get(idx++)).getURI().toString()));

			client.search(null, null, null, null);
			assertEquals("http://localhost/fhir/Bundle",
					UrlUtil.unescape(((HttpGet) capt.getAllValues().get(idx++)).getURI().toString()));
		} finally {
			TimeZone.setDefault(tz);
		}
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
		assertEquals("Sample Clinic", ((Location) entry.getResource()).getName());

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

		@Search(queryName = "match", type = Location.class)
		public Bundle getMatchesReturnBundle(final @RequiredParam(name = Location.SP_NAME) StringParam name, final @Count Integer count);

		@Search
		public Bundle search(@Sort SortSpec theSort);

		@Search
		public Bundle search(@OptionalParam(name = "stringParam") String theString, @OptionalParam(name = "stringTypeParam") StringType theStringDt, @OptionalParam(name = "dateParam") Date theDate,
				@OptionalParam(name = "calParam") Calendar theCal);

	}

}
