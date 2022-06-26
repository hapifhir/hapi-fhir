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
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

public class JobDefinitionReductionStep<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends JobDefinitionStep<PT, IT, OT> {


	public JobDefinitionReductionStep(@Nonnull String theStepId,
												 @Nonnull String theStepDescription,
												 @Nonnull IReductionStepWorker<PT, IT, OT> theJobStepWorker,
												 @Nonnull Class<IT> theInputType,
												 @Nonnull Class<OT> theOutputType) {
		super(theStepId, theStepDescription, (IJobStepWorker<PT, IT, OT>) theJobStepWorker, theInputType, theOutputType);
	}

	@Override
	public boolean isReductionStep() {
		return true;
	}
}
