package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
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
	public IAnyResource mergeGoldenResources(String theFromPersonId, String theToPersonId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource fromPerson = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromPersonId);
		IAnyResource toPerson = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToPersonId);
		myMdmControllerHelper.validateMergeResources(fromPerson, toPerson);
		myMdmControllerHelper.validateSameVersion(fromPerson, theFromPersonId);
		myMdmControllerHelper.validateSameVersion(toPerson, theToPersonId);

		return myGoldenResourceMergerSvc.mergeGoldenResources(fromPerson, toPerson, theMdmTransactionContext);
	}

	@Override
	public Stream<MdmLinkJson> queryLinks(@Nullable String thePersonId, @Nullable String theTargetId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext) {
		IIdType personId = MdmControllerUtil.extractPersonIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, thePersonId);
		IIdType targetId = MdmControllerUtil.extractTargetIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theTargetId);
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		MdmLinkSourceEnum linkSource = MdmControllerUtil.extractLinkSourceOrNull(theLinkSource);

		return myMdmLinkQuerySvc.queryLinks(personId, targetId, matchResult, linkSource, theMdmTransactionContext);
	}

	@Override
	public Stream<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext) {
		return myMdmLinkQuerySvc.getDuplicatePersons(theMdmTransactionContext);
	}

	@Override
	public IAnyResource updateLink(String theGoldenResourceId, String theTargetId, String theMatchResult, MdmTransactionContext theMdmTransactionContext) {
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		IAnyResource person = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		IAnyResource target = myMdmControllerHelper.getLatestTargetFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetId);
		myMdmControllerHelper.validateSameVersion(person, theGoldenResourceId);
		myMdmControllerHelper.validateSameVersion(target, theTargetId);

		return myIMdmLinkUpdaterSvc.updateLink(person, target, matchResult, theMdmTransactionContext);
	}

	@Override
	public void notDuplicateGoldenResource(String thePersonId, String theTargetPersonId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource person = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, thePersonId);
		IAnyResource target = myMdmControllerHelper.getLatestGoldenResourceFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetPersonId);

		myIMdmLinkUpdaterSvc.notDuplicatePerson(person, target, theMdmTransactionContext);
	}
}
