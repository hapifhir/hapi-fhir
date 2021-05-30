package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
	private DaoConfig myDaoConfig;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private MemoryCacheService myMemoryCacheService;

	/**
	 * Note that this will only return a maximum of 2 results!!
	 */
	public <R extends IBaseResource> Set<ResourcePersistentId> processMatchUrl(String theMatchUrl, Class<R> theResourceType, RequestDetails theRequest) {
		if (myDaoConfig.getMatchUrlCache()) {
			ResourcePersistentId existing = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, theMatchUrl);
			if (existing != null) {
				return Collections.singleton(existing);
			}
		}

		RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceType);
		SearchParameterMap paramMap = myMatchUrlService.translateMatchUrl(theMatchUrl, resourceDef);
		if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
			throw new InvalidRequestException("Invalid match URL[" + theMatchUrl + "] - URL has no search parameters");
		}
		paramMap.setLoadSynchronousUpTo(2);

		Set<ResourcePersistentId> retVal = search(paramMap, theResourceType, theRequest);

		if (myDaoConfig.getMatchUrlCache() && retVal.size() == 1) {
			ResourcePersistentId pid = retVal.iterator().next();
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.MATCH_URL, theMatchUrl, pid);
		}

		return retVal;
	}

	public <R extends IBaseResource> Set<ResourcePersistentId> search(SearchParameterMap theParamMap, Class<R> theResourceType, RequestDetails theRequest) {
		StopWatch sw = new StopWatch();
		IFhirResourceDao<R> dao = myDaoRegistry.getResourceDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException("No DAO for resource type: " + theResourceType.getName());
		}

		Set<ResourcePersistentId> retVal = dao.searchForIds(theParamMap, theRequest);

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage("Processed conditional resource URL with " + retVal.size() + " result(s) in " + sw.toString());
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(StorageProcessingMessage.class, message);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
		}
		return retVal;
	}


	public void matchUrlResolved(String theMatchUrl, ResourcePersistentId theResourcePersistentId) {
		Validate.notBlank(theMatchUrl);
		Validate.notNull(theResourcePersistentId);
		if (myDaoConfig.getMatchUrlCache()) {
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.MATCH_URL, theMatchUrl, theResourcePersistentId);
		}
	}
}
