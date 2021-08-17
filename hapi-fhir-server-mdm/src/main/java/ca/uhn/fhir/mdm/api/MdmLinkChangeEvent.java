package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.HashSet;
import java.util.Set;

public class MdmLinkChangeEvent implements IModelJson {

	@JsonProperty(value = "matchResult")
	private MdmMatchResultEnum myMdmMatchResult;
	@JsonProperty(value = "linkSource")
	private MdmLinkSourceEnum myMdmLinkSource;
	@JsonProperty(value = "eidMatch")
	private Boolean myEidMatch;
	@JsonProperty(value = "newGoldenResource")
	private Boolean myNewGoldenResource;
	@JsonProperty(value = "score")
	private Double myScore;
	@JsonProperty(value = "ruleCount")
	private Long myRuleCount;
	@JsonProperty(value = "targetResourceId", required = true)
	private String myTargetResourceId;
	@JsonProperty(value = "goldenResourceId", required = true)
	private String myGoldenResourceId;
	@JsonProperty(value = "duplicateResourceIds")
	private Set<String> myDuplicateGoldenResourceIds = new HashSet<>();

	public String getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public void setGoldenResourceId(IBaseResource theGoldenResourceId) {
		setGoldenResourceId(getIdAsString(theGoldenResourceId));
	}

	public void setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
	}

	private String getIdAsString(IBaseResource theResource) {
		if (theResource == null) {
			return null;
		}
		IIdType idElement = theResource.getIdElement();
		if (idElement == null) {
			return null;
		}
		return idElement.getValueAsString();
	}

	public String getTargetResourceId() {
		return myTargetResourceId;
	}

	public void setTargetResourceId(IBaseResource theTargetResource) {
		setTargetResourceId(getIdAsString(theTargetResource));
	}

	public void setTargetResourceId(String theTargetResourceId) {
		myTargetResourceId = theTargetResourceId;
	}

	public Set<String> getDuplicateGoldenResourceIds() {
		return myDuplicateGoldenResourceIds;
	}

	public void setDuplicateGoldenResourceIds(Set<String> theDuplicateGoldenResourceIds) {
		myDuplicateGoldenResourceIds = theDuplicateGoldenResourceIds;
	}

	public MdmLinkChangeEvent addDuplicateGoldenResourceId(IBaseResource theDuplicateGoldenResourceId) {
		String id = getIdAsString(theDuplicateGoldenResourceId);
		if (id != null) {
			getDuplicateGoldenResourceIds().add(id);
		}
		return this;
	}

	public MdmMatchResultEnum getMdmMatchResult() {
		return myMdmMatchResult;
	}

	public void setMdmMatchResult(MdmMatchResultEnum theMdmMatchResult) {
		myMdmMatchResult = theMdmMatchResult;
	}

	public MdmLinkSourceEnum getMdmLinkSource() {
		return myMdmLinkSource;
	}

	public void setMdmLinkSource(MdmLinkSourceEnum theMdmLinkSource) {
		myMdmLinkSource = theMdmLinkSource;
	}

	public Boolean getEidMatch() {
		return myEidMatch;
	}

	public void setEidMatch(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
	}

	public Boolean getNewGoldenResource() {
		return myNewGoldenResource;
	}

	public void setNewGoldenResource(Boolean theNewGoldenResource) {
		myNewGoldenResource = theNewGoldenResource;
	}

	public Double getScore() {
		return myScore;
	}

	public void setScore(Double theScore) {
		myScore = theScore;
	}

	public Long getRuleCount() {
		return myRuleCount;
	}

	public void setRuleCount(Long theRuleCount) {
		myRuleCount = theRuleCount;
	}

	@Override
	public String toString() {
		return "MdmLinkChangeEvent{" +
			"myMdmMatchResult=" + myMdmMatchResult +
			", myMdmLinkSource=" + myMdmLinkSource +
			", myEidMatch=" + myEidMatch +
			", myNewGoldenResource=" + myNewGoldenResource +
			", myScore=" + myScore +
			", myRuleCount=" + myRuleCount +
			", myTargetResourceId='" + myTargetResourceId + '\'' +
			", myGoldenResourceId='" + myGoldenResourceId + '\'' +
			", myDuplicateGoldenResourceIds=" + myDuplicateGoldenResourceIds +
			'}';
	}
}
