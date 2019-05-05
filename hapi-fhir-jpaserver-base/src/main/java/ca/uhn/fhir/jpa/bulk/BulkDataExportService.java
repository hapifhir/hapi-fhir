package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;

public class BulkDataExportService implements IBulkDataExportSvc {

	private static final long REFRESH_INTERVAL = 1000;

	@Override
	public void runPass() {

	}


	@DisallowConcurrentExecution
	@PersistJobDataAfterExecution
	public static class SubmitJob extends FireAtIntervalJob {
		@Autowired
		private IBulkDataExportSvc myTarget;

		public SubmitJob() {
			super(REFRESH_INTERVAL);
		}

		@Override
		protected void doExecute(JobExecutionContext theContext) {
			myTarget.runPass();
		}
	}


}
