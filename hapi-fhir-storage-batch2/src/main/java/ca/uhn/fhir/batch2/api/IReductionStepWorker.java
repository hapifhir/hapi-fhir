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

import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;

/**
 * Reduction step worker.
 * @param <PT> Job Parameter Type
 * @param <IT> Input Parameter type (real input for step is ListResult of IT
 * @param <OT> Output Job Report Type
 */
public interface IReductionStepWorker<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
		extends IJobStepWorker<PT, IT, OT> {

	/**
	 *
	 * If an exception is thrown, the workchunk will be marked as failed.
	 * @param theChunkDetails - the workchunk details for reduction
	 * @return
	 */
	@Nonnull
	ChunkOutcome consume(ChunkExecutionDetails<PT, IT> theChunkDetails);
}
