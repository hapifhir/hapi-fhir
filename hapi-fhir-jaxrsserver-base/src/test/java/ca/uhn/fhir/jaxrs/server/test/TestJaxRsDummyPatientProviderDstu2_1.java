package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import org.hl7.fhir.dstu2016may.model.Patient;

/**
 * A dummy patient provider exposing no methods
 */
public class TestJaxRsDummyPatientProviderDstu2_1 extends AbstractJaxRsResourceProvider<Patient> {

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
}
