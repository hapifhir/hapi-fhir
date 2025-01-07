/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.replacereferences.ReplaceReferencesRequest;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;

/**
 * Find all references to a source resource and replace them with references to the provided target
 */
public interface IReplaceReferencesSvc {

	/**
	 * Find all references to a source resource and replace them with references to the provided target
	 */
	IBaseParameters replaceReferences(
			ReplaceReferencesRequest theReplaceReferencesRequest, RequestDetails theRequestDetails);

	/**
	 * To support $merge preview mode, provide a count of how many references would be updated if replaceReferences
	 * was called
	 */
	Integer countResourcesReferencingResource(IIdType theResourceId, RequestDetails theRequestDetails);
}
