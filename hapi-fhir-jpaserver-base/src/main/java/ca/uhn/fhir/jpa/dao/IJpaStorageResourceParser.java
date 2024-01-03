/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

public interface IJpaStorageResourceParser extends IStorageResourceParser {

	/**
	 * Convert a storage entity into a FHIR resource model instance. This method may return null if the entity is not
	 * completely flushed, including the entities history entries.
	 */
	<R extends IBaseResource> R toResource(
			Class<R> theResourceType,
			IBaseResourceEntity theEntity,
			Collection<ResourceTag> theTagList,
			boolean theForHistoryOperation);

	/**
	 * Populate the metadata (Resource.meta.*) from a storage entity and other related
	 * objects pulled from the database
	 */
	<R extends IBaseResource> R populateResourceMetadata(
			IBaseResourceEntity theEntitySource,
			boolean theForHistoryOperation,
			@Nullable Collection<? extends BaseTag> tagList,
			long theVersion,
			R theResourceTarget);

	/**
	 * Populates a resource model object's metadata (Resource.meta.*) based on the
	 * values from a storage entity.
	 *
	 * @param theEntitySource The source
	 * @param theResourceTarget The target
	 */
	void updateResourceMetadata(IBaseResourceEntity theEntitySource, IBaseResource theResourceTarget);
}
