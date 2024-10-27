/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.api.server.storage;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Objects;

/**
 * This class is an abstraction for however primary keys are stored in the underlying storage engine. This might be
 * a Long, a String, or something else.  The generic type T represents the primary key type.
 */
public abstract class BaseResourcePersistentId<T> implements IResourcePersistentId<T> {
	private Long myVersion;
	private final String myResourceType;
	private IIdType myAssociatedResourceId;

	protected BaseResourcePersistentId(String theResourceType) {
		myResourceType = theResourceType;
	}

	protected BaseResourcePersistentId(Long theVersion, String theResourceType) {
		myVersion = theVersion;
		myResourceType = theResourceType;
	}

	@Override
	public IIdType getAssociatedResourceId() {
		return myAssociatedResourceId;
	}

	@Override
	public IResourcePersistentId<T> setAssociatedResourceId(IIdType theAssociatedResourceId) {
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

	@Override
	public Long getVersion() {
		return myVersion;
	}

	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	@Override
	public void setVersion(Long theVersion) {
		myVersion = theVersion;
	}

	@Override
	public String getResourceType() {
		return myResourceType;
	}
}
