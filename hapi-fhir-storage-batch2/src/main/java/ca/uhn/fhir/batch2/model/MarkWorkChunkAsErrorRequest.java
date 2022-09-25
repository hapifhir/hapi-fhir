package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public class MarkWorkChunkAsErrorRequest {
	private String myChunkId;

	private String myErrorMsg;

	private boolean myIncludeData;

	public String getChunkId() {
		return myChunkId;
	}

	public MarkWorkChunkAsErrorRequest setChunkId(String theChunkId) {
		myChunkId = theChunkId;
		return this;
	}

	public String getErrorMsg() {
		return myErrorMsg;
	}

	public MarkWorkChunkAsErrorRequest setErrorMsg(String theErrorMsg) {
		myErrorMsg = theErrorMsg;
		return this;
	}

	public boolean isIncludeData() {
		return myIncludeData;
	}

	public MarkWorkChunkAsErrorRequest setIncludeData(boolean theIncludeData) {
		myIncludeData = theIncludeData;
		return this;
	}
}
