package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;

public class MethodOutcome {

	private IdDt myId;
	private OperationOutcome myOperationOutcome;
	private IdDt myVersionId;

	public MethodOutcome() {
	}

	public MethodOutcome(IdDt theId) {
		myId = theId;
	}

	public MethodOutcome(IdDt theId, IdDt theVersionId) {
		myId = theId;
		myVersionId = theVersionId;
	}

	public MethodOutcome(IdDt theId, IdDt theVersionId, OperationOutcome theOperationOutcome) {
		myId = theId;
		myVersionId = theVersionId;
		myOperationOutcome = theOperationOutcome;
	}

	public IdDt getId() {
		return myId;
	}

	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	public IdDt getVersionId() {
		return myVersionId;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setOperationOutcome(OperationOutcome theOperationOutcome) {
		myOperationOutcome = theOperationOutcome;
	}

	public void setVersionId(IdDt theVersionId) {
		myVersionId = theVersionId;
	}

}
