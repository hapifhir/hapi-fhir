package ca.uhn.fhir.rest.gclient;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collection;

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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseCoding;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.rest.param.ParameterUtil;

class TokenCriterion implements ICriterion<TokenClientParam>, ICriterionInternal {

	private String myValue;
	private String myName;

	public TokenCriterion(String theName, String theSystem, String theCode) {
		myName = theName;
		myValue=toValue(theSystem, theCode);
	}

	private String toValue(String theSystem, String theCode) {
		String system = ParameterUtil.escape(theSystem);
		String code = ParameterUtil.escape(theCode);
		String value;
		if (StringUtils.isNotBlank(system)) {
			value = system + "|" + StringUtils.defaultString(code);
		} else if (system == null) {
			value = StringUtils.defaultString(code);
		} else {
			value = "|" + StringUtils.defaultString(code);
		}
		return value;
	}

	public TokenCriterion(String theParamName, List<BaseIdentifierDt> theValue) {
		myName=theParamName;
		StringBuilder b = new StringBuilder();
		for (BaseIdentifierDt next : theValue) {
			if (next.getSystemElement().isEmpty() && next.getValueElement().isEmpty()) {
				continue;
			}
			if (b.length() > 0) {
				b.append(',');
			}
			b.append(toValue(next.getSystemElement().getValueAsString(), next.getValueElement().getValue()));
		}
		myValue = b.toString();
	}

	public TokenCriterion(String theParamName, String theSystem, Collection<String> theValues) {
		myName = theParamName;
		StringBuilder b = new StringBuilder();
		for (String next : theValues) {
			if (isNotBlank(next)) {
				if (b.length() > 0) {
					b.append(',');
				}
				b.append(toValue(theSystem, next));
			}
		}
		myValue = b.toString();
	}

	public TokenCriterion(String theParamName, Collection<String> theCodes) {
		this(theParamName, null, theCodes);
	}

	public TokenCriterion(String theParamName, IBaseCoding... theCodings) {
		myName=theParamName;
		StringBuilder b = new StringBuilder();
		if (theCodings != null) {
		for (IBaseCoding next : theCodings) {
			if (isBlank(next.getSystem()) && isBlank(next.getCode())) {
				continue;
			}
			if (b.length() > 0) {
				b.append(',');
			}
			b.append(toValue(next.getSystem(), next.getCode()));
		}
		}
		myValue = b.toString();
	}

	@Override
	public String getParameterValue(FhirContext theContext) {
		return myValue;
	}

	@Override
	public String getParameterName() {
		return myName;
	}

}
