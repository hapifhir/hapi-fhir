package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.provider.MdmControllerHelper;
import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * This class acts as a layer between MdmProviders and MDM services to support a REST API that's not a FHIR Operation API.
 */
@Service
public class MdmControllerSvcImpl implements IMdmControllerSvc {
	@Autowired
	MdmControllerHelper myMdmControllerHelper;
	@Autowired
	IGoldenResourceMergerSvc myGoldenResourceMergerSvc;
	@Autowired
	IMdmLinkQuerySvc myMdmLinkQuerySvc;
	@Autowired
	IMdmLinkUpdaterSvc myIMdmLinkUpdaterSvc;

	@Override
	public IAnyResource mergeGoldenResources(String theFromGoldenResourceId, String theToGoldenResourceId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource fromGoldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResourceId);
		IAnyResource toGoldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResourceId);
		myMdmControllerHelper.validateMergeResources(fromGoldenResource, toGoldenResource);
		myMdmControllerHelper.validateSameVersion(fromGoldenResource, theFromGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(toGoldenResource, theToGoldenResourceId);

		return myGoldenResourceMergerSvc.mergeGoldenResources(fromGoldenResource, toGoldenResource, theMdmTransactionContext);
	}

	@Override
	public Stream<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		IIdType goldenResourceId = MdmControllerUtil.extractGoldenResourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IIdType sourceId = MdmControllerUtil.extractSourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theSourceResourceId);
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		MdmLinkSourceEnum linkSource = MdmControllerUtil.extractLinkSourceOrNull(theLinkSource);

		return myMdmLinkQuerySvc.queryLinks(goldenResourceId, sourceId, matchResult, linkSource, theMdmTransactionContext);
	}

	@Override
	public Stream<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext) {
		return myMdmLinkQuerySvc.getDuplicateGoldenResources(theMdmTransactionContext);
	}

	@Override
	public IAnyResource updateLink(String theGoldenResourceId, String theSourceResourceId, String theMatchResult, MdmTransactionContext theMdmTransactionContext) {
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		IAnyResource goldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource source = myMdmControllerHelper.getLatestSourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theSourceResourceId);
		myMdmControllerHelper.validateSameVersion(goldenResource, theGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(source, theSourceResourceId);

		return myIMdmLinkUpdaterSvc.updateLink(goldenResource, source, matchResult, theMdmTransactionContext);
	}

	@Override
	public void notDuplicateGoldenResource(String theGoldenResourceId, String theTargetGoldenResourceId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource goldenResource = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource target = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetGoldenResourceId);

		myIMdmLinkUpdaterSvc.notDuplicateGoldenResource(goldenResource, target, theMdmTransactionContext);
	}
}
