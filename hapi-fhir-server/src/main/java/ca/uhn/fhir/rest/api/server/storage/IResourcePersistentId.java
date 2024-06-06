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

public interface IResourcePersistentId<T> {

	IResourcePersistentId NOT_FOUND = new NotFoundPid();

	IIdType getAssociatedResourceId();

	IResourcePersistentId<T> setAssociatedResourceId(IIdType theAssociatedResourceId);

	T getId();

	Long getVersion();

	/**
	 * @param theVersion This should only be populated if a specific version is needed. If you want the current version,
	 *                   leave this as <code>null</code>
	 */
	void setVersion(Long theVersion);

	/**
	 * Note that, like Version, ResourceType is usually not populated.  It is only populated in scenarios where it
	 * is needed downstream.
	 */

	// TODO KHS this is only used by batch.  Consider moving this to a new interface just for batch resource ids.
	String getResourceType();
}
