package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

import static ca.uhn.fhir.jpa.search.cache.DatabaseSearchCacheSvcImpl.DEFAULT_MAX_DELETE_CANDIDATES_TO_FIND;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchCoordinatorSvcImplTest extends BaseJpaR4Test {


	@Autowired
	private ISearchDao mySearchDao;

	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;

	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinator;

	@Autowired
	private ISearchCacheSvc myDatabaseCacheSvc;

	@AfterEach
	public void after() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(DatabaseSearchCacheSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS);
		DatabaseSearchCacheSvcImpl.setMaximumSearchesToCheckForDeletionCandidacyForUnitTest(DEFAULT_MAX_DELETE_CANDIDATES_TO_FIND);
	}

	@Test
	public void testDeleteDontMarkPreviouslyMarkedSearchesAsDeleted() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(5);
		DatabaseSearchCacheSvcImpl.setMaximumSearchesToCheckForDeletionCandidacyForUnitTest(10);

		runInTransaction(()->{
			mySearchResultDao.deleteAll();
			mySearchIncludeDao.deleteAll();
			mySearchDao.deleteAll();
		});
		runInTransaction(()->{
			assertEquals(0, mySearchDao.count());
			assertEquals(0, mySearchResultDao.count());
		});

		// Create lots of searches
		runInTransaction(()->{
			for (int i = 0; i < 20; i++) {
				Search search = new Search();
				search.setCreated(DateUtils.addDays(new Date(), -1));
				search.setLastUpdated(DateUtils.addDays(new Date(), -1), DateUtils.addDays(new Date(), -1));
				search.setUuid(UUID.randomUUID().toString());
				search.setSearchType(SearchTypeEnum.SEARCH);
				search.setStatus(SearchStatusEnum.FINISHED);
				mySearchDao.save(search);

				// Add a bunch of search results to a few (enough that it will take multiple passes)
				if (i < 3) {
					for (int j = 0; j < 10; j++) {
						SearchResult sr = new SearchResult(search);
						sr.setOrder(j);
						sr.setResourcePid((long) j);
						mySearchResultDao.save(sr);
					}
				}

			}
		});

		runInTransaction(()->{
			assertEquals(20, mySearchDao.count());
			assertEquals(30, mySearchResultDao.count());
		});

		myDatabaseCacheSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(()->{
			// We should delete up to 10, but 3 don't get deleted since they have too many results to delete in one pass
			assertEquals(13, mySearchDao.count());
			assertEquals(3, mySearchDao.countDeleted());
			// We delete a max of 5 results per search, so half are gone
			assertEquals(15, mySearchResultDao.count());
		});

		myDatabaseCacheSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(()->{
			// Once again we attempt to delete 10, but the first 3 don't get deleted and still remain
			// (total is 6 because 3 weren't deleted, and they blocked another 3 that might have been)
			assertEquals(6, mySearchDao.count());
			assertEquals(6, mySearchDao.countDeleted());
			assertEquals(0, mySearchResultDao.count());
		});

		myDatabaseCacheSvc.pollForStaleSearchesAndDeleteThem();
		runInTransaction(()->{
			assertEquals(0, mySearchDao.count());
			assertEquals(0, mySearchDao.countDeleted());
			assertEquals(0, mySearchResultDao.count());
		});
	}

}
