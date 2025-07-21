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
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.DateRangeUtil;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class Batch2DaoSvcImpl implements IBatch2DaoSvc {
	private static final org.slf4j.Logger ourLog = Logs.getBatchTroubleshootingLog();

	private final IResourceTableDao myResourceTableDao;

	private final IResourceLinkDao myResourceLinkDao;

	private final MatchUrlService myMatchUrlService;

	private final DaoRegistry myDaoRegistry;

	private final FhirContext myFhirContext;

	private final IHapiTransactionService myTransactionService;

	private final PartitionSettings myPartitionSettings;

	private final SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Override
	public boolean isAllResourceTypeSupported() {
		return true;
	}

	public Batch2DaoSvcImpl(
			IResourceTableDao theResourceTableDao,
			IResourceLinkDao theResourceLinkDao,
			MatchUrlService theMatchUrlService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext,
			IHapiTransactionService theTransactionService,
			PartitionSettings thePartitionSettings,
			SearchBuilderFactory theSearchBuilderFactory) {
		myResourceTableDao = theResourceTableDao;
		myResourceLinkDao = theResourceLinkDao;
		myMatchUrlService = theMatchUrlService;
		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myTransactionService = theTransactionService;
		myPartitionSettings = thePartitionSettings;
		mySearchBuilderFactory = theSearchBuilderFactory;
	}

	@Override
	public IResourcePidStream fetchResourceIdStream(
			Date theStart, Date theEnd, RequestPartitionId theRequestPartitionId, String theUrl) {

		if (StringUtils.isBlank(theUrl)) {
			// first scenario
			return makeStreamResult(
					theRequestPartitionId, () -> streamResourceIdsNoUrl(theStart, theEnd, theRequestPartitionId));
		} else {
			return makeStreamResult(
					theRequestPartitionId,
					() -> streamResourceIdsWithUrl(theStart, theEnd, theUrl, theRequestPartitionId));
		}
	}

	@Override
	public Stream<IdDt> streamSourceIdsThatReferenceTargetId(IIdType theTargetId) {
		return myResourceLinkDao.streamSourceIdsForTargetFhirId(theTargetId.getResourceType(), theTargetId.getIdPart());
	}

	private Stream<TypedResourcePid> streamResourceIdsWithUrl(
			Date theStart, Date theEnd, String theUrl, RequestPartitionId theRequestPartitionId) {
		validateUrl(theUrl);

		String resourceType = theUrl.substring(0, theUrl.indexOf('?'));

		// Search in all partitions if no partition is provided
		ourLog.debug("No partition id detected in request - searching all partitions");
		RequestPartitionId thePartitionId = defaultIfNull(theRequestPartitionId, RequestPartitionId.allPartitions());

		SearchParameterMap searchParamMap;
		SystemRequestDetails request = new SystemRequestDetails();
		request.setRequestPartitionId(thePartitionId);

		if (resourceType.isBlank()) {
			searchParamMap = parseQuery(theUrl, null);
		} else {
			searchParamMap = parseQuery(theUrl);
		}

		searchParamMap.setLastUpdated(DateRangeUtil.narrowDateRange(searchParamMap.getLastUpdated(), theStart, theEnd));

		if (resourceType.isBlank()) {
			return searchForResourceIdsAndType(thePartitionId, request, searchParamMap);
		}

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

		return dao.searchForIdStream(searchParamMap, request, null).map(pid -> new TypedResourcePid(resourceType, pid));
	}

	/**
	 * Since the resource type is not specified, query the DB for resources matching the params and return resource ID and type
	 *
	 * @param theRequestPartitionId the partition to search on
	 * @param theRequestDetails the theRequestDetails details
	 * @param theSearchParams the search params
	 * @return Stream of typed resource pids
	 */
	private Stream<TypedResourcePid> searchForResourceIdsAndType(
			RequestPartitionId theRequestPartitionId,
			SystemRequestDetails theRequestDetails,
			SearchParameterMap theSearchParams) {
		ISearchBuilder<JpaPid> builder = mySearchBuilderFactory.newSearchBuilder(null, null);
		return myTransactionService
				.withRequest(theRequestDetails)
				.search(() -> builder.createQueryStream(
						theSearchParams,
						new SearchRuntimeDetails(
								theRequestDetails, UUID.randomUUID().toString()),
						theRequestDetails,
						theRequestPartitionId))
				.map(pid -> new TypedResourcePid(pid.getResourceType(), pid));
	}

	private static TypedResourcePid typedPidFromQueryArray(Object[] thePidTypeDateArray) {
		JpaPid pid = (JpaPid) thePidTypeDateArray[0];
		String resourceType = (String) thePidTypeDateArray[1];
		return new TypedResourcePid(resourceType, pid);
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

	/**
	 * At the moment there is no use-case for this method.
	 * This can be cleaned up at a later point in time if there is no use for it.
	 */
	@Nonnull
	private Stream<TypedResourcePid> streamResourceIdsNoUrl(
			Date theStart, Date theEnd, RequestPartitionId theRequestPartitionId) {
		Integer defaultPartitionId = myPartitionSettings.getDefaultPartitionId();
		Stream<Object[]> rowStream;

		if (theRequestPartitionId == null || theRequestPartitionId.isAllPartitions()) {
			ourLog.debug("Search for resources - all partitions");
			rowStream = myResourceTableDao.streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(
					theStart, theEnd);
		} else if (theRequestPartitionId.isPartition(defaultPartitionId)) {
			ourLog.debug("Search for resources - default partition");
			rowStream =
					myResourceTableDao
							.streamIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldestForDefaultPartition(
									theStart, theEnd, defaultPartitionId);
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

		return parseQuery(theUrl, def);
	}

	@Nonnull
	private SearchParameterMap parseQuery(
			String theUrl, @Nullable RuntimeResourceDefinition theRuntimeResourceDefinition) {
		SearchParameterMap searchParamMap = myMatchUrlService.translateMatchUrl(theUrl, theRuntimeResourceDefinition);
		// this matches idx_res_type_del_updated
		searchParamMap.setSort(new SortSpec(Constants.PARAM_LASTUPDATED).setChain(new SortSpec(Constants.PARAM_PID)));
		// TODO this limits us to 2G resources.
		searchParamMap.setLoadSynchronousUpTo(Integer.MAX_VALUE);
		return searchParamMap;
	}
}
