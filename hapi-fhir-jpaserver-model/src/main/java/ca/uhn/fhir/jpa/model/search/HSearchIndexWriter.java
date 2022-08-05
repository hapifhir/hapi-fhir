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
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.fhir.ucum.Pair;
import org.hibernate.search.engine.backend.document.DocumentElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Collection;

public class HSearchIndexWriter {
	private static final Logger ourLog = LoggerFactory.getLogger(HSearchIndexWriter.class);
	public static final String IDX_STRING_NORMALIZED = "norm";
	public static final String IDX_STRING_EXACT = "exact";
	public static final String IDX_STRING_TEXT = "text";
	public static final String IDX_STRING_LOWER = "lower";
	public static final String NESTED_SEARCH_PARAM_ROOT = "nsp";
	public static final String SEARCH_PARAM_ROOT = "sp";

	public static final String QTY_PARAM_NAME = "quantity";
	public static final String QTY_CODE = "code";
	public static final String QTY_SYSTEM = "system";
	public static final String QTY_VALUE = "value";
	public static final String QTY_CODE_NORM = "code-norm";
	public static final String QTY_VALUE_NORM = "value-norm";

	public static final String URI_VALUE = "uri-value";

	// fixme mb these are misguided
	public static final String COMPOS_PARAM_NAME = "obs-composite";
	public static final String COMPOS_CODE_SYSTEM = "code-system";
	public static final String COMPOS_CODE_VALUE = "code-value";
	public static final String COMPOS_QTY_CODE = "qty-code";
	public static final String COMPOS_QTY_SYSTEM = "qty-system";
	public static final String COMPOS_QTY_VALUE = "qty-value";
	public static final String COMPOS_QTY_CODE_NORM = "qty-code-norm";
	public static final String COMPOS_QTY_VALUE_NORM = "qty-value-norm";
	public static final String COMPOS_CONCEPT_CODE = "concept-code";
	public static final String COMPOS_CONCEPT_TEXT = "concept-text";
	public static final String NUMBER_VALUE = "number-value";


	final HSearchElementCache myNodeCache;
	final FhirContext myFhirContext;
	final ModelConfig myModelConfig;

	HSearchIndexWriter(FhirContext theFhirContext, ModelConfig theModelConfig, DocumentElement theRoot) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myNodeCache = new HSearchElementCache(theRoot);
	}

	public DocumentElement getSearchParamIndexNode(String theSearchParamName, String theIndexType) {
		return myNodeCache.getObjectElement(SEARCH_PARAM_ROOT, theSearchParamName, theIndexType);
	}

	public static HSearchIndexWriter forRoot(
			FhirContext theFhirContext, ModelConfig theModelConfig, DocumentElement theDocument) {
		return new HSearchIndexWriter(theFhirContext, theModelConfig, theDocument);
	}

	public void writeStringIndex(String theSearchParam, String theValue) {
		DocumentElement stringIndexNode = getSearchParamIndexNode(theSearchParam, "string");

		// we are assuming that our analyzer matches  StringUtil.normalizeStringForSearchIndexing(theValue).toLowerCase(Locale.ROOT))
		stringIndexNode.addValue(IDX_STRING_NORMALIZED, theValue);// for default search
		stringIndexNode.addValue(IDX_STRING_EXACT, theValue);
		stringIndexNode.addValue(IDX_STRING_TEXT, theValue);
		stringIndexNode.addValue(IDX_STRING_LOWER, theValue);

		ourLog.debug("Adding Search Param Text: {} -- {}", theSearchParam, theValue);
	}

	public void writeTokenIndex(String theSearchParam, IBaseCoding theValue) {
		DocumentElement nestedRoot = myNodeCache.getObjectElement(NESTED_SEARCH_PARAM_ROOT);
		DocumentElement nestedSpNode = nestedRoot.addObject(theSearchParam);
		DocumentElement nestedTokenNode = nestedSpNode.addObject("token");

		nestedTokenNode.addValue("code", theValue.getCode());
		nestedTokenNode.addValue("system", theValue.getSystem());
		nestedTokenNode.addValue("code-system", theValue.getSystem() + "|" + theValue.getCode());

		if (StringUtils.isNotEmpty(theValue.getDisplay())) {
			DocumentElement nestedStringNode = nestedSpNode.addObject("string");
			nestedStringNode.addValue(IDX_STRING_TEXT, theValue.getDisplay());
		}

		DocumentElement tokenIndexNode = getSearchParamIndexNode(theSearchParam, "token");
		// TODO mb we can use a token_filter with pattern_capture to generate all three off a single value.  Do this next, after merge.
		tokenIndexNode.addValue("code", theValue.getCode());
		tokenIndexNode.addValue("system", theValue.getSystem());
		tokenIndexNode.addValue("code-system", theValue.getSystem() + "|" + theValue.getCode());
		ourLog.debug("Adding Search Param Token: {} -- {}", theSearchParam, theValue);
		// TODO mb should we write the strings here too?  Or leave it to the old spidx indexing?
	}

	public void writeReferenceIndex(String theSearchParam, String theValue) {
		DocumentElement referenceIndexNode = getSearchParamIndexNode(theSearchParam, "reference");
		referenceIndexNode.addValue("value", theValue);
		ourLog.trace("Adding Search Param Reference: {} -- {}", theSearchParam, theValue);
	}

	public void writeDateIndex(String theSearchParam, DateSearchIndexData theValue) {
		DocumentElement dateIndexNode = getSearchParamIndexNode(theSearchParam, "dt");
		// Lower bound
		dateIndexNode.addValue("lower-ord", theValue.getLowerBoundOrdinal());
		dateIndexNode.addValue("lower", theValue.getLowerBoundDate().toInstant());
		// Upper bound
		dateIndexNode.addValue("upper-ord", theValue.getUpperBoundOrdinal());
		dateIndexNode.addValue("upper", theValue.getUpperBoundDate().toInstant());

		ourLog.trace("Adding Search Param Date. param: {} -- {}", theSearchParam, theValue);
	}



	public void writeQuantityIndex(String theSearchParam, Collection<QuantitySearchIndexData> theValueCollection) {
		DocumentElement nestedRoot = myNodeCache.getObjectElement(NESTED_SEARCH_PARAM_ROOT);

		for (QuantitySearchIndexData theValue : theValueCollection) {
			DocumentElement nestedSpNode = nestedRoot.addObject(theSearchParam);
			DocumentElement nestedQtyNode = nestedSpNode.addObject(QTY_PARAM_NAME);

			ourLog.trace("Adding Search Param Quantity: {} -- {}", theSearchParam, theValue);
			nestedQtyNode.addValue(QTY_CODE, theValue.getCode());
			nestedQtyNode.addValue(QTY_SYSTEM, theValue.getSystem());
			nestedQtyNode.addValue(QTY_VALUE, theValue.getValue());

			if ( ! myModelConfig.getNormalizedQuantitySearchLevel().storageOrSearchSupported()) { continue; }

			//-- convert the value/unit to the canonical form if any
			Pair canonicalForm = UcumServiceUtil.getCanonicalForm(theValue.getSystem(),
				BigDecimal.valueOf(theValue.getValue()), theValue.getCode());
			if (canonicalForm == null) { continue; }

			double canonicalValue = Double.parseDouble(canonicalForm.getValue().asDecimal());
			String canonicalUnits = canonicalForm.getCode();
			ourLog.trace("Adding search param normalized code and value: {} -- code:{}, value:{}",
				theSearchParam, canonicalUnits, canonicalValue);

			nestedQtyNode.addValue(QTY_CODE_NORM, canonicalUnits);
			nestedQtyNode.addValue(QTY_VALUE_NORM, canonicalValue);
		}

	}


	public void writeUriIndex(String theParamName, Collection<String> theUriValueCollection) {
		DocumentElement uriNode = myNodeCache.getObjectElement(SEARCH_PARAM_ROOT).addObject(theParamName);
		for (String uriSearchIndexValue : theUriValueCollection) {
			ourLog.trace("Adding Search Param Uri: {} -- {}", theParamName, uriSearchIndexValue);
			uriNode.addValue(URI_VALUE, uriSearchIndexValue);
		}
	}


	public void writeObservationComponentCompositeIndex(String theSearchParam, Collection<ObservationComponentSearchIndexData> theValueCollection) {
		DocumentElement nestedRoot = myNodeCache.getObjectElement(NESTED_SEARCH_PARAM_ROOT);

		for (ObservationComponentSearchIndexData theValue : theValueCollection) {
			DocumentElement nestedSpNode = nestedRoot.addObject(theSearchParam);
			DocumentElement nestedCompNode = nestedSpNode.addObject(COMPOS_PARAM_NAME);

			ourLog.trace("Adding Search Param CompositeTokenQuantity: {} -- {}", theSearchParam, theValue);
//			fixme jm: test multiple codes
			theValue.getCode().getCoding().forEach(code -> {
				DocumentElement nestedCompCodeNode = nestedCompNode.addObject("codes");
				nestedCompCodeNode.addValue(COMPOS_CODE_VALUE, code.getCode());
				nestedCompCodeNode.addValue(COMPOS_CODE_SYSTEM, code.getSystem());
			});

			if (theValue.getQuantity() != null) {
				nestedCompNode.addValue(COMPOS_QTY_VALUE, theValue.getQuantity().getValue().doubleValue());
				addValueIfNotNull(nestedCompNode, COMPOS_QTY_SYSTEM, theValue.getQuantity().getSystem());
				addValueIfNotNull(nestedCompNode, COMPOS_QTY_CODE, theValue.getQuantity().getCode());

//				fixme jm: duplicate
				if (myModelConfig.getNormalizedQuantitySearchLevel().storageOrSearchSupported()) {
					// convert the value/unit to the canonical form if any
					Pair canonicalForm = UcumServiceUtil.getCanonicalForm(theValue.getQuantity().getSystem(),
						BigDecimal.valueOf(theValue.getQuantity().getValue().doubleValue()), theValue.getQuantity().getCode());
					if (canonicalForm != null) {
						double canonicalValue = Double.parseDouble(canonicalForm.getValue().asDecimal());
						String canonicalUnits = canonicalForm.getCode();
						ourLog.trace("Adding Search Param CompositeTokenQuantity normalized code and value: {} -- code:{}, value:{}",
							theSearchParam, canonicalUnits, canonicalValue);

						nestedCompNode.addValue(QTY_CODE_NORM, canonicalUnits);
						nestedCompNode.addValue(QTY_VALUE_NORM, canonicalValue);
					}
				}
			}

			if (theValue.getConcept() != null && ! theValue.getConcept().isEmpty()) {
				addValueIfNotNull(nestedCompNode, COMPOS_CONCEPT_CODE, theValue.getConcept().getCoding());
				addValueIfNotNull(nestedCompNode, COMPOS_CONCEPT_TEXT, theValue.getConcept().getText());
			}
		}
	}

	private void addValueIfNotNull(DocumentElement theNode, String theFieldName, Object theValue) {
		if (theValue != null) {
			theNode.addValue(theFieldName, theValue);
		}

	}
	public void writeNumberIndex(String theParamName, Collection<BigDecimal> theNumberValueCollection) {
		DocumentElement numberNode = myNodeCache.getObjectElement(SEARCH_PARAM_ROOT).addObject(theParamName);
		for (BigDecimal numberSearchIndexValue : theNumberValueCollection) {
			ourLog.trace("Adding Search Param Number: {} -- {}", theParamName, numberSearchIndexValue);
			numberNode.addValue(NUMBER_VALUE, numberSearchIndexValue.doubleValue());
		}
	}

}
