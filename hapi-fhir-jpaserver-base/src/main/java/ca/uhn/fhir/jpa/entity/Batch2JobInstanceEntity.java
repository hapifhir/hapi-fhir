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

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
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
import static ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity.ERROR_MSG_MAX_LENGTH;
import static ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity.WARNING_MSG_MAX_LENGTH;
import static org.apache.commons.lang3.StringUtils.left;

@Entity
@Table(
		name = "BT2_JOB_INSTANCE",
		indexes = {@Index(name = "IDX_BT2JI_CT", columnList = "CREATE_TIME")})
public class Batch2JobInstanceEntity implements Serializable {

	public static final int STATUS_MAX_LENGTH = 20;
	public static final int TIME_REMAINING_LENGTH = 100;
	public static final int PARAMS_JSON_MAX_LENGTH = 2000;
	private static final long serialVersionUID = 8187134261799095422L;
	public static final int INITIATING_USER_NAME_MAX_LENGTH = 200;
	public static final int INITIATING_CLIENT_ID_MAX_LENGTH = 200;

	@Id
	@Column(name = "ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myId;

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

	@Column(name = "DEFINITION_ID", length = JobDefinition.ID_MAX_LENGTH, nullable = false)
	private String myDefinitionId;

	@Column(name = "DEFINITION_VER", nullable = false)
	private int myDefinitionVersion;

	@Column(name = "STAT", length = STATUS_MAX_LENGTH, nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusEnum myStatus;

	@Column(name = "JOB_CANCELLED", nullable = false)
	private boolean myCancelled;

	@Column(name = "FAST_TRACKING", nullable = true)
	private Boolean myFastTracking;

	// TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Column(name = "PARAMS_JSON", length = PARAMS_JSON_MAX_LENGTH, nullable = true)
	private String myParamsJson;

	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Column(name = "PARAMS_JSON_LOB", nullable = true)
	private String myParamsJsonLob;

	@Column(name = "PARAMS_JSON_VC", nullable = true, length = Length.LONG32)
	private String myParamsJsonVc;

	@Column(name = "CMB_RECS_PROCESSED", nullable = true)
	private Integer myCombinedRecordsProcessed;

	@Column(name = "CMB_RECS_PER_SEC", nullable = true)
	private Double myCombinedRecordsProcessedPerSecond;

	@Column(name = "TOT_ELAPSED_MILLIS", nullable = true)
	private Integer myTotalElapsedMillis;

	@Column(name = "WORK_CHUNKS_PURGED", nullable = false)
	private boolean myWorkChunksPurged;

	@Column(name = "PROGRESS_PCT", nullable = false)
	private double myProgress;

	@Column(name = "ERROR_MSG", length = ERROR_MSG_MAX_LENGTH, nullable = true)
	private String myErrorMessage;

	@Column(name = "ERROR_COUNT", nullable = false)
	private int myErrorCount;

	@Column(name = "EST_REMAINING", length = TIME_REMAINING_LENGTH, nullable = true)
	private String myEstimatedTimeRemaining;

	@Column(name = "CUR_GATED_STEP_ID", length = ID_MAX_LENGTH, nullable = true)
	private String myCurrentGatedStepId;

	@Column(name = "WARNING_MSG", length = WARNING_MSG_MAX_LENGTH, nullable = true)
	private String myWarningMessages;

	@Column(name = "USER_NAME", length = INITIATING_USER_NAME_MAX_LENGTH, nullable = true)
	private String myTriggeringUsername;

	@Column(name = "CLIENT_ID", length = INITIATING_CLIENT_ID_MAX_LENGTH, nullable = true)
	private String myTriggeringClientId;

	/**
	 * Any output from the job can be held in this column
	 * Even serialized json
	 */
	@Lob // TODO: VC column added in 7.2.0 - Remove non-VC column later
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "REPORT", nullable = true, length = Integer.MAX_VALUE - 1)
	private String myReport;

	@Column(name = "REPORT_VC", nullable = true, length = Length.LONG32)
	private String myReportVc;

	public String getCurrentGatedStepId() {
		return myCurrentGatedStepId;
	}

	public void setCurrentGatedStepId(String theCurrentGatedStepId) {
		myCurrentGatedStepId = theCurrentGatedStepId;
	}

	public boolean isCancelled() {
		return myCancelled;
	}

	public void setCancelled(boolean theCancelled) {
		myCancelled = theCancelled;
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public void setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
	}

	public Integer getTotalElapsedMillis() {
		return myTotalElapsedMillis;
	}

	public void setTotalElapsedMillis(Integer theTotalElapsedMillis) {
		myTotalElapsedMillis = theTotalElapsedMillis;
	}

	public Integer getCombinedRecordsProcessed() {
		return myCombinedRecordsProcessed;
	}

	public void setCombinedRecordsProcessed(Integer theCombinedRecordsProcessed) {
		myCombinedRecordsProcessed = theCombinedRecordsProcessed;
	}

	public Double getCombinedRecordsProcessedPerSecond() {
		return myCombinedRecordsProcessedPerSecond;
	}

	public void setCombinedRecordsProcessedPerSecond(Double theCombinedRecordsProcessedPerSecond) {
		myCombinedRecordsProcessedPerSecond = theCombinedRecordsProcessedPerSecond;
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

	public void setUpdateTime(Date theTime) {
		myUpdateTime = theTime;
	}

	public Date getUpdateTime() {
		return myUpdateTime;
	}

	public String getId() {
		return myId;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public String getDefinitionId() {
		return myDefinitionId;
	}

	public void setDefinitionId(String theDefinitionId) {
		myDefinitionId = theDefinitionId;
	}

	public int getDefinitionVersion() {
		return myDefinitionVersion;
	}

	public void setDefinitionVersion(int theDefinitionVersion) {
		myDefinitionVersion = theDefinitionVersion;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
	}

	public String getParams() {
		if (myParamsJsonVc != null) {
			return myParamsJsonVc;
		}
		if (myParamsJsonLob != null) {
			return myParamsJsonLob;
		}
		return myParamsJson;
	}

	public void setParams(String theParams) {
		myParamsJsonVc = theParams;
		myParamsJsonLob = null;
		myParamsJson = null;
	}

	public boolean getWorkChunksPurged() {
		return myWorkChunksPurged;
	}

	public void setWorkChunksPurged(boolean theWorkChunksPurged) {
		myWorkChunksPurged = theWorkChunksPurged;
	}

	public double getProgress() {
		return myProgress;
	}

	public void setProgress(double theProgress) {
		myProgress = theProgress;
	}

	public String getErrorMessage() {
		return myErrorMessage;
	}

	public void setErrorMessage(String theErrorMessage) {
		myErrorMessage = left(theErrorMessage, ERROR_MSG_MAX_LENGTH);
	}

	public String getEstimatedTimeRemaining() {
		return myEstimatedTimeRemaining;
	}

	public void setEstimatedTimeRemaining(String theEstimatedTimeRemaining) {
		myEstimatedTimeRemaining = left(theEstimatedTimeRemaining, TIME_REMAINING_LENGTH);
	}

	public String getReport() {
		return myReportVc != null ? myReportVc : myReport;
	}

	public void setReport(String theReport) {
		myReportVc = theReport;
		myReport = null;
	}

	public String getWarningMessages() {
		return myWarningMessages;
	}

	public void setWarningMessages(String theWarningMessages) {
		myWarningMessages = theWarningMessages;
	}

	public String getTriggeringUsername() {
		return myTriggeringUsername;
	}

	public Batch2JobInstanceEntity setTriggeringUsername(String theTriggeringUsername) {
		myTriggeringUsername = theTriggeringUsername;
		return this;
	}

	public String getTriggeringClientId() {
		return myTriggeringClientId;
	}

	public Batch2JobInstanceEntity setTriggeringClientId(String theTriggeringClientId) {
		myTriggeringClientId = theTriggeringClientId;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("id", myId)
				.append("definitionId", myDefinitionId)
				.append("definitionVersion", myDefinitionVersion)
				.append("errorCount", myErrorCount)
				.append("createTime", myCreateTime)
				.append("startTime", myStartTime)
				.append("endTime", myEndTime)
				.append("updateTime", myUpdateTime)
				.append("status", myStatus)
				.append("cancelled", myCancelled)
				.append("combinedRecordsProcessed", myCombinedRecordsProcessed)
				.append("combinedRecordsProcessedPerSecond", myCombinedRecordsProcessedPerSecond)
				.append("totalElapsedMillis", myTotalElapsedMillis)
				.append("workChunksPurged", myWorkChunksPurged)
				.append("progress", myProgress)
				.append("errorMessage", myErrorMessage)
				.append("estimatedTimeRemaining", myEstimatedTimeRemaining)
				.append("report", getReport())
				.append("warningMessages", myWarningMessages)
				.append("initiatingUsername", myTriggeringUsername)
				.append("initiatingclientId", myTriggeringClientId)
				.toString();
	}

	/**
	 * @return true if every step of the job has produced exactly 1 chunk.
	 */
	public boolean isFastTracking() {
		if (myFastTracking == null) {
			myFastTracking = false;
		}
		return myFastTracking;
	}

	public void setFastTracking(boolean theFastTracking) {
		myFastTracking = theFastTracking;
	}
}
