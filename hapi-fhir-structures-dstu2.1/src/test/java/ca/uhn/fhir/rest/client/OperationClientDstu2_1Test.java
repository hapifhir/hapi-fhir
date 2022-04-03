package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2016may.model.Parameters;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OperationClientDstu2_1Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OperationClientDstu2_1Test.class);
	private FhirContext ourCtx;
	private HttpClient ourHttpClient;

	private HttpResponse ourHttpResponse;
	private IOpClient ourAnnClient;
	private ArgumentCaptor<HttpUriRequest> capt;
	private IGenericClient ourGenClient;


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@BeforeEach
	public void before() throws Exception {
		ourCtx = FhirContext.forDstu2_1();

		ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
		
		Parameters outParams = new Parameters();
		outParams.addParameter().setName("FOO");
		final String retVal = ourCtx.newXmlParser().encodeResourceToString(outParams);

		capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(retVal), Charset.forName("UTF-8"));
			}
		});

		ourAnnClient = ourCtx.newRestfulClient(IOpClient.class, "http://foo");
		ourGenClient = ourCtx.newRestfulGenericClient("http://foo");
	}

	@Test
	public void testNonRepeatingGenericUsingParameters() throws Exception {
		ourGenClient
			.operation()
			.onServer()
			.named("nonrepeating")
			.withSearchParameter(Parameters.class, "valstr", new StringParam("str"))
			.andSearchParameter("valtok", new TokenParam("sys2", "val2"))
			.execute();
		Parameters response = ourAnnClient.nonrepeating(new StringParam("str"), new TokenParam("sys", "val"));
		assertEquals("FOO", response.getParameter().get(0).getName());
		
		HttpPost value = (HttpPost) capt.getAllValues().get(0);
		String requestBody = IOUtils.toString(((HttpPost) value).getEntity().getContent());
		IOUtils.closeQuietly(((HttpPost) value).getEntity().getContent());
		ourLog.info(requestBody);
		Parameters request = ourCtx.newJsonParser().parseResource(Parameters.class, requestBody);
		assertEquals("http://foo/$nonrepeating", value.getURI().toASCIIString());
		assertEquals(2, request.getParameter().size());
		assertEquals("valstr", request.getParameter().get(0).getName());
		assertEquals("str", ((StringType) request.getParameter().get(0).getValue()).getValue());
		assertEquals("valtok", request.getParameter().get(1).getName());
		assertEquals("sys2|val2", ((StringType) request.getParameter().get(1).getValue()).getValue());
	}

	@Test
	public void testNonRepeatingGenericUsingUrl() throws Exception {
		ourGenClient
			.operation()
			.onServer()
			.named("nonrepeating")
			.withSearchParameter(Parameters.class, "valstr", new StringParam("str"))
			.andSearchParameter("valtok", new TokenParam("sys2", "val2"))
			.useHttpGet()
			.execute();
		Parameters response = ourAnnClient.nonrepeating(new StringParam("str"), new TokenParam("sys", "val"));
		assertEquals("FOO", response.getParameter().get(0).getName());
		
		HttpGet value = (HttpGet) capt.getAllValues().get(0);
		assertEquals("http://foo/$nonrepeating?valstr=str&valtok=sys2%7Cval2", value.getURI().toASCIIString());
	}

	@Test
	public void testNonRepeatingUsingParameters() throws Exception {
		Parameters response = ourAnnClient.nonrepeating(new StringParam("str"), new TokenParam("sys", "val"));
		assertEquals("FOO", response.getParameter().get(0).getName());
		
		HttpPost value = (HttpPost) capt.getAllValues().get(0);
		String requestBody = IOUtils.toString(((HttpPost) value).getEntity().getContent());
		IOUtils.closeQuietly(((HttpPost) value).getEntity().getContent());
		ourLog.info(requestBody);
		Parameters request = ourCtx.newJsonParser().parseResource(Parameters.class, requestBody);
		assertEquals("http://foo/$nonrepeating", value.getURI().toASCIIString());
		assertEquals(2, request.getParameter().size());
		assertEquals("valstr", request.getParameter().get(0).getName());
		assertEquals("str", ((StringType) request.getParameter().get(0).getValue()).getValue());
		assertEquals("valtok", request.getParameter().get(1).getName());
		assertEquals("sys|val", ((StringType) request.getParameter().get(1).getValue()).getValue());
	}

	public interface IOpClient extends IBasicClient {

		@Operation(name = "$andlist", idempotent = true)
		public Parameters andlist(
				//@formatter:off
				@OperationParam(name="valstr", max=10) StringAndListParam theValStr,
				@OperationParam(name="valtok", max=10) TokenAndListParam theValTok
				//@formatter:on
		);

		@Operation(name = "$andlist-withnomax", idempotent = true)
		public Parameters andlistWithNoMax(
				//@formatter:off
				@OperationParam(name="valstr") StringAndListParam theValStr,
				@OperationParam(name="valtok") TokenAndListParam theValTok
				//@formatter:on
		);

		@Operation(name = "$nonrepeating", idempotent = true)
		public Parameters nonrepeating(
				//@formatter:off
				@OperationParam(name="valstr") StringParam theValStr,
				@OperationParam(name="valtok") TokenParam theValTok
				//@formatter:on
		);

		@Operation(name = "$orlist", idempotent = true)
		public Parameters orlist(
				//@formatter:off
				@OperationParam(name="valstr", max=10) List<StringOrListParam> theValStr,
				@OperationParam(name="valtok", max=10) List<TokenOrListParam> theValTok
				//@formatter:on
		);

		@Operation(name = "$orlist-withnomax", idempotent = true)
		public Parameters orlistWithNoMax(
				//@formatter:off
				@OperationParam(name="valstr") List<StringOrListParam> theValStr,
				@OperationParam(name="valtok") List<TokenOrListParam> theValTok
				//@formatter:on
		);

	}
}
