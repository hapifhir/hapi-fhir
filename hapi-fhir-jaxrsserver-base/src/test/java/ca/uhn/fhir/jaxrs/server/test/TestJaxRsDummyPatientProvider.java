package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Patient;

/**
 * A dummy patient provider exposing no methods 
 */
public class TestJaxRsDummyPatientProvider extends AbstractJaxRsResourceProvider<Patient> {
	
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
}
