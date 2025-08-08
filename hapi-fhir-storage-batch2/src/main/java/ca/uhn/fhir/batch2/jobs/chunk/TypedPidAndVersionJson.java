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
package ca.uhn.fhir.batch2.jobs.chunk;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.Objects;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class TypedPidAndVersionJson extends TypedPidJson {

	@JsonProperty("v")
	private Long myVersionId;

	public TypedPidAndVersionJson() {
	}

	public TypedPidAndVersionJson(String theResourceType, Integer thePartitionId, String theId, Long theVersionId) {
		super(theResourceType, thePartitionId, theId);
		myVersionId = theVersionId;
	}

	@Override
	public String toString() {
		// We put a space in here and not a "/" since this is a PID, not
		// a resource ID
		return "[" + getResourceType() + " " + getPid() + " " + myVersionId + "]";
	}


	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof TypedPidAndVersionJson)) return false;
		TypedPidAndVersionJson that = (TypedPidAndVersionJson) theO;
		return super.equals(theO) && Objects.equals(myVersionId, that.myVersionId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), myVersionId);
	}

	public Long getVersionId() {
		return myVersionId;
	}

	public void setVersionId(Long theVersionId) {
		myVersionId = theVersionId;
	}

	/**
	 * Returns an estimate of how long the JSON serialized (non-pretty printed) form
	 * of this object will be.
	 */
	public int estimateSerializedSize() {
		// Adds 7 chars: ,"v":""
		int retVal = super.estimateSerializedSize();
		if (myVersionId != null) {
			retVal += 7 + Long.toString(myVersionId).length();
		}
		return retVal;
	}

	public TypedPidJson toTypedPid() {
		return new TypedPidJson(getResourceType(), getPartitionId(), getPid());
	}
}
