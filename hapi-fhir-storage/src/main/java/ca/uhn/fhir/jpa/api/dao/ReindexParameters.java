package ca.uhn.fhir.jpa.api.dao;

public class ReindexParameters {
	public static final boolean REINDEX_SEARCH_PARAMETERS_DEFAULT = true;
	public static final boolean OPTIMISTIC_LOCK_DEFAULT = true;
	public static final boolean OPTIMIZE_STORAGE_DEFAULT = false;
	private boolean myReindexSearchParameters = REINDEX_SEARCH_PARAMETERS_DEFAULT;
	private boolean myOptimizeStorage = OPTIMIZE_STORAGE_DEFAULT;
	private boolean myOptimisticLock = OPTIMISTIC_LOCK_DEFAULT;

	public boolean isOptimisticLock() {
		return myOptimisticLock;
	}

	public ReindexParameters setOptimisticLock(boolean theOptimisticLock) {
		myOptimisticLock = theOptimisticLock;
		return this;
	}

	public boolean isReindexSearchParameters() {
		return myReindexSearchParameters;
	}

	public ReindexParameters setReindexSearchParameters(boolean theReindexSearchParameters) {
		myReindexSearchParameters = theReindexSearchParameters;
		return this;
	}

	public boolean isOptimizeStorage() {
		return myOptimizeStorage;
	}

	public ReindexParameters setOptimizeStorage(boolean theOptimizeStorage) {
		myOptimizeStorage = theOptimizeStorage;
		return this;
	}

}
