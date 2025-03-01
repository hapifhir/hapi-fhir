/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import com.google.common.annotations.VisibleForTesting;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class ReindexUtils {

	/**
	 * The reindex job definition id
	 */
	public static final String JOB_REINDEX = "REINDEX";

	public static final int REINDEX_MAX_RETRIES = 10;

	private static final Duration RETRY_DELAY = Duration.of(30, ChronoUnit.SECONDS);

	private static Duration myDelay;

	/**
	 * Returns the retry delay for reindex jobs that require polling.
	 */
	public static Duration getRetryLaterDelay() {
		if (myDelay != null) {
			return myDelay;
		}
		return RETRY_DELAY;
	}

	/**
	 * Sets the retry delay to use for reindex jobs.
	 * Do not use this in production code! Only test code.
	 */
	@VisibleForTesting
	public static void setRetryDelay(Duration theDuration) {
		myDelay = theDuration;
	}
}
