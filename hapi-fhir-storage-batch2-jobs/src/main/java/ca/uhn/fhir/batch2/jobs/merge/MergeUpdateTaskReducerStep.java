/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencePatchOutcomeJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateTaskReducerStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;

import java.util.Date;

public class MergeUpdateTaskReducerStep extends ReplaceReferenceUpdateTaskReducerStep<MergeJobParameters> {
	private final IHapiTransactionService myHapiTransactionService;
	private final MergeResourceHelper myMergeResourceHelper;

	public MergeUpdateTaskReducerStep(
			DaoRegistry theDaoRegistry,
			IHapiTransactionService theHapiTransactionService,
			MergeResourceHelper theMergeResourceHelper,
			MergeProvenanceSvc theMergeProvenanceSvc) {
		super(theDaoRegistry, theMergeProvenanceSvc);
		this.myHapiTransactionService = theHapiTransactionService;
		myMergeResourceHelper = theMergeResourceHelper;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<MergeJobParameters, ReplaceReferencePatchOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferenceResultsJson> theDataSink)
			throws JobExecutionFailedException {

		Date startTime = theStepExecutionDetails.getInstance().getStartTime();

		MergeJobParameters mergeJobParameters = theStepExecutionDetails.getParameters();
		SystemRequestDetails requestDetails =
				SystemRequestDetails.forRequestPartitionId(mergeJobParameters.getPartitionId());

		Patient resultResource = null;
		if (mergeJobParameters.getResultResource() != null) {
			resultResource =
					myFhirContext.newJsonParser().parseResource(Patient.class, mergeJobParameters.getResultResource());
		}

		myMergeResourceHelper.updateMergedResourcesAndCreateProvenance(
				myHapiTransactionService,
				mergeJobParameters.getSourceId().asIdDt(),
				mergeJobParameters.getTargetId().asIdDt(),
				getPatchOutputBundles(),
				resultResource,
				mergeJobParameters.getDeleteSource(),
				requestDetails,
				startTime);

		// Setting createProvenance to false. Because the provenance resource for merge has been created in the helper
		// method above. The reason is that the merge operation updates the target and source resources, unlike replace
		// references, and we would like the merge provenance to reference the target and source versions after the
		// update.
		return super.run(theStepExecutionDetails, theDataSink, false);
	}
}
