package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.subscriber.IResourceRetriever;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DaoResourceRetriever implements IResourceRetriever {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscription.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	DaoRegistry myDaoRegistry;

	@Override
	public IBaseResource getResource(IIdType payloadId) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceDef.getImplementingClass());
		return dao.read(payloadId.toVersionless());
	}
}
