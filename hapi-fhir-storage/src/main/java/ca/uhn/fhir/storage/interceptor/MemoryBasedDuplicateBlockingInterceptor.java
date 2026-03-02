/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.storage.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.util.HashingWriter;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.hash.HashCode;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This interceptor keeps a running hash of the last 1000 resources created in the
 * FHIR repository and blocks attempts to create any duplicate resources matching a
 * previously created resource.
 * <p>
 * Note that this interceptor was designed as a basic check to be deployed to the
 * public HAPI FHIR test server (https://hapi.fhir.org) and achieves highly
 * efficient performance at the expense of not being 100% reliable. It only relies
 * on an in-memory cache of hashes of recently created resources. It cannot detect
 * duplicates of older resources, resource updates which result in duplicates, etc.
 * </p>
 * <p>
 * This interceptor is provided in case it is of use to others, although it is
 * probably not reliable enough for serious production use.
 * </p>
 *
 * @since 8.10.0
 */
@Interceptor
public class MemoryBasedDuplicateBlockingInterceptor {

	public static final int ENTRY_COUNT = 1000;
	private static final Logger ourLog = LoggerFactory.getLogger(MemoryBasedDuplicateBlockingInterceptor.class);
	private static final int MAX_TIMESTAMP_COUNT = 50000;

	private final Object myLock = new Object();
	private final Deque<HashCode> myResourceHashDeque = new ArrayDeque<>(ENTRY_COUNT);
	private final Deque<Instant> myBlockedTimestamps = new ArrayDeque<>(MAX_TIMESTAMP_COUNT);
	private final Map<HashCode, String> myResourceHashSet = new HashMap<>();
	private final IParser myParser;
	private final FhirContext myFhirContext;

	public MemoryBasedDuplicateBlockingInterceptor(@Nonnull FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");

		myFhirContext = theFhirContext;
		myParser = myFhirContext.newJsonParser();
		myParser.setDontEncodeElements("*.id", "*.meta", "*.text");
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void onCreate(IBaseResource theResource) {
		HashCode hashCode = hashResource(theResource);

		String existingId;
		synchronized (myLock) {
			existingId = myResourceHashSet.get(hashCode);
		}

		if (existingId == null) {
			storeResourceHashInCacheOnCommit(theResource, hashCode);
		} else {
			blockDuplicateResource(theResource, existingId);
		}
	}

	private void blockDuplicateResource(IBaseResource theResource, String existingId) {
		/*
		 * Calculate how frequently we're blocking resources
		 * for the log line
		 */
		int throughputPerHour = incrementCounterAndCalculateThroughputPerHour();

		String resourceId = getResourceId(theResource);
		ourLog.warn(
				"Blocked creating a duplicate resource to {} existing: {} ({}/hr)",
				resourceId,
				existingId,
				throughputPerHour);
		throw new PreconditionFailedException(
				Msg.code(2840) + "Can not create resource duplicating existing resource: " + existingId);
	}

	private int incrementCounterAndCalculateThroughputPerHour() {
		Instant now = Instant.now();

		synchronized (myLock) {
			while (myBlockedTimestamps.size() >= MAX_TIMESTAMP_COUNT) {
				myBlockedTimestamps.removeFirst();
			}
			Instant cutoff = now.minus(1, ChronoUnit.HOURS);
			while (!myBlockedTimestamps.isEmpty()
					&& myBlockedTimestamps.getFirst().isBefore(cutoff)) {
				myBlockedTimestamps.removeFirst();
			}

			myBlockedTimestamps.add(now);

			if (myBlockedTimestamps.size() > 1) {
				Duration allTimestampDuration = Duration.between(cutoff, now);
				TimeUnit unit = TimeUnit.HOURS;
				return (int) StopWatch.getThroughput(myBlockedTimestamps.size(), allTimestampDuration.toMillis(), unit);
			}
		}

		return 1;
	}

	private void storeResourceHashInCacheOnCommit(IBaseResource theResource, HashCode theResourceHashCode) {
		String resourceId = getResourceId(theResource);

		HapiTransactionService.executeAfterCommitOrExecuteNowIfNoTransactionIsActive(() -> {
			synchronized (myLock) {
				while (myResourceHashDeque.size() >= ENTRY_COUNT) {
					HashCode removed = myResourceHashDeque.removeFirst();
					myResourceHashSet.remove(removed);
				}
				myResourceHashSet.put(theResourceHashCode, resourceId);
				myResourceHashDeque.add(theResourceHashCode);
			}
		});
	}

	private String getResourceId(IBaseResource theResource) {
		IIdType resourceIdType = theResource.getIdElement().toUnqualifiedVersionless();
		if (!resourceIdType.hasResourceType()) {
			String resourceType = myFhirContext.getResourceType(theResource);
			resourceIdType = resourceIdType.withResourceType(resourceType);
		}
		return resourceIdType.getValue();
	}

	@Nonnull
	private HashCode hashResource(IBaseResource theResource) {
		HashCode hashCode;
		try (HashingWriter hashingWriter = new HashingWriter()) {
			hashingWriter.append(myParser, theResource);
			hashCode = hashingWriter.getHash();
		}
		return hashCode;
	}
}
