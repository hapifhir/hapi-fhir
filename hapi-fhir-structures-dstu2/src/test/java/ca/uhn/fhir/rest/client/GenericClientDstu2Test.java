package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestSecurity;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.XmlParserDstu2Test;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu2Test.class);
	private static FhirContext ourCtx;
	private HttpClient myHttpClient;

	private HttpResponse myHttpResponse;

	private int myResponseCount = 0;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.setRestfulClientFactory(new ApacheRestfulClientFactory(ourCtx));
		ourCtx.getRestfulClientFactory().setConnectionRequestTimeout(10000);
		ourCtx.getRestfulClientFactory().setConnectTimeout(10000);
		ourCtx.getRestfulClientFactory().setPoolMaxPerRoute(100);
		ourCtx.getRestfulClientFactory().setPoolMaxTotal(100);

		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
		myResponseCount = 0;

		System.setProperty(BaseClient.HAPI_CLIENT_KEEPRESPONSES, "true");
	}

	private String extractBody(ArgumentCaptor<HttpUriRequest> capt, int count) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(count)).getEntity().getContent(), "UTF-8");
		return body;
	}

	private String getPatientFeedWithOneResult() {
		//@formatter:off
		String msg = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"<id>d039f91a-cc3c-4013-988e-af4d8d0614bd</id>\n" +
			"<entry>\n" +
			"<resource>"
			+ "<Patient>"
			+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
			+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
			+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
			+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
			+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
			+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
			+ "</Patient>"
			+ "</resource>\n"
			+ "   </entry>\n"
			+ "</Bundle>";
		//@formatter:on
		return msg;
	}

	@Test
	public void testAcceptHeaderFetchConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals("http://example.com/fhir/metadata", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(idx).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(idx).getHeaders("Accept")[0].getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));
		idx++;

		client.fetchConformance().ofType(Conformance.class).encodedJson().execute();
		assertEquals("http://example.com/fhir/metadata?_format=json", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(idx).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(idx).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_JSON));
		idx++;

		client.fetchConformance().ofType(Conformance.class).encodedXml().execute();
		assertEquals("http://example.com/fhir/metadata?_format=xml", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(idx).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(idx).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_XML));
		idx++;
	}

	@Test
	public void testAcceptHeaderPreflightConformance() throws Exception {
		String methodName = "testAcceptHeaderPreflightConformance";
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				if (myResponseCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(conf)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(patient)), Charset.forName("UTF-8"));
				}
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://" + methodName + ".example.com/fhir");

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://" + methodName + ".example.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(0).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_XML));
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_JSON));
		assertEquals("http://" + methodName + ".example.com/fhir/Patient/123", capt.getAllValues().get(1).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(1).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(1).getHeaders("Accept")[0].getValue(), containsString(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY));
		assertThat(capt.getAllValues().get(1).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_XML));
		assertThat(capt.getAllValues().get(1).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_JSON));
	}

	@Test
	public void testAcceptHeaderPreflightConformancePreferJson() throws Exception {
		String methodName = "testAcceptHeaderPreflightConformancePreferJson";
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				if (myResponseCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(conf)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(patient)), Charset.forName("UTF-8"));
				}
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://" + methodName + ".example.com/fhir");
		client.setEncoding(EncodingEnum.JSON);

		Patient resp = client.read(Patient.class, new IdDt("123"));
		assertEquals("FAMILY", resp.getName().get(0).getFamily().get(0).getValue());
		assertEquals("http://" + methodName + ".example.com/fhir/metadata?_format=json", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(0).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_JSON));
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue(), not(containsString(Constants.CT_FHIR_XML)));
		assertEquals("http://" + methodName + ".example.com/fhir/Patient/123?_format=json", capt.getAllValues().get(1).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(1).getHeaders("Accept").length);
		assertThat(capt.getAllValues().get(1).getHeaders("Accept")[0].getValue(), containsString(Constants.CT_FHIR_JSON));
		assertThat(capt.getAllValues().get(1).getHeaders("Accept")[0].getValue(), not(containsString(Constants.CT_FHIR_XML)));
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testConformance() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final String respString = p.encodeResourceToString(conf);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Conformance resp = (Conformance) client.fetchConformance().ofType(Conformance.class).execute();

		//@formatter:on
		assertEquals("http://example.com/fhir/metadata", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("COPY", resp.getCopyright());
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

	}

	@Test
	public void testCreate() throws Exception {
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

		client.create().resource(p).execute();

		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		p.setId("123");

		client.create().resource(p).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		String body = extractBody(capt, idx);
		assertThat(body, containsString("<family value=\"FOOFAMILY\"/>"));
		assertThat(body, not(containsString("123")));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
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

		client.create().resource(p).conditionalByUrl("Patient?name=http://foo|bar").execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("http://example.com/fhir/Patient?name=http%3A//foo%7Cbar", capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_IF_NONE_EXIST).getValue());
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

	@SuppressWarnings("deprecation")
	@Test
	public void testCreateNonFluent() throws Exception {
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

		client.create().resource(p).execute();

		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;
	}

	@Test
	public void testCreatePrefer() throws Exception {
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

		client.create().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER).length);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER)[0].getValue());
		idx++;

		client.create().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER).length);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER)[0].getValue());
		idx++;

	}

	@Test
	public void testCreateReturningResourceBody() throws Exception {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_200_OK, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(formatted), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.create().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testDeleteByResource() throws Exception {
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

		Patient pat = new Patient();
		pat.setId("Patient/123");

		client.delete().resource(pat).execute();
		assertEquals("DELETE", capt.getAllValues().get(idx).getMethod());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(idx).getURI().toString());
	}

	@Test
	public void testDeleteConditional() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_204_NO_CONTENT, ""));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type",
		// Constants.CT_TEXT + "; charset=UTF-8"));
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
	public void testDeleteInvalidRequest() throws Exception {
		Patient pat = new Patient();
		pat.setId("123");

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.delete().resource(pat).execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theResource.getId() must contain a resource type and logical ID at a minimum (e.g. Patient/1234), found: 123", e.getMessage());
		}

		try {
			client.delete().resourceById(new IdDt("123")).execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theId must contain a resource type and logical ID at a minimum (e.g. Patient/1234)found: 123", e.getMessage());
		}

		try {
			client.delete().resourceById("", "123").execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theResourceType can not be blank/null", e.getMessage());
		}

		try {
			client.delete().resourceById("Patient", "").execute();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("theLogicalId can not be blank/null", e.getMessage());
		}

		try {
			client.delete().resourceConditionalByType("InvalidType");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"InvalidType\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}
	}

	/**
	 * See #322
	 */
	@Test
	public void testFetchConformanceWithSmartExtensions() throws Exception {
		final String respString = IOUtils.toString(GenericClientDstu2Test.class.getResourceAsStream("/conformance_322.json"));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:8080/fhir");
		Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();

		Rest rest = conf.getRest().get(0);
		RestSecurity security = rest.getSecurity();

		List<ExtensionDt> ext = security.getUndeclaredExtensionsByUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		List<ExtensionDt> tokenExts = ext.get(0).getUndeclaredExtensionsByUrl("token");
		ExtensionDt tokenExt = tokenExts.get(0);
		UriDt value = (UriDt) tokenExt.getValue();
		assertEquals("https://my-server.org/token", value.getValueAsString());

	}

	/**
	 * See #322
	 */
	@Test
	public void testFetchConformanceWithSmartExtensionsAltCase() throws Exception {
		final String respString = IOUtils.toString(GenericClientDstu2Test.class.getResourceAsStream("/conformance_322.json")).replace("valueuri", "valueUri");
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:8080/fhir");
		Conformance conf = client.fetchConformance().ofType(Conformance.class).execute();

		Rest rest = conf.getRest().get(0);
		RestSecurity security = rest.getSecurity();

		List<ExtensionDt> ext = security.getUndeclaredExtensionsByUrl("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");
		List<ExtensionDt> tokenExts = ext.get(0).getUndeclaredExtensionsByUrl("token");
		ExtensionDt tokenExt = tokenExts.get(0);
		UriDt value = (UriDt) tokenExt.getValue();
		assertEquals("https://my-server.org/token", value.getValueAsString());

	}

	@Test
	public void testHistory() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;
		ca.uhn.fhir.model.dstu2.resource.Bundle response;

		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since((Date) null)
			.count(null)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onServer()
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt())
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onType(Patient.class)
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/123/_history", capt.getAllValues().get(idx).getURI().toString());
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.count(123)
			.since(new InstantDt("2001-01-02T11:22:33Z"))
			.execute();
		//@formatter:on
		assertThat(capt.getAllValues().get(idx).getURI().toString(), either(equalTo("http://example.com/fhir/Patient/123/_history?_since=2001-01-02T11:22:33Z&_count=123"))
			.or(equalTo("http://example.com/fhir/Patient/123/_history?_count=123&_since=2001-01-02T11:22:33Z")));
		assertEquals(1, response.getEntry().size());
		idx++;

		//@formatter:off
		response = client
			.history()
			.onInstance(new IdDt("Patient", "123"))
			.andReturnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.since(new InstantDt("2001-01-02T11:22:33Z").getValue())
			.execute();
		//@formatter:on
		assertThat(capt.getAllValues().get(idx).getURI().toString(), containsString("_since=2001-01"));
		assertEquals(1, response.getEntry().size());
		idx++;
	}

	@Test
	public void testInvalidClient() {
		try {
			ourCtx.getRestfulClientFactory().newClient(RestfulClientInstance.class, "http://foo");
			fail();
		} catch (ConfigurationException e) {
			assertEquals("ca.uhn.fhir.context.ConfigurationException: ca.uhn.fhir.rest.client.GenericClientDstu2Test.RestfulClientInstance is not an interface", e.toString());
		}
	}

	@Test
	public void testMetaAdd() throws Exception {
		IParser p = ourCtx.newXmlParser();

		MetaDt inMeta = new MetaDt().addProfile("urn:profile:in");

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		MetaDt resp = client
			.meta()
			.add()
			.onResource(new IdDt("Patient/123"))
			.meta(inMeta)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$meta-add", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"meta\"/><valueMeta><profile value=\"urn:profile:in\"/></valueMeta></parameter></Parameters>",
			extractBody(capt, idx));
		idx++;

	}

	@Test
	public void testMetaGet() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:in"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setName("meta").setValue(new MetaDt().addProfile("urn:profile:out"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		MetaDt resp = client
			.meta()
			.get(MetaDt.class)
			.fromServer()
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$meta", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.meta()
			.get(MetaDt.class)
			.fromType("Patient")
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/$meta", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.meta()
			.get(MetaDt.class)
			.fromResource(new IdDt("Patient/123"))
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$meta", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("urn:profile:out", resp.getProfile().get(0).getValue());
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

	}

	@Test
	public void testOperationAsGetWithInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setName("param1").setValue(new StringDt("STRINGVALIN1b"));
		inParams.addParameter().setName("param2").setValue(new StringDt("STRINGVALIN2"));

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withParameters(inParams)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION?param1=STRINGVALIN1&param1=STRINGVALIN1b&param2=STRINGVALIN2", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;
	}

	@Test
	public void testOperationAsGetWithNoInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals("GET", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.useHttpGet()
			.execute();
		// @formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;
	}

	@Test
	public void testOperationWithBundleResponseJson() throws Exception {

		final String resp = "{\n" + "    \"resourceType\":\"Bundle\",\n" + "    \"id\":\"8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19\",\n" + "    \"base\":\"http://fhirtest.uhn.ca/baseDstu2\"\n" + "}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(resp), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");

		client.registerInterceptor(new LoggingInterceptor(true));

		// Create the input parameters to pass to the server
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("start").setValue(new DateDt("2001-01-01"));
		inParams.addParameter().setName("end").setValue(new DateDt("2015-03-01"));

		// Invoke $everything on "Patient/1"
		Parameters outParams = client.operation().onInstance(new IdDt("Patient", "18066")).named("$everything").withParameters(inParams).execute();

		/*
		 * Note that the $everything operation returns a Bundle instead of a Parameters resource. The client operation
		 * methods return a Parameters instance however, so HAPI creates a Parameters object
		 * with a single parameter containing the value.
		 */
		ca.uhn.fhir.model.dstu2.resource.Bundle responseBundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) outParams.getParameter().get(0).getResource();

		// Print the response bundle
		assertEquals("8cef5f2a-0ba9-43a5-be26-c8dde9ff0e19", responseBundle.getId().getIdPart());
	}

	@Test
	public void testOperationWithBundleResponseXml() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		ca.uhn.fhir.model.dstu2.resource.Bundle outParams = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		outParams.setTotal(123);
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals(1, resp.getParameter().size());
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Bundle.class, resp.getParameter().get(0).getResource().getClass());
		idx++;
	}

	@Test
	public void testOperationWithInlineParams() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new StringDt("value1"))
			.andParameter("name2", new StringDt("value1"))
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueString value=\"value1\"/></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>",
			(extractBody(capt, idx)));
		idx++;

		/*
		 * Composite type
		 */

		//@formatter:off
		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new IdentifierDt("system1", "value1"))
			.andParameter("name2", new StringDt("value1"))
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><valueString value=\"value1\"/></parameter></Parameters>",
			(extractBody(capt, idx)));
		idx++;

		/*
		 * Resource
		 */

		//@formatter:off
		resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", new IdentifierDt("system1", "value1"))
			.andParameter("name2", new Patient().setActive(true))
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"name1\"/><valueIdentifier><system value=\"system1\"/><value value=\"value1\"/></valueIdentifier></parameter><parameter><name value=\"name2\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><active value=\"true\"/></Patient></resource></parameter></Parameters>",
			(extractBody(capt, idx)));
		idx++;

	}

	@Test(expected = IllegalArgumentException.class)
	public void testOperationWithInvalidParam() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Who knows what the heck this is!
		IBase weirdBase = new IBase() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<String> getFormatCommentsPost() {
				return null;
			}

			@Override
			public List<String> getFormatCommentsPre() {
				return null;
			}

			@Override
			public boolean hasFormatComment() {
				return false;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}
		};

		//@formatter:off
		client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameter(Parameters.class, "name1", weirdBase)
			.execute();
		//@formatter:on
	}

	@Test
	public void testOperationWithListOfParameterResponse() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		inParams.addParameter().setValue(new StringDt("STRINGVALIN1"));
		inParams.addParameter().setValue(new StringDt("STRINGVALIN2"));
		String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withParameters(inParams).execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withParameters(inParams).execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withParameters(inParams).execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		resp = client.operation().onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22")).named("$SOMEOPERATION").withParameters(inParams).execute();
		// @formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;
	}

	@Test
	public void testOperationWithNoInParameters() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters inParams = new Parameters();
		final String reqString = p.encodeResourceToString(inParams);

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onType(Patient.class)
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		//@formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("Patient", "123"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class).execute();
		//@formatter:on		
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals(respString, p.encodeResourceToString(resp));
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertEquals(extractBody(capt, idx), reqString);
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		// @formatter:off
		resp = client
			.operation()
			.onInstance(new IdDt("http://foo.com/bar/baz/Patient/123/_history/22"))
			.named("$SOMEOPERATION")
			.withNoParameters(Parameters.class)
			.execute();
		// @formatter:on
		assertEquals("http://example.com/fhir/Patient/123/$SOMEOPERATION", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;
	}

	@Test
	public void testOperationWithProfiledDatatypeParam() throws IOException, Exception {
		IParser p = ourCtx.newXmlParser();

		Parameters outParams = new Parameters();
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT1"));
		outParams.addParameter().setValue(new StringDt("STRINGVALOUT2"));
		final String respString = p.encodeResourceToString(outParams);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		//@formatter:off
		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.useHttpGet()
			.execute();
		//@formatter:off

		assertEquals("http://example.com/fhir/Patient/1/$validate-code?code=8495-4&system=http%3A%2F%2Floinc.org", capt.getAllValues().get(idx).getURI().toASCIIString());

		//@formatter:off
		idx++;
		client
			.operation()
			.onInstance(new IdDt("http://foo/Patient/1"))
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("8495-4"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.execute();
		//@formatter:off

		assertEquals("http://example.com/fhir/Patient/1/$validate-code", capt.getAllValues().get(idx).getURI().toASCIIString());
		ourLog.info(extractBody(capt, idx));
		assertEquals("<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"code\"/><valueCode value=\"8495-4\"/></parameter><parameter><name value=\"system\"/><valueUri value=\"http://loinc.org\"/></parameter></Parameters>", extractBody(capt, idx));

	}

	@Test
	public void testPageNext() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(getPatientFeedWithOneResult()), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_PREV).setUrl("http://foo.bar/prev");
		sourceBundle.getLinkOrCreate(IBaseBundle.LINK_NEXT).setUrl("http://foo.bar/next");

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.next(sourceBundle)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://foo.bar/next", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;

	}

	@Test
	public void testPageNextNoLink() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		try {
			client.loadPage().next(sourceBundle).execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), containsString("Can not perform paging operation because no link was found in Bundle with relation \"next\""));
		}
	}

	@Test
	public void testPagePrev() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(getPatientFeedWithOneResult()), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		int idx = 0;

		ca.uhn.fhir.model.dstu2.resource.Bundle sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("previous").setUrl("http://foo.bar/prev");

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://foo.bar/prev", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;

		/*
		 * Try with "prev" instead of "previous"
		 */

		sourceBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		sourceBundle.getLinkOrCreate("prev").setUrl("http://foo.bar/prev");

		//@formatter:off
		resp = client
			.loadPage()
			.previous(sourceBundle)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals("http://foo.bar/prev", capt.getAllValues().get(idx).getURI().toASCIIString());
		idx++;

	}

	@Test
	public void testProviderWhereWeForgotToSetTheContext() throws Exception {
		ApacheRestfulClientFactory clientFactory = new ApacheRestfulClientFactory(); // no ctx
		clientFactory.setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourCtx.setRestfulClientFactory(clientFactory);

		try {
			ourCtx.newRestfulGenericClient("http://localhost:8080/fhir");
			fail();
		} catch (IllegalStateException e) {
			assertEquals("ApacheRestfulClientFactory does not have FhirContext defined. This must be set via ApacheRestfulClientFactory#setFhirContext(FhirContext)", e.getMessage());
		}
	}

	@Test
	public void testReadByUri() throws Exception {

		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String input = ourCtx.newXmlParser().encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT")});
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(input), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response;

		int idx = 0;
		response = (Patient) client.read(new UriDt("http://domain2.example.com/base/Patient/123"));
		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadFluentByUri() throws Exception {

		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String input = ourCtx.newXmlParser().encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT")});
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(input), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient response;

		int idx = 0;
		response = (Patient) client.read().resource(Patient.class).withUrl(new IdDt("http://domain2.example.com/base/Patient/123")).execute();
		assertEquals("http://domain2.example.com/base/Patient/123", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("FAM", response.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testReadForUnknownType() throws Exception {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read(new UriDt("1"));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("The given URI is not an absolute URL and is not usable for this operation: 1", e.getMessage());
		}

		try {
			client.read(new UriDt("http://example.com/InvalidResource/1"));
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource name \"InvalidResource\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}
	}

	@Test
	public void testReadUpdatedHeaderDoesntOverwriteResourceValue() throws Exception {

		//@formatter:off
		final String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"   <id value=\"e2ee823b-ee4d-472d-b79d-495c23f16b99\"/>\n" +
			"   <meta>\n" +
			"      <lastUpdated value=\"2015-06-22T15:48:57.554-04:00\"/>\n" +
			"   </meta>\n" +
			"   <type value=\"searchset\"/>\n" +
			"   <base value=\"http://localhost:58109/fhir/context\"/>\n" +
			"   <total value=\"0\"/>\n" +
			"   <link>\n" +
			"      <relation value=\"self\"/>\n" +
			"      <url value=\"http://localhost:58109/fhir/context/Patient?_pretty=true\"/>\n" +
			"   </link>\n" +
			"</Bundle>";
		//@formatter:on

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getAllHeaders()).thenReturn(new Header[]{new BasicHeader(Constants.HEADER_LAST_MODIFIED, "Sat, 20 Jun 2015 19:32:17 GMT")});
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(input), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle response;

		//@formatter:off
		response = client
			.search()
			.forResource(Patient.class)
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on

		assertEquals("2015-06-22T15:48:57.554-04:00", ResourceMetadataKeyEnum.UPDATED.get(response).getValueAsString());
	}

	@Test
	public void testReadWithElementsParam() throws Exception {
		String msg = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		IBaseResource response = client.read()
			.resource("Patient")
			.withId("123")
			.elementsSubset("name", "identifier")
			.execute();
		//@formatter:on

		assertThat(capt.getValue().getURI().toString(),
			either(equalTo("http://example.com/fhir/Patient/123?_elements=name%2Cidentifier")).or(equalTo("http://example.com/fhir/Patient/123?_elements=identifier%2Cname")));
		assertEquals(Patient.class, response.getClass());

	}

	@Test
	public void testReadWithSummaryInvalid() throws Exception {
		String msg = "<>>>><<<<>";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_HTML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		try {
			client.read()
				.resource(Patient.class)
				.withId("123")
				.summaryMode(SummaryEnum.TEXT)
				.execute();
			fail();
		} catch (InvalidResponseException e) {
			assertThat(e.getMessage(), containsString("String does not appear to be valid"));
		}
		//@formatter:on
	}

	@Test
	public void testReadWithSummaryParamHtml() throws Exception {
		String msg = "<div>HELP IM A DIV</div>";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_HTML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		Patient response = client.read()
			.resource(Patient.class)
			.withId("123")
			.summaryMode(SummaryEnum.TEXT)
			.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/123?_summary=text", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getClass());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELP IM A DIV</div>", response.getText().getDiv().getValueAsString());

	}

	@Test
	public void testSearchByNumber() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
		client.search()
			.forResource("Encounter")
			.where(Encounter.LENGTH.greaterThan().number(123))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Encounter?length=gt123", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Encounter")
			.where(Encounter.LENGTH.lessThan().number(123))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Encounter?length=lt123", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Encounter")
			.where(Encounter.LENGTH.greaterThanOrEqual().number("123"))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Encounter?length=ge123", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Encounter")
			.where(Encounter.LENGTH.lessThanOrEqual().number("123"))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Encounter?length=le123", capt.getAllValues().get(idx).getURI().toString());
		idx++;

		//@formatter:off
		client.search()
			.forResource("Encounter")
			.where(Encounter.LENGTH.exactly().number(123))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Encounter?length=123", capt.getAllValues().get(idx).getURI().toString());
		idx++;

	}

	@Test
	public void testSearchByPost() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient/_search?_elements=identifier%2Cname", capt.getValue().getURI().toString());

		// assertThat(capt.getValue().getURI().toString(),
		// either(equalTo("http://example.com/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo("http://example.com/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		ourLog.info(Arrays.asList(capt.getValue().getAllHeaders()).toString());
		ourLog.info(capt.getValue().toString());

		HttpEntityEnclosingRequestBase v = (HttpEntityEnclosingRequestBase) capt.getValue();
		String req = IOUtils.toString(v.getEntity().getContent(), "UTF-8");
		assertEquals("name=james", req);

		assertEquals("application/x-www-form-urlencoded;charset=utf-8", v.getEntity().getContentType().getValue().replace(" ", "").toLowerCase());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY, capt.getValue().getFirstHeader("accept").getValue());
		assertThat(capt.getValue().getFirstHeader("user-agent").getValue(), not(emptyString()));
	}

	@Test
	public void testSearchByPostUseJson() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.usingStyle(SearchStyleEnum.POST)
			.encodedJson()
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString(), containsString("http://example.com/fhir/Patient/_search?"));
		assertThat(capt.getValue().getURI().toString(), containsString("_elements=identifier%2Cname"));
		assertThat(capt.getValue().getURI().toString(), not(containsString("_format=json")));

		// assertThat(capt.getValue().getURI().toString(),
		// either(equalTo("http://example.com/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo("http://example.com/fhir/Patient?name=james&_elements=identifier%2Cname")));

		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

		ourLog.info(Arrays.asList(capt.getValue().getAllHeaders()).toString());
		ourLog.info(capt.getValue().toString());

		HttpEntityEnclosingRequestBase v = (HttpEntityEnclosingRequestBase) capt.getValue();
		String req = IOUtils.toString(v.getEntity().getContent(), "UTF-8");
		assertEquals("name=james", req);

		assertEquals("application/x-www-form-urlencoded;charset=utf-8", v.getEntity().getContentType().getValue().replace(" ", "").toLowerCase());
		assertEquals(Constants.CT_FHIR_JSON, capt.getValue().getFirstHeader("accept").getValue());
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

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=james", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchByUrl() throws Exception {

		final String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
			.byUrl("http://foo?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://foo?name=http%3A//foo%7Cbar&_format=json", capt.getAllValues().get(idx).getURI().toString());
		assertNotNull(response);
		idx++;

		//@formatter:off
		response = client.search()
			.byUrl("Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", capt.getAllValues().get(idx).getURI().toString());
		assertNotNull(response);
		idx++;

		//@formatter:off
		response = client.search()
			.byUrl("/Patient?name=http://foo|bar")
			.encodedJson()
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?name=http%3A//foo%7Cbar&_format=json", capt.getAllValues().get(idx).getURI().toString());
		assertNotNull(response);
		idx++;

		//@formatter:off
		response = client.search()
			.byUrl("Patient")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(idx).getURI().toString());
		assertNotNull(response);
		idx++;

		//@formatter:off
		response = client.search()
			.byUrl("Patient?")
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on
		assertEquals("http://example.com/fhir/Patient?", capt.getAllValues().get(idx).getURI().toString());
		assertNotNull(response);
		idx++;

		try {
			client.search().byUrl("foo/bar?test=1");
		} catch (IllegalArgumentException e) {
			assertEquals("Search URL must be either a complete URL starting with http: or https:, or a relative FHIR URL in the form [ResourceType]?[Params]", e.getMessage());
		}
	}

	/**
	 * See #191
	 */
	@Test
	public void testSearchReturningDstu2Bundle() throws Exception {
		String msg = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle_orion.xml"));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.search()
			.forResource("Observation")
			.where(Patient.NAME.matches().value("FOO"))
			.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
			.execute();
		//@formatter:on

		Link link = response.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		Entry entry = response.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
	}

	@Test
	public void testSearchWithElementsParam() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.elementsSubset("name", "identifier")
			.returnBundle(Bundle.class)
			.execute();

		assertThat(capt.getValue().getURI().toString(),
			either(equalTo("http://example.com/fhir/Patient?name=james&_elements=name%2Cidentifier")).or(equalTo("http://example.com/fhir/Patient?name=james&_elements=identifier%2Cname")));
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithLastUpdated() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.lastUpdated(new DateRangeParam("2011-01-01", "2012-01-01"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=james&_lastUpdated=ge2011-01-01&_lastUpdated=le2012-01-01", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithMap() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		HashMap<String, List<IQueryParameterType>> params = new HashMap<String, List<IQueryParameterType>>();
		params.put("foo", Arrays.asList((IQueryParameterType) new DateParam("2001")));
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.where(params)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?foo=2001", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testSearchWithProfileAndSecurity() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.withProfile("http://foo1")
			.withProfile("http://foo2")
			.withSecurity("system1", "code1")
			.withSecurity("system2", "code2")
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_security=system1%7Ccode1&_security=system2%7Ccode2&_profile=http%3A%2F%2Ffoo1&_profile=http%3A%2F%2Ffoo2", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithReverseInclude() throws Exception {

		String msg = getPatientFeedWithOneResult();

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource(Patient.class)
			.encodedJson()
			.revInclude(new Include("Provenance:target"))
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json", capt.getValue().getURI().toString());

	}

	@Test
	public void testSearchWithSummaryParam() throws Exception {
		String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle response = client.search()
			.forResource("Patient")
			.where(Patient.NAME.matches().value("james"))
			.summaryMode(SummaryEnum.FALSE)
			.returnBundle(Bundle.class)
			.execute();

		assertEquals("http://example.com/fhir/Patient?name=james&_summary=false", capt.getValue().getURI().toString());
		assertEquals(Patient.class, response.getEntry().get(0).getResource().getClass());

	}

	@Test
	public void testTransactionWithListOfResources() throws Exception {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		List<IBaseResource> input = new ArrayList<IBaseResource>();

		Patient p1 = new Patient(); // No ID
		p1.addName().addFamily("PATIENT1");
		input.add(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().addFamily("PATIENT2");
		p2.setId("Patient/2");
		input.add(p2);

		//@formatter:off
		List<IBaseResource> response = client.transaction()
			.withResources(input)
			.encodedJson()
			.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		assertEquals(2, response.size());

		String requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		ca.uhn.fhir.model.dstu2.resource.Bundle requestBundle = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, requestString);
		assertEquals(2, requestBundle.getEntry().size());
		assertEquals("POST", requestBundle.getEntry().get(0).getRequest().getMethod());
		assertEquals("PUT", requestBundle.getEntry().get(1).getRequest().getMethod());
		assertEquals("Patient/2", requestBundle.getEntry().get(1).getRequest().getUrl());
		assertEquals("application/json+fhir", capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));

		p1 = (Patient) response.get(0);
		assertEquals(new IdDt("Patient/1/_history/1"), p1.getId().toUnqualified());
		// assertEquals("PATIENT1", p1.getName().get(0).getFamily().get(0).getValue());

		p2 = (Patient) response.get(1);
		assertEquals(new IdDt("Patient/2/_history/2"), p2.getId().toUnqualified());
		// assertEquals("PATIENT2", p2.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testTransactionWithString() throws Exception {

		ca.uhn.fhir.model.dstu2.resource.Bundle req = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		req.addEntry().setResource(new Patient());
		req.addEntry().setResource(new Observation());
		String reqStringJson = ourCtx.newJsonParser().encodeResourceToString(req);
		String reqStringXml = ourCtx.newXmlParser().encodeResourceToString(req);

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		final String respStringJson = ourCtx.newJsonParser().encodeResourceToString(resp);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respStringJson), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		//@formatter:off
		String response = client.transaction()
			.withBundle(reqStringJson)
			.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/", capt.getValue().getURI().toString());
		assertEquals(respStringJson, response);
		String requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		IOUtils.closeQuietly(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		assertEquals(reqStringJson, requestString);
		assertEquals("application/json+fhir; charset=UTF-8", capt.getValue().getFirstHeader("Content-Type").getValue());

		//@formatter:off
		response = client.transaction()
			.withBundle(reqStringJson)
			.encodedXml()
			.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir/", capt.getValue().getURI().toString());
		assertEquals(respStringJson, response);
		requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		IOUtils.closeQuietly(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		assertEquals(reqStringXml, requestString);
		assertEquals("application/xml+fhir; charset=UTF-8", capt.getValue().getFirstHeader("Content-Type").getValue());

	}

	@Test
	public void testTransactionWithTransactionResource() throws Exception {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getResponse().setLocation("Patient/2/_history/2");
		String respString = ourCtx.newJsonParser().encodeResourceToString(resp);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8")));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		ca.uhn.fhir.model.dstu2.resource.Bundle input = new ca.uhn.fhir.model.dstu2.resource.Bundle();

		Patient p1 = new Patient(); // No ID
		p1.addName().addFamily("PATIENT1");
		input.addEntry().setResource(p1);

		Patient p2 = new Patient(); // Yes ID
		p2.addName().addFamily("PATIENT2");
		p2.setId("Patient/2");
		input.addEntry().setResource(p2);

		//@formatter:off
		ca.uhn.fhir.model.dstu2.resource.Bundle response = client.transaction()
			.withBundle(input)
			.encodedJson()
			.execute();
		//@formatter:on

		assertEquals("http://example.com/fhir", capt.getValue().getURI().toString());
		assertEquals(2, response.getEntry().size());

		assertEquals("Patient/1/_history/1", response.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient/2/_history/2", response.getEntry().get(1).getResponse().getLocation());
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

		client.update().resource(p).conditionalByUrl("Patient?name=http://foo|bar").execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("http://example.com/fhir/Patient?name=http%3A//foo%7Cbar", capt.getAllValues().get(idx).getURI().toString());
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

	@Test
	public void testUpdateNonFluent() throws Exception {
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

		client.update(new IdDt("Patient/123"), p);
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;

		client.update("123", p);
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_CONTENT_TYPE).length);
		assertEquals(EncodingEnum.XML.getResourceContentType() + Constants.HEADER_SUFFIX_CT_UTF_8, capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue());
		assertThat(extractBody(capt, idx), containsString("<family value=\"FOOFAMILY\"/>"));
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(idx).getURI().toString());
		assertEquals("PUT", capt.getAllValues().get(idx).getRequestLine().getMethod());
		idx++;
	}

	@Test
	public void testUpdatePrefer() throws Exception {
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
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		client.update().resource(p).prefer(PreferReturnEnum.MINIMAL).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER).length);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_MINIMAL, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER)[0].getValue());
		idx++;

		client.update().resource(p).prefer(PreferReturnEnum.REPRESENTATION).execute();
		assertEquals(1, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER).length);
		assertEquals(Constants.HEADER_PREFER_RETURN + '=' + Constants.HEADER_PREFER_RETURN_REPRESENTATION, capt.getAllValues().get(idx).getHeaders(Constants.HEADER_PREFER)[0].getValue());
		idx++;

	}

	@Test
	public void testUpdateReturningResourceBody() throws Exception {
		Patient p = new Patient();
		p.setId("123");
		final String formatted = ourCtx.newXmlParser().encodeResourceToString(p);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), Constants.STATUS_HTTP_200_OK, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(formatted), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		p = new Patient();
		p.setId(new IdDt("1"));
		p.addName().addFamily("FOOFAMILY");

		MethodOutcome output = client.update().resource(p).execute();
		assertNotNull(output.getResource());
		assertEquals("Patient/123", output.getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void testValidateFluent() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");

		int idx = 0;
		MethodOutcome response;

		response = client.validate().resource(p).execute();
		assertEquals("http://example.com/fhir/Patient/$validate", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("application/xml+fhir", capt.getAllValues().get(idx).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			extractBody(capt, idx));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		idx++;

		response = client.validate().resource(ourCtx.newXmlParser().encodeResourceToString(p)).execute();
		assertEquals("http://example.com/fhir/Patient/$validate", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("application/xml+fhir", capt.getAllValues().get(idx).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			extractBody(capt, idx));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		idx++;

		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).execute();
		assertEquals("http://example.com/fhir/Patient/$validate", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("application/json+fhir", capt.getAllValues().get(idx).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"given\":[\"GIVEN\"]}]}}]}", extractBody(capt, idx));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		idx++;

		response = client.validate().resource(ourCtx.newJsonParser().encodeResourceToString(p)).prettyPrint().execute();
		assertEquals("http://example.com/fhir/Patient/$validate?_pretty=true", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals("application/json+fhir", capt.getAllValues().get(idx).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertThat(extractBody(capt, idx), containsString("\"resourceType\": \"Parameters\",\n"));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		idx++;
	}

	@Test
	public void testValidateNonFluent() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("FOOBAR");
		final String msg = ourCtx.newXmlParser().encodeResourceToString(oo);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient p = new Patient();
		p.addName().addGiven("GIVEN");

		int idx = 0;
		MethodOutcome response;

		//@formatter:off
		response = client.validate(p);
		//@formatter:on

		assertEquals("http://example.com/fhir/Patient/$validate", capt.getAllValues().get(idx).getURI().toASCIIString());
		assertEquals("POST", capt.getAllValues().get(idx).getRequestLine().getMethod());
		assertEquals(
			"<Parameters xmlns=\"http://hl7.org/fhir\"><parameter><name value=\"resource\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><name><given value=\"GIVEN\"/></name></Patient></resource></parameter></Parameters>",
			extractBody(capt, idx));
		assertNotNull(response.getOperationOutcome());
		assertEquals("FOOBAR", toOo(response.getOperationOutcome()).getIssueFirstRep().getDiagnosticsElement().getValue());
		idx++;
	}

	private OperationOutcome toOo(IBaseOperationOutcome theOperationOutcome) {
		return (OperationOutcome) theOperationOutcome;
	}

	public final static class RestfulClientInstance implements IRestfulClient {
		@Override
		public IInterceptorService getInterceptorService() {
			return null;
		}

		@Override
		public void setInterceptorService(@Nonnull IInterceptorService theInterceptorService) {
			// nothing
		}

		@Override
		public <T extends IBaseResource> T fetchResourceFromUrl(Class<T> theResourceType, String theUrl) {
			return null;
		}

		@Override
		public FhirContext getFhirContext() {
			return null;
		}

		@Override
		public IHttpClient getHttpClient() {
			return null;
		}

		@Override
		public String getServerBase() {
			return null;
		}

		@Override
		public void registerInterceptor(IClientInterceptor theInterceptor) {
			// nothing
		}

		@Override
		public void setPrettyPrint(Boolean thePrettyPrint) {
			// nothing
		}

		@Override
		public void setSummary(SummaryEnum theSummary) {
			// nothing
		}

		@Override
		public void unregisterInterceptor(IClientInterceptor theInterceptor) {
			// nothing
		}

		@Override
		public void setFormatParamStyle(RequestFormatParamStyleEnum theRequestFormatParamStyle) {
			// nothing
		}

		@Override
		public EncodingEnum getEncoding() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setEncoding(EncodingEnum theEncoding) {
			// nothing
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2();
	}

}
