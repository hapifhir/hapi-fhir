package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateWithPreferReturnR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CreateWithPreferReturnR4Test.class);
	private static IBaseOperationOutcome ourReturnOperationOutcome;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourReturnOperationOutcome = null;
	}

	@Test
	public void testCreatePreferMinimalNoOperationOutcome() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL)
			.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertThat(responseContent).isNullOrEmpty();
		// assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).doesNotContain("fhir");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).isNull();
		assertThat(response.getHeader("location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(response.getHeader("content-location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void create_withPreferReturnOperationOutcome_returnsOperationOutcome() throws Exception {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
			.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertThat(responseContent).contains("DIAG");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE).toLowerCase().replace(" ", "")).isEqualTo("application/xml+fhir;charset=utf-8");
		assertThat(response.getHeader("location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(response.getHeader("content-location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void create_providerLeavesCreatedFlagUnset_returnsSubmittedBody() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
			.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
			.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).contains(Constants.CT_FHIR_XML);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"001\"/><meta><versionId value=\"002\"/></meta><identifier><value value=\"002\"/></identifier></Patient>", responseContent);
		assertThat(response.getHeader("location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(response.getHeader("content-location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void testCreateWithNoPrefer() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
			.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML);

		String responseContent = response.getBody();

		ourLog.info("Response was:\n{}", responseContent);

		response.assertStatus(201);
		assertThat(response.getHeader("location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");
		assertThat(response.getHeader("content-location")).isEqualTo(ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}


	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			IdType id = new IdType("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			retVal.setOperationOutcome(ourReturnOperationOutcome);

			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			IdDt id = new IdDt("Patient/001/_history/002");
			MethodOutcome retVal = new MethodOutcome(id);

			thePatient.setId(id);
			retVal.setResource(thePatient);

			return retVal;
		}

	}

}
