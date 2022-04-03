package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BundleTypeDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BundleTypeDstu2Test.class);
	private FhirContext ourCtx;
	private HttpClient ourHttpClient;

	private HttpResponse ourHttpResponse;

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


	@BeforeEach
	public void before() {
		ourCtx = FhirContext.forDstu2();

		ourHttpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ourCtx.getRestfulClientFactory().setHttpClient(ourHttpClient);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ourHttpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	@Test
	public void testTransaction() throws Exception {
		String retVal = ourCtx.newXmlParser().encodeResourceToString(new Bundle());

		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(ourHttpClient.execute(capt.capture())).thenReturn(ourHttpResponse);
		when(ourHttpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(ourHttpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_FHIR_XML + "; charset=UTF-8"));
		when(ourHttpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(retVal), Charset.forName("UTF-8")));

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("value");

		IGenericClient client = ourCtx.newRestfulGenericClient("http://foo");
		client.transaction().withResources(Arrays.asList((IBaseResource) p1)).encodedXml().execute();

		HttpUriRequest value = capt.getValue();

		assertTrue(value instanceof HttpPost, "Expected request of type POST on long params list");
		HttpPost post = (HttpPost) value;
		String body = IOUtils.toString(post.getEntity().getContent());
		IOUtils.closeQuietly(post.getEntity().getContent());
		ourLog.info(body);

		assertThat(body, Matchers.containsString("<type value=\"" + BundleTypeEnum.TRANSACTION.getCode()));
	}
	
	
	public static void main(String[] args) {
		
		FhirContext ctx = FhirContext.forDstu2();
		IGenericClient client = ctx.newRestfulGenericClient("http://54.165.58.158:8081/FHIRServer/fhir");
		client.registerInterceptor(new BearerTokenAuthInterceptor("AN3uCTC5B"));
		client.registerInterceptor(new LoggingInterceptor(true));
		Bundle result = client.search().forResource(Patient.class).where(Patient.NAME.matches().value("Alice")).returnBundle(Bundle.class).execute();
		
		System.out.println(result.getEntry().size());
		
	}
	
}
