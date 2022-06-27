package ca.uhn.fhir.batch2.model;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class FetchJobInstancesRequest {
	
	private final String myJobDefinition;
	
	private final String myParameters;

	private final Set<StatusEnum> myStatuses = new HashSet<>();

	public FetchJobInstancesRequest(@Nonnull String theJobDefinition,
											  @Nonnull String theParameters) {
		myJobDefinition = theJobDefinition;
		myParameters = theParameters;
	}

	public String getJobDefinition() {
		return myJobDefinition;
	}

	public String getParameters() {
		return myParameters;
	}

	public void addStatus(StatusEnum theStatusEnum) {
		myStatuses.add(theStatusEnum);
	}

	public Set<StatusEnum> getStatuses() {
		return myStatuses;
	}
}
