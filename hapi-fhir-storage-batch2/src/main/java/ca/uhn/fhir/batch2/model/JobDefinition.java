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

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JobDefinition<PT extends IModelJson> {

	public static final int ID_MAX_LENGTH = 100;

	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final Class<PT> myParametersType;
	private final List<JobDefinitionStep<PT, ?, ?>> mySteps;
	private final String myJobDescription;
	private final IJobParametersValidator<PT> myParametersValidator;
	private final boolean myGatedExecution;
	private final List<String> myStepIds;
	private final IJobCompletionHandler<PT> myCompletionHandler;

	/**
	 * Constructor
	 */
	private JobDefinition(String theJobDefinitionId, int theJobDefinitionVersion, String theJobDescription, Class<PT> theParametersType, List<JobDefinitionStep<PT, ?, ?>> theSteps, IJobParametersValidator<PT> theParametersValidator, boolean theGatedExecution, IJobCompletionHandler<PT> theCompletionHandler) {
		Validate.isTrue(theJobDefinitionId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theJobDefinitionId, "No job definition ID supplied");
		Validate.notBlank(theJobDescription, "No job description supplied");
		Validate.isTrue(theJobDefinitionVersion >= 1, "No job definition version supplied (must be >= 1)");
		Validate.isTrue(theSteps.size() >= 2, "At least 2 steps must be supplied");
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myJobDescription = theJobDescription;
		mySteps = theSteps;
		myStepIds = mySteps.stream().map(t -> t.getStepId()).collect(Collectors.toList());
		myParametersType = theParametersType;
		myParametersValidator = theParametersValidator;
		myGatedExecution = theGatedExecution;
		myCompletionHandler = theCompletionHandler;
	}

	@Nullable
	public IJobCompletionHandler<PT> getCompletionHandler() {
		return myCompletionHandler;
	}

	@Nullable
	public IJobParametersValidator<PT> getParametersValidator() {
		return myParametersValidator;
	}

	public String getJobDescription() {
		return myJobDescription;
	}

	/**
	 * @return Returns a unique identifier for the job definition (i.e. for the "kind" of job)
	 */
	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	/**
	 * @return Returns a unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning. Must be greater than 0.
	 */
	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	/**
	 * @return Returns the parameters that this job can accept as input to create a new instance
	 */
	public Class<PT> getParametersType() {
		return myParametersType;
	}

	/**
	 * @return Returns the processing steps for this job
	 */
	public List<JobDefinitionStep<PT, ?, ?>> getSteps() {
		return mySteps;
	}

	public boolean isGatedExecution() {
		return myGatedExecution;
	}

	public int getStepIndex(String theStepId) {
		int retVal = myStepIds.indexOf(theStepId);
		Validate.isTrue(retVal != -1);
		return retVal;
	}

	public boolean isLastStep(String theStepId) {
		return getStepIndex(theStepId) == (myStepIds.size() - 1);
	}

	public static class Builder<PT extends IModelJson, NIT extends IModelJson> {

		private final List<JobDefinitionStep<PT, ?, ?>> mySteps;
		private String myJobDefinitionId;
		private int myJobDefinitionVersion;
		private String myJobDescription;
		private Class<PT> myJobParametersType;
		private Class<NIT> myNextInputType;
		@Nullable
		private IJobParametersValidator<PT> myParametersValidator;
		private boolean myGatedExecution;
		private IJobCompletionHandler<PT> myCompletionHandler;

		Builder() {
			mySteps = new ArrayList<>();
		}

		Builder(List<JobDefinitionStep<PT, ?, ?>> theSteps, String theJobDefinitionId, int theJobDefinitionVersion, String theJobDescription, Class<PT> theJobParametersType, Class<NIT> theNextInputType, IJobParametersValidator<PT> theParametersValidator, boolean theGatedExecution, IJobCompletionHandler<PT> theCompletionHandler) {
			mySteps = theSteps;
			myJobDefinitionId = theJobDefinitionId;
			myJobDefinitionVersion = theJobDefinitionVersion;
			myJobDescription = theJobDescription;
			myJobParametersType = theJobParametersType;
			myNextInputType = theNextInputType;
			myParametersValidator = theParametersValidator;
			myGatedExecution = theGatedExecution;
			myCompletionHandler = theCompletionHandler;
		}

		/**
		 * @param theJobDefinitionId A unique identifier for the job definition (i.e. for the "kind" of job)
		 */
		public Builder<PT, NIT> setJobDefinitionId(String theJobDefinitionId) {
			myJobDefinitionId = theJobDefinitionId;
			return this;
		}

		/**
		 * @param theJobDefinitionVersion A unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning. Must be greater than 0.
		 */
		public Builder<PT, NIT> setJobDefinitionVersion(int theJobDefinitionVersion) {
			Validate.isTrue(theJobDefinitionVersion > 0, "theJobDefinitionVersion must be > 0");
			myJobDefinitionVersion = theJobDefinitionVersion;
			return this;
		}

		/**
		 * Adds a processing step for this job.
		 *
		 * @param theStepId          A unique identifier for this step. This only needs to be unique within the scope
		 *                           of the individual job definition (i.e. diuplicates are fine for different jobs, or
		 *                           even different versions of the same job)
		 * @param theStepDescription A description of this step
		 * @param theStepWorker      The worker that will actually perform this step
		 */
		public <OT extends IModelJson> Builder<PT, OT> addFirstStep(String theStepId, String theStepDescription, Class<OT> theOutputType, IJobStepWorker<PT, VoidModel, OT> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, VoidModel.class, theOutputType));
			return new Builder<>(mySteps, myJobDefinitionId, myJobDefinitionVersion, myJobDescription, myJobParametersType, theOutputType, myParametersValidator, myGatedExecution, myCompletionHandler);
		}

		/**
		 * Adds a processing step for this job.
		 *
		 * @param theStepId          A unique identifier for this step. This only needs to be unique within the scope
		 *                           of the individual job definition (i.e. diuplicates are fine for different jobs, or
		 *                           even different versions of the same job)
		 * @param theStepDescription A description of this step
		 * @param theStepWorker      The worker that will actually perform this step
		 */
		public <OT extends IModelJson> Builder<PT, OT> addIntermediateStep(String theStepId, String theStepDescription, Class<OT> theOutputType, IJobStepWorker<PT, NIT, OT> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, myNextInputType, theOutputType));
			return new Builder<>(mySteps, myJobDefinitionId, myJobDefinitionVersion, myJobDescription, myJobParametersType, theOutputType, myParametersValidator, myGatedExecution, myCompletionHandler);
		}

		/**
		 * Adds a processing step for this job.
		 *
		 * @param theStepId          A unique identifier for this step. This only needs to be unique within the scope
		 *                           of the individual job definition (i.e. diuplicates are fine for different jobs, or
		 *                           even different versions of the same job)
		 * @param theStepDescription A description of this step
		 * @param theStepWorker      The worker that will actually perform this step
		 */
		public Builder<PT, VoidModel> addLastStep(String theStepId, String theStepDescription, IJobStepWorker<PT, NIT, VoidModel> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, myNextInputType, VoidModel.class));
			return new Builder<>(mySteps, myJobDefinitionId, myJobDefinitionVersion, myJobDescription, myJobParametersType, VoidModel.class, myParametersValidator, myGatedExecution, myCompletionHandler);
		}

		public JobDefinition<PT> build() {
			Validate.notNull(myJobParametersType, "No job parameters type was supplied");
			return new JobDefinition<>(myJobDefinitionId, myJobDefinitionVersion, myJobDescription, myJobParametersType, Collections.unmodifiableList(mySteps), myParametersValidator, myGatedExecution, myCompletionHandler);
		}

		public Builder<PT, NIT> setJobDescription(String theJobDescription) {
			myJobDescription = theJobDescription;
			return this;
		}

		/**
		 * Sets the datatype for the parameters used by this job. This model is a
		 * {@link IModelJson} JSON serializable object.
		 *
		 * <p>
		 * <b>Validation:</b>
		 * Fields should be annotated with
		 * any appropriate <code>javax.validation</code> (JSR 380) annotations (e.g.
		 * {@link javax.validation.constraints.Min} or {@link javax.validation.constraints.Pattern}).
		 * In addition, if there are validation rules that are too complex to express using
		 * JSR 380, you can also specify a programmatic validator using {@link #setParametersValidator(IJobParametersValidator)}.
		 * </p>
		 * <p>
		 * Any fields that contain sensitive data (e.g. passwords) that should not be
		 * provided back to the end user must be marked with {@link ca.uhn.fhir.model.api.annotation.PasswordField}
		 * as well.
		 * </p>
		 *
		 * @see ca.uhn.fhir.model.api.annotation.PasswordField
		 * @see javax.validation.constraints
		 * @see JobDefinition.Builder#setParametersValidator(IJobParametersValidator)
		 */
		@SuppressWarnings("unchecked")
		public <NPT extends IModelJson> Builder<NPT, NIT> setParametersType(@Nonnull Class<NPT> theJobParametersType) {
			Validate.notNull(theJobParametersType, "theJobParametersType must not be null");
			Validate.isTrue(myJobParametersType == null, "Can not supply multiple parameters types, already have: %s", myJobParametersType);
			myJobParametersType = (Class<PT>) theJobParametersType;
			return (Builder<NPT, NIT>) this;
		}

		/**
		 * Supplies a programmatic job parameters validator. Note that as much as possible,
		 * JSR 380 annotations should be used for validation. This method is provided only
		 * to satisfy rules that are too complex to be expressed using JSR 380.
		 *
		 * @param theParametersValidator The validator (must not be null. Do not call this method at all if you do not want a parameters validator).
		 */
		@SuppressWarnings("unchecked")
		public Builder<PT, NIT> setParametersValidator(@Nonnull IJobParametersValidator<PT> theParametersValidator) {
			Validate.notNull(theParametersValidator, "theParametersValidator must not be null");
			Validate.isTrue(myParametersValidator == null, "Can not supply multiple parameters validators. Already have: %s", myParametersValidator);
			myParametersValidator = theParametersValidator;
			return this;
		}

		/**
		 * If this is set, the framework will wait for all work chunks to be
		 * processed for an individual step before moving on to beginning
		 * processing on the next step. Otherwise, processing on subsequent
		 * steps may begin as soon as any data has been produced.
		 * <p>
		 * This is useful in a few cases:
		 * <ul>
		 *    <li>
		 *       If there are potential constraint issues, e.g. data being
		 *    	written by the third step depends on all data from the
		 *    	second step already being written
		 *    </li>
		 *    <li>
		 *       If multiple steps require expensive database queries, it may
		 *       reduce the chances of timeouts to ensure that they are run
		 *       discretely.
		 *    </li>
		 * </ul>
		 * </p>
		 * <p>
		 * Setting this mode means the job may take longer, since it will
		 * rely on a polling mechanism to determine that one step is
		 * complete before beginning any processing for the next step.
		 * </p>
		 */
		public Builder<PT, NIT> gatedExecution() {
			myGatedExecution = true;
			return this;
		}

		/**
		 * Supplies an optional callback that will be invoked when the job is complete
		 */
		public Builder<PT, NIT> completionHandler(IJobCompletionHandler<PT> theCompletionHandler) {
			Validate.isTrue(myCompletionHandler == null, "Can not supply multiple completion handlers");
			myCompletionHandler = theCompletionHandler;
			return this;
		}


	}

	public static Builder<IModelJson, VoidModel> newBuilder() {
		return new Builder<>();
	}

}
