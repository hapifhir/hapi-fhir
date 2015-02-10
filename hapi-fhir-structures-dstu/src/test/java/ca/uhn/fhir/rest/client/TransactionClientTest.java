package ca.uhn.fhir.rest.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.server.Constants;

public class TransactionClientTest {

	private FhirContext ctx;
	private HttpClient httpClient;
	private HttpResponse httpResponse;

	// atom-document-large.xml

	@Before
	public void before() {
		ctx = new FhirContext(Patient.class, Conformance.class);

		httpClient = mock(HttpClient.class, new ReturnsDeepStubs());
		ctx.getRestfulClientFactory().setHttpClient(httpClient);
		ctx.getRestfulClientFactory().setServerValidationModeEnum(ServerValidationModeEnum.NEVER);
		
		httpResponse = mock(HttpResponse.class, new ReturnsDeepStubs());
	}

	
	@Test
	public void testSimpleTransaction() throws Exception {
		Patient patient = new Patient();
		patient.setId(new IdDt("Patient/testPersistWithSimpleLinkP01"));
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP01");
		patient.addName().addFamily("Tester").addGiven("Joe");

		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/testPersistWithSimpleLinkP01"));

		List<IResource> resources = Arrays.asList((IResource)patient, obs);
		
		IClient client = ctx.newRestfulClient(IClient.class, "http://foo");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

		client.transaction(resources);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo", post.getURI().toString());

		Bundle bundle = ctx.newXmlParser().parseBundle(new InputStreamReader(post.getEntity().getContent()));
		ourLog.info(ctx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle));
		
		assertEquals(2, bundle.size());
		assertEquals("Patient/testPersistWithSimpleLinkP01", bundle.getEntries().get(0).getId().getValue());
		assertEquals("Patient/testPersistWithSimpleLinkP01", bundle.getEntries().get(0).getLinkSelf().getValue());
		assertEquals(null, bundle.getEntries().get(0).getLinkAlternate().getValue());

		assertTrue(bundle.getEntries().get(1).getId().isEmpty());

	}

	
	@Test
	public void testSimpleTransactionWithBundleParam() throws Exception {
		Patient patient = new Patient();
		patient.setId(new IdDt("Patient/testPersistWithSimpleLinkP01"));
		patient.addIdentifier("urn:system", "testPersistWithSimpleLinkP01");
		patient.addName().addFamily("Tester").addGiven("Joe");

		Observation obs = new Observation();
		obs.getName().addCoding().setSystem("urn:system").setCode("testPersistWithSimpleLinkO01");
		obs.setSubject(new ResourceReferenceDt("Patient/testPersistWithSimpleLinkP01"));

		Bundle transactionBundle = Bundle.withResources(Arrays.asList((IResource)patient, obs), ctx, "http://foo");
		
		IBundleClient client = ctx.newRestfulClient(IBundleClient.class, "http://foo");
		
		ArgumentCaptor<HttpUriRequest> capt = ArgumentCaptor.forClass(HttpUriRequest.class);
		when(httpClient.execute(capt.capture())).thenReturn(httpResponse);
		when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
		when(httpResponse.getEntity().getContentType()).thenReturn(new BasicHeader("content-type", Constants.CT_ATOM_XML + "; charset=UTF-8"));
		when(httpResponse.getEntity().getContent()).thenReturn(new ReaderInputStream(new StringReader(createBundle()), Charset.forName("UTF-8")));

		client.transaction(transactionBundle);

		assertEquals(HttpPost.class, capt.getValue().getClass());
		HttpPost post = (HttpPost) capt.getValue();
		assertEquals("http://foo", post.getURI().toString());

		Bundle bundle = ctx.newXmlParser().parseBundle(new InputStreamReader(post.getEntity().getContent()));
		ourLog.info(ctx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle));
		
		assertEquals(2, bundle.size());
		assertEquals("http://foo/Patient/testPersistWithSimpleLinkP01", bundle.getEntries().get(0).getId().getValue());
		assertEquals("http://foo/Patient/testPersistWithSimpleLinkP01", bundle.getEntries().get(0).getLinkSelf().getValue());
		assertEquals(null, bundle.getEntries().get(0).getLinkAlternate().getValue());

		assertTrue(bundle.getEntries().get(1).getId().isEmpty());

	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TransactionClientTest.class);
	private String createBundle() {
		return ctx.newXmlParser().encodeBundleToString(new Bundle());
	}
	
	private interface IClient extends IBasicClient {

		@Transaction
		public List<IResource> transaction(@TransactionParam List<IResource> theResources);

	
	}

	private interface IBundleClient extends IBasicClient {

		@Transaction
		public List<IResource> transaction(@TransactionParam Bundle theResources);

	
	}

}
