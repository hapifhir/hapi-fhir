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

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.util.MatchRuleUtil;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

	/**
	 * Maps a resource type to the EID system URIs that identify it. A resource type may be mapped to a
	 * single system or to several; see {@link EidSystemListDeserializer} for the accepted JSON forms.
	 * Declaration order is significant - the first system configured for a resource type is treated as
	 * its primary one.
	 */
	@JsonProperty(value = "eidSystems")
	@JsonDeserialize(contentUsing = EidSystemListDeserializer.class)
	Map<String, List<String>> myEnterpriseEidSystems = new LinkedHashMap<>();

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
		validate();
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
	 * Use {@link #getEnterpriseEIDSystemsForResourceType(String)} instead.
	 */
	@Deprecated
	public String getEnterpriseEIDSystem() {
		return myEnterpriseEIDSystem;
	}

	/**
	 * Use {@link #setEidSystemsByResourceType(Map)} or {@link #addEnterpriseEIDSystems(String, List)} instead.
	 */
	@Deprecated
	public void setEnterpriseEIDSystem(String theEnterpriseEIDSystem) {
		myEnterpriseEIDSystem = theEnterpriseEIDSystem;
	}

	/**
	 * Use {@link #setEidSystemsByResourceType(Map)} instead. Each system is escalated to a one-element list.
	 *
	 * @param theEnterpriseEIDSystems one EID system per resource type
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	public void setEnterpriseEIDSystems(Map<String, String> theEnterpriseEIDSystems) {
		Map<String, List<String>> escalated = new LinkedHashMap<>();
		theEnterpriseEIDSystems.forEach(
				(resourceType, eidSystem) -> escalated.put(resourceType, new ArrayList<>(List.of(eidSystem))));
		myEnterpriseEidSystems = escalated;
	}

	/**
	 * Replaces the EID systems configured for every resource type.
	 *
	 * @param theEidSystems the EID systems for each resource type, in the order they should be applied
	 */
	public void setEidSystemsByResourceType(Map<String, List<String>> theEidSystems) {
		Map<String, List<String>> copy = new LinkedHashMap<>();
		theEidSystems.forEach((resourceType, eidSystems) -> copy.put(resourceType, new ArrayList<>(eidSystems)));
		myEnterpriseEidSystems = copy;
	}

	/**
	 * Appends an EID system to those already configured for a resource type. Adding a system that is
	 * already configured for that resource type is a no-op.
	 *
	 * @param theResourceType the resource type the EID system identifies
	 * @param theEidSystem the EID system URI to append
	 */
	public void addEnterpriseEIDSystem(String theResourceType, String theEidSystem) {
		if (myEnterpriseEidSystems == null) {
			myEnterpriseEidSystems = new LinkedHashMap<>();
		}
		List<String> eidSystems =
				new ArrayList<>(myEnterpriseEidSystems.getOrDefault(theResourceType, Collections.emptyList()));
		if (!eidSystems.contains(theEidSystem)) {
			eidSystems.add(theEidSystem);
			myEnterpriseEidSystems.put(theResourceType, eidSystems);
		}
	}

	/**
	 * Appends EID systems to those already configured for a resource type, in the order given. A system
	 * already configured for that resource type is not added again, so this is exactly
	 * {@link #addEnterpriseEIDSystem(String, String)} called once per element.
	 * <p>
	 * To replace what is configured rather than add to it, use {@link #setEidSystemsByResourceType(Map)}.
	 * </p>
	 *
	 * @param theResourceType the resource type the EID systems identify
	 * @param theEidSystems the EID system URIs to append, in the order they should be applied
	 */
	public void addEnterpriseEIDSystems(String theResourceType, List<String> theEidSystems) {
		theEidSystems.forEach(eidSystem -> addEnterpriseEIDSystem(theResourceType, eidSystem));
	}

	/**
	 * Returns the EID systems configured for each resource type. Reads the {@code eidSystems} property if
	 * one is present, otherwise falls back to the deprecated {@code eidSystem} property scoped to all
	 * resource types, otherwise returns an empty map.
	 *
	 * @return the configured EID systems keyed by resource type, unmodifiable down to the lists themselves;
	 * never {@literal null}
	 */
	public Map<String, List<String>> getEidSystemsByResourceType() {
		// First try the new property.
		if (myEnterpriseEidSystems != null && !myEnterpriseEidSystems.isEmpty()) {
			Map<String, List<String>> retVal = new LinkedHashMap<>();
			myEnterpriseEidSystems.forEach(
					(resourceType, eidSystems) -> retVal.put(resourceType, Collections.unmodifiableList(eidSystems)));
			return Collections.unmodifiableMap(retVal);
			// If that fails, fall back to our deprecated property.
		} else if (!StringUtils.isBlank(myEnterpriseEIDSystem)) {
			return Map.of(ALL_RESOURCE_SEARCH_PARAM_TYPE, List.of(myEnterpriseEIDSystem));
			// Otherwise, return an empty map.
		} else {
			return Collections.emptyMap();
		}
	}

	/**
	 * Returns every EID system that identifies the given resource type, honouring the
	 * {@link ca.uhn.fhir.mdm.api.MdmConstants#ALL_RESOURCE_SEARCH_PARAM_TYPE} wildcard key.
	 *
	 * @param theResourceType the resource type to look up
	 * @return the configured EID systems in declaration order; empty if none are configured
	 */
	public List<String> getEnterpriseEIDSystemsForResourceType(String theResourceType) {
		Map<String, List<String>> eidSystems = getEidSystemsByResourceType();
		List<String> retVal = eidSystems.containsKey(ALL_RESOURCE_SEARCH_PARAM_TYPE)
				? eidSystems.get(ALL_RESOURCE_SEARCH_PARAM_TYPE)
				: eidSystems.get(theResourceType);
		return retVal == null ? Collections.emptyList() : Collections.unmodifiableList(retVal);
	}

	/**
	 * Use {@link #getEidSystemsByResourceType()} instead. Reports only the first EID system configured
	 * for each resource type.
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	public Map<String, String> getEnterpriseEIDSystems() {
		Map<String, String> retVal = new LinkedHashMap<>();
		getEidSystemsByResourceType().forEach((resourceType, eidSystems) -> {
			if (!eidSystems.isEmpty()) {
				retVal.put(resourceType, eidSystems.get(0));
			}
		});
		return retVal;
	}

	/**
	 * Use {@link #getEnterpriseEIDSystemsForResourceType(String)} instead. Reports only the first EID
	 * system configured for the resource type.
	 */
	@Deprecated(since = "8.14.0", forRemoval = true)
	public String getEnterpriseEIDSystemForResourceType(String theResourceType) {
		return getEnterpriseEIDSystemsForResourceType(theResourceType).stream()
				.findFirst()
				.orElse(null);
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

		Map<String, List<String>> enterpriseEIDSystems = getEidSystemsByResourceType();

		// If we have a * eid system, there should only be one.
		if (enterpriseEIDSystems.containsKey(ALL_RESOURCE_SEARCH_PARAM_TYPE)) {
			Validate.isTrue(enterpriseEIDSystems.size() == 1);
		}

		Validate.isTrue(
				MatchRuleUtil.canHandleRuleCount(myMatchFieldJsonList),
				String.format(
						"MDM cannot guarantee accuracy with more than %d match fields.", MatchRuleUtil.MAX_RULE_COUNT));
	}

	public String getSummary() {
		return myCandidateSearchParams.size() + " Candidate Search Params, " + myCandidateFilterSearchParams.size()
				+ " Filter Search Params, " + myMatchFieldJsonList.size()
				+ " Match Fields, " + myMatchResultMap.size()
				+ " Match Result Entries";
	}

	public String getFieldMatchNamesForVector(long theVector) {
		return myVectorMatchResultMap.getFieldMatchNames(theVector);
	}

	public Set<Map.Entry<String, MdmMatchResultEnum>> getMatchedRulesFromVectorMap(Long theLong) {
		Set<String> matchedRules = myVectorMatchResultMap.getMatchedRules(theLong);
		return myMatchResultMap.entrySet().stream()
				.filter(e -> matchedRules.contains(e.getKey()))
				.collect(Collectors.toSet());
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
		return String.join("\n", fieldMatchResult);
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
		public MdmRulesJsonConverter() {}

		@Override
		public MdmRulesJson convert(MdmRulesJson theMdmRulesJson) {
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
