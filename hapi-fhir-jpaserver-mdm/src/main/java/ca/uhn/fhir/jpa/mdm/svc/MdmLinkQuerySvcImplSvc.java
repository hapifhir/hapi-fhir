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
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.entity.MdmLink;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public class MdmLinkQuerySvcImplSvc implements IMdmLinkQuerySvc {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkQuerySvcImplSvc.class);

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IdHelperService myIdHelperService;

	@Autowired
	IMdmModelConverterSvc myMdmModelConverterSvc;

	@Override
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		Example<MdmLink> exampleLink = exampleLinkFromParameters(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource);
		Page<MdmLink> mdmLinkByExample = myMdmLinkDaoSvc.findMdmLinkByExample(exampleLink, thePageRequest);
		Page<MdmLinkJson> map = mdmLinkByExample.map(myMdmModelConverterSvc::toJson);
		return map;
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		Example<MdmLink> exampleLink = exampleLinkFromParameters(null, null, MdmMatchResultEnum.POSSIBLE_DUPLICATE, null);
		Page<MdmLink> mdmLinkPage = myMdmLinkDaoSvc.findMdmLinkByExample(exampleLink, thePageRequest);
		Page<MdmLinkJson> map = mdmLinkPage.map(myMdmModelConverterSvc::toJson);
		return map;
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
