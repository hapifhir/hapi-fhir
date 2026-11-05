/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.storage;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Optional;

public class PreviousVersionReader<T extends IBaseResource> {
	private final IFhirResourceDao<T> myDao;

	public PreviousVersionReader(IFhirResourceDao<T> theDao) {
		myDao = theDao;
	}

	public Optional<T> readPreviousVersion(T theResource, RequestDetails theRequestDetails) {
		return readPreviousVersion(theResource, false, theRequestDetails);
	}

	/**
	 * Read the previous version of the given resource.
	 *
	 * @param theResource the resource whose previous version should be read
	 * @param theDeletedOk whether a deleted previous version may be returned
	 * @param theRequestDetails the request details associated with the request
	 * @return the previous version, or empty if there is none
	 */
	public Optional<T> readPreviousVersion(T theResource, boolean theDeletedOk, RequestDetails theRequestDetails) {
		Long currentVersion = theResource.getIdElement().getVersionIdPartAsLong();
		if (currentVersion == null || currentVersion == 1L) {
			return Optional.empty();
		}
		long previousVersion = currentVersion - 1L;
		IIdType previousId = theResource.getIdElement().withVersion(Long.toString(previousVersion));
		try {
			return Optional.ofNullable(myDao.read(previousId, theRequestDetails, theDeletedOk));
		} catch (ResourceGoneException e) {
			// This will only happen in the case where theDeleteOk = false
			return Optional.empty();
		}
	}
}
