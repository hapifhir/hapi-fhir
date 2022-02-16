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

import ca.uhn.fhir.batch2.model.JobInstanceParameters;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class StepExecutionDetails {

	private final JobInstanceParameters myParameters;
	private final Map<String, Object> myData;

	public StepExecutionDetails(@Nonnull JobInstanceParameters theParameters, @Nullable Map<String, Object> theData) {
		Validate.notNull(theParameters);
		myParameters = theParameters;
		myData = theData;
	}

	/**
	 * Returns the data associated with this step execution. This method should never be
	 * called during the first step of a job, and will never return <code>null</code> during
	 * any subsequent steps.
	 *
	 * @throws NullPointerException If this method is called during the first step of a job
	 */
	@Nonnull
	public Map<String, Object> getData() {
		Validate.notNull(myData);
		return myData;
	}

	/**
	 * Returns the parameters associated with this job instance. Note that parameters
	 * are set when the job instance is created and can not be modified after that.
	 *
	 * @return
	 */
	@Nonnull
	public JobInstanceParameters getParameters() {
		return myParameters;
	}

}
