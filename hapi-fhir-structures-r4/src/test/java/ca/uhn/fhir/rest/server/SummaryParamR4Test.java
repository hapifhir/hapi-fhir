package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SummaryParamR4Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static SummaryEnum ourLastSummary;
	private static List<SummaryEnum> ourLastSummaryList;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SummaryParamR4Test.class);
	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLastSummary = null;
		ourLastSummaryList = null;
	}
	@Test
	public void testReadSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_summary=" + SummaryEnum.DATA.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, (containsString("<Patien")));
		assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
		assertThat(responseContent, (containsString("family")));
		assertThat(responseContent, (containsString("maritalStatus")));
		assertEquals(SummaryEnum.DATA, ourLastSummary);
	}



	@Test
	public void testReadSummaryText() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_summary=" + SummaryEnum.TEXT.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_HTML_WITH_UTF8.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, not(containsString("<Medic")));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">THE DIV</div>", responseContent);
		assertThat(responseContent, not(containsString("efer")));
		assertEquals(SummaryEnum.TEXT, ourLastSummary);
	}

	@Test
	public void testReadSummaryTextWithMandatory() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/MedicationRequest/1?_summary=" + SummaryEnum.TEXT.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_HTML_WITH_UTF8.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, not(containsString("<Patien")));
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">TEXT</div>", responseContent);
		assertThat(responseContent, not(containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
	}

	@Test
	public void testReadSummaryTrue() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/1?_summary=" + SummaryEnum.TRUE.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals(Constants.CT_FHIR_XML_NEW + Constants.CHARSET_UTF8_CTSUFFIX.replace(" ", "").toLowerCase(), status.getEntity().getContentType().getValue().replace(" ", "").replace("UTF", "utf"));
		assertThat(responseContent, not(containsString("<Bundle")));
		assertThat(responseContent, (containsString("<Patien")));
		assertThat(responseContent, not(containsString("<div>THE DIV</div>")));
		assertThat(responseContent, (containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertEquals(SummaryEnum.TRUE, ourLastSummary);
	}

	@Test
	public void testSearchSummaryCount() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true&_summary=" + SummaryEnum.COUNT.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, not(containsString("entry")));
		assertThat(responseContent, not(containsString("THE DIV")));
		assertThat(responseContent, not(containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertEquals(SummaryEnum.COUNT, ourLastSummary);
	}

	@Test
	public void testSearchSummaryCountAndData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true&_summary=" + SummaryEnum.COUNT.getCode() + "," + SummaryEnum.DATA.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, (containsString("entry")));
		assertThat(responseContent, not(containsString("THE DIV")));
		assertThat(responseContent, (containsString("family")));
		assertThat(responseContent, (containsString("maritalStatus")));
	}

	@Test
	public void testSearchSummaryData() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_summary=" + SummaryEnum.DATA.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient"));
		assertThat(responseContent, not(containsString("THE DIV")));
		assertThat(responseContent, containsString("family"));
		assertThat(responseContent, containsString("maritalStatus"));
		assertEquals(SummaryEnum.DATA, ourLastSummary);
	}

	@Test
	public void testSearchSummaryFalse() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_summary=false");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient"));
		assertThat(responseContent, containsString("THE DIV"));
		assertThat(responseContent, containsString("family"));
		assertThat(responseContent, containsString("maritalStatus"));
		assertEquals(SummaryEnum.FALSE, ourLastSummary);
	}

	@Test
	public void testSearchSummaryText() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_summary=" + SummaryEnum.TEXT.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, (containsString("entry")));
		assertThat(responseContent, (containsString("THE DIV")));
		assertThat(responseContent, not(containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertEquals(SummaryEnum.TEXT, ourLastSummary);
	}

	@Test
	public void testSearchSummaryTextWithMandatory() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/MedicationRequest?_summary=" + SummaryEnum.TEXT.getCode() + "&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, (containsString("entry")));
		assertThat(responseContent, (containsString(">TEXT<")));
		assertThat(responseContent, (containsString("Medication/123")));
		assertThat(responseContent, not(containsStringIgnoringCase("note")));
	}

	@Test
	public void testSearchSummaryTextMulti() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=multi&_summary=" + SummaryEnum.TEXT.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, (containsString("<total value=\"1\"/>")));
		assertThat(responseContent, (containsString("entry")));
		assertThat(responseContent, (containsString("THE DIV")));
		assertThat(responseContent, not(containsString("family")));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertThat(ourLastSummaryList, contains(SummaryEnum.TEXT));
	}

	@Test
	public void testSearchSummaryTrue() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_summary=" + SummaryEnum.TRUE.getCode());
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("<Patient"));
		assertThat(responseContent, not(containsString("THE DIV")));
		assertThat(responseContent, containsString("family"));
		assertThat(responseContent, not(containsString("maritalStatus")));
		assertEquals(SummaryEnum.TRUE, ourLastSummary);
	}

	@Test
	public void testSearchSummaryWithTextAndOthers() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_summary=text&_summary=data");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertEquals(400, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("Can not combine _summary=text with other values for _summary"));
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setResourceProviders(new DummyPatientResourceProvider(), new DummyMedicationRequestProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyMedicationRequestProvider implements IResourceProvider{

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return MedicationRequest.class;
		}
		
		@Read
		public MedicationRequest read(@IdParam IdType theId) {
			MedicationRequest retVal = new MedicationRequest();
			retVal.getText().setDivAsString("<div>TEXT</div>");
			retVal.addNote().setText("NOTE");
			retVal.setMedication(new Reference("Medication/123"));
			retVal.setId(theId);
			return retVal;
		}
		
		@Search
		public List<MedicationRequest> read() {
			return Arrays.asList(read(new IdType("999")));
		}

	}
	
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId, SummaryEnum theSummary) {
			ourLastSummary = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

		@Search(queryName = "multi")
		public Patient search(List<SummaryEnum> theSummary) {
			ourLastSummaryList = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}
		
		@Search()
		public Patient search(SummaryEnum theSummary) {
			ourLastSummary = theSummary;
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getText().setDivAsString("<div>THE DIV</div>");
			patient.addName().setFamily("FAMILY");
			patient.getMaritalStatus().addCoding().setCode("D");
			return patient;
		}

	}

}
