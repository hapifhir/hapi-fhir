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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

public class ImportTerminologyStepChunkConceptsForGeneratingClosure<PT extends TerminologyImportParameters>
		extends BaseImportTerminologyStep
		implements IJobStepWorker<PT, TerminologyFileSetJson, TerminologyFileSetJson>,
				ITerminologyImportFileHandlerStep<PT, TerminologyFileSetJson, TerminologyFileSetJson> {

	@Autowired
	private ITermConceptDao myConceptDao;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		myTxService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> generateClosures(theStepExecutionDetails, theDataSink));

		return RunOutcome.SUCCESS;
	}

	private void generateClosures(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink) {
		HapiTransactionService.requireTransaction();

		ImportTerminologyMetadataAttachmentJson jobMetadata =
				getJobMetadata(theStepExecutionDetails.getInstance().getInstanceId());
		String stagingVersion = jobMetadata.getCodeSystemStagingVersionId();

		TermCodeSystemVersion csv = myCodeSystemVersionDao.findByCodeSystemUriAndVersion(
				jobMetadata.getCodeSystem().getUrl(), stagingVersion);
		Validate.notNull(
				csv,
				"Could not find code system version %s for code system: %s",
				stagingVersion,
				jobMetadata.getCodeSystem().getUrl());

		Stream<TermConcept.TermConceptPk> pids = myConceptDao.findPidsByCodeSystemVersion(csv);
		int pidCount = 0;
		TerminologyFileSetJson outputChunk = new TerminologyFileSetJson();
		for (Iterator<TermConcept.TermConceptPk> iter = pids.iterator(); iter.hasNext(); ) {
			outputChunk.getConceptPidsToGenerateClosureFor().add(iter.next().getId());
			pidCount++;

			if (outputChunk.getConceptPidsToGenerateClosureFor().size() >= 2000 || !iter.hasNext()) {
				theDataSink.accept(outputChunk);
				outputChunk = new TerminologyFileSetJson();
			}
		}

		// Emit statistics
		outputChunk = new TerminologyFileSetJson();
		outputChunk
				.getRecordsAddedCounter(theStepExecutionDetails.getCurrentStepId())
				.incrementOtherChanges(pidCount);
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, outputChunk);
	}

	@Nonnull
	@Override
	public List<BaseImportTerminologyFileCsvStep.LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<PT, ?> theStepExecutionDetails) {
		// This step doesn't process any files
		return List.of();
	}

	@Override
	public boolean mustFindFile() {
		// This step doesn't process any files
		return false;
	}
}
