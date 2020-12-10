package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.stream.Stream;

/**
 * This service supports the MDM operation providers for those services that return multiple MDM links.
 */
public interface IMdmLinkQuerySvc {
	Stream<MdmLinkJson> queryLinks(IIdType theGoldenResourceId, IIdType theSourceResourceId, MdmMatchResultEnum theMatchResult, MdmLinkSourceEnum theLinkSource, MdmTransactionContext theMdmContext);
	Stream<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext);
}
