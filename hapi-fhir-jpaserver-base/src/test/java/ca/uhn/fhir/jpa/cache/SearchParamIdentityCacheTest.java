package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchParamIdentityCacheTest {

	@InjectMocks
	SearchParamIdentityCache mySearchParamIdentityCache;

	@Mock
	IResourceIndexedSearchParamIdentityDao myResourceIndexedSearchParamIdentityDao;

	@Mock
	PlatformTransactionManager myTxManager;

	@Test
	public void findOrCreateSearchParamIdentity_identityExistsInCache_searchNotExecuted() {
		Map<Long, Integer> hashIdentityToSearchParamIdMap = new HashMap<>();
		hashIdentityToSearchParamIdMap.put(-7533943853970611242L, 1);
		mySearchParamIdentityCache.setHashIdentityToSearchParamIdMap(hashIdentityToSearchParamIdMap);

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

		// verify
		waitOneSecond();
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInCacheExistsInDb_saveNotExecuted() {
		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

		// verify
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() ->
			verify(myResourceIndexedSearchParamIdentityDao, times(1)).save(any(IndexedSearchParamIdentity.class)));
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInCacheThanCacheHit_findNotExecuted() {
		// setup
		// first map invocation returns null (cache miss), second invocation returns 1 (cache hit)
		Map<Long, Integer> hashIdentityToSearchParamIdMap = new HashMap<>() {
			private boolean firstCall = true;

			@Override
			public Integer get(Object key) {
				if (firstCall) {
					firstCall = false;
					return null;
				}
				return 12345;
			}
		};
		mySearchParamIdentityCache.setHashIdentityToSearchParamIdMap(hashIdentityToSearchParamIdMap);

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

		// verify
		waitOneSecond();
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
		verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInDbSaveFailedRetryable_saveRetried() {
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new DataIntegrityViolationException("Entity Exists!"));

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

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
		IndexedSearchParamIdentity identity = new IndexedSearchParamIdentity();
		identity.setSpIdentityId(12345);
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new DataIntegrityViolationException("Save Failed!")).thenReturn(identity);

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(2)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(2)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	@Test
	public void findOrCreateSearchParamIdentity_missingInDbSaveFailedNonRetryable_saveNotRetried() {
		when(myResourceIndexedSearchParamIdentityDao.save(any(IndexedSearchParamIdentity.class)))
			.thenThrow(new RuntimeException("Save Failed!"));

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

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
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

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
		mySearchParamIdentityCache.initCache();

		// execute
		mySearchParamIdentityCache.findOrCreateSearchParamIdentity(-7533943853970611242L, "given", "Patient");

		// verify
		waitOneSecond();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			verify(myResourceIndexedSearchParamIdentityDao, times(0)).getSearchParameterIdByHashIdentity(anyLong());
			verify(myResourceIndexedSearchParamIdentityDao, times(0)).save(any(IndexedSearchParamIdentity.class));
		});
	}

	private void waitOneSecond() {
		await().atLeast(1, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> true);
	}
}
