package ca.uhn.fhir.jpa.mdm.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.stream.Stream;

public class MdmLinkQuerySvcImpl implements IMdmLinkQuerySvc {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkQuerySvcImpl.class);

	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Override
	public Stream<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext) {
		Example<MdmLink> exampleLink = exampleLinkFromParameters(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource);
		return myMdmLinkDaoSvc.findMdmLinkByExample(exampleLink).stream()
			.filter(mdmLink -> mdmLink.getMatchResult() != MdmMatchResultEnum.POSSIBLE_DUPLICATE)
			.map(this::toJson);
	}

	@Override
	public Stream<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext) {
		Example<MdmLink> exampleLink = exampleLinkFromParameters(null, null, MdmMatchResultEnum.POSSIBLE_DUPLICATE, null);
		return myMdmLinkDaoSvc.findMdmLinkByExample(exampleLink).stream().map(this::toJson);
	}

	private MdmLinkJson toJson(MdmLink theLink) {
		MdmLinkJson retval = new MdmLinkJson();
		String sourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getSourcePid()).toVersionless().getValue();
		retval.setSourceId(sourceId);
		String goldenResourceId = myIdHelperService.resourceIdFromPidOrThrowException(theLink.getGoldenResourcePid()).toVersionless().getValue();
		retval.setGoldenResourceId(goldenResourceId);
		retval.setCreated(theLink.getCreated());
		retval.setEidMatch(theLink.getEidMatch());
		retval.setLinkSource(theLink.getLinkSource());
		retval.setMatchResult(theLink.getMatchResult());
		retval.setLinkCreatedNewResource(theLink.getHadToCreateNewGoldenResource());
		retval.setScore(theLink.getScore());
		retval.setUpdated(theLink.getUpdated());
		retval.setVector(theLink.getVector());
		retval.setVersion(theLink.getVersion());
		return retval;
	}

	private Example<MdmLink> exampleLinkFromParameters(IIdType theGoldenResourceId, IIdType theSourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource) {
		MdmLink mdmLink = myMdmLinkDaoSvc.newMdmLink();
		if (theGoldenResourceId != null) {
			mdmLink.setGoldenResourcePid(myIdHelperService.getPidOrThrowException(theGoldenResourceId));
		}
		if (theSourceId != null) {
			mdmLink.setSourcePid(myIdHelperService.getPidOrThrowException(theSourceId));
		}
		if (theMatchResult != null) {
			mdmLink.setMatchResult(theMatchResult);
		}
		if (theLinkSource != null) {
			mdmLink.setLinkSource(theLinkSource);
		}
		return Example.of(mdmLink);
	}
}
