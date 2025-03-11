package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.Date;

public class TestMdmLink implements IMdmLink<StringResourceId> {

	private StringResourceId myId;

	private StringResourceId myGoldenResourceId;

	private StringResourceId mySourceResourceId;

	private MdmMatchResultEnum myMatchResultEnum;

	private MdmLinkSourceEnum myLinkSourceEnum;

	@Override
	public StringResourceId getId() {
		return myId;
	}

	@Override
	public IMdmLink<StringResourceId> setId(StringResourceId theId) {
		myId = theId;
		return this;
	}

	@Override
	public StringResourceId getGoldenResourcePersistenceId() {
		return myGoldenResourceId;
	}

	@Override
	public IMdmLink<StringResourceId> setGoldenResourcePersistenceId(StringResourceId theGoldenResourcePid) {
		myGoldenResourceId = theGoldenResourcePid;
		return this;
	}

	@Override
	public StringResourceId getSourcePersistenceId() {
		return mySourceResourceId;
	}

	@Override
	public IMdmLink<StringResourceId> setSourcePersistenceId(StringResourceId theSourcePid) {
		mySourceResourceId = theSourcePid;
		return this;
	}

	@Override
	public MdmMatchResultEnum getMatchResult() {
		return myMatchResultEnum;
	}

	@Override
	public IMdmLink<StringResourceId> setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResultEnum = theMatchResult;
		return this;
	}

	@Override
	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSourceEnum;
	}

	@Override
	public IMdmLink<StringResourceId> setLinkSource(MdmLinkSourceEnum theLinkSource) {
		myLinkSourceEnum = theLinkSource;
		return this;
	}

	@Override
	public Date getCreated() {
		return new Date();
	}

	@Override
	public IMdmLink<StringResourceId> setCreated(Date theCreated) {
		// unneeded
		return this;
	}

	@Override
	public Date getUpdated() {
		return new Date();
	}

	@Override
	public IMdmLink<StringResourceId> setUpdated(Date theUpdated) {
		// unneeded
		return this;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setVersion(String theVersion) {
		// unneeded
		return this;
	}

	@Override
	public Boolean getEidMatch() {
		return null;
	}

	@Override
	public Boolean isEidMatchPresent() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setEidMatch(Boolean theEidMatch) {
		return this;
	}

	@Override
	public Boolean getHadToCreateNewGoldenResource() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setHadToCreateNewGoldenResource(Boolean theHadToCreateNewGoldenResource) {
		return this;
	}

	@Override
	public Long getVector() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setVector(Long theVector) {
		return this;
	}

	@Override
	public Double getScore() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setScore(Double theScore) {
		return this;
	}

	@Override
	public Long getRuleCount() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setRuleCount(Long theRuleCount) {
		return this;
	}

	@Override
	public String getMdmSourceType() {
		return null;
	}

	@Override
	public IMdmLink<StringResourceId> setMdmSourceType(String theMdmSourceType) {
		return this;
	}

	@Override
	public void setPartitionId(PartitionablePartitionId thePartitionablePartitionId) {

	}

	@Override
	public PartitionablePartitionId getPartitionId() {
		return null;
	}
}
