package ca.uhn.fhir.narrative;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isBlank;

abstract public class BaseNarrativeGenerator implements INarrativeGenerator {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseNarrativeGenerator.class);

	private boolean myIgnoreFailures = true;
	private boolean myApplyDefaultDatatypeTemplates = true;
	protected HashMap<Class<?>, String> myClassToName;
	private boolean myCleanWhitespace = true;
	private boolean myIgnoreMissingTemplates = true;
	private volatile boolean myInitialized;
	protected HashMap<String, String> myNameToNarrativeTemplate;

//	@Override
	// FIXME KHS is this still used anywhere?
	public void generateNarrative(FhirContext theFhirContext, IBaseResource theResource, INarrative theNarrative) {
		String name = getName(theFhirContext, theResource);
		if (name == null) return;

		try {
			String result = processNamedTemplate(theFhirContext, name, theResource);

			if (isCleanWhitespace()) {
				ourLog.trace("Pre-whitespace cleaning: ", result);
				result = cleanWhitespace(result);
				ourLog.trace("Post-whitespace cleaning: ", result);
			}

			if (isBlank(result)) {
				return;
			}

			theNarrative.setDivAsString(result);
			theNarrative.setStatusAsString("generated");
			return;
		} catch (Exception e) {
			if (isIgnoreFailures()) {
				ourLog.error("Failed to generate narrative", e);
				try {
					theNarrative.setDivAsString("<div>No narrative available - Error: " + e.getMessage() + "</div>");
				} catch (Exception e1) {
					// last resort..
				}
				theNarrative.setStatusAsString("empty");
				return;
			}
			throw new DataFormatException(e);
		}
	}

	protected abstract String processNamedTemplate(FhirContext theFhirContext, String theName, IBaseResource theResource) throws Exception;

	protected String getName(FhirContext theContext, IBaseResource theResource) {
		if (!myInitialized) {
			initialize(theContext);
		}

		String name = myClassToName.get(theResource.getClass());
		if (name == null) {
			name = theContext.getResourceDefinition(theResource).getName().toLowerCase();
		}

		if (name == null || !myNameToNarrativeTemplate.containsKey(name)) {
			if (isIgnoreMissingTemplates()) {
				ourLog.debug("No narrative template available for resorce: {}", name);
				return null;
			}
			throw new DataFormatException("No narrative template for class " + theResource.getClass().getCanonicalName());
		}
		return name;
	}

	protected abstract List<String> getPropertyFile();

	private synchronized void initialize(final FhirContext theFhirContext) {
		if (myInitialized) {
			return;
		}

		ourLog.info("Initializing narrative generator");

		myClassToName = new HashMap<>();
		myNameToNarrativeTemplate = new HashMap<>();


		List<String> propFileName = getPropertyFile();

		try {
			if (myApplyDefaultDatatypeTemplates) {
				loadProperties(DefaultThymeleafNarrativeGenerator.NARRATIVES_PROPERTIES);
			}
			for (String next : propFileName) {
				loadProperties(next);
			}
		} catch (IOException e) {
			ourLog.info("Failed to load property file {}: {}", propFileName, e.getMessage());
			throw new ConfigurationException("Can not load property file " + propFileName, e);
		}

		initializeNarrativeEngine(theFhirContext);

		myInitialized = true;
	}

	protected abstract void initializeNarrativeEngine(FhirContext theFhirContext);

	protected String getNarrativeTemplate(String name) {
		return myNameToNarrativeTemplate.get(name);
	}

	private void loadProperties(String propFileName) throws IOException {
		ourLog.debug("Loading narrative properties file: {}", propFileName);

		Properties file = new Properties();

		InputStream resource = loadResource(propFileName);
		file.load(resource);
		for (Object nextKeyObj : file.keySet()) {
			String nextKey = (String) nextKeyObj;
			if (nextKey.endsWith(".profile")) {
				String name = nextKey.substring(0, nextKey.indexOf(".profile"));
				if (isBlank(name)) {
					continue;
				}

				String narrativePropName = name + ".narrative";
				String narrativeName = file.getProperty(narrativePropName);
				if (isBlank(narrativeName)) {
					//FIXME resource leak
					throw new ConfigurationException("Found property '" + nextKey + "' but no corresponding property '" + narrativePropName + "' in file " + propFileName);
				}

				if (StringUtils.isNotBlank(narrativeName)) {
					String narrative = IOUtils.toString(loadResource(narrativeName), Constants.CHARSET_UTF8);
					myNameToNarrativeTemplate.put(name, narrative);
				}

			} else if (nextKey.endsWith(".class")) {

				String name = nextKey.substring(0, nextKey.indexOf(".class"));
				if (isBlank(name)) {
					continue;
				}

				String className = file.getProperty(nextKey);

				Class<?> clazz;
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					ourLog.debug("Unknown datatype class '{}' identified in narrative file {}", name, propFileName);
					clazz = null;
				}

				if (clazz != null) {
					myClassToName.put(clazz, name);
				}

			} else if (nextKey.endsWith(".narrative")) {
				String name = nextKey.substring(0, nextKey.indexOf(".narrative"));
				if (isBlank(name)) {
					continue;
				}
				String narrativePropName = name + ".narrative";
				String narrativeName = file.getProperty(narrativePropName);
				if (StringUtils.isNotBlank(narrativeName)) {
					String narrative = IOUtils.toString(loadResource(narrativeName), Constants.CHARSET_UTF8);
					myNameToNarrativeTemplate.put(name, narrative);
				}
				continue;
			} else if (nextKey.endsWith(".title")) {
				ourLog.debug("Ignoring title property as narrative generator no longer generates titles: {}", nextKey);
			} else {
				throw new ConfigurationException("Invalid property name: " + nextKey);
			}

		}
	}

	protected static String cleanWhitespace(String theResult) {
		StringBuilder b = new StringBuilder();
		boolean inWhitespace = false;
		boolean betweenTags = false;
		boolean lastNonWhitespaceCharWasTagEnd = false;
		boolean inPre = false;
		for (int i = 0; i < theResult.length(); i++) {
			char nextChar = theResult.charAt(i);
			if (inPre) {
				b.append(nextChar);
				continue;
			} else if (nextChar == '>') {
				b.append(nextChar);
				betweenTags = true;
				lastNonWhitespaceCharWasTagEnd = true;
				continue;
			} else if (nextChar == '\n' || nextChar == '\r') {
				continue;
			}

			if (betweenTags) {
				if (Character.isWhitespace(nextChar)) {
					inWhitespace = true;
				} else if (nextChar == '<') {
					if (inWhitespace && !lastNonWhitespaceCharWasTagEnd) {
						b.append(' ');
					}
					inWhitespace = false;
					b.append(nextChar);
					inWhitespace = false;
					betweenTags = false;
					lastNonWhitespaceCharWasTagEnd = false;
					if (i + 3 < theResult.length()) {
						char char1 = Character.toLowerCase(theResult.charAt(i + 1));
						char char2 = Character.toLowerCase(theResult.charAt(i + 2));
						char char3 = Character.toLowerCase(theResult.charAt(i + 3));
						char char4 = Character.toLowerCase((i + 4 < theResult.length()) ? theResult.charAt(i + 4) : ' ');
						if (char1 == 'p' && char2 == 'r' && char3 == 'e') {
							inPre = true;
						} else if (char1 == '/' && char2 == 'p' && char3 == 'r' && char4 == 'e') {
							inPre = false;
						}
					}
				} else {
					lastNonWhitespaceCharWasTagEnd = false;
					if (inWhitespace) {
						b.append(' ');
						inWhitespace = false;
					}
					b.append(nextChar);
				}
			} else {
				b.append(nextChar);
			}
		}
		return b.toString();
	}

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will be trimmed from the generated narrative
	 * before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed. Repeated whitespace characters (e.g.
	 * "\n \n ") will be trimmed to a single space.
	 * </p>
	 */
	public boolean isCleanWhitespace() {
		return myCleanWhitespace;
	}

	/**
	 * If set to <code>true</code>, which is the default, if any failure occurs during narrative generation the
	 * generator will suppress any generated exceptions, and simply return a default narrative indicating that no
	 * narrative is available.
	 */
	public boolean isIgnoreFailures() {
		return myIgnoreFailures;
	}

	/**
	 * If set to true, will return an empty narrative block for any profiles where no template is available
	 */
	public boolean isIgnoreMissingTemplates() {
		return myIgnoreMissingTemplates;
	}

	protected InputStream loadResource(String name) throws IOException {
		if (name.startsWith("classpath:")) {
			String cpName = name.substring("classpath:".length());
			InputStream resource = BaseNarrativeGenerator.class.getResourceAsStream(cpName);
			if (resource == null) {
				resource = BaseNarrativeGenerator.class.getResourceAsStream("/" + cpName);
				if (resource == null) {
					throw new IOException("Can not find '" + cpName + "' on classpath");
				}
			}
			//FIXME resource leak
			return resource;
		} else if (name.startsWith("file:")) {
			File file = new File(name.substring("file:".length()));
			if (file.exists() == false) {
				throw new IOException("File not found: " + file.getAbsolutePath());
			}
			return new FileInputStream(file);
		} else {
			throw new IOException("Invalid resource name: '" + name + "' (must start with classpath: or file: )");
		}
	}

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will be trimmed from the generated narrative
	 * before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed. Repeated whitespace characters (e.g.
	 * "\n \n ") will be trimmed to a single space.
	 * </p>
	 */
	public void setCleanWhitespace(boolean theCleanWhitespace) {
		myCleanWhitespace = theCleanWhitespace;
	}

	/**
	 * If set to <code>true</code>, which is the default, if any failure occurs during narrative generation the
	 * generator will suppress any generated exceptions, and simply return a default narrative indicating that no
	 * narrative is available.
	 */
	public void setIgnoreFailures(boolean theIgnoreFailures) {
		myIgnoreFailures = theIgnoreFailures;
	}

	/**
	 * If set to true, will return an empty narrative block for any profiles where no template is available
	 */
	public void setIgnoreMissingTemplates(boolean theIgnoreMissingTemplates) {
		myIgnoreMissingTemplates = theIgnoreMissingTemplates;
	}
}
