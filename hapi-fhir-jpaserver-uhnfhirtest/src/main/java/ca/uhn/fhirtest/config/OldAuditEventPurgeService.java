package ca.uhn.fhirtest.config;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IDeleteExpungeJobSubmitter;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.r4.model.DateType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;
import java.util.List;

public class OldAuditEventPurgeService {
	private static final Logger ourLog = LoggerFactory.getLogger(OldAuditEventPurgeService.class);

	@Autowired
	private ISchedulerService mySchedulerSvc;

	@Autowired
	private IDeleteExpungeJobSubmitter myDeleteExpungeSubmitter;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@EventListener(ContextRefreshedEvent.class)
	public void start() {
		myStorageSettings.setAllowMultipleDelete(true);
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setDeleteExpungeEnabled(true);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(OldAuditEventPurgeServiceJob.class.getName());
		jobDetail.setJobClass(OldAuditEventPurgeServiceJob.class);
		mySchedulerSvc.scheduleLocalJob(DateUtils.MILLIS_PER_DAY, jobDetail);
	}

	private void doPass() {
		Date cutoff = DateUtils.addDays(new Date(), -7);
		String cutoffString = new DateType(cutoff).getValueAsString();
		String url = "AuditEvent?_lastUpdated=lt" + cutoffString;

		ourLog.info("Submitting an AuditEvent purge job with URL: {}", url);

		myDeleteExpungeSubmitter.submitJob(1000, List.of(url), false, null, new SystemRequestDetails());
	}

	public static class OldAuditEventPurgeServiceJob implements HapiJob {

		@Autowired
		private OldAuditEventPurgeService mySvc;

		@Override
		public void execute(JobExecutionContext context) {
			mySvc.doPass();
		}
	}
}
