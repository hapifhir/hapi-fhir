package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.StreamIterator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

/**
 * After all of the inclusions and exclusions have been processed, this step fetches
 * the final list of concepts. We do this for two reasons:
 * <ul>
 * <li>
 *     So that we can apply an accurate total concept count to {@link TermValueSet#setTotalConcepts(Long)}.
 * </li>
 * <li>
 *     We feed the list of concepts to {@link Step7CompactConceptsStep} which then
 *     renumbers the concept {@link TermValueSetConcept#setOrder(int) orders} to
 *     get rid of any gaps.
 * </li>
 * </ul>
 */
public class Step6LoadAllConceptIdsStep
		implements IJobStepWorker<
				PreExpandValueSetParameters, LoadAllConceptIdsWorkChunkJson, CompactConceptsWorkChunkJson> {

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

	@Autowired
	private ITermValueSetConceptDao myTermValueSetConceptDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private EntityManager myEntityManager;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PreExpandValueSetParameters, LoadAllConceptIdsWorkChunkJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<CompactConceptsWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		String url = theStepExecutionDetails.getParameters().getCanonicalUrl().url();
		String version = theStepExecutionDetails.getData().getStagingVersionId();

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermValueSet termValueSet = myTermValueSetDao
					.findTermValueSetByUrlAndVersion(url, version)
					.orElseThrow(
							// FIXME: add code
							() -> new JobExecutionFailedException(
									Msg.code(0) + "Missing ValueSet[url=" + url + ", version=" + version + "]"));

			Stream<TermValueSetConcept> allConcepts =
					myTermValueSetConceptDao.streamAllByTermValueSetOrdered(termValueSet);
			StreamIterator<TermValueSetConcept> conceptIterator = StreamIterator.iterator(allConcepts);

			CompactConceptsWorkChunkJson chunk = new CompactConceptsWorkChunkJson();

			int order = 0;
			int conceptCount = 0;
			while (conceptIterator.hasNext()) {
				TermValueSetConcept concept = conceptIterator.next();
				myEntityManager.detach(concept);
				conceptCount++;

				chunk.addConcept(concept.getPartitionId().getPartitionId(), concept.getId(), order++);

				if (chunk.getConcepts().size() >= 100 || !conceptIterator.hasNext()) {
					theDataSink.accept(chunk);
					chunk.getConcepts().clear();
				}
			}

			termValueSet.setTotalConcepts((long) conceptCount);
			myTermValueSetDao.save(termValueSet);
		});

		return RunOutcome.SUCCESS;
	}
}
