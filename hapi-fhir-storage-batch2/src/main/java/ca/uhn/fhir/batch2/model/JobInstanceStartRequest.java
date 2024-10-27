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
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobInstanceStartRequest implements IModelJson {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "parameters")
	private String myParameters;

	/**
	 * If true, batch2 will check the existing jobs and
	 * if one with the same parameters that is already running
	 * (ie, not failed, cancelled, etc)
	 * it will return that id
	 */
	@JsonProperty(value = "useCache")
	private boolean myUseCache;

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

	/**
	 * Constructor
	 *
	 * @since 6.8.0
	 */
	public JobInstanceStartRequest(String theJobDefinitionId, IModelJson theParameters) {
		setJobDefinitionId(theJobDefinitionId);
		setParameters(theParameters);
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

	/**
	 * Sets the parameters for the job.
	 * Please note that these need to be backward compatible as we do not have a way to migrate them to a different structure at the moment.
	 * @param theParameters the parameters
	 * @return the current instance.
	 */
	public JobInstanceStartRequest setParameters(IModelJson theParameters) {
		myParameters = JsonUtil.serializeWithSensitiveData(theParameters);
		return this;
	}

	public <T extends IModelJson> T getParameters(Class<T> theType) {
		if (myParameters == null) {
			return null;
		}
		return JsonUtil.deserialize(myParameters, theType);
	}

	public boolean isUseCache() {
		return myUseCache;
	}

	public void setUseCache(boolean theUseCache) {
		myUseCache = theUseCache;
	}

	@Override
	public String toString() {
		return "JobInstanceStartRequest{" + "myJobDefinitionId='"
				+ myJobDefinitionId + '\'' + ", myParameters='"
				+ myParameters + '\'' + ", myUseCache="
				+ myUseCache + '}';
	}
}
