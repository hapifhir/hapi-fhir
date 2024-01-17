/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.primitive.IdDt;
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
import java.util.List;
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
	public Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IdDt theId) {
		ourLog.debug("About to expand golden resource with golden resource id {}", theId);
		IResourcePersistentId<?> pidOrThrowException =
				myIdHelperService.getPidOrThrowException(RequestPartitionId.allPartitions(), theId);
		return expandMdmByGoldenResourcePid(theRequestPartitionId, pidOrThrowException);
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
