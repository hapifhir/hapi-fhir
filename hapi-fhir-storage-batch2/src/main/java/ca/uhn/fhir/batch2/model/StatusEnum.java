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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum StatusEnum {

	/**
	 * Task is waiting to execute and should begin with no intervention required.
	 */
	QUEUED(true),

	/**
	 * Task is current executing
	 */
	IN_PROGRESS(true),

	/**
	 * Task completed successfully
	 */
	COMPLETED(false),

	/**
	 * Task execution resulted in an error but the error may be transient (or transient status is unknown).
	 * Retrying may result in success.
	 */
	ERRORED(true),

	/**
	 * Task has failed and is known to be unrecoverable. There is no reason to believe that retrying will
	 * result in a different outcome.
	 */
	FAILED(true),

	/**
	 * Task has been cancelled.
	 */
	CANCELLED(true);

	private final boolean myIncomplete;
	private static Set<StatusEnum> ourIncompleteStatuses;

	StatusEnum(boolean theIncomplete) {
		myIncomplete = theIncomplete;
	}

	/**
	 * Statuses that represent a job that has not yet completed. I.e.
	 * all statuses except {@link #COMPLETED}
	 */
	public static Set<StatusEnum> getIncompleteStatuses() {
		Set<StatusEnum> retVal = ourIncompleteStatuses;
		if (retVal == null) {
			EnumSet<StatusEnum> set = EnumSet.noneOf(StatusEnum.class);
			for (StatusEnum next : values()) {
				if (next.myIncomplete) {
					set.add(next);
				}
			}
			ourIncompleteStatuses = Collections.unmodifiableSet(set);
			retVal = ourIncompleteStatuses;
		}
		return retVal;
	}

}
