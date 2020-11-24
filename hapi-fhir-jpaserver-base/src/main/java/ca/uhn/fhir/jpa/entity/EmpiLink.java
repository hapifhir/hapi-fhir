package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(name = "MPI_LINK", uniqueConstraints = {
	@UniqueConstraint(name = "IDX_EMPI_PERSON_TGT", columnNames = {"PERSON_PID", "TARGET_PID"}),
})
public class EmpiLink {
	public static final int VERSION_LENGTH = 16;
	private static final int MATCH_RESULT_LENGTH = 16;
	private static final int LINK_SOURCE_LENGTH = 16;
	public static final int TARGET_TYPE_LENGTH = 40;


	@SequenceGenerator(name = "SEQ_EMPI_LINK_ID", sequenceName = "SEQ_EMPI_LINK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPI_LINK_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "PERSON_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_PERSON"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myPerson;

	@Column(name = "PERSON_PID", nullable=false)
	private Long myPersonPid;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "TARGET_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_TARGET"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myTarget;

	@Column(name = "TARGET_PID", updatable=false, nullable=false)
	private Long myTargetPid;

	@Column(name = "MATCH_RESULT", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private EmpiMatchResultEnum myMatchResult;

	@Column(name = "LINK_SOURCE", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private EmpiLinkSourceEnum myLinkSource;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED", nullable = false)
	private Date myCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED", nullable = false)
	private Date myUpdated;

	@Column(name = "VERSION", nullable = false, length = VERSION_LENGTH)
	private String myVersion;

	/** This link was created as a result of an eid match **/
	@Column(name = "EID_MATCH")
	private Boolean myEidMatch;

	/** This link created a new person **/
	@Column(name = "NEW_PERSON")
	private Boolean myNewPerson;

	@Column(name = "VECTOR")
	private Long myVector;

	@Column(name = "SCORE")
	private Double myScore;

	public EmpiLink() {}

	public EmpiLink(String theVersion) {
		myVersion = theVersion;
	}

	@Column(name = "TARGET_TYPE", nullable = true, length = TARGET_TYPE_LENGTH)
	private String myEmpiTargetType;

	public Long getId() {
		return myId;
	}

	public EmpiLink setId(Long theId) {
		myId = theId;
		return this;
	}

	public ResourceTable getPerson() {
		return myPerson;
	}

	public EmpiLink setPerson(ResourceTable thePerson) {
		myPerson = thePerson;
		myPersonPid = thePerson.getId();
		return this;
	}

	public Long getPersonPid() {
		return myPersonPid;
	}

	public EmpiLink setPersonPid(Long thePersonPid) {
		myPersonPid = thePersonPid;
		return this;
	}

	public ResourceTable getTarget() {
		return myTarget;
	}

	public EmpiLink setTarget(ResourceTable theTarget) {
		myTarget = theTarget;
		myTargetPid = theTarget.getId();
		return this;
	}

	public Long getTargetPid() {
		return myTargetPid;
	}

	public EmpiLink setTargetPid(Long theTargetPid) {
		myTargetPid = theTargetPid;
		return this;
	}

	public EmpiMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public EmpiLink setMatchResult(EmpiMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public boolean isNoMatch() {
		return myMatchResult == EmpiMatchResultEnum.NO_MATCH;
	}

	public boolean isMatch() {
		return myMatchResult == EmpiMatchResultEnum.MATCH;
	}

	public boolean isPossibleMatch() {
		return myMatchResult == EmpiMatchResultEnum.POSSIBLE_MATCH;
	}

	public boolean isRedirect() {
		return myMatchResult == EmpiMatchResultEnum.REDIRECT;
	}

	public boolean isPossibleDuplicate() {
		return myMatchResult == EmpiMatchResultEnum.POSSIBLE_DUPLICATE;
	}

	public EmpiLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public EmpiLink setLinkSource(EmpiLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}

	public boolean isAuto() {
		return myLinkSource == EmpiLinkSourceEnum.AUTO;
	}

	public boolean isManual() {
		return myLinkSource == EmpiLinkSourceEnum.MANUAL;
	}

	public Date getCreated() {
		return myCreated;
	}

	public EmpiLink setCreated(Date theCreated) {
		myCreated = theCreated;
		return this;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public EmpiLink setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public EmpiLink setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public Long getVector() {
		return myVector;
	}

	public EmpiLink setVector(Long theVector) {
		myVector = theVector;
		return this;
	}

	public Double getScore() {
		return myScore;
	}

	public EmpiLink setScore(Double theScore) {
		myScore = theScore;
		return this;
	}

	public boolean isEidMatch() {
		return myEidMatch != null && myEidMatch;
	}

	public EmpiLink setEidMatch(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}

	public boolean isNewPerson() {
		return myNewPerson != null && myNewPerson;
	}

	public EmpiLink setNewPerson(Boolean theNewPerson) {
		myNewPerson = theNewPerson;
		return this;
	}

	public EmpiLink setEmpiTargetType(String theEmpiTargetType) {
		myEmpiTargetType = theEmpiTargetType;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myId", myId)
			.append("myPersonPid", myPersonPid)
			.append("myTargetPid", myTargetPid)
			.append("myEmpiTargetType", myEmpiTargetType)
			.append("myMatchResult", myMatchResult)
			.append("myLinkSource", myLinkSource)
			.append("myEidMatch", myEidMatch)
			.append("myNewPerson", myNewPerson)
			.append("myScore", myScore)
			.toString();
	}

	public String getEmpiTargetType() {
		return myEmpiTargetType;
	}
}
