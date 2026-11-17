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

public class CreateWithPreferReturnR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
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
	public void testCreatePreferMinimalNoOperationOutcome() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_MINIMAL)
				.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(201)
				.assertBodyEquals("");

		// assertThat(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE).getValue()).doesNotContain("fhir");
		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).isNull();
		response.assertHeader(Constants.HEADER_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");
		response.assertHeader(Constants.HEADER_CONTENT_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void create_withPreferReturnOperationOutcome_returnsOperationOutcome() {

		OperationOutcome oo = new OperationOutcome();
		oo.addIssue().setDiagnostics("DIAG");
		ourReturnOperationOutcome = oo;

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME)
				.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(201)
				.assertBodyContains("DIAG");

		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE).toLowerCase().replace(" ", "")).isEqualTo("application/xml+fhir;charset=utf-8");
		response.assertHeader(Constants.HEADER_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");
		response.assertHeader(Constants.HEADER_CONTENT_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void create_providerLeavesCreatedFlagUnset_returnsSubmittedBody() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient")
				.withHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION)
				.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(201)
				.assertBodyEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"001\"/><meta><versionId value=\"002\"/></meta><identifier><value value=\"002\"/></identifier></Patient>");

		assertThat(response.getHeader(Constants.HEADER_CONTENT_TYPE)).contains(Constants.CT_FHIR_XML);
		response.assertHeader(Constants.HEADER_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");
		response.assertHeader(Constants.HEADER_CONTENT_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");

	}

	@Test
	public void testCreateWithNoPrefer() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		ourServer.fhirRequest("/Patient")
				.post(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(201)
				.assertHeader(Constants.HEADER_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002")
				.assertHeader(Constants.HEADER_CONTENT_LOCATION, ourServer.getBaseUrl() + "/Patient/001/_history/002");

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
