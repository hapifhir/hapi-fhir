package ca.uhn.fhir.i18n;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
 */
public class HapiLocalizer {

	public static final String UNKNOWN_I18N_KEY_MESSAGE = "!MESSAGE!";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiLocalizer.class);
	private List<ResourceBundle> myBundle = new ArrayList<ResourceBundle>();

	private final Map<String, MessageFormat> myKeyToMessageFormat = new ConcurrentHashMap<String, MessageFormat>();
	private String[] myBundleNames;

	public HapiLocalizer() {
		this(HapiLocalizer.class.getPackage().getName() + ".hapi-messages");
	}

	public HapiLocalizer(String... theBundleNames) {
		myBundleNames = theBundleNames;
		init();
	}

	protected void init() {
		for (String nextName : myBundleNames) {
			myBundle.add(ResourceBundle.getBundle(nextName));
		}
	}

	private String findFormatString(String theQualifiedKey) {
		String formatString = null;
		for (ResourceBundle nextBundle : myBundle) {
			if (nextBundle.containsKey(theQualifiedKey)) {
				formatString = nextBundle.getString(theQualifiedKey);
			}
			if (isNotBlank(formatString)) {
				break;
			}
		}

		if (formatString == null) {
			ourLog.warn("Unknown localization key: {}", theQualifiedKey);
			formatString = UNKNOWN_I18N_KEY_MESSAGE;
		}
		return formatString;
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

			String formatString = findFormatString(theQualifiedKey);

			format = new MessageFormat(formatString.trim());
			myKeyToMessageFormat.put(theQualifiedKey, format);
			return format.format(theParameters).toString();
		}
		String retVal = findFormatString(theQualifiedKey);
		return retVal;
	}
	
	public Set<String> getAllKeys(){
		HashSet<String> retVal = new HashSet<String>();
		for (ResourceBundle nextBundle : myBundle) {
			Enumeration<String> keysEnum = nextBundle.getKeys();
			while (keysEnum.hasMoreElements()) {
				retVal.add(keysEnum.nextElement());
			}
		}
		return retVal;
	}
	
}
