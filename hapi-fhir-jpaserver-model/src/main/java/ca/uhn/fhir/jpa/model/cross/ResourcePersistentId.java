package ca.uhn.fhir.jpa.model.cross;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

	public ResourcePersistentId(Object theId) {
		assert !(theId instanceof Optional);
		myId = theId;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof ResourcePersistentId)) {
			return false;
		}
		ResourcePersistentId that = (ResourcePersistentId) theO;

		return ObjectUtil.equals(myId, that.myId);
	}

	@Override
	public int hashCode() {
		return myId.hashCode();
	}

	public Object getId() {
		return myId;
	}

	public void setId(Object theId) {
		myId = theId;
	}

	public Long getIdAsLong() {
		return (Long) myId;
	}

	@Override
	public String toString() {
		return myId.toString();
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
