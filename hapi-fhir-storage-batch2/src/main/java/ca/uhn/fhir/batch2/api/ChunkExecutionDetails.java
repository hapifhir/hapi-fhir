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

import ca.uhn.fhir.model.api.IModelJson;

public class ChunkExecutionDetails<PT extends IModelJson, IT extends IModelJson> {
	private final IT myData;

	private final PT myParameters;

	private final String myInstanceId;

	private final String myChunkId;

	public ChunkExecutionDetails(IT theData, PT theParameters, String theInstanceId, String theChunkId) {
		myData = theData;
		myParameters = theParameters;
		myInstanceId = theInstanceId;
		myChunkId = theChunkId;
	}

	public IT getData() {
		return myData;
	}

	public PT getParameters() {
		return myParameters;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public String getChunkId() {
		return myChunkId;
	}
}
