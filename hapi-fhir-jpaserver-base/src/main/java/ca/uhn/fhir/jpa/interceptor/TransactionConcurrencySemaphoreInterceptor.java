package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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

	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_PRE)
	public void pre(TransactionDetails theTransactionDetails, TransactionWriteOperationsDetails theWriteOperationsDetails) {
		List<Semaphore> heldSemaphores = new ArrayList<>();

		acquireSemaphoresForUrlList(heldSemaphores, theWriteOperationsDetails.getUpdateRequestUrls(), false);
		acquireSemaphoresForUrlList(heldSemaphores, theWriteOperationsDetails.getConditionalCreateRequestUrls(), true);

		theTransactionDetails.putUserData(HELD_SEMAPHORES, heldSemaphores);
	}

	private void acquireSemaphoresForUrlList(List<Semaphore> heldSemaphores, List<String> urls, boolean isConditionalCreates) {
		for (String next : urls) {

			if (isConditionalCreates) {
				if (myMemoryCacheService.getIfPresent(MemoryCacheService.CacheEnum.MATCH_URL, next) != null) {
					continue;
				}
			}

			Semaphore semaphore = mySemaphoreCache.get(next, t -> new Semaphore(1));
			if (heldSemaphores.contains(semaphore)) {
				continue;
			}

			assert semaphore != null;
			try {
				if (!semaphore.tryAcquire(10, TimeUnit.SECONDS)) {
					ourLog.warn("Timed out waiting for semaphore on request URL: {}", next);
				} else {
					heldSemaphores.add(semaphore);
				}
			} catch (InterruptedException e) {
				ourLog.warn("Interrupted during semaphore acquisition");
			}
		}
	}

	@Hook(Pointcut.STORAGE_TRANSACTION_WRITE_OPERATIONS_POST)
	public void post(TransactionDetails theTransactionDetails) {
		List<Semaphore> heldSemaphores = theTransactionDetails.getUserData(HELD_SEMAPHORES);
		for (Semaphore next : heldSemaphores) {
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
