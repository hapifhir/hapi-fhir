package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Figure out how we're going to run the query up front, and build a branchless strategy object.
 */
public class SearchStrategyFactory {
	private final DaoConfig myDaoConfig;
	@Nullable
	private final IFulltextSearchSvc myFulltextSearchSvc;

	public interface ISearchStrategy extends Supplier<IBundleProvider> {

	}

	// someday
//	public class DirectHSearch implements  ISearchStrategy {};
//	public class JPAOffsetSearch implements  ISearchStrategy {};
//	public class JPASavedSearch implements  ISearchStrategy {};
//	public class JPAHybridHSearchSavedSearch implements  ISearchStrategy {};
//	public class SavedSearchAdaptorStrategy implements  ISearchStrategy {};

	public SearchStrategyFactory(DaoConfig theDaoConfig, @Nullable IFulltextSearchSvc theFulltextSearchSvc) {
		myDaoConfig = theDaoConfig;
		myFulltextSearchSvc = theFulltextSearchSvc;
	}

	public boolean isSupportsHSearchDirect(String theResourceType, SearchParameterMap theParams, RequestDetails theRequestDetails) {
		return
			myFulltextSearchSvc != null &&
			myDaoConfig.isStoreResourceInLuceneIndex() &&
			myDaoConfig.isAdvancedLuceneIndexing() &&
			myFulltextSearchSvc.supportsAllOf(theParams) &&
			theParams.getSummaryMode() == null &&
			theParams.getSearchTotalMode() == null;
	}

	public ISearchStrategy makeDirectStrategy(String theSearchUUID, String theResourceType, SearchParameterMap theParams, RequestDetails theRequestDetails) {
		return () -> {
			List<IBaseResource> resources = myFulltextSearchSvc.searchForResources(theResourceType, theParams);
			SimpleBundleProvider result = new SimpleBundleProvider(resources, theSearchUUID);
			// we don't know the size
			result.setSize(null);
			return result;
		};
	}

}
