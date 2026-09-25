package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.MyPatientWithExtensions;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateDstu3Test.class);
	public static IBaseOperationOutcome ourReturnOo;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourReturnOo = null;
	}
	
	/**
	 * #472
	 */
	@Test
	public void testCreateReturnsLocationHeader() throws Exception {

		HttpTestResponse response = ourServer.fhirRequest("/Patient").post("{\"resourceType\":\"Patient\", \"status\":\"active\"}", "application/fhir+json; charset=utf-8");
		String responseContent = response.assertStatus(201).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(1, response.getHeaders("Location").size());
		assertEquals(1, response.getHeaders("Content-Location").size());
		assertEquals(ourServer.getBaseUrl() + "/Patient/1", response.getHeader("Location"));

	}

	@Test
	public void testCreateReturnsRepresentation() throws Exception {
		ourReturnOo = new OperationOutcome().addIssue(new OperationOutcomeIssueComponent().setDiagnostics("DIAG"));
		String expectedResponseContent = "{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\"},\"gender\":\"male\"}";
		
		String responseContent = ourServer.fhirRequest("/Patient").post("{\"resourceType\":\"Patient\", \"gender\":\"male\"}", "application/fhir+json; charset=utf-8").assertStatus(201).getBody();

		ourLog.info("Response was:\n{}", responseContent);
		assertEquals(expectedResponseContent, responseContent);

	}

	/**
	 * #342
	 */
	@Test
	public void testCreateWithInvalidContent() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient").post("FOO", "application/xml+fhir; charset=utf-8").assertStatus(400).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		assertThat(responseContent).contains("<OperationOutcome xmlns=\"http://hl7.org/fhir\"><issue><severity value=\"error\"/><code value=\"processing\"/><diagnostics value=\"");
		assertThat(responseContent).contains("Unexpected character 'F'");
		assertThat(responseContent).contains("Failed to parse request body as XML resource. Error was: " + Msg.code(1852));

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

	@Test
	public void testSearch() throws Exception {

		String responseContent = ourServer.fhirRequest("/Patient?_format=xml&_pretty=true").get().assertStatus(200).getBody();

		ourLog.info("Response was:\n{}", responseContent);

		//@formatter:off
		assertThat(responseContent).contains(
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

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	private static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome create(@ResourceParam Patient theIdParam) {
			theIdParam.setId("1");
			theIdParam.getMeta().setVersionId("1");
			
			return new MethodOutcome(new IdType("Patient", "1"), true).setOperationOutcome(ourReturnOo).setResource(theIdParam);
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read()
		public MyPatientWithExtensions read(@IdParam IdType theIdParam) {
			MyPatientWithExtensions p0 = new MyPatientWithExtensions();
			p0.setId(theIdParam);
			p0.setDateExt(new DateType("2011-01-01"));
			return p0;
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

}
