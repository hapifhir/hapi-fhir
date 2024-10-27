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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MdmSurvivorshipSvcImpl implements IMdmSurvivorshipService {
	private static final Pattern IS_UUID =
			Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

	protected final FhirContext myFhirContext;

	private final GoldenResourceHelper myGoldenResourceHelper;

	private final DaoRegistry myDaoRegistry;
	private final IMdmLinkQuerySvc myMdmLinkQuerySvc;

	private final IIdHelperService<?> myIIdHelperService;

	private final HapiTransactionService myTransactionService;

	public MdmSurvivorshipSvcImpl(
			FhirContext theFhirContext,
			GoldenResourceHelper theResourceHelper,
			DaoRegistry theDaoRegistry,
			IMdmLinkQuerySvc theLinkQuerySvc,
			IIdHelperService<?> theIIdHelperService,
			HapiTransactionService theHapiTransactionService) {
		myFhirContext = theFhirContext;
		myGoldenResourceHelper = theResourceHelper;
		myDaoRegistry = theDaoRegistry;
		myMdmLinkQuerySvc = theLinkQuerySvc;
		myIIdHelperService = theIIdHelperService;
		myTransactionService = theHapiTransactionService;
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
		Stream<IBaseResource> sourceResources =
				getMatchedSourceIdsByLinkUpdateDate(goldenResource, theMdmTransactionContext);

		IBaseResource toSave = myGoldenResourceHelper.createGoldenResourceFromMdmSourceResource(
				(IAnyResource) goldenResource,
				theMdmTransactionContext,
				null // we don't want to apply survivorship - just create a new GoldenResource
				);

		toSave.setId(goldenResource.getIdElement().toUnqualifiedVersionless());

		sourceResources.forEach(source -> {
			applySurvivorshipRulesToGoldenResource(source, toSave, theMdmTransactionContext);
		});

		// save it
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(goldenResource.fhirType());

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		// if using partitions, we should save to the correct partition
		Object resourcePartitionIdObj = toSave.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (resourcePartitionIdObj instanceof RequestPartitionId) {
			RequestPartitionId partitionId = (RequestPartitionId) resourcePartitionIdObj;
			requestDetails.setRequestPartitionId(partitionId);
		}
		dao.update(toSave, requestDetails);

		return (T) toSave;
	}

	@SuppressWarnings("rawtypes")
	private Stream<IBaseResource> getMatchedSourceIdsByLinkUpdateDate(
			IBaseResource theGoldenResource, MdmTransactionContext theMdmTransactionContext) {
		String resourceType = theGoldenResource.fhirType();
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

		MdmQuerySearchParameters searchParameters = new MdmQuerySearchParameters(new MdmPageRequest(0, 50, 50, 50));
		searchParameters.setGoldenResourceId(theGoldenResource.getIdElement());
		searchParameters.setSort("myUpdated");
		searchParameters.setMatchResult(MdmMatchResultEnum.MATCH);
		Page<MdmLinkJson> linksQuery = myMdmLinkQuerySvc.queryLinks(searchParameters, theMdmTransactionContext);

		// we want it ordered
		List<String> sourceIds = new ArrayList<>();
		linksQuery.forEach(link -> {
			String sourceId = link.getSourceId();
			// we want only the id part, not the resource type
			sourceId = sourceId.replace(resourceType + "/", "");
			sourceIds.add(sourceId);
		});
		Map<String, IResourcePersistentId> sourceIdToPid = new HashMap<>();
		if (!sourceIds.isEmpty()) {
			// we cannot call resolveResourcePersistentIds if there are no ids to call it with
			myTransactionService
					.withRequest(new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.allPartitions()))
					.execute(() -> {
						Map<String, ? extends IResourcePersistentId> ids =
								myIIdHelperService.resolveResourcePersistentIds(
										RequestPartitionId.allPartitions(), resourceType, sourceIds);
						sourceIdToPid.putAll(ids);
					});
		}

		return sourceIds.stream().map(id -> {
			IResourcePersistentId<?> pid = sourceIdToPid.get(id);
			return dao.readByPid(pid);
		});
	}
}
