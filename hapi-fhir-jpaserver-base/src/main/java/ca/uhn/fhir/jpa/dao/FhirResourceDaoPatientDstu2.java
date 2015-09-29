package ca.uhn.fhir.jpa.dao;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class FhirResourceDaoPatientDstu2 extends FhirResourceDaoDstu2<Patient>implements IFhirResourceDaoPatient<Patient> {

	@Override
	public IBundleProvider everything(HttpServletRequest theServletRequest, IdDt theId, UnsignedIntDt theCount, DateRangeParam theLastUpdated, SortSpec theSort) {
		SearchParameterMap paramMap = new SearchParameterMap();
		if (theCount != null) {
			paramMap.setCount(theCount.getValue());
		}

		paramMap.setRevIncludes(Collections.singleton(IResource.INCLUDE_ALL.asRecursive()));
		paramMap.setIncludes(Collections.singleton(IResource.INCLUDE_ALL.asRecursive()));
		paramMap.setEverythingMode(true);
		paramMap.setSort(theSort);
		paramMap.setLastUpdated(theLastUpdated);
		paramMap.add("_id", new StringParam(theId.getIdPart()));
		ca.uhn.fhir.rest.server.IBundleProvider retVal = search(paramMap);
		return retVal;
	}

}
