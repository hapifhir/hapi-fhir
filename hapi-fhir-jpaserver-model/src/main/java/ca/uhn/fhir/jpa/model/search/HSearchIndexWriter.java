/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.fhir.ucum.Pair;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class HSearchIndexWriter {
	private static final Logger ourLog = LoggerFactory.getLogger(HSearchIndexWriter.class);

	public static final String NESTED_SEARCH_PARAM_ROOT = "nsp";
	public static final String SEARCH_PARAM_ROOT = "sp";
	public static final String INDEX_TYPE_STRING = "string";
	public static final String IDX_STRING_NORMALIZED = "norm";
	public static final String IDX_STRING_EXACT = "exact";
	public static final String IDX_STRING_TEXT = "text";
	public static final String IDX_STRING_LOWER = "lower";

	public static final String INDEX_TYPE_TOKEN = "token";
	public static final String TOKEN_CODE = "code";
	public static final String TOKEN_SYSTEM = "system";
	public static final String TOKEN_SYSTEM_CODE = "code-system";
	public static final String INDEX_TYPE_QUANTITY = "quantity";

	// numeric
	public static final String VALUE_FIELD = "value";
	public static final String QTY_CODE = TOKEN_CODE;
	public static final String QTY_SYSTEM = TOKEN_SYSTEM;
	public static final String QTY_VALUE = VALUE_FIELD;
	public static final String QTY_CODE_NORM = "code-norm";
	public static final String QTY_VALUE_NORM = "value-norm";

	public static final String URI_VALUE = "uri-value";

	public static final String NUMBER_VALUE = "number-value";

	public static final String DATE_LOWER_ORD = "lower-ord";
	public static final String DATE_LOWER = "lower";
	public static final String DATE_UPPER_ORD = "upper-ord";
	public static final String DATE_UPPER = "upper";

	final HSearchElementCache myNodeCache;
	final StorageSettings myStorageSettings;

	HSearchIndexWriter(StorageSettings theStorageSettings, DocumentElement theRoot) {
		myStorageSettings = theStorageSettings;
		myNodeCache = new HSearchElementCache(theRoot);
	}

	public DocumentElement getSearchParamIndexNode(String theSearchParamName, String theIndexType) {
		return myNodeCache.getObjectElement(SEARCH_PARAM_ROOT, theSearchParamName, theIndexType);
	}

	public static HSearchIndexWriter forRoot(StorageSettings theStorageSettings, DocumentElement theDocument) {
		return new HSearchIndexWriter(theStorageSettings, theDocument);
	}

	public void writeStringIndex(String theSearchParam, String theValue) {
		DocumentElement stringIndexNode = getSearchParamIndexNode(theSearchParam, INDEX_TYPE_STRING);

		// we are assuming that our analyzer matches
		// StringUtil.normalizeStringForSearchIndexing(theValue).toLowerCase(Locale.ROOT))
		writeBasicStringFields(stringIndexNode, theValue);
		addDocumentValue(stringIndexNode, IDX_STRING_EXACT, theValue);
		addDocumentValue(stringIndexNode, IDX_STRING_TEXT, theValue);
		addDocumentValue(stringIndexNode, IDX_STRING_LOWER, theValue);

		ourLog.debug("Adding Search Param Text: {} -- {}", theSearchParam, theValue);
	}

	public void writeBasicStringFields(DocumentElement theIndexNode, String theValue) {
		addDocumentValue(theIndexNode, IDX_STRING_NORMALIZED, theValue);
	}

	public void writeTokenIndex(String theSearchParam, IBaseCoding theValue) {
		DocumentElement nestedRoot = myNodeCache.getObjectElement(NESTED_SEARCH_PARAM_ROOT);
		DocumentElement nestedSpNode = nestedRoot.addObject(theSearchParam);
		DocumentElement nestedTokenNode = nestedSpNode.addObject(INDEX_TYPE_TOKEN);

		writeTokenFields(nestedTokenNode, theValue);

		if (StringUtils.isNotEmpty(theValue.getDisplay())) {
			DocumentElement nestedStringNode = nestedSpNode.addObject(INDEX_TYPE_STRING);
			addDocumentValue(nestedStringNode, IDX_STRING_TEXT, theValue.getDisplay());
		}

		DocumentElement tokenIndexNode = getSearchParamIndexNode(theSearchParam, INDEX_TYPE_TOKEN);
		writeTokenFields(tokenIndexNode, theValue);
		ourLog.debug("Adding Search Param Token: {} -- {}", theSearchParam, theValue);
	}

	public void writeTokenFields(DocumentElement theDocumentElement, IBaseCoding theValue) {
		addDocumentValue(theDocumentElement, TOKEN_CODE, theValue.getCode());
		addDocumentValue(theDocumentElement, TOKEN_SYSTEM, theValue.getSystem());
		addDocumentValue(theDocumentElement, TOKEN_SYSTEM_CODE, theValue.getSystem() + "|" + theValue.getCode());
	}

	private void addDocumentValue(DocumentElement theDocumentElement, String theKey, Object theValue) {
		if (theValue != null) {
			theDocumentElement.addValue(theKey, theValue);
		}
	}

	public void writeReferenceIndex(String theSearchParam, String theValue) {
		DocumentElement referenceIndexNode = getSearchParamIndexNode(theSearchParam, "reference");
		addDocumentValue(referenceIndexNode, VALUE_FIELD, theValue);
		ourLog.trace("Adding Search Param Reference: {} -- {}", theSearchParam, theValue);
	}

	public void writeDateIndex(String theSearchParam, DateSearchIndexData theValue) {
		DocumentElement dateIndexNode = getSearchParamIndexNode(theSearchParam, "dt");
		writeDateFields(dateIndexNode, theValue);

		ourLog.trace("Adding Search Param Date. param: {} -- {}", theSearchParam, theValue);
	}

	public void writeDateFields(DocumentElement dateIndexNode, DateSearchIndexData theValue) {
		// Lower bound
		addDocumentValue(dateIndexNode, DATE_LOWER_ORD, theValue.getLowerBoundOrdinal());
		addDocumentValue(dateIndexNode, DATE_LOWER, theValue.getLowerBoundDate().toInstant());
		// Upper bound
		addDocumentValue(dateIndexNode, DATE_UPPER_ORD, theValue.getUpperBoundOrdinal());
		addDocumentValue(dateIndexNode, DATE_UPPER, theValue.getUpperBoundDate().toInstant());
	}

	public void writeQuantityIndex(String theSearchParam, QuantitySearchIndexData theValue) {
		DocumentElement nestedRoot = myNodeCache.getObjectElement(NESTED_SEARCH_PARAM_ROOT);

		DocumentElement nestedSpNode = nestedRoot.addObject(theSearchParam);
		DocumentElement nestedQtyNode = nestedSpNode.addObject(INDEX_TYPE_QUANTITY);

		ourLog.trace("Adding Search Param Quantity: {} -- {}", theSearchParam, theValue);
		writeQuantityFields(nestedQtyNode, theValue);
	}

	public void writeQuantityFields(DocumentElement nestedQtyNode, QuantitySearchIndexData theValue) {
		addDocumentValue(nestedQtyNode, QTY_CODE, theValue.getCode());
		addDocumentValue(nestedQtyNode, QTY_SYSTEM, theValue.getSystem());
		addDocumentValue(nestedQtyNode, QTY_VALUE, theValue.getValue());

		if (!myStorageSettings.getNormalizedQuantitySearchLevel().storageOrSearchSupported()) {
			return;
		}

		// -- convert the value/unit to the canonical form if any
		Pair canonicalForm = UcumServiceUtil.getCanonicalForm(
				theValue.getSystem(), BigDecimal.valueOf(theValue.getValue()), theValue.getCode());
		if (canonicalForm == null) {
			return;
		}

		double canonicalValue = Double.parseDouble(canonicalForm.getValue().asDecimal());
		String canonicalUnits = canonicalForm.getCode();

		addDocumentValue(nestedQtyNode, QTY_CODE_NORM, canonicalUnits);
		addDocumentValue(nestedQtyNode, QTY_VALUE_NORM, canonicalValue);
	}

	public void writeUriIndex(String theParamName, Collection<String> theUriValueCollection) {
		DocumentElement uriNode =
				myNodeCache.getObjectElement(SEARCH_PARAM_ROOT).addObject(theParamName);
		for (String uriSearchIndexValue : theUriValueCollection) {
			ourLog.trace("Adding Search Param Uri: {} -- {}", theParamName, uriSearchIndexValue);
			writeUriFields(uriNode, uriSearchIndexValue);
		}
	}

	public void writeUriFields(DocumentElement uriNode, String uriSearchIndexValue) {
		addDocumentValue(uriNode, URI_VALUE, uriSearchIndexValue);
	}

	public void writeNumberIndex(String theParamName, Collection<BigDecimal> theNumberValueCollection) {
		DocumentElement numberNode =
				myNodeCache.getObjectElement(SEARCH_PARAM_ROOT).addObject(theParamName);
		for (BigDecimal numberSearchIndexValue : theNumberValueCollection) {
			ourLog.trace("Adding Search Param Number: {} -- {}", theParamName, numberSearchIndexValue);
			writeNumberFields(numberNode, numberSearchIndexValue);
		}
	}

	public void writeNumberFields(DocumentElement numberNode, BigDecimal numberSearchIndexValue) {
		addDocumentValue(numberNode, NUMBER_VALUE, numberSearchIndexValue.doubleValue());
	}

	/**
	 * @param ignoredParamName unused - for consistent api
	 * @param theCompositeSearchIndexData extracted index data for this sp
	 */
	public void writeCompositeIndex(
			String ignoredParamName, Set<CompositeSearchIndexData> theCompositeSearchIndexData) {
		// must be nested.
		for (CompositeSearchIndexData compositeSearchIndexDatum : theCompositeSearchIndexData) {
			compositeSearchIndexDatum.writeIndexEntry(this, myNodeCache);
		}
	}
}
