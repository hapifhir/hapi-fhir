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
package ca.uhn.fhir.jpa.sp;

import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamIdentityDao;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.search.ISearchParamHashIdentityRegistry;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SearchParamIdentityCacheSvcImpl implements ISearchParamIdentityCacheSvc {
	private static final Logger ourLog = getLogger(SearchParamIdentityCacheSvcImpl.class);

	private static final int MAX_RETRY_COUNT = 20;
	private static final String CACHE_THREAD_PREFIX = "search-parameter-identity-cache-";
	private static final int THREAD_POOL_MAX_POOL_SIZE = 1000;
	private static final int THREAD_POOL_QUEUE_SIZE = 1000;
	private final IResourceIndexedSearchParamIdentityDao mySearchParamIdentityDao;
	private final TransactionTemplate myTxTemplate;
	private final ExecutorService myThreadPool;
	private final ISearchParamHashIdentityRegistry mySearchParamHashIdentityRegistry;
	private final MemoryCacheService myMemoryCacheService;
	private final UniqueTaskExecutor myUniqueTaskExecutor;

	public SearchParamIdentityCacheSvcImpl(
			IResourceIndexedSearchParamIdentityDao theResourceIndexedSearchParamIdentityDao,
			ISearchParamHashIdentityRegistry theSearchParamHashIdentityRegistry,
			PlatformTransactionManager theTxManager,
			MemoryCacheService theMemoryCacheService) {
		this.mySearchParamIdentityDao = theResourceIndexedSearchParamIdentityDao;
		myTxTemplate = new TransactionTemplate(theTxManager);
		mySearchParamHashIdentityRegistry = theSearchParamHashIdentityRegistry;
		myTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		myThreadPool = createExecutor();
		myMemoryCacheService = theMemoryCacheService;
		myUniqueTaskExecutor = new UniqueTaskExecutor(myThreadPool, myMemoryCacheService);
	}

	/**
	 * Creates a thread pool executor for asynchronously executing
	 * {@link PersistSearchParameterIdentityTask} instances.
	 * <p>
	 * Uses a fixed core pool size of 1, a maximum pool size of 1000,
	 * and a bounded queue with a capacity of 1000.
	 * <p>
	 * If the queue is full and all threads are busy, new tasks are silently
	 * discarded using {@link ThreadPoolExecutor.DiscardPolicy}.
	 *
	 * @return a configured {@link ExecutorService} instance
	 */
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

	@PostConstruct
	public void start() {
		initCache();
	}

	@PreDestroy
	public void preDestroy() {
		myThreadPool.shutdown();
	}

	/**
	 * Initializes the cache by preloading search parameter identities {@link IndexedSearchParamIdentity}
	 * from the database and the search parameter registry {@link ISearchParamHashIdentityRegistry}.
	 */
	protected void initCache() {
		// populate cache with IndexedSearchParamIdentities from database
		Collection<Object[]> spIdentities =
				Objects.requireNonNull(myTxTemplate.execute(t -> mySearchParamIdentityDao.getAllHashIdentities()));
		spIdentities.forEach(
				i -> CacheUtils.putSearchParamIdentityToCache(myMemoryCacheService, (Long) i[0], (Integer) i[1]));

		// pre-fill cache with SearchParamIdentities from SearchParamRegistry
		mySearchParamHashIdentityRegistry
				.getHashIdentityToIndexedSearchParamMap()
				.forEach((hashIdentity, indexedSearchParam) -> {
					if (CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, hashIdentity) == null) {
						PersistSearchParameterIdentityTask persistSpIdentityTask =
								new PersistSearchParameterIdentityTask.Builder()
										.hashIdentity(hashIdentity)
										.resourceType(indexedSearchParam.getResourceType())
										.paramName(indexedSearchParam.getParameterName())
										.memoryCacheService(myMemoryCacheService)
										.txTemplate(myTxTemplate)
										.searchParamIdentityDao(mySearchParamIdentityDao)
										.build();

						myUniqueTaskExecutor.submitIfAbsent(persistSpIdentityTask);
					}
				});
	}

	/**
	 * Asynchronously ensures that a {@link IndexedSearchParamIdentity} exists for the given
	 * hash identity, parameter name, and resource type. If the identity is already present
	 * in the in-memory cache, no action is taken.
	 *
	 * <p>If the identity is missing, a {@link PersistSearchParameterIdentityTask} is created
	 * and submitted for asynchronous execution. To avoid modifying the cache during an
	 * active transaction, task submission is deferred until after the transaction is committed.
	 *
	 * @param theHashIdentity The hash identity representing the search parameter.
	 * @param theResourceType The resource type (e.g., "Patient", "Observation").
	 * @param theParamName    The search parameter name.
	 *
	 * @see PersistSearchParameterIdentityTask
	 */
	public void findOrCreateSearchParamIdentity(Long theHashIdentity, String theResourceType, String theParamName) {
		// check if SearchParamIdentity is already in cache
		Integer spIdentityId = CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, theHashIdentity);

		if (spIdentityId != null) {
			return;
		}

		// cache miss, create PersistSearchParameterIdentityTask to execute it in separate thread
		PersistSearchParameterIdentityTask persistSpIdentityTask = new PersistSearchParameterIdentityTask.Builder()
				.hashIdentity(theHashIdentity)
				.resourceType(theResourceType)
				.paramName(theParamName)
				.memoryCacheService(myMemoryCacheService)
				.txTemplate(myTxTemplate)
				.searchParamIdentityDao(mySearchParamIdentityDao)
				.build();

		// submit PersistSearchParameterIdentityTask only if current transaction was commited
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					myUniqueTaskExecutor.submitIfAbsent(persistSpIdentityTask);
				}
			});
		} else {
			myUniqueTaskExecutor.submitIfAbsent(persistSpIdentityTask);
		}
	}

	/**
	 * This class is responsible for ensuring that a unique {@link IndexedSearchParamIdentity}
	 * exists for a given hash identity (parameter name and resource type). This task is
	 * executed asynchronously to avoid blocking the main thread during persistence.
	 *
	 * <p>
	 * This task checks the in-memory cache for the given hash identity and, if missing,
	 * attempts to create or retrieve the corresponding {@link IndexedSearchParamIdentity}
	 * from the database. The result is then added to the cache.
	 *
	 * <p>
	 * Up to 20 retries are permitted in case of a {@link DataIntegrityViolationException},
	 * which can occur due to concurrent insert attempts for the same identity. If all retries
	 * fail, the {@link IndexedSearchParamIdentity} will not be saved during this execution,
	 * but the task may be retried later when submitted again.
	 *
	 * @see IndexedSearchParamIdentity
	 * @see MemoryCacheService
	 */
	public static class PersistSearchParameterIdentityTask implements Callable<Void> {

		private final Long myHashIdentity;
		private final String myResourceType;
		private final String myParamName;
		private final TransactionTemplate myTxTemplate;
		private final MemoryCacheService myMemoryCacheService;
		private final IResourceIndexedSearchParamIdentityDao myResourceIndexedSearchParamIdentityDao;

		private PersistSearchParameterIdentityTask(Builder theBuilder) {
			this.myHashIdentity = theBuilder.myHashIdentity;
			this.myResourceType = theBuilder.myResourceType;
			this.myParamName = theBuilder.myParamName;
			this.myTxTemplate = theBuilder.myTxTemplate;
			this.myMemoryCacheService = theBuilder.myMemoryCacheService;
			this.myResourceIndexedSearchParamIdentityDao = theBuilder.mySearchParamIdentityDao;
		}

		public Long getHashIdentity() {
			return myHashIdentity;
		}

		public String getMyResourceType() {
			return myResourceType;
		}

		public String getMyParamName() {
			return myParamName;
		}

		public static class Builder {
			private Long myHashIdentity;
			private String myResourceType;
			private String myParamName;
			private TransactionTemplate myTxTemplate;
			private MemoryCacheService myMemoryCacheService;
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

			public Builder memoryCacheService(MemoryCacheService theMemoryCacheService) {
				this.myMemoryCacheService = theMemoryCacheService;
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
				spIdentityId = CacheUtils.getSearchParamIdentityFromCache(myMemoryCacheService, myHashIdentity);

				if (spIdentityId != null) {
					return null;
				}

				try {
					// try to retrieve search parameter identity from db, create if missing, update cache
					spIdentityId = findOrCreateIndexedSearchParamIdentity(myHashIdentity, myParamName, myResourceType);
					CacheUtils.putSearchParamIdentityToCache(myMemoryCacheService, myHashIdentity, spIdentityId);
					return null;
				} catch (DataIntegrityViolationException theDataIntegrityViolationException) {
					// retryable exception - unique search parameter identity or hash identity constraint violation
					ourLog.trace(
							"Failed to save SearchParamIndexIdentity for search parameter with hash identity: {}, "
									+ "paramName: {}, resourceType: {}, retrying attempt: {}",
							myHashIdentity,
							myParamName,
							myResourceType,
							retry,
							theDataIntegrityViolationException);
				}
				ourLog.warn(
						"Failed saving IndexedSearchParamIdentity with hash identity: {}, paramName: {}, resourceType: {}",
						myHashIdentity,
						myResourceType,
						myParamName);
			}
			return null;
		}

		private Integer findOrCreateIndexedSearchParamIdentity(
				Long theHashIdentity, String theParamName, String theResourceType) {

			return myTxTemplate.execute(tx -> {
				IndexedSearchParamIdentity identity =
						myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(theHashIdentity);
				if (identity != null) {
					ourLog.trace(
							"DB read success IndexedSearchParamIdentity with hash identity: {}, resourceType: {}, paramName: {}",
							theHashIdentity,
							theResourceType,
							theParamName);
					return identity.getSpIdentityId();
				}

				IndexedSearchParamIdentity indexedSearchParamIdentity = new IndexedSearchParamIdentity();
				indexedSearchParamIdentity.setHashIdentity(theHashIdentity);
				indexedSearchParamIdentity.setParamName(theParamName);
				indexedSearchParamIdentity.setResourceType(theResourceType);

				myResourceIndexedSearchParamIdentityDao.save(indexedSearchParamIdentity);
				ourLog.trace(
						"Write success IndexedSearchParamIdentity with hash identity: {}, resourceType: {}, paramName: {},",
						theHashIdentity,
						theResourceType,
						theParamName);

				return indexedSearchParamIdentity.getSpIdentityId();
			});
		}
	}

	public static class CacheUtils {

		private CacheUtils() {}

		public static Integer getSearchParamIdentityFromCache(
				MemoryCacheService memoryCacheService, Long hashIdentity) {
			return memoryCacheService.getIfPresent(
					MemoryCacheService.CacheEnum.HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY, hashIdentity);
		}

		public static void putSearchParamIdentityToCache(
				MemoryCacheService memoryCacheService, Long theHashIdentity, Integer theSpIdentityId) {
			memoryCacheService.put(
					MemoryCacheService.CacheEnum.HASH_IDENTITY_TO_SEARCH_PARAM_IDENTITY,
					theHashIdentity,
					theSpIdentityId);
		}
	}

	/**
	 * Ensures only one instance of the PersistSearchParameterIdentityTask is running per hash identity.
	 * If a task is already in progress, it will not be scheduled again.
	 */
	private static class UniqueTaskExecutor {
		private final ExecutorService myExecutor;
		private final MemoryCacheService myMemoryCacheService;

		public UniqueTaskExecutor(ExecutorService theExecutor, MemoryCacheService theMemoryCacheService) {
			myExecutor = theExecutor;
			myMemoryCacheService = theMemoryCacheService;
		}

		public void submitIfAbsent(PersistSearchParameterIdentityTask theTask) {
			Long hashIdentity = theTask.getHashIdentity();

			FutureTask<Void> newFutureTask = new LoggingFutureTask(theTask);

			FutureTask<Void> existingOrNewFutureTask = myMemoryCacheService.get(
					MemoryCacheService.CacheEnum.PERSIST_SEARCH_PARAM_IDENTITY_IN_FLIGHT_TASKS,
					hashIdentity,
					key -> newFutureTask);

			if (!Objects.equals(existingOrNewFutureTask, newFutureTask)) {
				// cache already have a task with same hashIdentity - skip scheduling
				return;
			}

			myExecutor.execute(() -> {
				try {
					newFutureTask.run();
				} finally {
					// remove from the cache once done or failed
					myMemoryCacheService.invalidate(
							MemoryCacheService.CacheEnum.PERSIST_SEARCH_PARAM_IDENTITY_IN_FLIGHT_TASKS, hashIdentity);
				}
			});
		}
	}

	/**
	 * A {@link FutureTask} implementation that logs any exception thrown
	 * during execution of a {@link PersistSearchParameterIdentityTask}.
	 * <p>
	 * Since {@link PersistSearchParameterIdentityTask} runs asynchronously in a
	 * separate thread, any exception it throws can only be observed by calling {@link #get()}.
	 * <p>
	 * This class overrides {@link #done()} to call {@code get()},
	 * and log {@link ExecutionException} or {@link InterruptedException}.
	 */
	private static class LoggingFutureTask extends FutureTask<Void> {
		private final Long myHashIdentity;
		private final String myResourceType;
		private final String myParamName;

		public LoggingFutureTask(PersistSearchParameterIdentityTask theTask) {
			super(theTask);
			this.myHashIdentity = theTask.getHashIdentity();
			this.myResourceType = theTask.getMyResourceType();
			this.myParamName = theTask.getMyParamName();
		}

		@Override
		protected void done() {
			try {
				get();
			} catch (ExecutionException theException) {
				ourLog.error(
						"PersistSearchParameterIdentityTask failed. Hash identity: {}, resourceType: {}, paramName: {}, ",
						myHashIdentity,
						myResourceType,
						myParamName,
						theException.getCause());
			} catch (InterruptedException theInterruptedException) {
				Thread.currentThread().interrupt();
				ourLog.warn(
						"PersistSearchParameterIdentityTask was interrupted. Hash identity: {}, resourceType: {}, paramName: {}, ",
						myHashIdentity,
						myResourceType,
						myParamName,
						theInterruptedException);
			}
		}
	}
}
