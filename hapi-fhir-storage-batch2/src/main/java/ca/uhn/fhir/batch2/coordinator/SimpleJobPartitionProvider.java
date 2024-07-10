/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

/**
 * Basic implementation which provides the partition list for a certain request which is composed of a single partition.
 */
public class SimpleJobPartitionProvider implements IJobPartitionProvider {
	protected final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public SimpleJobPartitionProvider(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation);
		return List.of(partitionId);
	}
}
