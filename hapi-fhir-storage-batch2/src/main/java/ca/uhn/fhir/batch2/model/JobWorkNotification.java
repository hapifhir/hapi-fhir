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

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public class JobWorkNotification implements IModelJson {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty(value = "targetStepId")
	private String myTargetStepId;

	@JsonProperty(value = "chunkId")
	private String myChunkId;

	@JsonProperty(value = "instanceId")
	private String myInstanceId;

	public JobWorkNotification() {
	}

	public JobWorkNotification(@Nonnull String theJobDefinitionId, int jobDefinitionVersion, @Nonnull String theInstanceId, @Nonnull String theTargetStepId, @Nonnull String theChunkId) {
		setJobDefinitionId(theJobDefinitionId);
		setJobDefinitionVersion(jobDefinitionVersion);
		setChunkId(theChunkId);
		setInstanceId(theInstanceId);
		setTargetStepId(theTargetStepId);
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

	public String getChunkId() {
		return myChunkId;
	}

	public void setChunkId(String theChunkId) {
		myChunkId = theChunkId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	@Override
	public String toString() {
		return String.format("job[%s] instance[%s] step[%s] chunk[%s]", myJobDefinitionId, myInstanceId, myTargetStepId, myChunkId);
	}
}
