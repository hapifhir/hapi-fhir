/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.StreamTemplate;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.jpa.api.pid.TypedResourceStream;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.util.DateRangeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GoldenResourceSearchSvcImpl implements IGoldenResourceSearchSvc {
	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Override
	@Transactional
	public IResourcePidStream fetchGoldenResourceIdStream(
			Date theStart,
			Date theEnd,
			@Nullable RequestPartitionId theRequestPartitionId,
			@Nonnull String theResourceType) {

		IHapiTransactionService.IExecutionBuilder txSettings =
				myTransactionService.withSystemRequest().withRequestPartitionId(theRequestPartitionId);

		Supplier<Stream<TypedResourcePid>> streamSupplier =
				() -> fetchResourceIdsPageWithResourceType(theStart, theEnd, theResourceType, theRequestPartitionId);

		StreamTemplate<TypedResourcePid> streamTemplate =
				StreamTemplate.fromSupplier(streamSupplier).withTransactionAdvice(txSettings);

		return new TypedResourceStream(theRequestPartitionId, streamTemplate);
	}

	private Stream<TypedResourcePid> fetchResourceIdsPageWithResourceType(
			Date theStart, Date theEnd, String theResourceType, RequestPartitionId theRequestPartitionId) {

		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(theResourceType);

		SearchParameterMap searchParamMap = myMatchUrlService.translateMatchUrl(theResourceType, def);
		searchParamMap.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		DateRangeParam chunkDateRange =
				DateRangeUtil.narrowDateRange(searchParamMap.getLastUpdated(), theStart, theEnd);
		searchParamMap.setLastUpdated(chunkDateRange);

		TokenOrListParam goldenRecordStatusToken = new TokenOrListParam()
				.add(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED)
				.add(MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS, MdmConstants.CODE_GOLDEN_RECORD);
		searchParamMap.add(Constants.PARAM_TAG, goldenRecordStatusToken);

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceType);
		SystemRequestDetails request = new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId);

		return dao.searchForIdStream(searchParamMap, request, null)
				.map(pid -> new TypedResourcePid(theResourceType, pid));
	}
}
