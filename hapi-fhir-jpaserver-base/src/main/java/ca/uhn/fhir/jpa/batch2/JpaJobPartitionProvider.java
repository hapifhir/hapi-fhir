/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The default JPA implementation, which uses {@link IRequestPartitionHelperSvc} and {@link IPartitionLookupSvc}
 * to compute the partition to run a batch2 job.
 * The latter will be used to handle cases when the job is configured to run against all partitions
 * (bulk system operation) and will return the actual list with all the configured partitions.
 */
public class JpaJobPartitionProvider implements IJobPartitionProvider {
	protected final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final IPartitionLookupSvc myPartitionLookupSvc;

	public JpaJobPartitionProvider(
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc, IPartitionLookupSvc thePartitionLookupSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myPartitionLookupSvc = thePartitionLookupSvc;
	}

	@Override
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation);
		if (!partitionId.isAllPartitions()) {
			return List.of(partitionId);
		}
		// handle (bulk) system operations that are typically configured with RequestPartitionId.allPartitions()
		// populate the actual list of all partitions
		List<RequestPartitionId> partitionIdList = myPartitionLookupSvc.listPartitions().stream()
				.map(PartitionEntity::toRequestPartitionId)
				.collect(Collectors.toList());
		partitionIdList.add(RequestPartitionId.defaultPartition());
		return partitionIdList;
	}
}
