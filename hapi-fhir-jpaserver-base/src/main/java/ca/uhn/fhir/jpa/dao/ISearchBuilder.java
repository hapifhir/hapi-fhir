package ca.uhn.fhir.jpa.dao;

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
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

public interface ISearchBuilder {

	IResultIterator createQuery(SearchParameterMap theParams, SearchRuntimeDetails theSearchRuntime, RequestDetails theRequest);

	void setMaxResultsToFetch(Integer theMaxResultsToFetch);

	Iterator<Long> createCountQuery(SearchParameterMap theParams, String theSearchUuid, RequestDetails theRequest);

	void loadResourcesByPid(Collection<Long> thePids, Collection<Long> theIncludedPids, List<IBaseResource> theResourceListToPopulate, boolean theForHistoryOperation, RequestDetails theDetails);

	Set<Long> loadIncludes(FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode,
								  DateRangeParam theLastUpdated, String theSearchIdOrDescription, RequestDetails theRequest);

	/**
	 * How many results may be fetched at once
	 */
	void setFetchSize(int theFetchSize);

	void setType(Class<? extends IBaseResource> theResourceType, String theResourceName);

	void setPreviouslyAddedResourcePids(List<Long> thePreviouslyAddedResourcePids);

}
