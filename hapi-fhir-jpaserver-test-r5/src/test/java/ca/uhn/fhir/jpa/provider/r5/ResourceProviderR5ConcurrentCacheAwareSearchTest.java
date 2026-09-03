package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.util.ThreadPoolUtil;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static ca.uhn.fhir.rest.server.BasePagingProvider.DEFAULT_MAX_PAGE_SIZE;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class ResourceProviderR5ConcurrentCacheAwareSearchTest extends BaseResourceProviderR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5ConcurrentCacheAwareSearchTest.class);
	private ThreadPoolTaskExecutor myThreadPool;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myStorageSettings.setSearchPreFetchThresholds(List.of(12, 22, 1000));

		myThreadPool = ThreadPoolUtil.newThreadPool(10, "CacheUnitTest-");
	}


	@AfterEach
	@Override
	public void after() throws Exception {
		super.after();

		myPagingProvider.setMaximumPageSize(DEFAULT_MAX_PAGE_SIZE);
	}

	@Test
	void testFetchSecondPageConcurrently() throws ExecutionException, InterruptedException {
		// Setup
		List<String> ids = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			ids.add(createPatient(withFamily(leftPad(Integer.toString(i), 5, '0'))).toUnqualifiedVersionless().getValue());
		}

		Bundle firstPage = myClient
			.search()
			.forResource("Patient")
			.count(10)
			.returnBundle(Bundle.class)
			.sort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.ASC))
			.execute();
		assertThat(firstPage.getId()).isNotBlank();

		assertThat(toUnqualifiedVersionlessIdValues(firstPage)).asList().containsExactlyElementsOf(
			ids.subList(0, 10)
		);

		// Make the same search a second time
		Bundle firstPageSecondAttempt = myClient
			.search()
			.forResource("Patient")
			.count(10)
			.returnBundle(Bundle.class)
			.sort(new SortSpec(Patient.SP_FAMILY, SortOrderEnum.ASC))
			.execute();
		assertEquals(firstPage.getId(), firstPageSecondAttempt.getId());

		logAllSearches();
		ourLog.info("Bundle ID: {}", firstPage.getId());

		runInTransaction(() -> {
			// Make sure we've only fetched up to the first prefetch threshold
			Search searchEntity = mySearchEntityDao.findByUuidAndFetchIncludes(firstPage.getIdElement().getIdPart()).orElseThrow();
			assertEquals(13, searchEntity.getNumFound());
			assertEquals(SearchStatusEnum.PASSCMPLET, searchEntity.getStatus());
		});

		// Test

		Callable<List<String>> callable = () -> {
			Bundle bundle = myClient
				.loadPage()
				.next(firstPage)
				.execute();
			return toUnqualifiedVersionlessIdValues(bundle);
		};
		List<Future<List<String>>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Future<List<String>> future = myThreadPool.submit(callable);
			futures.add(future);
		}

		for (Future<List<String>> future : futures) {
			assertThat(future.get()).asList().containsExactlyElementsOf(
				ids.subList(10, 20)
			);
		}
	}


}
