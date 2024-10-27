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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobOperationResultJson implements IModelJson {
	@JsonProperty("operation")
	private String myOperation;

	@JsonProperty("success")
	private Boolean mySuccess;

	@JsonProperty("message")
	private String myMessage;

	public static JobOperationResultJson newSuccess(String theOperation, String theMessage) {
		JobOperationResultJson result = new JobOperationResultJson();
		result.setSuccess(true);
		result.setOperation(theOperation);
		result.setMessage(theMessage);
		return result;
	}

	public static JobOperationResultJson newFailure(String theOperation, String theMessage) {
		JobOperationResultJson result = new JobOperationResultJson();
		result.setSuccess(false);
		result.setOperation(theOperation);
		result.setMessage(theMessage);
		return result;
	}

	public String getOperation() {
		return myOperation;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public Boolean getSuccess() {
		return mySuccess;
	}

	public void setSuccess(Boolean theSuccess) {
		mySuccess = theSuccess;
	}

	public String getMessage() {
		return myMessage;
	}

	public void setMessage(String theMessage) {
		myMessage = theMessage;
	}
}
