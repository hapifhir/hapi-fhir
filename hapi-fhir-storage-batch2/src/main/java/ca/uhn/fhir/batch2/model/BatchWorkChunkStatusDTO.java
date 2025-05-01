/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.model;

import java.util.Date;

public class BatchWorkChunkStatusDTO {
	public final String stepId;
	public final WorkChunkStatusEnum status;
	public final Date start;
	public final Date stop;

	public final Double avg;
	public final Long totalChunks;

	public BatchWorkChunkStatusDTO(
			String theStepId,
			WorkChunkStatusEnum theStatus,
			Date theStart,
			Date theStop,
			Double theAvg,
			Long theTotalChunks) {
		stepId = theStepId;
		status = theStatus;
		start = theStart;
		stop = theStop;
		avg = theAvg;
		totalChunks = theTotalChunks;
	}
}
