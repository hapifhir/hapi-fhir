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
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JobDefinition {

	public static final int ID_MAX_LENGTH = 100;

	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final Class<? extends IModelJson> myParametersType;
	private final List<JobDefinitionStep<?, ?, ?>> mySteps;
	private final String myJobDescription;

	/**
	 * Constructor
	 */
	private JobDefinition(String theJobDefinitionId, int theJobDefinitionVersion, String theJobDescription, Class<? extends IModelJson> theParametersType, List<JobDefinitionStep<?, ?, ?>> theSteps) {
		Validate.isTrue(theJobDefinitionId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theJobDefinitionId, "No job definition ID supplied");
		Validate.notBlank(theJobDescription, "No job description supplied");
		Validate.isTrue(theJobDefinitionVersion >= 1, "No job definition version supplied (must be >= 1)");
		Validate.isTrue(theSteps.size() >= 2, "At least 2 steps must be supplied");
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myJobDescription = theJobDescription;
		mySteps = theSteps;
		myParametersType = theParametersType;
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
	public Class<? extends IModelJson> getParametersType() {
		return myParametersType;
	}

	/**
	 * @return Returns the processing steps for this job
	 */
	public List<JobDefinitionStep<?, ?, ?>> getSteps() {
		return mySteps;
	}

	public static Builder<IModelJson> newBuilder() {
		return new Builder<>();
	}

	public static class Builder<PT extends IModelJson> {

		private final List<JobDefinitionStep<?,?,?>> mySteps = new ArrayList<>();
		private String myJobDefinitionId;
		private int myJobDefinitionVersion;
		private String myJobDescription;
		private Class<PT> myJobParametersType;

		/**
		 * @param theJobDefinitionId A unique identifier for the job definition (i.e. for the "kind" of job)
		 */
		public Builder<PT> setJobDefinitionId(String theJobDefinitionId) {
			myJobDefinitionId = theJobDefinitionId;
			return this;
		}

		/**
		 * @param theJobDefinitionVersion A unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning. Must be greater than 0.
		 */
		public Builder<PT> setJobDefinitionVersion(int theJobDefinitionVersion) {
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
		public <OT extends IModelJson> Builder<PT> addFirstStep(String theStepId, String theStepDescription, Class<OT> theOutputType, IJobStepWorker<PT, VoidModel, OT> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, VoidModel.class, theOutputType));
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
		public <IT extends IModelJson, OT extends IModelJson> Builder<PT> addIntermediateStep(String theStepId, String theStepDescription, Class<IT> theInputType, Class<OT> theOutputType, IJobStepWorker<PT, IT, OT> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, theInputType, theOutputType));
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
		public <IT extends IModelJson> Builder<PT> addLastStep(String theStepId, String theStepDescription, Class<IT> theInputType, IJobStepWorker<PT, IT, VoidModel> theStepWorker) {
			mySteps.add(new JobDefinitionStep<>(theStepId, theStepDescription, theStepWorker, theInputType, VoidModel.class));
			return this;
		}

		public JobDefinition build() {
			Validate.notNull(myJobParametersType, "No job parameters type was supplied");
			return new JobDefinition(myJobDefinitionId, myJobDefinitionVersion, myJobDescription, myJobParametersType, Collections.unmodifiableList(mySteps));
		}

		public Builder<PT> setJobDescription(String theJobDescription) {
			myJobDescription = theJobDescription;
			return this;
		}

		/**
		 * Sets the datatype for the parameters used by this job. This model is a
		 * {@link IModelJson} JSON serializable object. Fields should be annotated with
		 * any appropriate <code>javax.validation</code> annotations (e.g.
		 * {@link javax.validation.constraints.Min} or {@link javax.validation.constraints.Pattern}).
		 *
		 * Any fields that contain sensitive data (e.g. passwords) that should not be
		 * provided back to the end user must be marked with {@link ca.uhn.fhir.batch2.api.PasswordField}
		 * as well.
		 *
		 * @see ca.uhn.fhir.batch2.api.PasswordField
		 * @see javax.validation.constraints
		 */
		@SuppressWarnings("unchecked")
		public <NPT extends IModelJson> Builder<NPT> setParametersType(Class<NPT> theJobParametersType) {
			Validate.notNull(theJobParametersType, "theJobParametersType must not be null");
			myJobParametersType = (Class<PT>) theJobParametersType;
			return (Builder<NPT>) this;
		}

	}

}
