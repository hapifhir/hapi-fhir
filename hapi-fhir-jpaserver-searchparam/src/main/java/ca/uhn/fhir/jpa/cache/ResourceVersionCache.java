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

import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This maintains a mapping of resource id to resource version.  We cache these in order to
 * detect resources that were modified on remote servers in our cluster.
 */
public class ResourceVersionCache {
	private final Map<IIdType, Long> myVersionMap = new HashMap<>();

	public void clear() {
		myVersionMap.clear();
	}

	/**
	 * @param theResourceId
	 * @param theVersion
	 * @return previous value
	 */
	public Long put(IIdType theResourceId, Long theVersion) {
		return myVersionMap.put(new IdDt(theResourceId).toVersionless(), theVersion);
	}

	public Long getVersionForResourceId(IIdType theResourceId) {
		return myVersionMap.get(new IdDt(theResourceId));
	}

	public Long removeResourceId(IIdType theResourceId) {
		return myVersionMap.remove(new IdDt(theResourceId));
	}

	public void initialize(ResourceVersionMap theResourceVersionMap) {
		for (IIdType resourceId : theResourceVersionMap.keySet()) {
			myVersionMap.put(resourceId, theResourceVersionMap.get(resourceId));
		}
	}

	public int size() {
		return myVersionMap.size();
	}

	public Set<IIdType> keySet() {
		return myVersionMap.keySet();
	}
}
