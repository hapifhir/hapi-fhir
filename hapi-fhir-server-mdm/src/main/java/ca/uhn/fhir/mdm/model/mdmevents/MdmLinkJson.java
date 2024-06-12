/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.model.mdmevents;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

public class MdmLinkJson implements IModelJson {

	/**
	 * Golden resource FhirId
	 */
	@JsonProperty("goldenResourceId")
	private String myGoldenResourceId;

	/**
	 * Source resource FhirId
	 */
	@JsonProperty("sourceId")
	private String mySourceId;

	/**
	 * Golden resource PID
	 */
	@JsonIgnore
	private IResourcePersistentId<?> myGoldenPid;

	/**
	 * Source PID
	 */
	@JsonIgnore
	private IResourcePersistentId<?> mySourcePid;

	/**
	 * Kind of link (MATCH, etc)
	 */
	@JsonProperty("matchResult")
	private MdmMatchResultEnum myMatchResult;

	/**
	 * How the link was constructed (AUTO - by the system, MANUAL - by a user)
	 */
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

	public IResourcePersistentId<?> getGoldenPid() {
		return myGoldenPid;
	}

	public void setGoldenPid(IResourcePersistentId<?> theGoldenPid) {
		myGoldenPid = theGoldenPid;
	}

	public IResourcePersistentId<?> getSourcePid() {
		return mySourcePid;
	}

	public void setSourcePid(IResourcePersistentId<?> theSourcePid) {
		mySourcePid = theSourcePid;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final MdmLinkJson that = (MdmLinkJson) theO;
		return Objects.equals(myGoldenResourceId, that.myGoldenResourceId)
				&& Objects.equals(mySourceId, that.mySourceId)
				&& mySourcePid.equals(that.mySourcePid)
				&& myGoldenPid.equals(that.myGoldenPid)
				&& myMatchResult == that.myMatchResult
				&& myLinkSource == that.myLinkSource
				&& Objects.equals(myCreated, that.myCreated)
				&& Objects.equals(myUpdated, that.myUpdated)
				&& Objects.equals(myVersion, that.myVersion)
				&& Objects.equals(myEidMatch, that.myEidMatch)
				&& Objects.equals(myLinkCreatedNewResource, that.myLinkCreatedNewResource)
				&& Objects.equals(myVector, that.myVector)
				&& Objects.equals(myScore, that.myScore)
				&& Objects.equals(myRuleCount, that.myRuleCount);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				myGoldenResourceId,
				mySourceId,
				mySourcePid,
				myGoldenPid,
				myMatchResult,
				myLinkSource,
				myCreated,
				myUpdated,
				myVersion,
				myEidMatch,
				myLinkCreatedNewResource,
				myVector,
				myScore,
				myRuleCount);
	}

	@Override
	public String toString() {
		return "MdmLinkJson{"
				+ "myGoldenResourceId='" + myGoldenResourceId + '\''
				+ ", myGoldenPid='" + myGoldenPid + '\''
				+ ", mySourceId='" + mySourceId + '\''
				+ ", mySourcePid='" + mySourcePid + '\''
				+ ", myMatchResult=" + myMatchResult
				+ ", myLinkSource=" + myLinkSource
				+ ", myCreated=" + myCreated
				+ ", myUpdated=" + myUpdated
				+ ", myVersion='" + myVersion + '\''
				+ ", myEidMatch=" + myEidMatch
				+ ", myLinkCreatedNewResource=" + myLinkCreatedNewResource
				+ ", myVector=" + myVector
				+ ", myScore=" + myScore
				+ ", myRuleCount=" + myRuleCount
				+ '}';
	}
}
