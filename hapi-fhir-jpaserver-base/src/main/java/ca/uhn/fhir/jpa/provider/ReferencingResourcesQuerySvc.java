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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.stream.Stream;

/**
 * Queries for the resources that reference a given target resource. Every query runs across all partitions,
 * so referrers are still found when partitions are separate databases.
 */
// Created by Claude Opus 5
public class ReferencingResourcesQuerySvc {

	private final IResourceLinkDao myResourceLinkDao;
	private final IHapiTransactionService myHapiTransactionService;

	public ReferencingResourcesQuerySvc(
			IResourceLinkDao theResourceLinkDao, IHapiTransactionService theHapiTransactionService) {
		myResourceLinkDao = theResourceLinkDao;
		myHapiTransactionService = theHapiTransactionService;
	}

	public int countReferencingResourcesAcrossAllPartitions(IIdType theTargetId, RequestDetails theRequestDetails) {
		List<Integer> partialCounts = myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(RequestPartitionId.allPartitions())
				.searchList(partition -> List.of(myResourceLinkDao.countResourcesTargetingFhirTypeAndFhirId(
						theTargetId.getResourceType(), theTargetId.getIdPart())));

		return partialCounts.stream().mapToInt(Integer::intValue).sum();
	}

	public List<JpaPid> findReferencingResourcePidsAcrossAllPartitions(
			IIdType theTargetId, RequestDetails theRequestDetails) {
		return myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(RequestPartitionId.allPartitions())
				.searchList(partition -> {
					try (Stream<JpaPid> sourceIds = myResourceLinkDao.streamSourceIdsForTargetFhirId(
							theTargetId.getResourceType(), theTargetId.getIdPart())) {
						return sourceIds.toList();
					}
				});
	}
}
