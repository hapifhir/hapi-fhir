/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.search.ISearchParamHashIdentityRegistry;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SearchParamIdentityCache {
	private static final Logger ourLog = getLogger(SearchParamIdentityCache.class);

	private static final int MAX_RETRY_COUNT = 20;
	private static final String CACHE_THREAD_PREFIX = "searchparemeteridentity-cache-";
	private static final int THREAD_POOL_MAX_POOL_SIZE = 1000;
	private static final int THREAD_POOL_QUEUE_SIZE = 1000;
	private Map<Long, Integer> myHashIdentityToSearchParamId = new ConcurrentHashMap<>();
	private final IResourceIndexedSearchParamIdentityDao mySearchParamIdentityDao;
	private final TransactionTemplate myTxTemplate;
	private final ExecutorService myThreadPool;
	private final ISearchParamHashIdentityRegistry mySearchParamHashIdentityRegistry;
	private final UniqueTaskExecutor uniqueTaskExecutor;

	public SearchParamIdentityCache(
			IResourceIndexedSearchParamIdentityDao theResourceIndexedSearchParamIdentityDao,
			ISearchParamHashIdentityRegistry theSearchParamHashIdentityRegistry,
			PlatformTransactionManager theTxManager) {
		this.mySearchParamIdentityDao = theResourceIndexedSearchParamIdentityDao;
		myTxTemplate = new TransactionTemplate(theTxManager);
		mySearchParamHashIdentityRegistry = theSearchParamHashIdentityRegistry;
		myTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		myThreadPool = createExecutor();
		uniqueTaskExecutor = new UniqueTaskExecutor(myThreadPool);
	}

	private ExecutorService createExecutor() {
		ThreadFactory threadFactory = r -> {
			Thread t = new Thread(r);
			t.setName(CACHE_THREAD_PREFIX + t.getId());
			t.setDaemon(false);
			return t;
		};

		return new ThreadPoolExecutor(
				1,
				THREAD_POOL_MAX_POOL_SIZE,
				0L,
				TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<>(THREAD_POOL_QUEUE_SIZE),
				threadFactory,
				new ThreadPoolExecutor.DiscardPolicy());
	}

	@VisibleForTesting
	void setHashIdentityToSearchParamIdMap(Map<Long, Integer> theHashIdentityToSearchParamIdMap) {
		myHashIdentityToSearchParamId = theHashIdentityToSearchParamIdMap;
	}

	@PostConstruct
	public void start() {
		initCache();
	}

	@PreDestroy
	public void preDestroy() {
		myThreadPool.shutdown();
	}

	protected void initCache() {
		// populate cache with IndexedSearchParamIdentities from database
		Collection<Object[]> pids =
				Objects.requireNonNull(myTxTemplate.execute(t -> mySearchParamIdentityDao.getAllHashIdentities()));
		myHashIdentityToSearchParamId = pids.stream().collect(Collectors.toMap(i -> (Long) i[0], i -> (Integer) i[1]));
		// pre-fill cache with SearchParams from SearchParamRegistry
		mySearchParamHashIdentityRegistry
				.getHashIdentityToIndexedSearchParamMap()
				.forEach((hashIdentity, indexedSearchParam) -> {
					if (!myHashIdentityToSearchParamId.containsKey(hashIdentity)) {
						PersistSearchParameterIdentityTask persistSpIdentityTask =
								new PersistSearchParameterIdentityTask.Builder()
										.hashIdentity(hashIdentity)
										.resourceType(indexedSearchParam.getResourceType())
										.paramName(indexedSearchParam.getParameterName())
										.hashIdentityToSearchParamId(myHashIdentityToSearchParamId)
										.txTemplate(myTxTemplate)
										.searchParamIdentityDao(mySearchParamIdentityDao)
										.build();

						uniqueTaskExecutor.submitIfAbsent(persistSpIdentityTask);
					}
				});
	}

	public void findOrCreateSearchParamIdentity(Long theHashIdentity, String theResourceType, String theParamName) {
		// check if SearchParamIdentity is already in cache
		Integer spIdentityId = myHashIdentityToSearchParamId.get(theHashIdentity);

		if (spIdentityId != null) {
			return;
		}

		// cache miss, create SearchParamIdentity in separate thread
		PersistSearchParameterIdentityTask persistSpIdentityTask = new PersistSearchParameterIdentityTask.Builder()
				.hashIdentity(theHashIdentity)
				.resourceType(theResourceType)
				.paramName(theParamName)
				.hashIdentityToSearchParamId(myHashIdentityToSearchParamId)
				.txTemplate(myTxTemplate)
				.searchParamIdentityDao(mySearchParamIdentityDao)
				.build();

		uniqueTaskExecutor.submitIfAbsent(persistSpIdentityTask);
	}

	public static class PersistSearchParameterIdentityTask implements Callable<Void> {

		private final Long myHashIdentity;
		private final String myResourceType;
		private final String myParamName;
		private final TransactionTemplate myTxTemplate;
		private final Map<Long, Integer> myHashIdentityToSearchParamId;
		private final IResourceIndexedSearchParamIdentityDao myResourceIndexedSearchParamIdentityDao;

		private PersistSearchParameterIdentityTask(Builder theBuilder) {
			this.myHashIdentity = theBuilder.myHashIdentity;
			this.myResourceType = theBuilder.myResourceType;
			this.myParamName = theBuilder.myParamName;
			this.myTxTemplate = theBuilder.myTxTemplate;
			this.myHashIdentityToSearchParamId = theBuilder.myHashIdentityToSearchParamId;
			this.myResourceIndexedSearchParamIdentityDao = theBuilder.mySearchParamIdentityDao;
		}

		public Long getHashIdentity() {
			return myHashIdentity;
		}

		public static class Builder {
			private Long myHashIdentity;
			private String myResourceType;
			private String myParamName;
			private TransactionTemplate myTxTemplate;
			private Map<Long, Integer> myHashIdentityToSearchParamId;
			private IResourceIndexedSearchParamIdentityDao mySearchParamIdentityDao;

			public Builder hashIdentity(Long theHashIdentity) {
				this.myHashIdentity = theHashIdentity;
				return this;
			}

			public Builder resourceType(String theResourceType) {
				this.myResourceType = theResourceType;
				return this;
			}

			public Builder paramName(String theParamName) {
				this.myParamName = theParamName;
				return this;
			}

			public Builder txTemplate(TransactionTemplate theTxTemplate) {
				this.myTxTemplate = theTxTemplate;
				return this;
			}

			public Builder hashIdentityToSearchParamId(Map<Long, Integer> theHashIdentityToSearchParamId) {
				this.myHashIdentityToSearchParamId = theHashIdentityToSearchParamId;
				return this;
			}

			public Builder searchParamIdentityDao(IResourceIndexedSearchParamIdentityDao theSearchParamIdentityDao) {
				this.mySearchParamIdentityDao = theSearchParamIdentityDao;
				return this;
			}

			public PersistSearchParameterIdentityTask build() {
				return new PersistSearchParameterIdentityTask(this);
			}
		}

		@Override
		public Void call() throws Exception {
			Integer spIdentityId;
			int retry = 0;
			while (retry++ < MAX_RETRY_COUNT) {
				spIdentityId = myHashIdentityToSearchParamId.get(myHashIdentity);

				if (spIdentityId != null) {
					return null;
				}

				try {
					// try to retrieve search parameter identity from db, create if missing, update cache
					spIdentityId = findOrCreateIndexedSearchParamIdentity(myHashIdentity, myParamName, myResourceType);
					myHashIdentityToSearchParamId.put(myHashIdentity, spIdentityId);
					return null;
				} catch (DataIntegrityViolationException theDataIntegrityViolationException) {
					// retryable exception - unique search parameter identity or hash identity constraint violation
					ourLog.debug(
							"Failed to save SearchParamIndexIdentity for search parameter with hash identity: {}, "
									+ "paramName: {}, resourceType: {}, retrying attempt: {}",
							myHashIdentity,
							myParamName,
							myResourceType,
							retry,
							theDataIntegrityViolationException);
				}
				ourLog.warn(
						"Failed saving IndexedSearchParamIdentity with hash identity: {}, paramName: {}, "
								+ "resourceType: {}",
						myHashIdentity,
						myParamName,
						myResourceType);
			}
			return null;
		}

		private Integer findOrCreateIndexedSearchParamIdentity(
				Long theHashIdentity, String theParamName, String theResourceType) {

			return myTxTemplate.execute(tx -> {
				IndexedSearchParamIdentity identity =
						myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(theHashIdentity);
				if (identity != null) {
					ourLog.info(
							"DB read success IndexedSearchParamIdentity with hash identity: {}, paramName: {}, "
									+ "resourceType: {}",
							theHashIdentity,
							theParamName,
							theResourceType);
					return identity.getSpIdentityId();
				}

				IndexedSearchParamIdentity indexedSearchParamIdentity = new IndexedSearchParamIdentity();
				indexedSearchParamIdentity.setHashIdentity(theHashIdentity);
				indexedSearchParamIdentity.setParamName(theParamName);
				indexedSearchParamIdentity.setResourceType(theResourceType);

				myResourceIndexedSearchParamIdentityDao.save(indexedSearchParamIdentity);
				ourLog.info(
						"Write success search param identity {}, paramName: {}, resourceType: {}",
						theHashIdentity,
						theParamName,
						theResourceType);
				return indexedSearchParamIdentity.getSpIdentityId();
			});
		}
	}

	/**
	 * Ensures only one instance of the PersistSearchParameterIdentityTask is running per hash identity.
	 * If a task is already in progress, it will not be scheduled again.
	 */
	private static class UniqueTaskExecutor {
		private final ExecutorService myExecutor;
		private final ConcurrentHashMap<Long, Future<Void>> myInFlightTasks = new ConcurrentHashMap<>();

		public UniqueTaskExecutor(ExecutorService theExecutor) {
			myExecutor = theExecutor;
		}

		public void submitIfAbsent(PersistSearchParameterIdentityTask theTask) {
			Long key = theTask.getHashIdentity();

			// If there's already a Future in flight, reuse it.
			Future<?> existing = myInFlightTasks.get(key);
			if (existing != null) {
				return;
			}

			// Put FutureTask in the map. If another thread already put it - skip scheduling.
			FutureTask<Void> futureTask = new FutureTask<>(theTask);
			existing = myInFlightTasks.putIfAbsent(key, futureTask);
			if (existing != null) {
				return;
			}

			// Schedule FutureTask, remove it from the myInFlightTasks map once it's done or fails.
			myExecutor.execute(() -> {
				try {
					futureTask.run();
				} finally {
					myInFlightTasks.remove(key, futureTask);
				}
			});
		}
	}
}
