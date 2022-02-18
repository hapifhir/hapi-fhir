package ca.uhn.fhir.jpa.cache;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcePersistentIdMap {
	private final Map<IIdType, ResourcePersistentId> myMap = new HashMap<>();

	public static ResourcePersistentIdMap fromResourcePersistentIds(List<ResourcePersistentId> theResourcePersistentIds) {
		ResourcePersistentIdMap retval = new ResourcePersistentIdMap();
		theResourcePersistentIds.forEach(retval::add);
		return retval;
	}

	private void add(ResourcePersistentId theResourcePersistentId) {
		IIdType id = theResourcePersistentId.getAssociatedResourceId();
		myMap.put(id.toUnqualifiedVersionless(), theResourcePersistentId);
	}

	public boolean containsKey(IIdType theId) {
		return myMap.containsKey(theId.toUnqualifiedVersionless());
	}

	public ResourcePersistentId getResourcePersistentId(IIdType theId) {
		return myMap.get(theId.toUnqualifiedVersionless());
	}

	public boolean isEmpty() {
		return myMap.isEmpty();
	}

	public int size() {
		return myMap.size();
	}

	public void put(IIdType theId, ResourcePersistentId thePid) {
		myMap.put(theId, thePid);
	}

	public void putAll(ResourcePersistentIdMap theIdAndPID) {
		myMap.putAll(theIdAndPID.myMap);
	}
}
