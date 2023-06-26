package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.DateUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadR4Test.class);
	private static CloseableHttpClient ourClient;
	private final FhirContext myCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx);
	private int myPort;

	@BeforeEach
	public void before() {
		myPort = myRestfulServerExtension.getPort();
	}

	@Test
	public void testRead() throws Exception {
		myRestfulServerExtension.getRestfulServer().registerProvider(new PatientProvider());


		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_format=xml&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(null, status.getFirstHeader(Constants.HEADER_LOCATION));
			assertEquals("http://localhost:" + myPort + "/Patient/2/_history/2", status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());

			assertThat(responseContent, stringContainsInOrder(
				"<Patient xmlns=\"http://hl7.org/fhir\">",
				" <id value=\"2\"/>",
				" <meta>",
				"  <profile value=\"http://example.com/StructureDefinition/patient_with_extensions\"/>",
				" </meta>",
				" <modifierExtension url=\"http://example.com/ext/date\">",
				"  <valueDate value=\"2011-01-01\"/>",
				" </modifierExtension>",
				"</Patient>"));
		}
	}


	@Test
	public void testReadUsingPlainProvider() throws Exception {
		myRestfulServerExtension.getRestfulServer().registerProvider(new PlainGenericPatientProvider());

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_format=xml&_pretty=true");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {

			String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response was:\n{}", responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals(null, status.getFirstHeader(Constants.HEADER_LOCATION));
			assertEquals("http://localhost:" + myPort + "/Patient/2/_history/2", status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION).getValue());

			assertThat(responseContent, stringContainsInOrder(
				"<Patient xmlns=\"http://hl7.org/fhir\">",
				" <id value=\"2\"/>",
				" <meta>",
				"  <profile value=\"http://example.com/StructureDefinition/patient_with_extensions\"/>",
				" </meta>",
				" <modifierExtension url=\"http://example.com/ext/date\">",
				"  <valueDate value=\"2011-01-01\"/>",
				" </modifierExtension>",
				"</Patient>"));
		}
	}

	@Test
	public void testInvalidQueryParamsInRead() throws Exception {
		myRestfulServerExtension.getRestfulServer().registerProvider(new PatientProvider());

		CloseableHttpResponse status;
		HttpGet httpGet;

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_contained=both&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\""+ Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_contained]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_containedType=contained&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\""+ Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_containedType]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_count=10&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"" + Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_count]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_include=Patient:organization&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\""+ Msg.code(384)+ "Invalid query parameter(s) for this request: &quot;[_include]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_revinclude=Provenance:target&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\""+ Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_revinclude]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_sort=family&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\""+ Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_sort]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}

		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2?_total=accurate&_format=xml&_pretty=true");
		status = ourClient.execute(httpGet);
		try (InputStream inputStream = status.getEntity().getContent()) {
			assertEquals(400, status.getStatusLine().getStatusCode());

			String responseContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			assertThat(responseContent, stringContainsInOrder(
				"<OperationOutcome xmlns=\"http://hl7.org/fhir\">",
				" <issue>",
				"  <severity value=\"error\"/>",
				"  <code value=\"processing\"/>",
				"  <diagnostics value=\"" + Msg.code(384) + "Invalid query parameter(s) for this request: &quot;[_total]&quot;\"/>",
				" </issue>",
				"</OperationOutcome>"
			));
		}
	}

	@Test
	public void testIfModifiedSince() throws Exception {
		myRestfulServerExtension.getRestfulServer().registerProvider(new PatientProvider());

		HttpGet httpGet;

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it hasn't changed after the later time of 2012-01-01T13:00:00Z
		// so we expect a 304 (Not Modified)
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T13:00:00Z").getValue()));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(304, status.getStatusLine().getStatusCode());
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it hasn't changed after the same time of 2012-01-01T12:12:12Z
		// so we expect a 304 (Not Modified)
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T12:12:12Z").getValue()));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(304, status.getStatusLine().getStatusCode());
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it has changed after the earlier time of 2012-01-01T10:00:00Z
		// so we expect a 200
		httpGet = new HttpGet("http://localhost:" + myPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T10:00:00Z").getValue()));
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
		}

	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T12:12:12Z");
			p0.setId(theIdParam);
			if (theIdParam.hasVersionIdPart() == false) {
				p0.setIdElement(p0.getIdElement().withVersion("2"));
			}
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

	}

	public static class PlainGenericPatientProvider {

		@Read(version = true, typeName = "Patient")
		public IBaseResource read(@IdParam IIdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.getMeta().getLastUpdatedElement().setValueAsString("2012-01-01T12:12:12Z");
			p0.setId(theIdParam);
			if (theIdParam.hasVersionIdPart() == false) {
				p0.setIdElement(p0.getIdElement().withVersion("2"));
			}
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

	}


	@BeforeAll
	public static void beforeClass() throws Exception {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();
	}

	@AfterAll
	public static void afterClass() throws IOException {
		ourClient.close();
	}
}
