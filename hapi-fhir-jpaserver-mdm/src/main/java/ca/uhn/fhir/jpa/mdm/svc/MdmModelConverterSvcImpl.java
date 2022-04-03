package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import org.springframework.beans.factory.annotation.Autowired;

public class MdmModelConverterSvcImpl implements IMdmModelConverterSvc {

	@Autowired
	IJpaIdHelperService myIdHelperService;

	@Override
	public MdmLinkJson toJson(MdmLink theLink) {
		MdmLinkJson retVal = new MdmLinkJson();
		String sourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getSourcePid()).toVersionless().getValue();
		retVal.setSourceId(sourceId);
		String goldenResourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getGoldenResourcePid()).toVersionless().getValue();
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

}
