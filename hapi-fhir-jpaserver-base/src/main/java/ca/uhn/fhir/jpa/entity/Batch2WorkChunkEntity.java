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

import ca.uhn.fhir.batch2.model.StatusEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;
import static ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity.STATUS_MAX_LENGTH;
import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(name = "BT2_WORK_CHUNK", indexes = {
	@Index(name = "IDX_BT2WC_II_SEQ", columnList = "INSTANCE_ID,SEQ")
})
public class Batch2WorkChunkEntity implements Serializable {

	public static final int ERROR_MSG_MAX_LENGTH = 500;
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
	@Column(name = "RECORDS_PROCESSED", nullable = true)
	private Integer myRecordsProcessed;
	@Column(name = "DEFINITION_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myJobDefinitionId;
	@Column(name = "DEFINITION_VER", length = ID_MAX_LENGTH, nullable = false)
	private int myJobDefinitionVersion;
	@Column(name = "TGT_STEP_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myTargetStepId;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CHUNK_DATA", nullable = true, length = Integer.MAX_VALUE - 1)
	private String mySerializedData;
	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum myStatus;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INSTANCE_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_BT2WC_INSTANCE"))
	private Batch2JobInstanceEntity myInstance;
	@Column(name = "INSTANCE_ID", length = ID_MAX_LENGTH, nullable = false)
	private String myInstanceId;
	@Column(name = "ERROR_MSG", length = ERROR_MSG_MAX_LENGTH, nullable = true)
	private String myErrorMessage;
	@Column(name = "ERROR_COUNT", nullable = false)
	private int myErrorCount;

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
		return mySerializedData;
	}

	public void setSerializedData(String theSerializedData) {
		mySerializedData = theSerializedData;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
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
			.append("recordsProcessed", myRecordsProcessed)
			.append("targetStepId", myTargetStepId)
			.append("serializedData", mySerializedData)
			.append("status", myStatus)
			.append("errorMessage", myErrorMessage)
			.toString();
	}
}
