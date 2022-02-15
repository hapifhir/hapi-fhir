package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobInstanceParameters;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class StepExecutionDetails {

	private final JobInstanceParameters myParameters;
	private final Map<String, Object> myData;

	public StepExecutionDetails(@Nonnull JobInstanceParameters theParameters, @Nullable Map<String, Object> theData) {
		Validate.notNull(theParameters);
		myParameters = theParameters;
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
	 *
	 * @return
	 */
	@Nonnull
	public JobInstanceParameters getParameters() {
		return myParameters;
	}

}
