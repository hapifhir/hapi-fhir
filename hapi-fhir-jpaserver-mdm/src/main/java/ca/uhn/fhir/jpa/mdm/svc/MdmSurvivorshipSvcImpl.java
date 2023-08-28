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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {

	protected final FhirContext myFhirContext;

	private final DaoRegistry myDaoRegistry;

	private final IMdmLinkQuerySvc myMdmLinkQuerySvc;

	private final GoldenResourceHelper myGoldenResourceHelper;

//	@Autowired
//	private MdmLinkDaoSvc myMdmLinkDaoSvc;

	public MdmSurvivorshipSvcImpl(
		FhirContext theFhirContext,
		DaoRegistry theDaoRegistry,
		GoldenResourceHelper theResourceHelper,
		IMdmLinkQuerySvc theLinkDaoSvc
	) {
		myFhirContext = theFhirContext;
		myDaoRegistry = theDaoRegistry;
		myGoldenResourceHelper = theResourceHelper;
		myMdmLinkQuerySvc = theLinkDaoSvc;
	}

	/**
	 * Merges two golden resources by overwriting all field values on theGoldenResource param for CREATE_RESOURCE,
	 * UPDATE_RESOURCE, SUBMIT_RESOURCE_TO_MDM, UPDATE_LINK (when setting to MATCH) and MANUAL_MERGE_GOLDEN_RESOURCES.
	 * PID, identifiers and meta values are not affected by this operation.
	 *
	 * @param theTargetResource        Target resource to retrieve fields from
	 * @param theGoldenResource        Golden resource to merge fields into
	 * @param theMdmTransactionContext Current transaction context
	 * @param <T>
	 */
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

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <T extends IBase> T rebuildGoldenResourceCurrentLinksUsingSurvivorshipRules(
		T theGoldenResourceBase,
		MdmTransactionContext theMdmTransactionContext
	) {
		IBaseResource goldenResource = (IBaseResource) theGoldenResourceBase;
		Set<String> sourceIds = getAllValidLinkedSourceResourceIds((IAnyResource) goldenResource, theMdmTransactionContext);

		// get a list of links sorted by updated date
		MdmHistorySearchParameters parameters = new MdmHistorySearchParameters();
		parameters.setSourceIds(new ArrayList<>(sourceIds));
		parameters.setGoldenResourceIds(Collections.singletonList(goldenResource.getIdElement().getValueAsString()));
		List<MdmLinkWithRevisionJson> historyLinks = myMdmLinkQuerySvc.queryLinkHistory(parameters);

		// the result is immutable, but we need it non-immutable to sort
		historyLinks = new ArrayList<>(historyLinks);
		historyLinks.sort(Comparator.comparing(MdmLinkWithRevisionJson::getRevisionTimestamp));

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(goldenResource.fhirType());
		List<IBaseResource> sourceResources = new ArrayList<>();
		for (MdmLinkWithRevisionJson revisionLink : historyLinks) {
			MdmLinkJson link = revisionLink.getMdmLink();
			if (link.getMatchResult() != MdmMatchResultEnum.MATCH
					|| !sourceIds.contains(link.getSourceId())) {
				continue;
			}
			String sourceId = link.getSourceId();
			IResourcePersistentId pid = getSourcePersistenceId(sourceId);
			IBaseResource resource = dao.readByPid(pid);
			sourceResources.add(resource);
		}

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
		dao.update(toSave, new SystemRequestDetails());

		return (T) toSave;
	}

	private Set<String> getAllValidLinkedSourceResourceIds(
		IAnyResource theGoldenResource,
		MdmTransactionContext theTransactionContext
	) {
		Set<String> sourceIds = new HashSet<>();
		MdmQuerySearchParameters searchParameters = new MdmQuerySearchParameters(
			new MdmPageRequest(
				0,
				50,
				50,
				50
			));
		searchParameters.setGoldenResourceId(theGoldenResource.getIdElement());
		Page<MdmLinkJson> linksQuery = myMdmLinkQuerySvc.queryLinks(searchParameters, theTransactionContext);
		linksQuery.get().forEach(linkJson -> {
			if (linkJson.getMatchResult() == MdmMatchResultEnum.MATCH) {
				sourceIds.add(linkJson.getSourceId());
			}
		});

		return sourceIds;
	}

	protected IResourcePersistentId getSourcePersistenceId(String theIdStr) {
		IIdType idType = new IdType(theIdStr);
		JpaPid pid = JpaPid.fromIdAndResourceType(idType.getIdPartAsLong(), idType.getResourceType());
		return pid;
	}
}
