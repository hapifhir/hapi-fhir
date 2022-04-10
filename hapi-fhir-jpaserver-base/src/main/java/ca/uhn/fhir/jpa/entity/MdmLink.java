package ca.uhn.fhir.jpa.entity;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.model.entity.BasePartitionable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
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
	// TODO GGG DROP this index, and instead use the below one
	@UniqueConstraint(name = "IDX_EMPI_PERSON_TGT", columnNames = {"PERSON_PID", "TARGET_PID"}),
	// v---- this one
	//TODO GGG revisit adding this: @UniqueConstraint(name = "IDX_EMPI_GR_TGT", columnNames = {"GOLDEN_RESOURCE_PID", "TARGET_PID"}),
	//TODO GGG Should i make individual indices for PERSON/TARGET?
})
public class MdmLink extends BasePartitionable implements IMdmLink {
	public static final int VERSION_LENGTH = 16;
	private static final int MATCH_RESULT_LENGTH = 16;
	private static final int LINK_SOURCE_LENGTH = 16;
	public static final int SOURCE_TYPE_LENGTH = 40;

	@SequenceGenerator(name = "SEQ_EMPI_LINK_ID", sequenceName = "SEQ_EMPI_LINK_ID")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_EMPI_LINK_ID")
	@Id
	@Column(name = "PID")
	private Long myId;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "GOLDEN_RESOURCE_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_GOLDEN_RESOURCE"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myGoldenResource;

	@Column(name = "GOLDEN_RESOURCE_PID", nullable=false)
	private Long myGoldenResourcePid;

	@Deprecated
	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "PERSON_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_PERSON"), insertable=false, updatable=false, nullable=false)
	private ResourceTable myPerson;

	@Deprecated
	@Column(name = "PERSON_PID", nullable=false)
	private Long myPersonPid;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "TARGET_PID", referencedColumnName = "RES_ID", foreignKey = @ForeignKey(name = "FK_EMPI_LINK_TARGET"), insertable=false, updatable=false, nullable=false)
	private ResourceTable mySource;

	@Column(name = "TARGET_PID", updatable=false, nullable=false)
	private Long mySourcePid;

	@Column(name = "MATCH_RESULT", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private MdmMatchResultEnum myMatchResult;

	@Column(name = "LINK_SOURCE", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private MdmLinkSourceEnum myLinkSource;

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
	private Boolean myHadToCreateNewGoldenResource;

	@Column(name = "VECTOR")
	private Long myVector;

	@Column(name = "SCORE")
	private Double myScore;

	//TODO GGG GL-1340
	@Column(name = "RULE_COUNT")
	private Long myRuleCount;

	public MdmLink() {}

	public MdmLink(String theVersion) {
		myVersion = theVersion;
	}

	@Column(name = "TARGET_TYPE", nullable = true, length = SOURCE_TYPE_LENGTH)
	private String myMdmSourceType;

	public Long getId() {
		return myId;
	}

	public MdmLink setId(Long theId) {
		myId = theId;
		return this;
	}

	public ResourceTable getGoldenResource() {
		return myGoldenResource;
	}

	public MdmLink setGoldenResource(ResourceTable theGoldenResource) {
		myGoldenResource = theGoldenResource;
		myGoldenResourcePid = theGoldenResource.getId();

		myPerson = theGoldenResource;
		myPersonPid = theGoldenResource.getId();

		return this;
	}

	public Long getGoldenResourcePid() {
		return myGoldenResourcePid;
	}

	/**
	 * @deprecated  Use {@link #setGoldenResourcePid(Long)} instead
	 */
	@Deprecated
	public MdmLink setPersonPid(Long thePersonPid) {
		myPersonPid = thePersonPid;
		return this;
	}

	public MdmLink setGoldenResourcePid(Long theGoldenResourcePid) {
		setPersonPid(theGoldenResourcePid);

		myGoldenResourcePid = theGoldenResourcePid;
		return this;
	}

	public ResourceTable getSource() {
		return mySource;
	}

	public MdmLink setSource(ResourceTable theSource) {
		mySource = theSource;
		mySourcePid = theSource.getId();
		return this;
	}

	public Long getSourcePid() {
		return mySourcePid;
	}

	public MdmLink setSourcePid(Long theSourcePid) {
		mySourcePid = theSourcePid;
		return this;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public MdmLink setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public boolean isNoMatch() {
		return myMatchResult == MdmMatchResultEnum.NO_MATCH;
	}

	public boolean isMatch() {
		return myMatchResult == MdmMatchResultEnum.MATCH;
	}

	public boolean isPossibleMatch() {
		return myMatchResult == MdmMatchResultEnum.POSSIBLE_MATCH;
	}

	public boolean isRedirect() {
		return myMatchResult == MdmMatchResultEnum.REDIRECT;
	}

	public boolean isPossibleDuplicate() {
		return myMatchResult == MdmMatchResultEnum.POSSIBLE_DUPLICATE;
	}

	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public MdmLink setLinkSource(MdmLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}

	public boolean isAuto() {
		return myLinkSource == MdmLinkSourceEnum.AUTO;
	}

	public boolean isManual() {
		return myLinkSource == MdmLinkSourceEnum.MANUAL;
	}

	public Date getCreated() {
		return myCreated;
	}

	public MdmLink setCreated(Date theCreated) {
		myCreated = theCreated;
		return this;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public MdmLink setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public MdmLink setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public Long getVector() {
		return myVector;
	}

	public MdmLink setVector(Long theVector) {
		myVector = theVector;
		return this;
	}

	public Double getScore() {
		return myScore;
	}

	public MdmLink setScore(Double theScore) {
		myScore = theScore;
		return this;
	}

	public Boolean getEidMatch() {
		return myEidMatch;
	}

	/**
	 * Note that this method can not be called <code>getEidMatch</code> or
	 * <code>isEidMatch</code> because Hibernate Search complains about having
	 * 2 accessors for this property
	 */
	public boolean isEidMatchPresent() {
		return myEidMatch != null && myEidMatch;
	}

	public MdmLink setEidMatch(Boolean theEidMatch) {
		myEidMatch = theEidMatch;
		return this;
	}

	public boolean getHadToCreateNewGoldenResource() {
		return myHadToCreateNewGoldenResource != null && myHadToCreateNewGoldenResource;
	}

	public MdmLink setHadToCreateNewGoldenResource(Boolean theHadToCreateNewResource) {
		myHadToCreateNewGoldenResource = theHadToCreateNewResource;
		return this;
	}

	public MdmLink setMdmSourceType(String mdmSourceType) {
		myMdmSourceType = mdmSourceType;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myId", myId)
			.append("myGoldenResource", myGoldenResourcePid)
			.append("mySourcePid", mySourcePid)
			.append("myMdmSourceType", myMdmSourceType)
			.append("myMatchResult", myMatchResult)
			.append("myLinkSource", myLinkSource)
			.append("myEidMatch", myEidMatch)
			.append("myHadToCreateNewResource", myHadToCreateNewGoldenResource)
			.append("myScore", myScore)
			.append("myRuleCount", myRuleCount)
			.append("myPartitionId", getPartitionId())
			.toString();
	}

	public String getMdmSourceType() {
		return myMdmSourceType;
	}

	public Long getRuleCount() {
		return myRuleCount;
	}

	public void setRuleCount(Long theRuleCount) {
		myRuleCount = theRuleCount;
	}

}
