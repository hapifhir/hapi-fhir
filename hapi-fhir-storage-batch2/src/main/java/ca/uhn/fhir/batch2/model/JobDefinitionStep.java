package ca.uhn.fhir.batch2.model;

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

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

public class JobDefinitionStep<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {

	private final String myStepId;
	private final String myStepDescription;
	private final IJobStepWorker<PT, IT, OT> myJobStepWorker;
	private final Class<IT> myInputType;
	private final Class<OT> myOutputType;

	public JobDefinitionStep(@Nonnull String theStepId, @Nonnull String theStepDescription, @Nonnull IJobStepWorker<PT, IT, OT> theJobStepWorker, @Nonnull Class<IT> theInputType, @Nonnull Class<OT> theOutputType) {
		Validate.notBlank(theStepId, "No step ID specified");
		Validate.isTrue(theStepId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theStepDescription);
		Validate.notNull(theInputType);
		myStepId = theStepId;
		myStepDescription = theStepDescription;
		myJobStepWorker = theJobStepWorker;
		myInputType = theInputType;
		myOutputType = theOutputType;
	}

	public String getStepId() {
		return myStepId;
	}

	public String getStepDescription() {
		return myStepDescription;
	}

	public IJobStepWorker<PT, IT, OT> getJobStepWorker() {
		return myJobStepWorker;
	}

	public Class<IT> getInputType() {
		return myInputType;
	}
}
