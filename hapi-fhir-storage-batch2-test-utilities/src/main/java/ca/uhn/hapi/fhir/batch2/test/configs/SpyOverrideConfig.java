/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
