package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
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
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.testutil.RandomServerPortProvider;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class UpdateTest {
	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UpdateTest.class);
	private static int ourPort;
	private static DiagnosticReportProvider ourReportProvider;
	private static Server ourServer;

	@Test
	public void testUpdate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/Patient/001");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		OperationOutcome oo = new FhirContext().newXmlParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("OODETAILS", oo.getIssueFirstRep().getDetails().getValue());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());

	}
	
	@Test
	public void testUpdateMethodReturnsInvalidId() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/AAAAAA");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(500, status.getStatusLine().getStatusCode());

	}

	@Test
	public void testUpdateNoResponse() throws Exception {

		DiagnosticReport dr = new DiagnosticReport();
		dr.addCodedDiagnosis().addCoding().setCode("AAA");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/DiagnosticReport/001/_history/002", status.getFirstHeader("location").getValue());

		IOUtils.closeQuietly(status.getEntity().getContent());
	}

	@Test
	public void testUpdateWithTagMultiple() throws Exception {

		DiagnosticReport dr = new DiagnosticReport();
		dr.addCodedDiagnosis().addCoding().setCode("AAA");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog, Cat");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		assertEquals(2, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("Dog"), ourReportProvider.getLastTags().get(0));
		assertEquals(new Tag("Cat"), ourReportProvider.getLastTags().get(1));
		IOUtils.closeQuietly(status.getEntity().getContent());

		httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog; label=\"aa\", Cat; label=\"bb\"");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		status = ourClient.execute(httpPost);
		assertEquals(2, ourReportProvider.getLastTags().size());
		assertEquals(new Tag((String) null, "Dog", "aa"), ourReportProvider.getLastTags().get(0));
		assertEquals(new Tag((String) null, "Cat", "bb"), ourReportProvider.getLastTags().get(1));
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	@Test
	public void testUpdateWithTagSimple() throws Exception {

		DiagnosticReport dr = new DiagnosticReport();
		dr.addCodedDiagnosis().addCoding().setCode("AAA");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		assertEquals(1, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("Dog"), ourReportProvider.getLastTags().get(0));
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	@Test
	public void testUpdateWithTagWithScheme() throws Exception {

		DiagnosticReport dr = new DiagnosticReport();
		dr.addCodedDiagnosis().addCoding().setCode("AAA");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog; scheme=\"http://foo\"");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		assertEquals(1, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("http://foo", "Dog", null), ourReportProvider.getLastTags().get(0));
		IOUtils.closeQuietly(status.getEntity().getContent());

		httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog; scheme=\"http://foo\";");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		ourClient.execute(httpPost);
		assertEquals(1, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("http://foo", "Dog", null), ourReportProvider.getLastTags().get(0));
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	@Test
	public void testUpdateWithTagWithSchemeAndLabel() throws Exception {

		DiagnosticReport dr = new DiagnosticReport();
		dr.addCodedDiagnosis().addCoding().setCode("AAA");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog; scheme=\"http://foo\"; label=\"aaaa\"");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		CloseableHttpResponse status = ourClient.execute(httpPost);
		assertEquals(1, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("http://foo", "Dog", "aaaa"), ourReportProvider.getLastTags().get(0));
		IOUtils.closeQuietly(status.getEntity().getContent());

		httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.addHeader("Category", "Dog; scheme=\"http://foo\"; label=\"aaaa\";   ");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(dr), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));
		status=ourClient.execute(httpPost);
		assertEquals(1, ourReportProvider.getLastTags().size());
		assertEquals(new Tag("http://foo", "Dog", "aaaa"), ourReportProvider.getLastTags().get(0));
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	@Test
	public void testUpdateWithVersion() throws Exception {

		DiagnosticReport patient = new DiagnosticReport();
		patient.getIdentifier().setValue("001");

		HttpPut httpPut = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPut.addHeader("Content-Location", "/DiagnosticReport/001/_history/004");
		httpPut.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPut);

		// String responseContent =
		// IOUtils.toString(status.getEntity().getContent());
		// ourLog.info("Response was:\n{}", responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("http://localhost:" + ourPort + "/DiagnosticReport/001/_history/002", status.getFirstHeader("Location").getValue());
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	@Test()
	public void testUpdateWithVersionBadContentLocationHeader() throws Exception {

		DiagnosticReport patient = new DiagnosticReport();
		patient.getIdentifier().setValue("001");

		HttpPut httpPut = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPut.addHeader("Content-Location", "/Patient/001/_history/002");
		httpPut.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		CloseableHttpResponse status = ourClient.execute(httpPut);
		assertEquals(400, status.getStatusLine().getStatusCode());
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info("Response was:\n{}", responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());

	}

	public void testUpdateWrongResourceType() throws Exception {

		// TODO: this method sends in the wrong resource type vs. the URL so it
		// should
		// give a useful error message (and then make this unit test actually
		// run)
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPut httpPost = new HttpPut("http://localhost:" + ourPort + "/DiagnosticReport/001");
		httpPost.setEntity(new StringEntity(new FhirContext().newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		ourClient.execute(httpPost);
		fail();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(ourPort);

		PatientProvider patientProvider = new PatientProvider();

		ourReportProvider = new DiagnosticReportProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer();
		servlet.setResourceProviders(patientProvider, ourReportProvider, new ObservationProvider());
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

	
		@Update()
		public MethodOutcome updateDiagnosticReportWithVersionAndNoResponse(@IdParam IdDt theId, @ResourceParam DiagnosticReport theDr) {
			IdDt id = theId;
			
			if (theId.getValue().contains("AAAAAA")) {
				IdDt id2 = new IdDt(id.getIdPart(), "002");
				return new MethodOutcome(id2); // this is invalid
			}
			
			myLastTags = (TagList) theDr.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
			return new MethodOutcome(new IdDt("DiagnosticReport", id.getIdPart(), "002"));
		}

	}

	public static class ObservationProvider implements IResourceProvider{

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}
		
		@Update()
		public MethodOutcome updateDiagnosticReportWithVersion(@IdParam IdDt theId, @ResourceParam DiagnosticOrder thePatient) {
			/*
			 * TODO: THIS METHOD IS NOT USED. It's the wrong type (DiagnosticOrder), so it should cause an exception on startup. Also we should detect if there are multiple resource params on an
			 * update/create/etc method
			 */
			IdDt id = theId;
			return new MethodOutcome(id);
		}
	}
	
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		

		@Update()
		public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient thePatient) {
			IdDt id = theId.withVersion(thePatient.getIdentifierFirstRep().getValue().getValue());
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDetails("OODETAILS");
			return new MethodOutcome(id,oo);
		}

	}

}
