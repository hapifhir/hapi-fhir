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
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.Validate;

import java.util.Date;

public class WorkChunk implements IModelJson {

	@JsonProperty("id")
	private String myId;

	@JsonProperty("sequence")
	private int mySequence;

	@JsonProperty("status")
	private StatusEnum myStatus;

	@JsonProperty("jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty("jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty("targetStepId")
	private String myTargetStepId;

	@JsonProperty("instanceId")
	private String myInstanceId;

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

	@JsonProperty(value = "recordsProcessed", access = JsonProperty.Access.READ_ONLY)
	private Integer myRecordsProcessed;

	@JsonProperty(value = "errorMessage", access = JsonProperty.Access.READ_ONLY)
	private String myErrorMessage;

	@JsonProperty(value = "errorCount", access = JsonProperty.Access.READ_ONLY)
	private int myErrorCount;

	/**
	 * Constructor
	 */
	public WorkChunk() {
		super();
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

	public StatusEnum getStatus() {
		return myStatus;
	}

	public WorkChunk setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public WorkChunk setJobDefinitionId(String theJobDefinitionId) {
		Validate.notBlank(theJobDefinitionId);
		myJobDefinitionId = theJobDefinitionId;
		return this;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public WorkChunk setJobDefinitionVersion(int theJobDefinitionVersion) {
		Validate.isTrue(theJobDefinitionVersion >= 1);
		myJobDefinitionVersion = theJobDefinitionVersion;
		return this;
	}

	public String getTargetStepId() {
		return myTargetStepId;
	}

	public WorkChunk setTargetStepId(String theTargetStepId) {
		Validate.notBlank(theTargetStepId);
		myTargetStepId = theTargetStepId;
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

	public String getInstanceId() {
		return myInstanceId;
	}

	public WorkChunk setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
		return this;
	}

	public String getId() {
		return myId;
	}

	public WorkChunk setId(String theId) {
		Validate.notBlank(theId);
		myId = theId;
		return this;
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

	public String getErrorMessage() {
		return myErrorMessage;
	}

	public WorkChunk setErrorMessage(String theErrorMessage) {
		myErrorMessage = theErrorMessage;
		return this;
	}
}
