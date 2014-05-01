package ca.uhn.fhir.rest.server;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
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
import ca.uhn.fhir.rest.server.tester.PublicTesterServlet;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

public class TesterTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TesterTest.class);
	private int myPort;
	private Server myServer;
	private FhirContext myCtx;
	private RestfulServer myRestfulServer;

	@Before
	public void before() throws Exception {
		myPort = RandomServerPortProvider.findFreePort();
		myPort = 8888;
		myServer = new Server(myPort);
		myCtx = new FhirContext(Patient.class);
		myRestfulServer = new RestfulServer();
		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		PublicTesterServlet testerServlet = new PublicTesterServlet();
		testerServlet.setServerBase("http://localhost:" + myPort + "/fhir/context");
//		testerServlet.setServerBase("http://fhir.healthintersections.com.au/open");
		ServletHolder handler = new ServletHolder();
		handler.setServlet(testerServlet);
		proxyHandler.addServlet(handler, "/fhir/tester/*");
		
		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(myRestfulServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");
		
		myServer.setHandler(proxyHandler);

	}

	@After
	public void after() throws Exception {
		myServer.stop();
	}

	@Test
	public void testTester() throws Exception {
		if (true) return;
		
		myRestfulServer.setProviders(new SearchProvider(), new GlobalHistoryProvider());
		myServer.start();

		Thread.sleep(9999999L);
		
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
		public Patient findPatient(
				@Description(shortDefinition="The patient's identifier (MRN or other card number). Example system 'urn:hapitest:mrns', example MRN '00002'")
				@RequiredParam(name = Patient.SP_IDENTIFIER) IdentifierDt theIdentifier
				) {
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
		retVal.addIdentifier();
		retVal.getIdentifier().get(0).setUse(IdentifierUseEnum.OFFICIAL);
		retVal.getIdentifier().get(0).setSystem(new UriDt("urn:hapitest:mrns"));
		retVal.getIdentifier().get(0).setValue("00001");
		retVal.getName().setValue("Test Org");
		return retVal;
	}

}
