package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;

/**
 * This interface should be considered experimental and will likely change in future releases of HAPI. Use with caution!
 */
public interface IVersionSpecificBundleFactory {

	void addResourcesToBundle(List<IBaseResource> theResult, BundleTypeEnum theBundleType, String theServerBase, BundleInclusionRule theBundleInclusionRule, Set<Include> theIncludes);

	void addRootPropertiesToBundle(String theAuthor, String theServerBase, String theCompleteUrl, Integer theTotalResults, BundleTypeEnum theBundleType, IPrimitiveType<Date> theLastUpdated);

	void initializeBundleFromBundleProvider(IRestfulServer<?> theServer, IBundleProvider theResult, EncodingEnum theResponseEncoding, String theServerBase, String theCompleteUrl, boolean thePrettyPrint,
			int theOffset, Integer theCount, String theSearchId, BundleTypeEnum theBundleType, Set<Include> theIncludes);

	Bundle getDstu1Bundle();

	IBaseResource getResourceBundle();

	void initializeBundleFromResourceList(String theAuthor, List<? extends IBaseResource> theResult, String theServerBase, String theCompleteUrl, int theTotalResults, BundleTypeEnum theBundleType);

	void initializeWithBundleResource(IBaseResource theResource);

	List<IBaseResource> toListOfResources();

}
