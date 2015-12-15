package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.entity.TagTypeEnum;
import ca.uhn.fhir.jpa.util.DeleteConflict;

public interface IJpaFhirResourceDao<T extends IBaseResource> extends IFhirResourceDao<T> {

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 */
	ResourceTable delete(IIdType theResource, List<DeleteConflict> theDeleteConflictsListToPopulate);

	/**
	 * This method does not throw an exception if there are delete conflicts, but populates them
	 * in the provided list
	 * @return 
	 */
	List<ResourceTable> deleteByUrl(String theUrl, List<DeleteConflict> theDeleteConflictsListToPopulate);

	BaseHasResource readEntity(IIdType theId);

	/**
	 * @param theCheckForForcedId
	 *           If true, this method should fail if the requested ID contains a numeric PID which exists, but is
	 *           obscured by a "forced ID" so should not exist as far as the outside world is concerned.
	 */
	BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId);

	/**
	 * Updates index tables associated with the given resource. Does not create a new
	 * version or update the resource's update time.
	 */
	void reindex(T theResource, ResourceTable theEntity);

}
