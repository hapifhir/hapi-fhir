package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.*;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.TestUtil;

public class PatchClientDstu3Test {
	public interface IClientType extends IRestfulClient {

		@Patch(type=Patient.class)
		MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theBody, PatchTypeEnum thePatchType);
		
	}

	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testJsonPatchAnnotation() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareResponse();

		IClientType client = ourCtx.newRestfulClient(IClientType.class, "http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

		MethodOutcome outcome = client.patch(new IdType("Patient/123"), "{}", PatchTypeEnum.JSON_PATCH);

		assertEquals("PATCH", capt.getAllValues().get(0).getMethod());
		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
		assertEquals("{}", extractBodyAsString(capt));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
	}

	@Test
	@Ignore
	public void testJsonPatchFluent() throws Exception {
		ArgumentCaptor<HttpUriRequest> capt = prepareResponse();

		IGenericClient client = ourCtx.newRestfulGenericClient("http://example.com/fhir");

		Patient pt = new Patient();
		pt.getText().setDivAsString("A PATIENT");

//		MethodOutcome outcome = client.patch().resource("").
				
//				patch(new IdType("Patient/123"), "{}", PatchTypeEnum.JSON_PATCH);

//		assertEquals("PATCH", capt.getAllValues().get(0).getMethod());
//		assertEquals("http://example.com/fhir/Patient/123", capt.getAllValues().get(0).getURI().toASCIIString());
//		assertEquals(Constants.CT_JSON_PATCH, capt.getAllValues().get(0).getFirstHeader("content-type").getValue().replaceAll(";.*", ""));
//		assertEquals("{}", extractBodyAsString(capt));
//		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK</div>", ((OperationOutcome) outcome.getOperationOutcome()).getText().getDivAsString());
	}


	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}

	private ArgumentCaptor<HttpUriRequest> prepareResponse() throws IOException, ClientProtocolException {
		final IParser p = ourCtx.newXmlParser();

		final OperationOutcome resp1 = new OperationOutcome();
		resp1.getText().setDivAsString("OK");

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);
		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML_NEW + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<ReaderInputStream>() {
			@Override
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(p.encodeResourceToString(resp1)), Charset.forName("UTF-8"));
			}
		});
		return capt;
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
