package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateSearchIndexWriter {
	private static final Logger ourLog = LoggerFactory.getLogger(HibernateSearchIndexWriter.class);
	final HibernateSearchElementCache myNodeCache;
	final FhirContext myFhirContext;

	HibernateSearchIndexWriter(FhirContext theFhirContext, DocumentElement theRoot) {
		myFhirContext = theFhirContext;
		myNodeCache = new HibernateSearchElementCache(theRoot);
	}

	public DocumentElement getSearchParamIndexNode(String theSearchParamName, String theIndexType) {
		return myNodeCache.getNode("sp", theSearchParamName, theIndexType);

	}

	public static HibernateSearchIndexWriter forRoot(FhirContext theFhirContext, DocumentElement theDocument) {
		return new HibernateSearchIndexWriter(theFhirContext, theDocument);
	}

	public void writeStringIndex(String theSearchParam, String theValue) {
		DocumentElement stringIndexNode = getSearchParamIndexNode(theSearchParam, "string");
		stringIndexNode.addValue("text", theValue);
		ourLog.trace("Adding Search Param Text: {} -- {}", theSearchParam, theValue);
	}

	public void writeTokenIndex(String theSearchParam, TokenParam theValue) {
		DocumentElement tokenIndexNode = getSearchParamIndexNode(theSearchParam, "token");
		tokenIndexNode.addValue("code", theValue.getValue());
		tokenIndexNode.addValue("system", theValue.getSystem());
		//This next one returns as system|value
		tokenIndexNode.addValue("code-system", theValue.getValueAsQueryToken(myFhirContext));
		ourLog.trace("Adding Search Param Token: {} -- {}", theSearchParam, theValue);
	}
}
