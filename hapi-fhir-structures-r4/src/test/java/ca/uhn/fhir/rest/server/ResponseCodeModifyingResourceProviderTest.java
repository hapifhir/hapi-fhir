package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.provider.TestResponseCodeModifyingPatientProvider;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static ca.uhn.fhir.provider.TestResponseCodeModifyingPatientProvider.CUSTOM_RESPONSE_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseCodeModifyingResourceProviderTest {

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

}
