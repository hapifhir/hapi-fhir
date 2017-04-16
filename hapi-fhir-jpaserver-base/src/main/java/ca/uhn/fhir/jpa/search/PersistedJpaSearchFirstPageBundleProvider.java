package ca.uhn.fhir.jpa.search;

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
		checkForFailedSearch();
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
		checkForFailedSearch();
		return super.size();
	}

	private void checkForFailedSearch() {
		if (mySearch.getStatus() == SearchStatusEnum.FAILED) {
			throw new InternalErrorException("Failure while loading search results");
		}
	}


}
