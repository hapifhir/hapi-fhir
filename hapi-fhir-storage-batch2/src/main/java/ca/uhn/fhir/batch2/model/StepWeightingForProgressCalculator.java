package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StepWeightingForProgressCalculator {

	private final double myWeightForStepsWithoutExplicitWeight;
	private final Set<String> myStepIdsWithoutExplicitWeight;
	private Map<String, Double> myStepIdToWeight;

	StepWeightingForProgressCalculator(Map<String, Double> theStepIdToWeight, Set<String> theStepIdsWithoutExplicitWeight, double theWeightForStepsWithoutExplicitWeight) {
		Validate.notNull(theStepIdToWeight, "theStepIdToWeight must not be null");
		myStepIdToWeight = Map.copyOf(theStepIdToWeight);
		myStepIdsWithoutExplicitWeight = theStepIdsWithoutExplicitWeight;
		myWeightForStepsWithoutExplicitWeight = theWeightForStepsWithoutExplicitWeight;
	}

	// FIXME: document all getters here
	public Set<String> getStepIdsWithoutExplicitWeight() {
		return myStepIdsWithoutExplicitWeight;
	}

	public Map<String, Double> getStepIdToWeight() {
		return myStepIdToWeight;
	}

	public double getWeightForStepId(String theStepId) {
		Double retVal = myStepIdToWeight.get(theStepId);
		Validate.notNull(retVal, "No weight found for stepId %s", theStepId);
		return retVal;
	}

	public Set<String> getStepIdsWithExplicitWeighting() {
		return myStepIdToWeight.keySet();
	}

	public double getCombinedWeightForStepIdsWithoutExplicitWeight() {
		return myWeightForStepsWithoutExplicitWeight;
	}

	static Builder newBuilder() {
		return new Builder();
	}

	static class Builder {

		private final Map<String, Double> myStepIdToWeight = new HashMap<>();

		Builder() {
		}

		void setStepWeightForProgressCalculator(String theStepId, double theWeight) {
			Validate.isTrue(theWeight > 0.0, "All steps must have at least a small amount of weight, so step weight can not be <= 0.0");
			Double previousValue = myStepIdToWeight.put(theStepId, theWeight);
			Validate.isTrue(previousValue == null, "Already have weight for step: %s", theStepId);
		}

		public <PT extends IModelJson> StepWeightingForProgressCalculator build(List<JobDefinitionStep<PT, ?, ?>> theSteps) {

			Set<String> remainingJobStepIds = theSteps.stream().map(JobDefinitionStep::getStepId).collect(Collectors.toSet());

			double combinedWeightTotal = 0.0;
			for (String stepId : myStepIdToWeight.keySet()) {
				Validate.isTrue(remainingJobStepIds.remove(stepId), "Step weight provided for invalid step ID: %s", stepId);
				combinedWeightTotal += myStepIdToWeight.get(stepId);
			}

			int remainingStepCount = remainingJobStepIds.size();
			double weightForRemainingSteps = 0.0;
			if (remainingStepCount > 0) {
				Validate.isTrue(combinedWeightTotal < 1.0, "All steps must have at least a small amount of weight, no remaining room for steps: %s", remainingJobStepIds);
				weightForRemainingSteps = (1.0 - combinedWeightTotal);
			}

//			if (!myStepIdToWeight.isEmpty()) {
//				Validate.isTrue(combinedWeightTotal == 1.0, "Combined step weights can not be greater than 1.0, but was %f", combinedWeightTotal);
//			}

			return new StepWeightingForProgressCalculator(myStepIdToWeight, remainingJobStepIds, weightForRemainingSteps);
		}


	}

}
