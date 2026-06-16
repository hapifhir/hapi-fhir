package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Given a Batch2 job with a series of steps, by default we calculate the job progress
 * by counting all of the work chunks across all steps, dividing:
 * <code>total incomplete chunks / total chunks</code>.
 * However, it's also possible to assign weights to specific steps, if a specific
 * step is expected to process steps faster than others, or if it won't get
 * populated with steps until later in the process.
 * An individual step may have an explicit weight assigned, which is a double between
 * 0.0-0.999.
 */
public class StepWeightingForProgressCalculator {

	private final double myWeightForStepsWithoutExplicitWeight;
	private final Set<String> myStepIdsWithoutExplicitWeight;
	private final Map<String, Double> myStepIdToWeight;

	StepWeightingForProgressCalculator(
			Map<String, Double> theStepIdToWeight,
			Set<String> theStepIdsWithoutExplicitWeight,
			double theWeightForStepsWithoutExplicitWeight) {
		Validate.notNull(theStepIdToWeight, "theStepIdToWeight must not be null");
		myStepIdToWeight = Map.copyOf(theStepIdToWeight);
		myStepIdsWithoutExplicitWeight = theStepIdsWithoutExplicitWeight;
		myWeightForStepsWithoutExplicitWeight = theWeightForStepsWithoutExplicitWeight;
	}

	/**
	 * Returns the list of step IDs that do not have an explicit weight assigned.
	 */
	public Set<String> getStepIdsWithoutExplicitWeight() {
		return myStepIdsWithoutExplicitWeight;
	}

	/**
	 * If there are one or more steps with an explicit weight assigned, this method returns
	 * the combined weight for the remaining steps. For example, given a job with 10 steps, where
	 * two steps have an explicit weight of <code>0.1</code> each and the remaining steps
	 * have no explicit weight assigned, this method would return <code>0.8</code>.
	 */
	public double getCombinedWeightForStepIdsWithoutExplicitWeight() {
		return myWeightForStepsWithoutExplicitWeight;
	}

	/**
	 * Returns the list of step IDs that have an explicit weight assigned. The IDs
	 * returns by this method may be passed to {@link #getWeightForStepId(String)}.
	 */
	public Set<String> getStepIdsWithExplicitWeight() {
		return myStepIdToWeight.keySet();
	}

	/**
	 * Returns the weight for a specific job step.
	 */
	public double getWeightForStepId(String theStepId) {
		Double retVal = myStepIdToWeight.get(theStepId);
		Validate.notNull(retVal, "No weight found for stepId %s", theStepId);
		return retVal;
	}

	static Builder newBuilder() {
		return new Builder();
	}

	static class Builder {

		private final Map<String, Double> myStepIdToWeight = new HashMap<>();

		Builder() {}

		void setStepWeightForProgressCalculator(String theStepId, double theWeight) {
			Validate.isTrue(
					theWeight > 0.0,
					"All steps must have at least a small amount of weight, so step weight can not be <= 0.0");
			Double previousValue = myStepIdToWeight.put(theStepId, theWeight);
			Validate.isTrue(previousValue == null, "Already have weight for step: %s", theStepId);
		}

		public <PT extends IModelJson> StepWeightingForProgressCalculator build(
				List<JobDefinitionStep<PT, ?, ?>> theSteps) {

			Set<String> remainingJobStepIds =
					theSteps.stream().map(JobDefinitionStep::getStepId).collect(Collectors.toSet());

			double combinedWeightTotal = 0.0;
			for (String stepId : myStepIdToWeight.keySet()) {
				Validate.isTrue(
						remainingJobStepIds.remove(stepId), "Step weight provided for invalid step ID: %s", stepId);
				combinedWeightTotal += myStepIdToWeight.get(stepId);
			}

			int remainingStepCount = remainingJobStepIds.size();
			double weightForRemainingSteps = 0.0;
			if (remainingStepCount > 0) {
				Validate.isTrue(
						combinedWeightTotal < 1.0,
						"All steps must have at least a small amount of weight, no remaining room for steps: %s",
						remainingJobStepIds);
				weightForRemainingSteps = (1.0 - combinedWeightTotal);
			}

			return new StepWeightingForProgressCalculator(
					myStepIdToWeight, remainingJobStepIds, weightForRemainingSteps);
		}
	}
}
