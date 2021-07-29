package ca.uhn.fhir.jpa.model.search;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SearchParamTextWrapper {
	final private Map<String, String> mySearchParamTexts;

	public SearchParamTextWrapper(Map<String, String> theSearchParamTexts) {
		this.mySearchParamTexts = theSearchParamTexts;
	}

	public Set<Map.Entry<String, String>> entrySet() {
		return mySearchParamTexts.entrySet();
	}

	public Map<String, String> getMap() {
		return Collections.unmodifiableMap(mySearchParamTexts);
	}
}
