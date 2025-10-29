/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Small service class to inject DB access into an interceptor
 * For example, used in bulk export security to allow querying for resource to match against permission argument filters
 */
@Service
public class AuthResourceResolver implements IAuthResourceResolver {
	private final DaoRegistry myDaoRegistry;

	public AuthResourceResolver(DaoRegistry myDaoRegistry) {
		this.myDaoRegistry = myDaoRegistry;
	}

	public IBaseResource resolveCompartmentById(IIdType theResourceId) {
		return myDaoRegistry
				.getResourceDao(theResourceId.getResourceType())
				.read(theResourceId, new SystemRequestDetails());
	}

	public List<IBaseResource> resolveCompartmentByIds(List<String> theResourceIds, String theResourceType) {
		TokenOrListParam t = new TokenOrListParam(null, theResourceIds.toArray(String[]::new));

		SearchParameterMap m = new SearchParameterMap();
		m.add(Constants.PARAM_ID, t);
		return myDaoRegistry.getResourceDao(theResourceType).searchForResources(m, new SystemRequestDetails());
	}
}
