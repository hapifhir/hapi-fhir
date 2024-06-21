package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchCoordinatorSvcImplTest extends BaseJpaR4Test {


	@Autowired
	private ISearchDao mySearchDao;

	@Autowired
	private ISearchResultDao mySearchResultDao;

	@Autowired
	private ISearchCacheSvc myDatabaseCacheSvc;

	@AfterEach
	public void after() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(DatabaseSearchCacheSvcImpl.DEFAULT_MAX_RESULTS_TO_DELETE_IN_ONE_PAS);
	}

	/**
	 * Semi-obsolete test.  This used to test incremental deletion, but we now work until done or a timeout.
	 */
	@Test
	public void testDeleteDontMarkPreviouslyMarkedSearchesAsDeleted() {
		DatabaseSearchCacheSvcImpl.setMaximumResultsToDeleteInOnePassForUnitTest(5);

		runInTransaction(()->{
			mySearchResultDao.deleteAll();
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

		myDatabaseCacheSvc.pollForStaleSearchesAndDeleteThem(RequestPartitionId.allPartitions(), Instant.now().plus(10, ChronoUnit.SECONDS));
		runInTransaction(()->{
			// We should delete up to 10, but 3 don't get deleted since they have too many results to delete in one pass
			assertEquals(0, mySearchDao.count());
			assertEquals(0, mySearchDao.countDeleted());
			// We delete a max of 5 results per search, so half are gone
			assertEquals(0, mySearchResultDao.count());
		});
	}

}
