package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchStatusEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
public class FhirResourceDaoR4SearchOptimizedTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchOptimizedTest.class);

	@After
	public final void after() {
	}

	@Before
	public void start() {
		runInTransaction(() -> {
			for (int i = 0; i < 200; i++) {
				Patient p = new Patient();
				p.setId("PT" + leftPad(Integer.toString(i), 5, '0'));
				p.setActive(true);
				p.addName().setFamily("FAM" + leftPad(Integer.toString(i), 5, '0'));
				myPatientDao.update(p);
			}
		});
	}

	@Test
	public void testFetchOnlySmallBatches() {

		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(20, 50, 190));

		/*
		 * Load the first page of 10
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));

		/*
		 * 20 should be prefetched since that's the initial page size
		 */

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(20, search.getTotalCount().intValue());
			assertEquals(search.getTotalCount().intValue(), mySearchResultDao.count());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		results = myPatientDao.search(params);
		ids = toUnqualifiedVersionlessIdValues(results, 10, 15, false);
		assertEquals("Patient/PT00010", ids.get(0));
		assertEquals("Patient/PT00014", ids.get(4));

		/*
		 * Search should be untouched
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(1, search.getVersion().intValue());
		});

		/*
		 * Now load a page that crosses the next threshold
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 15, 25, false);
		assertEquals("Patient/PT00015", ids.get(0));
		assertEquals("Patient/PT00024", ids.get(9));

		/*
		 * Search gets incremented twice as a part of loading the next batch
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(50, search.getTotalCount().intValue());
			assertEquals(search.getTotalCount().intValue(), mySearchResultDao.count());
			assertEquals(3, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		results = myPatientDao.search(params);
		ids = toUnqualifiedVersionlessIdValues(results, 25, 30, false);
		assertEquals("Patient/PT00025", ids.get(0));
		assertEquals("Patient/PT00029", ids.get(4));

		/*
		 * Search should be untouched
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(3, search.getVersion().intValue());
		});

		/*
		 * Now load a page that crosses the next threshold
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 50, 60, false);
		assertEquals("Patient/PT00050", ids.get(0));
		assertEquals("Patient/PT00059", ids.get(9));

		/*
		 * Search gets incremented twice as a part of loading the next batch
		 */
		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(190, search.getTotalCount().intValue());
			assertEquals(search.getTotalCount().intValue(), mySearchResultDao.count());
			assertEquals(5, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.FINISHED, search.getStatus());
		});

		/*
		 * Finally, load a page at the very end of the possible pages
		 */

		ids = toUnqualifiedVersionlessIdValues(results, 180, 200, false);
		assertEquals(10, ids.size());
		assertEquals("Patient/PT00180", ids.get(0));
		assertEquals("Patient/PT00189", ids.get(9));


	}


	@Test
	public void testFetchSecondBatchInManyThreads() throws Throwable {

		myDaoConfig.setSearchPreFetchThresholds(Arrays.asList(20, -1));

		/*
		 * Load the first page of 10
		 */

		SearchParameterMap params = new SearchParameterMap();
		params.setSort(new SortSpec(Patient.SP_NAME));
		final IBundleProvider results = myPatientDao.search(params);
		String uuid = results.getUuid();
		List<String> ids = toUnqualifiedVersionlessIdValues(results, 0, 10, true);
		assertEquals("Patient/PT00000", ids.get(0));
		assertEquals("Patient/PT00009", ids.get(9));

		/*
		 * 20 should be prefetched since that's the initial page size
		 */

		runInTransaction(() -> {
			Search search = mySearchEntityDao.findByUuid(uuid);
			assertEquals(20, search.getTotalCount().intValue());
			assertEquals(search.getTotalCount().intValue(), mySearchResultDao.count());
			assertEquals(1, search.getVersion().intValue());
			assertEquals(SearchStatusEnum.PASSCMPLET, search.getStatus());
		});

		/*
		 * Load a few more that shouldn't require a new page fetch
		 */

		ThreadPoolExecutorFactoryBean executorFactory = new ThreadPoolExecutorFactoryBean();
		executorFactory.setCorePoolSize(20);
		executorFactory.setMaxPoolSize(20);
		executorFactory.afterPropertiesSet();
		ExecutorService executor = executorFactory.getObject();

		List<Future<Throwable>> futures = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			int finalI = i;
			Future<Throwable> future = executor.submit(() -> {
				try {
					List<String> ids1 = toUnqualifiedVersionlessIdValues(results, 180, 190, false);
					assertEquals("Patient/PT00180", ids1.get(0));
					assertEquals("Patient/PT00189", ids1.get(9));
				} catch (Throwable t) {
					ourLog.error("Exception in thread {} - {}", finalI, t.toString());
					return t;
				}
				return null;
			});
			futures.add(future);
		}

		for (Future<Throwable> next : futures) {
			Throwable t = next.get();
			if (t!=null) {
				throw t;
			}
		}
		executor.shutdownNow();
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
