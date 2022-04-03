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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This class is an abstraction for however primary keys are stored in the underlying storage engine. This might be
 * a Long, a String, or something else.
 */
public class ResourcePersistentId {
	private Object myId;
	private Long myVersion;
	private IIdType myAssociatedResourceId;

	public ResourcePersistentId(Object theId) {
		this(theId, null);
	}

	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	public ResourcePersistentId(Object theId, Long theVersion) {
		assert !(theId instanceof Optional);
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

	public Object getId() {
		return myId;
	}

	public void setId(Object theId) {
		myId = theId;
	}

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

	public static List<Long> toLongList(Collection<ResourcePersistentId> thePids) {
		List<Long> retVal = new ArrayList<>(thePids.size());
		for (ResourcePersistentId next : thePids) {
			retVal.add(next.getIdAsLong());
		}
		return retVal;
	}

	public static List<ResourcePersistentId> fromLongList(List<Long> theResultList) {
		List<ResourcePersistentId> retVal = new ArrayList<>(theResultList.size());
		for (Long next : theResultList) {
			retVal.add(new ResourcePersistentId(next));
		}
		return retVal;
	}
}
