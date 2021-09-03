package ca.uhn.fhir.jpa.model.search;

import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Collects our lucene extended indexing data.
 *
 */
public class ExtendedLuceneIndexData {
	private static final Logger ourLog = LoggerFactory.getLogger(ExtendedLuceneIndexData.class);

	// wipmb add the Resource - do we already have it as json somewhere?
	// TODO figure out the document layout.  Flat sp + modfier for now.
	final private Map<String, String> mySearchParamTexts;

	public ExtendedLuceneIndexData() {
		this.mySearchParamTexts = new HashMap<>();
	}

	// TODO mb delete?
	public ExtendedLuceneIndexData(Map<String, String> theSearchParamTexts) {
		this.mySearchParamTexts = theSearchParamTexts;
	}

	public Map<String, String> getMap() {
		return Collections.unmodifiableMap(mySearchParamTexts);
	}

	public void writeIndexElements(DocumentElement theDocument) {
		mySearchParamTexts.entrySet()
			.forEach(entry -> {
				theDocument.addValue(SearchParamTextPropertyBinder.SEARCH_PARAM_TEXT_PREFIX + entry.getKey(), entry.getValue());
				ourLog.trace("Adding Search Param Text: {}{} -- {}", SearchParamTextPropertyBinder.SEARCH_PARAM_TEXT_PREFIX, entry.getKey(), entry.getValue());
			});
	}

	public void addIndexData(String theSpName, String theText) {
		mySearchParamTexts.put(theSpName, theText);
	}
}
