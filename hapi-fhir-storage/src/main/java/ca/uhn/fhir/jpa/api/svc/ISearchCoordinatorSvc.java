/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nullable;

public interface ISearchCoordinatorSvc<T extends IResourcePersistentId> {

	/**
	 * Create a new search for the given search parameters
	 *
	 * @param theRequestDetails The RequestDetails associated with the request. If you want to supply a fixed
	 *                          {@link RequestPartitionId} you can use a {@link ca.uhn.fhir.rest.api.server.SystemRequestDetails}
	 *                          and supply it there.
	 */
	IBundleProvider createNewSearch(
			IFhirResourceDao<?> theCallingDao,
			SearchParameterMap theParams,
			String theResourceType,
			CacheControlDirective theCacheControlDirective,
			@Nullable RequestDetails theRequestDetails);

	/**
	 * Continue an existing search, given the UUID previously returned from a {@link #createNewSearch(IFhirResourceDao, SearchParameterMap, String, CacheControlDirective, RequestDetails)}
	 * call to {@link IBundleProvider#getUuid()}.
	 *
	 * @param theRequestDetails The RequestDetails associated with the request. If you want to supply a fixed
	 *                          {@link RequestPartitionId} you can use a {@link ca.uhn.fhir.rest.api.server.SystemRequestDetails}
	 *                          and supply it there.
	 */
	IBundleProvider continueExistingSearch(String theSearchUuid, @Nullable RequestDetails theRequestDetails);
}
