package ca.uhn.fhir.rest.api.server.storage;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.util.ObjectUtil;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Optional;

/**
 * This class is an abstraction for however primary keys are stored in the underlying storage engine. This might be
 * a Long, a String, or something else.
 *
 * @param myId This is the only required field that needs to be populated, other fields can be populated for specific use cases.
 */
public class ResourcePersistentId<T> {
	private T myId;
	private Long myVersion;
	private String myResourceType;
	private IIdType myAssociatedResourceId;

	/**
	 * @deprecated use subclass
	 */
	public ResourcePersistentId(T theId) {
		this(theId, null);
	}

	/**
	 * @deprecated use subclass
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	public ResourcePersistentId(T theId, Long theVersion) {
		assert !(theId instanceof Optional);
		assert !(theId instanceof ResourcePersistentId);
		myId = theId;
		myVersion = theVersion;
	}

	public IIdType getAssociatedResourceId() {
		return myAssociatedResourceId;
	}

	public ResourcePersistentId setAssociatedResourceId(IIdType theAssociatedResourceId) {
		myAssociatedResourceId = theAssociatedResourceId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof ResourcePersistentId)) {
			return false;
		}
		ResourcePersistentId that = (ResourcePersistentId) theO;

		boolean retVal = ObjectUtil.equals(myId, that.myId);
		retVal &= ObjectUtil.equals(myVersion, that.myVersion);
		return retVal;
	}

	@Override
	public int hashCode() {
		int retVal = myId.hashCode();
		if (myVersion != null) {
			retVal += myVersion.hashCode();
		}
		return retVal;
	}

	/**
	 * @deprecated use getId() method of subclass
	 */
	public T getId() {
		return myId;
	}

	/**
	 * @deprecated use getId() method of subclass
	 */
	public Long getIdAsLong() {
		if (myId instanceof String) {
			return Long.parseLong((String) myId);
		}
		return (Long) myId;
	}

	@Override
	public String toString() {
		return myId.toString();
	}

	public Long getVersion() {
		return myVersion;
	}

	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
