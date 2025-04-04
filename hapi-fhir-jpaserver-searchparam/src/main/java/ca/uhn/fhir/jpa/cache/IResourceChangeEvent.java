/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Registered IResourceChangeListener instances are called with this event to provide them with a list of ids of resources
 * that match the search parameters and that changed from the last time they were checked.
 */
public interface IResourceChangeEvent {
	List<IIdType> getCreatedResourceIds();

	List<IIdType> getUpdatedResourceIds();

	List<IIdType> getDeletedResourceIds();

	/**
	 * @return true when all three lists are empty
	 */
	boolean isEmpty();
}
