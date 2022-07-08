package ca.uhn.fhir.rest.param;

/*-
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.DataFormatException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParamWithPrefix.class);


	public static final String MSG_PREFIX_INVALID_FORMAT = "Invalid date/time/quantity format: ";

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

		if (offset > 0 && theString.length() == offset) {
			throw new DataFormatException(Msg.code(1940) + MSG_PREFIX_INVALID_FORMAT + "\"" + theString + "\"");
		}

		String prefix = theString.substring(0, offset);
		if (!isBlank(prefix)) {
		
			myPrefix = ParamPrefixEnum.forValue(prefix);

			if (myPrefix == null) {
				// prefix doesn't match standard values.  Try legacy values
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
				case "=":
					myPrefix = ParamPrefixEnum.EQUAL;
					break;
				default :
					throw new DataFormatException(Msg.code(1941) + "Invalid prefix: \"" + prefix + "\"");
				}
				ourLog.warn("Date parameter has legacy prefix '{}' which has been removed from FHIR. This should be replaced with '{}'", prefix, myPrefix.getValue());
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
