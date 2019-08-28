package ca.uhn.fhir.jpa.search;

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

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl.DEFAULT_CUTOFF_SLACK;

/**
 * Deletes old searches
 */
//
// NOTE: This is not a @Service because we manually instantiate
// it in BaseConfig. This is so that we can override the definition
// in Smile.
//
public class StaleSearchDeletingSvcImpl implements IStaleSearchDeletingSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StaleSearchDeletingSvcImpl.class);
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void pollForStaleSearchesAndDeleteThem() {
		mySearchCacheSvc.pollForStaleSearchesAndDeleteThem();
	}

	@Scheduled(fixedDelay = DEFAULT_CUTOFF_SLACK)
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void schedulePollForStaleSearches() {
		if (!myDaoConfig.isSchedulingDisabled()) {
			pollForStaleSearchesAndDeleteThem();
		}
	}
}
