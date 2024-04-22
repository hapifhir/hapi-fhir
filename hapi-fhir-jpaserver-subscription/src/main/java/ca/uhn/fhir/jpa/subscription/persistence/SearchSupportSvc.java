/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.persistence;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.api.svc.ISearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchSupportSvc {
	@Autowired
	private ISearchSvc mySearchService;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private ISearchCoordinatorSvc<? extends IResourcePersistentId<?>> mySearchCoordinatorSvc;

	public IBundleProvider search(
			IFhirResourceDao<?> callingDao,
			SearchParameterMap params,
			String resourceType,
			RequestPartitionId thePartitionId) {
		return mySearchCoordinatorSvc.registerSearch(
				callingDao, params, resourceType, new CacheControlDirective(), null, thePartitionId);
	}

	public List<? extends IResourcePersistentId<?>> getResources(
			String theCurrentSearchUuid, int fromIndex, int toIndex, RequestPartitionId requestPartitionId) {
		return mySearchCoordinatorSvc.getResources(theCurrentSearchUuid, fromIndex, toIndex, null, requestPartitionId);
	}

	public ISearchBuilder getSearchBuilder(
			IFhirResourceDao<?> resourceDao, String resourceType, Class<? extends IBaseResource> theResourceClass) {
		return mySearchBuilderFactory.newSearchBuilder(resourceDao, resourceType, theResourceClass);
	}

	public IBundleProvider search(
			SearchParameterMap params, String theResourceName, RequestPartitionId thePartitionId) {
		return mySearchService.executeQuery(theResourceName, params, thePartitionId);
	}
}
