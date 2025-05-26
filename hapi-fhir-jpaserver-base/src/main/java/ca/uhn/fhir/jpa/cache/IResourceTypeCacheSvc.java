/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.cache;

public interface IResourceTypeCacheSvc {
	/**
	 * Retrieves the resource type ID for the given resource type.
	 * If the resource type does not exist, a new custom resource type will be created,
	 * added to the database and cache, and its ID will be returned.
	 *
	 * @param theResType the resource type to retrieve or create
	 * @return the resource type ID or throws an exception if the resource creation fails
	 */
	short getResourceTypeId(String theResType);

	/**
	 * Adds the given resource type and its corresponding ID to the cache.
	 *
	 * @param theResType the resource type to be added to the cache
	 * @param theResTypeId the ID of the resource type to be added to the cache
	 */
	void addToCache(String theResType, Short theResTypeId);
}
