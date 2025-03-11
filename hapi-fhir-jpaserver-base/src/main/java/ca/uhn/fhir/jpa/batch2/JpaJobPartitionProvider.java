/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.coordinator.DefaultJobPartitionProvider;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The default JPA implementation, which uses {@link IRequestPartitionHelperSvc} and {@link IPartitionLookupSvc}
 * to compute the {@link PartitionedUrl} list to run a batch2 job.
 * The latter will be used to handle cases when the job is configured to run against all partitions
 * (bulk system operation) and will return the actual list with all the configured partitions.
 */
@Deprecated
public class JpaJobPartitionProvider extends DefaultJobPartitionProvider {
	private final IPartitionLookupSvc myPartitionLookupSvc;

	public JpaJobPartitionProvider(
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc, IPartitionLookupSvc thePartitionLookupSvc) {
		super(theRequestPartitionHelperSvc);
		myPartitionLookupSvc = thePartitionLookupSvc;
	}

	public JpaJobPartitionProvider(
			FhirContext theFhirContext,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			MatchUrlService theMatchUrlService,
			IPartitionLookupSvc thePartitionLookupSvc) {
		super(theFhirContext, theRequestPartitionHelperSvc, theMatchUrlService);
		myPartitionLookupSvc = thePartitionLookupSvc;
	}

	@Override
	public List<RequestPartitionId> getAllPartitions() {
		return myPartitionLookupSvc.listPartitions().stream()
				.map(PartitionEntity::toRequestPartitionId)
				.collect(Collectors.toList());
	}
}
