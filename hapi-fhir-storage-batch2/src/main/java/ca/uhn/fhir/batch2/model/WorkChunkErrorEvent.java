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
package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Payload for the work-chunk error event including the error message, and the allowed retry count.
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public class WorkChunkErrorEvent extends BaseWorkChunkEvent {

	private String myErrorMsg;

	public WorkChunkErrorEvent(String theChunkId) {
		super(theChunkId);
	}

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

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		WorkChunkErrorEvent that = (WorkChunkErrorEvent) theO;

		return new EqualsBuilder()
				.appendSuper(super.equals(theO))
				.append(myChunkId, that.myChunkId)
				.append(myErrorMsg, that.myErrorMsg)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.appendSuper(super.hashCode())
				.append(myChunkId)
				.append(myErrorMsg)
				.toHashCode();
	}
}
