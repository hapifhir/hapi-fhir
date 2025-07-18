/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.svcs;

import jakarta.annotation.Nonnull;

import java.util.Collection;

public interface ISearchLimiterSvc {

	/**
	 * Add a resource type to omit from all search results for the named operation.
	 * @param theOperationName operation name (eg: $everything, $export, etc)
	 * @param theResourceType the resource name to omit (eg: Group, List, etc)
	 */
	void addOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType);

	/**
	 * Get all omitted resources for the named operation.
	 */
	Collection<String> getResourcesToOmitForOperationSearches(@Nonnull String theOperationName);

	/**
	 * Remove the resource type from the omission criteria.
	 * @param theOperationName the operation name
	 * @param theResourceType the resource type to remove
	 */
	void removeOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType);

	/**
	 * Remove all omitted resource types for the operation.
	 * @param theOperationName the operation name
	 */
	void removeAllResourcesForOperation(String theOperationName);
}
