package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateSearchIndexWriter {
	private static final Logger ourLog = LoggerFactory.getLogger(HibernateSearchIndexWriter.class);
	public static final String IDX_STRING_NORMALIZED = "norm";
	public static final String IDX_STRING_EXACT = "exact";
	public static final String IDX_STRING_TEXT = "text";
	final HibernateSearchElementCache myNodeCache;
	final FhirContext myFhirContext;

	HibernateSearchIndexWriter(FhirContext theFhirContext, DocumentElement theRoot) {
		myFhirContext = theFhirContext;
		myNodeCache = new HibernateSearchElementCache(theRoot);
	}

	public DocumentElement getSearchParamIndexNode(String theSearchParamName, String theIndexType) {
		return myNodeCache.getObjectElement("sp", theSearchParamName, theIndexType);

	}

	public static HibernateSearchIndexWriter forRoot(FhirContext theFhirContext, DocumentElement theDocument) {
		return new HibernateSearchIndexWriter(theFhirContext, theDocument);
	}

	public void writeStringIndex(String theSearchParam, String theValue) {
		DocumentElement stringIndexNode = getSearchParamIndexNode(theSearchParam, "string");

		stringIndexNode.addValue(IDX_STRING_NORMALIZED, theValue);// for default search
		stringIndexNode.addValue(IDX_STRING_EXACT, theValue);
		stringIndexNode.addValue(IDX_STRING_TEXT, theValue);
		ourLog.debug("Adding Search Param Text: {} -- {}", theSearchParam, theValue);
	}

	public void writeTokenIndex(String theSearchParam, TokenParam theValue) {
		DocumentElement tokenIndexNode = getSearchParamIndexNode(theSearchParam, "token");
		// wip can we instead add formatted versions to the same field and simplify the query?
		// or use a token_filter to generate all three off a single value?
		tokenIndexNode.addValue("code", theValue.getValue());
		tokenIndexNode.addValue("system", theValue.getSystem());
		//This next one returns as system|value
		tokenIndexNode.addValue("code-system", theValue.getValueAsQueryToken(myFhirContext));
		ourLog.debug("Adding Search Param Token: {} -- {}", theSearchParam, theValue);
	}

    public void writeReferenceIndex(String theSearchParam, String theValue) {
		 DocumentElement referenceIndexNode = getSearchParamIndexNode(theSearchParam, "reference");
		 referenceIndexNode.addValue("value", theValue);
		 ourLog.trace("Adding Search Param Reference: {} -- {}", theSearchParam, theValue);
    }
}
