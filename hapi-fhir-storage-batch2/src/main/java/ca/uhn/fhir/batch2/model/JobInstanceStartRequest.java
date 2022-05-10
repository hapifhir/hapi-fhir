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
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstanceStartRequest implements IModelJson {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "parameters")
	private String myParameters;

	/**
	 * Constructor
	 */
	public JobInstanceStartRequest() {
		super();
	}

	/**
	 * Copy constructor
	 */
	public JobInstanceStartRequest(JobInstanceStartRequest theJobInstance) {
		super();
		setJobDefinitionId(theJobInstance.getJobDefinitionId());
		setParameters(theJobInstance.getParameters());
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

	public JobInstanceStartRequest setParameters(IModelJson theParameters) {
		myParameters = JsonUtil.serializeOrInvalidRequest(theParameters);
		return this;
	}

	public <T extends IModelJson> T getParameters(Class<T> theType) {
		return JsonUtil.deserialize(myParameters, theType);
	}

}
