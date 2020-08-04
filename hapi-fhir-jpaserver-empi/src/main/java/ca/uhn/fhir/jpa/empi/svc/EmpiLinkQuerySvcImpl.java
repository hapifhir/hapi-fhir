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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.stream.Collectors;

public class EmpiLinkQuerySvcImpl implements IEmpiLinkQuerySvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkQuerySvcImpl.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Override
	public IBaseParameters queryLinks(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(thePersonId, theTargetId, theMatchResult, theLinkSource);
		List<EmpiLink> empiLinks = myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink).stream()
			.filter(empiLink -> empiLink.getMatchResult() != EmpiMatchResultEnum.POSSIBLE_DUPLICATE)
			.collect(Collectors.toList());
		// TODO RC1 KHS page results
		return parametersFromEmpiLinks(empiLinks, true);
	}

	@Override
	public IBaseParameters getPossibleDuplicates(EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(null, null, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, null);
		List<EmpiLink> empiLinks = myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink);
		// TODO RC1 page results
		return parametersFromEmpiLinks(empiLinks, false);
	}

	private IBaseParameters parametersFromEmpiLinks(List<EmpiLink> theEmpiLinks, boolean includeResultAndSource) {
		IBaseParameters retval = ParametersUtil.newInstance(myFhirContext);

		for (EmpiLink empiLink : theEmpiLinks) {
			IBase resultPart = ParametersUtil.addParameterToParameters(myFhirContext, retval, "link");
			String personId = myIdHelperService.resourceIdFromPidOrThrowException(empiLink.getPersonPid()).toVersionless().getValue();
			ParametersUtil.addPartString(myFhirContext, resultPart, "personId", personId);

			String targetId = myIdHelperService.resourceIdFromPidOrThrowException(empiLink.getTargetPid()).toVersionless().getValue();
			ParametersUtil.addPartString(myFhirContext, resultPart, "targetId", targetId);

			if (includeResultAndSource) {
				ParametersUtil.addPartString(myFhirContext, resultPart, "matchResult", empiLink.getMatchResult().name());
				ParametersUtil.addPartString(myFhirContext, resultPart, "linkSource", empiLink.getLinkSource().name());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "eidMatch", empiLink.getEidMatch());
				ParametersUtil.addPartBoolean(myFhirContext, resultPart, "newPerson", empiLink.getNewPerson());
				ParametersUtil.addPartDecimal(myFhirContext, resultPart, "score", empiLink.getScore());
			}
		}
		return retval;
	}

	private Example<EmpiLink> exampleLinkFromParameters(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource) {
		EmpiLink empiLink = myEmpiLinkDaoSvc.newEmpiLink();
		if (thePersonId != null) {
			empiLink.setPersonPid(myIdHelperService.getPidOrThrowException(thePersonId));
		}
		if (theTargetId != null) {
			empiLink.setTargetPid(myIdHelperService.getPidOrThrowException(theTargetId));
		}
		if (theMatchResult != null) {
			empiLink.setMatchResult(theMatchResult);
		}
		if (theLinkSource != null) {
			empiLink.setLinkSource(theLinkSource);
		}
		return Example.of(empiLink);
	}
}
