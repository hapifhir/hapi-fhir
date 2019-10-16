package ca.uhn.fhir.jpa.model.sched;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;

import static ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob.NEXT_EXECUTION_TIME;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FireAtIntervalJobTest {

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	private JobExecutionContext myJobExecutionContext;

	@Test
	public void testExecutionThrowsException() {

		FireAtIntervalJob job = new FireAtIntervalJob(1000) {
			@Override
			protected void doExecute(JobExecutionContext theContext) {
				throw new NullPointerException();
			}
		};

		// No exception thrown please
		job.execute(myJobExecutionContext);

		verify(myJobExecutionContext.getJobDetail().getJobDataMap(), times(1)).put(eq(NEXT_EXECUTION_TIME), anyLong());

	}


}
