package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RuleTarget {
	IBaseResource resource;
	Collection<IIdType> resourceIds = null;
	String resourceType = null;
	private Map<String, String[]> mySearchParams = null;

	public Map<String, String[]> getSearchParams() {
		return mySearchParams;
	}

	public void setSearchParams(RequestDetails theRequestDetails) {
		mySearchParams = stripMdmSuffix(theRequestDetails.getParameters());
	}

	private Map<String, String[]> stripMdmSuffix(Map<String, String[]> theParameters) {
		Map<String, String[]> retval = new HashMap<>();
		for (Map.Entry<String, String[]> entry : theParameters.entrySet()) {
			String key = entry.getKey();
			String[] value = entry.getValue();
			if (key.endsWith(Constants.PARAMQUALIFIER_MDM)) {
				key = key.split(Constants.PARAMQUALIFIER_MDM)[0];
			}
			retval.put(key, value);
		}
		return retval;
	}
}
