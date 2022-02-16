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
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobInstance implements IModelJson {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty(value = "instanceId", access = JsonProperty.Access.READ_ONLY)
	private String myInstanceId;

	@JsonProperty(value = "status")
	private StatusEnum myStatus;

	@JsonProperty(value = "cancelled")
	private boolean myCancelled;

	@JsonProperty(value = "parameters")
	private List<JobInstanceParameter> myParameters;

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
		setJobDefinitionId(theJobInstance.getJobDefinitionId());
		setJobDefinitionVersion(theJobInstance.getJobDefinitionVersion());
		setInstanceId(theJobInstance.getInstanceId());
		setEstimatedTimeRemaining(theJobInstance.getEstimatedTimeRemaining());
		setTotalElapsedMillis(theJobInstance.getTotalElapsedMillis());
		setCreateTime(theJobInstance.getCreateTime());
		setStartTime(theJobInstance.getStartTime());
		setEndTime(theJobInstance.getEndTime());
		setErrorCount(theJobInstance.getErrorCount());
		setStatus(theJobInstance.getStatus());
		setErrorMessage(theJobInstance.getErrorMessage());
		setWorkChunksPurged(theJobInstance.isWorkChunksPurged());
		setProgress(theJobInstance.getProgress());
		setCombinedRecordsProcessed(theJobInstance.getCombinedRecordsProcessed());
		setCombinedRecordsProcessedPerSecond(theJobInstance.getCombinedRecordsProcessedPerSecond());
		getParameters().addAll(theJobInstance.getParameters());
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public void setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
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

	public List<JobInstanceParameter> getParameters() {
		if (myParameters == null) {
			myParameters = new ArrayList<>();
		}
		return myParameters;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
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

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public JobInstance addParameter(@Nonnull JobInstanceParameter theParameter) {
		Validate.notNull(theParameter);
		getParameters().add(theParameter);
		return this;
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

	public void setErrorMessage(String theErrorMessage) {
		myErrorMessage = theErrorMessage;
	}

	public void setCancelled(boolean theCancelled) {
		myCancelled = theCancelled;
	}

	public boolean isCancelled() {
		return myCancelled;
	}
}
