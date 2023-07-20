/*-
 * #%L
 * HAPI FHIR - CDS Hooks
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.cdshooks.svc;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsMethod;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.ICdsCrServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseCdsCrMethod implements ICdsMethod {
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	ICdsCrServiceFactory myCdsCrServiceFactory;

	public Object invoke(ObjectMapper theObjectMapper, IModelJson theJson, String theServiceId) {
		try {
			return myCdsCrServiceFactory.create(myDaoRegistry, theServiceId).invoke(theJson);
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause() instanceof BaseServerResponseException) {
				throw (BaseServerResponseException) e.getCause();
			}
			throw new ConfigurationException("Failed to invoke $apply on " + theServiceId, e);
		}
	}
}
