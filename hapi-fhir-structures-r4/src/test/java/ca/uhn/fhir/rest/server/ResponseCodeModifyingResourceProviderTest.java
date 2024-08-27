package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.ServletRequest;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_202_ACCEPTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseCodeModifyingResourceProviderTest {

	public static final int CUSTOM_RESPONSE_CODE = STATUS_HTTP_202_ACCEPTED;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.R4)
		 .registerProvider(new TestResponseCodeModifyingPatientProvider());

	private IGenericClient myClient;

	private Patient myPatient;

	@BeforeEach
	public void before() {
		myPatient = new Patient();
		myPatient.getNameFirstRep().addGiven("John").setFamily("Smith");
		myClient = myServer.getFhirClient();
	}

	@Test
	public void testCreatePatientReturnsModifiedResponseCode() {
		MethodOutcome outcome = myClient.create().resource(myPatient).execute();
		assertEquals(CUSTOM_RESPONSE_CODE, outcome.getResponseStatusCode());
	}

	@Test
	public void testUpdatePatientReturnsModifiedResponseCode() {
		myPatient.setId("1");
		MethodOutcome outcome = myClient.update().resource(myPatient).execute();
		assertEquals(CUSTOM_RESPONSE_CODE, outcome.getResponseStatusCode());
	}

	class TestResponseCodeModifyingPatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, ServletRequest theServletRequest) {
			MethodOutcome methodOutcome = new MethodOutcome();
			methodOutcome.setResponseStatusCode(CUSTOM_RESPONSE_CODE);
			return methodOutcome;
		}

		@Update()
		public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient) {
			MethodOutcome methodOutcome = new MethodOutcome();
			methodOutcome.setResponseStatusCode(CUSTOM_RESPONSE_CODE);
			return methodOutcome;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
	}

}
