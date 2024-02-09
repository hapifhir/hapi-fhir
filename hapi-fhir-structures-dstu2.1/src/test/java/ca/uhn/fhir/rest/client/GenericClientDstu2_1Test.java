package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.CustomTypeDstu2_1Test;
import ca.uhn.fhir.parser.CustomTypeDstu2_1Test.MyCustomPatient;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;
import ca.uhn.fhir.rest.client.interceptor.CookieInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UserInfoInterceptor;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;
import com.google.common.base.Charsets;
import com.helger.commons.io.stream.StringInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2016may.model.Binary;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleType;
import org.hl7.fhir.dstu2016may.model.Conformance;
import org.hl7.fhir.dstu2016may.model.Device;
import org.hl7.fhir.dstu2016may.model.Encounter;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
import org.hl7.fhir.dstu2016may.model.Parameters;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericClientDstu2_1Test {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(GenericClientDstu2_1Test.class);
	private int myAnswerCount;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@BeforeEach
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
		myAnswerCount = 0;
		HapiSystemProperties.enableHapiClientKeepResponses();
	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.DSTU2_1.getFhirVersionString() + "/DSTU2_1; apache)";
	}

	private byte[] extractBodyAsByteArray(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		byte[] body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent());
		return body;
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), StandardCharsets.UTF_8);
		return body;
	}

	private ArgumentCaptor<HttpUriRequest> prepareClientForSearchResponse() throws IOException {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});
		return capt;
	}

	@Test
	public void testPatchJsonByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.withId("Patient/123")
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByIdType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.withId(new IdType("http://localhost/fhir/Patient/123/_history/234"))
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByConditionalString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.conditionalByUrl("Patient?foo=bar")
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?foo=bar");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByConditionalParam() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.conditional("Patient").where(Patient.NAME.matches().value("TEST"))
				.and(Patient.FAMILY.matches().value("TEST2"))
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=TEST&family=TEST2");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchJsonByConditionalParamResourceType() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.conditional(Patient.class).where(Patient.NAME.matches().value("TEST"))
				.and(Patient.FAMILY.matches().value("TEST2"))
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=TEST&family=TEST2");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_JSON_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchXmlByIdString() throws Exception {
		OperationOutcome conf = new OperationOutcome();
		conf.getText().setDivAsString("OK!");
		final String respString = ourCtx.newJsonParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		String patch = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";

		MethodOutcome outcome = client
				.patch()
				.withBody(patch)
				.withId("Patient/123")
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient/123");
		assertThat(capt.getAllValues().get(0).getRequestLine().getMethod()).isEqualTo("PATCH");
		assertThat(extractBodyAsString(capt)).isEqualTo(patch);
		assertThat(capt.getAllValues().get(idx).getFirstHeader("Content-Type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_XML_PATCH);
		idx++;

		OperationOutcome oo = (OperationOutcome) outcome.getOperationOutcome();
		assertThat(oo.getText().getDivAsString()).contains("OK!");
	}

	@Test
	public void testPatchInvalid() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client
					.patch()
					.withBody("AA")
					.withId("Patient/123")
					.execute();
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1386) + "Unable to determine encoding of patch");
		}
	}

	@Test
	public void testAcceptHeaderWithEncodingSpecified() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.setEncoding(EncodingEnum.JSON);
		client.search()
				.forResource("Device")
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?_format=json");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue()).isEqualTo(Constants.CT_FHIR_JSON);
		idx++;

		client.setEncoding(EncodingEnum.XML);
		client.search()
				.forResource("Device")
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?_format=xml");
		assertThat(capt.getAllValues().get(idx).getFirstHeader(Constants.HEADER_ACCEPT).getValue()).isEqualTo(Constants.CT_FHIR_XML);
		idx++;

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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		Binary bin = new Binary();
		bin.setContent(ourCtx.newJsonParser().encodeResourceToString(pt).getBytes(StandardCharsets.UTF_8));
		bin.setContentType(Constants.CT_FHIR_JSON);
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/json+fhir;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.CT_FHIR_JSON);
		Binary output = ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt));
		assertThat(output.getContentType()).isEqualTo(Constants.CT_FHIR_JSON);

		Patient outputPt = (Patient) ourCtx.newJsonParser().parseResource(new String(output.getContent(), StandardCharsets.UTF_8));
		assertThat(outputPt.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">A PATIENT</div>");
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/json+fhir;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.CT_FHIR_JSON);
		assertThat(ourCtx.newJsonParser().parseResource(Binary.class, extractBodyAsString(capt)).getContent()).containsExactly(new byte[]{0, 1, 2, 3, 4});

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testClientFailures() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenThrow(IllegalStateException.class, RuntimeException.class, IOException.class);

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1360) + "java.lang.IllegalStateException");
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (RuntimeException e) {
			assertThat(e.toString()).isEqualTo("java.lang.RuntimeException");
		}

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).contains("java.io.IOException");
		}
	}

	@Test
	public void testCookieInterceptor() throws Exception {
		final String respString = CustomTypeDstu2_1Test.createBundle(CustomTypeDstu2_1Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new CookieInterceptor("foo=bar"));

		Bundle resp = client
				.history()
				.onType(Patient.class)
				.andReturnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(0).getFirstHeader("Cookie").getValue()).isEqualTo("foo=bar");
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
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(2);
		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");
		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
		assertThat(capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", "")).isEqualTo(Constants.CT_FHIR_JSON);

		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://foo.com/base/Patient/222/_history/3");
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
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.create().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(1);
		assertThat(outcome.getOperationOutcome()).isNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
	}

	@Test
	public void testExplicitCustomTypeHistoryType() throws Exception {
		final String respString = CustomTypeDstu2_1Test.createBundle(CustomTypeDstu2_1Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
				.history()
				.onType(CustomTypeDstu2_1Test.MyCustomPatient.class)
				.andReturnBundle(Bundle.class)
				.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/_history");
	}

	@Test
	public void testExplicitCustomTypeLoadPage() throws Exception {
		final String respString = CustomTypeDstu2_1Test.createBundle(CustomTypeDstu2_1Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = new Bundle();
		bundle.addLink().setRelation("next").setUrl("http://foo/next");

		Bundle resp = client
				.loadPage()
				.next(bundle)
				.preferResponseType(MyCustomPatient.class)
				.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://foo/next");

		resp = client
				.loadPage()
				.next(bundle)
				.preferResponseTypes(toTypeList(MyCustomPatient.class))
				.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://foo/next");
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Parameters resp = client
				.operation()
				.onServer()
				.named("foo")
				.withNoParameters(Parameters.class)
				.preferResponseType(MyCustomPatient.class)
				.execute();

		assertThat(resp.getParameter()).hasSize(1);
		assertThat(resp.getParameter().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/$foo");

		resp = client
				.operation()
				.onType(MyCustomPatient.class)
				.named("foo")
				.withNoParameters(Parameters.class)
				.execute();

		assertThat(resp.getParameter()).hasSize(1);
		assertThat(resp.getParameter().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/$foo");
	}

	@Test
	public void testExplicitCustomTypeSearch() throws Exception {
		final String respString = CustomTypeDstu2_1Test.createBundle(CustomTypeDstu2_1Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Bundle resp = client
				.search()
				.forResource(CustomTypeDstu2_1Test.MyCustomPatient.class)
				.returnBundle(Bundle.class)
				.execute();

		assertThat(resp.getEntry()).hasSize(1);
		assertThat(resp.getEntry().get(0).getResource().getClass()).isEqualTo(CustomTypeDstu2_1Test.MyCustomPatient.class);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient");
	}

	@Test
	public void testFetchCapabilityStatementReceiveCapabilityStatement() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			private int myCount = 0;

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myCount == 1 || myCount == 2) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myCount++;
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformanceCapabilityStatement.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/metadata");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/Patient/1");

		client.forceConformanceCheck();
		assertThat(capt.getAllValues()).hasSize(4);
		assertThat(capt.getAllValues().get(3).getURI().toASCIIString()).isEqualTo("http://testForceConformanceCapabilityStatement.com/fhir/metadata");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testFetchCapabilityStatementReceiveConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myAnswerCount >= 1) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformanceConformance.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://testForceConformanceConformance.com/fhir/metadata");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://testForceConformanceConformance.com/fhir/Patient/1");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toASCIIString()).isEqualTo("http://testForceConformanceConformance.com/fhir/Patient/1");

		myAnswerCount = 0;
		client.forceConformanceCheck();
		assertThat(capt.getAllValues()).hasSize(4);
		assertThat(capt.getAllValues().get(3).getURI().toASCIIString()).isEqualTo("http://testForceConformanceConformance.com/fhir/metadata");
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testForceConformance() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final Conformance conf = new Conformance();
		conf.setCopyright("COPY");

		final Patient patient = new Patient();
		patient.addName().addFamily("FAM");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {

			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				final String respString;
				if (myAnswerCount >= 1) {
					ourLog.info("Encoding patient");
					respString = p.encodeResourceToString(patient);
				} else {
					ourLog.info("Encoding conformance");
					respString = p.encodeResourceToString(conf);
				}
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = ourCtx.newRestfulGenericClient("http://testForceConformance.com/fhir");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(2);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/metadata");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/Patient/1");

		client.read().resource("Patient").withId("1").execute();
		assertThat(capt.getAllValues()).hasSize(3);
		assertThat(capt.getAllValues().get(2).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/Patient/1");

		myAnswerCount = 0;
		client.forceConformanceCheck();
		assertThat(capt.getAllValues()).hasSize(4);
		assertThat(capt.getAllValues().get(3).getURI().toASCIIString()).isEqualTo("http://testForceConformance.com/fhir/metadata");
	}

	@Test
	public void testHttp499() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 499, "Wacky Message"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("HELLO", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (UnclassifiedServerFailureException e) {
			assertThat(e.toString()).isEqualTo("ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException: HTTP 499 Wacky Message");
			assertThat(e.getResponseBody()).isEqualTo("HELLO");
		}

	}

	@Test
	public void testHttp501() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 501, "Not Implemented"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public StringInputStream answer(InvocationOnMock theInvocation) {
				return new StringInputStream("not implemented", Charsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 501 Not Implemented");
		}

	}

	/**
	 * See #150
	 */
	@Test
	public void testNullAndEmptyParamValuesAreIgnored() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
				.search()
				.forResource(Patient.class)
				.where(Patient.FAMILY.matches().value((String) null))
				.and(Patient.BIRTHDATE.exactly().day((Date) null))
				.and(Patient.GENDER.exactly().code(null))
				.and(Patient.ORGANIZATION.hasId((String) null))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient");
		idx++;

		client
				.search()
				.forResource(Encounter.class)
				.where(Encounter.LENGTH.exactly().number(null))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Encounter");
		idx++;

		client
				.search()
				.forResource(Observation.class)
				.where(Observation.VALUE_QUANTITY.exactly().number(null).andUnits(null))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Observation");
		idx++;

	}

	@Test
	public void testPutDoesntForceAllIdsJson() throws Exception {
		IParser p = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().addFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertThat(encoded).isEqualTo("{\"resourceType\":\"Bundle\",\"id\":\"BUNDLE1\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"PATIENT1\",\"name\":[{\"family\":[\"PATIENT1\"]}]}}]}");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
				.update()
				.resource(bundle)
				.prefer(PreferReturnEnum.REPRESENTATION)
				.encodedJson()
				.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertThat(httpRequest.getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Bundle/BUNDLE1");

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertThat(requestString).isEqualTo(encoded);
	}

	@Test
	public void testPutDoesntForceAllIdsXml() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.setId("PATIENT1");
		patient.addName().addFamily("PATIENT1");

		Bundle bundle = new Bundle();
		bundle.setId("BUNDLE1");
		bundle.addEntry().setResource(patient);

		final String encoded = p.encodeResourceToString(bundle);
		assertThat(encoded).isEqualTo("<Bundle xmlns=\"http://hl7.org/fhir\"><id value=\"BUNDLE1\"/><entry><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"PATIENT1\"/><name><family value=\"PATIENT1\"/></name></Patient></resource></entry></Bundle>");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(encoded), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
				.update()
				.resource(bundle)
				.prefer(PreferReturnEnum.REPRESENTATION)
				.encodedXml()
				.execute();

		HttpPut httpRequest = (HttpPut) capt.getValue();
		assertThat(httpRequest.getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Bundle/BUNDLE1");

		String requestString = IOUtils.toString(httpRequest.getEntity().getContent(), StandardCharsets.UTF_8);
		assertThat(requestString).isEqualTo(encoded);
	}

	@Test
	public void testReadWithUnparseableResponse() throws Exception {
		String msg = "{\"resourceTypeeeee\":\"Patient\"}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8));

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		try {
			client.read().resource("Patient").withId("123").elementsSubset("name", "identifier").execute();
			fail("");		} catch (FhirClientConnectionException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1359) + "Failed to parse response from server when performing GET to URL http://example.com/fhir/Patient/123?_elements=identifier%2Cname - ca.uhn.fhir.parser.DataFormatException: " + Msg.code(1838) + "Invalid JSON content detected, missing required element: 'resourceType'");
		}
	}

	@Test
	public void testResponseHasContentTypeMissing() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage()).isEqualTo("Response contains no Content-Type");
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testResponseHasContentTypeNonFhir() throws Exception {
		IParser p = ourCtx.newXmlParser();
		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		final String respString = p.encodeResourceToString(patient);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", "text/plain"));
		// when(myHttpResponse.getEntity().getContentType()).thenReturn(null);
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		try {
			client.read().resource(Patient.class).withId("1").execute();
			fail("");		} catch (NonFhirResponseException e) {
			assertThat(e.getMessage()).isEqualTo("Response contains non FHIR Content-Type 'text/plain' : <Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"FAM\"/></name></Patient>");
		}

		// Patient resp = client.read().resource(Patient.class).withId("1").execute();
		// assertEquals("FAM", resp.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testSearchByDate() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		DateTimeDt now = DateTimeDt.withCurrentTime();
		String dateString = now.getValueAsString().substring(0, 10);

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.after().day(dateString))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=gt" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.after().day(now.getValue()))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=gt" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.afterOrEquals().day(dateString))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=ge" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.before().day(dateString))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=lt" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.beforeOrEquals().day(dateString))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=le" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.exactly().day(dateString))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?birthdate=" + dateString);
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.after().second("2011-01-02T22:33:01Z"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?birthdate=gt2011-01-02T22:33:01Z");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.after().second(now.getValueAsString()))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?birthdate=gt" + now.getValueAsString());
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.BIRTHDATE.after().now())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).startsWith("http://example.com/fhir/Patient?birthdate=gt2");
		dateString = UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString()).substring(44);
		ourLog.info(dateString);
		assertThat(new DateTimeDt(dateString).getPrecision()).isEqualTo(TemporalPrecisionEnum.SECOND);
		idx++;
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSearchByQuantity() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.approximately().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=ap123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("CODE"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=ap123||CODE");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.approximately().number("123").andUnits("SYSTEM", "CODE"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=ap123|SYSTEM|CODE");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.exactly().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("CODE"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=123||CODE");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.exactly().number("123").andUnits("SYSTEM", "CODE"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=123|SYSTEM|CODE");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.greaterThan().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=gt123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.lessThan().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=lt123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.greaterThanOrEquals().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=ge123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.lessThanOrEquals().number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=le123||");
		idx++;

		client.search()
				.forResource("Observation")
				.where(Observation.VALUE_QUANTITY.withPrefix(ParamPrefixEnum.GREATERTHAN).number(123).andNoUnits())
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Observation?value-quantity=gt123||");
		idx++;

	}

	@Test
	public void testSearchByString() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value("AAA"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=AAA");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().value(new StringDt("AAA")))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name=AAA");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().values("AAA", "BBB"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=AAA,BBB");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matches().values(Arrays.asList("AAA", "BBB")))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name=AAA,BBB");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().value("AAA"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Aexact=AAA");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().value(new StringDt("AAA")))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?name%3Aexact=AAA");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().values("AAA", "BBB"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name:exact=AAA,BBB");
		idx++;

		client.search()
				.forResource("Patient")
				.where(Patient.NAME.matchesExactly().values(Arrays.asList("AAA", "BBB")))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?name:exact=AAA,BBB");
		idx++;

	}

	@Test
	public void testSearchByUrl() throws Exception {
		final String msg = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_JSON + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).then(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(msg), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client.search()
				.forResource("Device")
				.where(Device.URL.matches().value("http://foo.com"))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?url=http://foo.com");
		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Device?url=http%3A%2F%2Ffoo.com");
		idx++;

		client.search()
				.forResource("Device")
				.where(Device.URL.matches().value(new StringDt("http://foo.com")))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Device?url=http://foo.com");
		idx++;

	}

	@Test
	public void testSearchWithMultipleTokens() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareClientForSearchResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		Collection<String> values = Arrays.asList("VAL1", "VAL2", "VAL3A,B");

		client.search()
				.forResource("Patient")
				.where(Patient.IDENTIFIER.exactly().systemAndValues("SYS", values))
				.returnBundle(Bundle.class)
				.execute();

		assertThat(capt.getAllValues().get(idx).getURI().toString()).isEqualTo("http://example.com/fhir/Patient?identifier=SYS%7CVAL1%2CSYS%7CVAL2%2CSYS%7CVAL3A%5C%2CB");
		assertThat(UrlUtil.unescape(capt.getAllValues().get(idx).getURI().toString())).isEqualTo("http://example.com/fhir/Patient?identifier=SYS|VAL1,SYS|VAL2,SYS|VAL3A\\,B");
		idx++;

	}

	/**
	 * See #371
	 */
	@Test
	public void testSortDstu3Test() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();
		b.setType(BundleType.SEARCHSET);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		int idx = 0;

		client
				.search()
				.forResource(Patient.class)
				.sort().ascending("address")
				.returnBundle(Bundle.class)
				.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=address");

		client
				.search()
				.forResource(Patient.class)
				.sort().descending("address")
				.returnBundle(Bundle.class)
				.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=-address");

		client
				.search()
				.forResource(Patient.class)
				.sort().descending("address")
				.sort().ascending("name")
				.sort().descending(Patient.BIRTHDATE)
				.returnBundle(Bundle.class)
				.execute();
		assertThat(capt.getAllValues().get(idx++).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient?_sort=-address%2Cname%2C-birthdate");

	}

	@Test
	public void testTransactionWithInvalidBody() {
		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		// Transaction
		try {
			client.transaction().withBundle("FOO");
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1395) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Create
		try {
			client.create().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Update
		try {
			client.update().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

		// Validate
		try {
			client.validate().resource("FOO").execute();
			fail("");		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1368) + "Unable to determing encoding of request (body does not appear to be valid XML or JSON)");
		}

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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("222");
		pt.getText().setDivAsString("A PATIENT");

		client.update().resource(pt).withId("111").execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/111");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue().toLowerCase().replace(" ", "")).isEqualTo("application/json+fhir;charset=utf-8");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.CT_FHIR_JSON);
		String body = extractBodyAsString(capt);
		assertThat(body).contains("\"id\":\"111\"");
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
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				if (myAnswerCount++ == 0) {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
				} else {
					return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
				}
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(2);
		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");
		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
		assertThat(capt.getAllValues().get(1).getURI().toASCIIString()).isEqualTo("http://foo.com/base/Patient/222/_history/3");
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
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[] { new BasicHeader(Constants.HEADER_LOCATION, "http://foo.com/base/Patient/222/_history/3") };
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				myAnswerCount++;
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.update().resource(pt).prefer(PreferReturnEnum.REPRESENTATION).execute();

		assertThat(myAnswerCount).isEqualTo(1);
		assertThat(outcome.getOperationOutcome()).isNull();
		assertThat(outcome.getResource()).isNotNull();

		assertThat(((Patient) outcome.getResource()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">FINAL VALUE</div>");

		assertThat(capt.getAllValues()).hasSize(myAnswerCount);
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Patient/222");
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Binary bin = new Binary();
		bin.setContentType("application/foo");
		bin.setContent(new byte[] { 0, 1, 2, 3, 4 });
		client.create().resource(bin).execute();

		ourLog.info(Arrays.asList(capt.getAllValues().get(0).getAllHeaders()).toString());

		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/Binary");
		validateUserAgent(capt);

		assertThat(capt.getAllValues().get(0).getHeaders("Content-Type")[0].getValue()).isEqualTo("application/foo");
		assertThat(capt.getAllValues().get(0).getHeaders("Accept")[0].getValue()).isEqualTo(Constants.HEADER_ACCEPT_VALUE_XML_OR_JSON_LEGACY);
		assertThat(extractBodyAsByteArray(capt)).containsExactly(new byte[]{0, 1, 2, 3, 4});

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
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		client.fetchConformance().ofType(Conformance.class).execute();
		assertThat(capt.getAllValues().get(0).getURI().toASCIIString()).isEqualTo("http://example.com/fhir/metadata");
		validateUserAgent(capt);
	}

	@Test
	public void testUserInfoInterceptor() throws Exception {
		final String respString = CustomTypeDstu2_1Test.createBundle(CustomTypeDstu2_1Test.createResource(false));
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		client.registerInterceptor(new UserInfoInterceptor("user_id", "user_name", "app-name"));

		Bundle resp = client
				.history()
				.onType(Patient.class)
				.andReturnBundle(Bundle.class)
				.execute();

	}

	@Test
	public void testValidate() throws Exception {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp0 = new OperationOutcome();
		resp0.getText().setDivAsString("OK!");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getAllHeaders()).thenAnswer(new Answer<Header[]>() {
			@Override
			public Header[] answer(InvocationOnMock theInvocation) {
				return new Header[] {};
			}
		});
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp0)), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.setId("Patient/222");
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.validate().resource(pt).execute();

		assertThat(outcome.getOperationOutcome()).isNotNull();
		assertThat(((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");

	}

	@Test
	public void testValidateCustomTypeFromClientRead() throws Exception {
		IParser p = ourCtx.newXmlParser();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");

		final String respString = p.encodeResourceToString(patient);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		MyPatientWithExtensions read = client.read().resource(MyPatientWithExtensions.class).withId(new IdType("1")).execute();
		assertThat(read.getText().getDivAsString()).isEqualTo("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>");

		// Ensure that we haven't overridden the default type for the name
		assertThat(MyPatientWithExtensions.class.isAssignableFrom(Patient.class)).isFalse();
		assertThat(Patient.class.isAssignableFrom(MyPatientWithExtensions.class)).isFalse();
		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");
		IParser parser = ourCtx.newXmlParser();
		String encoded = parser.encodeResourceToString(pt);
		pt = (Patient) parser.parseResource(encoded);

	}

	@Test
	public void testValidateCustomTypeFromClientSearch() throws Exception {
		IParser p = ourCtx.newXmlParser();

		Bundle b = new Bundle();

		MyPatientWithExtensions patient = new MyPatientWithExtensions();
		patient.setId("123");
		patient.getText().setDivAsString("OK!");
		b.addEntry().setResource(patient);

		final String respString = p.encodeResourceToString(b);
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) {
				return new ReaderInputStream(new StringReader(respString), StandardCharsets.UTF_8);
			}
		});

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");
		Bundle bundle = client.search().forResource(MyPatientWithExtensions.class).returnBundle(Bundle.class).execute();

		assertThat(bundle.getEntry()).hasSize(1);
		assertThat(bundle.getEntry().get(0).getResource().getClass()).isEqualTo(MyPatientWithExtensions.class);
	}

	private List<Class<? extends IBaseResource>> toTypeList(Class<? extends IBaseResource> theClass) {
		ArrayList<Class<? extends IBaseResource>> retVal = new ArrayList<Class<? extends IBaseResource>>();
		retVal.add(theClass);
		return retVal;
	}

	private void validateUserAgent(ArgumentCaptor<HttpUriRequest> capt) {
		assertThat(capt.getAllValues().get(0).getHeaders("User-Agent").length).isEqualTo(1);
		assertThat(capt.getAllValues().get(0).getHeaders("User-Agent")[0].getValue()).isEqualTo(expectedUserAgent());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2_1();
	}

}
