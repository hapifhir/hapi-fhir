package ca.uhn.fhir.jpa.subscription.asynch;

import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

public class AsyncResourceModifiedProcessingSchedulerSvc implements IHasScheduledJobs {

	public static final long ONE_MINUTE = 60 * DateUtils.MILLIS_PER_SECOND;

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(AsyncResourceModifiedProcessingSchedulerSvc.Job.class);

		theSchedulerService.scheduleClusteredJob(ONE_MINUTE, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private AsyncResourceModifiedSubmitterSvc myAsyncResourceModifiedSubmitterSvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myAsyncResourceModifiedSubmitterSvc.runDeliveryPass();
		}
	}


}
