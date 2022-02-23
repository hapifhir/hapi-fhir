package ca.uhn.fhir.jpa.batch.reader;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * This service is used by batch processes to search resources
 */
public class BatchResourceSearcher {
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public IResultIterator performSearch(ResourceSearch theResourceSearch, Integer theBatchSize) {
		String resourceName = theResourceSearch.getResourceName();
		RequestPartitionId requestPartitionId = theResourceSearch.getRequestPartitionId();

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceName);
		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, resourceName, theResourceSearch.getResourceType());
		sb.setFetchSize(theBatchSize);
		SystemRequestDetails requestDetails = buildSystemRequestDetails(requestPartitionId);
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(requestDetails, UUID.randomUUID().toString());
		IResultIterator resultIter = sb.createQuery(theResourceSearch.getSearchParameterMap(), searchRuntimeDetails, requestDetails, requestPartitionId);
		return resultIter;
	}

	@Nonnull
	private SystemRequestDetails buildSystemRequestDetails(RequestPartitionId theRequestPartitionId) {
		SystemRequestDetails retval = new SystemRequestDetails();
		retval.setRequestPartitionId(theRequestPartitionId);
		return retval;
	}
}
