package ca.uhn.fhir.jpa.model.search;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;

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
		// TODO mb we can use a token_filter with pattern_capture to generate all three off a single value.  Do this next, after merge.
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

	public void writeDateIndex(String theSearchParam, DateRangeParam theValue) {
		DocumentElement dateIndexNode = getSearchParamIndexNode(theSearchParam, "dt");
		// Lower bound
		dateIndexNode.addValue("lower-ord", theValue.getLowerBoundAsDateInteger());
		dateIndexNode.addValue("lower", LocalDateTime.ofInstant(theValue.getLowerBoundAsInstant().toInstant(), ZoneId.systemDefault()));
		// Upper bound
		dateIndexNode.addValue("upper-ord", theValue.getUpperBoundAsDateInteger());
		dateIndexNode.addValue("upper", LocalDateTime.ofInstant(theValue.getUpperBoundAsInstant().toInstant(), ZoneId.systemDefault()));
		ourLog.trace("Adding Search Param Reference: {} -- {}", theSearchParam, theValue);
	}
}
