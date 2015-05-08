package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.instance.model.IBaseResource;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class GenericClientDstu2Test {
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

		//@formatter:off
		Bundle response = client.search()
				.forResource(Patient.class)
				.encodedJson()
				.revInclude(new Include("Provenance:target"))
				.execute();
		//@formatter:on

		assertEquals(
				"http://example.com/fhir/Patient?_revinclude=Provenance%3Atarget&_format=json",
				capt.getValue().getURI().toString());

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
	public void testOperationWithBundleResponse() throws Exception {
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
	public void testTransactionWithListOfResources() throws Exception {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getTransactionResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getTransactionResponse().setLocation("Patient/2/_history/2");
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

		assertEquals("http://example.com/fhir?_format=json", capt.getValue().getURI().toString());
		assertEquals(2, response.size());

		String requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		ca.uhn.fhir.model.dstu2.resource.Bundle requestBundle = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, requestString);
		assertEquals(2, requestBundle.getEntry().size());
		assertEquals("POST", requestBundle.getEntry().get(0).getTransaction().getMethod());
		assertEquals("PUT", requestBundle.getEntry().get(1).getTransaction().getMethod());
		assertEquals("Patient/2", requestBundle.getEntry().get(1).getTransaction().getUrl());

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
		resp.addEntry().getTransactionResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getTransactionResponse().setLocation("Patient/2/_history/2");
		final String respStringJson = ourCtx.newJsonParser().encodeResourceToString(resp);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respStringJson), Charset.forName("UTF-8"));
			}});
		
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

		assertEquals("http://example.com/fhir/?_format=xml", capt.getValue().getURI().toString());
		assertEquals(respStringJson, response);
		requestString = IOUtils.toString(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		IOUtils.closeQuietly(((HttpEntityEnclosingRequest) capt.getValue()).getEntity().getContent());
		assertEquals(reqStringXml, requestString);
		assertEquals("application/xml+fhir; charset=UTF-8", capt.getValue().getFirstHeader("Content-Type").getValue());

	}
	
	
	
	@Test
	public void testTransactionWithTransactionResource() throws Exception {

		ca.uhn.fhir.model.dstu2.resource.Bundle resp = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		resp.addEntry().getTransactionResponse().setLocation("Patient/1/_history/1");
		resp.addEntry().getTransactionResponse().setLocation("Patient/2/_history/2");
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

		assertEquals("http://example.com/fhir?_format=json", capt.getValue().getURI().toString());
		assertEquals(2, response.getEntry().size());

		assertEquals("Patient/1/_history/1", response.getEntry().get(0).getTransactionResponse().getLocation());
		assertEquals("Patient/2/_history/2", response.getEntry().get(1).getTransactionResponse().getLocation());
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
