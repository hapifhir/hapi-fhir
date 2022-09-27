package ca.uhn.fhir.jpa.model.search;

public interface CompositeSearchIndexData {
	void writeIndexEntry(HSearchIndexWriter theHSearchIndexWriter, HSearchElementCache theRoot);
}
