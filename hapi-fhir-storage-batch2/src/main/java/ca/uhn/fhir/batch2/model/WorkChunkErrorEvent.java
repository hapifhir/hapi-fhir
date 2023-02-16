package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT;

public class WorkChunkErrorEvent extends WorkChunkEventBase {

	private String myErrorMsg;
	private boolean myIncludeData;

	private int maxRetries = MAX_CHUNK_ERROR_COUNT;

	public WorkChunkErrorEvent(String theChunkId, String theErrorMessage) {
		super(theChunkId);
		myErrorMsg = theErrorMessage;
	}

	public String getErrorMsg() {
		return myErrorMsg;
	}

	public WorkChunkErrorEvent setErrorMsg(String theErrorMsg) {
		myErrorMsg = theErrorMsg;
		return this;
	}

	public boolean isIncludeData() {
		return myIncludeData;
	}

	public WorkChunkErrorEvent setIncludeData(boolean theIncludeData) {
		myIncludeData = theIncludeData;
		return this;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int theMaxRetries) {
		maxRetries = theMaxRetries;
	}

}
