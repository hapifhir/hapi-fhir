package ca.uhn.hapi.fhir.batch2.test.configs;

import ca.uhn.fhir.batch2.channel.BatchJobSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.spy;

/**
 * Provides spying overrides of beans we want to spy on.
 *
 * We spy the BatchJobSender so we can test state transitions without
 * actually sending messages onto the queue
 */
@Configuration
public class SpyOverrideConfig {
	@Autowired
	BatchJobSender myRealJobSender;

	@Primary
	@Bean
	public BatchJobSender batchJobSenderSpy() {
		return spy(myRealJobSender);
	}
}
