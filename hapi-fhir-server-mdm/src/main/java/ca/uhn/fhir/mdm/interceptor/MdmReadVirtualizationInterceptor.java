/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionResults;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <b>This class is experimental and subject to change. Use with caution.</b>
 * <p>
 * This interceptor provides an "MDM Virtualized" endpoint, meaning that
 * searches are expanded to include MDM-linked resources (including any
 * linked golden resource, and also including any other resources linked
 * to that golden resource). Searches for non-MDM resources which have
 * a reference to an MDM resource will have their reference parameter
 * expanded to include the golden and linked resources.
 * </p>
 * <p>
 * In addition, responses are cleaned up so that only the golden resource
 * is included in responses, and references to non-golden resources
 * are rewritten.
 * </p>
 * <p>
 * This interceptor does not modify data that is being stored/written
 * in any way, it only modifies data that is being returned by the
 * server.
 * </p>
 *
 * @since 8.0.0
 */
public class MdmReadVirtualizationInterceptor<P extends IResourcePersistentId<?>> {
	private static final Logger ourMdmTroubleshootingLog = Logs.getMdmTroubleshootingLog();

	private static final String CURRENTLY_PROCESSING_FLAG =
			MdmReadVirtualizationInterceptor.class.getName() + "_CURRENTLY_PROCESSING";

	private static final MdmSearchExpansionSvc.IParamTester PARAM_TESTER_NO_RES_ID = (paramName, param) -> {
		boolean hasChain = false;
		if (param instanceof ReferenceParam) {
			hasChain = ((ReferenceParam) param).hasChain();
		}
		return !hasChain && !IAnyResource.SP_RES_ID.equals(paramName);
	};

	private static final MdmSearchExpansionSvc.IParamTester PARAM_TESTER_ALL = (paramName, param) -> {
		boolean hasChain = false;
		if (param instanceof ReferenceParam) {
			hasChain = ((ReferenceParam) param).hasChain();
		}
		return !hasChain;
	};

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmSearchExpansionSvc myMdmSearchExpansionSvc;

	@Hook(
			value = Pointcut.STORAGE_PRESEARCH_REGISTERED,
			order = MdmConstants.ORDER_PRESEARCH_REGISTERED_MDM_READ_VIRTUALIZATION_INTERCEPTOR)
	public void preSearchRegistered(
			RequestDetails theRequestDetails,
			SearchParameterMap theSearchParameterMap,
			ICachedSearchDetails theSearchDetails) {
		ourMdmTroubleshootingLog
				.atTrace()
				.setMessage("MDM virtualization original search: {}{}")
				.addArgument(theRequestDetails.getResourceName())
				.addArgument(() -> theSearchParameterMap.toNormalizedQueryString(myFhirContext))
				.log();

		String resourceType = theSearchDetails.getResourceType();

		if (theSearchParameterMap.hasIncludes() || theSearchParameterMap.hasRevIncludes()) {
			myMdmSearchExpansionSvc.expandSearchAndStoreInRequestDetails(
					resourceType, theRequestDetails, theSearchParameterMap, PARAM_TESTER_ALL);
		} else {
			// If we don't have any includes, it's not worth auto-expanding the _id parameter since we'll only end
			// up filtering out the extra resources afterward
			myMdmSearchExpansionSvc.expandSearchAndStoreInRequestDetails(
					resourceType, theRequestDetails, theSearchParameterMap, PARAM_TESTER_NO_RES_ID);
		}

		ourMdmTroubleshootingLog
				.atDebug()
				.setMessage("MDM virtualization remapped search: {}{}")
				.addArgument(theRequestDetails.getResourceName())
				.addArgument(() -> theSearchParameterMap.toNormalizedQueryString(myFhirContext))
				.log();
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void preShowResources(RequestDetails theRequestDetails, IPreResourceShowDetails theDetails) {
		MdmSearchExpansionResults expansionResults = MdmSearchExpansionSvc.getCachedExpansionResults(theRequestDetails);
		if (expansionResults == null) {
			// This means the PRESEARCH hook didn't save anything, which probably means
			// no RequestDetails is available
			return;
		}

		if (theRequestDetails.getUserData().get(CURRENTLY_PROCESSING_FLAG) != null) {
			// Avoid recursive calls
			return;
		}

		/*
		 * If a resource being returned is a resource that was mdm-expanded,
		 * we'll replace that resource with the originally requested resource,
		 * making sure to avoid adding duplicates to the results.
		 */
		Set<IIdType> resourcesInBundle = new HashSet<>();
		for (int resourceIdx = 0; resourceIdx < theDetails.size(); resourceIdx++) {
			IBaseResource resource = theDetails.getResource(resourceIdx);
			IIdType id = resource.getIdElement().toUnqualifiedVersionless();
			Optional<IIdType> originalIdOpt = expansionResults.getOriginalIdForExpandedId(id);
			if (originalIdOpt.isPresent()) {
				IIdType originalId = originalIdOpt.get();
				if (resourcesInBundle.add(originalId)) {
					IBaseResource originalResource = fetchResourceFromRepository(theRequestDetails, originalId);
					theDetails.setResource(resourceIdx, originalResource);
				} else {
					theDetails.setResource(resourceIdx, null);
				}
			} else {
				if (!resourcesInBundle.add(id)) {
					theDetails.setResource(resourceIdx, null);
				}
			}
		}

		FhirTerser terser = myFhirContext.newTerser();

		for (IBaseResource resource : theDetails.getAllResources()) {
			// Extract all the references in the resources we're returning
			// in case we need to remap them to golden equivalents
			List<ResourceReferenceInfo> referenceInfos = terser.getAllResourceReferences(resource);
			for (ResourceReferenceInfo referenceInfo : referenceInfos) {
				IIdType referenceId = referenceInfo
						.getResourceReference()
						.getReferenceElement()
						.toUnqualifiedVersionless();
				if (referenceId.hasResourceType()
						&& referenceId.hasIdPart()
						&& !referenceId.isLocal()
						&& !referenceId.isUuid()) {
					Optional<IIdType> nonExpandedId = expansionResults.getOriginalIdForExpandedId(referenceId);
					if (nonExpandedId != null && nonExpandedId.isPresent()) {
						ourMdmTroubleshootingLog.debug(
								"MDM virtualization is replacing reference at {} value {} with {}",
								referenceInfo.getName(),
								referenceInfo.getResourceReference().getReferenceElement(),
								nonExpandedId.get().getValue());
						referenceInfo
								.getResourceReference()
								.setReference(nonExpandedId.get().getValue());
					}
				}
			}
		}

		ourMdmTroubleshootingLog
				.atTrace()
				.setMessage("Returning resources: {}")
				.addArgument(() -> theDetails.getAllResources().stream()
						.map(t -> t.getIdElement().toUnqualifiedVersionless().getValue())
						.sorted()
						.collect(Collectors.toList()))
				.log();
	}

	private IBaseResource fetchResourceFromRepository(RequestDetails theRequestDetails, IIdType originalId) {
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(originalId.getResourceType());
		theRequestDetails.getUserData().put(CURRENTLY_PROCESSING_FLAG, Boolean.TRUE);
		IBaseResource originalResource;
		try {
			originalResource = dao.read(originalId, theRequestDetails);
		} finally {
			theRequestDetails.getUserData().remove(CURRENTLY_PROCESSING_FLAG);
		}
		return originalResource;
	}
}
