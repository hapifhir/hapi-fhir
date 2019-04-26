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

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl.SearchTask;

public class PersistedJpaSearchFirstPageBundleProvider extends PersistedJpaBundleProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaSearchFirstPageBundleProvider.class);
	private SearchTask mySearchTask;
	private ISearchBuilder mySearchBuilder;
	private Search mySearch;
	private PlatformTransactionManager myTxManager;

	public PersistedJpaSearchFirstPageBundleProvider(Search theSearch, IDao theDao, SearchTask theSearchTask, ISearchBuilder theSearchBuilder, PlatformTransactionManager theTxManager) {
		super(theSearch.getUuid(), theDao);
		setSearchEntity(theSearch);
		mySearchTask = theSearchTask;
		mySearchBuilder = theSearchBuilder;
		mySearch = theSearch;
		myTxManager = theTxManager;
	}

	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		SearchCoordinatorSvcImpl.verifySearchHasntFailedOrThrowInternalErrorException(mySearch);

		ourLog.trace("Fetching search resource PIDs");
		final List<Long> pids = mySearchTask.getResourcePids(theFromIndex, theToIndex);
		ourLog.trace("Done fetching search resource PIDs");

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		List<IBaseResource> retVal = txTemplate.execute(theStatus -> toResourceList(mySearchBuilder, pids));

		int totalCountWanted = theToIndex - theFromIndex;
		if (retVal.size() < totalCountWanted) {
			if (mySearch.getStatus() == SearchStatusEnum.PASSCMPLET) {
				int remainingWanted = totalCountWanted - retVal.size();
				int fromIndex = theToIndex - remainingWanted;
				List<IBaseResource> remaining = super.getResources(fromIndex, theToIndex);
				retVal.addAll(remaining);
			}
		}
		ourLog.trace("Loaded resources to return");

		return retVal;
	}

	@Override
	public Integer size() {
		ourLog.trace("Waiting for initial sync");
		Integer size = mySearchTask.awaitInitialSync();
		ourLog.trace("Finished waiting for local sync");

		SearchCoordinatorSvcImpl.verifySearchHasntFailedOrThrowInternalErrorException(mySearch);
		if (size != null) {
			return size;
		}
		return super.size();
	}

}
