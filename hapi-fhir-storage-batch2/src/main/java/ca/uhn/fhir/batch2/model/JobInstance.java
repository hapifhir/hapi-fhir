/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.IJobInstance;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.Logs;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class JobInstance implements IModelJson, IJobInstance {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "parameters")
	private String myParameters;

	@JsonProperty(value = "jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty(value = "instanceId", access = JsonProperty.Access.READ_ONLY)
	private String myInstanceId;

	@JsonProperty(value = "status")
	private StatusEnum myStatus;

	@JsonProperty(value = "cancelled")
	private boolean myCancelled;

	/**
	 * True if every step of the job has produced exactly 1 chunk.
	 */
	@JsonProperty(value = "fastTracking")
	private boolean myFastTracking;

	// time when the job instance was actually first created/stored
	@JsonProperty(value = "createTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myCreateTime;

	// time when the current status was 'started'
	@JsonProperty(value = "startTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myStartTime;

	@JsonProperty(value = "endTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myEndTime;

	@JsonProperty(value = "updateTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myUpdateTime;

	@JsonProperty(value = "combinedRecordsProcessed")
	private Integer myCombinedRecordsProcessed;

	@JsonProperty(value = "combinedRecordsProcessedPerSecond")
	private Double myCombinedRecordsProcessedPerSecond;

	@JsonProperty(value = "totalElapsedMillis")
	private Integer myTotalElapsedMillis;

	@JsonProperty(value = "workChunksPurged", access = JsonProperty.Access.READ_ONLY)
	private boolean myWorkChunksPurged;

	@JsonProperty(value = "progress", access = JsonProperty.Access.READ_ONLY)
	private double myProgress;

	@JsonProperty(value = "currentGatedStepId", access = JsonProperty.Access.READ_ONLY)
	private String myCurrentGatedStepId;

	@JsonProperty(value = "errorMessage", access = JsonProperty.Access.READ_ONLY)
	private String myErrorMessage;

	@JsonProperty(value = "errorCount", access = JsonProperty.Access.READ_ONLY)
	private int myErrorCount;

	@JsonProperty(value = "estimatedCompletion", access = JsonProperty.Access.READ_ONLY)
	private String myEstimatedTimeRemaining;

	@JsonProperty(value = "report", access = JsonProperty.Access.READ_WRITE)
	private String myReport;

	@JsonProperty(value = "warningMessages", access = JsonProperty.Access.READ_ONLY)
	private String myWarningMessages;

	@JsonProperty(value = "triggeringUsername", access = JsonProperty.Access.READ_ONLY)
	private String myTriggeringUsername;

	@JsonProperty(value = "triggeringClientId", access = JsonProperty.Access.READ_ONLY)
	private String myTriggeringClientId;

	/**
	 * Constructor
	 */
	public JobInstance() {
		super();
	}

	/**
	 * Copy constructor
	 */
	public JobInstance(JobInstance theJobInstance) {
		setJobDefinitionId(theJobInstance.getJobDefinitionId());
		setParameters(theJobInstance.getParameters());
		setCancelled(theJobInstance.isCancelled());
		setFastTracking(theJobInstance.isFastTracking());
		setCombinedRecordsProcessed(theJobInstance.getCombinedRecordsProcessed());
		setCombinedRecordsProcessedPerSecond(theJobInstance.getCombinedRecordsProcessedPerSecond());
		setCreateTime(theJobInstance.getCreateTime());
		setEndTime(theJobInstance.getEndTime());
		setUpdateTime(theJobInstance.getUpdateTime());
		setErrorCount(theJobInstance.getErrorCount());
		setErrorMessage(theJobInstance.getErrorMessage());
		setEstimatedTimeRemaining(theJobInstance.getEstimatedTimeRemaining());
		setInstanceId(theJobInstance.getInstanceId());
		setJobDefinitionVersion(theJobInstance.getJobDefinitionVersion());
		setProgress(theJobInstance.getProgress());
		setStartTime(theJobInstance.getStartTime());
		setStatus(theJobInstance.getStatus());
		setTotalElapsedMillis(theJobInstance.getTotalElapsedMillis());
		setWorkChunksPurged(theJobInstance.isWorkChunksPurged());
		setCurrentGatedStepId(theJobInstance.getCurrentGatedStepId());
		setReport(theJobInstance.getReport());
		setWarningMessages(theJobInstance.getWarningMessages());
		setTriggeringUsername(theJobInstance.getTriggeringUsername());
		setTriggeringClientId(theJobInstance.getTriggeringClientId());
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public void setJobDefinitionId(String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public String getParameters() {
		return myParameters;
	}

	public void setParameters(String theParameters) {
		myParameters = theParameters;
	}

	public <T extends IModelJson> T getParameters(Class<T> theType) {
		if (myParameters == null) {
			return null;
		}
		return JsonUtil.deserialize(myParameters, theType);
	}

	public void setParameters(IModelJson theParameters) {
		myParameters = JsonUtil.serializeOrInvalidRequest(theParameters);
	}

	public void setUpdateTime(Date theUpdateTime) {
		myUpdateTime = theUpdateTime;
	}

	public Date getUpdateTime() {
		return myUpdateTime;
	}

	public static JobInstance fromJobDefinition(JobDefinition<?> theJobDefinition) {
		JobInstance instance = new JobInstance();
		instance.setJobDefinition(theJobDefinition);
		if (theJobDefinition.isGatedExecution()) {
			instance.setFastTracking(true);
			instance.setCurrentGatedStepId(theJobDefinition.getFirstStepId());
		}
		return instance;
	}

	public static JobInstance fromInstanceId(String theInstanceId) {
		JobInstance instance = new JobInstance();
		instance.setInstanceId(theInstanceId);
		return instance;
	}

	@Override
	public String getCurrentGatedStepId() {
		return myCurrentGatedStepId;
	}

	public void setCurrentGatedStepId(String theCurrentGatedStepId) {
		myCurrentGatedStepId = theCurrentGatedStepId;
	}

	@Override
	public int getErrorCount() {
		return myErrorCount;
	}

	public JobInstance setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
		return this;
	}

	@Override
	public String getEstimatedTimeRemaining() {
		return myEstimatedTimeRemaining;
	}

	public void setEstimatedTimeRemaining(String theEstimatedTimeRemaining) {
		myEstimatedTimeRemaining = theEstimatedTimeRemaining;
	}

	@Override
	public boolean isWorkChunksPurged() {
		return myWorkChunksPurged;
	}

	public void setWorkChunksPurged(boolean theWorkChunksPurged) {
		myWorkChunksPurged = theWorkChunksPurged;
	}

	@Override
	public StatusEnum getStatus() {
		return myStatus;
	}

	public JobInstance setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}

	@Override
	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public void setJobDefinitionVersion(int theJobDefinitionVersion) {
		myJobDefinitionVersion = theJobDefinitionVersion;
	}

	@Override
	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	@Override
	public Date getStartTime() {
		return myStartTime;
	}

	public JobInstance setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
		return this;
	}

	@Override
	public Date getEndTime() {
		return myEndTime;
	}

	public JobInstance setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
		return this;
	}

	@Override
	public Integer getCombinedRecordsProcessed() {
		return myCombinedRecordsProcessed;
	}

	public void setCombinedRecordsProcessed(Integer theCombinedRecordsProcessed) {
		myCombinedRecordsProcessed = theCombinedRecordsProcessed;
	}

	@Override
	public Double getCombinedRecordsProcessedPerSecond() {
		return myCombinedRecordsProcessedPerSecond;
	}

	public void setCombinedRecordsProcessedPerSecond(Double theCombinedRecordsProcessedPerSecond) {
		myCombinedRecordsProcessedPerSecond = theCombinedRecordsProcessedPerSecond;
	}

	@Override
	public Date getCreateTime() {
		return myCreateTime;
	}

	public JobInstance setCreateTime(Date theCreateTime) {
		myCreateTime = theCreateTime;
		return this;
	}

	@Override
	public Integer getTotalElapsedMillis() {
		return myTotalElapsedMillis;
	}

	public void setTotalElapsedMillis(Integer theTotalElapsedMillis) {
		myTotalElapsedMillis = theTotalElapsedMillis;
	}

	@Override
	public double getProgress() {
		return myProgress;
	}

	public void setProgress(double theProgress) {
		myProgress = theProgress;
	}

	@Override
	public String getErrorMessage() {
		return myErrorMessage;
	}

	public JobInstance setErrorMessage(String theErrorMessage) {
		myErrorMessage = theErrorMessage;
		return this;
	}

	public String getWarningMessages() {
		return myWarningMessages;
	}

	public JobInstance setWarningMessages(String theWarningMessages) {
		myWarningMessages = theWarningMessages;
		return this;
	}

	public void setJobDefinition(JobDefinition<?> theJobDefinition) {
		setJobDefinitionId(theJobDefinition.getJobDefinitionId());
		setJobDefinitionVersion(theJobDefinition.getJobDefinitionVersion());
	}

	@Override
	public boolean isCancelled() {
		return myCancelled;
	}

	public void setCancelled(boolean theCancelled) {
		myCancelled = theCancelled;
	}

	@Override
	public String getReport() {
		return myReport;
	}

	public void setReport(String theReport) {
		myReport = theReport;
	}

	public String getTriggeringUsername() {
		return myTriggeringUsername;
	}

	public JobInstance setTriggeringUsername(String theTriggeringUsername) {
		myTriggeringUsername = theTriggeringUsername;
		return this;
	}

	public String getTriggeringClientId() {
		return myTriggeringClientId;
	}

	public JobInstance setTriggeringClientId(String theTriggeringClientId) {
		myTriggeringClientId = theTriggeringClientId;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("jobDefinitionId", getJobDefinitionId() + "/" + myJobDefinitionVersion)
				.append("instanceId", myInstanceId)
				.append("status", myStatus)
				.append("myCancelled", myCancelled)
				.append("createTime", myCreateTime)
				.append("startTime", myStartTime)
				.append("endTime", myEndTime)
				.append("updateTime", myUpdateTime)
				.append("combinedRecordsProcessed", myCombinedRecordsProcessed)
				.append("combinedRecordsProcessedPerSecond", myCombinedRecordsProcessedPerSecond)
				.append("totalElapsedMillis", myTotalElapsedMillis)
				.append("workChunksPurged", myWorkChunksPurged)
				.append("progress", myProgress)
				.append("errorMessage", myErrorMessage)
				.append("errorCount", myErrorCount)
				.append("estimatedTimeRemaining", myEstimatedTimeRemaining)
				.append("report", myReport)
				.append("warningMessages", myWarningMessages)
				.append("triggeringUsername", myTriggeringUsername)
				.append("triggeringClientId", myTriggeringClientId)
				.toString();
	}

	/**
	 * Returns true if the job instance is in:
	 * {@link StatusEnum#IN_PROGRESS}
	 * {@link StatusEnum#FINALIZE}
	 * and is not cancelled
	 */
	public boolean isRunning() {
		if (isCancelled()) {
			return false;
		}

		switch (getStatus()) {
			case IN_PROGRESS:
			case ERRORED:
			case FINALIZE:
				return true;
			case COMPLETED:
			case QUEUED:
			case FAILED:
			case CANCELLED:
			default:
				Logs.getBatchTroubleshootingLog().debug("Status {} is considered \"not running\"", myStatus);
		}
		return false;
	}

	public boolean isFinished() {
		return myStatus == StatusEnum.COMPLETED || myStatus == StatusEnum.FAILED || myStatus == StatusEnum.CANCELLED;
	}

	public boolean hasGatedStep() {
		return !isBlank(myCurrentGatedStepId);
	}

	public boolean isPendingCancellationRequest() {
		return myCancelled && myStatus.isCancellable();
	}

	/**
	 * @return true if every step of the job has produced exactly 1 chunk.
	 */
	@Override
	public boolean isFastTracking() {
		return myFastTracking;
	}

	@Override
	public void setFastTracking(boolean theFastTracking) {
		myFastTracking = theFastTracking;
	}
}
