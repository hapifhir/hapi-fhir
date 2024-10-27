/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

import java.util.Objects;

public class TypedResourcePid {
	public final String resourceType;
	public final IResourcePersistentId id;

	public TypedResourcePid(String theResourceType, IResourcePersistentId theId) {
		this.resourceType = theResourceType;
		this.id = theId;
	}

	public TypedResourcePid(String theResourceType, Long theId) {
		this.resourceType = theResourceType;
		this.id = JpaPid.fromId(theId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TypedResourcePid that = (TypedResourcePid) o;
		return resourceType.equals(that.resourceType) && id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceType, id);
	}

	@Override
	public String toString() {
		return resourceType + "[" + id + "]";
	}
}
