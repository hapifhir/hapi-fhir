package ca.uhn.fhir.jpa.api.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

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

/**
 * Note that this interface is not considered a stable interface. While it is possible to build applications
 * that use it directly, please be aware that we may modify methods, add methods, or even remove methods from
 * time to time, even within minor point releases.
 */
public interface IDao {
	String RESOURCE_PID_KEY = "RESOURCE_PID";

	MetadataKeyResourcePid RESOURCE_PID = new MetadataKeyResourcePid(RESOURCE_PID_KEY);

	MetadataKeyCurrentlyReindexing CURRENTLY_REINDEXING = new MetadataKeyCurrentlyReindexing("CURRENTLY_REINDEXING");

	FhirContext getContext();

	IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation);

	<R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation);

}
