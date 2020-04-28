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

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpiRulesJson implements IModelJson {
	@JsonProperty("candidateSearchParams")
	List<EmpiResourceSearchParamJson> myResourceSearchParams = new ArrayList<>();
	@JsonProperty("candidateFilterSearchParams")
	List<EmpiFilterSearchParamJson> myFilterSearchParams = new ArrayList<>();
	@JsonProperty("matchFields")
	List<EmpiFieldMatchJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty("weightMap")
	Map<String, EmpiMatchResultEnum> myMatchResultMap = new HashMap<>();
	@JsonProperty(value = "eidSystem")
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

	EmpiMatchResultEnum getMatchResult(String theFieldMatchNames) {
		return myMatchResultMap.get(theFieldMatchNames);
	}

	public EmpiMatchResultEnum getMatchResult(Long theMatchVector) {
		initVectorWeightMapIfRequired();
		EmpiMatchResultEnum result = myVectorWeightMap.get(theMatchVector);
		return MoreObjects.firstNonNull(result, EmpiMatchResultEnum.NO_MATCH);
	}

	public void putMatchResult(String theFieldMatchNames, EmpiMatchResultEnum theMatchResult) {
		myMatchResultMap.put(theFieldMatchNames, theMatchResult);

		initVectorWeightMapIfRequired();
		myVectorWeightMap.put(theFieldMatchNames, theMatchResult);
	}

	Map<String, EmpiMatchResultEnum> getMatchResultMap() {
		return Collections.unmodifiableMap(myMatchResultMap);
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

	public void setEnterpriseEIDSystem(String theEnterpriseEIDSystem) {
		myEnterpriseEIDSystem = theEnterpriseEIDSystem;
	}
}
