package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum StatusEnum {

	/**
	 * Task is waiting to execute and should begin with no intervention required.
	 */
	QUEUED(true, false),

	/**
	 * Task is current executing
	 */
	IN_PROGRESS(true, false),

	/**
	 * Task completed successfully
	 */
	COMPLETED(false, true),

	/**
	 * Task execution resulted in an error but the error may be transient (or transient status is unknown).
	 * Retrying may result in success.
	 */
	ERRORED(true, true),

	/**
	 * Task has failed and is known to be unrecoverable. There is no reason to believe that retrying will
	 * result in a different outcome.
	 */
	FAILED(true, true),

	/**
	 * Task has been cancelled.
	 */
	CANCELLED(true, true);

	private final boolean myIncomplete;
	private final boolean myEnded;
	private static Set<StatusEnum> ourIncompleteStatuses;
	private static Set<StatusEnum> ourEndedStatuses;
	private static Set<StatusEnum> ourNotEndedStatuses;

	StatusEnum(boolean theIncomplete, boolean theEnded) {
		myIncomplete = theIncomplete;
		myEnded = theEnded;
	}

	/**
	 * Statuses that represent a job that has not yet completed. I.e.
	 * all statuses except {@link #COMPLETED}
	 */
	public static Set<StatusEnum> getIncompleteStatuses() {
		Set<StatusEnum> retVal = ourIncompleteStatuses;
		if (retVal == null) {
			EnumSet<StatusEnum> incompleteSet = EnumSet.noneOf(StatusEnum.class);
			for (StatusEnum next : values()) {
				if (next.myIncomplete) {
					incompleteSet.add(next);
				}
			}
			ourIncompleteStatuses = Collections.unmodifiableSet(incompleteSet);
			retVal = ourIncompleteStatuses;
		}
		return retVal;
	}

	/**
	 * Statuses that represent a job that has ended. I.e.
	 * all statuses except {@link #QUEUED and #COMPLETED}
	 */
	@Nonnull
	public static Set<StatusEnum> getEndedStatuses() {
		Set<StatusEnum> retVal = ourEndedStatuses;
		if (retVal == null) {
			initializeStaticEndedStatuses();
		}
		retVal = ourEndedStatuses;
		return retVal;
	}

	/**
	 * Statuses that represent a job that has not ended. I.e.
	 * {@link #QUEUED and #COMPLETED}
	 */
	@Nonnull
	public static Set<StatusEnum> getNotEndedStatuses() {
		Set<StatusEnum> retVal = ourNotEndedStatuses;
		if (retVal == null) {
			initializeStaticEndedStatuses();
		}
		retVal = ourNotEndedStatuses;
		return retVal;
	}

	@Nonnull
	private static void initializeStaticEndedStatuses() {
		EnumSet<StatusEnum> endedSet = EnumSet.noneOf(StatusEnum.class);
		EnumSet<StatusEnum> notEndedSet = EnumSet.noneOf(StatusEnum.class);
		for (StatusEnum next : values()) {
			if (next.myEnded) {
				endedSet.add(next);
			} else {
				notEndedSet.add(next);
			}
		}
		ourEndedStatuses = Collections.unmodifiableSet(endedSet);
		ourNotEndedStatuses = Collections.unmodifiableSet(notEndedSet);
	}

}
