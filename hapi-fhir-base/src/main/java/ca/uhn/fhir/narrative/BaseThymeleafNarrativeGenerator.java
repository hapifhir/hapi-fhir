package ca.uhn.fhir.narrative;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.Context;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.attr.AbstractAttrProcessor;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateparser.xmlsax.XhtmlAndHtml5NonValidatingSAXTemplateParser;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseThymeleafNarrativeGenerator implements INarrativeGenerator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseThymeleafNarrativeGenerator.class);
	private static final XhtmlAndHtml5NonValidatingSAXTemplateParser PARSER = new XhtmlAndHtml5NonValidatingSAXTemplateParser(1);

	private Configuration myThymeleafConfig;
	private boolean myApplyDefaultDatatypeTemplates = true;
	private HashMap<Class<?>, String> myClassToName;
	private boolean myCleanWhitespace = true;
	private boolean myIgnoreFailures = true;
	private boolean myIgnoreMissingTemplates = true;
	private volatile boolean myInitialized;
	private HashMap<String, String> myNameToNarrativeTemplate;
	private HashMap<String, String> myNameToTitleTemplate;
	private TemplateEngine myProfileTemplateEngine;
	private HashMap<String, String> myProfileToName;
	private TemplateEngine myTitleTemplateEngine;

	public BaseThymeleafNarrativeGenerator() {
		myThymeleafConfig = new Configuration();
		myThymeleafConfig.addTemplateResolver(new ClassLoaderTemplateResolver());
		myThymeleafConfig.addMessageResolver(new StandardMessageResolver());
		myThymeleafConfig.setTemplateModeHandlers(StandardTemplateModeHandlers.ALL_TEMPLATE_MODE_HANDLERS);
		myThymeleafConfig.initialize();
	}

	@Override
	public void generateNarrative(IBaseResource theResource, BaseNarrativeDt<?> theNarrative) {
		generateNarrative(null, theResource, theNarrative);
	}

	@Override
	public void setFhirContext(FhirContext theFhirContext) {
		if (theFhirContext == null) {
			throw new NullPointerException("Can not set theFhirContext to null");
		}
		if (myFhirContext != null && myFhirContext != theFhirContext) {
			throw new IllegalStateException("Narrative generators may not be reused/shared across multiple FhirContext instances");
		}
		myFhirContext = theFhirContext;
	}

	@Override
	public void generateNarrative(String theProfile, IBaseResource theResource, BaseNarrativeDt<?> theNarrative) {
		if (!myInitialized) {
			initialize();
		}

		String name = null;
		if (StringUtils.isNotBlank(theProfile)) {
			name = myProfileToName.get(theProfile);
		}
		if (name == null) {
			name = myClassToName.get(theResource.getClass());
		}
		if (name == null) {
			name = myFhirContext.getResourceDefinition(theResource).getName().toLowerCase();
		}

		if (name == null) {
			if (myIgnoreMissingTemplates) {
				ourLog.debug("No narrative template available for profile: {}", theProfile);
				theNarrative.getDiv().setValueAsString("<div>No narrative template available for resource profile: " + theProfile + "</div>");
				theNarrative.getStatus().setValueAsString("empty");
				return;
			} else {
				throw new DataFormatException("No narrative template for class " + theResource.getClass().getCanonicalName());
			}
		}

		try {
			Context context = new Context();
			context.setVariable("resource", theResource);
			context.setVariable("fhirVersion", myFhirContext.getVersion().getVersion().name());

			String result = myProfileTemplateEngine.process(name, context);

			if (myCleanWhitespace) {
				ourLog.trace("Pre-whitespace cleaning: ", result);
				result = cleanWhitespace(result);
				ourLog.trace("Post-whitespace cleaning: ", result);
			}
			
			if (isBlank(result)) {
				return;
			}

			theNarrative.getDiv().setValueAsString(result);
			theNarrative.getStatus().setValueAsString("generated");
			return;
		} catch (Exception e) {
			if (myIgnoreFailures) {
				ourLog.error("Failed to generate narrative", e);
				theNarrative.getDiv().setValueAsString("<div>No narrative available - Error: " + e.getMessage() + "</div>");
				theNarrative.getStatus().setValueAsString("empty");
				return;
			} else {
				throw new DataFormatException(e);
			}
		}
	}

	@Override
	public String generateTitle(IBaseResource theResource) {
		return generateTitle( null, theResource);
	}

	@Override
	public String generateTitle(String theProfile, IBaseResource theResource) {
		if (!myInitialized) {
			initialize();
		}

		ourLog.trace("Generating resource title {}", theResource);

		String name = null;
		if (StringUtils.isNotBlank(theProfile)) {
			name = myProfileToName.get(theProfile);
		}
		if (name == null) {
			name = myClassToName.get(theResource.getClass());
		}
		if (name == null) {
			name = myFhirContext.getResourceDefinition(theResource).getName().toLowerCase();
		}

		ourLog.trace("Template name is {}", name);

		if (name == null) {
			if (myIgnoreMissingTemplates) {
				ourLog.debug("No title template available for profile: {}", theProfile);
				return null;
			} else {
				throw new DataFormatException("No title template for class " + theResource.getClass().getCanonicalName());
			}
		}

		try {
			Context context = new Context();
			context.setVariable("resource", theResource);
			context.setVariable("fhirVersion", myFhirContext.getVersion().getVersion().name());

			String result = myTitleTemplateEngine.process(name, context);

			ourLog.trace("Produced {}", result);

			StringBuilder b = new StringBuilder();
			boolean inTag = false;
			for (int i = 0; i < result.length(); i++) {
				char nextChar = result.charAt(i);
				char prevChar = i > 0 ? result.charAt(i - 1) : '\n';
				if (nextChar == '<') {
					inTag = true;
					continue;
				} else if (inTag) {
					if (nextChar == '>') {
						inTag = false;
					}
					continue;
				} else if (nextChar <= ' ') {
					if (prevChar <= ' ' || prevChar == '>') {
						continue;
					} else {
						b.append(' ');
					}
				} else {
					b.append(nextChar);
				}
			}

			while (b.length() > 0 && b.charAt(b.length() - 1) == ' ') {
				b.setLength(b.length() - 1);
			}

			result = b.toString();
			if (result.startsWith("<") && result.contains(">")) {
				result = result.substring(result.indexOf('>') + 1);
			}
			if (result.endsWith(">") && result.contains("<")) {
				result = result.substring(0, result.lastIndexOf('<'));
			}

			result = result.replace("&gt;", ">").replace("&lt;", "<").replace("&amp;", "&");

			return result;
		} catch (Exception e) {
			if (myIgnoreFailures) {
				ourLog.error("Failed to generate narrative", e);
				return "No title available - Error: " + e.getMessage();
			} else {
				throw new DataFormatException(e);
			}
		}
	}

	protected abstract List<String> getPropertyFile();

	private Document getXhtmlDOMFor(final Reader source) {
		final Configuration configuration1 = myThymeleafConfig;
		try {
			return PARSER.parseTemplate(configuration1, "input", source);
		} catch (final Exception e) {
			throw new TemplateInputException("Exception during parsing of source", e);
		}
	}

	private synchronized void initialize() {
		if (myInitialized) {
			return;
		}

		ourLog.info("Initializing narrative generator");

		myProfileToName = new HashMap<String, String>();
		myClassToName = new HashMap<Class<?>, String>();
		myNameToNarrativeTemplate = new HashMap<String, String>();
		myNameToTitleTemplate = new HashMap<String, String>();

		List<String> propFileName = getPropertyFile();

		try {
			if (myApplyDefaultDatatypeTemplates) {
				loadProperties(DefaultThymeleafNarrativeGenerator.NARRATIVES_PROPERTIES);
			}
			for (String next : propFileName) {
				loadProperties(next);
			}
		} catch (IOException e) {
			ourLog.info("Failed to load property file " + propFileName, e);
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
		{
			myTitleTemplateEngine = new TemplateEngine();
			TemplateResolver resolver = new TemplateResolver();
			resolver.setResourceResolver(new TitleResourceResolver());
			myTitleTemplateEngine.setTemplateResolver(resolver);
			StandardDialect dialect = new StandardDialect();
			HashSet<IProcessor> additionalProcessors = new HashSet<IProcessor>();
			additionalProcessors.add(new NarrativeAttributeProcessor());
			dialect.setAdditionalProcessors(additionalProcessors);
			myTitleTemplateEngine.setDialect(dialect);
			myTitleTemplateEngine.initialize();
		}

		myInitialized = true;
	}

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will be trimmed from the generated narrative
	 * before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed. Repeated whitespace characters (e.g.
	 * "\n       \n       ") will be trimmed to a single space.
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
				String titlePropName = name + ".title";
				String titleName = file.getProperty(titlePropName);
				if (isBlank(narrativeName) && isBlank(titleName)) {
					throw new ConfigurationException("Found property '" + nextKey + "' but no corresponding property '" + narrativePropName + "' or '" + titlePropName + "' in file " + propFileName);
				}

				myProfileToName.put(file.getProperty(nextKey), name);

				if (StringUtils.isNotBlank(narrativeName)) {
					String narrative = IOUtils.toString(loadResource(narrativeName));
					myNameToNarrativeTemplate.put(name, narrative);
				}
				if (StringUtils.isNotBlank(titleName)) {
					String title = IOUtils.toString(loadResource(titleName));
					myNameToTitleTemplate.put(name, title);
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

//				if (isBlank(narrativeName) && isBlank(titleName)) {
//					throw new ConfigurationException("Found property '" + nextKey + "' but no corresponding property '" + narrativePropName + "' or '" + titlePropName + "' in file " + propFileName);
//				}

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
					String narrative = IOUtils.toString(loadResource(narrativeName));
					myNameToNarrativeTemplate.put(name, narrative);
				}
				continue;
			} else if (nextKey.endsWith(".title")) {
				String name = nextKey.substring(0, nextKey.indexOf(".title"));
				if (isBlank(name)) {
					continue;
				}
				
				String titlePropName = name + ".title";
				String titleName = file.getProperty(titlePropName);
				if (StringUtils.isNotBlank(titleName)) {
					String title = IOUtils.toString(loadResource(titleName));
					myNameToTitleTemplate.put(name, title);
				}
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

	/**
	 * If set to <code>true</code> (which is the default), most whitespace will be trimmed from the generated narrative
	 * before it is returned.
	 * <p>
	 * Note that in order to preserve formatting, not all whitespace is trimmed. Repeated whitespace characters (e.g.
	 * "\n       \n       ") will be trimmed to a single space.
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

	static String cleanWhitespace(String theResult) {
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
					if (i + 3 < theResult.length()) {
						char char1 = Character.toLowerCase(theResult.charAt(i + 1));
						char char2 = Character.toLowerCase(theResult.charAt(i + 2));
						char char3 = Character.toLowerCase(theResult.charAt(i + 3));
						char char4 = Character.toLowerCase((i + 4 < theResult.length()) ? theResult.charAt(i + 4) : ' ');
						if (char1 == 'p' && char2 == 'r' && char3 == 'e') {
							inPre = true;
						} else if (char1 == '/' && char2 == 'p' && char3 == 'r'&&char4=='e') {
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
	
	private FhirContext myFhirContext;

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

			if (value == null) {
				return ProcessorResult.ok();
			}

			Context context = new Context();
			context.setVariable("resource", value);

			String name = null;
			if (value != null) {
				Class<? extends Object> nextClass = value.getClass();
				do {
					name = myClassToName.get(nextClass);
					nextClass = nextClass.getSuperclass();
				} while (name == null && nextClass.equals(Object.class) == false);
				
				if (name == null) {
					if (value instanceof IBaseResource) {
						name = myFhirContext.getResourceDefinition((IBaseResource)value).getName();
					} else if (value instanceof IDatatype) {
						name = value.getClass().getSimpleName();
						name = name.substring(0, name.length() - 2);
					} else {
						throw new DataFormatException("Don't know how to determine name for type: " + value.getClass());
					}
					name = name.toLowerCase();
					if (!myNameToNarrativeTemplate.containsKey(name)) {
						name = null;
					}
				}
			}

			if (name == null) {
				if (myIgnoreMissingTemplates) {
					ourLog.debug("No narrative template available for type: {}", value.getClass());
					return ProcessorResult.ok();
				} else {
					throw new DataFormatException("No narrative template for class " + value.getClass());
				}
			}

			String result = myProfileTemplateEngine.process(name, context);
			String trim = result.trim();
			if (!isBlank(trim + "AAA")) {
				Document dom = getXhtmlDOMFor(new StringReader(trim));
				
				Element firstChild = (Element) dom.getFirstChild();
				for (int i = 0; i < firstChild.getChildren().size(); i++) {
					Node next = firstChild.getChildren().get(i);
					if (i == 0 && firstChild.getChildren().size() == 1) {
						if (next instanceof org.thymeleaf.dom.Text) {
							org.thymeleaf.dom.Text nextText = (org.thymeleaf.dom.Text) next;
							nextText.setContent(nextText.getContent().trim());
						}
					}
					theElement.addChild(next);
				}
				
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

	private final class TitleResourceResolver implements IResourceResolver {
		@Override
		public String getName() {
			return getClass().getCanonicalName();
		}

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters theTemplateProcessingParameters, String theName) {
			String template = myNameToTitleTemplate.get(theName);
			if (template == null) {
				ourLog.info("No narative template for resource profile: {}", theName);
				return new ReaderInputStream(new StringReader(""));
			}
			return new ReaderInputStream(new StringReader(template));
		}
	}
}
