package ca.uhn.fhir.jpa.util;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

public class StopWatchTest {

	@Test
	public void testStopwatch() throws Exception {
		StopWatch sw = new StopWatch();
		
		Thread.sleep(100);
		
		assertThat(sw.getMillis(new Date()), greaterThan(10L));
		assertThat(sw.getMillis(), greaterThan(10L));
		assertThat(sw.getStartedDate().getTime(), lessThan(System.currentTimeMillis()));
	}
	
	@Test
	public void testStopwatchWithDate() throws Exception {
		StopWatch sw = new StopWatch(new Date());
		
		Thread.sleep(100);
		
		assertThat(sw.getMillis(new Date()), greaterThan(10L));
		assertThat(sw.getMillis(), greaterThan(10L));
		assertThat(sw.getStartedDate().getTime(), lessThan(System.currentTimeMillis()));
	}

}
