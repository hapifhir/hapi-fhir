package ca.uhn.fhir.jpa.search;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

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

	public PersistedJpaSearchFirstPageBundleProvider(Search theSearch, IDao theDao, SearchTask theSearchTask, ISearchBuilder theSearchBuilder) {
		super(theSearch.getUuid(), theDao);
		setSearchEntity(theSearch);
		mySearchTask = theSearchTask;
		mySearchBuilder = theSearchBuilder;
		mySearch = theSearch;
	}

	@Override
	public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
		checkForFailedSearch();
		List<Long> pids = mySearchTask.getResourcePids(theFromIndex, theToIndex);
		return toResourceList(mySearchBuilder, pids);
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
