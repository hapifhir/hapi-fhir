package ca.uhn.fhir.i18n;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
 */
public class HapiLocalizer {

	private ResourceBundle myBundle;
	private final Map<String, MessageFormat> myKeyToMessageFormat = new ConcurrentHashMap<String, MessageFormat>();

	public HapiLocalizer() {
		myBundle = ResourceBundle.getBundle(HapiLocalizer.class.getPackage().getName() + ".hapi-messages");
	}

	public String getMessage(Class<?> theType, String theKey, Object... theParameters) {
		return getMessage(theType.getName() + '.' + theKey, theParameters);
	}
	
	public String getMessage(String theQualifiedKey, Object... theParameters) {
		if (theParameters != null && theParameters.length > 0) {
			MessageFormat format = myKeyToMessageFormat.get(theQualifiedKey);
			if (format != null) {
				return format.format(theParameters).toString();
			}
			
			String formatString = myBundle.getString(theQualifiedKey);
			if (formatString== null) {
				formatString = "!MESSAGE!";
			}
			
			format = new MessageFormat(formatString);
			myKeyToMessageFormat.put(theQualifiedKey, format);
			return format.format(theParameters).toString();
		} else {
			String retVal = myBundle.getString(theQualifiedKey);
			if (retVal == null) {
				retVal = "!MESSAGE!";
			}
			return retVal;
		}
	}
	
	
}
