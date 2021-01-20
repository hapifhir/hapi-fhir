package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

public class EmpiLinkJson implements IModelJson {
	@JsonProperty("personId")
	private String myPersonId;

	@JsonProperty("targetId")
	private String myTargetId;

	@JsonProperty("matchResult")
	private EmpiMatchResultEnum myMatchResult;

	@JsonProperty("linkSource")
	private EmpiLinkSourceEnum myLinkSource;

	@JsonProperty("created")
	private Date myCreated;

	@JsonProperty("updated")
	private Date myUpdated;

	@JsonProperty("version")
	private String myVersion;

	/** This link was created as a result of an eid match **/
	@JsonProperty("eidMatch")
	private Boolean myEidMatch;

	/** This link created a new person **/
	@JsonProperty("newPerson")
	private Boolean myNewPerson;

	@JsonProperty("vector")
	private Long myVector;

	@JsonProperty("score")
	private Double myScore;

	public String getPersonId() {
		return myPersonId;
	}

	public EmpiLinkJson setPersonId(String thePersonId) {
		myPersonId = thePersonId;
		return this;
	}

	public String getTargetId() {
		return myTargetId;
	}

	public EmpiLinkJson setTargetId(String theTargetId) {
		myTargetId = theTargetId;
		return this;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public EmpiLinkJson setMatchResult(EmpiMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public EmpiLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public EmpiLinkJson setLinkSource(EmpiLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}

	public Date getCreated() {
		return myCreated;
	}

	public EmpiLinkJson setCreated(Date theCreated) {
		myCreated = theCreated;
		return this;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public EmpiLinkJson setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public EmpiLinkJson setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public Boolean getEidMatch() {
		return myEidMatch;
	}

	public EmpiLinkJson setEidMatch(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}

	public Boolean getNewPerson() {
		return myNewPerson;
	}

	public EmpiLinkJson setNewPerson(Boolean theNewPerson) {
		myNewPerson = theNewPerson;
		return this;
	}

	public Long getVector() {
		return myVector;
	}

	public EmpiLinkJson setVector(Long theVector) {
		myVector = theVector;
		return this;
	}

	public Double getScore() {
		return myScore;
	}

	public EmpiLinkJson setScore(Double theScore) {
		myScore = theScore;
		return this;
	}
}
