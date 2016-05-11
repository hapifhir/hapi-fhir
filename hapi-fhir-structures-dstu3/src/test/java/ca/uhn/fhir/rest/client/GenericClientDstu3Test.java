package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Conformance;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.CustomTypeDstu3Test;
import ca.uhn.fhir.parser.CustomTypeDstu3Test.MyCustomPatient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;

public class GenericClientDstu3Test {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu3Test.class);
	private int myAnswerCount;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
		myAnswerCount = 0;
	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR DSTU3; apache)";
	}

	private byte[] extractBodyAsByteArray(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		byte[] body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent());
		return body;
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	@Test
	public void testBinaryCreateWithFhirContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

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

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		Binary bin = new Binary();
		bin.setContent(ourCtx.newJsonParser().encodeResourceToString(pt).getBytes("UTF-8"));
		bin.setContentType(Constants.CT_FHIR_JSON);
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		Binary output = ourCtx.newXmlParser().parseResource(Binary.class, extractBodyAsString(capt));
		assertEquals(Constants.CT_FHIR_JSON, output.getContentType());

		Patient outputPt = (Patient) ourCtx.newJsonParser().parseResource(new String(output.getContent(), "UTF-8"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div>", outputPt.getText().getDivAsString());
	}

	@Test
	public void testBinaryCreateWithNoContentType() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

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

		Binary bin = new Binary();
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, ourCtx.newXmlParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent());

	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(2, myAnswerCount);
		assertNotNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://foo.com/base/Patient/222/_history/3", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(1, myAnswerCount);
		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeHistoryType() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
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

		//@formatter:off
		Bundle resp = client
			.history()
			.onType(CustomTypeDstu3Test.MyCustomPatient.class)
			.andReturnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/_history", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeLoadPage() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
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
		Bundle bundle = new Bundle();
		bundle.addLink().setRelation("next").setUrl("http://foo/next");

		//@formatter:off
		Bundle resp = client
			.loadPage()
			.next(bundle)
			.preferResponseType(MyCustomPatient.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());

		//@formatter:off
		resp = client
			.loadPage()
			.next(bundle)
			.preferResponseTypes(toTypeList(MyCustomPatient.class))
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://foo/next", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeOperation() throws Exception {

		Parameters param = new Parameters();
		Patient patient = new Patient();
		patient.addName().addFamily("FOO");
		param.addParameter().setName("foo").setResource(patient);
		final String respString = ourCtx.newXmlParser().encodeResourceToString(param);

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

		//@formatter:off
		Parameters resp = client
			.operation()
			.onServer()
			.named("foo")
			.withNoParameters(Parameters.class)
			.preferResponseType(MyCustomPatient.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/$foo", capt.getAllValues().get(0).getURI().toASCIIString());

		//@formatter:off
		resp = client
			.operation()
			.onType(MyCustomPatient.class)
			.named("foo")
			.withNoParameters(Parameters.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getParameter().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getParameter().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient/$foo", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testExplicitCustomTypeSearch() throws Exception {
		final String respString = CustomTypeDstu3Test.createBundle(CustomTypeDstu3Test.createResource(false));
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

		//@formatter:off
		Bundle resp = client
			.search()
			.forResource(CustomTypeDstu3Test.MyCustomPatient.class)
			.returnBundle(Bundle.class)
			.execute();
		//@formatter:on

		assertEquals(1, resp.getEntry().size());
		assertEquals(CustomTypeDstu3Test.MyCustomPatient.class, resp.getEntry().get(0).getResource().getClass());
		assertEquals("http://example.com/fhir/Patient", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testUpdateById() throws Exception {
		IParser p = ourCtx.newXmlParser();

		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");

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

		Patient pt = new Patient();
		pt.setId("222");
		pt.getText().setDivAsString("A PATIENT");

		client.update().resource(pt).withId("111").execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Patient/111", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/xml+fhir;charset=utf-8", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", ""));
		assertEquals(Constants.CT_FHIR_XML, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		String body = extractBodyAsString(capt);
		assertThat(body, containsString("<id value=\"111\"/>"));
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsOO() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(2, myAnswerCount);
		assertNotNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/222", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals("http://foo.com/base/Patient/222/_history/3", capt.getAllValues().get(1).getURI().toASCIIString());
	}

	@Test
	public void testUpdateWithPreferRepresentationServerReturnsResource() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Patient resp1 = new Patient();
		resp1.getText().setDivAsString("FINAL VALUE");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) throws Throwable {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertEquals(1, myAnswerCount);
		assertNull(outcome.getOperationOutcome());
		assertNotNull(outcome.getResource());

		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>", ((Patient) outcome.getResource()).getText().getDivAsString());

		assertEquals(myAnswerCount, capt.getAllValues().size());
		assertEquals("http://example.com/fhir/Patient/222", capt.getAllValues().get(0).getURI().toASCIIString());
	}

	@Test
	public void testUserAgentForBinary() throws Exception {
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

		Binary bin = new Binary();
		bin.setContentType("application/foo");
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertEquals("http://example.com/fhir/Binary", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);

		assertEquals("application/foo", capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue());
		assertEquals(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON, capt.getAllValues().get(0).getHeaders("Accept")[0].getValue());
		assertArrayEquals(new byte[] { 0, 1, 2, 3, 4 }, extractBodyAsByteArray(capt));

	}

	@Test
	public void testUserAgentForConformance() throws Exception {
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

		client.fetchConformance().ofType(Conformance.class).execute();
		assertEquals("http://example.com/fhir/metadata", capt.getAllValues().get(0).getURI().toASCIIString());
		validateUserAgent(capt);
	}

	private List<Class<? extends IBaseResource>> toTypeList(Class<? extends IBaseResource> theClass) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>();
		retVal.add(theClass);
		return retVal;
	}

	private void validateUserAgent(ArgumentCaptor<HttpUriRequest> capt) {
		assertEquals(1, capt.getAllValues().get(0).getHeaders("User-Agent").length);
		assertEquals(expectedUserAgent(), capt.getAllValues().get(0).getHeaders("User-Agent")[0].getValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu3();
	}
}
