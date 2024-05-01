package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.OperationOutcome;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Parameters;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.stringContainsInOrder;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class ValidateHl7OrgDstu2Test {
	private static EncodingEnum ourLastEncoding;
	private static String ourLastResourceBody;
	private static OperationOutcome ourOutcomeToReturn;
	private static ValidationModeEnum ourLastMode;
	private static String ourLastProfile;
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .registerProvider(new OrganizationProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();

  @BeforeEach()
	public void before() {
		ourLastResourceBody = null;
		ourLastEncoding = null;
		ourOutcomeToReturn = null;
		ourLastMode = null;
		ourLastProfile = null;
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
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(resp).containsSequence("<OperationOutcome");
		assertThat(ourLastProfile).isEqualTo("http://foo");
		assertThat(ourLastMode).isEqualTo(ValidationModeEnum.CREATE);
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

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(resp).containsSequence("<OperationOutcome");
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

		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(resp).containsSequence("<OperationOutcome", "FOOBAR");
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
		assertThat(status.getStatusLine().getStatusCode()).isEqualTo(200);

		assertThat(ourLastResourceBody).containsSequence("\"resourceType\":\"Organization\"", "\"identifier\"", "\"value\":\"001");
		assertThat(ourLastEncoding).isEqualTo(EncodingEnum.JSON);

	}

	@AfterAll
	public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
	}


	public static class PatientProvider implements IResourceProvider {

		@Validate()
		public MethodOutcome validatePatient(@ResourceParam Patient thePatient, @Validate.Mode ValidationModeEnum theMode, @Validate.Profile String theProfile) {
			IdType id = new IdType(thePatient.getIdentifier().get(0).getValue());
			if (thePatient.getIdElement().isEmpty() == false) {
				id = thePatient.getIdElement();
			}

			ourLastMode = theMode;
			ourLastProfile = theProfile;

			MethodOutcome outcome = new MethodOutcome(id.withVersion("002"));
			outcome.setOperationOutcome(ourOutcomeToReturn);
			return outcome;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

	public static class OrganizationProvider implements IResourceProvider {

		@Validate()
		public MethodOutcome validate(@ResourceParam String theResourceBody, @ResourceParam EncodingEnum theEncoding) {
			ourLastResourceBody = theResourceBody;
			ourLastEncoding = theEncoding;

			return new MethodOutcome(new IdDt("001"));
		}

		@Override
		public Class<Organization> getResourceType() {
			return Organization.class;
		}

	}

}
