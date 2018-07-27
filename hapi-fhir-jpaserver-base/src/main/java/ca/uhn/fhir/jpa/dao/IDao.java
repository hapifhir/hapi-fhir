package ca.uhn.fhir.jpa.dao;

import java.util.Collection;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.ResourceTag;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

	RuntimeSearchParam getSearchParamByName(RuntimeResourceDefinition theResourceDef, String theParamName);

	Collection<RuntimeSearchParam> getSearchParamsByResourceType(RuntimeResourceDefinition theResourceDef);

	/**
	 * Populate all of the runtime dependencies that a bundle provider requires in order to work
	 */
	void injectDependenciesIntoBundleProvider(PersistedJpaBundleProvider theProvider);

	ISearchBuilder newSearchBuilder();

	void populateFullTextFields(IBaseResource theResource, ResourceTable theEntity);

	<R extends IBaseResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType);

	IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation);

	<R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation);

}
