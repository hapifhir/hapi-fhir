/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import jakarta.annotation.Nonnull;

/**
 * This is a bit of a deviation from our normal workflow. We need to use a reduction step here in order to return
 * the package outcome as the final result of the job as a whole. However, there will only ever be one input chunk,
 * so we are only actually reducing the list of outcomes within that one chunk.
 */
public class FinalizeInstallationStep
		implements IReductionStepWorker<
				PackageInstallationJobParameters, InstallationOutcomeJson, PackageInstallOutcomeJson> {

	private PackageInstallOutcomeJson myOutcome;

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson> theChunkDetails) {
		// Since recursively installing dependencies isn't in scope yet, there's nothing to consolidate
		myOutcome = theChunkDetails.getData().getOutcomes().stream().findFirst().orElse(null);

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageInstallOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		theDataSink.accept(myOutcome);

		return RunOutcome.SUCCESS;
	}

	@Override
	public IReductionStepWorker<PackageInstallationJobParameters, InstallationOutcomeJson, PackageInstallOutcomeJson>
			newInstance() {
		return new FinalizeInstallationStep();
	}
}
