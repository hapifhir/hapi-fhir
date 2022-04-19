package ca.uhn.fhir.batch2.model;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.jpa.util.JsonDateDeserializer;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class JobInstance extends JobInstanceStartRequest implements IModelJson {

	@JsonProperty(value = "jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty(value = "instanceId", access = JsonProperty.Access.READ_ONLY)
	private String myInstanceId;

	@JsonProperty(value = "status")
	private StatusEnum myStatus;

	@JsonProperty(value = "cancelled")
	private boolean myCancelled;

	@JsonProperty(value = "createTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myCreateTime;

	@JsonProperty(value = "startTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myStartTime;

	@JsonProperty(value = "endTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myEndTime;

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
		super(theJobInstance);
		setCancelled(theJobInstance.isCancelled());
		setCombinedRecordsProcessed(theJobInstance.getCombinedRecordsProcessed());
		setCombinedRecordsProcessedPerSecond(theJobInstance.getCombinedRecordsProcessedPerSecond());
		setCreateTime(theJobInstance.getCreateTime());
		setEndTime(theJobInstance.getEndTime());
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
	}

	public String getCurrentGatedStepId() {
		return myCurrentGatedStepId;
	}

	public void setCurrentGatedStepId(String theCurrentGatedStepId) {
		myCurrentGatedStepId = theCurrentGatedStepId;
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public JobInstance setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
		return this;
	}

	public String getEstimatedTimeRemaining() {
		return myEstimatedTimeRemaining;
	}

	public void setEstimatedTimeRemaining(String theEstimatedTimeRemaining) {
		myEstimatedTimeRemaining = theEstimatedTimeRemaining;
	}

	public boolean isWorkChunksPurged() {
		return myWorkChunksPurged;
	}

	public void setWorkChunksPurged(boolean theWorkChunksPurged) {
		myWorkChunksPurged = theWorkChunksPurged;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public JobInstance setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public void setJobDefinitionVersion(int theJobDefinitionVersion) {
		myJobDefinitionVersion = theJobDefinitionVersion;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public Date getStartTime() {
		return myStartTime;
	}

	public JobInstance setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
		return this;
	}

	public Date getEndTime() {
		return myEndTime;
	}

	public JobInstance setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
		return this;
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

	public JobInstance setCreateTime(Date theCreateTime) {
		myCreateTime = theCreateTime;
		return this;
	}

	public Integer getTotalElapsedMillis() {
		return myTotalElapsedMillis;
	}

	public void setTotalElapsedMillis(Integer theTotalElapsedMillis) {
		myTotalElapsedMillis = theTotalElapsedMillis;
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

	public JobInstance setErrorMessage(String theErrorMessage) {
		myErrorMessage = theErrorMessage;
		return this;
	}

	public boolean isCancelled() {
		return myCancelled;
	}

	public void setCancelled(boolean theCancelled) {
		myCancelled = theCancelled;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("jobDefinitionId", getJobDefinitionId() + "/" + myJobDefinitionVersion)
			.append("instanceId", myInstanceId)
			.append("status", myStatus)
			.append("createTime", myCreateTime)
			.append("startTime", myStartTime)
			.append("endTime", myEndTime)
			.append("combinedRecordsProcessed", myCombinedRecordsProcessed)
			.append("combinedRecordsProcessedPerSecond", myCombinedRecordsProcessedPerSecond)
			.append("totalElapsedMillis", myTotalElapsedMillis)
			.append("workChunksPurged", myWorkChunksPurged)
			.append("progress", myProgress)
			.append("errorMessage", myErrorMessage)
			.append("errorCount", myErrorCount)
			.append("estimatedTimeRemaining", myEstimatedTimeRemaining)
			.toString();
	}

	/**
	 * Returns true if the job instance is in {@link StatusEnum#IN_PROGRESS} and is not cancelled
	 */
	public boolean isRunning() {
		return getStatus() == StatusEnum.IN_PROGRESS && !isCancelled();
	}
}
