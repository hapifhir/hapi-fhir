package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsResourceProvider;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

/**
 * A dummy patient provider exposing no methods 
 */
public abstract class AbstractDummyPatientProvider extends AbstractJaxRsResourceProvider<Patient> {

	public AbstractDummyPatientProvider() {
		super(FhirContext.forR4());
	}

	@Override
	public abstract String getBaseForServer();


	@Search
	public List<Patient> search(@RequiredParam(name = Patient.SP_NAME) final StringParam name) {
		return null;
	}

	@Override
	public Class<Patient> getResourceType() {
		return Patient.class;
	}
}
