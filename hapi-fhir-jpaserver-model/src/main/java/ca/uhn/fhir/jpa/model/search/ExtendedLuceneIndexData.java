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

	// wip mb add the Resource - do we already have it as json somewhere?
	// wip mb this needs to be something bigger.
	final private Map<String, String> mySearchParamTexts;

	public ExtendedLuceneIndexData() {
		this.mySearchParamTexts = new HashMap<>();
	}

	public Map<String, String> getMap() {
		return Collections.unmodifiableMap(mySearchParamTexts);
	}

	public void writeIndexElements(DocumentElement theDocument) {
		DocumentElement searchParamHolder = theDocument.addObject("sp");

		// WIP Use RestSearchParameterTypeEnum to define templates.
		mySearchParamTexts.forEach((key, value) -> {
			DocumentElement spNode = searchParamHolder.addObject(key);
			DocumentElement stringIndexNode = spNode.addObject("string");
			stringIndexNode.addValue("text", value);
			ourLog.trace("Adding Search Param Text: {}{} -- {}", SearchParamTextPropertyBinder.SEARCH_PARAM_TEXT_PREFIX, key, value);
		});
	}

	public void addIndexData(String theSpName, String theText) {
		mySearchParamTexts.put(theSpName, theText);
	}
}
