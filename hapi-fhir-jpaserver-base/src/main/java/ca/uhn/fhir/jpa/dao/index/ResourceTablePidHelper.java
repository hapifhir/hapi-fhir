package ca.uhn.fhir.jpa.dao.index;

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

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResourceTablePidHelper {
	private static final String RESOURCE_PID = "RESOURCE_PID";

	private final IdHelperService myIdHelperService;

	@Autowired
	public ResourceTablePidHelper(IdHelperService theIdHelperService) {
		myIdHelperService = theIdHelperService;
	}

	@Nullable
	public Long getPidOrNull(IBaseResource theResource) {
		IAnyResource anyResource = (IAnyResource) theResource;
		Long retval = (Long) anyResource.getUserData(RESOURCE_PID);
		if (retval == null) {
			IIdType id = theResource.getIdElement();
			try {
				retval = myIdHelperService.resolveResourcePersistentIds(id.getResourceType(), id.getIdPart()).getIdAsLong();
			} catch (ResourceNotFoundException e) {
				return null;
			}
		}
		return retval;
	}

	@Nonnull
	public Long getPidOrThrowException(IIdType theId) {
		return getPidOrThrowException(theId, null);
	}

	@Nonnull
	public Long getPidOrThrowException(IIdType theId, RequestDetails theRequestDetails) {
		List<IIdType> ids = Collections.singletonList(theId);
		List<ResourcePersistentId> resourcePersistentIds = myIdHelperService.resolveResourcePersistentIds(ids, theRequestDetails);
		return resourcePersistentIds.get(0).getIdAsLong();
	}

	@Nonnull
	public Long getPidOrThrowException(IAnyResource theResource) {
		return (Long) theResource.getUserData(RESOURCE_PID);
	}

	@Nonnull
	public Map<Long, IIdType> getPidToIdMap(Collection<IIdType> theSubscriberIds) {
		return theSubscriberIds.stream().collect(Collectors.toMap(this::getPidOrThrowException, Function.identity()));
	}
}
