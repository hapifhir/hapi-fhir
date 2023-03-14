package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.api.svc.ISearchUrlJobMaintenanceSvc;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * The purpose of this service is to define and register a job that will clean up
 * entries created by an instance of {@link ResourceSearchUrlSvc}.
 */
public class SearchUrlJobMaintenanceSvcImpl implements ISearchUrlJobMaintenanceSvc, IHasScheduledJobs {

	private ResourceSearchUrlSvc myResourceSearchUrlSvc;

	/**
	 * An hour at 3k resources/second is ~10M resources.  That's easy to manage with deletes by age.
	 * We can shorten this if we have memory or storage pressure.  MUST be longer that longest transaction
	 * possible to work.
	 */
	public static final long OUR_CUTOFF_IN_MILLISECONDS = 1 * DateUtils.MILLIS_PER_HOUR;

	public SearchUrlJobMaintenanceSvcImpl(ResourceSearchUrlSvc theResourceSearchUrlSvc) {
		myResourceSearchUrlSvc = theResourceSearchUrlSvc;
	}

	@Override
	public void removeStaleEntries() {
		final Date cutoffDate = calculateCutoffDate();
		myResourceSearchUrlSvc.deleteEntriesOlderThan(cutoffDate);
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(SearchUrlMaintenanceJob.class.getName());
		jobDetail.setJobClass(SearchUrlMaintenanceJob.class);
		theSchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_MINUTE, jobDetail);
	}

	private Date calculateCutoffDate() {
		return new Date(System.currentTimeMillis() - OUR_CUTOFF_IN_MILLISECONDS);
	}

	public static class SearchUrlMaintenanceJob implements HapiJob{


		@Autowired
		private ISearchUrlJobMaintenanceSvc mySearchUrlJobMaintenanceSvc;

		@Override
		public void execute(JobExecutionContext theJobExecutionContext) throws JobExecutionException {
			mySearchUrlJobMaintenanceSvc.removeStaleEntries();
		}

	}
}
