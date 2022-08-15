package ca.uhn.fhir.jpa.search.cache;

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

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl.toPage;

public class DatabaseSearchResultCacheSvcImpl implements ISearchResultCacheSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(DatabaseSearchResultCacheSvcImpl.class);

	@Autowired
	private ISearchResultDao mySearchResultDao;

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public List<ResourcePersistentId> fetchResultPids(Search theSearch, int theFrom, int theTo) {
		final Pageable page = toPage(theFrom, theTo);
		if (page == null) {
			return Collections.emptyList();
		}

		List<Long> retVal = mySearchResultDao
			.findWithSearchPid(theSearch.getId(), page)
			.getContent();

		ourLog.debug("fetchResultPids for range {}-{} returned {} pids", theFrom, theTo, retVal.size());

		return ResourcePersistentId.fromLongList(retVal);
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public List<ResourcePersistentId> fetchAllResultPids(Search theSearch) {
		List<Long> retVal = mySearchResultDao.findWithSearchPidOrderIndependent(theSearch.getId());
		ourLog.trace("fetchAllResultPids returned {} pids", retVal.size());
		return ResourcePersistentId.fromLongList(retVal);
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public void storeResults(Search theSearch, List<ResourcePersistentId> thePreviouslyStoredResourcePids, List<ResourcePersistentId> theNewResourcePids) {
		List<SearchResult> resultsToSave = Lists.newArrayList();

		ourLog.debug("Storing {} results with {} previous for search", theNewResourcePids.size(), thePreviouslyStoredResourcePids.size());

		int order = thePreviouslyStoredResourcePids.size();
		for (ResourcePersistentId nextPid : theNewResourcePids) {
			SearchResult nextResult = new SearchResult(theSearch);
			nextResult.setResourcePid(nextPid.getIdAsLong());
			nextResult.setOrder(order);
			resultsToSave.add(nextResult);
			ourLog.trace("Saving ORDER[{}] Resource {}", order, nextResult.getResourcePid());

			order++;
		}

		mySearchResultDao.saveAll(resultsToSave);
	}

}
