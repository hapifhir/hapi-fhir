/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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
package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;

import static ca.uhn.fhir.batch2.util.Batch2Utils.REDUCTION_STEP_CHUNK_ID_PLACEHOLDER;

public class StepExecutionDetails<PT extends IModelJson, IT extends IModelJson> {

	private final PT myParameters;
	private final IT myData;
	private final IJobInstance myInstance;
	private final WorkChunk myChunk;

	/**
	 * Create and returns a step execution details for a reduction job
	 */
	public static <P1 extends IModelJson, I1 extends IModelJson>
			StepExecutionDetails<P1, I1> createReductionStepDetails(
					P1 theParameters, I1 theIntermediateParams, JobInstance theInstance) {
		WorkChunk reductionChunk = new WorkChunk().setId(REDUCTION_STEP_CHUNK_ID_PLACEHOLDER);

		return new StepExecutionDetails<>(theParameters, theIntermediateParams, theInstance, reductionChunk);
	}

	/**
	 * Deprecated in 7.3
	 */
	@Deprecated
	public StepExecutionDetails(
			@Nonnull PT theParameters, @Nullable IT theData, @Nonnull JobInstance theInstance, String theChunkId) {
		this(
				theParameters,
				theData,
				theInstance,
				new WorkChunk()
						.setId(theChunkId)
						.setInstanceId(theInstance.getInstanceId())
						.setData(theData));
	}

	public StepExecutionDetails(
			@Nonnull PT theParameters,
			@Nullable IT theData,
			@Nonnull JobInstance theInstance,
			@Nonnull WorkChunk theChunk) {
		Validate.notNull(theParameters);
		myParameters = theParameters;
		myData = theData;
		// Make a copy so the step worker can't change the one passed in
		myInstance = new JobInstance(theInstance);
		myChunk = theChunk;
	}

	/**
	 * Returns the data associated with this step execution.
	 * This method should never be called during the first step of a job,
	 * or in a reduction step, and will never return <code>null</code> during
	 * any other steps.
	 *
	 * @throws NullPointerException If this method is called during the first step of a job
	 */
	@Nonnull
	public IT getData() {
		Validate.notNull(myData);
		return myData;
	}

	/**
	 * Returns the parameters associated with this job instance. Note that parameters
	 * are set when the job instance is created and can not be modified after that.
	 */
	@Nonnull
	public PT getParameters() {
		return myParameters;
	}

	/**
	 * Returns the job instance ID being executed
	 */
	@Nonnull
	public IJobInstance getInstance() {
		return myInstance;
	}

	/**
	 * Returns the work chunk ID being executed
	 */
	@Nonnull
	public String getChunkId() {
		return myChunk.getId();
	}

	@Nonnull
	public WorkChunk getWorkChunk() {
		return myChunk;
	}

	/**
	 * Returns true if there's a workchunk to store data to.
	 * If false, failures and data storage go straight to the jobinstance instead
	 * @return - true if there's a workchunk in the db to store to.
	 * 			false if the output goes to the jobinstance instead
	 */
	public boolean hasAssociatedWorkChunk() {
		return myChunk != null && !myChunk.isReductionWorkChunk();
	}
}
