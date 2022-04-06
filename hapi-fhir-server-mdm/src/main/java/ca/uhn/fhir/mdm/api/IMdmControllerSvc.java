package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.data.domain.Page;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public interface IMdmControllerSvc {

	@Deprecated
	Page<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest);

	Page<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest, RequestDetails theRequestDetails);

	Page<MdmLinkJson> queryLinksFromPartitionList(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest, List<Integer> thePartitionIds);

	Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest);

	Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest, RequestDetails theRequestDetails);

	void notDuplicateGoldenResource(String theGoldenResourceId, String theTargetGoldenResourceId, MdmTransactionContext theMdmTransactionContext);

	IAnyResource mergeGoldenResources(String theFromGoldenResourceId, String theToGoldenResourceId, IAnyResource theManuallyMergedGoldenResource, MdmTransactionContext theMdmTransactionContext);

	IAnyResource updateLink(String theGoldenResourceId, String theSourceResourceId, String theMatchResult, MdmTransactionContext theMdmTransactionContext);

	IAnyResource createLink(String theGoldenResourceId, String theSourceResourceId, @Nullable String theMatchResult, MdmTransactionContext theMdmTransactionContext);

	IBaseParameters submitMdmClearJob(List<String> theUrls, IPrimitiveType<BigDecimal> theBatchSize, ServletRequestDetails theRequestDetails);
}
