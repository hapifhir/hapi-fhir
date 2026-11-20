package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.batch2.maintenance.WorkChunkHeartbeatService;
import ca.uhn.fhir.batch2.maintenance.WorkChunkHeartbeatService.HeartbeatHandle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = WorkChunkHeartbeatServiceSchedulingTest.TestConfiguration.class)
@ExtendWith(SpringExtension.class)
@DirtiesContext
public class WorkChunkHeartbeatServiceSchedulingTest {

	@Autowired
	private HapiSchedulerServiceImpl mySchedulerService;

	private WorkChunkHeartbeatService myHeartbeatService;

	@BeforeEach
	public void before() {
		mySchedulerService.setDefaultGroup("HAPI");
		myHeartbeatService = new WorkChunkHeartbeatService(mySchedulerService);
	}

	@AfterEach
	public void after() throws SchedulerException {
		mySchedulerService.purgeAllScheduledJobsForUnitTest();
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"HAPI", "TENANT_A"})
	public void close_removesHeartbeat(String theDefaultGroup) throws SchedulerException {
		mySchedulerService.setDefaultGroup(theDefaultGroup);
		HeartbeatHandle heartbeat = myHeartbeatService.scheduleHeartbeatJob("instance-1", "chunk-1");
		assertThat(mySchedulerService.getLocalJobKeysForUnitTest()).hasSize(1);

		heartbeat.close();
		assertThat(mySchedulerService.getLocalJobKeysForUnitTest()).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({"instance-1, chunk-2", "instance-2, chunk-1"})
	public void close_keepsOtherHeartbeats(String theInstanceId, String theChunkId) throws SchedulerException {
		myHeartbeatService.scheduleHeartbeatJob(theInstanceId, theChunkId);
		Set<JobKey> otherHeartbeats = mySchedulerService.getLocalJobKeysForUnitTest();
		HeartbeatHandle heartbeat = myHeartbeatService.scheduleHeartbeatJob("instance-1", "chunk-1");
		assertThat(mySchedulerService.getLocalJobKeysForUnitTest()).hasSize(2);

		heartbeat.close();
		assertThat(mySchedulerService.getLocalJobKeysForUnitTest()).isEqualTo(otherHeartbeats);
	}

	@Configuration
	public static class TestConfiguration {

		@Bean
		public HapiSchedulerServiceImpl schedulerService() {
			return new HapiSchedulerServiceImpl();
		}

		@Bean
		public AutowiringSpringBeanJobFactory springBeanJobFactory() {
			return new AutowiringSpringBeanJobFactory();
		}
	}
}
