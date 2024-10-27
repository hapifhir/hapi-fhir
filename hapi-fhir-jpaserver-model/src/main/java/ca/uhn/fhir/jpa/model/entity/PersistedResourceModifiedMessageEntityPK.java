package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
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

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersistedResourceModifiedMessageEntityPK implements IPersistedResourceModifiedMessagePK, Serializable {

	@Column(name = "RES_ID", length = 256, nullable = false)
	private String myResourcePid;

	@Column(name = "RES_VER", length = 8, nullable = false)
	private String myResourceVersion;

	public String getResourcePid() {
		return myResourcePid;
	}

	public PersistedResourceModifiedMessageEntityPK setResourcePid(String theResourcePid) {
		myResourcePid = theResourcePid;
		return this;
	}

	public String getResourceVersion() {
		return myResourceVersion;
	}

	public PersistedResourceModifiedMessageEntityPK setResourceVersion(String theResourceVersion) {
		myResourceVersion = theResourceVersion;
		return this;
	}

	public static PersistedResourceModifiedMessageEntityPK with(String theResourcePid, String theResourceVersion) {
		return new PersistedResourceModifiedMessageEntityPK()
				.setResourcePid(theResourcePid)
				.setResourceVersion(theResourceVersion);
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		PersistedResourceModifiedMessageEntityPK that = (PersistedResourceModifiedMessageEntityPK) theO;
		return myResourcePid.equals(that.myResourcePid) && myResourceVersion.equals(that.myResourceVersion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourcePid, myResourceVersion);
	}

	@Override
	public String toString() {
		return myResourcePid + "/" + myResourceVersion;
	}
}
