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
package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public class FindGoldenResourceCandidatesParams {

	/**
	 * The resource to find matches for
	 */
	private final IAnyResource myResource;

	/**
	 * The mdm context
	 */
	private final MdmTransactionContext myContext;

	public FindGoldenResourceCandidatesParams(IAnyResource theResource, MdmTransactionContext theContext) {
		myResource = theResource;
		myContext = theContext;
	}

	public IAnyResource getResource() {
		return myResource;
	}

	public MdmTransactionContext getContext() {
		return myContext;
	}
}
