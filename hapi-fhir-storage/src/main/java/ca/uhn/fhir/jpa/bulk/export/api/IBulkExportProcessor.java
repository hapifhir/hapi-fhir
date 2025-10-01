/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.bulk.export.api;

import ca.uhn.fhir.jpa.bulk.export.model.ExpandPatientIdsParams;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface IBulkExportProcessor<T extends IResourcePersistentId> {

	/**
	 * For fetching PIDs of resources
	 * @param theParams
	 * @return
	 */
	Iterator<T> getResourcePidIterator(ExportPIDIteratorParameters theParams);

	/**
	 * Expand out patient ids based on group, patient ids, and whether or not mdm expansion is
	 * desired
	 * @param theParams - parameters for doing the expansion of patient ids
	 * @return a list of patient ids (or an empty list if not applicable)
	 */
	@Nonnull
	Set<T> expandPatientIdList(ExpandPatientIdsParams theParams);

	/**
	 * Does the MDM expansion of resources if necessary
	 * @param theResources - the list of resources to expand
	 */
	void expandMdmResources(List<IBaseResource> theResources);
}
