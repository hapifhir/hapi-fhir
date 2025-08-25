/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.patch;

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParametersValidator;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class BulkPatchJobParametersValidator<PT extends BulkPatchJobParameters>
		extends BaseBulkModifyJobParametersValidator<PT> {

	private final FhirContext myFhirContext;

	public BulkPatchJobParametersValidator(FhirContext theFhirContext, IDaoRegistry theDaoRegistry) {
		super(theDaoRegistry);
		myFhirContext = theFhirContext;
	}

	@Override
	protected void validateJobSpecificParameters(PT theParameters, List<String> theIssueListToPopulate) {
		validatePatch(theParameters, theIssueListToPopulate);
	}

	private void validatePatch(PT theParameters, List<String> theIssueListToPopulate) {
		IBaseResource patch;
		try {
			patch = theParameters.getFhirPatch(myFhirContext);
			if (patch == null) {
				theIssueListToPopulate.add("No Patch document was provided");
				return;
			}
		} catch (DataFormatException e) {
			theIssueListToPopulate.add("Failed to parse FHIRPatch document: " + e.getMessage());
			return;
		}

		if (!"Parameters".equals(myFhirContext.getResourceType(patch))) {
			theIssueListToPopulate.add(
					"FHIRPatch document must be a Parameters resource, found: " + myFhirContext.getResourceType(patch));
			return;
		}

		try {
			new FhirPatch(myFhirContext).validate(patch);
		} catch (InvalidRequestException e) {
			theIssueListToPopulate.add("Provided FHIRPatch document is invalid: " + e.getMessage());
		}
	}
}
