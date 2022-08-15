package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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


import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.TransactionWriteOperationsDetails;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * This interceptor uses semaphores to avoid multiple concurrent FHIR transaction
 * bundles from processing the same records at the same time, avoiding concurrency
 * issues.
 */
@Interceptor
public class TransactionConcurrencySemaphoreInterceptor {

	private static final Logger ourLog = LoggerFactory.getLogger(TransactionConcurrencySemaphoreInterceptor.class);
	private static final String HELD_SEMAPHORES = TransactionConcurrencySemaphoreInterceptor.class.getName() + "_HELD_SEMAPHORES";
	private final Cache<String, Semaphore> mySemaphoreCache;
	private final MemoryCacheService myMemoryCacheService;
	private boolean myLogWaits;
	private final Semaphore myLockingSemaphore = new Semaphore(1);

	/**
	 * Constructor
	 */
	public TransactionConcurrencySemaphoreInterceptor(MemoryCacheService theMemoryCacheService) {
		myMemoryCacheService = theMemoryCacheService;
		mySemaphoreCache = Caffeine
			.newBuilder()
			.expireAfterAccess(1, TimeUnit.MINUTES)
			.build();
	}

	/**
	 * Should the interceptor log if a wait for a semaphore is required
	 */
	public boolean isLogWaits() {
		return myLogWaits;
	}

	/**
	 * Should the interceptor log if a wait for a semaphore is required
	 */
	public void setLogWaits(boolean theLogWaits) {
		myLogWaits = theLogWaits;
	}

	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE)
	public void pre(TransactionDetails theTransactionDetails, TransactionWriteOperationsDetails theWriteOperationsDetails) {
		List<Semaphore> heldSemaphores = new ArrayList<>();
		Map<String, Semaphore> pendingAndHeldSemaphores = new HashMap<>();

		AtomicBoolean locked = new AtomicBoolean(false);
		try {
			acquireSemaphoresForUrlList(locked, heldSemaphores, pendingAndHeldSemaphores, theWriteOperationsDetails.getUpdateRequestUrls(), false);
			acquireSemaphoresForUrlList(locked, heldSemaphores, pendingAndHeldSemaphores, theWriteOperationsDetails.getConditionalCreateRequestUrls(), true);

			pendingAndHeldSemaphores.keySet().removeIf(k -> pendingAndHeldSemaphores.get(k) == null);
			if (!pendingAndHeldSemaphores.isEmpty()) {
				if (isLogWaits()) {
					ourLog.info("Waiting to acquire write semaphore for URLs:{}{}",
						(pendingAndHeldSemaphores.size() > 1 ? "\n * " : ""),
						(pendingAndHeldSemaphores.keySet().stream().sorted().collect(Collectors.joining("\n * "))));
				}
				for (Map.Entry<String, Semaphore> nextEntry : pendingAndHeldSemaphores.entrySet()) {
					Semaphore nextSemaphore = nextEntry.getValue();
					try {
						if (nextSemaphore.tryAcquire(10, TimeUnit.SECONDS)) {
							ourLog.trace("Acquired semaphore {} on request URL: {}", nextSemaphore, nextEntry.getKey());
							heldSemaphores.add(nextSemaphore);
						} else {
							ourLog.warn("Timed out waiting for semaphore {} on request URL: {}", nextSemaphore, nextEntry.getKey());
							break;
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			}

		theTransactionDetails.putUserData(HELD_SEMAPHORES, heldSemaphores);

		} finally {
			if (locked.get()) {
				myLockingSemaphore.release();
			}
		}
	}

	private void acquireSemaphoresForUrlList(AtomicBoolean theLocked, List<Semaphore> theHeldSemaphores, Map<String, Semaphore> thePendingAndHeldSemaphores, List<String> urls, boolean isConditionalCreates) {
		for (String nextUrl : urls) {

			if (isConditionalCreates) {
				if (myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, nextUrl) != null) {
					continue;
				}
			}

			Semaphore semaphore = mySemaphoreCache.get(nextUrl, t -> new Semaphore(1));
			if (thePendingAndHeldSemaphores.containsKey(nextUrl)) {
				continue;
			}

			if (!theLocked.get()) {
				myLockingSemaphore.acquireUninterruptibly();
				theLocked.set(true);
			}

			assert semaphore != null;
			if (semaphore.tryAcquire()) {
				ourLog.trace("Acquired semaphore {} on request URL: {}", semaphore, nextUrl);
				theHeldSemaphores.add(semaphore);
				thePendingAndHeldSemaphores.put(nextUrl, null);
				} else {
				thePendingAndHeldSemaphores.put(nextUrl, semaphore);
			}
		}
	}

	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST)
	public void post(TransactionDetails theTransactionDetails) {
		List<Semaphore> heldSemaphores = theTransactionDetails.getUserData(HELD_SEMAPHORES);
		for (Semaphore next : heldSemaphores) {
			ourLog.trace("Releasing semaphore {}", next);
			next.release();
		}
	}

	/**
	 * Clear all semaphors from the list. This is really mostly intended for testing scenarios.
	 */
	public void clearSemaphores() {
		mySemaphoreCache.invalidateAll();
	}

	/**
	 * Returns a count of all semaphores currently in the cache (incuding held and unheld semaphores)
	 */
	public long countSemaphores() {
		return mySemaphoreCache.estimatedSize();
	}


}
