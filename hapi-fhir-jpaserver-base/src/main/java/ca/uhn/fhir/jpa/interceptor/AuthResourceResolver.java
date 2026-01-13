/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthResourceResolver;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Small service class to inject DB access into an interceptor. Some examples include:
 * <ul>
 *     <li>bulk export security to allow querying for resource to match against permission argument filters</li>
 *     <li>instance $meta operations where only the instance id is known at the time of the request</li>
 * </ul>
 */
public class AuthResourceResolver implements IAuthResourceResolver {

	private static final String RESOURCE_CACHE_KEY = AuthResourceResolver.class.getName() + "_RESOURCE_CACHE";

	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public AuthResourceResolver(DaoRegistry myDaoRegistry, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		this.myDaoRegistry = myDaoRegistry;
		this.myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public IBaseResource resolveResourceById(IIdType theResourceId, RequestDetails theRequestDetails) {
		String resourceIdKey = theResourceId.getValue();

		Map<String, IBaseResource> cache = getResourceCache(theRequestDetails);
		if (cache.containsKey(resourceIdKey)) {
			return cache.get(resourceIdKey);
		}

		SystemRequestDetails systemRequestDetails =
				newSystemRequestDetailsWithPartitionInfoForRead(theRequestDetails, theResourceId);
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		IBaseResource resource = dao.read(theResourceId, systemRequestDetails);
		cache.put(resourceIdKey, resource);

		return resource;
	}

	@Override
	public List<IBaseResource> resolveResourcesByIds(
			List<String> theResourceIds, String theResourceType, RequestDetails theRequestDetails) {

		Map<String, IBaseResource> cache = getResourceCache(theRequestDetails);
		List<IBaseResource> results = new ArrayList<>();
		List<String> idsToFetch = new ArrayList<>();

		for (String id : theResourceIds) {
			String cacheKey = toCacheKey(theResourceType, id);
			if (cache.containsKey(cacheKey) && cache.get(cacheKey) != null) {
				results.add(cache.get(cacheKey));
			} else {
				idsToFetch.add(id);
			}
		}

		if (isNotEmpty(idsToFetch)) {
			List<IBaseResource> fetched = doSearch(idsToFetch, theResourceType, theRequestDetails);
			for (IBaseResource resource : fetched) {
				String cacheKey =
						toCacheKey(theResourceType, resource.getIdElement().getIdPart());
				cache.put(cacheKey, resource);
				results.add(resource);
			}
		}

		return results;
	}

	private List<IBaseResource> doSearch(
			List<String> theResourceIds, String theResourceType, RequestDetails theRequestDetails) {
		TokenOrListParam orListParam = new TokenOrListParam(null, theResourceIds.toArray(String[]::new));

		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_ID, orListParam);

		SystemRequestDetails systemRequestDetails =
				newSystemRequestDetailsWithPartitionInfoForSearch(theRequestDetails, theResourceType, params);

		return myDaoRegistry.getResourceDao(theResourceType).searchForResources(params, systemRequestDetails);
	}

	private SystemRequestDetails newSystemRequestDetailsWithPartitionInfoForRead(
			RequestDetails theRequestDetails, IIdType theResourceId) {
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
				theRequestDetails, ReadPartitionIdRequestDetails.forRead(theResourceId));

		return SystemRequestDetails.forRequestPartitionId(requestPartitionId);
	}

	private SystemRequestDetails newSystemRequestDetailsWithPartitionInfoForSearch(
			RequestDetails theRequestDetails, String theResourceType, SearchParameterMap theParams) {
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
						theRequestDetails, theResourceType, theParams);

		return SystemRequestDetails.forRequestPartitionId(requestPartitionId);
	}

	private Map<String, IBaseResource> getResourceCache(RequestDetails theRequestDetails) {
		@SuppressWarnings("unchecked")
		Map<String, IBaseResource> cache =
				(Map<String, IBaseResource>) theRequestDetails.getUserData().get(RESOURCE_CACHE_KEY);

		if (cache == null) {
			cache = new HashMap<>();
			theRequestDetails.getUserData().put(RESOURCE_CACHE_KEY, cache);
		}
		return cache;
	}

	private String toCacheKey(String theResourceType, String theId) {
		IdDt id = new IdDt(theId);
		if (!id.hasResourceType()) {
			id = new IdDt(theResourceType, theId);
		}
		return id.toUnqualifiedVersionless().getValue();
	}
}
