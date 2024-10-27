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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.SearchParameterUtil;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;

public class JpaBulkExportProcessor implements IBulkExportProcessor<JpaPid> {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBulkExportProcessor.class);

	public static final int QUERY_CHUNK_SIZE = 100;
	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES =
			List.of("Practitioner", "Organization");

	@Autowired
	private FhirContext myContext;

	@Autowired
	private BulkExportHelperService myBulkExportHelperSvc;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	protected SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	protected IMdmLinkDao<JpaPid, MdmLink> myMdmLinkDao;

	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private IHapiTransactionService myHapiTransactionService;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	private IFhirPath myFhirPath;

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

		Set<String> patientSearchParams =
				SearchParameterUtil.getPatientSearchParamsForResourceType(myContext, theParams.getResourceType());

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

	private static void filterBySpecificPatient(
			ExportPIDIteratorParameters theParams,
			String resourceType,
			String patientSearchParam,
			SearchParameterMap map) {
		if (resourceType.equalsIgnoreCase("Patient")) {
			if (theParams.getPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam = getReferenceOrListParam(theParams);
				map.add(PARAM_ID, referenceOrListParam);
			}
		} else {
			if (theParams.getPatientIds() != null) {
				ReferenceOrListParam referenceOrListParam = getReferenceOrListParam(theParams);
				map.add(patientSearchParam, referenceOrListParam);
			} else {
				map.add(patientSearchParam, new ReferenceParam().setMissing(false));
			}
		}
	}

	@Nonnull
	private static ReferenceOrListParam getReferenceOrListParam(ExportPIDIteratorParameters theParams) {
		ReferenceOrListParam referenceOrListParam = new ReferenceOrListParam();
		for (String patientId : theParams.getPatientIds()) {
			referenceOrListParam.addOr(new ReferenceParam(patientId));
		}
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
			pids = getExpandedPatientList(theParams);
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
			Set<JpaPid> expandedMemberResourceIds = expandAllPatientPidsFromGroup(theParams);
			assert !expandedMemberResourceIds.isEmpty();
			Logs.getBatchTroubleshootingLog()
					.debug("{} has been expanded to members:[{}]", theParams.getGroupId(), expandedMemberResourceIds);

			// for each patient pid ->
			//	search for the target resources, with their correct patient references, chunked.
			// The results will be jammed into myReadPids
			QueryChunker<JpaPid> queryChunker = new QueryChunker<>();
			queryChunker.chunk(expandedMemberResourceIds, QUERY_CHUNK_SIZE, (idChunk) -> {
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
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(theResourceType);
		RuntimeResourceDefinition def = myContext.getResourceDefinition(theResourceType);
		Class<? extends IBaseResource> typeClass = def.getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(dao, theResourceType, typeClass);
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
				annotateBackwardsReferences(resource);
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
	 * In case we are doing a Group Bulk Export and resourceType `Patient` is requested, we can just return the group members,
	 * possibly expanded by MDM, and don't have to go and fetch other resource DAOs.
	 */
	@SuppressWarnings("unchecked")
	private LinkedHashSet<JpaPid> getExpandedPatientList(ExportPIDIteratorParameters theParameters) throws IOException {
		List<JpaPid> members = getMembersFromGroupWithFilter(theParameters, true);
		List<IIdType> ids =
				members.stream().map(member -> new IdDt("Patient/" + member)).collect(Collectors.toList());
		ourLog.info("While extracting patients from a group, we found {} patients.", ids.size());
		ourLog.info("Found patients: {}", ids.stream().map(id -> id.getValue()).collect(Collectors.joining(", ")));

		List<JpaPid> pidsOrThrowException = members;
		LinkedHashSet<JpaPid> patientPidsToExport = new LinkedHashSet<>(pidsOrThrowException);

		if (theParameters.isExpandMdm()) {
			RequestPartitionId partitionId = theParameters.getPartitionIdOrAllPartitions();
			SystemRequestDetails srd = new SystemRequestDetails().setRequestPartitionId(partitionId);
			IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(theParameters.getGroupId()), srd);
			JpaPid pidOrNull = myIdHelperService.getPidOrNull(partitionId, group);
			List<MdmPidTuple<JpaPid>> goldenPidSourcePidTuple =
					myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			goldenPidSourcePidTuple.forEach(tuple -> {
				patientPidsToExport.add(tuple.getGoldenPid());
				patientPidsToExport.add(tuple.getSourcePid());
			});
			populateMdmResourceCache(goldenPidSourcePidTuple);
		}
		return patientPidsToExport;
	}

	/**
	 * Given the parameters, find all members' patient references in the group with the typeFilter applied.
	 *
	 * @return A list of strings representing the Patient IDs of the members (e.g. ["P1", "P2", "P3"]
	 */
	@SuppressWarnings("unchecked")
	private List<JpaPid> getMembersFromGroupWithFilter(
			ExportPIDIteratorParameters theParameters, boolean theConsiderSince) throws IOException {
		RuntimeResourceDefinition def = myContext.getResourceDefinition("Patient");
		List<JpaPid> resPids = new ArrayList<>();

		List<SearchParameterMap> maps =
				myBulkExportHelperSvc.createSearchParameterMapsForResourceType(def, theParameters, theConsiderSince);

		maps.forEach(map -> addMembershipToGroupClause(map, theParameters.getGroupId()));

		for (SearchParameterMap map : maps) {
			ISearchBuilder<JpaPid> searchBuilder = getSearchBuilderForResourceType("Patient");
			ourLog.debug(
					"Searching for members of group {} with job instance {} with map {}",
					theParameters.getGroupId(),
					theParameters.getInstanceId(),
					map);
			try (IResultIterator<JpaPid> resultIterator = searchBuilder.createQuery(
					map,
					new SearchRuntimeDetails(null, theParameters.getInstanceId()),
					null,
					theParameters.getPartitionIdOrAllPartitions())) {

				while (resultIterator.hasNext()) {
					resPids.add(resultIterator.next());
				}
			}
		}
		return resPids;
	}

	/**
	 * This method takes an {@link SearchParameterMap} and adds a clause to it that will filter the search results to only
	 * return members of the defined group.
	 *
	 * @param theMap     the map to add the clause to.
	 * @param theGroupId the group ID to filter by.
	 */
	private void addMembershipToGroupClause(SearchParameterMap theMap, String theGroupId) {
		HasOrListParam hasOrListParam = new HasOrListParam();
		hasOrListParam.addOr(new HasParam("Group", "member", "_id", theGroupId));
		theMap.add(PARAM_HAS, hasOrListParam);
	}

	/**
	 * @param thePidTuples
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private void populateMdmResourceCache(List<MdmPidTuple<JpaPid>> thePidTuples) {
		if (myMdmExpansionCacheSvc.hasBeenPopulated()) {
			return;
		}
		// First, convert this zipped set of tuples to a map of
		// {
		//   patient/gold-1 -> [patient/1, patient/2]
		//   patient/gold-2 -> [patient/3, patient/4]
		// }
		Map<JpaPid, Set<JpaPid>> goldenResourceToSourcePidMap = new HashMap<>();
		extract(thePidTuples, goldenResourceToSourcePidMap);

		// Next, lets convert it to an inverted index for fast lookup
		// {
		//   patient/1 -> patient/gold-1
		//   patient/2 -> patient/gold-1
		//   patient/3 -> patient/gold-2
		//   patient/4 -> patient/gold-2
		// }
		Map<String, String> sourceResourceIdToGoldenResourceIdMap = new HashMap<>();
		goldenResourceToSourcePidMap.forEach((key, value) -> {
			String goldenResourceId =
					myIdHelperService.translatePidIdToForcedIdWithCache(key).orElse(key.toString());
			PersistentIdToForcedIdMap pidsToForcedIds = myIdHelperService.translatePidsToForcedIds(value);

			Set<String> sourceResourceIds = pidsToForcedIds.getResolvedResourceIds();

			sourceResourceIds.forEach(
					sourceResourceId -> sourceResourceIdToGoldenResourceIdMap.put(sourceResourceId, goldenResourceId));
		});

		// Now that we have built our cached expansion, store it.
		myMdmExpansionCacheSvc.setCacheContents(sourceResourceIdToGoldenResourceIdMap);
	}

	private void extract(
			List<MdmPidTuple<JpaPid>> theGoldenPidTargetPidTuples,
			Map<JpaPid, Set<JpaPid>> theGoldenResourceToSourcePidMap) {
		for (MdmPidTuple<JpaPid> goldenPidTargetPidTuple : theGoldenPidTargetPidTuples) {
			JpaPid goldenPid = goldenPidTargetPidTuple.getGoldenPid();
			JpaPid sourcePid = goldenPidTargetPidTuple.getSourcePid();
			theGoldenResourceToSourcePidMap
					.computeIfAbsent(goldenPid, key -> new HashSet<>())
					.add(sourcePid);
		}
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
		return mySearchParamRegistry.getActiveSearchParam(resourceToCheck, activeSearchParamName);
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

	/**
	 * Given the local myGroupId, perform an expansion to retrieve all resource IDs of member patients.
	 * if myMdmEnabled is set to true, we also reach out to the IMdmLinkDao to attempt to also expand it into matched
	 * patients.
	 *
	 * @return a Set of Strings representing the resource IDs of all members of a group.
	 */
	private Set<JpaPid> expandAllPatientPidsFromGroup(ExportPIDIteratorParameters theParams) throws IOException {
		Set<JpaPid> expandedIds = new HashSet<>();
		RequestPartitionId partitionId = theParams.getPartitionIdOrAllPartitions();
		SystemRequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId(partitionId);
		IBaseResource group =
				myDaoRegistry.getResourceDao("Group").read(new IdDt(theParams.getGroupId()), requestDetails);
		JpaPid pidOrNull = myIdHelperService.getPidOrNull(partitionId, group);

		// Attempt to perform MDM Expansion of membership
		if (theParams.isExpandMdm()) {
			expandedIds.addAll(performMembershipExpansionViaMdmTable(pidOrNull));
		}

		// Now manually add the members of the group (its possible even with mdm expansion that some members dont have
		// MDM matches,
		// so would be otherwise skipped
		List<JpaPid> membersFromGroupWithFilter = getMembersFromGroupWithFilter(theParams, false);
		ourLog.debug("Group with ID [{}] has been expanded to: {}", theParams.getGroupId(), membersFromGroupWithFilter);
		expandedIds.addAll(membersFromGroupWithFilter);

		return expandedIds;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Set<JpaPid> performMembershipExpansionViaMdmTable(JpaPid pidOrNull) {
		List<MdmPidTuple<JpaPid>> goldenPidTargetPidTuples =
				myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
		// Now lets translate these pids into resource IDs
		Set<JpaPid> uniquePids = new HashSet<>();
		goldenPidTargetPidTuples.forEach(tuple -> {
			uniquePids.add(tuple.getGoldenPid());
			uniquePids.add(tuple.getSourcePid());
		});
		PersistentIdToForcedIdMap pidToForcedIdMap = myIdHelperService.translatePidsToForcedIds(uniquePids);

		Map<JpaPid, Set<JpaPid>> goldenResourceToSourcePidMap = new HashMap<>();
		extract(goldenPidTargetPidTuples, goldenResourceToSourcePidMap);
		populateMdmResourceCache(goldenPidTargetPidTuples);

		return uniquePids;
	}

	/* Mdm Expansion */

	private RuntimeSearchParam getRuntimeSearchParam(IBaseResource theResource) {
		Optional<RuntimeSearchParam> oPatientSearchParam =
				SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, theResource.fhirType());
		if (!oPatientSearchParam.isPresent()) {
			String errorMessage = String.format(
					"[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!",
					theResource.fhirType());
			throw new IllegalArgumentException(Msg.code(2242) + errorMessage);
		} else {
			return oPatientSearchParam.get();
		}
	}

	private void annotateBackwardsReferences(IBaseResource iBaseResource) {
		Optional<String> patientReference = getPatientReference(iBaseResource);
		if (patientReference.isPresent()) {
			addGoldenResourceExtension(iBaseResource, patientReference.get());
		} else {
			ourLog.error(
					"Failed to find the patient reference information for resource {}. This is a bug, "
							+ "as all resources which can be exported via Group Bulk Export must reference a patient.",
					iBaseResource);
		}
	}

	private Optional<String> getPatientReference(IBaseResource iBaseResource) {
		String fhirPath;

		RuntimeSearchParam runtimeSearchParam = getRuntimeSearchParam(iBaseResource);
		fhirPath = getPatientFhirPath(runtimeSearchParam);

		if (iBaseResource.fhirType().equalsIgnoreCase("Patient")) {
			return Optional.of(iBaseResource.getIdElement().getIdPart());
		} else {
			Optional<IBaseReference> optionalReference =
					getFhirParser().evaluateFirst(iBaseResource, fhirPath, IBaseReference.class);
			if (optionalReference.isPresent()) {
				return optionalReference.map(theIBaseReference ->
						theIBaseReference.getReferenceElement().getIdPart());
			} else {
				return Optional.empty();
			}
		}
	}

	private void addGoldenResourceExtension(IBaseResource iBaseResource, String sourceResourceId) {
		String goldenResourceId = myMdmExpansionCacheSvc.getGoldenResourceId(sourceResourceId);
		IBaseExtension<?, ?> extension = ExtensionUtil.getOrCreateExtension(
				iBaseResource, HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
		if (!StringUtils.isBlank(goldenResourceId)) {
			ExtensionUtil.setExtension(myContext, extension, "reference", prefixPatient(goldenResourceId));
		}
	}

	private String prefixPatient(String theResourceId) {
		return "Patient/" + theResourceId;
	}

	private IFhirPath getFhirParser() {
		if (myFhirPath == null) {
			myFhirPath = myContext.newFhirPath();
		}
		return myFhirPath;
	}

	private String getPatientFhirPath(RuntimeSearchParam theRuntimeParam) {
		String path = theRuntimeParam.getPath();
		// GGG: Yes this is a stupid hack, but by default this runtime search param will return stuff like
		// Observation.subject.where(resolve() is Patient) which unfortunately our FHIRpath evaluator doesn't play
		// nicely with
		// our FHIRPath evaluator.
		if (path.contains(".where")) {
			path = path.substring(0, path.indexOf(".where"));
		}
		return path;
	}
}
