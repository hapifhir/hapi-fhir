/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.docs.interceptor;

// START SNIPPET: interceptor
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IBaseInterceptorBroadcaster.IInterceptorFilterHook;
import ca.uhn.fhir.interceptor.api.Pointcut;

public class WorkChunkProcessingInterceptor {

	@Hook(Pointcut.BATCH2_CHUNK_PROCESS_FILTER)
	public IInterceptorFilterHook batch2ProcessFilter(JobInstance theJobInstance, WorkChunk theWorkChunk) {
		return theContinuation -> {
			try {
				// Perform pre-processing logic before the work chunk is processed

				// Process the work chunk (Note: If the continuation is not ran, an IllegalStateException will be
				// thrown)
				theContinuation.run();
			} catch (Exception e) {
				// Handle any exceptions that occur during work chunk processing

				// rethrow the exception
				throw e;
			} finally {
				// Perform any necessary cleanup or final operations
			}
		};
	}
}
// END SNIPPET: interceptor
