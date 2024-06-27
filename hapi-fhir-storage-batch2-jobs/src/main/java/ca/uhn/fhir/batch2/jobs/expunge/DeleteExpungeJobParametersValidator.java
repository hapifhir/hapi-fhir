/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.expunge;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.jpa.api.svc.IDeleteExpungeSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class DeleteExpungeJobParametersValidator implements IJobParametersValidator<DeleteExpungeJobParameters> {
	private final IUrlListValidator myUrlListValidator;
	private final IDeleteExpungeSvc<?> myDeleteExpungeSvc;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public DeleteExpungeJobParametersValidator(
			IUrlListValidator theUrlListValidator,
			IDeleteExpungeSvc<?> theDeleteExpungeSvc,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myUrlListValidator = theUrlListValidator;
		myDeleteExpungeSvc = theDeleteExpungeSvc;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull DeleteExpungeJobParameters theParameters) {

		// Make sure cascade is supported if requested
		if (theParameters.isCascade() && !myDeleteExpungeSvc.isCascadeSupported()) {
			return List.of("Cascading delete is not supported on this server");
		}

		// Verify that the user has access to all requested partitions
		myRequestPartitionHelperSvc.validateHasPartitionPermissions(
				theRequestDetails, null, theParameters.getRequestPartitionId());

		for (PartitionedUrl partitionedUrl : theParameters.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			ValidateUtil.isTrueOrThrowInvalidRequest(
					url.matches("[a-zA-Z]+\\?.*"),
					"Delete expunge URLs must be in the format [resourceType]?[parameters]");
			if (partitionedUrl.getRequestPartitionId() != null) {
				myRequestPartitionHelperSvc.validateHasPartitionPermissions(
						theRequestDetails, null, partitionedUrl.getRequestPartitionId());
			}
		}
		return myUrlListValidator.validatePartitionedUrls(theParameters.getPartitionedUrls());
	}
}
