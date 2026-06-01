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
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;

public class ImportTerminologyStepGenerateConceptClosures<PT extends BaseTerminologyImportParameters>
		implements IJobStepWorker<PT, TerminologyFileSetJson, TerminologyFileSetJson>,
				ITerminologyImportFileHandlerStep<PT, TerminologyFileSetJson, TerminologyFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportTerminologyStepGenerateConceptClosures.class);

	@Autowired
	private ITermConceptDao myConceptDao;

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		StopWatch sw = new StopWatch();

		Integer defaultPartitionId = myPartitionSettings.getDefaultPartitionId();
		List<TermConcept.TermConceptPk> ids = data.getConceptPidsToGenerateClosureFor().stream()
				.map(t -> new TermConcept.TermConceptPk(t, defaultPartitionId))
				.toList();

		// Actually calculate the hierarchy closures
		myTxService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> QueryChunker.chunk(ids, this::generateClosures));

		ourLog.atInfo()
				.setMessage("Calculated hierarchy closure for {} concepts in {}. {}/sec")
				.addArgument(ids.size())
				.addArgument(sw)
				.addArgument(sw.formatThroughput(ids.size(), TimeUnit.SECONDS))
				.log();

		// Emit statistics
		TerminologyFileSetJson outputChunk = new TerminologyFileSetJson();
		outputChunk
				.getRecordsAddedCounter(theStepExecutionDetails.getCurrentStepId())
				.incrementOtherChanges(ids.size());
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, outputChunk);

		return RunOutcome.SUCCESS;
	}

	private void generateClosures(List<TermConcept.TermConceptPk> ids) {
		List<TermConcept> concepts = myConceptDao.findAllById(ids);
		for (TermConcept concept : concepts) {
			concept.setParentPids(null);
			concept.prePersist();
			myEntityManager.merge(concept);
		}

		myEntityManager.flush();
	}

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
			StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, PT theJobParameters, String theFileName) {
		// This step doesn't process any files
		return Optional.empty();
	}

	@Override
	public boolean mustFindFile() {
		// This step doesn't process any files
		return false;
	}
}
