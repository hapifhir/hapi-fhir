package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Required;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.provider.ServerProfileProvider;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ResfulServerSelfReferenceTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResfulServerSelfReferenceTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx;

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);
		ourCtx = new FhirContext(Patient.class);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();
		ServerProfileProvider profProvider=new ServerProfileProvider(ourCtx);

		ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(new DummyRestfulServer(patientProvider,profProvider));
		proxyHandler.addServletWithMapping(servletHolder, "/fhir/context/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();


	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}


	@Test
	public void testSearchByParamIdentifier() throws Exception {

		String baseUri = "http://localhost:" + ourPort + "/fhir/context";
		String uri = baseUri + "/Patient?identifier=urn:hapitest:mrns%7C00001";
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse status = ourClient.execute(httpGet);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);

		assertEquals(1, bundle.getEntries().size());

		Patient patient = (Patient) bundle.getEntries().get(0).getResource();
		assertEquals("PatientOne", patient.getName().get(0).getGiven().get(0).getValue());

		assertEquals(uri, bundle.getLinkSelf().getValue());
		assertEquals(baseUri, bundle.getLinkBase().getValue());
	}



	
	public static class DummyRestfulServer extends RestfulServer {

		private static final long serialVersionUID = 1L;
		
		private Collection<IResourceProvider> myResourceProviders;

		public DummyRestfulServer(IResourceProvider... theResourceProviders) {
			myResourceProviders = Arrays.asList(theResourceProviders);
		}

		@Override
		public Collection<IResourceProvider> getResourceProviders() {
			return myResourceProviders;
		}

	    @Override
	    public ISecurityManager getSecurityManager() {
	        return null;
	    }

	}
	
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = new Patient();
				patient.addIdentifier();
				patient.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
				patient.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
				patient.getIdentifier().get(0).setValue("00001");
				patient.addName();
				patient.getName().get(0).addFamily("Test");
				patient.getName().get(0).addGiven("PatientOne");
				patient.getGender().setText("M");
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

		

		@Search()
		public Patient getPatient(@Required(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier) {
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
		@Read()
		public Patient getResourceById(@Read.IdParam IdDt theId) {
			return getIdToPatient().get(theId.getValue());
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}


	
}
