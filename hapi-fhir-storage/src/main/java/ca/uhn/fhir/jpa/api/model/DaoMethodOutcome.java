package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class DaoMethodOutcome extends MethodOutcome {

	private IBasePersistedResource myEntity;
	private IBaseResource myPreviousResource;
	private boolean myNop;
	private ResourcePersistentId myResourcePersistentId;

	/**
	 * Constructor
	 */
	public DaoMethodOutcome() {
		super();
	}

	/**
	 * Was this a NO-OP - Typically because of an update to a resource that already matched the contents provided
	 */
	public boolean isNop() {
		return myNop;
	}

	/**
	 * Was this a NO-OP - Typically because of an update to a resource that already matched the contents provided
	 */
	public DaoMethodOutcome setNop(boolean theNop) {
		myNop = theNop;
		return this;
	}

	public IBasePersistedResource getEntity() {
		return myEntity;
	}

	public DaoMethodOutcome setEntity(IBasePersistedResource theEntity) {
		myEntity = theEntity;
		return this;
	}

	/**
	 * For update operations, this is the body of the resource as it was before the
	 * update
	 */
	public IBaseResource getPreviousResource() {
		return myPreviousResource;
	}

	/**
	 * For update operations, this is the body of the resource as it was before the
	 * update
	 */
	public void setPreviousResource(IBaseResource thePreviousResource) {
		myPreviousResource = thePreviousResource;
	}

	@Override
	public DaoMethodOutcome setCreated(Boolean theCreated) {
		super.setCreated(theCreated);
		return this;
	}

	public ResourcePersistentId getPersistentId() {
		return myResourcePersistentId;
	}

	public DaoMethodOutcome setPersistentId(ResourcePersistentId theResourcePersistentId) {
		myResourcePersistentId = theResourcePersistentId;
		return this;
	}

}
