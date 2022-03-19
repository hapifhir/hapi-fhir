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
 * This exception indicates an <b>unrecoverable</b> processing failure. It should be
 * thrown by {@link IJobStepWorker} instances in the case that an error occurs that
 * is not expected to produce a success if it is retried.
 * <p>
 * In the case of potentially recoverable errors (IOException, database constraint errors, etc)
 * you should throw {@link ca.uhn.fhir.rest.server.exceptions.InternalErrorException} instead.
 * </p>
 */
public class JobExecutionFailedException extends RuntimeException {

	private static final long serialVersionUID = 4871161727526723730L;

	/**
	 * Constructor
	 */
	public JobExecutionFailedException(String theMessage) {
		super(theMessage);
	}

	/**
	 * Constructor
	 */
	public JobExecutionFailedException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}
}
