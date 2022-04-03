package ca.uhn.fhir.jpa.api.svc;

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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface ISearchCoordinatorSvc {

	void cancelAllActiveSearches();

	List<ResourcePersistentId> getResources(String theUuid, int theFrom, int theTo, @Nullable RequestDetails theRequestDetails);

	IBundleProvider registerSearch(IFhirResourceDao<?> theCallingDao, SearchParameterMap theParams, String theResourceType, CacheControlDirective theCacheControlDirective, @Nullable RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId);

	/**
	 * Fetch the total number of search results for the given currently executing search, if one is currently executing and
	 * the total is known. Will return empty otherwise
	 */
	Optional<Integer> getSearchTotal(String theUuid);

}
