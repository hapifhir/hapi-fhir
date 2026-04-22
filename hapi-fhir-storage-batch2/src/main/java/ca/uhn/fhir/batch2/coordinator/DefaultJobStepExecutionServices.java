/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobInstance;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

public class DefaultJobStepExecutionServices implements IJobStepExecutionServices {

	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public DefaultJobStepExecutionServices(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@Override
	public SystemRequestDetails newRequestDetails(IJobInstance theJobInstance) {
		SystemRequestDetails systemRequestDetails = new SystemRequestDetails();

		myInterceptorBroadcaster.ifHasCallHooks(Pointcut.STORAGE_BATCH_TASK_NEW_REQUEST_DETAILS, () -> {
			HookParams params = new HookParams();
			params.add(IJobInstance.class, theJobInstance);
			params.add(RequestDetails.class, systemRequestDetails);
			return params;
		});

		return systemRequestDetails;
	}
}
