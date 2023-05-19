package ca.uhn.fhir.jpa.searchparam.matcher;

public class ExtraMatchParams {
	/**
	 * Strings, particularly, could have exact matches (case, accents, etc)
	 * or inexact for non-normalized strings that don't have these things.
	 */
	private boolean myIsExactMatch;

	/**
	 * Identification system for Identification matching
	 */
	private String myIdentificationSystem;

	public boolean isExactMatch() {
		return myIsExactMatch;
	}

	public void setExactMatch(boolean theExactMatch) {
		myIsExactMatch = theExactMatch;
	}

	public String getIdentificationSystem() {
		return myIdentificationSystem;
	}

	public void setIdentificationSystem(String theIdentificationSystem) {
		myIdentificationSystem = theIdentificationSystem;
	}
}
