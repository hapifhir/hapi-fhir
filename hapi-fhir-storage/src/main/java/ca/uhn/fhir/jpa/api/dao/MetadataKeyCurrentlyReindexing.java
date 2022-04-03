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
import org.hl7.fhir.instance.model.api.IBaseResource;

public final class MetadataKeyCurrentlyReindexing extends ResourceMetadataKeySupportingAnyResource<Boolean, Boolean> {
	private static final long serialVersionUID = 1L;

	MetadataKeyCurrentlyReindexing(String theValue) {
		super(theValue);
	}

	@Override
	public Boolean get(IAnyResource theResource) {
		return (Boolean) theResource.getUserData(IDao.CURRENTLY_REINDEXING.name());
	}

	@Override
	public Boolean get(IResource theResource) {
		return (Boolean) theResource.getResourceMetadata().get(IDao.CURRENTLY_REINDEXING);
	}

	@Override
	public void put(IAnyResource theResource, Boolean theObject) {
		theResource.setUserData(IDao.CURRENTLY_REINDEXING.name(), theObject);
	}

	public void put(IBaseResource theResource, Boolean theValue) {
		if (theResource instanceof IAnyResource) {
			put((IAnyResource) theResource, theValue);
		} else {
			put((IResource) theResource, theValue);
		}
	}

	@Override
	public void put(IResource theResource, Boolean theObject) {
		theResource.getResourceMetadata().put(IDao.CURRENTLY_REINDEXING, theObject);
	}
}
