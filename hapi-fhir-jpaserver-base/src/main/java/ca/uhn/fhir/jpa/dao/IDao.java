package ca.uhn.fhir.jpa.dao;

import java.util.Collection;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum.ResourceMetadataKeySupportingAnyResource;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public interface IDao {

	public static final ResourceMetadataKeySupportingAnyResource<Long, Long> RESOURCE_PID = new ResourceMetadataKeySupportingAnyResource<Long, Long>("RESOURCE_PID") {

		private static final long serialVersionUID = 1L;

		@Override
		public Long get(IAnyResource theResource) {
			return (Long) theResource.getUserData(RESOURCE_PID.name());
		}

		@Override
		public Long get(IResource theResource) {
			return (Long) theResource.getResourceMetadata().get(RESOURCE_PID);
		}

		@Override
		public void put(IAnyResource theResource, Long theObject) {
			theResource.setUserData(RESOURCE_PID.name(), theObject);
		}

		@Override
		public void put(IResource theResource, Long theObject) {
			theResource.getResourceMetadata().put(RESOURCE_PID, theObject);
		}
	};

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

	<R extends IBaseResource> R toResource(Class<R> theResourceType, BaseHasResource theEntity, boolean theForHistoryOperation);

}
