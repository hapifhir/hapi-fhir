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
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * This step updates the {@link TermValueSetConcept#setOrder(int) concept orders} to
 * get rid of any gaps. This is necessary because when we search the concepts we use
 * the order indexes for specifying paging, e.g. in
 * {@link ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptViewDao#findByTermValueSetId(int, int, Long)}.
 */
public class Step7CompactConceptsStep
		implements IJobStepWorker<
				PreExpandValueSetParameters, CompactConceptsWorkChunkJson, ExpandValueSetStepOutcomeJson> {

	@Autowired
	private ITermValueSetConceptDao myValueSetConceptDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PreExpandValueSetParameters, CompactConceptsWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<ExpandValueSetStepOutcomeJson> theDataSink)
			throws JobExecutionFailedException {
		CompactConceptsWorkChunkJson data = theStepExecutionDetails.getData();

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			for (CompactConceptsWorkChunkJson.Concept conceptDetails : data.getConcepts()) {

				Optional<TermValueSetConcept> conceptOpt = myValueSetConceptDao.findById(
						new IdAndPartitionId(conceptDetails.getId(), conceptDetails.getPartitionId()));
				if (conceptOpt.isPresent()) {
					TermValueSetConcept concept = conceptOpt.get();
					concept.setOrder(conceptDetails.getOrder());
					myValueSetConceptDao.save(concept);
				}
			}
		});

		return RunOutcome.SUCCESS;
	}
}
