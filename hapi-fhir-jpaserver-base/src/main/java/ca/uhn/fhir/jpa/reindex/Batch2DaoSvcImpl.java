/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.pid.EmptyResourcePidList;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.pid.MixedResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Batch2DaoSvcImpl implements IBatch2DaoSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Batch2DaoSvcImpl.class);

	private final IResourceTableDao myResourceTableDao;

	private final MatchUrlService myMatchUrlService;

	private final DaoRegistry myDaoRegistry;

	private final FhirContext myFhirContext;

	private final IHapiTransactionService myTransactionService;

	private final JpaStorageSettings myJpaStorageSettings;

	@Override
	public boolean isAllResourceTypeSupported() {
		return true;
	}

	public Batch2DaoSvcImpl(
			IResourceTableDao theResourceTableDao,
			MatchUrlService theMatchUrlService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IHapiTransactionService theTransactionService,
			JpaStorageSettings theJpaStorageSettings) {
		myResourceTableDao = theResourceTableDao;
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myTransactionService = theTransactionService;
		myJpaStorageSettings = theJpaStorageSettings;
	}

	@Override
	public IResourcePidList fetchResourceIdsPage(
			Date theStart, Date theEnd, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theUrl) {
		return myTransactionService
				.withSystemRequest()
				.withRequestPartitionId(theRequestPartitionId)
				.execute(() -> {
					if (theUrl == null) {
						return fetchResourceIdsPageNoUrl(theStart, theEnd, theRequestPartitionId);
					} else {
						return fetchResourceIdsPageWithUrl(theEnd, theUrl, theRequestPartitionId);
					}
				});
	}

	@Nonnull
	private HomogeneousResourcePidList fetchResourceIdsPageWithUrl(
			Date theEnd, @Nonnull String theUrl, @Nullable RequestPartitionId theRequestPartitionId) {
		if (!theUrl.contains("?")) {
			throw new InternalErrorException(Msg.code(2422) + "this should never happen: URL is missing a '?'");
		}

		final Integer internalSynchronousSearchSize = myJpaStorageSettings.getInternalSynchronousSearchSize();

		if (internalSynchronousSearchSize == null || internalSynchronousSearchSize <= 0) {
			throw new InternalErrorException(Msg.code(2423)
					+ "this should never happen: internalSynchronousSearchSize is null or less than or equal to 0");
		}

		List<IResourcePersistentId> currentIds = fetchResourceIdsPageWithUrl(0, theUrl, theRequestPartitionId);
		ourLog.debug("FIRST currentIds: {}", currentIds.size());

		final List<IResourcePersistentId> allIds = new ArrayList<>(currentIds);

		while (internalSynchronousSearchSize < currentIds.size()) {
			// Ensure the offset is set to the last ID in the cumulative List, otherwise, we'll be stuck in an infinite
			// loop here:
			currentIds = fetchResourceIdsPageWithUrl(allIds.size(), theUrl, theRequestPartitionId);
			ourLog.debug("NEXT currentIds: {}", currentIds.size());

			allIds.addAll(currentIds);
		}

		final String resourceType = theUrl.substring(0, theUrl.indexOf('?'));

		return new HomogeneousResourcePidList(resourceType, allIds, theEnd, theRequestPartitionId);
	}

	private List<IResourcePersistentId> fetchResourceIdsPageWithUrl(
			int theOffset, String theUrl, RequestPartitionId theRequestPartitionId) {
		String resourceType = theUrl.substring(0, theUrl.indexOf('?'));
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(resourceType);

		SearchParameterMap searchParamMap = myMatchUrlService.translateMatchUrl(theUrl, def);
		searchParamMap.setSort(new SortSpec(Constants.PARAM_ID, SortOrderEnum.ASC));
		searchParamMap.setOffset(theOffset);
		searchParamMap.setLoadSynchronousUpTo(myJpaStorageSettings.getInternalSynchronousSearchSize() + 1);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		SystemRequestDetails request = new SystemRequestDetails();
		request.setRequestPartitionId(theRequestPartitionId);

		return dao.searchForIds(searchParamMap, request);
	}

	@Nonnull
	private IResourcePidList fetchResourceIdsPageNoUrl(
			Date theStart, Date theEnd, RequestPartitionId theRequestPartitionId) {
		final Pageable page = Pageable.unpaged();
		Slice<Object[]> slice;
		if (theRequestPartitionId == null || theRequestPartitionId.isAllPartitions()) {
			slice = myResourceTableDao.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(
					page, theStart, theEnd);
		} else if (theRequestPartitionId.isDefaultPartition()) {
			slice =
					myResourceTableDao
							.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(
									page, theStart, theEnd);
		} else {
			slice =
					myResourceTableDao
							.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForPartitionIds(
									page, theStart, theEnd, theRequestPartitionId.getPartitionIds());
		}

		List<Object[]> content = slice.getContent();
		if (content.isEmpty()) {
			return new EmptyResourcePidList();
		}

		List<IResourcePersistentId> ids =
				content.stream().map(t -> JpaPid.fromId((Long) t[0])).collect(Collectors.toList());

		List<String> types = content.stream().map(t -> (String) t[1]).collect(Collectors.toList());

		Date lastDate = (Date) content.get(content.size() - 1)[2];

		return new MixedResourcePidList(types, ids, lastDate, theRequestPartitionId);
	}
}
