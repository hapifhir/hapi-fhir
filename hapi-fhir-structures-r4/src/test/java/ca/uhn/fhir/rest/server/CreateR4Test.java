package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateR4Test.class);
	public static OperationOutcome ourReturnOo;
	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProviderCreate())
		 .registerProvider(new PatientProviderRead())
		 .registerProvider(new PatientProviderSearch())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.JSON)
		 .withServer(s->s.setDefaultPreferReturn(RestfulServer.DEFAULT_PREFER_RETURN))
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourReturnOo = null;
	}

	@Test
	public void testCreateIgnoresIdInResourceBody() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post("{\"resourceType\":\"Patient\", \"id\":\"999\", \"status\":\"active\"}", "application/fhir+json; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);

		assertEquals(1, response.getHeaders("Location").size());
		assertEquals(1, response.getHeaders("Content-Location").size());
		assertEquals(ourServer.getBaseUrl() + "/Patient/1", response.getHeader("Location"));

	}

	@Test
	public void testCreateFailsIfNoContentTypeProvided() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").method("POST", "{\"resourceType\":\"Patient\", \"id\":\"999\", \"status\":\"active\"}".getBytes(StandardCharsets.UTF_8), null).assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("No Content-Type header was provided in the request. This is required for \\\"CREATE\\\" operation");
	}

	/**
	 * #472
	 */
	@Test
	public void testCreateReturnsLocationHeader() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post("{\"resourceType\":\"Patient\", \"status\":\"active\"}", "application/fhir+json; charset=utf-8");
		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);

		assertEquals(1, response.getHeaders("Location").size());
		assertEquals(1, response.getHeaders("Content-Location").size());
		assertEquals(ourServer.getBaseUrl() + "/Patient/1", response.getHeader("Location"));

	}

	@Test
	public void testCreateReturnsOperationOutcome() throws Exception {
		ourReturnOo = new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setDiagnostics("DIAG"));

		String responseContent = ourServer.fhirRequest("/Patient").withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.post("{\"resourceType\":\"Patient\", \"status\":\"active\"}", "application/fhir+json; charset=utf-8").assertStatus(201).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("DIAG");
	}

	@Test
	public void testCreateReturnsRepresentation() throws Exception {
		ourReturnOo = new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setDiagnostics("DIAG"));
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"gender\":\"male\"}";

		String responseContent = ourServer.fhirRequest("/Patient").post("{\"resourceType\":\"Patient\", \"gender\":\"male\"}", "application/fhir+json; charset=utf-8").assertStatus(201).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(expectedResponseContent, responseContent);
	}

	@Test
	public void testCreateWithIncorrectContent1() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").post("{\"foo\":\"bar\"}", "application/xml+fhir; charset=utf-8").assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"");
		assertThat(responseContent).contains("Failed to parse request body as XML resource.");

	}

	@Test
	public void testCreateWithIncorrectContent2() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").post("{\"foo\":\"bar\"}", "application/fhir+xml; charset=utf-8").assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"");
		assertThat(responseContent).contains("Failed to parse request body as XML resource.");

	}

	@Test
	public void testCreateWithIncorrectContent3() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").post("{\"foo\":\"bar\"}", "application/fhir+json; charset=utf-8").assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("Failed to parse request body as JSON resource.");

	}

	/**
	 * #342
	 */
	@Test
	public void testCreateWithInvalidContent() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").post("FOO", "application/xml+fhir; charset=utf-8").assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"");
		assertThat(responseContent).contains(Msg.code(450) + "Failed to parse request body as XML resource. Error was: " + Msg.code(1852) + "Failed to parse XML content: Unexpected character 'F'");

	}


	@Test
	public void testCreatePreferDefaultRepresentation() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(body, "application/fhir+json; charset=utf-8").assertStatus(201);
		assertEquals("application/fhir+json;charset=utf-8", response.getHeader(Constants.HEADER_CONTENT_TYPE));

		String responseContent = response.getBody();
		ourLog.info("Response was:\n{}", responseContent);
		assertThat(responseContent).contains("\"resourceType\":\"Patient\"");

	}

	@Test
	public void testCreatePreferDefaultOperationOutcome() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);

		ourServer.getRestfulServer().setDefaultPreferReturn(PreferReturnEnum.OPERATION_OUTCOME);
		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(body, "application/fhir+json; charset=utf-8").assertStatus(201);
		assertEquals("application/fhir+json;charset=utf-8", response.getHeader(Constants.HEADER_CONTENT_TYPE));

		String responseContent = response.getBody();
		ourLog.info("Response was:\n{}", responseContent);
		assertThat(responseContent).contains("\"resourceType\":\"OperationOutcome\"");


	}

	@Test
	public void testCreatePreferDefaultMinimal() throws Exception {
		ourReturnOo = new OperationOutcome();
		ourReturnOo.addIssue().setDiagnostics("FOO");

		Patient p = new Patient();
		p.setActive(true);
		String body = ourCtx.newJsonParser().encodeResourceToString(p);

		ourServer.getRestfulServer().setDefaultPreferReturn(PreferReturnEnum.MINIMAL);
		HttpTestResponse response = ourServer.fhirRequest("/Patient").post(body, "application/fhir+json; charset=utf-8").assertStatus(201);
		assertNull(response.getHeader(Constants.HEADER_CONTENT_TYPE));

		String responseContent = response.getBody();
		assertThat(responseContent).isNullOrEmpty();

	}

	@Test
	public void testSearch() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient?_format=xml&_pretty=true").get().assertStatus(200).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		//@formatter:off
		assertThat(responseContent).containsSubsequence(
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"0\"/>",
			"<meta>",
			"<profile value=\"http://example.com/StructureDefinition/patient_with_extensions\"/>",
			"</meta>",
			"<modifierExtension url=\"http://example.com/ext/date\">",
			"<valueDate value=\"2011-01-01\"/>",
			"</modifierExtension>",
			"</Patient>");
		//@formatter:on

		assertThat(responseContent).doesNotContain("http://hl7.org/fhir/");
	}

	public static class PatientProviderRead implements IResourceProvider {

		@Read()
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(theIdParam);
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}
	}

	public static class PatientProviderCreate implements IResourceProvider {
		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Create()
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			assertNull(thePatient.getIdElement().getIdPart());
			thePatient.setId("1");
			thePatient.getMeta().setVersionId("1");
			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(ourReturnOo).setResource(thePatient);
		}
	}

	public static class PatientProviderSearch implements IResourceProvider {


		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}


		@Search
		public List<IBaseResource> search() {
			ArrayList<IBaseResource> retVal = new ArrayList<>();

			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(new IdType("Patient/0"));
			p0.setDateExt(new DateType("2011-01-01"));
			retVal.add(p0);

			Patient p1 = new Patient();
			p1.setId(new IdType("Patient/1"));
			p1.addName().setFamily("The Family");
			retVal.add(p1);

			return retVal;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
