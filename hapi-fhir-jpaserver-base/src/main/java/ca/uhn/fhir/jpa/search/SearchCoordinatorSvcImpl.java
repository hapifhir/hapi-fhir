package ca.uhn.fhir.jpa.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.PlatformTransactionManager;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamUriDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.server.IBundleProvider;

public class SearchCoordinatorSvcImpl {

	
	public SearchCoordinatorSvcImpl() {
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("search_coord_");
		myExecutor = Executors.newCachedThreadPool(threadFactory);
	}
	
	public IBundleProvider registerSearch(IDao theCallingDao, SearchParameterMap theParams) {
		StopWatch w = new StopWatch();

		if (theParams.isLoadSynchronous()) {
			SearchBuilder sb = theCallingDao.newSearchBuilder();
			Iterator<Long> resultIter = sb.createQuery(theParams);
			
			// Load the results synchronously
			List<Long> pids = new ArrayList<Long>();
			while (resultIter.hasNext()) {
				pids.add(resultIter.next());
				if (theParams.getLoadSynchronousUpTo() != null && pids.size() >= theParams.getLoadSynchronousUpTo()) {
					break;
				}
			}
			
			resources = sb.loadResourcesByPid(pids, theResourceListToPopulate, theRevIncludedPids, theForHistoryOperation, entityManager, context, theDao);
		}

		mySearchEntity = new Search();
		mySearchEntity.setUuid(UUID.randomUUID().toString());
		mySearchEntity.setCreated(new Date());
		mySearchEntity.setTotalCount(-1);
		mySearchEntity.setPreferredPageSize(myParams.getCount());
		mySearchEntity.setSearchType(myParams.getEverythingMode() != null ? SearchTypeEnum.EVERYTHING : SearchTypeEnum.SEARCH);
		mySearchEntity.setLastUpdated(myParams.getLastUpdated());
		mySearchEntity.setResourceType(myResourceName);

		for (Include next : myParams.getIncludes()) {
			mySearchEntity.getIncludes().add(new SearchInclude(mySearchEntity, next.getValue(), false, next.isRecurse()));
		}
		for (Include next : myParams.getRevIncludes()) {
			mySearchEntity.getIncludes().add(new SearchInclude(mySearchEntity, next.getValue(), true, next.isRecurse()));
		}

		List<Long> firstPage = loadSearchPage(theParams, 0, 999);
		mySearchEntity.setTotalCount(firstPage.size());

		myEntityManager.persist(mySearchEntity);
		for (SearchInclude next : mySearchEntity.getIncludes()) {
			myEntityManager.persist(next);
		}

		IBundleProvider retVal = doReturnProvider();

		ourLog.info("Search initial phase completed in {}ms", w);
		return retVal;

		
	}
	
}
