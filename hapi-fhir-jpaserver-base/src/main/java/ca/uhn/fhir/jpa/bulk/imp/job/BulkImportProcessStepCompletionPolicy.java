package ca.uhn.fhir.jpa.bulk.imp.job;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.policy.CompletionPolicySupport;
import org.springframework.beans.factory.annotation.Value;

import static ca.uhn.fhir.jpa.bulk.imp.job.BulkImportJobConfig.JOB_PARAM_COMMIT_INTERVAL;

public class BulkImportProcessStepCompletionPolicy extends CompletionPolicySupport {

	@Value("#{jobParameters['" + JOB_PARAM_COMMIT_INTERVAL + "']}")
	private int myChunkSize;

	@Override
	public RepeatContext start(RepeatContext context) {
		return super.start(context);
	}

	@Override
	public void update(RepeatContext context) {
		super.update(context);
	}

	@Override
	public boolean isComplete(RepeatContext context) {
		if (context.getStartedCount() < myChunkSize) {
			return false;
		}
		return true;
	}
}
