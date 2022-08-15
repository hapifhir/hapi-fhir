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

import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This immutable map holds a copy of current resource versions read from the repository.
 */
public class ResourceVersionMap {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceVersionMap.class);
	private final Set<IIdType> mySourceIds = new HashSet<>();
	// Key versionless id, value version
	private final Map<IIdType, Long> myMap = new HashMap<>();
	private ResourceVersionMap() {}

	public static ResourceVersionMap fromResourceTableEntities(List<ResourceTable> theEntities) {
		ResourceVersionMap retval = new ResourceVersionMap();
		theEntities.forEach(entity -> retval.add(entity.getIdDt()));
		return retval;
	}

	public static ResourceVersionMap fromResources(List<? extends IBaseResource> theResources) {
		ResourceVersionMap retval = new ResourceVersionMap();
		theResources.forEach(resource -> retval.add(resource.getIdElement()));
		return retval;
	}

	public static ResourceVersionMap empty() {
		return new ResourceVersionMap();
	}

	private void add(IIdType theId) {
		if (theId.getVersionIdPart() == null) {
			ourLog.warn("Not storing {} in ResourceVersionMap because it does not have a version.", theId);
			return;
		}
		IdDt id = new IdDt(theId);
		mySourceIds.add(id);
		myMap.put(id.toUnqualifiedVersionless(), id.getVersionIdPartAsLong());
	}

	public Long getVersion(IIdType theResourceId) {
		return get(theResourceId);
	}

	public int size() {
		return myMap.size();
	}

	public Set<IIdType> keySet() {
		return Collections.unmodifiableSet(myMap.keySet());
	}

	public Set<IIdType> getSourceIds() {
		return Collections.unmodifiableSet(mySourceIds);
	}

	public Long get(IIdType theId) {
		return myMap.get(new IdDt(theId.toUnqualifiedVersionless()));
	}

	public boolean containsKey(IIdType theId) {
		return myMap.containsKey(new IdDt(theId.toUnqualifiedVersionless()));
	}

	public boolean isEmpty() {
		return myMap.isEmpty();
	}
}
