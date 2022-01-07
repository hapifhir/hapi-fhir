package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermReindexingSvc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TermReindexingSvcImplTest {

	@Mock JobExecutionContext myJobExecutionContext;
	@Mock ITermReindexingSvc jobReindexingSvc;


	@Test
	void validateReindexingJobExecutesReindexing() {
		TermReindexingSvcImpl.Job innerJob = new TermReindexingSvcImpl.Job();

		// validates that inner Job has an myTermReindexingSvc field of class ITermReindexingSvc
		try {
			ReflectionTestUtils.setField(innerJob, "myTermReindexingSvc", jobReindexingSvc);
		} catch (Exception theE) {
			fail("ITermReindexingSvc implementation inner Job doesn't have a 'myTermReindexingSvc' property");
		}

		innerJob.execute(myJobExecutionContext);

		// validates that inner Job calls myTermReindexingSvc.processReindexing when executed
		verify(jobReindexingSvc, atLeastOnce()).processReindexing();
	}

}
