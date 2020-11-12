package ca.uhn.fhir.jpa.empi.svc;

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

import ca.uhn.fhir.empi.api.EmpiLinkJson;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.empi.model.MdmTransactionContext;
import ca.uhn.fhir.empi.provider.EmpiControllerHelper;
import ca.uhn.fhir.empi.provider.EmpiControllerUtil;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * This class acts as a layer between EmpiProviders and EMPI services to support a REST API that's not a FHIR Operation API.
 */
@Service
public class EmpiControllerSvcImpl implements IEmpiControllerSvc {
	@Autowired
	EmpiControllerHelper myEmpiControllerHelper;
	@Autowired
	IGoldenResourceMergerSvc myEmpiPersonMergerSvc;
	@Autowired
	IEmpiLinkQuerySvc myEmpiLinkQuerySvc;
	@Autowired
	IEmpiLinkUpdaterSvc myIEmpiLinkUpdaterSvc;

	@Override
	public IAnyResource mergeGoldenResources(String theFromPersonId, String theToPersonId, MdmTransactionContext theMdmTransactionContext) {
		IAnyResource fromPerson = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromPersonId);
		IAnyResource toPerson = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToPersonId);
		myEmpiControllerHelper.validateMergeResources(fromPerson, toPerson);
		myEmpiControllerHelper.validateSameVersion(fromPerson, theFromPersonId);
		myEmpiControllerHelper.validateSameVersion(toPerson, theToPersonId);

		return myEmpiPersonMergerSvc.mergeGoldenResources(fromPerson, toPerson, theMdmTransactionContext);
	}

	@Override
	public Stream<EmpiLinkJson> queryLinks(@Nullable String thePersonId, @Nullable String theTargetId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theEmpiContext) {
		IIdType personId = EmpiControllerUtil.extractPersonIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, thePersonId);
		IIdType targetId = EmpiControllerUtil.extractTargetIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theTargetId);
		EmpiMatchResultEnum matchResult = EmpiControllerUtil.extractMatchResultOrNull(theMatchResult);
		EmpiLinkSourceEnum linkSource = EmpiControllerUtil.extractLinkSourceOrNull(theLinkSource);

		return myEmpiLinkQuerySvc.queryLinks(personId, targetId, matchResult, linkSource, theEmpiContext);
	}

	@Override
	public Stream<EmpiLinkJson> getDuplicateGoldenResources(MdmTransactionContext theEmpiContext) {
		return myEmpiLinkQuerySvc.getDuplicatePersons(theEmpiContext);
	}

	@Override
	public IAnyResource updateLink(String thePersonId, String theTargetId, String theMatchResult, MdmTransactionContext theEmpiContext) {
		EmpiMatchResultEnum matchResult = EmpiControllerUtil.extractMatchResultOrNull(theMatchResult);
		IAnyResource person = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, thePersonId);
		IAnyResource target = myEmpiControllerHelper.getLatestTargetFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetId);
		myEmpiControllerHelper.validateSameVersion(person, thePersonId);
		myEmpiControllerHelper.validateSameVersion(target, theTargetId);

		return myIEmpiLinkUpdaterSvc.updateLink(person, target, matchResult, theEmpiContext);
	}

	@Override
	public void notDuplicateGoldenResource(String thePersonId, String theTargetPersonId, MdmTransactionContext theEmpiContext) {
		IAnyResource person = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_GOLDEN_RESOURCE_ID, thePersonId);
		IAnyResource target = myEmpiControllerHelper.getLatestPersonFromIdOrThrowException(ProviderConstants.MDM_UPDATE_LINK_RESOURCE_ID, theTargetPersonId);

		myIEmpiLinkUpdaterSvc.notDuplicatePerson(person, target, theEmpiContext);
	}
}
