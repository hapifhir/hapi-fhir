package ca.uhn.fhir.narrative;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.thymeleaf.util.DOMUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseThymeleafNarrativeGenerator implements INarrativeGenerator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseThymeleafNarrativeGenerator.class);

	private boolean myCleanWhitespace = true;
	private boolean myIgnoreFailures = true;
	private boolean myIgnoreMissingTemplates = true;

	private TemplateEngine myProfileTemplateEngine;
	private HashMap<String, String> myProfileToNarrativeName;
	private HashMap<Class<?>, String> myClassToNarrativeName;
	private HashMap<String, String> myNameToNarrativeTemplate;
	private boolean myApplyDefaultDatatypeTemplates=true;
	private volatile boolean myInitialized;

	@Override
	public NarrativeDt generateNarrative(IResource theResource) {
		return generateNarrative(null, theResource);
	}

	@Override
	public NarrativeDt generateNarrative(String theProfile, IResource theResource) {
		if (!myInitialized) {
			initialize();
		}

		String name = null;
		if (StringUtils.isNotBlank(theProfile)) {
			name = myProfileToNarrativeName.get(theProfile);
		}
		if (name == null) {
			name = myClassToNarrativeName.get(theResource.getClass());
		}

		if (name == null) {
			if (myIgnoreMissingTemplates) {
				ourLog.debug("No narrative template available for profile: {}", theProfile);
				return new NarrativeDt(new XhtmlDt("<div>No narrative available</div>"), NarrativeStatusEnum.EMPTY);
			} else {
				throw new DataFormatException("No narrative template for class " + theResource.getClass().getCanonicalName());
			}
		}

		try {
			Context context = new Context();
			context.setVariable("resource", theResource);
			
			String result = myProfileTemplateEngine.process(name, context);

			if (myCleanWhitespace) {
				ourLog.trace("Pre-whitespace cleaning: ", result);
				result = cleanWhitespace(result);
				ourLog.trace("Post-whitespace cleaning: ", result);
			}

			XhtmlDt div = new XhtmlDt(result);
			return new NarrativeDt(div, NarrativeStatusEnum.GENERATED);
		} catch (Exception e) {
			if (myIgnoreFailures) {
				ourLog.error("Failed to generate narrative", e);
				return new NarrativeDt(new XhtmlDt("<div>No narrative available</div>"), NarrativeStatusEnum.EMPTY);
			} else {
				throw new DataFormatException(e);
			}
		}
	}

	private synchronized void initialize() {
		if (myInitialized) {
			return;
		}
		myProfileToNarrativeName = new HashMap<String, String>();
		myClassToNarrativeName = new HashMap<Class<?>, String>();
		myNameToNarrativeTemplate = new HashMap<String, String>();

		List<String> propFileName = getPropertyFile();

		try {
			if (myApplyDefaultDatatypeTemplates) {
				loadProperties(DefaultThymeleafNarrativeGenerator.NARRATIVES_PROPERTIES);
			}
			for (String next : propFileName) {
				loadProperties(next);
			}
		} catch (IOException e) {
			throw new ConfigurationException("Can not load property file " + propFileName, e);
		}

		{
			myProfileTemplateEngine = new TemplateEngine();
			TemplateResolver resolver = new TemplateResolver();
			resolver.setResourceResolver(new ProfileResourceResolver());
			myProfileTemplateEngine.setTemplateResolver(resolver);
			StandardDialect dialect = new StandardDialect();
			HashSet<IProcessor> additionalProcessors = new HashSet<IProcessor>();
			additionalProcessors.add(new NarrativeAttributeProcessor());
			dialect.setAdditionalProcessors(additionalProcessors);
			myProfileTemplateEngine.setDialect(dialect);
			myProfileTemplateEngine.initialize();
		}

		myInitialized = true;
	}

	protected abstract List<String> getPropertyFile();

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will
	 * be trimmed from the generated narrative before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed.
	 * Repeated whitespace characters (e.g. "\n       \n       ") will be
	 * trimmed to a single space.
	 * </p>
	 */
	public boolean isCleanWhitespace() {
		return myCleanWhitespace;
	}

	/**
	 * If set to <code>true</code>, which is the default, if any failure occurs
	 * during narrative generation the generator will suppress any generated
	 * exceptions, and simply return a default narrative indicating that no
	 * narrative is available.
	 */
	public boolean isIgnoreFailures() {
		return myIgnoreFailures;
	}

	/**
	 * If set to true, will return an empty narrative block for any profiles
	 * where no template is available
	 */
	public boolean isIgnoreMissingTemplates() {
		return myIgnoreMissingTemplates;
	}

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will
	 * be trimmed from the generated narrative before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed.
	 * Repeated whitespace characters (e.g. "\n       \n       ") will be
	 * trimmed to a single space.
	 * </p>
	 */
	public void setCleanWhitespace(boolean theCleanWhitespace) {
		myCleanWhitespace = theCleanWhitespace;
	}

	/**
	 * If set to <code>true</code>, which is the default, if any failure occurs
	 * during narrative generation the generator will suppress any generated
	 * exceptions, and simply return a default narrative indicating that no
	 * narrative is available.
	 */
	public void setIgnoreFailures(boolean theIgnoreFailures) {
		myIgnoreFailures = theIgnoreFailures;
	}

	/**
	 * If set to true, will return an empty narrative block for any profiles
	 * where no template is available
	 */
	public void setIgnoreMissingTemplates(boolean theIgnoreMissingTemplates) {
		myIgnoreMissingTemplates = theIgnoreMissingTemplates;
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
					throw new ConfigurationException("Found property '" + nextKey + "' but no corresponding property '" + narrativePropName + "' in file " + propFileName);
				}

				String narrative = IOUtils.toString(loadResource(narrativeName));
				myProfileToNarrativeName.put(file.getProperty(nextKey), name);
				myNameToNarrativeTemplate.put(name, narrative);

			} else if (nextKey.endsWith(".class")) {

				String name = nextKey.substring(0, nextKey.indexOf(".class"));
				if (isBlank(name)) {
					continue;
				}

				String className = file.getProperty(nextKey);

				Class<?> dtClass;
				try {
					dtClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					ourLog.warn("Unknown datatype class '{}' identified in narrative file {}", name, propFileName);
					continue;
				}

				String narrativePropName = name + ".narrative";
				String narrativeName = file.getProperty(narrativePropName);
				if (isBlank(narrativeName)) {
					throw new ConfigurationException("Found property '" + nextKey + "' but no corresponding property '" + narrativePropName + "' in file " + propFileName);
				}

				String narrative = IOUtils.toString(loadResource(narrativeName));
				myClassToNarrativeName.put(dtClass, name);
				myNameToNarrativeTemplate.put(name, narrative);

			} else if (nextKey.endsWith(".narrative")) {
				continue;
			} else {
				throw new ConfigurationException("Invalid property name: " + nextKey);
			}

		}
	}

	private InputStream loadResource(String name) throws IOException {
		if (name.startsWith("classpath:")) {
			String cpName = name.substring("classpath:".length());
			InputStream resource = DefaultThymeleafNarrativeGenerator.class.getResourceAsStream(cpName);
			if (resource == null) {
				resource = DefaultThymeleafNarrativeGenerator.class.getResourceAsStream("/" + cpName);
				if (resource == null) {
					throw new IOException("Can not find '" + cpName + "' on classpath");
				}
			}
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

	static String cleanWhitespace(String theResult) {
		StringBuilder b = new StringBuilder();
		boolean inWhitespace = false;
		boolean betweenTags = false;
		boolean lastNonWhitespaceCharWasTagEnd = false;
		for (int i = 0; i < theResult.length(); i++) {
			char nextChar = theResult.charAt(i);
			if (nextChar == '>') {
				b.append(nextChar);
				betweenTags = true;
				lastNonWhitespaceCharWasTagEnd = true;
				continue;
			} else if (nextChar == '\n' || nextChar == '\r') {
				// if (inWhitespace) {
				// b.append(' ');
				// inWhitespace = false;
				// }
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

	public class NarrativeAttributeProcessor extends AbstractAttrProcessor {

		protected NarrativeAttributeProcessor() {
			super("narrative");
		}

		@Override
		public int getPrecedence() {
			return 0;
		}

		@Override
		protected ProcessorResult processAttribute(Arguments theArguments, Element theElement, String theAttributeName) {
			final String attributeValue = theElement.getAttributeValue(theAttributeName);

			final Configuration configuration = theArguments.getConfiguration();
			final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

			final IStandardExpression expression = expressionParser.parseExpression(configuration, theArguments, attributeValue);
			final Object value = expression.execute(configuration, theArguments);


			theElement.removeAttribute(theAttributeName);
			theElement.clearChildren();

			Context context = new Context();
			context.setVariable("resource", value);
			
			String name = myClassToNarrativeName.get(value.getClass());
			if (name == null) {
				if (myIgnoreMissingTemplates) {
					ourLog.debug("No narrative template available for type: {}", value.getClass());
					return ProcessorResult.ok();
				} else {
					throw new DataFormatException("No narrative template for class " + value.getClass());
				}
			}

			String result = myProfileTemplateEngine.process(name, context);
			Document dom = DOMUtils.getXhtmlDOMFor(new StringReader(result));

			Element firstChild = (Element) dom.getFirstChild();
			for (Node next : firstChild.getChildren()) {
				theElement.addChild(next);
			}

			return ProcessorResult.ok();
		}

	}

	// public String generateString(Patient theValue) {
	//
	// Context context = new Context();
	// context.setVariable("resource", theValue);
	// String result =
	// myProfileTemplateEngine.process("ca/uhn/fhir/narrative/Patient.html",
	// context);
	//
	// ourLog.info("Result: {}", result);
	//
	// return result;
	// }

	private final class ProfileResourceResolver implements IResourceResolver {
		@Override
		public String getName() {
			return getClass().getCanonicalName();
		}

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters theTemplateProcessingParameters, String theName) {
			String template = myNameToNarrativeTemplate.get(theName);
			if (template == null) {
				ourLog.info("No narative template for resource profile: {}", theName);
				return new ReaderInputStream(new StringReader(""));
			}
			return new ReaderInputStream(new StringReader(template));
		}
	}
}
