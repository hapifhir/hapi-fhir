package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
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
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.AdverseReaction;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
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
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	private static EncodingEnum ourLastEncoding;
	private static String ourLastResourceBody;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateTest.class);
	private static int ourPort;
	private static DiagnosticReportProvider ourReportProvider;

	private static Server ourServer;

	@Before()
	public void before() {
		ourLastResourceBody=null;
		ourLastEncoding=null;
	}
	
	@Test
	public void testCreate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue().toUpperCase(), StringContains.containsString("UTF-8"));

		assertThat(ourLastResourceBody, stringContainsInOrder("<Patient ", "<identifier>","<value value=\"001"));
		assertEquals(EncodingEnum.XML, ourLastEncoding);
	}

	@Test
	public void testCreateWithWrongContentTypeXml() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		String inputString = ourCtx.newJsonParser().encodeResourceToString(patient);
		ContentType inputCt = ContentType.create(Constants.CT_FHIR_XML, "UTF-8");
		httpPost.setEntity(new StringEntity(inputString, inputCt));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("Unexpected character"));
	}

	@Test
	public void testCreateWithWrongContentTypeJson() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		String inputString = ourCtx.newXmlParser().encodeResourceToString(patient);
		ContentType inputCt = ContentType.create(Constants.CT_FHIR_JSON, "UTF-8");
		httpPost.setEntity(new StringEntity(inputString, inputCt));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("Unexpected char"));
	}

	@Test
	public void testCreateById() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient/1234");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/1234/_history/002", status.getFirstHeader("location").getValue());

	}

	@Test
	public void testCreateCustomType() throws Exception {

		Observation obs = new Observation();
		obs.getIdentifier().setValue("001");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Observation");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(obs), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Observation/001/_history/002", status.getFirstHeader("location").getValue());

	}

	@Test
	public void testCreateJson() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");
		patient.addName().addFamily("line1\nline2");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Patient");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());

		ourLog.info(ourLastResourceBody);
		assertEquals("{\"resourceType\":\"Patient\",\"identifier\":[{\"value\":\"001\"},{\"value\":\"002\"}],\"name\":[{\"family\":[\"line1\\nline2\"]}]}", ourLastResourceBody);
	}

	@Test
	public void testCreateWithNoParsed() throws Exception {

		Organization org = new Organization();
		org.addIdentifier().setValue("001");
		org.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/Organization");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(org), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Organization/001", status.getFirstHeader("location").getValue());
		assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue(), StringContains.containsString("UTF-8"));

		assertThat(ourLastResourceBody, stringContainsInOrder("\"resourceType\":\"Organization\"", "\"identifier\"","\"value\":\"001"));
		assertEquals(EncodingEnum.JSON, ourLastEncoding);
		
	}

	@Test
	public void testCreateWithUnprocessableEntity() throws Exception {

		DiagnosticReport report = new DiagnosticReport();
		report.getIdentifier().setValue("001");

		HttpPost httpPost = new HttpPost("http://localhost:" + ourPort + "/DiagnosticReport");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(report), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(422, status.getStatusLine().getStatusCode());

		OperationOutcome outcome = ourCtx.newXmlParser().parseResource(OperationOutcome.class, new StringReader(responseContent));
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
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(
				patientProvider, ourReportProvider, new DummyAdverseReactionResourceProvider(), new CustomObservationProvider(), 
				new OrganizationProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}
	@ResourceDef(name = "Observation")
	public static class CustomObservation extends Observation {

		@Extension(url = "http://foo#string", definedLocally = false, isModifier = false)
		@Child(name = "string")
		private StringDt myString;

		public StringDt getString() {
			if (myString == null) {
				myString = new StringDt();
			}
			return myString;
		}

		public void setString(StringDt theString) {
			myString = theString;
		}
	}
	
	public static class CustomObservationProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam CustomObservation thePatient) {
			IdDt id = new IdDt(thePatient.getIdentifier().getValue().getValue());
			if (thePatient.getId().isEmpty() == false) {
				id = thePatient.getId();
			}
			return new MethodOutcome(id.withVersion("002"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

	}
	
	public static class DiagnosticReportProvider implements IResourceProvider {
		private TagList myLastTags;

		@Create()
		public MethodOutcome createDiagnosticReport(@ResourceParam DiagnosticReport thePatient) {
			OperationOutcome outcome = new OperationOutcome();
			outcome.addIssue().setDetails("FOOBAR");
			throw new UnprocessableEntityException(outcome);
		}

		public TagList getLastTags() {
			return myLastTags;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
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

	public static class OrganizationProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			ourLastResourceBody=theResourceBody;
			ourLastEncoding=theEncoding;
			
			return new MethodOutcome(new IdDt("001"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Organization.class;
		}

	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			IdDt id = new IdDt(thePatient.getIdentifier().get(0).getValue().getValue());
			if (thePatient.getId().isEmpty() == false) {
				id = thePatient.getId();
			}
			
			ourLastResourceBody=theResourceBody;
			ourLastEncoding=theEncoding;
			
			return new MethodOutcome(id.withVersion("002"));
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
