package ca.uhn.fhir.jpa.api.dao;

/*-
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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum.ResourceMetadataKeySupportingAnyResource;
import org.hl7.fhir.instance.model.api.IAnyResource;

public final class MetadataKeyResourcePid extends ResourceMetadataKeySupportingAnyResource<Long, Long> {
	private static final long serialVersionUID = 1L;

	MetadataKeyResourcePid(String theValue) {
		super(theValue);
	}

	@Override
	public Long get(IAnyResource theResource) {
		return (Long) theResource.getUserData(IDao.RESOURCE_PID.name());
	}

	@Override
	public Long get(IResource theResource) {
		return (Long) theResource.getResourceMetadata().get(IDao.RESOURCE_PID);
	}

	@Override
	public void put(IAnyResource theResource, Long theObject) {
		theResource.setUserData(IDao.RESOURCE_PID.name(), theObject);
	}

	@Override
	public void put(IResource theResource, Long theObject) {
		theResource.getResourceMetadata().put(IDao.RESOURCE_PID, theObject);
	}
}
