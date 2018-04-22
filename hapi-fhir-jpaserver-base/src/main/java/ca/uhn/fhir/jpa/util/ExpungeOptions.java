package ca.uhn.fhir.jpa.util;

public class ExpungeOptions {
	private int myLimit = 1000;
	private boolean myExpungeOldVersions;
	private boolean myExpungeDeletedResources;
	private boolean myExpungeEverything;

	/**
	 * The maximum number of resources versions to expunge
	 */
	public int getLimit() {
		return myLimit;
	}

	public boolean isExpungeEverything() {
		return myExpungeEverything;
	}

	public ExpungeOptions setExpungeEverything(boolean theExpungeEverything) {
		myExpungeEverything = theExpungeEverything;
		return this;
	}

	/**
	 * The maximum number of resource versions to expunge
	 */
	public void setLimit(int theLimit) {
		myLimit = theLimit;
	}

	public boolean isExpungeDeletedResources() {
		return myExpungeDeletedResources;
	}

	public ExpungeOptions setExpungeDeletedResources(boolean theExpungeDeletedResources) {
		myExpungeDeletedResources = theExpungeDeletedResources;
		return this;
	}

	public boolean isExpungeOldVersions() {
		return myExpungeOldVersions;
	}

	public ExpungeOptions setExpungeOldVersions(boolean theExpungeOldVersions) {
		myExpungeOldVersions = theExpungeOldVersions;
		return this;
	}
}
