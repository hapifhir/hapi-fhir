package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
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
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class CreateTest {
	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateTest.class);
	private static int ourPort;
	private static DiagnosticReportProvider ourReportProvider;
	private static Server ourServer;

	

	@Test
	public void testCreate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());

	}

	
	@Test
	public void testCreateById() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/1234");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/1234/_history/002", status.getFirstHeader("location").getValue());

	}


	
	@Test
	public void testCreateJson() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(new FhirContext().newJsonParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());

	}

	@Test
	public void testCreateWithUnprocessableEntity() throws Exception {

		DiagnosticReport report = new DiagnosticReport();
		report.getIdentifier().setValue("001");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/DiagnosticReport");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(report), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());

		OperationOutcome outcome = new FhirContext().newXmlParser().parseResource(OperationOutcome.class, new StringReader(responseContent));
		assertEquals("FOOBAR", outcome.getIssueFirstRep().getDetails().getValue());

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ourReportProvider = new DiagnosticReportProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.setResourceProviders(patientProvider, ourReportProvider,  new DummyAdverseReactionResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DiagnosticReportProvider implements IResourceProvider {
		private TagList myLastTags;

		public TagList getLastTags() {
			return myLastTags;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

	
		@Create()
		public MethodOutcome createDiagnosticReport(@ResourceParam DiagnosticReport thePatient) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue().setDetails("FOOBAR");
			throw new UnprocessableEntityException(outcome);
		}

	}
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyAdverseReactionResourceProvider implements IResourceProvider {

		/*
		 * *********************
		 * NO NEW METHODS *********************
		 */

		@Create()
		public MethodOutcome create(@ResourceParam AdverseReaction thePatient) {
			IdDt id = new IdDt("Patient", thePatient.getIdentifier().get(0).getValue().getValue(), thePatient.getIdentifier().get(1).getValue().getValue());
			return new MethodOutcome(id);
		}

		@Search()
		public Collection<AdverseReaction> getAllResources() {
			ArrayList<AdverseReaction> retVal = new ArrayList<AdverseReaction>();

			AdverseReaction ar1 = new AdverseReaction();
			ar1.setId("1");
			retVal.add(ar1);

			AdverseReaction ar2 = new AdverseReaction();
			ar2.setId("2");
			retVal.add(ar2);

			return retVal;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return AdverseReaction.class;
		}

	}


	
	
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			IdDt id = new IdDt(thePatient.getIdentifier().get(0).getValue().getValue());
			if (thePatient.getId().isEmpty()==false) {
				id=thePatient.getId();
			}
			return new MethodOutcome(id.withVersion("002"));
		}


	}

}
