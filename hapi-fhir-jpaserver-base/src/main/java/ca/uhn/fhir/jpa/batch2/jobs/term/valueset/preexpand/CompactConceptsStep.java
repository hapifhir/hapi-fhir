package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.model.entity.IdAndPartitionId;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CompactConceptsStep implements IJobStepWorker<PreExpandValueSetParameters, CompactConceptsWorkChunkJson, ExpandValueSetStepOutcomeJson> {

	@Autowired
	private ITermValueSetConceptDao myValueSetConceptDao;
	@Autowired
	private IHapiTransactionService myTxService;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PreExpandValueSetParameters, CompactConceptsWorkChunkJson> theStepExecutionDetails, @Nonnull IJobDataSink<ExpandValueSetStepOutcomeJson> theDataSink) throws JobExecutionFailedException {
		CompactConceptsWorkChunkJson data = theStepExecutionDetails.getData();

		myTxService.withSystemRequestOnDefaultPartition().execute(()->{
			for (CompactConceptsWorkChunkJson.Concept conceptDetails : data.getConcepts()) {

				Optional<TermValueSetConcept> conceptOpt = myValueSetConceptDao.findById(new IdAndPartitionId(conceptDetails.getId(), conceptDetails.getPartitionId()));
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
