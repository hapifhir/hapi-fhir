package ca.uhn.fhir.i18n;

import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Finds all properties files on the class path that match the given base name and merges them.
 * This implementation avoids the unnecessary overhead of writing to and reading from a stream.
 */
public class MultiFileResourceBundleControl extends ResourceBundle.Control {

	/**
	 * Returns a list containing only Locale.ROOT as a candidate.
	 * This forces the loader to look for the base bundle only (e.g., "bundle.properties").
	 *
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
	 *
	 * @param baseName the base name of the resource bundle
	 * @param locale   the locale where the search is failing
	 * @return null to indicate no fallback should be attempted
	 */
	@Override
	public Locale getFallbackLocale(String baseName, Locale locale) {
		return null;
	}

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
			throws IOException, IllegalAccessException, InstantiationException {

		// We only care about handling the root .properties file
		if (!"java.properties".equals(format)
				|| (locale != null && !locale.toString().isEmpty())) {
			return super.newBundle(baseName, locale, format, loader, reload);
		}

		final String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
		final Properties mergedProperties = new Properties();

		// Load from all matching resources
		final Enumeration<URL> resources = loader.getResources(resourceName);
		while (resources.hasMoreElements()) {
			final URL url = resources.nextElement();

			try (InputStream is = url.openStream()) {
				mergedProperties.load(is);
			}
		}

		// Directly wrap the merged Properties object instead of performing a stream round-trip.
		return new PropertiesResourceBundle(mergedProperties);
	}

	/**
	 * A lightweight, private ResourceBundle implementation that is backed directly by a Properties object.
	 */
	private static class PropertiesResourceBundle extends ResourceBundle {
		private final Properties properties;

		PropertiesResourceBundle(Properties properties) {
			this.properties = properties;
		}

		@Override
		protected Object handleGetObject(@Nonnull String key) {
			return properties.getProperty(key);
		}

		@Override
		@Nonnull
		public Enumeration<String> getKeys() {
			return Collections.enumeration(properties.stringPropertyNames());
		}
	}
}
