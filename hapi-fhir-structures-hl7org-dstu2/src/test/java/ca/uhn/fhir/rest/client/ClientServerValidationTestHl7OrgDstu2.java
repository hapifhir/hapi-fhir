package ca.uhn.fhir.rest.client;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hl7.fhir.dstu2.model.Conformance;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.exceptions.FhirClientInappropriateForServerException;

public class ClientServerValidationTestHl7OrgDstu2 {

	private FhirContext myCtx;
	private HttpClient myHttpClient;
	private HttpResponse myHttpResponse;
	private boolean myFirstResponse;

	@Before
	public void before() {
		myHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		myHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());

		myCtx = FhirContext.forDstu2Hl7Org();
		myCtx.getRestfulClientFactory().setHttpClient(myHttpClient);
		myFirstResponse = true;
	}

	@Test
	public void testServerReturnsAppropriateVersionForDstu2() throws Exception {
		String appropriateFhirVersion = "1.0.2";
		assertThat(appropriateFhirVersion, is(FhirVersionEnum.DSTU2_HL7ORG.getFhirVersionString()));
		Conformance conf = new Conformance();
		conf.setFhirVersion(appropriateFhirVersion);
		final String confResource = myCtx.newXmlParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenAnswer(new Answer<InputStream>() {
			@Override
			public InputStream answer(InvocationOnMock theInvocation) throws Throwable {
				if (myFirstResponse) {
					myFirstResponse=false;
					return new ReaderInputStream(new StringReader(confResource), Charset.forName("UTF-8"));
				} else {
					return new ReaderInputStream(new StringReader(myCtx.newXmlParser().encodeResourceToString(new Patient())), Charset.forName("UTF-8"));
				}
			}});

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		IGenericClient client = myCtx.newRestfulGenericClient("http://foo");
		
		// don't load the conformance until the first time the client is actually used 
		assertTrue(myFirstResponse);
		client.read(new UriDt("http://foo/Patient/123"));
		assertFalse(myFirstResponse);
		myCtx.newRestfulGenericClient("http://foo").read(new UriDt("http://foo/Patient/123"));
		myCtx.newRestfulGenericClient("http://foo").read(new UriDt("http://foo/Patient/123"));

		// Conformance only loaded once, then 3 reads
		verify(myHttpClient, times(4)).execute(Matchers.any(HttpUriRequest.class));
	}

	@Test
	public void testServerReturnsWrongVersionForDstu2() throws Exception {
		String wrongFhirVersion = "3.0.1";
		assertThat(wrongFhirVersion, is(FhirVersionEnum.DSTU3.getFhirVersionString())); // asserting that what we assume to be the DSTU3 FHIR version is still correct
		Conformance conf = new Conformance();
		conf.setFhirVersion(wrongFhirVersion);
		String msg = myCtx.newXmlParser().encodeResourceToString(conf);

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

		when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

		when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
		try {
			myCtx.newRestfulGenericClient("http://foo").read(new UriDt("http://foo/Patient/1"));
			fail();
		} catch (FhirClientInappropriateForServerException e) {
			assertThat(e.toString(), containsString("The server at base URL \"http://foo/metadata\" returned a conformance statement indicating that it supports FHIR version \"3.0.1\" which corresponds to DSTU3, but this client is configured to use DSTU2_HL7ORG (via the FhirContext)"));
		}
	}

   @Test
   public void testServerReturnsRightVersionForDstu2() throws Exception {
     String appropriateFhirVersion = "1.0.2";
     assertThat(appropriateFhirVersion, is(FhirVersionEnum.DSTU2_HL7ORG.getFhirVersionString()));
     Conformance conf = new Conformance();
     conf.setFhirVersion(appropriateFhirVersion);
     String msg = myCtx.newXmlParser().encodeResourceToString(conf);

     ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);

     when(myHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
     when(myHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
     when(myHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(msg), Charset.forName("UTF-8")));

     when(myHttpClient.execute(capt.capture())).thenReturn(myHttpResponse);

     myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.ONCE);
     myCtx.newRestfulGenericClient("http://foo").forceConformanceCheck();
  }

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
