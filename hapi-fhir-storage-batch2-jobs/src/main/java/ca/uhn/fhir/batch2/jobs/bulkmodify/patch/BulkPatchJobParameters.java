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

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import static org.apache.commons.lang3.StringUtils.isBlank;

@SuppressWarnings("UnusedReturnValue")
public class BulkPatchJobParameters extends BaseBulkModifyJobParameters {

	@JsonProperty("fhirPatch")
	private String myFhirPatch;

	public BulkPatchJobParameters setFhirPatch(@Nonnull FhirContext theContext, @Nullable IBaseResource theFhirPatch) {
		myFhirPatch = theFhirPatch != null ? theContext.newJsonParser().encodeResourceToString(theFhirPatch) : null;
		return this;
	}

	public BulkPatchJobParameters setFhirPatch(@Nullable String theFhirPatch) {
		myFhirPatch = theFhirPatch;
		return this;
	}

	@Nullable
	public IBaseResource getFhirPatch(@Nonnull FhirContext theContext) {
		if (isBlank(myFhirPatch)) {
			return null;
		}
		return theContext.newJsonParser().parseResource(myFhirPatch);
	}
}
