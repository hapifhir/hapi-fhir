/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.svcs.ISearchLimiterSvc;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SearchLimiterSvc implements ISearchLimiterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(ISearchLimiterSvc.class);

	private final Multimap<String, String> myOperationToOmittedResourceTypes = HashMultimap.create();

	private final Map<String, Set<String>> myOperationToOmittedResourceTypeCacheMap = new HashMap<>();

	public void addOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType) {
		if (ourLog.isDebugEnabled()) {
			ourLog.debug(
					"Filtering operation {} to prevent including resources of type {}",
					theOperationName,
					theResourceType);
		}
		myOperationToOmittedResourceTypes.put(theOperationName, theResourceType);
	}

	@Override
	public Collection<String> getResourcesToOmitForOperationSearches(@Nonnull String theOperationName) {
		return myOperationToOmittedResourceTypes.get(theOperationName);
	}

	@Override
	public void removeOmittedResourceType(@Nonnull String theOperationName, @Nonnull String theResourceType) {
		ourLog.debug("Removing any filtering of {} for {} operation", theResourceType, theOperationName);
		myOperationToOmittedResourceTypes.remove(theOperationName, theResourceType);
	}

	@Override
	public void removeAllResourcesForOperation(String theOperationName) {
		ourLog.debug("Removing any filtering of resources for {} operations", theOperationName);
		myOperationToOmittedResourceTypes.removeAll(theOperationName);
	}
}
