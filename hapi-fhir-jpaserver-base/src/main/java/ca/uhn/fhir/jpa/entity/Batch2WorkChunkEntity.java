/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Length;

import java.io.Serializable;
import java.util.Date;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;
import static ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity.STATUS_MAX_LENGTH;
import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(
		name = "BT2_WORK_CHUNK",
		indexes = {
			@Index(name = "IDX_BT2WC_II_SEQ", columnList = "INSTANCE_ID,SEQ"),
			@Index(name = "IDX_BT2WC_II_SI_S_SEQ_ID", columnList = "INSTANCE_ID,TGT_STEP_ID,STAT,SEQ,ID")
		})
public class Batch2WorkChunkEntity implements Serializable {

	public static final int ERROR_MSG_MAX_LENGTH = 500;
	public static final int WARNING_MSG_MAX_LENGTH = 4000;
	private static final long serialVersionUID = -6202771941965780558L;

	@Id
	@Column(name = "ID", length = ID_MAX_LENGTH)
	private String myId;

	@Column(name = "SEQ", nullable = false)
	private int mySequence;

	@Column(name = "CREATE_TIME", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myCreateTime;

	@Column(name = "START_TIME", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myStartTime;

	@Column(name = "END_TIME", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myEndTime;

	@Version
	@Column(name = "UPDATE_TIME", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myUpdateTime;

	@Column(name = "RECORDS_PROCESSED", nullable = true)
	private Integer myRecordsProcessed;

	@Column(name = "DEFINITION_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myJobDefinitionId;

	@Column(name = "DEFINITION_VER", length = ID_MAX_LENGTH, nullable = false)
	private int myJobDefinitionVersion;

	@Column(name = "TGT_STEP_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myTargetStepId;

	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CHUNK_DATA", nullable = true, length = Integer.MAX_VALUE - 1)
	private String mySerializedData;

	@Column(name = "CHUNK_DATA_VC", nullable = true, length = Length.LONG32)
	private String mySerializedDataVc;

	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private WorkChunkStatusEnum myStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "INSTANCE_ID",
			insertable = false,
			updatable = false,
			foreignKey = @ForeignKey(name = "FK_BT2WC_INSTANCE"))
	private Batch2JobInstanceEntity myInstance;

	@Column(name = "INSTANCE_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myInstanceId;

	@Column(name = "ERROR_MSG", length = ERROR_MSG_MAX_LENGTH, nullable = true)
	private String myErrorMessage;

	@Column(name = "ERROR_COUNT", nullable = false)
	private int myErrorCount;

	@Column(name = "WARNING_MSG", length = WARNING_MSG_MAX_LENGTH, nullable = true)
	private String myWarningMessage;

	/**
	 * The next time the work chunk can attempt to rerun its work step.
	 */
	@Column(name = "NEXT_POLL_TIME", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date myNextPollTime;

	/**
	 * The number of times the work chunk has had its state set back to POLL_WAITING.
	 * <p>
	 * TODO: Note that this column was added in 7.2.0, so it is nullable in order to
	 * account for existing rows that were added before the column was added. In
	 * the future we should make this non-null.
	 */
	@Column(name = "POLL_ATTEMPTS", nullable = true)
	private Integer myPollAttempts;

	/**
	 * Default constructor for Hibernate.
	 */
	public Batch2WorkChunkEntity() {
		myPollAttempts = 0;
	}

	/**
	 * Projection constructor for no-data path.
	 */
	public Batch2WorkChunkEntity(
			String theId,
			int theSequence,
			String theJobDefinitionId,
			int theJobDefinitionVersion,
			String theInstanceId,
			String theTargetStepId,
			WorkChunkStatusEnum theStatus,
			Date theCreateTime,
			Date theStartTime,
			Date theUpdateTime,
			Date theEndTime,
			String theErrorMessage,
			int theErrorCount,
			Integer theRecordsProcessed,
			String theWarningMessage,
			Date theNextPollTime,
			Integer thePollAttempts) {
		myId = theId;
		mySequence = theSequence;
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myInstanceId = theInstanceId;
		myTargetStepId = theTargetStepId;
		myStatus = theStatus;
		myCreateTime = theCreateTime;
		myStartTime = theStartTime;
		myUpdateTime = theUpdateTime;
		myEndTime = theEndTime;
		myErrorMessage = theErrorMessage;
		myErrorCount = theErrorCount;
		myRecordsProcessed = theRecordsProcessed;
		myWarningMessage = theWarningMessage;
		myNextPollTime = theNextPollTime;
		myPollAttempts = thePollAttempts != null ? thePollAttempts : 0;
	}

	public static Batch2WorkChunkEntity fromWorkChunk(WorkChunk theWorkChunk) {
		Batch2WorkChunkEntity entity = new Batch2WorkChunkEntity(
				theWorkChunk.getId(),
				theWorkChunk.getSequence(),
				theWorkChunk.getJobDefinitionId(),
				theWorkChunk.getJobDefinitionVersion(),
				theWorkChunk.getInstanceId(),
				theWorkChunk.getTargetStepId(),
				theWorkChunk.getStatus(),
				theWorkChunk.getCreateTime(),
				theWorkChunk.getStartTime(),
				theWorkChunk.getUpdateTime(),
				theWorkChunk.getEndTime(),
				theWorkChunk.getErrorMessage(),
				theWorkChunk.getErrorCount(),
				theWorkChunk.getRecordsProcessed(),
				theWorkChunk.getWarningMessage(),
				theWorkChunk.getNextPollTime(),
				theWorkChunk.getPollAttempts());
		entity.setSerializedData(theWorkChunk.getData());

		return entity;
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public void setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
	}

	public String getErrorMessage() {
		return myErrorMessage;
	}

	public void setErrorMessage(String theErrorMessage) {
		myErrorMessage = left(theErrorMessage, ERROR_MSG_MAX_LENGTH);
	}

	public String getWarningMessage() {
		return myWarningMessage;
	}

	public void setWarningMessage(String theWarningMessage) {
		myWarningMessage = theWarningMessage;
	}

	public int getSequence() {
		return mySequence;
	}

	public void setSequence(int theSequence) {
		mySequence = theSequence;
	}

	public Date getCreateTime() {
		return myCreateTime;
	}

	public void setCreateTime(Date theCreateTime) {
		myCreateTime = theCreateTime;
	}

	public Date getStartTime() {
		return myStartTime;
	}

	public void setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
	}

	public Date getEndTime() {
		return myEndTime;
	}

	public void setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
	}

	public Date getUpdateTime() {
		return myUpdateTime;
	}

	public Integer getRecordsProcessed() {
		return myRecordsProcessed;
	}

	public void setRecordsProcessed(Integer theRecordsProcessed) {
		myRecordsProcessed = theRecordsProcessed;
	}

	public Batch2JobInstanceEntity getInstance() {
		return myInstance;
	}

	public void setInstance(Batch2JobInstanceEntity theInstance) {
		myInstance = theInstance;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public void setJobDefinitionId(String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public void setJobDefinitionVersion(int theJobDefinitionVersion) {
		myJobDefinitionVersion = theJobDefinitionVersion;
	}

	public String getTargetStepId() {
		return myTargetStepId;
	}

	public void setTargetStepId(String theTargetStepId) {
		myTargetStepId = theTargetStepId;
	}

	public String getSerializedData() {
		return mySerializedDataVc != null ? mySerializedDataVc : mySerializedData;
	}

	public void setSerializedData(String theSerializedData) {
		mySerializedData = null;
		mySerializedDataVc = theSerializedData;
	}

	public WorkChunkStatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(WorkChunkStatusEnum theStatus) {
		myStatus = theStatus;
	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public Date getNextPollTime() {
		return myNextPollTime;
	}

	public void setNextPollTime(Date theNextPollTime) {
		myNextPollTime = theNextPollTime;
	}

	public Integer getPollAttempts() {
		if (myPollAttempts == null) {
			return 0;
		}
		return myPollAttempts;
	}

	public void setPollAttempts(int thePollAttempts) {
		myPollAttempts = thePollAttempts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("instanceId", myInstanceId)
				.append("sequence", mySequence)
				.append("errorCount", myErrorCount)
				.append("jobDefinitionId", myJobDefinitionId)
				.append("jobDefinitionVersion", myJobDefinitionVersion)
				.append("createTime", myCreateTime)
				.append("startTime", myStartTime)
				.append("endTime", myEndTime)
				.append("updateTime", myUpdateTime)
				.append("recordsProcessed", myRecordsProcessed)
				.append("targetStepId", myTargetStepId)
				.append("serializedData", getSerializedData())
				.append("status", myStatus)
				.append("errorMessage", myErrorMessage)
				.append("warningMessage", myWarningMessage)
				.append("nextPollTime", myNextPollTime)
				.append("pollAttempts", myPollAttempts)
				.toString();
	}
}
