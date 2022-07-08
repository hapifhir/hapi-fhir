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

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class MdmLinkQuerySvcImplSvc implements IMdmLinkQuerySvc {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkQuerySvcImplSvc.class);

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IMdmModelConverterSvc myMdmModelConverterSvc;

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		return queryLinks(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource, theMdmContext, thePageRequest, null);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		Page<MdmLink> mdmLinks = myMdmLinkDaoSvc.executeTypedQuery(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource, thePageRequest, thePartitionId);
		return mdmLinks.map(myMdmModelConverterSvc::toJson);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		return getDuplicateGoldenResources(theMdmContext, thePageRequest, null);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		Page<MdmLink> mdmLinkPage = myMdmLinkDaoSvc.executeTypedQuery(null, null, MdmMatchResultEnum.POSSIBLE_DUPLICATE, null, thePageRequest, thePartitionId);
		return mdmLinkPage.map(myMdmModelConverterSvc::toJson);
	}
}
