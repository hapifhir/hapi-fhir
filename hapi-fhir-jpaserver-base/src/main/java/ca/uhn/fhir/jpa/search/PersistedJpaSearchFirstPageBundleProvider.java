package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl.SearchTask;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class PersistedJpaSearchFirstPageBundleProvider extends PersistedJpaBundleProvider {

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
		final List<Long> pids = mySearchTask.getResourcePids(theFromIndex, theToIndex);
		
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		return txTemplate.execute(new TransactionCallback<List<IBaseResource>>() {
			@Override
			public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
				return toResourceList(mySearchBuilder, pids);
			}});
	}

	@Override
	public Integer size() {
		mySearchTask.awaitInitialSync();
		SearchCoordinatorSvcImpl.verifySearchHasntFailedOrThrowInternalErrorException(mySearch);
		return super.size();
	}

}
