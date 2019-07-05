package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.util.ArrayDeque;
import java.util.Queue;

public class CurrentThreadCaptureQueriesListener extends BaseCaptureQueriesListener {

	private static final ThreadLocal<Queue<SqlQuery>> ourQueues = new ThreadLocal<>();

	@Override
	protected Queue<SqlQuery> provideQueryList() {
		return ourQueues.get();
	}


	/**
	 * Get the current queue of items and stop collecting
	 */
	public static SqlQueryList getCurrentQueueAndStopCapturing() {
		Queue<SqlQuery> retVal = ourQueues.get();
		ourQueues.remove();
		if (retVal == null) {
			return new SqlQueryList();
		}
		return new SqlQueryList(retVal);
	}

	/**
	 * Starts capturing queries for the current thread.
	 * <p>
	 * Note that you should strongly consider calling this in a
	 * try-finally block to ensure that you also call
	 * {@link #getCurrentQueueAndStopCapturing()} afterward. Otherwise
	 * this method is a potential memory leak!
	 * </p>
	 */
	public static void startCapturing() {
		ourQueues.set(new ArrayDeque<>());
	}

}
