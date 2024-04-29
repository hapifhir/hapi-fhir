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
package ca.uhn.fhir.batch2.jobs.chunk;

import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class TypedPidJson implements IModelJson {

	@JsonProperty("type")
	private String myResourceType;

	@JsonProperty("id")
	private String myPid;

	public TypedPidJson() {}

	public TypedPidJson(String theResourceType, String theId) {
		myResourceType = theResourceType;
		myPid = theId;
	}

	public TypedPidJson(TypedResourcePid theTypedResourcePid) {
		myResourceType = theTypedResourcePid.resourceType;
		myPid = theTypedResourcePid.id.toString();
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

		if (theO == null || getClass() != theO.getClass()) return false;

		TypedPidJson id = (TypedPidJson) theO;

		return new EqualsBuilder()
				.append(myResourceType, id.myResourceType)
				.append(myPid, id.myPid)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myResourceType).append(myPid).toHashCode();
	}
}
