/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobWorkCursor;
import jakarta.annotation.Nullable;

public interface IReductionStepExecutorService {
	/**
	 * Triggers (or enqueues) a reduction step execution for the given job instance.
	 *
	 * @param theInstanceId    the job instance whose reduction step should run
	 * @param theJobWorkCursor the cursor pointing at the reduction step
	 * @param theDriverChunkId the id of the 'driver' work chunk that triggered this reduction, or
	 *                         {@code null} when the reduction is run inline without a driver chunk. When
	 *                         present, its heartbeat is refreshed while the reduction runs so that a
	 *                         long-running reduction is not mistaken for a dead worker on redelivery.
	 */
	void triggerReductionStep(
			String theInstanceId, JobWorkCursor<?, ?, ?> theJobWorkCursor, @Nullable String theDriverChunkId);

	void reducerPass();
}
