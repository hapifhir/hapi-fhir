/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Figure out how we're going to run the query up front, and build a branchless strategy object.
 */
public class SearchStrategyFactory {
	private final JpaStorageSettings myStorageSettings;

	@Nullable
	private final IFulltextSearchSvc myFulltextSearchSvc;

	public interface ISearchStrategy extends Supplier<IBundleProvider> {}

	// someday
	//	public class DirectHSearch implements  ISearchStrategy {};
	//	public class JPAOffsetSearch implements  ISearchStrategy {};
	//	public class JPASavedSearch implements  ISearchStrategy {};
	//	public class JPAHybridHSearchSavedSearch implements  ISearchStrategy {};
	//	public class SavedSearchAdaptorStrategy implements  ISearchStrategy {};

	public SearchStrategyFactory(
			JpaStorageSettings theStorageSettings, @Nullable IFulltextSearchSvc theFulltextSearchSvc) {
		myStorageSettings = theStorageSettings;
		myFulltextSearchSvc = theFulltextSearchSvc;
	}

	public boolean isSupportsHSearchDirect(
			String theResourceType, SearchParameterMap theParams, RequestDetails theRequestDetails) {
		return myFulltextSearchSvc != null
				&& myStorageSettings.isStoreResourceInHSearchIndex()
				&& myStorageSettings.isAdvancedHSearchIndexing()
				&& myFulltextSearchSvc.supportsAllOf(theParams)
				&& theParams.getSummaryMode() == null
				&& theParams.getSearchTotalMode() == null;
	}

	public ISearchStrategy makeDirectStrategy(
			String theSearchUUID,
			String theResourceType,
			SearchParameterMap theParams,
			RequestDetails theRequestDetails) {
		return () -> {
			if (myFulltextSearchSvc == null) {
				return new SimpleBundleProvider(Collections.emptyList(), theSearchUUID);
			}

			List<IBaseResource> resources =
					myFulltextSearchSvc.searchForResources(theResourceType, theParams, theRequestDetails);
			SimpleBundleProvider result = new SimpleBundleProvider(resources, theSearchUUID);
			result.setSize(resources.size());
			return result;
		};
	}
}
