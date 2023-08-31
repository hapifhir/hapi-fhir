package ca.uhn.fhir.provider;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;

import javax.servlet.ServletRequest;

import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_202_ACCEPTED;

public class TestResponseCodeModifyingPatientProvider implements IResourceProvider {

	public static final int CUSTOM_RESPONSE_CODE = STATUS_HTTP_202_ACCEPTED;

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
