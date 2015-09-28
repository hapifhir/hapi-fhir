package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;

public class BaseJpaResourceProviderPatientDstu2 extends JpaResourceProviderDstu2<Patient> {

	@Operation(name = "everything", idempotent = true)
	public ca.uhn.fhir.rest.server.IBundleProvider everything(

			javax.servlet.http.HttpServletRequest theServletRequest,

			@IdParam 
			ca.uhn.fhir.model.primitive.IdDt theId,
			
			@Description(formalDefinition="Results from this method are returned across multiple pages. This parameter controls the size of those pages.") 
			@OperationParam(name = "_count") 
			ca.uhn.fhir.model.primitive.UnsignedIntDt theCount) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoPatient<Patient>)getDao()).everything(theServletRequest, theId, theCount);
		} finally {
			endRequest(theServletRequest);
		}

	}

}
