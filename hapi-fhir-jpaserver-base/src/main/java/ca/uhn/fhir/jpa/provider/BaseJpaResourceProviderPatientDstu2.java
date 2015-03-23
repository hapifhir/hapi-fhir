package ca.uhn.fhir.jpa.provider;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class BaseJpaResourceProviderPatientDstu2 extends JpaResourceProviderDstu2<Patient> {

	@Operation(name="$everything", idempotent=true)
	public IBundleProvider everything(HttpServletRequest theServletRequest, @IdParam IdDt theId) {
		startRequest(theServletRequest);
		try {
			SearchParameterMap paramMap = new SearchParameterMap();
			paramMap.setRevIncludes(Collections.singleton(new Include("*")));
			paramMap.setIncludes(Collections.singleton(new Include("*")));
			paramMap.add("_id", new StringParam(theId.getIdPart()));		
			ca.uhn.fhir.rest.server.IBundleProvider retVal = getDao().search(paramMap);
			return retVal;
		} finally {
			endRequest(theServletRequest);
		}
	}
	
}
