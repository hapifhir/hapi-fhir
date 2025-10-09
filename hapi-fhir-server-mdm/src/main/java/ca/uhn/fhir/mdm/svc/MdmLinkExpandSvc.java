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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SearchParameterUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class MdmLinkExpandSvc implements IMdmLinkExpandSvc {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();


	@Autowired
	private FhirContext myContext;

	@Autowired
	IMdmLinkDao myMdmLinkDao;

	@Autowired
	IIdHelperService myIdHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	private IFhirPath myFhirPath;

	public MdmLinkExpandSvc() {
		myFhirPath = myContext.newFhirPath();
	}

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

	@Override
	public Set<JpaPid> expandGroup(String theGroupResourceId, RequestPartitionId theRequestPartitionId) {
		IdDt groupId = new IdDt(theGroupResourceId);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(theRequestPartitionId);
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(groupId, requestDetails);
//		FIXME GGG think more about this cast. How does mongo do this?
		JpaPid pidOrNull = (JpaPid) myIdHelperService.getPidOrNull(theRequestPartitionId, group);
		// Attempt to perform MDM Expansion of membership
		return performMembershipExpansionViaMdmTable(pidOrNull);
	}@
		Override
	public void annotateResource(IBaseResource iBaseResource) {
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

	@SuppressWarnings({"rawtypes", "unchecked"})
	private Set<JpaPid> performMembershipExpansionViaMdmTable(JpaPid pidOrNull) {
		List<MdmPidTuple<JpaPid>> goldenPidTargetPidTuples =
			myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);

		Set<JpaPid> uniquePids = new HashSet<>();
		goldenPidTargetPidTuples.forEach(tuple -> {
			uniquePids.add(tuple.getGoldenPid());
			uniquePids.add(tuple.getSourcePid());
		});
		populateMdmResourceCache(goldenPidTargetPidTuples);
		return uniquePids;
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
			String goldenResourceId = (String) myIdHelperService.translatePidIdToForcedIdWithCache(key).orElseGet(key::toString);
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
	private Optional<String> getPatientReference(IBaseResource iBaseResource) {
		String fhirPath;

		RuntimeSearchParam runtimeSearchParam = getRuntimeSearchParam(iBaseResource);
		fhirPath = getPatientFhirPath(runtimeSearchParam);

		if (iBaseResource.fhirType().equalsIgnoreCase("Patient")) {
			return Optional.of(iBaseResource.getIdElement().getIdPart());
		} else {
			Optional<IBaseReference> optionalReference =
				myFhirPath.evaluateFirst(iBaseResource, fhirPath, IBaseReference.class);
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

	private String getPatientFhirPath(RuntimeSearchParam theRuntimeParam) {
		String path = theRuntimeParam.getPath();
		// GGG: Yes this is a stupid hack, but by default this runtime search param will return stuff like
		// Observation.subject.where(resolve() is Patient) which unfortunately our FHIRpath evaluator doesn't play
		// nicely with
		if (path.contains(".where")) {
			path = path.substring(0, path.indexOf(".where"));
		}
		return path;
	}

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
}
