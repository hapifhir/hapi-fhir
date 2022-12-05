package ca.uhn.fhir.batch2.jobs.models;

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

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Id implements IModelJson {

	@JsonProperty("type")
	private String myResourceType;
	@JsonProperty("id")
	private String myId;

	@Override
	public String toString() {
		// We put a space in here and not a "/" since this is a PID, not
		// a resource ID
		return "[" + myResourceType + " " + myId + "]";
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Id setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getId() {
		return myId;
	}

	public Id setId(String theId) {
		myId = theId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		Id id = (Id) theO;

		return new EqualsBuilder().append(myResourceType, id.myResourceType).append(myId, id.myId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myResourceType).append(myId).toHashCode();
	}

	public static Id getIdFromPID(BaseResourcePersistentId thePID, String theResourceType) {
		Id id = new Id();
		id.setId(((JpaPid)thePID).getId().toString());
		id.setResourceType(theResourceType);
		return id;
	}
}
