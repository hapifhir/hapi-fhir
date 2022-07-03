package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.model.api.IModelJson;

import javax.annotation.Nonnull;

public class JobErrorDetails<PT extends IModelJson> extends JobCompletionDetails<PT> {
	private final String myErrorMessage;
	private final int myErrorCount;
	private final StatusEnum myStatus;

	public JobErrorDetails(@Nonnull PT theParameters, @Nonnull String theInstanceId, String theErrorMessage, int theErrorCount, StatusEnum theStatus) {
		super(theParameters, theInstanceId);
		myErrorMessage = theErrorMessage;
		myErrorCount = theErrorCount;
		myStatus = theStatus;
	}

	public String getErrorMessage() {
		return myErrorMessage;
	}

	public int getErrorCount() {
		return myErrorCount;
	}

	public StatusEnum getStatus() {
		return myStatus;
	}
}
