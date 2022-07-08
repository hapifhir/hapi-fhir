package ca.uhn.fhir.mdm.rules.json;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.MdmMatchEvaluation;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBase;

import javax.annotation.Nonnull;

/**
 * Contains all business data for determining if a match exists on a particular field, given:
 * <p></p>
 * 1. A {@link MdmMatcherEnum} which determines the actual similarity values.
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

	public boolean isMatcherSupportingEmptyFields() {
		return (getMatcher() == null) ? false : getMatcher().isMatchingEmptyFields();
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

	public MdmMatchEvaluation match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		if (myMatcher != null) {
			boolean result = myMatcher.match(theFhirContext, theLeftValue, theRightValue);
			return new MdmMatchEvaluation(result, result ? 1.0 : 0.0);
		}
		if (mySimilarity != null) {
			return mySimilarity.match(theFhirContext, theLeftValue, theRightValue);
		}
		throw new InternalErrorException(Msg.code(1522) + "Field Match " + myName + " has neither a matcher nor a similarity.");
	}

	public String getFhirPath() {
		return myFhirPath;
	}

	public MdmFieldMatchJson setFhirPath(String theFhirPath) {
		myFhirPath = theFhirPath;
		return this;
	}
}
