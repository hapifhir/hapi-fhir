package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isBlank;

/*
 * #%L
 * HAPI FHIR - Core Library
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

public class QualifiedParamList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;

	private String myQualifier;

	public QualifiedParamList() {
		super();
	}

	public QualifiedParamList(int theCapacity) {
		super(theCapacity);
	}

	public QualifiedParamList(IQueryParameterOr<?> theNextOr, FhirContext theContext) {
		for (IQueryParameterType next : theNextOr.getValuesAsQueryTokens()) {
			if (myQualifier == null) {
				myQualifier = next.getQueryParameterQualifier();
			}
			add(next.getValueAsQueryToken(theContext));
		}
	}

	public String getQualifier() {
		return myQualifier;
	}

	public void setQualifier(String theQualifier) {
		myQualifier = theQualifier;
	}

	public static QualifiedParamList singleton(String theParamValue) {
		return singleton(null, theParamValue);
	}

	public static QualifiedParamList singleton(String theQualifier, String theParamValue) {
		QualifiedParamList retVal = new QualifiedParamList(1);
		retVal.setQualifier(theQualifier);
		retVal.add(theParamValue);
		return retVal;
	}

	public static QualifiedParamList splitQueryStringByCommasIgnoreEscape(String theQualifier, String theParams) {
		QualifiedParamList retVal = new QualifiedParamList();
		retVal.setQualifier(theQualifier);

		StringTokenizer tok = new StringTokenizer(theParams, ",", true);
		String prev = null;
		while (tok.hasMoreElements()) {
			String str = tok.nextToken();
			if (isBlank(str)) {
				prev = null;
				continue;
			}

			if (str.equals(",")) {
				if (countTrailingSlashes(prev) % 2 == 1) {
					int idx = retVal.size() - 1;
					String existing = retVal.get(idx);
					prev = existing.substring(0, existing.length() - 1) + ',';
					retVal.set(idx, prev);
				} else {
					prev = null;
				}
				continue;
			}

			if (prev != null && prev.length() > 0 && prev.charAt(prev.length() - 1) == ',') {
				int idx = retVal.size() - 1;
				String existing = retVal.get(idx);
				prev = existing + str;
				retVal.set(idx, prev);
			} else {
				retVal.add(str);
				prev = str;
			}

		}

		// If no value was found, at least add that empty string as a value. It should get ignored later, but at
		// least this lets us give a sensible error message if the parameter name was bad. See
		// ResourceProviderR4Test#testParameterWithNoValueThrowsError_InvalidChainOnCustomSearch for an example
		if (retVal.size() == 0) {
			retVal.add("");
		}

		return retVal;
	}

	private static int countTrailingSlashes(String theString) {
		if (theString == null) {
			return 0;
		}
		int retVal = 0;
		for (int i = theString.length() - 1; i >= 0; i--) {
			char nextChar = theString.charAt(i);
			if (nextChar != '\\') {
				break;
			}
			retVal++;
		}
		return retVal;
	}

}
