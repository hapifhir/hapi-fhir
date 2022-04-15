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

import java.util.EnumSet;

public enum StatusEnum {

	/**
	 * Task is waiting to execute and should begin with no intervention required.
	 */
	QUEUED,

	/**
	 * Task is current executing
	 */
	IN_PROGRESS,

	/**
	 * Task completed successfully
	 */
	COMPLETED,

	/**
	 * Task execution resulted in an error but the error may be transient (or transient status is unknown).
	 * Retrying may result in success.
	 */
	ERRORED,

	/**
	 * Task has failed and is known to be unrecoverable. There is no reason to believe that retrying will
	 * result in a different outcome.
	 */
	FAILED;

	/**
	 * Statuses that represent a job that has not yet completed. I.e.
	 * all statuses except {@link #COMPLETED}
	 */
	public static final EnumSet<StatusEnum> INCOMPLETE_STATUSES = EnumSet.of(
		QUEUED,
		IN_PROGRESS,
		ERRORED,
		FAILED
	);

}
