package ca.uhn.fhir.mdm.api;

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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MdmLinkJson implements IModelJson {

	@JsonProperty("goldenResourceId")
	private String myGoldenResourceId;

	@JsonProperty("sourceId")
	private String mySourceId;

	@JsonProperty("matchResult")
	private MdmMatchResultEnum myMatchResult;

	@JsonProperty("linkSource")
	private MdmLinkSourceEnum myLinkSource;

	@JsonProperty("created")
	private Date myCreated;

	@JsonProperty("updated")
	private Date myUpdated;

	@JsonProperty("version")
	private String myVersion;

	/**
	 * This link was created as a result of an eid match
	 **/
	@JsonProperty("eidMatch")
	private Boolean myEidMatch;

	/**
	 * This link created a new golden resource
	 **/
	@JsonProperty("linkCreatedNewGoldenResource")
	private Boolean myLinkCreatedNewResource;

	@JsonProperty("vector")
	private Long myVector;

	@JsonProperty("score")
	private Double myScore;

	@JsonProperty("ruleCount")
	private Long myRuleCount;

	public String getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public MdmLinkJson setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
		return this;
	}

	public String getSourceId() {
		return mySourceId;
	}

	public MdmLinkJson setSourceId(String theSourceId) {
		mySourceId = theSourceId;
		return this;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public MdmLinkJson setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public MdmLinkJson setLinkSource(MdmLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}

	public Date getCreated() {
		return myCreated;
	}

	public MdmLinkJson setCreated(Date theCreated) {
		myCreated = theCreated;
		return this;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public MdmLinkJson setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public MdmLinkJson setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public Boolean getEidMatch() {
		return myEidMatch;
	}

	public MdmLinkJson setEidMatch(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}

	public Boolean getLinkCreatedNewResource() {
		return myLinkCreatedNewResource;
	}

	public MdmLinkJson setLinkCreatedNewResource(Boolean theLinkCreatedNewResource) {
		myLinkCreatedNewResource = theLinkCreatedNewResource;
		return this;
	}

	public Long getVector() {
		return myVector;
	}

	public MdmLinkJson setVector(Long theVector) {
		myVector = theVector;
		return this;
	}

	public Double getScore() {
		return myScore;
	}

	public MdmLinkJson setScore(Double theScore) {
		myScore = theScore;
		return this;
	}

	public Long getRuleCount() {
		return myRuleCount;
	}

	public void setRuleCount(Long theRuleCount) {
		myRuleCount = theRuleCount;
	}

	@Override
	public String toString() {
		return "MdmLinkJson{" +
			"myGoldenResourceId='" + myGoldenResourceId + '\'' +
			", mySourceId='" + mySourceId + '\'' +
			", myMatchResult=" + myMatchResult +
			", myLinkSource=" + myLinkSource +
			", myCreated=" + myCreated +
			", myUpdated=" + myUpdated +
			", myVersion='" + myVersion + '\'' +
			", myEidMatch=" + myEidMatch +
			", myLinkCreatedNewResource=" + myLinkCreatedNewResource +
			", myVector=" + myVector +
			", myScore=" + myScore +
			", myRuleCount=" + myRuleCount +
			'}';
	}
}
