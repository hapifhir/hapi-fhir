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

import static org.hl7.fhir.r4.model.Observation.SP_VALUE_QUANTITY;

public class HibernateSearchIndexWriter {
	private static final Logger ourLog = LoggerFactory.getLogger(HibernateSearchIndexWriter.class);
	public static final String IDX_STRING_NORMALIZED = "norm";
	public static final String IDX_STRING_EXACT = "exact";
	public static final String IDX_STRING_TEXT = "text";
	public static final String NESTED_SEARCH_PARAM_ROOT = "nsp";
	public static final String SEARCH_PARAM_ROOT = "sp";

	public static final String QTY_PARAM_NAME = "quantity";
	public static final String QTY_CODE = "code";
	public static final String QTY_SYSTEM = "system";
	public static final String QTY_VALUE = "value";
	public static final String QTY_CODE_NORM = "code-norm";
	public static final String QTY_VALUE_NORM = "value-norm";



	final HibernateSearchElementCache myNodeCache;
	final FhirContext myFhirContext;
	final ModelConfig myModelConfig;

	HibernateSearchIndexWriter(FhirContext theFhirContext, ModelConfig theModelConfig, DocumentElement theRoot) {
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
		myNodeCache = new HibernateSearchElementCache(theRoot);
	}

	public DocumentElement getSearchParamIndexNode(String theSearchParamName, String theIndexType) {
		return myNodeCache.getObjectElement(SEARCH_PARAM_ROOT, theSearchParamName, theIndexType);
	}

	public static HibernateSearchIndexWriter forRoot(
			FhirContext theFhirContext, ModelConfig theModelConfig, DocumentElement theDocument) {
		return new HibernateSearchIndexWriter(theFhirContext, theModelConfig, theDocument);
	}

	public void writeStringIndex(String theSearchParam, String theValue) {
		DocumentElement stringIndexNode = getSearchParamIndexNode(theSearchParam, "string");

		// we are assuming that our analyzer matches  StringUtil.normalizeStringForSearchIndexing(theValue).toLowerCase(Locale.ROOT))
		stringIndexNode.addValue(IDX_STRING_NORMALIZED, theValue);// for default search
		stringIndexNode.addValue(IDX_STRING_EXACT, theValue);
		stringIndexNode.addValue(IDX_STRING_TEXT, theValue);
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
		ourLog.trace("Adding Search Param Reference: {} -- {}", theSearchParam, theValue);
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



}
