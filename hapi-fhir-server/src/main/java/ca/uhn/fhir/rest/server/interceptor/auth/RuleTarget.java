package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
