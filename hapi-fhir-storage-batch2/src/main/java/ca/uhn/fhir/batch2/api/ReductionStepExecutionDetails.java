package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.ListResult;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
 * This class is used for Reduction Step for Batch2 Jobs.
 * @param <PT> - Job Parameters type
 * @param <IT> - Input data type
 * @param <OT> - Output data type. Output will actually be a ListResult of these objects.
 */
public class ReductionStepExecutionDetails<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson>
	extends StepExecutionDetails<PT, IT> {

	public ReductionStepExecutionDetails(@Nonnull PT theParameters,
													 @Nullable IT theData,
													 @Nonnull String theInstanceId) {
		super(theParameters, theData, theInstanceId, "VOID");
	}

	@Override
	@Nonnull
	public final IT getData() {
		throw new UnsupportedOperationException(Msg.code(2099) + " Reduction steps should have all data by the time execution is called.");
	}

	@Override
	public boolean hasAssociatedWorkChunk() {
		return false;
	}
}
