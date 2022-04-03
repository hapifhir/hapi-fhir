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

import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

/**
 * This interface is implemented by step workers within the Batch2 framework. It will be called
 * 0..* times depending on the number of work chunks produced by the previous step. It will
 * always be called once in the case of the first step. It can produce 0..* work chunks that
 * will be processed in subsequent steps.
 *
 * @param <PT> The job parameters type datatype
 * @param <IT> The step input datatype
 * @param <OT> The step output datatype
 */
public interface IJobStepWorker<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {

	/**
	 * Executes a step
	 *
	 * @param theStepExecutionDetails Contains details about the individual execution
	 * @param theDataSink             A data sink for data produced during this step. This may never
	 *                                be used during the final step of a job.
	 * @return Returns a {@link RunOutcome} containing details about the execution. See the javadoc for that class for details about how to populate it.
	 * @throws JobExecutionFailedException This exception indicates an unrecoverable failure. If a
	 *                                     step worker throws this exception, processing for the
	 *                                     job will be aborted.
	 */
	@Nonnull
	RunOutcome run(@Nonnull StepExecutionDetails<PT, IT> theStepExecutionDetails, @Nonnull IJobDataSink<OT> theDataSink) throws JobExecutionFailedException;


}
