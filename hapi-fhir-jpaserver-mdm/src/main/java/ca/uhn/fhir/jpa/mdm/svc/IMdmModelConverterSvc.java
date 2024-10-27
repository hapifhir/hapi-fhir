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

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;

/**
 * Contract for decoupling API dependency from the base / JPA modules.
 */
public interface IMdmModelConverterSvc {

	/**
	 * Creates JSON representation of the provided MDM link
	 *
	 * @param theLink Link to convert
	 * @return Returns the converted link
	 */
	MdmLinkJson toJson(IMdmLink theLink);

	/**
	 * Creates JSON representation of the provided MDM link with revision data
	 *
	 * @param theMdmLinkRevision Link with revision data to convert
	 * @return Returns the converted link
	 */
	MdmLinkWithRevisionJson toJson(MdmLinkWithRevision<? extends IMdmLink<?>> theMdmLinkRevision);
}
