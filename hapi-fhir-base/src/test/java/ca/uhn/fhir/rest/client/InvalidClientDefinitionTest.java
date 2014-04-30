package ca.uhn.fhir.rest.client;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.ClientTest.CustomPatient;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.param.QualifiedDateParam;

public class InvalidClientDefinitionTest {

	@Test
	public void testAnnotationTypeIsNotAssignableToMethodReturnType() {
		// TODO: this should fail
		new FhirContext().newRestfulClient(ITestClientWithCustomType.class, "http://example.com");
	}
	
	public interface ITestClientWithCustomType extends IBasicClient {
		@Search(type=Patient.class)
		public CustomPatient getPatientByDob(@RequiredParam(name=Patient.SP_BIRTHDATE) QualifiedDateParam theBirthDate);
	}
	
}
