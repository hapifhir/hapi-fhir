/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.i18n;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.VersionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * This feature is not yet in its final state and should be considered an internal part of HAPI for now - use with caution
 */
public class HapiLocalizer {

	@SuppressWarnings("WeakerAccess")
	public static final String UNKNOWN_I18N_KEY_MESSAGE = "!MESSAGE!";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HapiLocalizer.class);
	private static boolean ourFailOnMissingMessage;
	private final Map<String, MessageFormat> myKeyToMessageFormat = new ConcurrentHashMap<>();
	private List<ResourceBundle> myBundle;
	private final Map<String, String> myHardcodedMessages = new HashMap<>();
	private Locale myLocale = Locale.getDefault();

	public HapiLocalizer() {
		this(HapiLocalizer.class.getPackage().getName() + ".hapi-messages");
	}

	public HapiLocalizer(String... theBundleNames) {
		init(theBundleNames);
		addMessage("hapi.version", VersionUtil.getVersion());
	}

	/**
	 * Subclasses may use this to add hardcoded messages
	 */
	@SuppressWarnings("WeakerAccess")
	protected void addMessage(String theKey, String theMessage) {
		myHardcodedMessages.put(theKey, theMessage);
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
		String formatString = myHardcodedMessages.get(theQualifiedKey);
		if (isBlank(formatString)) {
			for (ResourceBundle nextBundle : myBundle) {
				if (nextBundle.containsKey(theQualifiedKey)) {
					formatString = nextBundle.getString(theQualifiedKey);
					formatString = trim(formatString);
				}
				if (isNotBlank(formatString)) {
					break;
				}
			}
		}

		if (formatString == null) {
			ourLog.warn("Unknown localization key: {}", theQualifiedKey);
			if (ourFailOnMissingMessage) {
				throw new ConfigurationException(Msg.code(1908) + "Unknown localization key: " + theQualifiedKey);
			}
			formatString = UNKNOWN_I18N_KEY_MESSAGE;
		}
		return formatString;
	}

	public String getMessage(Class<?> theType, String theKey, Object... theParameters) {
		return getMessage(toKey(theType, theKey), theParameters);
	}

	/**
	 * Create the message and sanitize parameters using {@link UrlUtil#sanitizeUrlPart(CharSequence)}
	 */
	public String getMessageSanitized(Class<?> theType, String theKey, Object... theParameters) {
		if (theParameters != null) {
			for (int i = 0; i < theParameters.length; i++) {
				if (theParameters[i] instanceof CharSequence) {
					theParameters[i] = UrlUtil.sanitizeUrlPart((CharSequence) theParameters[i]);
				}
			}
		}
		return getMessage(toKey(theType, theKey), theParameters);
	}

	public String getMessage(String theQualifiedKey, Object... theParameters) {
		if (theParameters != null && theParameters.length > 0) {
			MessageFormat format = myKeyToMessageFormat.get(theQualifiedKey);
			if (format != null) {
				return format.format(theParameters);
			}

			String formatString = getFormatString(theQualifiedKey);

			format = newMessageFormat(formatString);
			myKeyToMessageFormat.put(theQualifiedKey, format);
			return format.format(theParameters);
		}
		return getFormatString(theQualifiedKey);
	}

	MessageFormat newMessageFormat(String theFormatString) {
		StringBuilder pattern = new StringBuilder(theFormatString.trim());

		for (int i = 0; i < (pattern.length() - 1); i++) {
			if (pattern.charAt(i) == '{') {
				char nextChar = pattern.charAt(i + 1);
				if (nextChar >= '0' && nextChar <= '9') {
					continue;
				}

				pattern.replace(i, i + 1, "'{'");
				int closeBraceIndex = pattern.indexOf("}", i);
				if (closeBraceIndex > 0) {
					i = closeBraceIndex;
					pattern.replace(i, i + 1, "'}'");
				}
			}
		}

		return new MessageFormat(pattern.toString());
	}

	protected void init(String[] theBundleNames) {
		myBundle = new ArrayList<>();
		for (String nextName : theBundleNames) {
			// LUKETODO:  this fails because we eventually get: Unknown localization key: moduletype.ADMIN_JSON.shortName
//			final ResourceBundle bundleOldStyle = ResourceBundle.getBundle(nextName);
//			myBundle.add(bundleOldStyle);
//			ourLog.info("1234: Loading bundle: {}", nextName);
			final ResourceBundle bundleNewStyle = ResourceBundle.getBundle(nextName, new MultiFileResourceBundleControl());
//			if (! bundleOldStyle.keySet().equals(bundleNewStyle.keySet())) {
//				ourLog.warn("1234: bundle keys do not match for class: {} {}: old style keys: class: {} {}", bundleOldStyle.getClass(), bundleOldStyle.getKeys(), bundleNewStyle.getClass(), bundleNewStyle.getKeys());
//			}
//			ourLog.info("1234: bundle keySet: {} contains dqm: {} or cql: {}", bundleNewStyle.keySet(), bundleNewStyle.containsKey("module_config.evaluate_measure.chunksize"), bundleNewStyle.containsKey("module_config.cql.use_embedded_libraries"));
			myBundle.add(bundleNewStyle);
		}
	}

	/**
	 * Find all properties files on the class path that match the given base name and locale, and merge them into a single
	 * properties object.
	 */
//	public static class MultiFileResourceBundleControl extends ResourceBundle.Control {
	public static class MultiFileResourceBundleControl extends ResourceBundle.Control {
		/**
		 * Returns a list containing only Locale.ROOT as a candidate.
		 * This forces the loader to look for the base bundle only (e.g., "bundle.properties").
		 * @param baseName the base name of the resource bundle
		 * @param locale   the locale to load (this parameter is ignored)
		 * @return a singleton list containing Locale.ROOT
		 */
		@Override
		public List<Locale> getCandidateLocales(String baseName, Locale locale) {
			return Collections.singletonList(Locale.ROOT);
		}

		/**
		 * Prevents fallback to the default locale if the root bundle isn't found.
		 * @param baseName the base name of the resource bundle
		 * @param locale   the locale where the search is failing
		 * @return null to indicate no fallback should be attempted
		 */
		@Override
		public Locale getFallbackLocale(String baseName, Locale locale) {
			return null;
		}

		@Override
		public ResourceBundle newBundle(
				String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
				throws IOException, IllegalAccessException, InstantiationException {
//			ourLog.info("1234: locale: {}", locale);
			if ("java.properties".equals(format) && (locale == null || locale.toString().isEmpty())) {
				String bundleName = toBundleName(baseName, locale);
				String resourceName = toResourceName(bundleName, "properties");

				Properties properties = new Properties();

				var urls = new ArrayList<>();
				// Load from all matching resources
				Enumeration<URL> resources = loader.getResources(resourceName);
//				final ArrayList<URL> list = Collections.list(resources);
//				ourLog.info("1234: before loop");
				while (resources.hasMoreElements()) {
					URL url = resources.nextElement();
					urls.add(url);
					try (InputStream is = url.openStream()) {
						properties.load(is);
					}
//					ourLog.info("1234: urL: {}, properties contains dqm: {} or cql: {}", url, properties.get("module_config.evaluate_measure.chunksize"), properties.get("module_config.cql.use_embedded_libraries"));
				}
//				ourLog.info("1234: after loop");

//				ourLog.info("1234:resourceName:{}, urls: {} properties.toString(): {}", resourceName, urls, properties.toString());

//				ourLog.info("1234: bundleName: {}, resourceName: {}, properties size: {} properties contains dqm: {} or cql: {}", bundleName, resourceName,
//				properties.entrySet().size(), properties.get("module_config.evaluate_measure.chunksize"), properties.get("module_config.cql.use_embedded_libraries"));


				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				try (Writer writer = new OutputStreamWriter(outStream, StandardCharsets.UTF_8)) {
					properties.store(writer, "module properties");
				}

				InputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
				final PropertyResourceBundle bundle = new PropertyResourceBundle(inStream);



//				final PropertyResourceBundle bundle = new PropertyResourceBundle(properties.store()));
//				ourLog.info("1234: bundle keySet: {} contains dqm: {} or cql: {}", bundle.keySet(), bundle.containsKey("module_config.evaluate_measure.chunksize"), bundle.containsKey("module_config.cql.use_embedded_libraries"));
				return bundle;
			}
			return super.newBundle(baseName, locale, format, loader, reload);
		}
	}

	public Locale getLocale() {
		return myLocale;
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

	@VisibleForTesting
	List<ResourceBundle> getBundles() {
		return myBundle;
	}
}
