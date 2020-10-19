package ca.uhn.fhir.jpa.sched;

import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class BaseHapiSchedulerTest {

	@Test
	public void testMissingConfig() {
		BaseHapiScheduler sched = new BaseHapiScheduler("hello", new AutowiringSpringBeanJobFactory()) {
		};
		try {
			sched.init();
			fail();
		} catch (SchedulerException e) {
			assertEquals("java.lang.NullPointerException: No instance name supplied", e.getMessage());
		}
	}


}
