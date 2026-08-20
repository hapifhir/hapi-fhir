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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

import static ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx.STEP_ID_GENERATE_REPORT;

public class Step4And5WriteConceptsStep<OT extends IModelJson> extends BaseImportTerminologyStep
		implements IJobStepWorker<PreExpandValueSetParameters, WriteConceptsWorkChunkJson, OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(Step4And5WriteConceptsStep.class);

	private final boolean myInclude;

	@Autowired
	private ITermValueSetStorageSvc myTermValueSetStorageSvc;

	public Step4And5WriteConceptsStep(boolean theInclude) {
		myInclude = theInclude;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PreExpandValueSetParameters, WriteConceptsWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<OT> theDataSink)
			throws JobExecutionFailedException {

		WriteConceptsWorkChunkJson data = theStepExecutionDetails.getData();

		int startingOrder = data.getStartingOrder();
		int startingOrderOffset = data.getStartingOrderOffset();
		ValueSet delta = data.getValueSet();

		ourLog.atInfo()
				.setMessage("Writing {} concepts as {} with starting order {} and offset {}")
				.addArgument(delta.getExpansion().getContains().size())
				.addArgument(myInclude ? "INCLUDE" : "EXCLUDE")
				.addArgument(startingOrder)
				.addArgument(startingOrderOffset)
				.log();

		Callable<UploadStatistics> uploadFunction = () -> {
			if (myInclude) {
				return myTermValueSetStorageSvc.addConceptsToExpansion(delta, startingOrder + startingOrderOffset);
			} else {
				return myTermValueSetStorageSvc.removeConceptsFromExpansion(delta);
			}
		};
		UploadStatistics statistics = super.executeInNewTransactionWithRetry(uploadFunction, theStepExecutionDetails);

		TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter =
				new TerminologyFileSetJson.RecordsAddedCounter();
		recordsAddedCounter.increment(statistics);

		ExpandValueSetStepOutcomeJson outcome = new ExpandValueSetStepOutcomeJson();
		outcome.setStartingOrder(startingOrder);
		outcome.setStagingVersion(data.getStagingVersion());
		outcome.setSourceCompose(data.getValueSet().getCompose());
		outcome.setRecordsAddedCounter(recordsAddedCounter);
		theDataSink.acceptForFutureStep(STEP_ID_GENERATE_REPORT, outcome);

		return RunOutcome.SUCCESS;
	}
}
