package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

public interface IDao {

	MetadataKeyResourcePid RESOURCE_PID = new MetadataKeyResourcePid("RESOURCE_PID");

	MetadataKeyCurrentlyReindexing CURRENTLY_REINDEXING = new MetadataKeyCurrentlyReindexing("CURRENTLY_REINDEXING");

	FhirContext getContext();

	/**
	 * Populate all of the runtime dependencies that a bundle provider requires in order to work
	 */
	void injectDependenciesIntoBundleProvider(PersistedJpaBundleProvider theProvider);

	ISearchBuilder newSearchBuilder();

	IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation);

	<R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation);

	ISearchParamRegistry getSearchParamRegistry();
}
