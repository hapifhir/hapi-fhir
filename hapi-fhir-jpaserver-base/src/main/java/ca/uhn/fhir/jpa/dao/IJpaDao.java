package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;

public interface IJpaDao<T extends IBaseResource> {
	@SuppressWarnings("unchecked")
	IBasePersistedResource updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource
		theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
										boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry);

	IBasePersistedResource updateInternal(RequestDetails theRequestDetails, T theResource, boolean thePerformIndexing, boolean theForceUpdateVersion,
										  IBasePersistedResource theEntity, IIdType theResourceId, IBaseResource theOldResource);
}
