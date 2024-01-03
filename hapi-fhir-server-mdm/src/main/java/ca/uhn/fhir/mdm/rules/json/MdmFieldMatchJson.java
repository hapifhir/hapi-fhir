/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

/**
 * Contains all business data for determining if a match exists on a particular field, given:
 * <p></p>
 * 1. A {@link MatchTypeEnum} which determines the actual similarity values.
 * 2. A given resource type (e.g. Patient)
 * 3. A given FHIRPath expression for finding the particular primitive to be used for comparison. (e.g. name.given)
 */
public class MdmFieldMatchJson implements IModelJson {
	@JsonProperty(value = "name", required = true)
	String myName;

	@JsonProperty(value = "resourceType", required = true)
	String myResourceType;

	@JsonProperty(value = "resourcePath", required = false)
	String myResourcePath;

	@JsonProperty(value = "fhirPath", required = false)
	String myFhirPath;

	@JsonProperty(value = "matcher", required = false)
	MdmMatcherJson myMatcher;

	@JsonProperty(value = "similarity", required = false)
	MdmSimilarityJson mySimilarity;

	public String getResourceType() {
		return myResourceType;
	}

	public MdmFieldMatchJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getResourcePath() {
		return myResourcePath;
	}

	public MdmFieldMatchJson setResourcePath(String theResourcePath) {
		myResourcePath = theResourcePath;
		return this;
	}

	public String getName() {
		return myName;
	}

	public MdmFieldMatchJson setName(@Nonnull String theName) {
		myName = theName;
		return this;
	}

	public MdmMatcherJson getMatcher() {
		return myMatcher;
	}

	public MdmFieldMatchJson setMatcher(MdmMatcherJson theMatcher) {
		myMatcher = theMatcher;
		return this;
	}

	public MdmSimilarityJson getSimilarity() {
		return mySimilarity;
	}

	public MdmFieldMatchJson setSimilarity(MdmSimilarityJson theSimilarity) {
		mySimilarity = theSimilarity;
		return this;
	}

	public String getFhirPath() {
		return myFhirPath;
	}

	public MdmFieldMatchJson setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
		return this;
	}
}
