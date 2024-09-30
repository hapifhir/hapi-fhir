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
package ca.uhn.fhir.batch2.jobs.rehoming;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.IUrlListValidator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class RehomeJobParametersValidator implements IJobParametersValidator<RehomeJobParameters> {
	private final IUrlListValidator myUrlListValidator;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public RehomeJobParametersValidator(
			IUrlListValidator theUrlListValidator,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myUrlListValidator = theUrlListValidator;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull RehomeJobParameters theParameters) {

		// Verify that the user has access to all requested partitions
		for (PartitionedUrl partitionedUrl : theParameters.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			ValidateUtil.isTrueOrThrowInvalidRequest(
					url.matches("[a-zA-Z]+\\?.*"),
					"Rehoming URLs must be in the format [resourceType]?[parameters]");

			if (partitionedUrl.getRequestPartitionId() != null) {
				myRequestPartitionHelperSvc.validateHasPartitionPermissions(
						theRequestDetails, null, partitionedUrl.getRequestPartitionId());
			}
		}
		return myUrlListValidator.validateUrls(theParameters.getUrls());
	}
}
