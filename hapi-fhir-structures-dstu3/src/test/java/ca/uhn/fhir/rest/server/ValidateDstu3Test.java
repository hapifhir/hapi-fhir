package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidateDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static EncodingEnum ourLastEncoding;
	private static IdType ourLastId;
	private static ValidationModeEnum ourLastMode;
	public static Patient ourLastPatient;
	private static String ourLastProfile;
	private static String ourLastResourceBody;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidateDstu3Test.class);
	private static OperationOutcome ourOutcomeToReturn;

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .registerProvider(new OrganizationProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@BeforeEach()
	public void before() {
		ourLastResourceBody = null;
		ourLastEncoding = null;
		ourOutcomeToReturn = null;
		ourLastMode = null;
		ourLastProfile = null;
	}

	@Test
	public void testValidate() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp).contains("<OperationOutcome");
	}

	@Test
	public void testValidateBlankMode() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);
		params.addParameter().setName("mode").setValue(new CodeType(" "));

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testValidateInvalidMode() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);
		params.addParameter().setName("mode").setValue(new CodeType("AAA"));

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(400, status.getStatusLine().getStatusCode());

		assertThat(resp).contains("Invalid mode value: &quot;AAA&quot;");
	}

	@Test
	public void testValidateMissingResource() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("mode").setValue(new CodeType("create"));

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertNull(ourLastPatient);
		assertEquals(200, status.getStatusLine().getStatusCode());
	}

	@Test
	public void testValidateWithGet() throws Exception {
		ourOutcomeToReturn = new OperationOutcome();
		ourOutcomeToReturn.addIssue().setDiagnostics("FOOBAR");

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/123/$validate");

		HttpResponse status = ourClient.execute(httpGet);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp).contains("<OperationOutcome", "FOOBAR");
		assertNull(ourLastPatient);
		assertEquals("Patient", ourLastId.getResourceType());
		assertEquals("123", ourLastId.getIdPart());
	}

	@Test
	public void testValidateWithNoParsed() throws Exception {

		Organization org = new Organization();
		org.addIdentifier().setValue("001");
		org.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(org);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Organization/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newJsonParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_JSON, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(ourLastResourceBody).contains("\"resourceType\":\"Organization\"", "\"identifier\"", "\"value\":\"001");
		assertEquals(EncodingEnum.JSON, ourLastEncoding);

	}

	@Test
	public void testValidateWithOptions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);
		params.addParameter().setName("profile").setValue(new StringType("http://foo"));
		params.addParameter().setName("mode").setValue(new StringType(ValidationModeEnum.CREATE.getCode()));

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(resp);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp).contains("<OperationOutcome");
		assertEquals("http://foo", ourLastProfile);
		assertEquals(ValidationModeEnum.CREATE, ourLastMode);
	}

	@Test
	public void testValidateWithResults() throws Exception {

		ourOutcomeToReturn = new OperationOutcome();
		ourOutcomeToReturn.addIssue().setDiagnostics("FOOBAR");

		Patient patient = new Patient();
		patient.addIdentifier().setValue("001");
		patient.addIdentifier().setValue("002");

		Parameters params = new Parameters();
		params.addParameter().setName("resource").setResource(patient);

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient/$validate");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(params), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);
		String resp = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		assertThat(resp).contains("<OperationOutcome", "FOOBAR");
	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class OrganizationProvider implements IResourceProvider {

		@Override
		public Class<Organization> getResourceType() {
			return Organization.class;
		}

		@Validate()
		public MethodOutcome validate(@ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			ourLastResourceBody = theResourceBody;
			ourLastEncoding = theEncoding;

			return new MethodOutcome(new IdType("001"));
		}

	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Validate()
		public MethodOutcome validatePatient(@ResourceParam Patient thePatient, @IdParam(optional=true) IdType theId, @Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile) {

			ourLastPatient = thePatient;
			ourLastId = theId;
			IdType id;
			if (thePatient != null) {
				id = new IdType(thePatient.getIdentifier().get(0).getValue());
				if (thePatient.getIdElement().isEmpty() == false) {
					id = thePatient.getIdElement();
				}
			} else {
				id = new IdType("1");
			}
			ourLastMode = theMode;
			ourLastProfile = theProfile;

			MethodOutcome outcome = new MethodOutcome(id.withVersion("002"));
			outcome.setOperationOutcome(ourOutcomeToReturn);
			return outcome;
		}

	}

}
