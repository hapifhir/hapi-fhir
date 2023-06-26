package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.History;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringStartsWith;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlainProviderR4Test {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PlainProviderR4Test.class);
	private CloseableHttpClient myClient;
	private int myPort;
	private RestfulServer myRestfulServer;
	private Server myServer;

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@BeforeEach
	public void before() throws Exception {
		myServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder();
		myRestfulServer = new RestfulServer(ourCtx);
		myRestfulServer.setDefaultResponseEncoding(EncodingEnum.XML);
		servletHolder.setServlet(myRestfulServer);
		proxyHandler.addServletWithMapping(servletHolder, "/fhir/context/*");
		myServer.setHandler(proxyHandler);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		
		builder.setConnectionManager(connectionManager);
		myClient = builder.build();

	}

	@Test
	public void testGlobalHistory() throws Exception {
		GlobalHistoryProvider provider = new GlobalHistoryProvider();
		myRestfulServer.setProviders(provider);
		JettyUtil.startServer(myServer);
        myPort = JettyUtil.getPortForStartedServer(myServer);

		String baseUri = "http://localhost:" + myPort + "/fhir/context";
		HttpResponse status = myClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02&_count=12"));

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());
		
		assertThat(provider.myLastSince.getValueAsString(), StringStartsWith.startsWith("2012-01-02T00:01:02"));
		assertThat(provider.myLastCount.getValueAsString(), IsEqual.equalTo("12"));

		status = myClient.execute(new HttpGet(baseUri + "/_history?&_count=12"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());
		assertNull(provider.myLastSince);
		assertThat(provider.myLastCount.getValueAsString(), IsEqual.equalTo("12"));
		
		status =myClient.execute(new HttpGet(baseUri + "/_history?_since=2012-01-02T00%3A01%3A02"));
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());
		assertThat(provider.myLastSince.getValueAsString(), StringStartsWith.startsWith("2012-01-02T00:01:02"));
		assertNull(provider.myLastCount);
	}

	@Test
	public void testGlobalHistoryNoParams() throws Exception {
		GlobalHistoryProvider provider = new GlobalHistoryProvider();
		myRestfulServer.setProviders(provider);
		JettyUtil.startServer(myServer);
        myPort = JettyUtil.getPortForStartedServer(myServer);

		String baseUri = "http://localhost:" + myPort + "/fhir/context";
		CloseableHttpResponse status = myClient.execute(new HttpGet(baseUri + "/_history"));
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);
		assertEquals(3, bundle.getEntry().size());
		assertNull(provider.myLastSince);
		assertNull(provider.myLastCount);
		
	}

	@Test
	public void testSearchByParamIdentifier() throws Exception {
		myRestfulServer.setProviders(new SearchProvider());
		JettyUtil.startServer(myServer);
        myPort = JettyUtil.getPortForStartedServer(myServer);

		String baseUri = "http://localhost:" + myPort + "/fhir/context";
		String uri = baseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
		HttpGet httpGet = new HttpGet(uri);
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent());
			IOUtils.closeQuietly(status.getEntity().getContent());
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, responseContent);

			assertEquals(1, bundle.getEntry().size());

			Patient patient = (Patient) bundle.getEntry().get(0).getResource();
			assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

			assertEquals(uri.replace(":hapitest:", "%3Ahapitest%3A"), bundle.getLink("self").getUrl());
		}

	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static Organization createOrganization() {
		Organization retVal = new Organization();
		retVal.setId("1");
		retVal.addIdentifier();
		retVal.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
		retVal.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
		retVal.getIdentifier().get(0).setValue("00001");
		retVal.setName("Test Org");
		return retVal;
	}

	private static Patient createPatient() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.addIdentifier();
		patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
		patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
		patient.getIdentifier().get(0).setValue("00001");
		patient.addName();
		patient.getName().get(0).setFamily("Test");
		patient.getName().get(0).addGiven("PatientOne");
		patient.getGenderElement().setValueAsString("male");
		return patient;
	}

	public static class GlobalHistoryProvider {

		private IntegerType myLastCount;
		private InstantType myLastSince;

		@History
		public List<IBaseResource> getGlobalHistory(@Since InstantType theSince, @Count IntegerType theCount) {
			myLastSince = theSince;
			myLastCount = theCount;
			ArrayList<IBaseResource> retVal = new ArrayList<>();

			Resource p = createPatient();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("A");
			p.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T01:00:01");
			retVal.add(p);

			p = createPatient();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("B");
			p.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T01:00:03");
			retVal.add(p);

			p = createOrganization();
			p.setId(new IdType("1"));
			p.getMeta().setVersionId("A");
			p.getMeta().getLastUpdatedElement().setValueAsString("2013-01-01T01:00:01");
			retVal.add(p);

			return retVal;
		}

	}


	public static class SearchProvider {

		@Search(type = Patient.class)
		public Patient findPatient(@RequiredParam(name = Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			for (Patient next : getIdToPatient().values()) {
				for (Identifier nextId : next.getIdentifier()) {
					if (nextId.getSystem().equals(theIdentifier.getSystem()) && nextId.getValue().equals(theIdentifier.getValue())) {
						return next;
					}
				}
			}
			return null;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<>();
			{
				Patient patient = createPatient();
				idToPatient.put("1", patient);
			}
			{
				Patient patient = new Patient();
				patient.getIdentifier().add(new Identifier());
				patient.getIdentifier().get(0).setUse(Identifier.IdentifierUse.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00002");
				patient.getName().add(new HumanName());
				patient.getName().get(0).setFamily("Test");
				patient.getName().get(0).addGiven("PatientTwo");
				patient.getGenderElement().setValueAsString("female");
				idToPatient.put("2", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *            The resource identity
		 * @return The resource
		 */
		@Read(type = Patient.class)
		public Patient getPatientById(@IdParam IdType theId) {
			return getIdToPatient().get(theId.getValue());
		}

	}

}
