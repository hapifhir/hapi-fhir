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

import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmQuerySearchParameters;
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
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource, thePageRequest, null, null);
		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest, List<Integer> thePartitionId) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(theGoldenResourceId, theSourceResourceId, theMatchResult, theLinkSource, thePageRequest, thePartitionId, null);
		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> queryLinks(MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmContext) {
		Page<? extends IMdmLink> mdmLinks = myMdmLinkDaoSvc.executeTypedQuery(theMdmQuerySearchParameters);
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
		MdmQuerySearchParameters mdmQuerySearchParameters =
			new MdmQuerySearchParameters(null, null, MdmMatchResultEnum.POSSIBLE_DUPLICATE, null, thePageRequest, thePartitionId, null);
		Page<? extends IMdmLink> mdmLinkPage = myMdmLinkDaoSvc.executeTypedQuery(mdmQuerySearchParameters);
		return mdmLinkPage.map(myMdmModelConverterSvc::toJson);
	}
}
