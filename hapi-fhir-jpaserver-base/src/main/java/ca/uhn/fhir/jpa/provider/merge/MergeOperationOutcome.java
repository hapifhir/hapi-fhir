/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * See <a href="https://build.fhir.org/patient-operation-merge.html">Patient $merge spec</a>
 */
public class MergeOperationOutcome {
	private IBaseOperationOutcome myOperationOutcome;
	private int myHttpStatusCode;
	private IBaseResource myUpdatedTargetResource;
	private IBaseResource myTask;

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	public void setOperationOutcome(IBaseOperationOutcome theOperationOutcome) {
		this.myOperationOutcome = theOperationOutcome;
	}

	public int getHttpStatusCode() {
		return myHttpStatusCode;
	}

	public void setHttpStatusCode(int theHttpStatusCode) {
		this.myHttpStatusCode = theHttpStatusCode;
	}

	public IBaseResource getUpdatedTargetResource() {
		return myUpdatedTargetResource;
	}

	public void setUpdatedTargetResource(IBaseResource theUpdatedTargetResource) {
		this.myUpdatedTargetResource = theUpdatedTargetResource;
	}

	public IBaseResource getTask() {
		return myTask;
	}

	public void setTask(IBaseResource theTask) {
		this.myTask = theTask;
	}
}
