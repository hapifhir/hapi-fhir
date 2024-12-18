package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencesJobParameters;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Task;

public class MergeErrorHandler implements IJobCompletionHandler<MergeJobParameters> {

	private final Batch2TaskHelper myBatch2TaskHelper;
	private final IFhirResourceDao<Task> myTaskDao;

	public MergeErrorHandler(Batch2TaskHelper theBatch2TaskHelper, IFhirResourceDao<Task> theTaskDao) {
		myBatch2TaskHelper = theBatch2TaskHelper;
		myTaskDao = theTaskDao;
	}

	@Override
	public void jobComplete(JobCompletionDetails<MergeJobParameters> theDetails) {

		ReplaceReferencesJobParameters jobParameters = theDetails.getParameters();

		SystemRequestDetails requestDetails =
				SystemRequestDetails.forRequestPartitionId(jobParameters.getPartitionId());

		myBatch2TaskHelper.updateTaskStatusOnJobCompletion(myTaskDao, requestDetails, theDetails);
	}
}
