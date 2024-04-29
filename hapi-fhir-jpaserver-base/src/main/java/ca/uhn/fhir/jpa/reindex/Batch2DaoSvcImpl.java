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
package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.jpa.api.pid.StreamTemplate;
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.jpa.api.pid.TypedResourceStream;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.DateRangeUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Batch2DaoSvcImpl implements IBatch2DaoSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Batch2DaoSvcImpl.class);

	private final IResourceTableDao myResourceTableDao;

	private final MatchUrlService myMatchUrlService;

	private final DaoRegistry myDaoRegistry;

	private final FhirContext myFhirContext;

	private final IHapiTransactionService myTransactionService;

	@Override
	public boolean isAllResourceTypeSupported() {
		return true;
	}

	public Batch2DaoSvcImpl(
			IResourceTableDao theResourceTableDao,
			MatchUrlService theMatchUrlService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IHapiTransactionService theTransactionService) {
		myResourceTableDao = theResourceTableDao;
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myTransactionService = theTransactionService;
	}

	@Override
	public IResourcePidStream fetchResourceIdStream(
			Date theStart, Date theEnd, RequestPartitionId theRequestPartitionId, String theUrl) {
		if (theUrl == null) {
			return makeStreamResult(
					theRequestPartitionId, () -> streamResourceIdsNoUrl(theStart, theEnd, theRequestPartitionId));
		} else {
			return makeStreamResult(
					theRequestPartitionId,
					() -> streamResourceIdsWithUrl(theStart, theEnd, theUrl, theRequestPartitionId));
		}
	}

	private Stream<TypedResourcePid> streamResourceIdsWithUrl(
			Date theStart, Date theEnd, String theUrl, RequestPartitionId theRequestPartitionId) {
		validateUrl(theUrl);

		SearchParameterMap searchParamMap = parseQuery(theUrl);
		searchParamMap.setLastUpdated(DateRangeUtil.narrowDateRange(searchParamMap.getLastUpdated(), theStart, theEnd));

		String resourceType = theUrl.substring(0, theUrl.indexOf('?'));
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

		SystemRequestDetails request = new SystemRequestDetails().setRequestPartitionId(theRequestPartitionId);

		return dao.searchForIdStream(searchParamMap, request, null).map(pid -> new TypedResourcePid(resourceType, pid));
	}

	private static TypedResourcePid typedPidFromQueryArray(Object[] thePidTypeDateArray) {
		String resourceType = (String) thePidTypeDateArray[1];
		Long pid = (Long) thePidTypeDateArray[0];
		return new TypedResourcePid(resourceType, JpaPid.fromId(pid));
	}

	@Nonnull
	private TypedResourceStream makeStreamResult(
			RequestPartitionId theRequestPartitionId, Supplier<Stream<TypedResourcePid>> streamSupplier) {

		IHapiTransactionService.IExecutionBuilder txSettings =
				myTransactionService.withSystemRequest().withRequestPartitionId(theRequestPartitionId);

		StreamTemplate<TypedResourcePid> streamTemplate =
				StreamTemplate.fromSupplier(streamSupplier).withTransactionAdvice(txSettings);

		return new TypedResourceStream(theRequestPartitionId, streamTemplate);
	}

	@Nonnull
	private Stream<TypedResourcePid> streamResourceIdsNoUrl(
			Date theStart, Date theEnd, RequestPartitionId theRequestPartitionId) {
		Stream<Object[]> rowStream;
		if (theRequestPartitionId == null || theRequestPartitionId.isAllPartitions()) {
			ourLog.debug("Search for resources - all partitions");
			rowStream = myResourceTableDao.streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(
					theStart, theEnd);
		} else if (theRequestPartitionId.isDefaultPartition()) {
			ourLog.debug("Search for resources - default partition");
			rowStream =
					myResourceTableDao
							.streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(
									theStart, theEnd);
		} else {
			ourLog.debug("Search for resources - partition {}", theRequestPartitionId);
			rowStream =
					myResourceTableDao
							.streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForPartitionIds(
									theStart, theEnd, theRequestPartitionId.getPartitionIds());
		}

		return rowStream.map(Batch2DaoSvcImpl::typedPidFromQueryArray);
	}

	@Deprecated(since = "6.11", forRemoval = true) // delete once the default method in the interface is gone.
	@Override
	public IResourcePidList fetchResourceIdsPage(
			Date theStart, Date theEnd, @Nullable RequestPartitionId theRequestPartitionId, @Nullable String theUrl) {
		Validate.isTrue(false, "Unimplemented");
		return null;
	}

	private static void validateUrl(@Nonnull String theUrl) {
		if (!theUrl.contains("?")) {
			throw new InternalErrorException(Msg.code(2422) + "this should never happen: URL is missing a '?'");
		}
	}

	@Nonnull
	private SearchParameterMap parseQuery(String theUrl) {
		String resourceType = theUrl.substring(0, theUrl.indexOf('?'));
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(resourceType);

		SearchParameterMap searchParamMap = myMatchUrlService.translateMatchUrl(theUrl, def);
		// this matches idx_res_type_del_updated
		searchParamMap.setSort(new SortSpec(Constants.PARAM_LASTUPDATED).setChain(new SortSpec(Constants.PARAM_PID)));
		// TODO this limits us to 2G resources.
		searchParamMap.setLoadSynchronousUpTo(Integer.MAX_VALUE);
		return searchParamMap;
	}
}
