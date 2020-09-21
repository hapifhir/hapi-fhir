package ca.uhn.fhir.empi.rules.json;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchEvaluation;
import ca.uhn.fhir.empi.rules.matcher.EmpiMatcherEnum;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBase;

import javax.annotation.Nonnull;

/**
 * Contains all business data for determining if a match exists on a particular field, given:
 * <p>
 * 1. A {@link EmpiMatcherEnum} which determines the actual similarity values.
 * 2. A given resource type (e.g. Patient)
 * 3. A given FHIRPath expression for finding the particular primitive to be used for comparison. (e.g. name.given)
 */
public class EmpiFieldMatchJson implements IModelJson {
	@JsonProperty(value = "name", required = true)
	String myName;

	@JsonProperty(value = "resourceType", required = true)
	String myResourceType;

	@JsonProperty(value = "resourcePath", required = true)
	String myResourcePath;

	@JsonProperty(value = "matcher", required = false)
	EmpiMatcherJson myMatcher;

	@JsonProperty(value = "similarity", required = false)
	EmpiSimilarityJson mySimilarity;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public String getResourceType() {
		return myResourceType;
	}

	public EmpiFieldMatchJson setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public String getResourcePath() {
		return myResourcePath;
	}

	public EmpiFieldMatchJson setResourcePath(String theResourcePath) {
		myResourcePath = theResourcePath;
		return this;
	}

	public String getName() {
		return myName;
	}

	public EmpiFieldMatchJson setName(@Nonnull String theName) {
		myName = theName;
		return this;
	}

	public EmpiMatcherJson getMatcher() {
		return myMatcher;
	}

	public EmpiFieldMatchJson setMatcher(EmpiMatcherJson theMatcher) {
		myMatcher = theMatcher;
		return this;
	}

	public EmpiSimilarityJson getSimilarity() {
		return mySimilarity;
	}

	public EmpiFieldMatchJson setSimilarity(EmpiSimilarityJson theSimilarity) {
		mySimilarity = theSimilarity;
		return this;
	}

	public boolean getExact() {
		return myExact;
	}

	public EmpiFieldMatchJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}

	public EmpiMatchEvaluation match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		if (myMatcher != null) {
			boolean result = myMatcher.myAlgorithm.match(theFhirContext, theLeftValue, theRightValue, myExact);
			return new EmpiMatchEvaluation(result, result ? 1.0 : 0.0);
		}
		if (mySimilarity != null) {
			return mySimilarity.myAlgorithm.match(theFhirContext, theLeftValue, theRightValue,myExact, mySimilarity.getMatchThreshold());
		}
		throw new InternalErrorException("Field Match " + myName + " has neither a matcher nor a similarity.");
	}
}
