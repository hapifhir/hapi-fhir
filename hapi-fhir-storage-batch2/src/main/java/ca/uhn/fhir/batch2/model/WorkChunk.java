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

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

import static ca.uhn.fhir.batch2.util.Batch2Utils.REDUCTION_STEP_CHUNK_ID_PLACEHOLDER;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Payload for step processing.
 * Implements a state machine on {@link WorkChunkStatusEnum}.
 *
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public class WorkChunk extends WorkChunkMetadata {

	@JsonProperty("data")
	private String myData;

	@JsonProperty("createTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myCreateTime;

	@JsonProperty("startTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myStartTime;

	@JsonProperty("endTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myEndTime;

	@JsonProperty("updateTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myUpdateTime;

	/**
	 * Timestamp of when next to call the current workchunk poll step.
	 */
	@JsonProperty("nextPollTimestamp")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myNextPollTime;

	/**
	 * Total polling attempts done thus far.
	 */
	@JsonProperty("pollAttempts")
	private Integer myPollAttempts;

	@JsonProperty(value = "recordsProcessed", access = JsonProperty.Access.READ_ONLY)
	private Integer myRecordsProcessed;

	@JsonProperty(value = "errorMessage", access = JsonProperty.Access.READ_ONLY)
	private String myErrorMessage;

	@JsonProperty(value = "errorCount", access = JsonProperty.Access.READ_ONLY)
	private int myErrorCount;

	@JsonProperty(value = "warningMessage", access = JsonProperty.Access.READ_ONLY)
	private String myWarningMessage;

	/**
	 * Constructor
	 */
	public WorkChunk() {
		super();
	}

	public WorkChunk setId(String theId) {
		super.setId(theId);
		return this;
	}

	public WorkChunk setStatus(WorkChunkStatusEnum theStatus) {
		super.setStatus(theStatus);
		return this;
	}

	public WorkChunk setInstanceId(String theInstanceId) {
		super.setInstanceId(theInstanceId);
		return this;
	}

	public WorkChunk setTargetStepId(String theTargetStepId) {
		super.setTargetStepId(theTargetStepId);
		return this;
	}

	public WorkChunk setJobDefinitionVersion(int theJobDefinitionVersion) {
		super.setJobDefinitionVersion(theJobDefinitionVersion);
		return this;
	}

	public WorkChunk setJobDefinitionId(String theJobDefinitionId) {
		super.setJobDefinitionId(theJobDefinitionId);
		return this;
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public WorkChunk setErrorCount(int theErrorCount) {
		myErrorCount = theErrorCount;
		return this;
	}

	public Date getStartTime() {
		return myStartTime;
	}

	public WorkChunk setStartTime(Date theStartTime) {
		myStartTime = theStartTime;
		return this;
	}

	public Date getEndTime() {
		return myEndTime;
	}

	public WorkChunk setEndTime(Date theEndTime) {
		myEndTime = theEndTime;
		return this;
	}

	public Integer getRecordsProcessed() {
		return myRecordsProcessed;
	}

	public WorkChunk setRecordsProcessed(Integer theRecordsProcessed) {
		myRecordsProcessed = theRecordsProcessed;
		return this;
	}

	public String getData() {
		return myData;
	}

	public WorkChunk setData(String theData) {
		myData = theData;
		return this;
	}

	public WorkChunk setData(IModelJson theData) {
		setData(JsonUtil.serializeOrInvalidRequest(theData));
		return this;
	}

	public <T extends IModelJson> T getData(Class<T> theType) {
		return JsonUtil.deserialize(getData(), theType);
	}

	public Date getCreateTime() {
		return myCreateTime;
	}

	public void setCreateTime(Date theCreateTime) {
		myCreateTime = theCreateTime;
	}

	public String getErrorMessage() {
		return myErrorMessage;
	}

	public WorkChunk setErrorMessage(String theErrorMessage) {
		myErrorMessage = theErrorMessage;
		return this;
	}

	public Date getUpdateTime() {
		return myUpdateTime;
	}

	public void setUpdateTime(Date theUpdateTime) {
		myUpdateTime = theUpdateTime;
	}

	public Date getNextPollTime() {
		return myNextPollTime;
	}

	public void setNextPollTime(Date theNextPollTime) {
		myNextPollTime = theNextPollTime;
	}

	public Integer getPollAttempts() {
		return myPollAttempts;
	}

	public void setPollAttempts(int thePollAttempts) {
		myPollAttempts = thePollAttempts;
	}

	public String getWarningMessage() {
		return myWarningMessage;
	}

	public WorkChunk setWarningMessage(String theWarningMessage) {
		myWarningMessage = theWarningMessage;
		return this;
	}

	/**
	 * Returns true if the workchunk is a reduction workchunk; false otherwise
	 */
	public boolean isReductionWorkChunk() {
		return getId() != null && getId().equals(REDUCTION_STEP_CHUNK_ID_PLACEHOLDER);
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this);
		b.append("Id", getId());
		b.append("Sequence", getSequence());
		b.append("Status", getStatus());
		b.append("JobDefinitionId", getJobDefinitionId());
		b.append("JobDefinitionVersion", getJobDefinitionVersion());
		b.append("TargetStepId", getTargetStepId());
		b.append("InstanceId", getInstanceId());
		b.append("Data", isNotBlank(myData) ? "(present)" : "(absent)");
		b.append("CreateTime", myCreateTime);
		b.append("StartTime", myStartTime);
		b.append("EndTime", myEndTime);
		b.append("UpdateTime", myUpdateTime);
		b.append("RecordsProcessed", myRecordsProcessed);
		if (myNextPollTime != null) {
			b.append("NextPollTime", myNextPollTime);
		}
		b.append("PollAttempts", myPollAttempts);
		if (isNotBlank(myErrorMessage)) {
			b.append("ErrorMessage", myErrorMessage);
		}
		if (myErrorCount > 0) {
			b.append("ErrorCount", myErrorCount);
		}
		if (isNotBlank(myWarningMessage)) {
			b.append("WarningMessage", myWarningMessage);
		}
		return b.toString();
	}
}
