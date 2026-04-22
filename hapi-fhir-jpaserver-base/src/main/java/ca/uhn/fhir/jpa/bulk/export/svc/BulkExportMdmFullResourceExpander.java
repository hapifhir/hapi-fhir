/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.svc.IBulkExportMdmFullResourceExpander;
import ca.uhn.fhir.mdm.svc.MdmExpansionCacheSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of MDM resource expansion for bulk export operations.
 * Expands group memberships via MDM links and annotates exported resources with golden resource references.
 */
public class BulkExportMdmFullResourceExpander implements IBulkExportMdmFullResourceExpander<JpaPid> {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkExportMdmFullResourceExpander.class);

	private final MdmExpansionCacheSvc myMdmExpansionCacheSvc;
	private final IMdmLinkDao myMdmLinkDao;
	private final IIdHelperService<JpaPid> myIdHelperService;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myContext;
	private IFhirPath myFhirPath;

	public BulkExportMdmFullResourceExpander(
			MdmExpansionCacheSvc theMdmExpansionCacheSvc,
			IMdmLinkDao theMdmLinkDao,
			IIdHelperService<JpaPid> theIdHelperService,
			DaoRegistry theDaoRegistry,
			FhirContext theFhirContext) {
		myMdmExpansionCacheSvc = theMdmExpansionCacheSvc;
		myMdmLinkDao = theMdmLinkDao;
		myIdHelperService = theIdHelperService;
		myDaoRegistry = theDaoRegistry;
		myContext = theFhirContext;
	}

	@Override
	public Set<JpaPid> expandGroup(String theGroupResourceId, RequestPartitionId theRequestPartitionId) {
		IdDt groupId = new IdDt(theGroupResourceId);
		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(theRequestPartitionId);
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(groupId, requestDetails);
		JpaPid pidOrNull = myIdHelperService.getPidOrNull(theRequestPartitionId, group);
		// Attempt to perform MDM Expansion of membership
		return performMembershipExpansionViaMdmTable(pidOrNull);
	}

	@Override
	public Set<String> expandPatient(String thePatientId, RequestPartitionId theRequestPartitionId) {
		Set<String> expandedPatientIdsAsString = new HashSet<>();
		Set<JpaPid> expandedPatientJpaPids = new HashSet<>();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setRequestPartitionId(theRequestPartitionId);

		IdDt patientIdDt = new IdDt(thePatientId);

		IBaseResource patient;
		try {
			patient = myDaoRegistry.getResourceDao("Patient").read(patientIdDt, requestDetails);
		} catch (Exception e) {
			ourLog.warn("Failed to read patient {} for MDM expansion: {}", thePatientId, e.getMessage());
			return expandedPatientIdsAsString;
		}

		JpaPid patientPid = myIdHelperService.getPidOrNull(theRequestPartitionId, patient);
		if (patientPid == null) {
			ourLog.warn("Failed to resolve PID for patient {}", thePatientId);
			return expandedPatientIdsAsString;
		}

		List<MdmPidTuple<JpaPid>> allLinkedPatients =
				myMdmLinkDao.expandPidsBySourcePidAndMatchResult(patientPid, MdmMatchResultEnum.MATCH);

		if (allLinkedPatients.isEmpty()) {
			expandedPatientJpaPids.add(patientPid);
			ourLog.debug("Patient {} has no MDM links, including only this patient", thePatientId);
		} else {

			ourLog.debug("Patient {} expanded to {} linked patients", thePatientId, allLinkedPatients.size());
			List<MdmPidTuple<JpaPid>> goldenPidSourcePidTuples = new ArrayList<>(allLinkedPatients);

			populateMdmResourceCache(goldenPidSourcePidTuples);

			for (MdmPidTuple<JpaPid> tuple : goldenPidSourcePidTuples) {
				expandedPatientJpaPids.add(tuple.getGoldenPid());
				expandedPatientJpaPids.add(tuple.getSourcePid());
			}
		}

		for (JpaPid pid : expandedPatientJpaPids) {
			String patientIdString;

			Optional<String> forcedIdOpt = myIdHelperService.translatePidIdToForcedIdWithCache(pid);
			patientIdString = forcedIdOpt.orElse("Patient/" + pid.getId().toString());

			expandedPatientIdsAsString.add(patientIdString);
		}

		ourLog.debug(
				"Expanded patient {} to {} total patient IDs via MDM", thePatientId, expandedPatientIdsAsString.size());
		return expandedPatientIdsAsString;
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

	private RuntimeSearchParam getRuntimeSearchParam(IBaseResource theResource) {
		Optional<RuntimeSearchParam> oPatientSearchParam =
				SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, theResource.fhirType());
		if (oPatientSearchParam.isEmpty()) {
			String errorMessage = String.format(
					"[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!",
					theResource.fhirType());
			throw new IllegalArgumentException(Msg.code(2242) + errorMessage);
		} else {
			return oPatientSearchParam.get();
		}
	}

	@Override
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
				return optionalReference.map(theIBaseReference -> theIBaseReference
						.getReferenceElement()
						.toUnqualifiedVersionless()
						.toString());
			} else {
				return Optional.empty();
			}
		}
	}

	private void addGoldenResourceExtension(IBaseResource iBaseResource, String sourceResourceId) {
		// EHP: reimplement this, it is currently completely broken given the distributed nature of the job.
		String goldenResourceId = ""; // TODO we must be able to fetch this, for now, will be no-op
		if (!StringUtils.isBlank(goldenResourceId)) {
			IBaseExtension<?, ?> extension = ExtensionUtil.getOrCreateExtension(
					iBaseResource, HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
			ExtensionUtil.setExtension(myContext, extension, "reference", goldenResourceId);
		}
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
