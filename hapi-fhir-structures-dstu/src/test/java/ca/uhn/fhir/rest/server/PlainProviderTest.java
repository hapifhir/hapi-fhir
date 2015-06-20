package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringStartsWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.util.PortUtil;

public class PlainProviderTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PlainProviderTest.class);
	private int myPort;
	private Server myServer;
	private CloseableHttpClient myClient;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private RestfulServer myRestfulServer;

	@Before
	public void before() throws Exception {
		myPort = PortUtil.findFreePort();
		myServer = new Server(myPort);

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder();
		myRestfulServer = new RestfulServer(ourCtx);
		servletHolder.setServlet(myRestfulServer);
		proxyHandler.addServletWithMapping(servletHolder, "/fhir/context/*");
		myServer.setHandler(proxyHandler);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		
		builder.setConnectionManager(connectionManager);
		myClient = builder.build();

	}

	@After
	public void after() throws Exception {
		myServer.stop();
	}

	@Test
	public void testSearchByParamIdentifier() throws Exception {
		myRestfulServer.setProviders(new SearchProvider());
		myServer.start();

		String baseUri = "http://localhost:" + myPort + "/fhir/context";
		String uri = baseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse status = myClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		assertEquals(uri.replace(":hapitest:", "%3Ahapitest%3A"), bundle.getLinkSelf().getValue());
		assertEquals(baseUri, bundle.getLinkBase().getValue());
		
		httpGet.releaseConnection();
	}
	
	@Test
	public void testGlobalHistory() throws Exception {
		GlobalHistoryProvider provider = new GlobalHistoryProvider();
		myRestfulServer.setProviders(provider);
		myServer.start();

		String baseUri = "http://localhost:" + myPort + "/fhir/context";
		HttpResponse status = myClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02&_count=12"));

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.getEntries().size());
		
		assertThat(provider.myLastSince.getValueAsString(), StringStartsWith.startsWith("2012-01-02T00:01:02"));
		assertThat(provider.myLastCount.getValueAsString(), IsEqual.equalTo("12"));

		status = myClient.execute(new HttpGet(baseUri + "/_history?&_count=12"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.getEntries().size());
		assertNull(provider.myLastSince.getValueAsString());
		assertThat(provider.myLastCount.getValueAsString(), IsEqual.equalTo("12"));
		
		status =myClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.getEntries().size());
		assertThat(provider.myLastSince.getValueAsString(), StringStartsWith.startsWith("2012-01-02T00:01:02"));
		assertNull(provider.myLastCount.getValueAsString());

		status =myClient.execute(new HttpGet(baseUri + "/_history"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(3, bundle.getEntries().size());
		assertNull(provider.myLastSince.getValueAsString());
		assertNull(provider.myLastCount.getValueAsString());
		
	}
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class SearchProvider {

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new IdentifierDt());
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanNameDt());
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getGender().setText("F");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		@Search(type = Patient.class)
		public Patient findPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (IdentifierDt nextId : next.getIdentifier()) {
					if (nextId.matchesSystemAndValue(theIdentifier)) {
						return next;
					}
				}
			}
			return null;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read(type = Patient.class)
		public Patient getPatientById(@IdParam IdDt theId) {
			return getIdToPatient().get(theId.getValue());
		}

	}

	public static class GlobalHistoryProvider {

		private InstantDt myLastSince;
		private IntegerDt myLastCount;

		@History
		public List<IResource> getGlobalHistory(@Since InstantDt theSince, @Count IntegerDt theCount) {
			myLastSince = theSince;
			myLastCount = theCount;
			ArrayList<IResource> retVal = new ArrayList<IResource>();

			IResource p = createPatient();
			p.setId(new IdDt("1"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, new IdDt("A"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new InstantDt("2012-01-01T00:00:01"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt("2012-01-01T01:00:01"));
			retVal.add(p);

			p = createPatient();
			p.setId(new IdDt("1"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, new IdDt("B"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new InstantDt("2012-01-01T00:00:01"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt("2012-01-01T01:00:03"));
			retVal.add(p);

			p = createOrganization();
			p.setId(new IdDt("1"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.VERSION_ID, new IdDt("A"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.PUBLISHED, new InstantDt("2013-01-01T00:00:01"));
			p.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, new InstantDt("2013-01-01T01:00:01"));
			retVal.add(p);

			return retVal;
		}

	}

	private static Patient createPatient() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.addIdentifier();
		patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
		patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
		patient.getIdentifier().get(0).setValue("00001");
		patient.addName();
		patient.getName().get(0).addFamily("Test");
		patient.getName().get(0).addGiven("PatientOne");
		patient.getGender().setText("M");
		return patient;
	}

	private static Organization createOrganization() {
		Organization retVal = new Organization();
		retVal.setId("1");
		retVal.addIdentifier();
		retVal.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
		retVal.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
		retVal.getIdentifier().get(0).setValue("00001");
		retVal.getName().setValue("Test Org");
		return retVal;
	}

}
