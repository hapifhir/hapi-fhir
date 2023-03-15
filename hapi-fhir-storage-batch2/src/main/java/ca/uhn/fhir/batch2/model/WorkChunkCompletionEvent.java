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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Payload for the work-chunk completion event with the record and error counts.
 */
public class WorkChunkCompletionEvent extends BaseWorkChunkEvent {
	int myRecordsProcessed;
	int myRecoveredErrorCount;

	public WorkChunkCompletionEvent(String theChunkId, int theRecordsProcessed, int theRecoveredErrorCount) {
		super(theChunkId);
		myRecordsProcessed = theRecordsProcessed;
		myRecoveredErrorCount = theRecoveredErrorCount;
	}

	public int getRecordsProcessed() {
		return myRecordsProcessed;
	}

	public int getRecoveredErrorCount() {
		return myRecoveredErrorCount;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		WorkChunkCompletionEvent that = (WorkChunkCompletionEvent) theO;

		return new EqualsBuilder().appendSuper(super.equals(theO)).append(myRecordsProcessed, that.myRecordsProcessed).append(myRecoveredErrorCount, that.myRecoveredErrorCount).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(myRecordsProcessed).append(myRecoveredErrorCount).toHashCode();
	}
}
