package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;

/**
 * A dummy patient provider exposing no methods 
 */
public class TestJaxRsDummyPatientProviderDstu3 extends AbstractJaxRsResourceProvider<Patient> {
	
    public TestJaxRsDummyPatientProviderDstu3() {
        super(FhirContext.forDstu3());
    }
    
	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}

}
