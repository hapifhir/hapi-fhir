/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.SearchParameterUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for validating if a target exists
 * and if the requested export resource types contain search references to Patient.
 * In addition to that it also sets resource types in BulkExportJobParameters,
 * which are part of Patient compartment, when export resource types are not provided.
 */
public class BulkDataExportSupport {
	private final FhirContext myFhirContext;
	private final DaoRegistry myDaoRegistry;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperService;
	private Set<String> myCompartmentResources;

	public BulkDataExportSupport(
			@Nonnull FhirContext theFhirContext,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull IRequestPartitionHelperSvc theRequestPartitionHelperService) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myRequestPartitionHelperService = theRequestPartitionHelperService;
	}

	/**
	 * Throw ResourceNotFound if the target resources don't exist.
	 *
	 * @param theRequestDetails     the caller details
	 * @param theTargetResourceName the type of the target
	 * @param theIdParams           the id(s) to verify exist
	 */
	public void validateTargetsExists(
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull String theTargetResourceName,
			@Nonnull Iterable<IIdType> theIdParams) {
		if (theIdParams.iterator().hasNext()) {
			RequestPartitionId partitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForRead(
					theRequestDetails,
					theTargetResourceName,
					theIdParams.iterator().next());
			SystemRequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId(partitionId);
			for (IIdType nextId : theIdParams) {
				myDaoRegistry.getResourceDao(theTargetResourceName).read(nextId, requestDetails);
			}
		}
	}

	public void validateOrDefaultResourceTypesForGroupBulkExport(
			@Nonnull BulkExportJobParameters theBulkExportJobParameters) {
		if (CollectionUtils.isNotEmpty(theBulkExportJobParameters.getResourceTypes())) {
			validateResourceTypesAllContainPatientSearchParams(theBulkExportJobParameters.getResourceTypes());
		} else {
			// all patient resource types
			Set<String> groupTypes = new HashSet<>(getPatientCompartmentResources());

			// Add the forward reference resource types from the patients, e.g. Practitioner, Organization
			groupTypes.addAll(BulkDataExportUtil.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES);

			groupTypes.removeIf(t -> !myDaoRegistry.isResourceTypeSupported(t));
			theBulkExportJobParameters.setResourceTypes(groupTypes);
		}
	}

	public void validateResourceTypesAllContainPatientSearchParams(Collection<String> theResourceTypes) {
		if (theResourceTypes != null) {
			List<String> badResourceTypes = theResourceTypes.stream()
					.filter(resourceType ->
							!BulkDataExportUtil.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(
									resourceType))
					.filter(resourceType -> !getPatientCompartmentResources().contains(resourceType))
					.collect(Collectors.toList());

			if (!badResourceTypes.isEmpty()) {
				throw new InvalidRequestException(Msg.code(512)
						+ String.format(
								"Resource types [%s] are invalid for this type of export, as they do not contain search parameters that refer to patients.",
								String.join(",", badResourceTypes)));
			}
		}
	}

	public Set<String> getPatientCompartmentResources() {
		return getPatientCompartmentResources(myFhirContext);
	}

	Set<String> getPatientCompartmentResources(FhirContext theFhirContext) {
		if (myCompartmentResources == null) {
			myCompartmentResources =
					new HashSet<>(SearchParameterUtil.getAllResourceTypesThatAreInPatientCompartment(theFhirContext));
		}
		return myCompartmentResources;
	}
}
