package ca.uhn.fhir.batch2.jobs.chunk;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceIdListWorkChunkJson implements IModelJson {

	@JsonProperty("ids")
	private List<TypedPidJson> myTypedPids;

	// FIXME KHS don't use this method.  Take an approach similar to the idchunks
	public List<TypedPidJson> getTypedPids() {
		if (myTypedPids == null) {
			myTypedPids = new ArrayList<>();
		}
		return myTypedPids;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("ids", myTypedPids)
			.toString();
	}

	public List<ResourcePersistentId> getResourcePersistentIds() {
		if (myTypedPids.isEmpty()) {
			return Collections.emptyList();
		}

		return myTypedPids
			.stream()
			.map(t -> new ResourcePersistentId(t.getPid()))
			.collect(Collectors.toList());
	}

	public int size() {
		return myTypedPids.size();
	}

	public void addTypedPid(String theResourceType, Long thePid) {
		getTypedPids().add(new TypedPidJson(theResourceType, thePid.toString()));
	}

	public static class TypedPidJson implements IModelJson {

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

			return new EqualsBuilder().append(myResourceType, id.myResourceType).append(myPid, id.myPid).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(myResourceType).append(myPid).toHashCode();
		}
	}

}
