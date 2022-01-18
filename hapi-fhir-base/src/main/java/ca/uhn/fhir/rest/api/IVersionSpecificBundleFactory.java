package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * This interface should be considered experimental and will likely change in future releases of HAPI. Use with caution!
 */
public interface IVersionSpecificBundleFactory {

	void addResourcesToBundle(List<IBaseResource> theResult, BundleTypeEnum theBundleType, String theServerBase, @Nullable BundleInclusionRule theBundleInclusionRule, @Nullable Set<Include> theIncludes);

	void addRootPropertiesToBundle(String theId, @Nonnull BundleLinks theBundleLinks, Integer theTotalResults, IPrimitiveType<Date> theLastUpdated);

	IBaseResource getResourceBundle();

	/**
	 * @deprecated This was deprecated in HAPI FHIR 4.1.0 as it provides duplicate functionality to the {@link #addRootPropertiesToBundle(String, BundleLinks, Integer, IPrimitiveType<Date>)}
	 * and {@link #addResourcesToBundle(List, BundleTypeEnum, String, BundleInclusionRule, Set)} methods
	 */
	@Deprecated
	default void initializeBundleFromResourceList(String theAuthor, List<? extends IBaseResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType) {
		addTotalResultsToBundle(theResult.size(), theBundleType);
		addResourcesToBundle(new ArrayList<>(theResult), theBundleType, null, null, null);
	}

	void initializeWithBundleResource(IBaseResource theResource);

	List<IBaseResource> toListOfResources();

	void addTotalResultsToBundle(Integer theTotalResults, BundleTypeEnum theBundleType);
}
