/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.jpa.dao.IdSubstitutionMap.toVersionlessValue;

public class EntriesToProcessMap {

	private final IdentityHashMap<IBase, IIdType> myEntriesToProcess = new IdentityHashMap<>();
	private final Map<String, IIdType> myVersionlessIdToVersionedId = new HashMap<>();
	private final Map<String, IBase> myVersionlessIdToResponseBundleEntry = new HashMap<>();

	public void put(IBase theBundleEntry, IIdType theId, IBase theResponseBundleEntry) {
		myEntriesToProcess.put(theBundleEntry, theId);
		String key = toVersionlessValue(theId);
		myVersionlessIdToVersionedId.put(key, theId);
		myVersionlessIdToResponseBundleEntry.put(key, theResponseBundleEntry);
	}

	public IIdType getIdWithVersionlessComparison(IIdType theId) {
		return myVersionlessIdToVersionedId.get(toVersionlessValue(theId));
	}

	public IBase getResponseBundleEntryWithVersionlessComparison(IIdType theId) {
		return myVersionlessIdToResponseBundleEntry.get(toVersionlessValue(theId));
	}

	public Set<Map.Entry<IBase, IIdType>> entrySet() {
		return myEntriesToProcess.entrySet();
	}
}
