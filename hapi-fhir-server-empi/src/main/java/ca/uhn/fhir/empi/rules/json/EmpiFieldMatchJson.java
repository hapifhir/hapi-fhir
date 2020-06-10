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

import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Contains all business data for determining if a match exists on a particular field, given:
 *
 * 1. A {@link EmpiMetricEnum} which determines the actual similarity values.
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
	@JsonProperty(value = "metric", required = true)
	EmpiMetricEnum myMetric;
	@JsonProperty("matchThreshold")
	Double myMatchThreshold;
	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public EmpiMetricEnum getMetric() {
		return myMetric;
	}

	public EmpiFieldMatchJson setMetric(EmpiMetricEnum theMetric) {
		myMetric = theMetric;
		return this;
	}

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

	@Nullable
	public Double getMatchThreshold() {
		return myMatchThreshold;
	}

	public EmpiFieldMatchJson setMatchThreshold(double theMatchThreshold) {
		myMatchThreshold = theMatchThreshold;
		return this;
	}

	public String getName() {
		return myName;
	}

	public EmpiFieldMatchJson setName(@Nonnull String theName) {
		myName = theName;
		return this;
	}

	public boolean getExact() {
		return myExact;
	}

	public EmpiFieldMatchJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}
}
