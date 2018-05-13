package ca.uhn.fhir.i18n;

import ca.uhn.fhir.context.ConfigurationException;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

/**
 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
 */
public class HapiLocalizer {

	@SuppressWarnings("WeakerAccess")
	public static final String UNKNOWN_I18N_KEY_MESSAGE = "!MESSAGE!";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiLocalizer.class);
	private static boolean ourFailOnMissingMessage;
	private final Map<String, MessageFormat> myKeyToMessageFormat = new ConcurrentHashMap<>();
	private List<ResourceBundle> myBundle = new ArrayList<>();
	private String[] myBundleNames;

	public HapiLocalizer() {
		this(HapiLocalizer.class.getPackage().getName() + ".hapi-messages");
	}

	public HapiLocalizer(String... theBundleNames) {
		myBundleNames = theBundleNames;
		init();
	}

	public Set<String> getAllKeys() {
		HashSet<String> retVal = new HashSet<>();
		for (ResourceBundle nextBundle : myBundle) {
			Enumeration<String> keysEnum = nextBundle.getKeys();
			while (keysEnum.hasMoreElements()) {
				retVal.add(keysEnum.nextElement());
			}
		}
		return retVal;
	}

	/**
	 * @return Returns the raw message format string for the given key, or returns {@link #UNKNOWN_I18N_KEY_MESSAGE} if not found
	 */
	@SuppressWarnings("WeakerAccess")
	public String getFormatString(String theQualifiedKey) {
		String formatString = null;
		for (ResourceBundle nextBundle : myBundle) {
			if (nextBundle.containsKey(theQualifiedKey)) {
				formatString = nextBundle.getString(theQualifiedKey);
				formatString = trim(formatString);
			}
			if (isNotBlank(formatString)) {
				break;
			}
		}

		if (formatString == null) {
			ourLog.warn("Unknown localization key: {}", theQualifiedKey);
			if (ourFailOnMissingMessage) {
				throw new ConfigurationException("Unknown localization key: " + theQualifiedKey);
			}
			formatString = UNKNOWN_I18N_KEY_MESSAGE;
		}
		return formatString;
	}

	public String getMessage(Class<?> theType, String theKey, Object... theParameters) {
		return getMessage(toKey(theType, theKey), theParameters);
	}

	public String getMessage(String theQualifiedKey, Object... theParameters) {
		if (theParameters != null && theParameters.length > 0) {
			MessageFormat format = myKeyToMessageFormat.get(theQualifiedKey);
			if (format != null) {
				return format.format(theParameters);
			}

			String formatString = getFormatString(theQualifiedKey);

			format = new MessageFormat(formatString.trim());
			myKeyToMessageFormat.put(theQualifiedKey, format);
			return format.format(theParameters);
		}
		return getFormatString(theQualifiedKey);
	}

	protected void init() {
		for (String nextName : myBundleNames) {
			myBundle.add(ResourceBundle.getBundle(nextName));
		}
	}

	/**
	 * This <b>global setting</b> causes the localizer to fail if any attempts
	 * are made to retrieve a key that does not exist. This method is primarily for
	 * unit tests.
	 */
	public static void setOurFailOnMissingMessage(boolean ourFailOnMissingMessage) {
		HapiLocalizer.ourFailOnMissingMessage = ourFailOnMissingMessage;
	}

	public static String toKey(Class<?> theType, String theKey) {
		return theType.getName() + '.' + theKey;
	}

}
