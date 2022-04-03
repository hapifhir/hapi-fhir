package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.impl.BaseClient;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.IdType;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
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
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NonGenericClientDstu2_1Test {
	private static FhirContext ourCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;

	@BeforeEach
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	
		System.setProperty(BaseClient.HAPI_CLIENT_KEEPRESPONSES, "true");

	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt, int theIdx) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(theIdx)).getEntity().getContent(), "UTF-8");
		return body;
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IClientWithCustomType client = ourCtx.newRestfulClient(IClientWithCustomType.class, "http://example.com/fhir");
		List<MyPatientWithExtensions> list = client.search();
		
		assertEquals(1, list.size());
		assertEquals(MyPatientWithExtensions.class, list.get(0).getClass());
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
			public ReaderInputStream answer(InvocationOnMock theInvocation) throws Throwable {
				return new ReaderInputStream(new StringReader(respString), Charset.forName("UTF-8"));
			}
		});

		IClientWithCustomType client = ourCtx.newRestfulClient(IClientWithCustomType.class, "http://example.com/fhir");
		MyPatientWithExtensions read = client.read(new IdType("1"));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div>", read.getText().getDivAsString());
	}
	
	@Test
	public void testValidateResourceOnly() throws Exception {
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

		IClient client = ourCtx.newRestfulClient(IClient.class, "http://example.com/fhir");

		Patient patient = new Patient();
		patient.addName().addFamily("FAM");
		
		int idx = 0;
		MethodOutcome outcome = client.validate(patient, null, null);
		String resp = ourCtx.newXmlParser().encodeResourceToString(outcome.getOperationOutcome());
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div></text></OperationOutcome>", resp);
		assertEquals("http://example.com/fhir/$validate", capt.getAllValues().get(idx).getURI().toString());
		String request = extractBodyAsString(capt,idx);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"family\":[\"FAM\"]}]}}]}", request);

		idx = 1;
		outcome = client.validate(patient, ValidationModeEnum.CREATE, "http://foo");
		resp = ourCtx.newXmlParser().encodeResourceToString(outcome.getOperationOutcome());
		assertEquals("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">OK!</div></text></OperationOutcome>", resp);
		assertEquals("http://example.com/fhir/$validate", capt.getAllValues().get(idx).getURI().toString());
		request = extractBodyAsString(capt,idx);
		assertEquals("{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"resource\",\"resource\":{\"resourceType\":\"Patient\",\"name\":[{\"family\":[\"FAM\"]}]}},{\"name\":\"mode\",\"valueString\":\"create\"},{\"name\":\"profile\",\"valueString\":\"http://foo\"}]}", request);
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
	
	@BeforeAll
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2_1();
	}

	private interface IClient extends IRestfulClient {
		
		@Validate
		MethodOutcome validate(@ResourceParam IBaseResource theResource, @Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile);
		
	}

	private interface IClientWithCustomType extends IRestfulClient {
		
		@Search
		List<MyPatientWithExtensions> search();

		@Read
		MyPatientWithExtensions read(@IdParam IdType theId);
		
	}

}
