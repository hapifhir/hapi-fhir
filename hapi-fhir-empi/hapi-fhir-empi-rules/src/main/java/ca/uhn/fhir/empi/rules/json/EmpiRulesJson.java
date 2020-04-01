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

import ca.uhn.fhir.jpa.api.EmpiMatchResultEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.*;

public class EmpiRulesJson implements IModelJson {
	@JsonProperty("resourceSearchParams")
	List<EmpiResourceSearchParamJson> myResourceSearchParams = new ArrayList<>();
	@JsonProperty("filterSearchParams")
	List<EmpiFilterSearchParamJson> myFilterSearchParams = new ArrayList<>();
	@JsonProperty("matchFields")
	List<EmpiFieldMatchJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty("weightMap")
	Map<String, Double> myWeightMap = new HashMap<>();
	@JsonProperty("noMatchThreshold")
	double myNoMatchThreshold;
	@JsonProperty("matchThreshold")
	double myMatchThreshold;
	@JsonProperty("eidSystem")
	String myEnterpriseEIDSystem;

	transient VectorWeightMap myVectorWeightMap;

	public void addMatchField(EmpiFieldMatchJson theMatchRuleName) {
		myMatchFieldJsonList.add(theMatchRuleName);
	}

	public void addResourceSearchParam(EmpiResourceSearchParamJson theSearchParam) {
		myResourceSearchParams.add(theSearchParam);
	}

	public void addFilterSearchParam(EmpiFilterSearchParamJson theSearchParam) {
		myFilterSearchParams.add(theSearchParam);
	}

	int size() {
		return myMatchFieldJsonList.size();
	}

	EmpiFieldMatchJson get(int theIndex) {
		return myMatchFieldJsonList.get(theIndex);
	}

	public EmpiMatchResultEnum getMatchResult(double theWeight) {
		if (theWeight <= myNoMatchThreshold) {
			return EmpiMatchResultEnum.NO_MATCH;
		} else if (theWeight >= myMatchThreshold) {
			return EmpiMatchResultEnum.MATCH;
		} else {
			return EmpiMatchResultEnum.POSSIBLE_MATCH;
		}
	}

	double getWeight(String theFieldMatchNames) {
		return myWeightMap.get(theFieldMatchNames);
	}

	public double getWeight(Long theMatchVector) {
		initVectorWeightMapIfRequired();
		Double result = myVectorWeightMap.get(theMatchVector);
		return MoreObjects.firstNonNull(result, 0.0);
	}

	public void putWeight(String theFieldMatchNames, double theWeight) {
		myWeightMap.put(theFieldMatchNames, theWeight);

		initVectorWeightMapIfRequired();
		myVectorWeightMap.put(theFieldMatchNames, theWeight);
	}

	Map<String, Double> getWeightMap() {
		return Collections.unmodifiableMap(myWeightMap);
	}

	VectorWeightMap getVectorWeightMap() {
		initVectorWeightMapIfRequired();
		return myVectorWeightMap;
	}

	private void initVectorWeightMapIfRequired() {
		if (myVectorWeightMap == null) {
			myVectorWeightMap = new VectorWeightMap(this);
		}
	}

	public double getMatchThreshold() {
		return myMatchThreshold;
	}

	public EmpiRulesJson setMatchThreshold(double theMatchThreshold) {
		myMatchThreshold = theMatchThreshold;
		return this;
	}

	public double getNoMatchThreshold() {
		return myNoMatchThreshold;
	}

	public EmpiRulesJson setNoMatchThreshold(double theNoMatchThreshold) {
		myNoMatchThreshold = theNoMatchThreshold;
		return this;
	}

	public List<EmpiFieldMatchJson> getMatchFields() {
		return Collections.unmodifiableList(myMatchFieldJsonList);
	}

	public List<EmpiResourceSearchParamJson> getResourceSearchParams() {
		return Collections.unmodifiableList(myResourceSearchParams);
	}

	public List<EmpiFilterSearchParamJson> getFilterSearchParams() {
		return Collections.unmodifiableList(myFilterSearchParams);
	}

	public String getEnterpriseEIDSystem() {
		return myEnterpriseEIDSystem;
	}

}
