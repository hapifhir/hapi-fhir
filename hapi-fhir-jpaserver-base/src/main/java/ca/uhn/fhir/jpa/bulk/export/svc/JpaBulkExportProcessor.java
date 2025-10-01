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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExpandPatientIdsParams;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.svc.MdmExpandersHolder;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.api.server.storage.BaseResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.TaskChunker;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JpaBulkExportProcessor implements IBulkExportProcessor<JpaPid> {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBulkExportProcessor.class);

	public static final int QUERY_CHUNK_SIZE = 100;
	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES =
			List.of("Practitioner", "Organization");

	private FhirContext myContext;
	private BulkExportHelperService myBulkExportHelperSvc;
	private JpaStorageSettings myStorageSettings;
	private DaoRegistry myDaoRegistry;
	protected SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	private IIdHelperService<JpaPid> myIdHelperService;
	private EntityManager myEntityManager;
	private IHapiTransactionService myHapiTransactionService;
	private ISearchParamRegistry mySearchParamRegistry;
	private MdmExpandersHolder myMdmExpandersHolder;
	private MatchUrlService myMatchUrlService;

	@Autowired
	public JpaBulkExportProcessor(
			FhirContext theContext,
			BulkExportHelperService theBulkExportHelperSvc,
			JpaStorageSettings theStorageSettings,
			DaoRegistry theDaoRegistry,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			IIdHelperService<JpaPid> theIdHelperService,
			EntityManager theEntityManager,
			IHapiTransactionService theHapiTransactionService,
			ISearchParamRegistry theSearchParamRegistry,
			MatchUrlService theMatchUrlService,
			MdmExpandersHolder theMdmExpandersHolder) {
		myContext = theContext;
		myBulkExportHelperSvc = theBulkExportHelperSvc;
		myStorageSettings = theStorageSettings;
		myDaoRegistry = theDaoRegistry;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myIdHelperService = theIdHelperService;
		myEntityManager = theEntityManager;
		myHapiTransactionService = theHapiTransactionService;
		mySearchParamRegistry = theSearchParamRegistry;
		myMatchUrlService = theMatchUrlService;
		myMdmExpandersHolder = theMdmExpandersHolder;
	}

	@Override
	public Iterator<JpaPid> getResourcePidIterator(ExportPIDIteratorParameters theParams) {
		return myHapiTransactionService
				.withSystemRequest()
				.withRequestPartitionId(theParams.getPartitionIdOrAllPartitions())
				.readOnly()
				.execute(() -> {
					String resourceType = theParams.getResourceType();
					String jobId = theParams.getInstanceId();
					String chunkId = theParams.getChunkId();
					RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);

					LinkedHashSet<JpaPid> pids;
					if (theParams.getExportStyle() == BulkExportJobParameters.ExportStyle.PATIENT) {
						pids = getPidsForPatientStyleExport(theParams, resourceType, jobId, chunkId, def);
					} else if (theParams.getExportStyle() == BulkExportJobParameters.ExportStyle.GROUP) {
						pids = getPidsForGroupStyleExport(theParams, resourceType, def);
					} else {
						pids = getPidsForSystemStyleExport(theParams, jobId, chunkId, def);
					}

					ourLog.debug("Finished expanding resource pids to export, size is {}", pids.size());
					return pids.iterator();
				});
	}

	@Nonnull
	@Override
	public Set<JpaPid> expandPatientIdList(ExpandPatientIdsParams theParams) {
		return myHapiTransactionService
				.withSystemRequest()
				.withRequestPartitionId(theParams.getRequestPartitionId())
				.readOnly()
				.execute(() -> {
					Set<JpaPid> patientPids = new HashSet<>();
					switch (theParams.getExportStyle()) {
						case GROUP -> {
							populateListOfPatientIdsForGroupExport(theParams, patientPids);
						}
						case PATIENT -> {
							populateListOfPatientIdsForPatientExport(theParams, patientPids);
						}
						default -> {
							// nothing to do (patients parameter is not supported for system level exports)
						}
					}

					return patientPids;
				});
	}

	private void populateListOfPatientIdsForPatientExport(ExpandPatientIdsParams theParams, Set<JpaPid> theJpaPids)
			throws IOException {
		RequestPartitionId partitionId = theParams.getRequestPartitionId();
		List<SearchParameterMap> maps = makeSearchParameterMapsForPatientExport(theParams);

		Set<JpaPid> pids =
				new HashSet<>(getPatientPidsUsingSearchMaps(maps, null, null, theParams.getRequestPartitionId()));

		/*
		 * the pids do not necessarily have the associated IIdType attached ot them.
		 * But we need them, so we'll expand them out here.
		 * This will be fast if the pid is still in the cache (which it should be)
		 */
		myIdHelperService.fillOutPids(pids, myContext);

		if (theParams.isToDoMdmExpansion()) {
			Collection<IIdType> patientIds = pids.stream()
					.map(IResourcePersistentId::getAssociatedResourceId)
					.collect(Collectors.toList());

			// MDM expansion requested -> we'll have MdmExpansionHolder do the work
			// of fetching mdm linked patients as well as converting all of them to
			// JpaPid
			Set<JpaPid> resolvedAndMdmExpanded = myMdmExpandersHolder
					.getBulkExportMDMResourceExpanderInstance()
					.expandPatients(patientIds, partitionId);
			theJpaPids.addAll(resolvedAndMdmExpanded);
		} else {
			theJpaPids.addAll(pids);
		}
	}

	private void populateListOfPatientIdsForGroupExport(ExpandPatientIdsParams theParams, Set<JpaPid> thePatientPids)
			throws IOException {
		RequestPartitionId partitionId = theParams.getRequestPartitionId();

		// we have to apply the parameters to filter
		// the patients we want (ie, not all the members of the group necessarily fit the filters)
		// so first we get a set of SP maps
		RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		List<SearchParameterMap> maps = myBulkExportHelperSvc.createSearchParameterMapsForResourcetype(
				def, theParams.getFilters(), theParams.getStartDate(), theParams.getEndDate(), true);

		// use those maps to get the patient ids we care about
		List<JpaPid> pids =
				getPatientPidsUsingSearchMaps(maps, theParams.getGroupId(), null, theParams.getRequestPartitionId());

		/*
		 * and fill them out.
		 *
		 * Like with patient export above, the JpaPids here might not have
		 * their associated resource id populated.
		 * But since we need it, we'll "fill them out" here.
		 * (should be fast, because the ids should be in the cache)
		 */
		Set<JpaPid> pidsSet = new HashSet<>(pids);
		myIdHelperService.fillOutPids(pidsSet, myContext);

		Set<IIdType> patientIds = pidsSet.stream()
				.map(BaseResourcePersistentId::getAssociatedResourceId)
				.collect(Collectors.toSet());

		if (theParams.isToDoMdmExpansion()) {
			// expand them out and add them to our list
			Set<JpaPid> jpaPids = myMdmExpandersHolder
					.getBulkExportMDMResourceExpanderInstance()
					.expandPatients(patientIds, partitionId);
			thePatientPids.addAll(jpaPids);
		} else {
			// no mdm expansion; just add them to the list
			myIdHelperService.resolveResourcePids(
					partitionId,
					patientIds,
					ResolveIdentityMode.excludeDeleted().cacheOk());
		}
	}

	@SuppressWarnings("unchecked")
	private LinkedHashSet<JpaPid> getPidsForPatientStyleExport(
			ExportPIDIteratorParameters theParams,
			String resourceType,
			String theJobId,
			String theChunkId,
			RuntimeResourceDefinition def)
			throws IOException {
		LinkedHashSet<JpaPid> pids = new LinkedHashSet<>();

		// Patient
		if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.DISABLED) {
			String errorMessage =
					"You attempted to start a Patient Bulk Export, but the system has `Index Missing Fields` disabled. It must be enabled for Patient Bulk Export";
			ourLog.error(errorMessage);
			throw new IllegalStateException(Msg.code(797) + errorMessage);
		}

		Set<String> patientSearchParams = getPatientActiveSearchParamsForResourceType(theParams.getResourceType());
		for (String patientSearchParam : patientSearchParams) {
			List<SearchParameterMap> maps =
					myBulkExportHelperSvc.createSearchParameterMapsForResourceType(def, theParams, false);
			for (SearchParameterMap map : maps) {
				// Ensure users did not monkey with the patient compartment search parameter.
				validateSearchParametersForPatient(map, theParams);

				ISearchBuilder<JpaPid> searchBuilder = getSearchBuilderForResourceType(theParams.getResourceType());

				filterBySpecificPatient(theParams, resourceType, patientSearchParam, map);

				SearchRuntimeDetails searchRuntime = new SearchRuntimeDetails(null, theJobId);

				Logs.getBatchTroubleshootingLog()
						.debug(
								"Executing query for bulk export job[{}] chunk[{}]: {}",
								theJobId,
								theChunkId,
								map.toNormalizedQueryString(myContext));

				try (IResultIterator<JpaPid> resultIterator = searchBuilder.createQuery(
						map, searchRuntime, new SystemRequestDetails(), theParams.getPartitionIdOrAllPartitions())) {
					int pidCount = 0;
					while (resultIterator.hasNext()) {
						if (pidCount % 10000 == 0) {
							Logs.getBatchTroubleshootingLog()
									.debug(
											"Bulk export job[{}] chunk[{}] has loaded {} pids",
											theJobId,
											theChunkId,
											pidCount);
						}
						pidCount++;
						pids.add(resultIterator.next());
					}
				}
			}
		}

		return pids;
	}

	private void filterBySpecificPatient(
			ExportPIDIteratorParameters theParams,
			String resourceType,
			String patientSearchParam,
			SearchParameterMap map) {
		if (resourceType.equalsIgnoreCase("Patient")) {
			if (theParams.getExpandedPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam =
						makeReferenceOrListParam(theParams.getExpandedPatientIds().stream()
								.map(f -> {
									return f.getAssociatedResourceId()
											.toUnqualifiedVersionless()
											.getValue();
								})
								.collect(Collectors.toList()));
				map.add(PARAM_ID, referenceOrListParam);
			} else if (theParams.getPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam = makeReferenceOrListParam(theParams.getPatientIds());
				map.add(PARAM_ID, referenceOrListParam);
			}
		} else {
			if (theParams.getExpandedPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam =
						makeReferenceOrListParam(theParams.getExpandedPatientIds().stream()
								.map(f -> {
									return f.getAssociatedResourceId()
											.toUnqualifiedVersionless()
											.getValue();
								})
								.collect(Collectors.toList()));
				map.add(patientSearchParam, referenceOrListParam);
			} else if (theParams.getPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam = makeReferenceOrListParam(theParams.getPatientIds());
				map.add(patientSearchParam, referenceOrListParam);
			} else {
				map.add(patientSearchParam, new ReferenceParam().setMissing(false));
			}
		}
	}

	@Nonnull
	private ReferenceOrListParam makeReferenceOrListParam(@Nonnull List<String> thePatientIds) {
		final ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
		thePatientIds.forEach(patientId -> referenceOrListParam.addOr(new ReferenceParam(patientId)));
		return referenceOrListParam;
	}

	@SuppressWarnings("unchecked")
	private LinkedHashSet<JpaPid> getPidsForSystemStyleExport(
			ExportPIDIteratorParameters theParams, String theJobId, String theChunkId, RuntimeResourceDefinition theDef)
			throws IOException {
		LinkedHashSet<JpaPid> pids = new LinkedHashSet<>();
		// System
		List<SearchParameterMap> maps =
				myBulkExportHelperSvc.createSearchParameterMapsForResourceType(theDef, theParams, true);
		ISearchBuilder<JpaPid> searchBuilder = getSearchBuilderForResourceType(theParams.getResourceType());

		for (SearchParameterMap map : maps) {
			Logs.getBatchTroubleshootingLog()
					.debug(
							"Executing query for bulk export job[{}] chunk[{}]: {}",
							theJobId,
							theChunkId,
							map.toNormalizedQueryString(myContext));

			// requires a transaction
			try (IResultIterator<JpaPid> resultIterator = searchBuilder.createQuery(
					map, new SearchRuntimeDetails(null, theJobId), null, theParams.getPartitionIdOrAllPartitions())) {
				int pidCount = 0;
				while (resultIterator.hasNext()) {
					if (pidCount % 10000 == 0) {
						Logs.getBatchTroubleshootingLog()
								.debug(
										"Bulk export job[{}] chunk[{}] has loaded {} pids",
										theJobId,
										theChunkId,
										pidCount);
					}
					pidCount++;
					pids.add(resultIterator.next());
				}
			}
		}
		return pids;
	}

	private LinkedHashSet<JpaPid> getPidsForGroupStyleExport(
			ExportPIDIteratorParameters theParams, String theResourceType, RuntimeResourceDefinition theDef)
			throws IOException {
		LinkedHashSet<JpaPid> pids;

		if (theResourceType.equalsIgnoreCase("Patient")) {
			ourLog.info("Expanding Patients of a Group Bulk Export.");
			pids = getExpandedPatientList(theParams, true);
			ourLog.info("Obtained {} PIDs", pids.size());
		} else if (theResourceType.equalsIgnoreCase("Group")) {
			pids = getSingletonGroupList(theParams);
		} else {
			pids = getRelatedResourceTypePids(theParams, theDef);
		}
		return pids;
	}

	private LinkedHashSet<JpaPid> getRelatedResourceTypePids(
			ExportPIDIteratorParameters theParams, RuntimeResourceDefinition theDef) throws IOException {
		LinkedHashSet<JpaPid> pids = new LinkedHashSet<>();
		// Check if the patient compartment search parameter is active to enable export of this resource
		RuntimeSearchParam activeSearchParam =
				getActivePatientSearchParamForCurrentResourceType(theParams.getResourceType());
		if (activeSearchParam != null) {
			// expand the group pid -> list of patients in that group (list of patient pids)
			Set<JpaPid> expandedMemberResourceIds = getExpandedPatientList(theParams, false);
			assert !expandedMemberResourceIds.isEmpty();
			Logs.getBatchTroubleshootingLog()
					.debug("{} has been expanded to members:[{}]", theParams.getGroupId(), expandedMemberResourceIds);

			// for each patient pid ->
			//	search for the target resources, with their correct patient references, chunked.
			// The results will be jammed into myReadPids
			TaskChunker.chunk(expandedMemberResourceIds, QUERY_CHUNK_SIZE, (idChunk) -> {
				try {
					queryResourceTypeWithReferencesToPatients(pids, idChunk, theParams, theDef);
				} catch (IOException ex) {
					// we will never see this;
					// SearchBuilder#QueryIterator does not (nor can ever) throw
					// an IOException... but Java requires the check,
					// so we'll put a log here (just in the off chance)
					ourLog.error("Couldn't close query iterator ", ex);
					throw new RuntimeException(Msg.code(2346) + "Couldn't close query iterator", ex);
				}
			});
		} else {
			ourLog.warn("No active patient compartment search parameter(s) for resource type "
					+ theParams.getResourceType());
		}
		return pids;
	}

	private LinkedHashSet<JpaPid> getSingletonGroupList(ExportPIDIteratorParameters theParams) {
		RequestPartitionId partitionId = theParams.getPartitionIdOrAllPartitions();
		IBaseResource group = myDaoRegistry
				.getResourceDao("Group")
				.read(new IdDt(theParams.getGroupId()), new SystemRequestDetails().setRequestPartitionId(partitionId));
		JpaPid pidOrNull = myIdHelperService.getPidOrNull(partitionId, group);
		LinkedHashSet<JpaPid> pids = new LinkedHashSet<>();
		pids.add(pidOrNull);
		return pids;
	}

	/**
	 * Get a ISearchBuilder for the given resource type.
	 */
	protected ISearchBuilder<JpaPid> getSearchBuilderForResourceType(String theResourceType) {
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResourceType);
		Class<? extends IBaseResource> typeClass = def.getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(theResourceType, typeClass);
	}

	protected RuntimeSearchParam getPatientSearchParamForCurrentResourceType(String theResourceType) {
		RuntimeSearchParam searchParam = null;
		Optional<RuntimeSearchParam> onlyPatientSearchParamForResourceType =
				SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, theResourceType);
		if (onlyPatientSearchParamForResourceType.isPresent()) {
			searchParam = onlyPatientSearchParamForResourceType.get();
		}
		return searchParam;
	}

	@Override
	public void expandMdmResources(List<IBaseResource> theResources) {
		for (IBaseResource resource : theResources) {
			if (!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(resource.fhirType())) {
				myMdmExpandersHolder.getBulkExportMDMResourceExpanderInstance().annotateResource(resource);
			}
		}
	}

	/**
	 * For Patient
	 **/
	private RuntimeSearchParam validateSearchParametersForPatient(
			SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		RuntimeSearchParam runtimeSearchParam =
				getPatientSearchParamForCurrentResourceType(theParams.getResourceType());
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(Msg.code(796)
					+ String.format(
							"Patient Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!",
							runtimeSearchParam.getName()));
		}
		return runtimeSearchParam;
	}

	/**
	 * for group exports
	 **/
	private void validateSearchParametersForGroup(SearchParameterMap expandedSpMap, String theResourceType) {
		// we only validate for certain types
		if (!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(theResourceType)) {
			RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType(theResourceType);
			if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
				throw new IllegalArgumentException(Msg.code(792)
						+ String.format(
								"Group Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!",
								runtimeSearchParam.getName()));
			}
		}
	}

	/**
	 * Given the local myGroupId, perform an expansion to retrieve all resource IDs of member patients.
	 * if myMdmEnabled is set to true, we also attempt to also expand it into matched
	 * patients.
	 *
	 * @return a Set of Strings representing the resource IDs of all members of a group.
	 */
	private LinkedHashSet<JpaPid> getExpandedPatientList(
			ExportPIDIteratorParameters theParameters, boolean theConsiderDateRange) throws IOException {

		if (theParameters.getExpandedPatientIds() != null) {
			List<JpaPid> existingMembers = theParameters.getExpandedPatientIds().stream()
					.map(pid -> (JpaPid) pid)
					.toList();
			return new LinkedHashSet<>(existingMembers);
		}

		List<JpaPid> members = getMembersFromGroupWithFilter(theParameters, theConsiderDateRange);
		ourLog.info(
				"Group with ID [{}] has been expanded to {} members, member JpaIds: {}",
				theParameters.getGroupId(),
				members.size(),
				members);
		LinkedHashSet<JpaPid> patientPidsToExport = new LinkedHashSet<>(members);

		if (theParameters.isExpandMdm()) {
			RequestPartitionId partitionId = theParameters.getPartitionIdOrAllPartitions();
			patientPidsToExport.addAll(myMdmExpandersHolder
					.getBulkExportMDMResourceExpanderInstance()
					.expandGroup(theParameters.getGroupId(), partitionId));
		}
		return patientPidsToExport;
	}

	/**
	 * Given the parameters, find all members' patient references in the group with the typeFilter applied.
	 *
	 * @return A list of strings representing the Patient IDs of the members (e.g. ["P1", "P2", "P3"]
	 */
	private List<JpaPid> getMembersFromGroupWithFilter(
			ExportPIDIteratorParameters theParameters, boolean theConsiderDateRange) throws IOException {
		final List<SearchParameterMap> maps =
				makeSearchParameterMapsForGroupExport(theParameters, theConsiderDateRange);

		return getPatientPidsUsingSearchMaps(
				maps,
				theParameters.getGroupId(),
				theParameters.getInstanceId(),
				theParameters.getPartitionIdOrAllPartitions());
	}

	private List<JpaPid> getPatientPidsUsingSearchMaps(
			List<SearchParameterMap> maps,
			String theGroupId,
			String theInstanceId,
			RequestPartitionId theRequestPartitionId)
			throws IOException {
		final List<JpaPid> resPids = new ArrayList<>();
		for (SearchParameterMap map : maps) {
			ISearchBuilder<JpaPid> searchBuilder = getSearchBuilderForResourceType("Patient");
			if (isNotBlank(theGroupId)) {
				ourLog.debug(
						"Searching for members of group {} with job instance {} with map {}",
						theGroupId,
						theInstanceId,
						map);
			}
			try (IResultIterator<JpaPid> resultIterator = searchBuilder.createQuery(
					map, new SearchRuntimeDetails(null, theInstanceId), null, theRequestPartitionId)) {

				while (resultIterator.hasNext()) {
					resPids.add(resultIterator.next());
				}
			}
		}
		return resPids;
	}

	@Nonnull
	private List<SearchParameterMap> makeSearchParameterMapsForGroupExport(
			@Nonnull ExportPIDIteratorParameters theParameters, boolean theConsiderDateRange) {
		final RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		final List<SearchParameterMap> maps = myBulkExportHelperSvc.createSearchParameterMapsForResourceType(
				def, theParameters, theConsiderDateRange);
		maps.forEach(map -> {
			map.add(PARAM_HAS, makeGroupMemberHasOrListParam(theParameters.getGroupId()));
			final List<String> patientIds = theParameters.getPatientIds();
			if (patientIds != null && !patientIds.isEmpty()) {
				map.add(PARAM_ID, makeReferenceOrListParam(patientIds));
			}
		});
		return maps;
	}

	private List<SearchParameterMap> makeSearchParameterMapsForPatientExport(ExpandPatientIdsParams theParams) {
		final RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		final List<SearchParameterMap> maps = myBulkExportHelperSvc.createSearchParameterMapsForResourcetype(
				def, theParams.getFilters(), theParams.getStartDate(), theParams.getEndDate(), true);

		if (!theParams.getPatientIds().isEmpty()) {
			// Patient Instance Export
			maps.forEach(map -> {
				map.add(PARAM_ID, makeReferenceOrListParam(theParams.getPatientIds()));
			});
		}

		return maps;
	}

	@Nonnull
	private HasOrListParam makeGroupMemberHasOrListParam(@Nonnull String theGroupId) {
		final HasParam hasParam = new HasParam("Group", "member", "_id", theGroupId);
		return new HasOrListParam().addOr(hasParam);
	}

	// gets all the resources related to each patient provided in the list of thePatientPids
	@SuppressWarnings("unchecked")
	private void queryResourceTypeWithReferencesToPatients(
			Set<JpaPid> theReadPids,
			List<JpaPid> thePatientPids,
			ExportPIDIteratorParameters theParams,
			RuntimeResourceDefinition theDef)
			throws IOException {

		// Convert Resource Persistent IDs to actual client IDs.
		Set<JpaPid> pidSet = new HashSet<>(thePatientPids);
		Set<String> patientIds = myIdHelperService.translatePidsToFhirResourceIds(pidSet);

		// Build SP map
		// First, inject the _typeFilters and _since from the export job
		List<SearchParameterMap> expandedSpMaps =
				myBulkExportHelperSvc.createSearchParameterMapsForResourceType(theDef, theParams, true);
		for (SearchParameterMap expandedSpMap : expandedSpMaps) {

			// Since we are in a bulk job, we have to ensure the user didn't jam in a patient search param, since we
			// need to manually set that.
			validateSearchParametersForGroup(expandedSpMap, theParams.getResourceType());

			// Fetch and cache a search builder for this resource type
			// filter by ResourceType
			ISearchBuilder<JpaPid> searchBuilder = getSearchBuilderForResourceType(theParams.getResourceType());

			// Now, further filter the query with patient references defined by the chunk of IDs we have.
			// filter by PatientIds
			if (PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(theParams.getResourceType())) {
				filterSearchByHasParam(patientIds, expandedSpMap, theParams);
			} else {
				filterSearchByResourceIds(patientIds, expandedSpMap, theParams);
			}

			// Execute query and all found pids to our local iterator.
			RequestPartitionId partitionId = theParams.getPartitionIdOrAllPartitions();
			try (IResultIterator<JpaPid> resultIterator = searchBuilder.createQuery(
					expandedSpMap, new SearchRuntimeDetails(null, theParams.getInstanceId()), null, partitionId)) {
				while (resultIterator.hasNext()) {
					theReadPids.add(resultIterator.next());
				}
			}

			// Construct our Includes filter
			// We use this to recursively fetch resources of interest
			// (but should only request those the user has requested/can see)
			Set<Include> includes = new HashSet<>();
			for (String resourceType : theParams.getRequestedResourceTypes()) {
				includes.add(new Include(resourceType + ":*", true));
			}

			SystemRequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId(partitionId);
			SearchBuilderLoadIncludesParameters<JpaPid> loadIncludesParameters =
					new SearchBuilderLoadIncludesParameters<>();
			loadIncludesParameters.setFhirContext(myContext);
			loadIncludesParameters.setMatches(theReadPids);
			loadIncludesParameters.setEntityManager(myEntityManager);
			loadIncludesParameters.setRequestDetails(requestDetails);
			loadIncludesParameters.setIncludeFilters(includes);
			loadIncludesParameters.setReverseMode(false);
			loadIncludesParameters.setLastUpdated(expandedSpMap.getLastUpdated());
			loadIncludesParameters.setSearchIdOrDescription(theParams.getInstanceId());
			loadIncludesParameters.setDesiredResourceTypes(theParams.getRequestedResourceTypes());
			Set<JpaPid> includeIds = searchBuilder.loadIncludes(loadIncludesParameters);

			// gets rid of the Patient duplicates
			theReadPids.addAll(includeIds.stream()
					.filter((id) -> !id.getResourceType().equals("Patient"))
					.collect(Collectors.toSet()));
		}
	}

	private RuntimeSearchParam getActivePatientSearchParamForCurrentResourceType(String theResourceType) {
		String activeSearchParamName = "";
		String resourceToCheck = theResourceType;
		if (!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(theResourceType)) {
			activeSearchParamName =
					getPatientSearchParamForCurrentResourceType(theResourceType).getName();
		} else if ("Practitioner".equalsIgnoreCase(theResourceType)) {
			resourceToCheck = "Patient";
			activeSearchParamName = "general-practitioner";
		} else if ("Organization".equalsIgnoreCase(theResourceType)) {
			resourceToCheck = "Patient";
			activeSearchParamName = "organization";
		}
		return mySearchParamRegistry.getActiveSearchParam(
				resourceToCheck, activeSearchParamName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
	}

	/**
	 * Must not be called for resources types listed in PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES
	 *
	 * @param idChunk
	 * @param expandedSpMap
	 * @param theParams
	 */
	private void filterSearchByResourceIds(
			Set<String> idChunk, SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		ReferenceOrListParam orList = new ReferenceOrListParam();
		idChunk.forEach(id -> orList.add(new ReferenceParam(id)));
		RuntimeSearchParam patientSearchParamForCurrentResourceType =
				getPatientSearchParamForCurrentResourceType(theParams.getResourceType());
		expandedSpMap.add(patientSearchParamForCurrentResourceType.getName(), orList);
	}

	/**
	 * @param idChunk
	 * @param expandedSpMap
	 */
	private void filterSearchByHasParam(
			Set<String> idChunk, SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		HasOrListParam hasOrListParam = new HasOrListParam();
		idChunk.stream().forEach(id -> hasOrListParam.addOr(buildHasParam(id, theParams.getResourceType())));
		expandedSpMap.add("_has", hasOrListParam);
	}

	private HasParam buildHasParam(String theResourceId, String theResourceType) {
		if ("Practitioner".equalsIgnoreCase(theResourceType)) {
			return new HasParam("Patient", "general-practitioner", "_id", theResourceId);
		} else if ("Organization".equalsIgnoreCase(theResourceType)) {
			return new HasParam("Patient", "organization", "_id", theResourceId);
		} else {
			throw new IllegalArgumentException(
					Msg.code(2077) + " We can't handle forward references onto type " + theResourceType);
		}
	}

	private Set<String> getPatientActiveSearchParamsForResourceType(String theResourceType) {
		Set<String> allPatientSearchParams =
				SearchParameterUtil.getPatientSearchParamsForResourceType(myContext, theResourceType);

		// Only consider the search params that are active
		Set<String> patientSearchParams = allPatientSearchParams.stream()
				.filter(s -> mySearchParamRegistry.hasActiveSearchParam(
						theResourceType, s, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH))
				.collect(Collectors.toSet());
		if (patientSearchParams.isEmpty()) {
			String errorMessage = String.format(
					"Resource type [%s] is not eligible for this type of export, as it contains no active search parameters.",
					theResourceType);
			throw new IllegalArgumentException(Msg.code(2817) + errorMessage);
		}
		return patientSearchParams;
	}
}
