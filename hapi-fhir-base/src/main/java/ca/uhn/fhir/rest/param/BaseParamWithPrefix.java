package ca.uhn.fhir.rest.param;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParamWithPrefix.class);
	
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
			if (theString.length() == offset) {
				break;
			} else {
				char nextChar = theString.charAt(offset);
				if (nextChar == '-' || nextChar == '%' || Character.isDigit(nextChar)) {
					break;
				}
			}
			offset++;
		}

		String prefix = theString.substring(0, offset);
		if (!isBlank(prefix)) {
		
			myPrefix = ParamPrefixEnum.forValue(prefix);
	
			if (myPrefix == null) {
				switch (prefix) {
				case ">=":
					myPrefix = ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
					break;
				case ">":
					myPrefix = ParamPrefixEnum.GREATERTHAN;
					break;
				case "<=":
					myPrefix = ParamPrefixEnum.LESSTHAN_OR_EQUALS;
					break;
				case "<":
					myPrefix = ParamPrefixEnum.LESSTHAN;
					break;
				case "~":
					myPrefix = ParamPrefixEnum.APPROXIMATE;
					break;
				default :
					ourLog.warn("Invalid prefix being ignored: {}", prefix);
					break;
				}
				
				if (myPrefix != null) {
					ourLog.warn("Date parameter has legacy prefix '{}' which has been removed from FHIR. This should be replaced with '{}'", prefix, myPrefix);
				}
				
			}
			
		}
		
		return theString.substring(offset);
	}

	/**
	 * Returns the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	public ParamPrefixEnum getPrefix() {
		return myPrefix;
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
