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

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.Logs;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.util.List;

/**
 * This immutable object is produced by reconciling a work notification message to its corresponding job definition.
 * It holds information required to execute the current step and send data to the next step.
 *
 * @param <PT> Job Parameter type
 * @param <IT> Current step input data type
 * @param <OT> Current step output data type and next step input data type
 */
public class JobWorkCursor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public final JobDefinition<PT> jobDefinition;
	public final boolean isFirstStep;
	public final JobDefinitionStep<PT, IT, OT> currentStep;
	public final JobDefinitionStep<PT, OT, ?> nextStep;

	public JobWorkCursor(
			JobDefinition<PT> theJobDefinition,
			boolean theIsFirstStep,
			JobDefinitionStep<PT, IT, OT> theCurrentStep,
			JobDefinitionStep<PT, OT, ?> theNextStep) {
		jobDefinition = theJobDefinition;
		isFirstStep = theIsFirstStep;
		currentStep = theCurrentStep;
		nextStep = theNextStep;
		validate();
	}

	private void validate() {
		if (isFirstStep) {
			Validate.isTrue(currentStep.getInputType() == VoidModel.class);
		}
		// Note that if it is not the first step, it can have VoidModel as it's input type--not all steps require input
		// from
		// the previous step
		if (nextStep != null) {
			Validate.isTrue(currentStep.getOutputType() == nextStep.getInputType());
		}
	}

	public static <PT extends IModelJson> JobWorkCursor<PT, ?, ?> fromJobDefinitionAndRequestedStepId(
			JobDefinition<PT> theJobDefinition, String theRequestedStepId) {
		boolean isFirstStep = false;
		JobDefinitionStep<PT, ?, ?> currentStep = null;
		JobDefinitionStep<PT, ?, ?> nextStep = null;

		List<JobDefinitionStep<PT, ?, ?>> steps = theJobDefinition.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			JobDefinitionStep<PT, ?, ?> step = steps.get(i);
			if (step.getStepId().equals(theRequestedStepId)) {
				currentStep = step;
				if (i == 0) {
					isFirstStep = true;
				}
				if (i < (steps.size() - 1)) {
					nextStep = steps.get(i + 1);
				}
				break;
			}
		}

		if (currentStep == null) {
			String msg = "Unknown step[" + theRequestedStepId + "] for job definition ID["
					+ theJobDefinition.getJobDefinitionId() + "] version[" + theJobDefinition.getJobDefinitionVersion()
					+ "]";
			ourLog.warn(msg);
			throw new InternalErrorException(Msg.code(2042) + msg);
		}

		return new JobWorkCursor(theJobDefinition, isFirstStep, currentStep, nextStep);
	}

	public String getCurrentStepId() {
		return currentStep.getStepId();
	}

	public boolean isFirstStep() {
		return isFirstStep;
	}

	public boolean isFinalStep() {
		return nextStep == null;
	}

	@SuppressWarnings("unchecked")
	public JobWorkCursor<PT, IT, VoidModel> asFinalCursor() {
		Validate.isTrue(isFinalStep());
		return (JobWorkCursor<PT, IT, VoidModel>) this;
	}

	public JobDefinition<PT> getJobDefinition() {
		return jobDefinition;
	}

	public JobDefinitionStep<PT, IT, OT> getCurrentStep() {
		return currentStep;
	}

	public boolean isReductionStep() {
		return currentStep.isReductionStep();
	}
}
