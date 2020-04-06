package ca.uhn.fhir.empi.rules.json;

/*-
 * #%L
 * hapi-fhir-empi-rules
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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

/**
 * Contains all business data for determining if a match exists on a particular field, given:
 *
 * 1. A {@link DistanceMetricEnum} which determines the actual similarity values.
 * 2. A given resource type (e.g. Patient)
 * 3. A given FHIRPath expression for finding the particular primitive to be used for comparison. (e.g. name.given)
 */
public class EmpiFieldMatchJson implements IModelJson {
	@JsonProperty("name")
	String myName;
	@JsonProperty("resourceType")
	String myResourceType;
	@JsonProperty("resourcePath")
	String myResourcePath;
	@JsonProperty("metric")
	DistanceMetricEnum myMetric;
	@JsonProperty("matchThreshold")
	double myMatchThreshold;

	public DistanceMetricEnum getMetric() {
		return myMetric;
	}

	public EmpiFieldMatchJson setMetric(DistanceMetricEnum theMetric) {
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

	public double getMatchThreshold() {
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
}
