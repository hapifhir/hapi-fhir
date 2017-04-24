package ca.uhn.fhir.rest.param;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.util.CoverageIgnore;

@SuppressWarnings("deprecation")
public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private ParamPrefixEnum myPrefix;

	/**
	 * Constructor
	 */
	// Default since this is internal
	BaseParamWithPrefix() {
		super();
	}

	/**
	 * Eg. if this is invoked with "gt2012-11-02", sets the prefix to GREATER_THAN and returns "2012-11-02"
	 */
	String extractPrefixAndReturnRest(String theString) {
		int offset = 0;
		while (true) {
			if (theString.length() == offset || Character.isDigit(theString.charAt(offset))) {
				break;
			}
			offset++;
		}

		String prefix = theString.substring(0, offset);
		myPrefix = ParamPrefixEnum.forValue(prefix);
		if (myPrefix == null) {
			myPrefix = ParamPrefixEnum.forDstu1Value(prefix);
		}

		return theString.substring(offset);
	}

	/**
	 * @deprecated Use {@link #getPrefix() instead}
	 */
	@Deprecated
	public QuantityCompararatorEnum getComparator() {
		ParamPrefixEnum prefix = getPrefix();
		if (prefix == null) {
			return null;
		}
		
		return QuantityCompararatorEnum.forCode(prefix.getDstu1Value());
	}

	/**
	 * Returns the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	public ParamPrefixEnum getPrefix() {
		return myPrefix;
	}

	/**
	 * @deprecated Use {@link #setPrefix(ParamPrefixEnum)} instead
	 */
	@SuppressWarnings("unchecked")
	@CoverageIgnore
	@Deprecated
	public T setComparator(QuantityCompararatorEnum theComparator) {
		if (theComparator != null) {
			myPrefix = ParamPrefixEnum.forDstu1Value(theComparator.getCode());
		} else {
			myPrefix = null;
		}
		return (T) this;
	}

	/**
	 * @deprecated Use {@link #setPrefix(ParamPrefixEnum)} instead
	 */
	@SuppressWarnings("unchecked")
	@CoverageIgnore
	@Deprecated
	public T setComparator(String theComparator) {
		if (isNotBlank(theComparator)) {
			myPrefix = ParamPrefixEnum.forDstu1Value(theComparator);
		} else {
			myPrefix = null;
		}
		return (T) this;
	}

	/**
	 * Sets the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	@SuppressWarnings("unchecked")
	public T setPrefix(ParamPrefixEnum thePrefix) {
		myPrefix = thePrefix;
		return (T) this;
	}

}
