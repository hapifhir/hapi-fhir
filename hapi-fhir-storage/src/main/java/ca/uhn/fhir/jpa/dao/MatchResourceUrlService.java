package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
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
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MatchResourceUrlService {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MatchResourceUrlService.class);

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
	public <R extends IBaseResource> Set<ResourcePersistentId> processMatchUrl(String theMatchUrl, Class<R> theResourceType, TransactionDetails theTransactionDetails, RequestDetails theRequest) {
		return processMatchUrl(theMatchUrl, theResourceType, theTransactionDetails, theRequest, null);
	}

	/**
	 * Note that this will only return a maximum of 2 results!!
	 */
	public <R extends IBaseResource> Set<ResourcePersistentId> processMatchUrl(String theMatchUrl, Class<R> theResourceType, TransactionDetails theTransactionDetails, RequestDetails theRequest, IBaseResource theConditionalOperationTargetOrNull) {
		Set<ResourcePersistentId> retVal = null;

		String resourceType = myContext.getResourceType(theResourceType);
		String matchUrl = massageForStorage(resourceType, theMatchUrl);

		ResourcePersistentId resolvedInTransaction = theTransactionDetails.getResolvedMatchUrls().get(matchUrl);
		if (resolvedInTransaction != null) {
			// If the resource has previously been looked up within the transaction, there's no need to re-authorize it.
			if (resolvedInTransaction == TransactionDetails.NOT_FOUND) {
				return Collections.emptySet();
			} else {
				return Collections.singleton(resolvedInTransaction);
			}
		}

		ResourcePersistentId resolvedInCache = processMatchUrlUsingCacheOnly(resourceType, matchUrl);
		if (resolvedInCache != null) {
			retVal = Collections.singleton(resolvedInCache);
		}

		if (retVal == null) {
			RuntimeResourceDefinition resourceDef = myContext.getResourceDefinition(theResourceType);
			SearchParameterMap paramMap = myMatchUrlService.translateMatchUrl(matchUrl, resourceDef);
			if (paramMap.isEmpty() && paramMap.getLastUpdated() == null) {
				throw new InvalidRequestException(Msg.code(518) + "Invalid match URL[" + matchUrl + "] - URL has no search parameters");
			}
			paramMap.setLoadSynchronousUpTo(2);

			retVal = search(paramMap, theResourceType, theRequest, theConditionalOperationTargetOrNull);
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.STORAGE_PRESHOW_RESOURCES, myInterceptorBroadcaster, theRequest)) {
			Map<IBaseResource, ResourcePersistentId> resourceToPidMap = new HashMap<>();

			IFhirResourceDao<R> dao = getResourceDao(theResourceType);

			for (ResourcePersistentId pid : retVal) {
				resourceToPidMap.put(dao.readByPid(pid), pid);
			}

			SimplePreResourceShowDetails accessDetails = new SimplePreResourceShowDetails(resourceToPidMap.keySet());
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);

			try {
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);

				retVal = accessDetails.toList()
					.stream()
					.map(resourceToPidMap::get)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet());
			} catch (ForbiddenOperationException e) {
				// If the search matches a resource that the user does not have authorization for,
				// we want to treat it the same as if the search matched no resources, in order not to leak information.
				ourLog.warn("Inline match URL [" + matchUrl + "] specified a resource the user is not authorized to access.", e);
				retVal = new HashSet<>();
			}
		}

		if (retVal.size() == 1) {
			ResourcePersistentId pid = retVal.iterator().next();
			theTransactionDetails.addResolvedMatchUrl(matchUrl, pid);
			if (myDaoConfig.isMatchUrlCacheEnabled()) {
				myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.MATCH_URL, matchUrl, pid);
			}
		}

		return retVal;
	}

	private <R extends IBaseResource> IFhirResourceDao<R> getResourceDao(Class<R> theResourceType) {
		IFhirResourceDao<R> dao = myDaoRegistry.getResourceDao(theResourceType);
		if (dao == null) {
			throw new InternalErrorException(Msg.code(519) + "No DAO for resource type: " + theResourceType.getName());
		}
		return dao;
	}

	private String massageForStorage(String theResourceType, String theMatchUrl) {
		Validate.notBlank(theMatchUrl, "theMatchUrl must not be null or blank");
		int questionMarkIdx = theMatchUrl.indexOf("?");
		if (questionMarkIdx > 0) {
			return theMatchUrl;
		}
		if (questionMarkIdx == 0) {
			return theResourceType + theMatchUrl;
		}
		return theResourceType + "?" + theMatchUrl;
	}

	@Nullable
	public ResourcePersistentId processMatchUrlUsingCacheOnly(String theResourceType, String theMatchUrl) {
		ResourcePersistentId existing = null;
		if (myDaoConfig.isMatchUrlCacheEnabled()) {
			String matchUrl = massageForStorage(theResourceType, theMatchUrl);
			existing = myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, matchUrl);
		}
		return existing;
	}

	public <R extends IBaseResource> Set<ResourcePersistentId> search(SearchParameterMap theParamMap, Class<R> theResourceType, RequestDetails theRequest, @Nullable IBaseResource theConditionalOperationTargetOrNull) {
		StopWatch sw = new StopWatch();
		IFhirResourceDao<R> dao = getResourceDao(theResourceType);

		List<ResourcePersistentId> retVal = dao.searchForIds(theParamMap, theRequest, theConditionalOperationTargetOrNull);

		// Interceptor broadcast: JPA_PERFTRACE_INFO
		if (CompositeInterceptorBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_INFO, myInterceptorBroadcaster, theRequest)) {
			StorageProcessingMessage message = new StorageProcessingMessage();
			message.setMessage("Processed conditional resource URL with " + retVal.size() + " result(s) in " + sw);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest)
				.add(StorageProcessingMessage.class, message);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.JPA_PERFTRACE_INFO, params);
		}

		return new HashSet<>(retVal);
	}


	public void matchUrlResolved(TransactionDetails theTransactionDetails, String theResourceType, String theMatchUrl, ResourcePersistentId theResourcePersistentId) {
		Validate.notBlank(theMatchUrl);
		Validate.notNull(theResourcePersistentId);
		String matchUrl = massageForStorage(theResourceType, theMatchUrl);
		theTransactionDetails.addResolvedMatchUrl(matchUrl, theResourcePersistentId);
		if (myDaoConfig.isMatchUrlCacheEnabled()) {
			myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.MATCH_URL, matchUrl, theResourcePersistentId);
		}
	}

}
