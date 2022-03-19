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
 * Return type for {@link IJobStepWorker#run(StepExecutionDetails, IJobDataSink)}
 */
public class RunOutcome {

	/**
	 * RunOutcome with 0 records processed
	 */
	public static final RunOutcome SUCCESS = new RunOutcome(0);

	private final int myRecordsProcessed;

	/**
	 * Constructor
	 *
	 * @param theRecordsProcessed The number of records processed by this step. This number is not used for anything
	 *                            other than calculating the total number of records processed by the job. Therefore in many
	 *                            cases it will make sense to return a count of 0 for all steps except for the final
	 *                            step. For example, if you have a step that fetches files and a step that saves them,
	 *                            you might choose to only return a non-zero count indicating the number of saved files
	 *                            (or even the number of records within those files) so that the ultimate total
	 *                            reflects the real total.
	 */
	public RunOutcome(int theRecordsProcessed) {
		myRecordsProcessed = theRecordsProcessed;
	}

	public int getRecordsProcessed() {
		return myRecordsProcessed;
	}
}
