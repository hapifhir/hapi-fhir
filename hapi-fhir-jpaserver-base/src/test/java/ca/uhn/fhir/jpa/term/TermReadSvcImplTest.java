package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TermReadSvcImplTest {

	@InjectMocks
	private TermReadSvcImpl termReadService;

	@Spy
	private JpaStorageSettings storageSettings = new JpaStorageSettings();

	@Mock
	private ISchedulerService schedulerService;

	@Test
	void testScheduleJobs_whenDefaultInterval_shouldScheduleCorrectly() {
		// Setup - using real JpaStorageSettings with default value
		int defaultMinutes = storageSettings.getPreExpandValueSetsScheduleInMinutes();

		ArgumentCaptor<Long> millisCaptor = ArgumentCaptor.forClass(Long.class);
		ArgumentCaptor<ScheduledJobDefinition> jobCaptor = ArgumentCaptor.forClass(ScheduledJobDefinition.class);

		// Execute
		termReadService.scheduleJobs(schedulerService);

		// Verify
		verify(schedulerService).scheduleClusteredJob(millisCaptor.capture(), jobCaptor.capture());

		long expectedMillis = defaultMinutes * DateUtils.MILLIS_PER_MINUTE;
		assertEquals(expectedMillis, millisCaptor.getValue(),
			"Interval should match default configuration (" + defaultMinutes + " minutes)");

		ScheduledJobDefinition job = jobCaptor.getValue();
		assertEquals(TermReadSvcImpl.class.getName(), job.getId());
		assertEquals(TermReadSvcImpl.Job.class, job.getJobClass());
	}

	@Test
	void testScheduleJobs_whenCustomInterval_shouldUseOverriddenValue() {
		// Setup - override the default with custom value
		int customMinutes = 30;
		storageSettings.setPreExpandValueSetsScheduleInMinutes(customMinutes);

		ArgumentCaptor<Long> millisCaptor = ArgumentCaptor.forClass(Long.class);

		// Execute
		termReadService.scheduleJobs(schedulerService);

		// Verify
		verify(schedulerService).scheduleClusteredJob(millisCaptor.capture(), any(ScheduledJobDefinition.class));
		assertEquals(customMinutes * DateUtils.MILLIS_PER_MINUTE, millisCaptor.getValue(),
			"Interval should match overridden value");
	}
}
