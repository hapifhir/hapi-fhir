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

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class JobCompletionDetails<PT extends IModelJson> {

	private final PT myParameters;
	private final IJobInstance myInstance;

	public JobCompletionDetails(@Nonnull PT theParameters, @Nonnull JobInstance theInstance) {
		Validate.notNull(theParameters);
		myParameters = theParameters;
		// Make a copy of the instance.  Even though the interface is read-only, we don't want to risk users of this API
		// casting the instance and changing values inside it.
		myInstance = new JobInstance(theInstance);
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

}
