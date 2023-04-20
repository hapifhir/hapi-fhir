package ca.uhn.fhir.jpa.api.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ReindexOutcome {

	private List<String> myWarnings;

	public List<String> getWarnings() {
		return defaultIfNull(myWarnings, Collections.emptyList());
	}

	public void addWarning(String theWarning) {
		if (myWarnings == null) {
			myWarnings = new ArrayList<>();
		}
		myWarnings.add(theWarning);
	}

}
