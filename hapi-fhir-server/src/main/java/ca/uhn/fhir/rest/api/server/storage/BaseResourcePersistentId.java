package ca.uhn.fhir.rest.api.server.storage;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Objects;

/**
 * This class is an abstraction for however primary keys are stored in the underlying storage engine. This might be
 * a Long, a String, or something else.
 */
public abstract class BaseResourcePersistentId<T> {
	public static final BaseResourcePersistentId NOT_FOUND =  new NotFoundPid();
	private Long myVersion;
	private final String myResourceType;
	// TODO KHS can this be final?
	private IIdType myAssociatedResourceId;


	/**
	 * @deprecated use subclass
	 */
	protected BaseResourcePersistentId(String theResourceType) {
		myResourceType = theResourceType;
	}

	/**
	 * @param theVersion      This should only be populated if a specific version is needed. If you want the current version,
	 *                        leave this as <code>null</code>
	 * @param theResourceType
	 * @deprecated use subclass
	 */
	protected BaseResourcePersistentId(Long theVersion, String theResourceType) {
		myVersion = theVersion;
		myResourceType = theResourceType;
	}

	public IIdType getAssociatedResourceId() {
		return myAssociatedResourceId;
	}

	public BaseResourcePersistentId<T> setAssociatedResourceId(IIdType theAssociatedResourceId) {
		myAssociatedResourceId = theAssociatedResourceId;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		BaseResourcePersistentId<?> that = (BaseResourcePersistentId<?>) theO;
		return Objects.equals(myVersion, that.myVersion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myVersion);
	}

	public abstract T getId();


	public Long getVersion() {
		return myVersion;
	}

	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}

	public String getResourceType() {
		return myResourceType;
	}
}
