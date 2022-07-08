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

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.mdm.api.MdmConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE;

@JsonDeserialize(converter = MdmRulesJson.MdmRulesJsonConverter.class)
public class MdmRulesJson implements IModelJson {

	@JsonProperty(value = "version", required = true)
	String myVersion;
	@JsonProperty(value = "candidateSearchParams", required = true)
	List<MdmResourceSearchParamJson> myCandidateSearchParams = new ArrayList<>();
	@JsonProperty(value = "candidateFilterSearchParams", required = true)
	List<MdmFilterSearchParamJson> myCandidateFilterSearchParams = new ArrayList<>();
	@JsonProperty(value = "matchFields", required = true)
	List<MdmFieldMatchJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty(value = "matchResultMap", required = true)
	Map<String, MdmMatchResultEnum> myMatchResultMap = new HashMap<>();

	/**
	 * This field is deprecated, use eidSystems instead.
	 */
	@Deprecated
	@JsonProperty(value = "eidSystem")
	String myEnterpriseEIDSystem;

	@JsonProperty(value = "eidSystems")
	Map<String, String> myEnterpriseEidSystems = new HashMap<>();
	@JsonProperty(value = "mdmTypes")
	List<String> myMdmTypes;

	transient VectorMatchResultMap myVectorMatchResultMap;

	public void addMatchField(MdmFieldMatchJson theMatchRuleName) {
		myMatchFieldJsonList.add(theMatchRuleName);
	}

	public void addResourceSearchParam(MdmResourceSearchParamJson theSearchParam) {
		myCandidateSearchParams.add(theSearchParam);
	}

	public void addFilterSearchParam(MdmFilterSearchParamJson theSearchParam) {
		myCandidateFilterSearchParams.add(theSearchParam);
	}

	int size() {
		return myMatchFieldJsonList.size();
	}

	MdmFieldMatchJson get(int theIndex) {
		return myMatchFieldJsonList.get(theIndex);
	}

	MdmMatchResultEnum getMatchResult(String theFieldMatchNames) {
		return myMatchResultMap.get(theFieldMatchNames);
	}

	public MdmMatchResultEnum getMatchResult(Long theMatchVector) {
		return myVectorMatchResultMap.get(theMatchVector);
	}

	public void putMatchResult(String theFieldMatchNames, MdmMatchResultEnum theMatchResult) {
		myMatchResultMap.put(theFieldMatchNames, theMatchResult);
		initialize();
	}

	Map<String, MdmMatchResultEnum> getMatchResultMap() {
		return Collections.unmodifiableMap(myMatchResultMap);
	}

	/**
	 * Must call initialize() before calling getMatchResult(Long)
	 */
	public void initialize() {
		myVectorMatchResultMap = new VectorMatchResultMap(this);
	}

	public List<MdmFieldMatchJson> getMatchFields() {
		return Collections.unmodifiableList(myMatchFieldJsonList);
	}

	public List<MdmResourceSearchParamJson> getCandidateSearchParams() {
		return Collections.unmodifiableList(myCandidateSearchParams);
	}

	public List<MdmFilterSearchParamJson> getCandidateFilterSearchParams() {
		return Collections.unmodifiableList(myCandidateFilterSearchParams);
	}

	/**
	 * Use {@link this#getEnterpriseEIDSystemForResourceType(String)} instead.
	 */
	@Deprecated
	public String getEnterpriseEIDSystem() {
		return myEnterpriseEIDSystem;
	}

	/**
	 * Use {@link this#setEnterpriseEIDSystems(Map)} (String)} or {@link this#addEnterpriseEIDSystem(String, String)} instead.
	 */
	@Deprecated
	public void setEnterpriseEIDSystem(String theEnterpriseEIDSystem) {
		myEnterpriseEIDSystem = theEnterpriseEIDSystem;
	}

	public void setEnterpriseEIDSystems(Map<String, String> theEnterpriseEIDSystems) {
		myEnterpriseEidSystems = theEnterpriseEIDSystems;
	}

	public void addEnterpriseEIDSystem(String theResourceType, String theEidSystem) {
		if (myEnterpriseEidSystems == null) {
			myEnterpriseEidSystems = new HashMap<>();
		}
		myEnterpriseEidSystems.put(theResourceType, theEidSystem);
	}

	public Map<String, String> getEnterpriseEIDSystems() {
		//First try the new property.
		if (myEnterpriseEidSystems != null && !myEnterpriseEidSystems.isEmpty()) {
			return myEnterpriseEidSystems;
		//If that fails, fall back to our deprecated property.
		} else if (!StringUtils.isBlank(myEnterpriseEIDSystem)) {
			HashMap<String , String> retVal = new HashMap<>();
			retVal.put(ALL_RESOURCE_SEARCH_PARAM_TYPE, myEnterpriseEIDSystem);
			return retVal;
		//Otherwise, return an empty map.
		} else {
			return Collections.emptyMap();
		}
	}

	public String getEnterpriseEIDSystemForResourceType(String theResourceType) {
		Map<String, String> enterpriseEIDSystems = getEnterpriseEIDSystems();
		if (enterpriseEIDSystems.containsKey(ALL_RESOURCE_SEARCH_PARAM_TYPE)) {
			return enterpriseEIDSystems.get(ALL_RESOURCE_SEARCH_PARAM_TYPE);
		} else {
			return enterpriseEIDSystems.get(theResourceType);
		}
	}

	public String getVersion() {
		return myVersion;
	}

	public MdmRulesJson setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	private void validate() {
		Validate.notBlank(myVersion, "version may not be blank");

		Map<String, String> enterpriseEIDSystems = getEnterpriseEIDSystems();

		//If we have a * eid system, there should only be one.
		if (enterpriseEIDSystems.containsKey(ALL_RESOURCE_SEARCH_PARAM_TYPE)) {
			Validate.isTrue(enterpriseEIDSystems.size() == 1);
		}

	}

	public String getSummary() {
		return myCandidateSearchParams.size() + " Candidate Search Params, " +
			myCandidateFilterSearchParams.size() + " Filter Search Params, " +
			myMatchFieldJsonList.size() + " Match Fields, " +
			myMatchResultMap.size() + " Match Result Entries";
	}

	public String getFieldMatchNamesForVector(long theVector) {
		return myVectorMatchResultMap.getFieldMatchNames(theVector);
	}

	public String getDetailedFieldMatchResultWithSuccessInformation(long theVector) {
		List<String> fieldMatchResult = new ArrayList<>();
		for (int i = 0; i < myMatchFieldJsonList.size(); ++i) {
			if ((theVector & (1 << i)) == 0) {
				fieldMatchResult.add(myMatchFieldJsonList.get(i).getName() + ": NO");
			} else {
				fieldMatchResult.add(myMatchFieldJsonList.get(i).getName() + ": YES");
			}
		}
		return String.join("\n" ,fieldMatchResult);
	}

	@VisibleForTesting
	VectorMatchResultMap getVectorMatchResultMapForUnitTest() {
		return myVectorMatchResultMap;
	}

	/**
	 * Ensure the vector map is initialized after we deserialize
	 */
	static class MdmRulesJsonConverter extends StdConverter<MdmRulesJson, MdmRulesJson> {

		/**
		 * This empty constructor is required by Jackson
		 */
		public MdmRulesJsonConverter() {
		}

		@Override
		public MdmRulesJson convert(MdmRulesJson theMdmRulesJson) {
			theMdmRulesJson.validate();
			theMdmRulesJson.initialize();
			return theMdmRulesJson;
		}
	}

	public List<String> getMdmTypes() {
		return myMdmTypes;
	}

	public void setMdmTypes(List<String> theMdmTypes) {
		myMdmTypes = theMdmTypes;
	}

}
