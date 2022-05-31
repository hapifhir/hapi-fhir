package ca.uhn.fhir.jpa.reindex;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.DateRangeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceReindexSvcImpl implements IResourceReindexSvc {

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
	public IdChunk fetchResourceIdsPage(Date theStart, Date theEnd, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theUrl) {

		int pageSize = 20000;
		if (theUrl == null) {
			return fetchResourceIdsPageNoUrl(theStart, theEnd, pageSize, theRequestPartitionId);
		} else {
			return fetchResourceIdsPageWithUrl(theStart, theEnd, pageSize, theUrl, theRequestPartitionId);
		}
	}

	private IdChunk fetchResourceIdsPageWithUrl(Date theStart, Date theEnd, int thePageSize, String theUrl, RequestPartitionId theRequestPartitionId) {

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
		List<ResourcePersistentId> ids = dao.searchForIds(searchParamMap, request);

		// just a list of the same size where every element is the same resource type
		List<String> resourceTypes = ids
			.stream()
			.map(t -> resourceType)
			.collect(Collectors.toList());

		Date lastDate = null;
		if (ids.size() > 0) {
			lastDate = dao.readByPid(ids.get(ids.size() - 1)).getMeta().getLastUpdated();
		}

		return new IdChunk(ids, resourceTypes, lastDate);
	}

	@Nonnull
	private IdChunk fetchResourceIdsPageNoUrl(Date theStart, Date theEnd, int thePagesize, RequestPartitionId theRequestPartitionId) {
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
			return new IdChunk(Collections.emptyList(), Collections.emptyList(), null);
		}

		List<ResourcePersistentId> ids = content
			.stream()
			.map(t -> new ResourcePersistentId(t[0]))
			.collect(Collectors.toList());

		List<String> types = content
			.stream()
			.map(t -> (String) t[1])
			.collect(Collectors.toList());

		Date lastDate = (Date) content.get(content.size() - 1)[2];

		return new IdChunk(ids, types, lastDate);
	}
}
