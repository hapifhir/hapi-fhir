package ca.uhn.fhir.jpa.search;

public interface IStaleSearchDeletingSvc {

	void pollForStaleSearchesAndDeleteThem();

	void schedulePollForStaleSearches();

}
