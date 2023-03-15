package ca.uhn.fhir.jpa.mdm.svc;

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

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class MdmModelConverterSvcImpl implements IMdmModelConverterSvc {

	@Autowired
	IIdHelperService myIdHelperService;

	@Override
	public MdmLinkJson toJson(IMdmLink theLink) {
		MdmLinkJson retVal = new MdmLinkJson();
		String sourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getSourcePersistenceId(), theLink.getMdmSourceType()).toVersionless().getValue();
		retVal.setSourceId(sourceId);
		String goldenResourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getGoldenResourcePersistenceId(), theLink.getMdmSourceType()).toVersionless().getValue();
		retVal.setGoldenResourceId(goldenResourceId);
		retVal.setCreated(theLink.getCreated());
		retVal.setEidMatch(theLink.getEidMatch());
		retVal.setLinkSource(theLink.getLinkSource());
		retVal.setMatchResult(theLink.getMatchResult());
		retVal.setLinkCreatedNewResource(theLink.getHadToCreateNewGoldenResource());
		retVal.setScore(theLink.getScore());
		retVal.setUpdated(theLink.getUpdated());
		retVal.setVector(theLink.getVector());
		retVal.setVersion(theLink.getVersion());
		retVal.setRuleCount(theLink.getRuleCount());
		return retVal;
	}

	@Override
	public MdmLinkRevisionJson toJson(MdmLinkWithRevision<? extends IMdmLink<?>> theMdmLinkRevision) {
		final MdmLinkJson mdmLinkJson = toJson(theMdmLinkRevision.getMdmLink());

		// TODO:  what are the required methods and what do they do?
		// TODO: handle Instant properly
		return new MdmLinkRevisionJson(mdmLinkJson, theMdmLinkRevision.getEnversRevision().getRevisionNumber(), theMdmLinkRevision.getEnversRevision().getRevisionTimestamp());
	}

	// TODO: THIS IS NOT ACCURATE PRODUCTION BEHAVIOUR: get rid of this once we've merged the envers changes to master and this branch
	private static MdmLinkJson toJsonTmp(IMdmLink theLink) {
		final MdmLinkJson retVal = new MdmLinkJson();
		retVal.setSourceId(theLink.getSourcePersistenceId().getId().toString());
		retVal.setGoldenResourceId(theLink.getGoldenResourcePersistenceId().getId().toString());
		retVal.setCreated(theLink.getCreated());
		retVal.setEidMatch(theLink.getEidMatch());
		retVal.setLinkSource(theLink.getLinkSource());
		retVal.setMatchResult(theLink.getMatchResult());
		retVal.setLinkCreatedNewResource(theLink.getHadToCreateNewGoldenResource());
		retVal.setScore(theLink.getScore());
		retVal.setUpdated(theLink.getUpdated());
		retVal.setVector(theLink.getVector());
		retVal.setVersion(theLink.getVersion());
		retVal.setRuleCount(theLink.getRuleCount());
		return retVal;
	}
}
