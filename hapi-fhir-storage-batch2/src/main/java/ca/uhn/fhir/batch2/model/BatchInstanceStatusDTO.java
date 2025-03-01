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

public class BatchInstanceStatusDTO {
	public final String id;
	public final StatusEnum status;
	public final Date start;
	public final Date stop;

	public BatchInstanceStatusDTO(String theId, StatusEnum theStatus, Date theStart, Date theStop) {
		id = theId;
		status = theStatus;
		start = theStart;
		stop = theStop;
	}
}
