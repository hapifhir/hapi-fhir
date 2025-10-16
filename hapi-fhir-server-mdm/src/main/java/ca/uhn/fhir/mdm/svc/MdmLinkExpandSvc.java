/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MdmLinkExpandSvc implements IMdmLinkExpandSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	IMdmLinkDao myMdmLinkDao;

	@Autowired
	IIdHelperService myIdHelperService;

	public MdmLinkExpandSvc() {}

	/**
	 * Given a source resource, perform MDM expansion and return all the resource IDs of all resources that are
	 * MDM-Matched to this resource.
	 *
	 * @param theResource The resource to MDM-Expand
	 * @return A set of strings representing the FHIR IDs of the expanded resources.
	 */
	@Override
	public Set<String> expandMdmBySourceResource(RequestPartitionId theRequestPartitionId, IBaseResource theResource) {
		ourLog.debug("About to MDM-expand source resource {}", theResource);
		return expandMdmBySourceResourceId(theRequestPartitionId, theResource.getIdElement());
	}

	/**
	 * Given a resource ID of a source resource, perform MDM expansion and return all the resource IDs of all resources that are
	 * MDM-Matched to this resource.
	 *
	 * @param theRequestPartitionId The partition ID associated with the request.
	 * @param theId                 The Resource ID of the resource to MDM-Expand
	 * @return A set of strings representing the FHIR ids of the expanded resources.
	 */
	@Override
	public Set<String> expandMdmBySourceResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		ourLog.debug("About to expand source resource with resource id {}", theId);
		return expandMdmBySourceResourcePid(
				theRequestPartitionId,
				myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theId));
	}

	/**
	 * Given a partition ID and a PID of a source resource, perform MDM expansion and return all the resource IDs of all resources that are
	 * MDM-Matched to this resource.
	 *
	 * @param theRequestPartitionId The partition ID associated with the request.
	 * @param theSourceResourcePid  The PID of the resource to MDM-Expand
	 * @return A set of strings representing the FHIR ids of the expanded resources.
	 */
	@Override
	public Set<String> expandMdmBySourceResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theSourceResourcePid) {
		ourLog.debug("About to expand source resource with PID {}", theSourceResourcePid);
		final List<MdmPidTuple<?>> goldenPidSourcePidTuples =
				myMdmLinkDao.expandPidsBySourcePidAndMatchResult(theSourceResourcePid, MdmMatchResultEnum.MATCH);

		return flattenPidTuplesToSet(theRequestPartitionId, theSourceResourcePid, goldenPidSourcePidTuples);
	}

	/**
	 *  Given a PID of a golden resource, perform MDM expansion and return all the resource IDs of all resources that are
	 *  MDM-Matched to this golden resource.
	 *
	 * @param theRequestPartitionId Partition information from the request
	 * @param theGoldenResourcePid The PID of the golden resource to MDM-Expand.
	 * @return A set of strings representing the FHIR ids of the expanded resources.
	 */
	@Override
	public Set<String> expandMdmByGoldenResourceId(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		ourLog.debug("About to expand golden resource with PID {}", theGoldenResourcePid);
		final List<MdmPidTuple<?>> goldenPidSourcePidTuples = myMdmLinkDao.expandPidsByGoldenResourcePidAndMatchResult(
				theGoldenResourcePid, MdmMatchResultEnum.MATCH);
		return flattenPidTuplesToSet(theRequestPartitionId, theGoldenResourcePid, goldenPidSourcePidTuples);
	}

	/**
	 *  Given a resource ID of a golden resource, perform MDM expansion and return all the resource IDs of all resources that are
	 *  MDM-Matched to this golden resource.
	 *
	 * @param theRequestPartitionId Partition information from the request
	 * @param theGoldenResourcePid The resource ID of the golden resource to MDM-Expand.
	 * @return A set of strings representing the FHIR ids of the expanded resources.
	 */
	@Override
	public Set<String> expandMdmByGoldenResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		ourLog.debug("About to expand golden resource with PID {}", theGoldenResourcePid);
		final List<MdmPidTuple<?>> goldenPidSourcePidTuples = myMdmLinkDao.expandPidsByGoldenResourcePidAndMatchResult(
				theGoldenResourcePid, MdmMatchResultEnum.MATCH);
		return flattenPidTuplesToSet(theRequestPartitionId, theGoldenResourcePid, goldenPidSourcePidTuples);
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		ourLog.debug("About to expand golden resource with golden resource id {}", theId);
		IResourcePersistentId<?> pidOrThrowException =
				myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theId);
		return expandMdmByGoldenResourcePid(theRequestPartitionId, pidOrThrowException);
	}

	/***
	 * Return an id->golden-record-id map, for those ids that do have one.
	 * @param ids
	 * @return
	 */
	public Map<IIdType, String> getGoldenResourceIdsFromIds(List<IIdType> ids) {
		// ---------------------------------------------------------------------------------------------
		// Goal: Create a IIDType to golden record ID for those ids that do have one.
		// Steps:
		// 1.- Go from IIDTypes to source pid for each one of the specified ids
		// 2.- Find the golden record pids for those source pids that do have one (not all of them will)
		// 3.- Find the typed resource IDs for the golden record pids found in step 2
		// 4.- Create and return a map of the original ids (those that do have a golden record) to
		// the golden record's typed resource IDs
		// ---------------------------------------------------------------------------------------------

		// Find the pids and place them in a map to find our way back
		Map<IResourcePersistentId, IIdType> pidToIdMap = ids.stream()
				.collect(Collectors.toMap(
						id -> myIdHelperService.getPidOrThrowException(
								RequestPartitionId.allPartitions(), (IIdType) id),
						id -> id));

		// Get the mapped pids from the ids - not all of them will have a golden record
		Set<IResourcePersistentId> pidSet = pidToIdMap.keySet();

		// Get the golden record pids for the source pids that do have in a single DB call, and place results in a map
		Collection<MdmPidTuple<?>> allGoldenTuples =
				myMdmLinkDao.resolveGoldenResources(pidSet.stream().toList());
		// We will get **all** the mappings for the gold record associated with the given pid. (i.e. There PatientA and
		// PatientB
		// both point to the same golden record, we will get back two map entries, even though we passed in the pid of
		// PatientA only). We filter so we keep only the record for PatientA (and drop PatientB)
		Collection<MdmPidTuple<?>> goldenTuples = allGoldenTuples.stream()
				.filter(t -> pidSet.contains(t.getSourcePid()))
				.toList();
		Map<IResourcePersistentId, IResourcePersistentId> sourcePidToGoldenPidMap =
				goldenTuples.stream().collect(Collectors.toMap(MdmPidTuple::getSourcePid, MdmPidTuple::getGoldenPid));

		// Now find the typed resource IDs (Patient/ABC) for all the golden record PIDs
		Collection<IResourcePersistentId> pids = sourcePidToGoldenPidMap.values();
		PersistentIdToForcedIdMap<IResourcePersistentId<?>> forcedIdMap =
				myIdHelperService.translatePidsToForcedIds(new HashSet(pids));

		// Build the source id --> golden typed resource IDs
		Map<IIdType, String> iIdTypeStringMap = sourcePidToGoldenPidMap.keySet().stream()
				.collect(Collectors.toMap(
						pidToIdMap::get,
						pid -> forcedIdMap.get(sourcePidToGoldenPidMap.get(pid)).orElse("")));
		return iIdTypeStringMap;
	}

	@Nonnull
	public Set<String> flattenPidTuplesToSet(
			RequestPartitionId theRequestPartitionId,
			IResourcePersistentId<?> theInitialPid,
			List<MdmPidTuple<?>> theGoldenPidSourcePidTuples) {
		final Set<IResourcePersistentId> flattenedPids = theGoldenPidSourcePidTuples.stream()
				.map(tuple -> flattenTuple(theRequestPartitionId, tuple))
				.flatMap(Collection::stream)
				.collect(Collectors.toUnmodifiableSet());
		final Set<String> resourceIds = myIdHelperService.translatePidsToFhirResourceIds(flattenedPids);
		ourLog.debug("Pid {} has been expanded to [{}]", theInitialPid, String.join(",", resourceIds));
		return resourceIds;
	}

	@Nonnull
	static Set<IResourcePersistentId> flattenTuple(RequestPartitionId theRequestPartitionId, MdmPidTuple<?> theTuple) {
		if (theRequestPartitionId.isPartitionCovered(theTuple.getGoldenPartitionId())) {
			if (theRequestPartitionId.isPartitionCovered(theTuple.getSourcePartitionId())) {
				return Set.of(theTuple.getSourcePid(), theTuple.getGoldenPid());
			}
			return Set.of(theTuple.getGoldenPid());
		}

		if (theRequestPartitionId.isPartitionCovered(theTuple.getSourcePartitionId())) {
			return Set.of(theTuple.getSourcePid());
		}

		return Collections.emptySet();
	}
}
