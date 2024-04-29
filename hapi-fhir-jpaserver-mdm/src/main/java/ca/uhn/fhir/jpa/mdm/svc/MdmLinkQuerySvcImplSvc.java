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

import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MdmLinkQuerySvcImplSvc implements IMdmLinkQuerySvc {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkQuerySvcImplSvc.class);

	@Autowired
	MdmLinkDaoSvc myMdmLinkDaoSvc;

	@Autowired
	IMdmModelConverterSvc myMdmModelConverterSvc;

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(
			IIdType theGoldenResourceId,
			IIdType theSourceResourceId,
			MdmMatchResultEnum theMatchResult,
			MdmLinkSourceEnum theLinkSource,
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
				.setGoldenResourceId(theGoldenResourceId)
				.setSourceId(theSourceResourceId)
				.setMatchResult(theMatchResult)
				.setLinkSource(theLinkSource);

		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Deprecated
	@Transactional
	public Page<MdmLinkJson> queryLinks(
			IIdType theGoldenResourceId,
			IIdType theSourceResourceId,
			MdmMatchResultEnum theMatchResult,
			MdmLinkSourceEnum theLinkSource,
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionIds) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
				.setGoldenResourceId(theGoldenResourceId)
				.setSourceId(theSourceResourceId)
				.setMatchResult(theMatchResult)
				.setLinkSource(theLinkSource)
				.setPartitionIds(thePartitionIds);

		return queryLinks(mdmQuerySearchParameters, theMdmContext);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> queryLinks(
			MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmContext) {
		@SuppressWarnings("unchecked")
		Page<? extends IMdmLink<?>> mdmLinks = myMdmLinkDaoSvc.executeTypedQuery(theMdmQuerySearchParameters);
		return mdmLinks.map(myMdmModelConverterSvc::toJson);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(
			MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest) {
		return getDuplicateGoldenResources(theMdmContext, thePageRequest, null, null);
	}

	@Override
	@Transactional
	public Page<MdmLinkJson> getDuplicateGoldenResources(
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionIds,
			String theRequestResourceType) {
		MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters(thePageRequest)
				.setMatchResult(MdmMatchResultEnum.POSSIBLE_DUPLICATE)
				.setPartitionIds(thePartitionIds)
				.setResourceType(theRequestResourceType);

		@SuppressWarnings("unchecked")
		Page<? extends IMdmLink<?>> mdmLinkPage = myMdmLinkDaoSvc.executeTypedQuery(mdmQuerySearchParameters);
		return mdmLinkPage.map(myMdmModelConverterSvc::toJson);
	}

	@Override
	public List<MdmLinkWithRevisionJson> queryLinkHistory(MdmHistorySearchParameters theMdmHistorySearchParameters) {
		@SuppressWarnings("unchecked")
		final List<MdmLinkWithRevision<? extends IMdmLink<?>>> mdmLinkHistoryFromDao =
				myMdmLinkDaoSvc.findMdmLinkHistory(theMdmHistorySearchParameters);

		Comparator<MdmLinkWithRevisionJson> linkHistoryComparator =
				Comparator.<MdmLinkWithRevisionJson, String>comparing(
								l -> l.getMdmLink().getGoldenResourceId())
						.thenComparing(l -> l.getMdmLink().getSourceId())
						.thenComparing(Comparator.comparingLong(MdmLinkWithRevisionJson::getRevisionNumber)
								.reversed());

		return mdmLinkHistoryFromDao.stream()
				.map(myMdmModelConverterSvc::toJson)
				.sorted(linkHistoryComparator)
				.collect(Collectors.toUnmodifiableList());
	}
}
