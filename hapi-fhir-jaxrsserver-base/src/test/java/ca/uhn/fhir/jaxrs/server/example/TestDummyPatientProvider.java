package ca.uhn.fhir.jaxrs.server.example;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class TestDummyPatientProvider extends AbstractJaxRsResourceProvider<Patient> {
	
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
}
