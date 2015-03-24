package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
