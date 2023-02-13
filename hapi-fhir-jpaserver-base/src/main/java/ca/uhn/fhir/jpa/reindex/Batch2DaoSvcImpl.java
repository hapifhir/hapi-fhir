package ca.uhn.fhir.jpa.reindex;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.pid.EmptyResourcePidList;
import ca.uhn.fhir.jpa.api.pid.HomogeneousResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.pid.MixedResourcePidList;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.DateRangeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

public class Batch2DaoSvcImpl implements IBatch2DaoSvc {

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public boolean isAllResourceTypeSupported() {
		return true;
	}

	@Override
	@Transactional
	public IResourcePidList fetchResourceIdsPage(Date theStart, Date theEnd, @Nonnull Integer thePageSize, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theUrl) {

		if (theUrl == null) {
			return fetchResourceIdsPageNoUrl(theStart, theEnd, thePageSize, theRequestPartitionId);
		} else {
			return fetchResourceIdsPageWithUrl(theStart, theEnd, thePageSize, theUrl, theRequestPartitionId);
		}
	}

	private IResourcePidList fetchResourceIdsPageWithUrl(Date theStart, Date theEnd, int thePageSize, String theUrl, RequestPartitionId theRequestPartitionId) {

		String resourceType = theUrl.substring(0, theUrl.indexOf('?'));
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(resourceType);

		SearchParameterMap searchParamMap = myMatchUrlService.translateMatchUrl(theUrl, def);
		searchParamMap.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		DateRangeParam chunkDateRange = DateRangeUtil.narrowDateRange(searchParamMap.getLastUpdated(), theStart, theEnd);
		searchParamMap.setLastUpdated(chunkDateRange);
		searchParamMap.setCount(thePageSize);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		SystemRequestDetails request = new SystemRequestDetails();
		request.setRequestPartitionId(theRequestPartitionId);
		List<IResourcePersistentId> ids = dao.searchForIds(searchParamMap, request);

		Date lastDate = null;
		if (isNotEmpty(ids)) {
			IResourcePersistentId lastResourcePersistentId = ids.get(ids.size() - 1);
			lastDate = dao.readByPid(lastResourcePersistentId, true).getMeta().getLastUpdated();
		}

		return new HomogeneousResourcePidList(resourceType, ids, lastDate);
	}

	@Nonnull
	private IResourcePidList fetchResourceIdsPageNoUrl(Date theStart, Date theEnd, int thePagesize, RequestPartitionId theRequestPartitionId) {
		Pageable page = Pageable.ofSize(thePagesize);
		Slice<Object[]> slice;
		if (theRequestPartitionId == null || theRequestPartitionId.isAllPartitions()) {
			slice = myResourceTableDao.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(page, theStart, theEnd);
		} else if (theRequestPartitionId.isDefaultPartition()) {
			slice = myResourceTableDao.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(page, theStart, theEnd);
		} else {
			slice = myResourceTableDao.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForPartitionIds(page, theStart, theEnd, theRequestPartitionId.getPartitionIds());
		}

		List<Object[]> content = slice.getContent();
		if (content.isEmpty()) {
			return new EmptyResourcePidList();
		}

		List<IResourcePersistentId> ids = content
			.stream()
			.map(t -> JpaPid.fromId((Long) t[0]))
			.collect(Collectors.toList());

		List<String> types = content
			.stream()
			.map(t -> (String) t[1])
			.collect(Collectors.toList());

		Date lastDate = (Date) content.get(content.size() - 1)[2];

		return new MixedResourcePidList(types, ids, lastDate);
	}
}
