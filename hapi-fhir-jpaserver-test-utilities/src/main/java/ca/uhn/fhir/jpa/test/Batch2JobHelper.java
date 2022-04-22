package ca.uhn.fhir.jpa.test;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class Batch2JobHelper {

	@Autowired
	private IJobMaintenanceService myJobCleanerService;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	public void awaitJobCompletion(String theId) {
		await().until(() -> {
			myJobCleanerService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, equalTo(StatusEnum.COMPLETED));
	}

	public JobInstance awaitJobFailure(String theId) {
		await().until(() -> {
			myJobCleanerService.runMaintenancePass();
			return myJobCoordinator.getInstance(theId).getStatus();
		}, Matchers.anyOf(equalTo(StatusEnum.ERRORED),equalTo(StatusEnum.FAILED)));
		return myJobCoordinator.getInstance(theId);
	}

}
