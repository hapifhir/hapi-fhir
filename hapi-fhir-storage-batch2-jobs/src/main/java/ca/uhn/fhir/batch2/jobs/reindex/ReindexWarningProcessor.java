package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IWarningProcessor;

public class ReindexWarningProcessor implements IWarningProcessor {

	private String myRecoveredWarning;

	@Override
	public void recoverWarningMessage(String theErrorMessage) {
		// save non-fatal error as warning, current only support unique search param reindexing error on existing duplicates
		if (theErrorMessage.contains("Can not create resource") && theErrorMessage.contains("it would create a duplicate unique index matching query")) {
			String searchParamName = theErrorMessage.substring(theErrorMessage.indexOf("SearchParameter"), theErrorMessage.length() - 1);
			myRecoveredWarning = "Failed to reindex resource because unique search parameter " + searchParamName + " could not be enforced.";
		}
	}

	@Override
	public String getRecoveredWarningMessage() {
		return myRecoveredWarning;
	}
}
