/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.api;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Exception that is thrown when a polling step needs to be retried at a later
 * time.
 */
public class RetryChunkLaterException extends RuntimeException {

	private static final Duration ONE_MINUTE = Duration.of(1, ChronoUnit.MINUTES);

	/**
	 * The delay to wait (in ms) for the next poll call.
	 * For now, it's a constant, but we hold it here in
	 * case we want to change this behaviour in the future.
	 */
	private final Duration myNextPollDuration;

	public RetryChunkLaterException() {
		this(ONE_MINUTE);
	}

	public RetryChunkLaterException(Duration theDuration) {
		super();
		this.myNextPollDuration = theDuration;
	}

	public Duration getNextPollDuration() {
		return myNextPollDuration;
	}
}
