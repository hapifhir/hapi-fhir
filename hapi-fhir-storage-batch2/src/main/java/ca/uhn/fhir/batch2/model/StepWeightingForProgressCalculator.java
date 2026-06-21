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
 * An individual step may have an explicit weight assigned, which is a double that
 * is <code>&gt; 0.0</code> and <code>&lt; 1.0</code>.
 */
public class StepWeightingForProgressCalculator {
	private static final double WEIGHT_SUM_EPSILON = 1e-9;

	private final double myWeightForStepsWithoutExplicitWeight;
	private final Set<String> myStepIdsWithoutExplicitWeight;
	private final Map<String, Double> myStepIdToWeight;

	StepWeightingForProgressCalculator(
			Map<String, Double> theStepIdToWeight,
			Set<String> theStepIdsWithoutExplicitWeight,
			double theWeightForStepsWithoutExplicitWeight) {
		Validate.notNull(theStepIdToWeight, "theStepIdToWeight must not be null");
		myStepIdToWeight = Map.copyOf(theStepIdToWeight);
		myStepIdsWithoutExplicitWeight = Set.copyOf(theStepIdsWithoutExplicitWeight);
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
			Validate.notBlank(theStepId, "theStepId must not be blank");
			Validate.isTrue(theWeight < 1.0d, "theWeight must be >= 0.0 and < 1.0");
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

			/*
			 * Because double is IEEE 754 binary floating point, most "round" decimals like
			 * 0.1, 0.3, 0.4 cannot be represented exactly in binary — they're stored as the
			 * nearest representable value, slightly above or below. When you add several of
			 * them, the tiny representation errors accumulate, and the sum can land just
			 * above the true mathematical total. Weights such as 0.3 + 0.3 + 0.4 can sum to
			 * 1.0000000000000002 and spuriously cause failures.
			 */
			Validate.isTrue(
					combinedWeightTotal <= 1.0 + WEIGHT_SUM_EPSILON,
					"Combined step weights can not be greater than 1.0, but was %s",
					combinedWeightTotal);

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
