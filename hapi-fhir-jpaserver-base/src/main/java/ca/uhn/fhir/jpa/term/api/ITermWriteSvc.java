/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.api;

import org.hl7.fhir.instance.model.api.IBaseResource;

public interface ITermWriteSvc {

	/**
	 * Prepares a code system version for staging, meaning that it is ready to begin acceping
	 * new codes, properties, relationships, etc. This method will create the new version
	 * if one does not already exist but will leave the existing one untouched if it does.
	 * <p>
	 * The new version will not be activated if it is not already active.
	 * </p>
	 */
	StartStagingCodeSystemVersionResponse startStagingCodeSystemVersion(String theCodeSystemUrl, String theVersionId);


	UploadCodeSystemConceptsResponse uploadCodeSystemConcepts(IBaseResource theCodeSystem);



	/**
	 * @param stagingVersionId A temporary ID associated with the version that is being staged
	 */
	record StartStagingCodeSystemVersionResponse(String stagingVersionId) {}

	record UploadCodeSystemConceptsResponse(int conceptsAdded, int conceptLinksAdded, int propertiesAdded, int designationsAdded) {}

}
