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
package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.model.api.IModelJson;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.function.Supplier;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

public class JobDefinitionStep<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {

	private final String myStepId;
	private final String myStepDescription;
	/**
	 * Either myJobStepWorkerSupplier or myJobStepWorker will be set
	 */
	protected final Supplier<IReductionStepWorker<PT, IT, OT>> myReductionStepWorkerSupplier;
	protected final IJobStepWorker<PT, IT, OT> myJobStepWorker;
	private final Class<IT> myInputType;

	private final Class<OT> myOutputType;

	public JobDefinitionStep(
			@Nonnull String theStepId,
			@Nonnull String theStepDescription,
			@Nonnull IJobStepWorker<PT, IT, OT> theJobStepWorker,
			@Nonnull Class<IT> theInputType,
			@Nonnull Class<OT> theOutputType) {
		this(theStepId, theStepDescription, null, theJobStepWorker, theInputType, theOutputType);
	}

	public JobDefinitionStep(
		@Nonnull String theStepId,
		@Nonnull String theStepDescription,
		Supplier<IReductionStepWorker<PT, IT, OT>> theReductionStepWorkerSupplier,
		@Nonnull Class<IT> theInputType,
		@Nonnull Class<OT> theOutputType) {
		this(theStepId, theStepDescription, theReductionStepWorkerSupplier, null, theInputType, theOutputType);
	}

	private JobDefinitionStep(String theStepId, String theStepDescription, Supplier<IReductionStepWorker<PT, IT, OT>> theReductionStepWorkerSupplier, IJobStepWorker<PT, IT, OT> theJobStepWorker, Class<IT> theInputType, Class<OT> theOutputType) {
		Validate.notBlank(theStepId, "No step ID specified");
		Validate.isTrue(theStepId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theStepDescription);
		Objects.requireNonNull(theInputType);
		if (theReductionStepWorkerSupplier == null) {
			Objects.requireNonNull(theJobStepWorker);
		}
		if (theJobStepWorker == null) {
			Objects.requireNonNull(theReductionStepWorkerSupplier);
		}
		myStepId = theStepId;
		myStepDescription = theStepDescription;
		myReductionStepWorkerSupplier = theReductionStepWorkerSupplier;
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
		if (myReductionStepWorkerSupplier != null) {
			return myReductionStepWorkerSupplier.get();
		}
		return myJobStepWorker;
	}

	public Class<IT> getInputType() {
		return myInputType;
	}

	public Class<OT> getOutputType() {
		return myOutputType;
	}

	public boolean isReductionStep() {
		return false;
	}
}
