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
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.stream.Stream;

public class EmpiLinkQuerySvcImpl implements IEmpiLinkQuerySvc {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiLinkQuerySvcImpl.class);

	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	EmpiLinkDaoSvc myEmpiLinkDaoSvc;

	@Override
	public Stream<EmpiLinkJson> queryLinks(IIdType thePersonId, IIdType theTargetId, EmpiMatchResultEnum theMatchResult, EmpiLinkSourceEnum theLinkSource, EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(thePersonId, theTargetId, theMatchResult, theLinkSource);
		return myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink).stream()
			.filter(empiLink -> empiLink.getMatchResult() != EmpiMatchResultEnum.POSSIBLE_DUPLICATE)
			.map(this::toJson);
	}



	@Override
	public Stream<EmpiLinkJson> getDuplicatePersons(EmpiTransactionContext theEmpiContext) {
		Example<EmpiLink> exampleLink = exampleLinkFromParameters(null, null, EmpiMatchResultEnum.POSSIBLE_DUPLICATE, null);
		return myEmpiLinkDaoSvc.findEmpiLinkByExample(exampleLink).stream().map(this::toJson);
	}

	private EmpiLinkJson toJson(EmpiLink theLink) {
		EmpiLinkJson retval = new EmpiLinkJson();
		String targetId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getTargetPid()).toVersionless().getValue();
		retval.setTargetId(targetId);
		String personId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getPersonPid()).toVersionless().getValue();
		retval.setPersonId(personId);
		retval.setCreated(theLink.getCreated());
		retval.setEidMatch(theLink.getEidMatch());
		retval.setLinkSource(theLink.getLinkSource());
		retval.setMatchResult(theLink.getMatchResult());
		retval.setNewPerson(theLink.getNewPerson());
		retval.setScore(theLink.getScore());
		retval.setUpdated(theLink.getUpdated());
		retval.setVector(theLink.getVector());
		retval.setVersion(theLink.getVersion());
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
