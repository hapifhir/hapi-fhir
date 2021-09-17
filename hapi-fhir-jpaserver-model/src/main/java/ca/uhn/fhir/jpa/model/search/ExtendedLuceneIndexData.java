package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.rest.param.TokenParam;
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
	final private Map<String, String> mySearchParamStrings;
	private Map<String, TokenParam> mySearchParamTokens;
	private FhirContext myFhirContext;

	public ExtendedLuceneIndexData(FhirContext theFhirContext) {
		this.myFhirContext = theFhirContext;
		this.mySearchParamStrings = new HashMap<>();
		this.mySearchParamTokens= new HashMap<>();
	}

	public Map<String, String> getMap() {
		return Collections.unmodifiableMap(mySearchParamStrings);
	}

	public void writeIndexElements(DocumentElement theDocument) {
		DocumentElement searchParamHolder = theDocument.addObject("sp");

		// WIP Use RestSearchParameterTypeEnum to define templates.
		mySearchParamStrings.forEach((key, value) -> {
			DocumentElement spNode = searchParamHolder.addObject(key);
			DocumentElement stringIndexNode = spNode.addObject("string");
			stringIndexNode.addValue("text", value);
			ourLog.trace("Adding Search Param Text: {}{} -- {}", SearchParamTextPropertyBinder.SEARCH_PARAM_TEXT_PREFIX, key, value);
		});
		//WIP: storing tokens can probably be done better than this.
		mySearchParamTokens.forEach((key, value) -> {
			DocumentElement spNode = searchParamHolder.addObject(key);
			DocumentElement tokenIndexNode = spNode.addObject("token");
			tokenIndexNode.addValue("code", value.getValue());
			tokenIndexNode.addValue("system", value.getSystem());
			//This next one returns as system|value
			tokenIndexNode.addValue("code-system", value.getValueAsQueryToken(myFhirContext));
			ourLog.trace("Adding Search Param Token: {}{} -- {}", SearchParamTextPropertyBinder.SEARCH_PARAM_TEXT_PREFIX, key, value);
		});
	}

	public void addStringIndexData(String theSpName, String theText) {
		String value = theText;
		if (mySearchParamStrings.containsKey(theSpName)) {
			value = mySearchParamStrings.get(theSpName) + " " + value;
		}
		mySearchParamStrings.put(theSpName, value);
	}

	public void addTokenIndexData(String theSpName, String theSystem,  String theValue) {
		mySearchParamTokens.put(theSpName, new TokenParam(theSystem, theValue));
	}
}
