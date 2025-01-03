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

import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.Objects;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class TypedPidJson implements IModelJson, Comparable<TypedPidJson> {

	@JsonProperty("type")
	private String myResourceType;

	@JsonProperty("part")
	private Integer myPartitionId;

	@JsonProperty("id")
	private String myPid;

	public TypedPidJson() {}

	public TypedPidJson(String theResourceType, Integer thePartitionId, String theId) {
		myResourceType = theResourceType;
		myPartitionId = thePartitionId;
		myPid = theId;
	}

	public TypedPidJson(TypedResourcePid theTypedResourcePid) {
		this(theTypedResourcePid.resourceType, theTypedResourcePid.id);
	}

	public TypedPidJson(String theResourceType, IResourcePersistentId<?> theResourceId) {
		myResourceType = theResourceType;
		myPid = theResourceId.getId().toString();
		myPartitionId = theResourceId.getPartitionId();
	}

	@Override
	public String toString() {
		// We put a space in here and not a "/" since this is a PID, not
		// a resource ID
		return "[" + myResourceType + " " + myPid + "]";
	}

	public String getResourceType() {
		return myResourceType;
	}

	public TypedPidJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getPid() {
		return myPid;
	}

	public TypedPidJson setPid(String thePid) {
		myPid = thePid;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (!(theO instanceof TypedPidJson)) return false;
		TypedPidJson that = (TypedPidJson) theO;
		return Objects.equals(myResourceType, that.myResourceType)
				&& Objects.equals(myPartitionId, that.myPartitionId)
				&& Objects.equals(myPid, that.myPid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceType, myPartitionId, myPid);
	}

	public Integer getPartitionId() {
		return myPartitionId;
	}

	/**
	 * Returns an estimate of how long the JSON serialized (non-pretty printed) form
	 * of this object will be.
	 */
	public int estimateSerializedSize() {
		// 29 chars: {"id":"","type":"","part":""}
		return 19
				+ defaultString(myPid).length()
				+ defaultString(myResourceType).length()
				+ defaultIfNull(myPartitionId, "").toString().length();
	}

	@Override
	public int compareTo(@Nonnull TypedPidJson o) {
		int retVal = defaultIfNull(o.myPartitionId, Integer.MIN_VALUE)
				.compareTo(defaultIfNull(myPartitionId, Integer.MIN_VALUE));
		if (retVal == 0) {
			retVal = o.myResourceType.compareTo(myResourceType);
		}
		if (retVal == 0) {
			retVal = o.myPid.compareTo(myPid);
		}
		return retVal;
	}
}
