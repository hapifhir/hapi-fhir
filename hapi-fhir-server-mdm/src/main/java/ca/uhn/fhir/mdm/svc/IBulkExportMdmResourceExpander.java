/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Set;

/**
 * Interface for mdm expanding Group resources on group bulk export and Patient resources on patient bulk export
 */
public interface IBulkExportMdmResourceExpander<T extends IResourcePersistentId> {

	/**
	 * For the Group resource with the given id, returns all the persistent id ofs
	 * the members of the group + the mdm matched resources to a member in the group
	 */
	Set<T> expandGroup(String groupResourceId, RequestPartitionId requestPartitionId);

	/**
	 * Expands a single patient ID to include all patients linked via MDM.
	 * For the given patient:
	 * - Finds the patient's golden resource (if MDM-linked)
	 * - Finds all other patients linked to that golden resource
	 * - Returns the complete set of patient ID strings in the MDM cluster
	 *
	 * @param thePatientId Patient ID to expand (e.g., "Patient/123")
	 * @param theRequestPartitionId Partition context for the request
	 * @return Set of String patient IDs including the original patient, golden resource, and all linked patients
	 *         (e.g., {"Patient/123", "Patient/456", "Patient/Golden"})
	 */
	Set<String> expandPatient(String thePatientId, RequestPartitionId theRequestPartitionId);

	/**
	 * annotates the given resource to be exported with the implementation specific extra information if applicable
	 */
	void annotateResource(IBaseResource resource);
}
