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
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.function.Supplier;

public class LazyDaoMethodOutcome extends DaoMethodOutcome {

	private Supplier<EntityAndResource> myEntitySupplier;
	private Supplier<IIdType> myIdSupplier;
	private Runnable myEntitySupplierUseCallback;

	/**
	 * Constructor
	 */
	public LazyDaoMethodOutcome(ResourcePersistentId theResourcePersistentId) {
		setPersistentId(theResourcePersistentId);
	}

	@Override
	public IBasePersistedResource getEntity() {
		IBasePersistedResource retVal = super.getEntity();
		if (retVal == null) {
			tryToRunSupplier();
			retVal = super.getEntity();
		}
		return retVal;
	}

	private void tryToRunSupplier() {
		if (myEntitySupplier != null) {
			EntityAndResource entityAndResource = myEntitySupplier.get();
			setEntity(entityAndResource.getEntity());
			setResource(entityAndResource.getResource());
			setId(entityAndResource.getResource().getIdElement());
			myEntitySupplierUseCallback.run();
		}
	}

	@Override
	public IIdType getId() {
		IIdType retVal = super.getId();
		if (retVal == null) {
			if (super.hasResource()) {
				retVal = getResource().getIdElement();
				setId(retVal);
			} else {
				if (myIdSupplier != null) {
					retVal = myIdSupplier.get();
					setId(retVal);
				}
			}
		}
		return retVal;
	}

	@Override
	public IBaseResource getResource() {
		IBaseResource retVal = super.getResource();
		if (retVal == null) {
			tryToRunSupplier();
			retVal = super.getResource();
		}
		return retVal;
	}

	public void setEntitySupplier(Supplier<EntityAndResource> theEntitySupplier) {
		myEntitySupplier = theEntitySupplier;
	}

	public void setEntitySupplierUseCallback(Runnable theEntitySupplierUseCallback) {
		myEntitySupplierUseCallback = theEntitySupplierUseCallback;
	}

	public void setIdSupplier(Supplier<IIdType> theIdSupplier) {
		myIdSupplier = theIdSupplier;
	}


	public static class EntityAndResource {
		private final IBasePersistedResource myEntity;
		private final IBaseResource myResource;

		public EntityAndResource(IBasePersistedResource theEntity, IBaseResource theResource) {
			myEntity = theEntity;
			myResource = theResource;
		}

		public IBasePersistedResource getEntity() {
			return myEntity;
		}

		public IBaseResource getResource() {
			return myResource;
		}
	}

}
