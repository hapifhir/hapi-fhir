package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class StepExecutionDetails {

	private final ListMultimap<String, JobInstanceParameter> myParameters;
	private final Map<String, Object> myData;

	public StepExecutionDetails(@Nonnull ListMultimap<String, JobInstanceParameter> theParameters, @Nullable Map<String, Object> theData) {
		Validate.notNull(theParameters);
		myParameters = Multimaps.unmodifiableListMultimap(theParameters);
		myData = theData;
	}

	/**
	 * Returns the data associated with this step execution. This method should never be
	 * called during the first step of a job, and will never return <code>null</code> during
	 * any subsequent steps.
	 *
	 * @throws NullPointerException If this method is called during the first step of a job
	 */
	@Nonnull
	public Map<String, Object> getData() {
		Validate.notNull(myData);
		return myData;
	}

	/**
	 * Returns the parameters associated with this job instance. Note that parameters
	 * are set when the job instance is created and can not be modified after that.
	 */
	@Nonnull
	public ListMultimap<String, JobInstanceParameter> getParameters() {
		return myParameters;
	}

	public List<String> getParameterValues(@Nonnull String theParamName) {
		Validate.notBlank(theParamName);

		return myParameters
			.get(theParamName)
			.stream()
			.map(t -> t.getValue())
			.filter(t -> isNotBlank(t))
			.collect(Collectors.toList());
	}

	public Optional<String> getParameterValue(@Nonnull String theParamName) {
		return myParameters
			.get(theParamName)
			.stream()
			.map(t -> t.getValue())
			.filter(theValue -> isNotBlank(theValue))
			.findFirst();
	}

	public Optional<Integer> getParameterValueInteger(@Nonnull String theParamName) {
		return myParameters.get(theParamName)
			.stream()
			.filter(t -> isNotBlank(t.getValue()))
			.map(t -> parseIntegerParam(t))
			.findFirst();
	}

	private Integer parseIntegerParam(JobInstanceParameter theParameter) {
		String value = theParameter.getValue();
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new JobExecutionFailedException("Invalid parameter value for parameter " + theParameter.getName() + ". Expected integer.");
		}
	}
}
