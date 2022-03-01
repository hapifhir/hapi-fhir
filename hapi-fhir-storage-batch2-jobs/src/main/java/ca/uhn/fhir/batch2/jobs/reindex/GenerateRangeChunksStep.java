package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class GenerateRangeChunksStep implements IFirstJobStepWorker<ReindexJobParameters, ReindexRangeChunk> {

	private IFhirSystemDao<?,?> mySystemDao;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexRangeChunk> theDataSink) throws JobExecutionFailedException {

		ReindexJobParameters params = theStepExecutionDetails.getParameters();
		if (isNotBlank(params.getResourceType())) {

		}

//		mySystemDao.history()

		return null;
	}

}
