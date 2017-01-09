package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.util.CoverageIgnore;

public class MethodOutcome {

	private Boolean myCreated;
	private IIdType myId;
	private IBaseOperationOutcome myOperationOutcome;
	private IBaseResource myResource;

	/**
	 * Constructor
	 */
	public MethodOutcome() {
		super();
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
	@CoverageIgnore
	public MethodOutcome(IIdType theId, Boolean theCreated) {
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
	public MethodOutcome(IIdType theId, IBaseOperationOutcome theBaseOperationOutcome) {
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
	public MethodOutcome(IIdType theId, IBaseOperationOutcome theBaseOperationOutcome, Boolean theCreated) {
		myId = theId;
		myOperationOutcome = theBaseOperationOutcome;
		myCreated = theCreated;
	}

	/**
	 * Constructor
	 * 
	 * @param theId
	 *            The ID of the created/updated resource
	 */
	public MethodOutcome(IIdType theId) {
		myId = theId;
	}

	/**
	 * Constructor
	 * 
	 * @param theOperationOutcome
	 *            The operation outcome resource to return
	 */
	public MethodOutcome(IBaseOperationOutcome theOperationOutcome) {
		myOperationOutcome = theOperationOutcome;
	}

	/**
	 * This will be set to {@link Boolean#TRUE} for instance of MethodOutcome which are
	 * returned to client instances, if the server has responded with an HTTP 201 Created.
	 */
	public Boolean getCreated() {
		return myCreated;
	}

	public IIdType getId() {
		return myId;
	}

	/**
	 * Returns the {@link IBaseOperationOutcome} resource to return to the client or <code>null</code> if none.
	 * 
	 * @return This method <b>will return null</b>, unlike many methods in the API.
	 */
	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	/**
	 * <b>From a client response:</b> If the method returned an actual resource body (e.g. a create/update with
	 * "Prefer: return=representation") this field will be populated with the
	 * resource itself.
	 */
	public IBaseResource getResource() {
		return myResource;
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
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 */
	public MethodOutcome setCreated(Boolean theCreated) {
		myCreated = theCreated;
		return this;
	}

	/**
	 * @param theId
	 *            The ID of the created/updated resource
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 */
	public MethodOutcome setId(IIdType theId) {
		myId = theId;
		return this;
	}

	/**
	 * Sets the {@link IBaseOperationOutcome} resource to return to the client. Set to <code>null</code> (which is the default) if none.
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 */
	public MethodOutcome setOperationOutcome(IBaseOperationOutcome theBaseOperationOutcome) {
		myOperationOutcome = theBaseOperationOutcome;
		return this;
	}

	/**
	 * <b>In a server response</b>: This field may be populated in server code with the final resource for operations
	 * where a resource body is being created/updated. E.g. for an update method, this field could be populated with
	 * the resource after the update is applied, with the new version ID, lastUpdate time, etc. 
	 * <p>
	 * This field is optional, but if it is populated the server will return the resource body if requested to
	 * do so via the HTTP Prefer header.
	 * </p> 
	 * @return Returns a reference to <code>this</code> for easy method chaining
	 */
	public MethodOutcome setResource(IBaseResource theResource) {
		myResource = theResource;
		return this;
	}

}
