/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.MdmMatchEvaluation;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;

public class MdmSimilarityJson implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	String myAlgorithm;

	@JsonProperty(value = "matchThreshold", required = true)
	Double myMatchThreshold;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public String getAlgorithm() {
		return myAlgorithm;
	}

	public MdmSimilarityJson setAlgorithm(String theAlgorithm) {
		myAlgorithm = theAlgorithm;
		return this;
	}

	/**
	 * Convenience overload for backward compatibility with code that passes a {@link MdmSimilarityEnum}.
	 * @deprecated Use {@link #setAlgorithm(String)} instead.
	 */
	@Deprecated(since = "8_10_0", forRemoval = true)
	public MdmSimilarityJson setAlgorithm(MdmSimilarityEnum theAlgorithm) {
		myAlgorithm = theAlgorithm.name();
		return this;
	}

	@Nullable
	public Double getMatchThreshold() {
		return myMatchThreshold;
	}

	public MdmSimilarityJson setMatchThreshold(double theMatchThreshold) {
		myMatchThreshold = theMatchThreshold;
		return this;
	}

	public boolean getExact() {
		return myExact;
	}

	public MdmSimilarityJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}

	/**
	 * @deprecated Matching is now performed by {@link ca.uhn.fhir.mdm.rules.svc.MdmResourceFieldMatcher}
	 *   using the {@link ca.uhn.fhir.mdm.rules.similarity.ISimilarityFactory}. This method only works
	 *   for built-in {@link MdmSimilarityEnum} algorithm names.
	 */
	@Deprecated(since = "8_10_0", forRemoval = true)
	public MdmMatchEvaluation match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		MdmSimilarityEnum similarityEnum = MdmSimilarityEnum.valueOf(myAlgorithm);
		return similarityEnum.match(theFhirContext, theLeftValue, theRightValue, myExact, myMatchThreshold);
	}
}
