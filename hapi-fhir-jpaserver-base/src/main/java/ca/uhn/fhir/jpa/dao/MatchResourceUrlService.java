package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MatchResourceUrlService {
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public <R extends IBaseResource> Set<ResourcePersistentId> processMatchUrl(String theMatchUrl, Class<R> theResourceType, RequestDetails theRequest) {
		StopWatch sw = new StopWatch();

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceType);

		SearchParameterMap paramMap = myMatchUrlService.translateMatchUrl(theMatchUrl, resourceDef);
		paramMap.setLoadSynchronous(true);

		if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}

		IFhirResourceDao<R> dao = myDaoRegistry.getResourceDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException("No DAO for resource type: " + theResourceType.getName());
		}

		Set<ResourcePersistentId> retVal = dao.searchForIds(paramMap, theRequest);

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		if (JpaInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage("Processed conditional resource URL with " + retVal.size() + " result(s) in " + sw.toString());
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(StorageProcessingMessage.class, message);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
		}
		return retVal;
	}


}
