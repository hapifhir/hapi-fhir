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
package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.r4.model.Patient;

class MergeValidationResult {
	final Patient sourceResource;
	final Patient targetResource;
	final boolean isValid;
	final Integer httpStatusCode;

	private MergeValidationResult(
			boolean theIsValid, Integer theHttpStatusCode, Patient theSourceResource, Patient theTargetResource) {
		isValid = theIsValid;
		httpStatusCode = theHttpStatusCode;
		sourceResource = theSourceResource;
		targetResource = theTargetResource;
	}

	public static MergeValidationResult invalidResult(int theHttpStatusCode) {
		return new MergeValidationResult(false, theHttpStatusCode, null, null);
	}

	public static MergeValidationResult validResult(Patient theSourceResource, Patient theTargetResource) {
		return new MergeValidationResult(true, null, theSourceResource, theTargetResource);
	}
}
