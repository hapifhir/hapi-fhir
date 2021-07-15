package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.data.domain.Page;

import javax.annotation.Nullable;

public interface IMdmControllerSvc {

	Page<MdmLinkJson> queryLinks(@Nullable String theGoldenResourceId, @Nullable String theSourceResourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest);

	Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest);

	void notDuplicateGoldenResource(String theGoldenResourceId, String theTargetGoldenResourceId, MdmTransactionContext theMdmTransactionContext);

	IAnyResource mergeGoldenResources(String theFromGoldenResourceId, String theToGoldenResourceId, IAnyResource theManuallyMergedGoldenResource, MdmTransactionContext theMdmTransactionContext);

	IAnyResource updateLink(String theGoldenResourceId, String theSourceResourceId, String theMatchResult, MdmTransactionContext theMdmTransactionContext);
}
