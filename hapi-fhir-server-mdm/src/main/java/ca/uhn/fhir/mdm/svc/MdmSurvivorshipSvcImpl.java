/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {

	protected final FhirContext myFhirContext;

	private final GoldenResourceHelper myGoldenResourceHelper;

	private final DaoRegistry myDaoRegistry;
	private final IMdmLinkQuerySvc myMdmLinkQuerySvc;

	private final IIdHelperService<?> myIIdHelperService;

	public MdmSurvivorshipSvcImpl(
			FhirContext theFhirContext,
			GoldenResourceHelper theResourceHelper,
			DaoRegistry theDaoRegistry,
			IMdmLinkQuerySvc theLinkQuerySvc,
			IIdHelperService<?> theIIdHelperService
	) {
		myFhirContext = theFhirContext;
		myGoldenResourceHelper = theResourceHelper;
		myDaoRegistry = theDaoRegistry;
		myMdmLinkQuerySvc = theLinkQuerySvc;
		myIIdHelperService = theIIdHelperService;
	}

	// this logic is custom in smile vs hapi
	@Override
	public <T extends IBase> void applySurvivorshipRulesToGoldenResource(
			T theTargetResource, T theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		switch (theMdmTransactionContext.getRestOperation()) {
			case MERGE_GOLDEN_RESOURCES:
				TerserUtil.mergeFields(
						myFhirContext,
						(IBaseResource) theTargetResource,
						(IBaseResource) theGoldenResource,
						TerserUtil.EXCLUDE_IDS_AND_META);
				break;
			default:
				TerserUtil.replaceFields(
						myFhirContext,
						(IBaseResource) theTargetResource,
						(IBaseResource) theGoldenResource,
						TerserUtil.EXCLUDE_IDS_AND_META);
				break;
		}
	}

	// This logic is the same for all implementations (including jpa or mongo)
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <T extends IBase> T rebuildGoldenResourceWithSurvivorshipRules(
			T theGoldenResourceBase, MdmTransactionContext theMdmTransactionContext) {
		IBaseResource goldenResource = (IBaseResource) theGoldenResourceBase;

		// we want a list of source ids linked to this
		// golden resource id; sorted and filtered for only MATCH results
		List<IBaseResource> sourceResources = getMatchedSourceIdsSortedByUpdateDate(
			goldenResource,
			theMdmTransactionContext
		);

		IBaseResource toSave = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
			(IAnyResource) goldenResource,
			theMdmTransactionContext,
			null // we don't want to apply survivorship
		);
		toSave.setId(((IAnyResource) goldenResource).getId());

		for (IBaseResource source : sourceResources) {
			applySurvivorshipRulesToGoldenResource(source, toSave, theMdmTransactionContext);
		}

		// save it
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(goldenResource.fhirType());
		dao.update(toSave, new SystemRequestDetails());

		return (T) toSave;
	}

	private List<IBaseResource> getMatchedSourceIdsSortedByUpdateDate(
		IBaseResource theGoldenResource,
		MdmTransactionContext theTransactionContext
	) {
		// first we fetch all related source ids;
		// this includes all source ids (even NO_MATCH or POSSIBLE_MATCH)
		// and is not sorted
		Set<String> sourceIds = getAllValidLinkedSourceResourceIds((IAnyResource) theGoldenResource, theTransactionContext);

		// we'll sort and pare them down here
		return getMatchedSourceIdsSortedByUpdateDate(theGoldenResource,
			sourceIds);
	}

	protected List<IBaseResource> getMatchedSourceIdsSortedByUpdateDate(
		IBaseResource theGoldenResource, Set<String> theSourceIds) {
		String resourceType = theGoldenResource.fhirType();

		MdmHistorySearchParameters parameters = new MdmHistorySearchParameters();
		parameters.setSourceIds(new ArrayList<>(theSourceIds));
		parameters.setGoldenResourceIds(
			Collections.singletonList(theGoldenResource.getIdElement().getValueAsString()));
		List<MdmLinkWithRevisionJson> historyLinks = myMdmLinkQuerySvc.queryLinkHistory(parameters);

		// the result is immutable, but we need it non-immutable to sort
		historyLinks = new ArrayList<>(historyLinks);
		historyLinks.sort(Comparator.comparing(MdmLinkWithRevisionJson::getRevisionTimestamp));

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
		List<IBaseResource> sourceResources = new ArrayList<>();
		for (MdmLinkWithRevisionJson revisionLink : historyLinks) {
			MdmLinkJson link = revisionLink.getMdmLink();
			if (link.getMatchResult() != MdmMatchResultEnum.MATCH || !theSourceIds.contains(link.getSourceId())) {
				continue;
			}
			String sourceId = link.getSourceId();
			// +1 because of "/" in id: "ResourceType/Id"
			IResourcePersistentId<?> pid = getResourcePID(sourceId.substring(resourceType.length() + 1), resourceType);
			IBaseResource resource = dao.readByPid(pid);
			sourceResources.add(resource);
		}
		return sourceResources;
	}

	private Set<String> getAllValidLinkedSourceResourceIds(
		IAnyResource theGoldenResource, MdmTransactionContext theTransactionContext) {
		Set<String> sourceIds = new HashSet<>();
		MdmQuerySearchParameters searchParameters = new MdmQuerySearchParameters(new MdmPageRequest(0, 50, 50, 50));
		searchParameters.setGoldenResourceId(theGoldenResource.getIdElement());
		Page<MdmLinkJson> linksQuery = myMdmLinkQuerySvc.queryLinks(searchParameters, theTransactionContext);
		linksQuery.get().forEach(linkJson -> {
			if (linkJson.getMatchResult() == MdmMatchResultEnum.MATCH) {
				sourceIds.add(linkJson.getSourceId());
			}
		});

		return sourceIds;
	}

	private IResourcePersistentId<?> getResourcePID(String theId, String theResourceType) {
		return myIIdHelperService.newPidFromStringIdAndResourceName(theId, theResourceType);
	}
}
