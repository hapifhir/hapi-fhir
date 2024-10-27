/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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
