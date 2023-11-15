package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import ca.uhn.fhir.jpa.entity.MdmLink;

import java.util.ArrayList;
import java.util.List;

public class MDMLinkResults {

	private List<MdmLink> myResults;

	public List<MdmLink> getResults() {
		if (myResults == null) {
			myResults = new ArrayList<>();
		}
		return myResults;
	}

	public MDMLinkResults addResult(MdmLink theLink) {
		getResults().add(theLink);
		return this;
	}

	public void setResults(List<MdmLink> theResults) {
		myResults = theResults;
	}
}
