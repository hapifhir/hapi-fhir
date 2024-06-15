package ca.uhn.fhir.jpa.sched;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseHapiSchedulerTest {

	@Test
	public void testMissingConfig() {
		BaseHapiScheduler sched = new BaseHapiScheduler("hello", new AutowiringSpringBeanJobFactory()) {
		};
		try {
			sched.init();
			fail("");
		} catch (SchedulerException e) {
			assertEquals(Msg.code(1633) + "java.lang.NullPointerException: No instance name supplied", e.getMessage());
		}
	}

	@Test
	public void testSchedulersShareTheSameServiceName() throws SchedulerException {

		String instanceName = "local-scheduler";
		BaseHapiScheduler firstScheduler = new BaseHapiScheduler("hello", new AutowiringSpringBeanJobFactory()) {
		};
		firstScheduler.setInstanceName(instanceName);

		BaseHapiScheduler secondScheduler = new BaseHapiScheduler("hello", new AutowiringSpringBeanJobFactory()) {
		};
		secondScheduler.setInstanceName(instanceName);

		firstScheduler.init();
		secondScheduler.init();

		assertThat(firstScheduler.getPropertiesForUnitTest().get(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME)).isEqualTo(instanceName);
		assertThat(secondScheduler.getPropertiesForUnitTest().get(StdSchedulerFactory.PROP_SCHED_INSTANCE_NAME)).isEqualTo(instanceName);

		assertThat(firstScheduler.getPropertiesForUnitTest().get(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID)).isEqualTo(instanceName);
		assertThat(secondScheduler.getPropertiesForUnitTest().get(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID)).isEqualTo(instanceName);

	}


}
