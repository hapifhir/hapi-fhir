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
package ca.uhn.fhir.batch2.util;

import org.hl7.fhir.r4.model.InstantType;

import java.util.Date;

public class Batch2Utils {
	private Batch2Utils() {}

	/**
	 * The batch 2 system assumes that all records have a start date later than this date.  This date is used as a starting
	 * date when performing operations that pull resources by time windows.
	 */
	public static final Date BATCH_START_DATE = new InstantType("2000-01-01T00:00:00Z").getValue();

	/**
	 * This is a placeholder chunkid for the reduction step to allow it to be
	 * used in the message handling
	 */
	public static final String REDUCTION_STEP_CHUNK_ID_PLACEHOLDER = "REDUCTION";
}
