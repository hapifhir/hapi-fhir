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

import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationRequest;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.api.ResourceModificationResponse;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyResourcesStep;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidAndVersionJson;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BulkPatchModifyResourcesStep<PT extends BulkPatchJobParameters>
		extends BaseBulkModifyResourcesStep<PT, BulkPatchModificationContext> {

	@Autowired
	private FhirContext myFhirContext;

	private final boolean myRewriteHistory;

	public BulkPatchModifyResourcesStep(boolean theRewriteHistory) {
		myRewriteHistory = theRewriteHistory;
	}

	@Nullable
	@Override
	protected BulkPatchModificationContext preModifyResources(
			PT theJobParameters, List<TypedPidAndVersionJson> thePids) {
		IBaseResource patch = theJobParameters.getFhirPatch(myFhirContext);
		return new BulkPatchModificationContext(patch);
	}

	@Nonnull
	@Override
	protected ResourceModificationResponse modifyResource(
			PT theJobParameters,
			BulkPatchModificationContext theModificationContext,
			@Nonnull ResourceModificationRequest theModificationRequest) {
		IBaseResource resourceToPatch = theModificationRequest.getResource();
		IBaseResource patchToApply = theModificationContext.getPatch();
		new FhirPatch(myFhirContext).apply(resourceToPatch, patchToApply);
		return ResourceModificationResponse.updateResource(resourceToPatch);
	}

	@Override
	protected boolean isRewriteHistory(BulkPatchModificationContext theState, IBaseResource theResource) {
		return myRewriteHistory;
	}
}
