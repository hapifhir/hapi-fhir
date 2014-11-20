package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.primitive.IdDt;

public class MethodOutcome {

	private IdDt myId;
	private BaseOperationOutcome myOperationOutcome;
	private IdDt myVersionId;
	private Boolean myCreated;

	/**
	 * Constructor
	 */
	public MethodOutcome() {
	}

	/**
	 * Constructor
	 * 
	 * @param theId
	 *            The ID of the created/updated resource
	 */
	public MethodOutcome(IdDt theId) {
		myId = theId;
	}

	/**
	 * Constructor
	 * 
	 * @param theId
	 *            The ID of the created/updated resource
	 * 
	 * @param theCreated
	 *            If not null, indicates whether the resource was created (as opposed to being updated). This is generally not needed, since the server can assume based on the method being called
	 *            whether the result was a creation or an update. However, it can be useful if you are implementing an update method that does a create if the ID doesn't already exist.
	 */
	public MethodOutcome(IdDt theId, Boolean theCreated) {
		myId = theId;
		myCreated = theCreated;
	}

	/**
	 * Constructor
	 * 
	 * @param theId
	 *            The ID of the created/updated resource
	 * 
	 * @param theBaseOperationOutcome
	 *            The operation outcome to return with the response (or null for none)
	 */
	public MethodOutcome(IdDt theId, BaseOperationOutcome theBaseOperationOutcome) {
		myId = theId;
		myOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * Constructor
	 * 
	 * @param theId
	 *            The ID of the created/updated resource
	 * 
	 * @param theBaseOperationOutcome
	 *            The operation outcome to return with the response (or null for none)
	 * 
	 * @param theCreated
	 *            If not null, indicates whether the resource was created (as opposed to being updated). This is generally not needed, since the server can assume based on the method being called
	 *            whether the result was a creation or an update. However, it can be useful if you are implementing an update method that does a create if the ID doesn't already exist.
	 */
	public MethodOutcome(IdDt theId, BaseOperationOutcome theBaseOperationOutcome, Boolean theCreated) {
		myId = theId;
		myOperationOutcome = theBaseOperationOutcome;
		myCreated = theCreated;
	}

	/**
	 * @deprecated Use the constructor which accepts a single IdDt parameter, and include the logical ID and version ID in that IdDt instance
	 */
	public MethodOutcome(IdDt theId, IdDt theVersionId) {
		myId = theId;
		myVersionId = theVersionId;
	}

	/**
	 * @deprecated Use the constructor which accepts a single IdDt parameter, and include the logical ID and version ID in that IdDt instance
	 */
	public MethodOutcome(IdDt theId, IdDt theVersionId, BaseOperationOutcome theBaseOperationOutcome) {
		myId = theId;
		myVersionId = theVersionId;
		myOperationOutcome = theBaseOperationOutcome;
	}

	public IdDt getId() {
		return myId;
	}

	/**
	 * Returns the {@link BaseOperationOutcome} resource to return to the client or <code>null</code> if none.
	 * 
	 * @return This method <b>will return null</b>, unlike many methods in the API.
	 */
	public BaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	/**
	 * @deprecated {@link MethodOutcome#getId()} should return the complete ID including version if it is available
	 */
	public IdDt getVersionId() {
		return myVersionId;
	}

	public Boolean getCreated() {
		return myCreated;
	}

	/**
	 * If not null, indicates whether the resource was created (as opposed to being updated). This is generally not needed, since the server can assume based on the method being called whether the
	 * result was a creation or an update. However, it can be useful if you are implementing an update method that does a create if the ID doesn't already exist.
	 * <p>
	 * Users of HAPI should only interact with this method in Server applications
	 * </p>
	 * 
	 * @param theCreated
	 *            If not null, indicates whether the resource was created (as opposed to being updated). This is generally not needed, since the server can assume based on the method being called
	 *            whether the result was a creation or an update. However, it can be useful if you are implementing an update method that does a create if the ID doesn't already exist.
	 */
	public void setCreated(Boolean theCreated) {
		myCreated = theCreated;
	}

	/**
	 * @param theId
	 *            The ID of the created/updated resource
	 */
	public void setId(IdDt theId) {
		myId = theId;
	}

	/**
	 * Sets the {@link BaseOperationOutcome} resource to return to the client. Set to <code>null</code> (which is the default) if none.
	 */
	public void setOperationOutcome(BaseOperationOutcome theBaseOperationOutcome) {
		myOperationOutcome = theBaseOperationOutcome;
	}

	/**
	 * @deprecated Put the ID and version ID into the same IdDt instance and pass it to {@link #setId(IdDt)}
	 */
	public void setVersionId(IdDt theVersionId) {
		myVersionId = theVersionId;
	}

}
