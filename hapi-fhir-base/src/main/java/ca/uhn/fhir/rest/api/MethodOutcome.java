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
	private IdDt myVersionId;
	private boolean myCreated;
	private OperationOutcome myOperationOutcome;

	public MethodOutcome() {
	}

	public MethodOutcome(IdDt theId) {
		myId=theId;
	}

	public MethodOutcome(boolean theCreated, IdDt theId, IdDt theVersionId) {
		super();
		myId = theId;
		myVersionId = theVersionId;
		myCreated=theCreated;
	}

	public IdDt getId() {
		return myId;
	}

	public IdDt getVersionId() {
		return myVersionId;
	}

	public void setId(IdDt theId) {
		myId = theId;
	}

	public void setVersionId(IdDt theVersionId) {
		myVersionId = theVersionId;
	}

	/**
	 * Set to <code>true</code> if the method resulted in the creation of a new resource. Set to 
	 * <code>false</code> if the method resulted in an update/modification/removal to an existing resource.
	 */
	public boolean isCreated() {
		return myCreated;
	}

	/**
	 * Set to <code>true</code> if the method resulted in the creation of a new resource. Set to 
	 * <code>false</code> if the method resulted in an update/modification/removal to an existing resource.
	 */
	public void setCreated(boolean theCreated) {
		myCreated=theCreated;
	}

	public void setOperationOutcome(OperationOutcome theOperationOutcome) {
		myOperationOutcome=theOperationOutcome;
	}

	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

}
