/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MdmModelConverterSvcImpl implements IMdmModelConverterSvc {

	@SuppressWarnings("rawtypes")
	@Autowired
	IIdHelperService myIdHelperService;

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public MdmLinkJson toJson(IMdmLink theLink) {
		MdmLinkJson retVal = new MdmLinkJson();
		String sourceId = myIdHelperService
				.resourceIdFromPidOrThrowException(theLink.getSourcePersistenceId(), theLink.getMdmSourceType())
				.toVersionless()
				.getValue();
		retVal.setSourceId(sourceId);
		if (theLink.getSourcePersistenceId() != null) {
			retVal.setSourcePid(theLink.getSourcePersistenceId());
		}
		String goldenResourceId = myIdHelperService
				.resourceIdFromPidOrThrowException(theLink.getGoldenResourcePersistenceId(), theLink.getMdmSourceType())
				.toVersionless()
				.getValue();
		retVal.setGoldenResourceId(goldenResourceId);
		if (theLink.getGoldenResourcePersistenceId() != null) {
			retVal.setGoldenPid(theLink.getGoldenResourcePersistenceId());
		}
		retVal.setCreated(theLink.getCreated());
		retVal.setEidMatch(theLink.getEidMatch());
		retVal.setLinkSource(theLink.getLinkSource());
		retVal.setMatchResult(theLink.getMatchResult());
		retVal.setLinkCreatedNewResource(theLink.getHadToCreateNewGoldenResource());
		Double score = theLink.getScore() == null
				? null
				: BigDecimal.valueOf(theLink.getScore())
						.setScale(4, RoundingMode.HALF_UP)
						.doubleValue();
		retVal.setScore(score);
		retVal.setUpdated(theLink.getUpdated());
		retVal.setVersion(theLink.getVersion());
		retVal.setRuleCount(theLink.getRuleCount());
		return retVal;
	}

	@Override
	public MdmLinkWithRevisionJson toJson(MdmLinkWithRevision<? extends IMdmLink<?>> theMdmLinkRevision) {
		final MdmLinkJson mdmLinkJson = toJson(theMdmLinkRevision.getMdmLink());

		return new MdmLinkWithRevisionJson(
				mdmLinkJson,
				theMdmLinkRevision.getEnversRevision().getRevisionNumber(),
				theMdmLinkRevision.getEnversRevision().getRevisionTimestamp());
	}
}
