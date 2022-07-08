package ca.uhn.fhir.batch2.api;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

/**
 * When a step throws an Exception that is not a {@link JobExecutionFailedException},
 * the Smile Batch 2 framework will rethrow the exception as a {@link JobStepFailedException}.
 * This will cause the job notification message to remain on the channel and the system will try executing the send
 * operation repeatedly until it succeeds.
 */
public class JobStepFailedException extends RuntimeException {

	/**
	 * Constructor
	 */
	public JobStepFailedException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}
}
