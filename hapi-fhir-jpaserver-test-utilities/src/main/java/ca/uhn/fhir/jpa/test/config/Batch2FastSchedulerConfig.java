package ca.uhn.fhir.jpa.test.config;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * A fast scheduler to use for Batch2 job Integration Tests.
 * This scheduler will run every 200ms (instead of the default 1min)
 * so that our ITs can complete in a sane amount of time.
 */
@Configuration
public class Batch2FastSchedulerConfig {
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;

	@PostConstruct
	void fastScheduler() {
		((JobMaintenanceServiceImpl)myJobMaintenanceService).setScheduledJobFrequencyMillis(200);
	}
}
