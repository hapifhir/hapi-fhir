package ca.uhn.fhir.jpa.sp;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.search.ISearchParamHashIdentityRegistry;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.server.util.IndexedSearchParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParamIdentityCacheSvcImplTest {

	@InjectMocks
	SearchParamIdentityCacheSvcImpl mySearchParamIdentityCacheSvc;

	@Mock
	IResourceIndexedSearchParamIdentityDao myResourceIndexedSearchParamIdentityDao;

	@Mock
	PlatformTransactionManager myTxManager;

	@Mock
	ISearchParamHashIdentityRegistry mySearchParamHashIdentityRegistry;

	@Spy
	MemoryCacheService myMemoryCacheService = new MemoryCacheService(new JpaStorageSettings());

	@Test
	public void findOrCreateSearchParamIdentity_identityExistsInCache_searchNotExecuted() {
		// setup
		doReturn(12345).when(myMemoryCacheService)
			.getIfPresent(eq(MemoryCacheService.CacheEnum.HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY), anyLong());

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInCacheMissingInDb_saveExecutedOnce() {
		// setup
		mockSearchParamIdentitySave();

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInCacheThanCacheHit_findNotExecuted() {
		// setup - first cache invocation returns null (cache miss), second invocation returns 12345 (cache hit)
		doReturn(null).doReturn(12345).when(myMemoryCacheService)
			.getIfPresent(eq(MemoryCacheService.CacheEnum.HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY), anyLong());

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInDbSaveFailedRetryable_saveRetried() {
		// setup
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new DataIntegrityViolationException("Entity Exists!"));

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(20)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(20)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInDbSaveFailedThanSucceed_saveRetriedOnce() {
		// setup
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new DataIntegrityViolationException("Save Failed!")).thenAnswer(invocation -> {
				IndexedSearchParamIdentity paramIdentity = invocation.getArgument(0);
				paramIdentity.setSpIdentityId(12345);
				return paramIdentity;
			});

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(2)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(2)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInDbSaveFailedNonRetryable_saveNotRetried() {
		// setup
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new RuntimeException("Save Failed!"));

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_existsInDb_saveNotExecuted() {
		// setup
		IndexedSearchParamIdentity identity = new IndexedSearchParamIdentity();
		identity.setSpIdentityId(12345);
		when(myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(anyLong()))
			.thenReturn(identity);

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_withInitCache_findNotExecuted() {
		// setup
		when(myResourceIndexedSearchParamIdentityDao.getAllHashIdentities())
			.thenReturn(Collections.singletonList(new Object[]{-7533943853970611242L, 12345}));
		mySearchParamIdentityCacheSvc.initCache();

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_withInitCacheAndSpRegistryPreFill_saveExecutedOnce() {
		// setup
		Map<Long, IndexedSearchParam> searchParamRegistry = new HashMap<>();
		searchParamRegistry.put(-7533943853970611242L, new IndexedSearchParam("given", "Patient"));
		when(mySearchParamHashIdentityRegistry.getHashIdentityToIndexedSearchParamMap())
			.thenReturn(searchParamRegistry);
		mockSearchParamIdentitySave();
		mySearchParamIdentityCacheSvc.initCache();
		waitOneSecond();

		// execute
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_multipleThreadsCreateSameSearchParam_saveExecutedOnce() throws InterruptedException {
		// setup
		mockSearchParamIdentitySave();

		// execute
		ExecutorService executor = Executors.newFixedThreadPool(50);
		for (int i = 0; i < 50; i++) {
			executor.submit(() -> mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(-7533943853970611242L, "Patient", "given"));
		}
		executor.shutdown();

		// verify
		assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS)); // wait for all tasks to finish
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	private void mockSearchParamIdentitySave() {
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenAnswer(invocation -> {
				IndexedSearchParamIdentity paramIdentity = invocation.getArgument(0);
				paramIdentity.setSpIdentityId(12345);
				return paramIdentity;
			});
	}

	private void waitOneSecond() {
		await().atLeast(1, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> true);
	}
}
