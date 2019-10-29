package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import org.hl7.fhir.dstu2.model.Patient;

/**
 * A dummy patient provider exposing no methods
 */
public class TestJaxRsDummyPatientProviderDstu2Hl7Org extends AbstractJaxRsResourceProvider<Patient> {

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
}
