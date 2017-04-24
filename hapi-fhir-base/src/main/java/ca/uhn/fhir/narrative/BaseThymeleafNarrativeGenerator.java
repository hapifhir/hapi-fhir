package ca.uhn.fhir.narrative;

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
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.DefaultTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.templateresource.StringTemplateResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.Constants;

public abstract class BaseThymeleafNarrativeGenerator implements INarrativeGenerator {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseThymeleafNarrativeGenerator.class);

	private boolean myApplyDefaultDatatypeTemplates = true;

	private HashMap<Class<?>, String> myClassToName;
	private boolean myCleanWhitespace = true;
	private boolean myIgnoreFailures = true;
	private boolean myIgnoreMissingTemplates = true;
	private volatile boolean myInitialized;
	private HashMap<String, String> myNameToNarrativeTemplate;
	private TemplateEngine myProfileTemplateEngine;

	/**
	 * Constructor
	 */
	public BaseThymeleafNarrativeGenerator() {
		super();
	}

	@Override
	public void generateNarrative(FhirContext theContext, IBaseResource theResource, INarrative theNarrative) {
		if (!myInitialized) {
			initialize(theContext);
		}

		String name = myClassToName.get(theResource.getClass());
		if (name == null) {
			name = theContext.getResourceDefinition(theResource).getName().toLowerCase();
		}

		if (name == null || !myNameToNarrativeTemplate.containsKey(name)) {
			if (myIgnoreMissingTemplates) {
				ourLog.debug("No narrative template available for resorce: {}", name);
				return;
			}
			throw new DataFormatException("No narrative template for class " + theResource.getClass().getCanonicalName());
		}

		try {
			Context context = new Context();
			context.setVariable("resource", theResource);
			context.setVariable("fhirVersion", theContext.getVersion().getVersion().name());

			String result = myProfileTemplateEngine.process(name, context);

			if (myCleanWhitespace) {
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
			if (myIgnoreFailures) {
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

	protected abstract List<String> getPropertyFile();

	private synchronized void initialize(final FhirContext theContext) {
		if (myInitialized) {
			return;
		}

		ourLog.info("Initializing narrative generator");

		myClassToName = new HashMap<Class<?>, String>();
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
			ourLog.info("Failed to load property file " + propFileName, e);
			throw new ConfigurationException("Can not load property file " + propFileName, e);
		}

		{
			myProfileTemplateEngine = new TemplateEngine();
			ProfileResourceResolver resolver = new ProfileResourceResolver();
			myProfileTemplateEngine.setTemplateResolver(resolver);
			StandardDialect dialect = new StandardDialect() {
				@Override
				public Set<IProcessor> getProcessors(String theDialectPrefix) {
					Set<IProcessor> retVal = super.getProcessors(theDialectPrefix);
					retVal.add(new NarrativeAttributeProcessor(theContext, theDialectPrefix));
					return retVal;
				}

			};
			myProfileTemplateEngine.setDialect(dialect);
		}

		myInitialized = true;
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

	public class NarrativeAttributeProcessor extends AbstractAttributeTagProcessor {

		private FhirContext myContext;

		protected NarrativeAttributeProcessor(FhirContext theContext, String theDialectPrefix) {
			super(TemplateMode.XML, theDialectPrefix, null, false, "narrative", true, 0, true);
			myContext = theContext;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void doProcess(ITemplateContext theContext, IProcessableElementTag theTag, AttributeName theAttributeName, String theAttributeValue, IElementTagStructureHandler theStructureHandler) {
			IEngineConfiguration configuration = theContext.getConfiguration();
			IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);

			final IStandardExpression expression = expressionParser.parseExpression(theContext, theAttributeValue);
			final Object value = expression.execute(theContext);

			if (value == null) {
				return;
			}

			Context context = new Context();
			context.setVariable("fhirVersion", myContext.getVersion().getVersion().name());
			context.setVariable("resource", value);

			String name = null;

			Class<? extends Object> nextClass = value.getClass();
			do {
				name = myClassToName.get(nextClass);
				nextClass = nextClass.getSuperclass();
			} while (name == null && nextClass.equals(Object.class) == false);

			if (name == null) {
				if (value instanceof IBaseResource) {
					name = myContext.getResourceDefinition((Class<? extends IBaseResource>) value).getName();
				} else if (value instanceof IDatatype) {
					name = value.getClass().getSimpleName();
					name = name.substring(0, name.length() - 2);
				} else if (value instanceof IBaseDatatype) {
					name = value.getClass().getSimpleName();
					if (name.endsWith("Type")) {
						name = name.substring(0, name.length() - 4);
					}
				} else {
					throw new DataFormatException("Don't know how to determine name for type: " + value.getClass());
				}
				name = name.toLowerCase();
				if (!myNameToNarrativeTemplate.containsKey(name)) {
					name = null;
				}
			}

			if (name == null) {
				if (myIgnoreMissingTemplates) {
					ourLog.debug("No narrative template available for type: {}", value.getClass());
					return;
				}
				throw new DataFormatException("No narrative template for class " + value.getClass());
			}

			String result = myProfileTemplateEngine.process(name, context);
			String trim = result.trim();

			theStructureHandler.setBody(trim, true);

		}

	}

	// public class NarrativeAttributeProcessor extends AbstractAttributeTagProcessor {
	//
	// private FhirContext myContext;
	//
	// protected NarrativeAttributeProcessor(FhirContext theContext) {
	// super()
	// myContext = theContext;
	// }
	//
	// @Override
	// public int getPrecedence() {
	// return 0;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected ProcessorResult processAttribute(Arguments theArguments, Element theElement, String theAttributeName) {
	// final String attributeValue = theElement.getAttributeValue(theAttributeName);
	//
	// final Configuration configuration = theArguments.getConfiguration();
	// final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(configuration);
	//
	// final IStandardExpression expression = expressionParser.parseExpression(configuration, theArguments, attributeValue);
	// final Object value = expression.execute(configuration, theArguments);
	//
	// theElement.removeAttribute(theAttributeName);
	// theElement.clearChildren();
	//
	// if (value == null) {
	// return ProcessorResult.ok();
	// }
	//
	// Context context = new Context();
	// context.setVariable("fhirVersion", myContext.getVersion().getVersion().name());
	// context.setVariable("resource", value);
	//
	// String name = null;
	// if (value != null) {
	// Class<? extends Object> nextClass = value.getClass();
	// do {
	// name = myClassToName.get(nextClass);
	// nextClass = nextClass.getSuperclass();
	// } while (name == null && nextClass.equals(Object.class) == false);
	//
	// if (name == null) {
	// if (value instanceof IBaseResource) {
	// name = myContext.getResourceDefinition((Class<? extends IBaseResource>) value).getName();
	// } else if (value instanceof IDatatype) {
	// name = value.getClass().getSimpleName();
	// name = name.substring(0, name.length() - 2);
	// } else if (value instanceof IBaseDatatype) {
	// name = value.getClass().getSimpleName();
	// if (name.endsWith("Type")) {
	// name = name.substring(0, name.length() - 4);
	// }
	// } else {
	// throw new DataFormatException("Don't know how to determine name for type: " + value.getClass());
	// }
	// name = name.toLowerCase();
	// if (!myNameToNarrativeTemplate.containsKey(name)) {
	// name = null;
	// }
	// }
	// }
	//
	// if (name == null) {
	// if (myIgnoreMissingTemplates) {
	// ourLog.debug("No narrative template available for type: {}", value.getClass());
	// return ProcessorResult.ok();
	// } else {
	// throw new DataFormatException("No narrative template for class " + value.getClass());
	// }
	// }
	//
	// String result = myProfileTemplateEngine.process(name, context);
	// String trim = result.trim();
	// if (!isBlank(trim + "AAA")) {
	// Document dom = getXhtmlDOMFor(new StringReader(trim));
	//
	// Element firstChild = (Element) dom.getFirstChild();
	// for (int i = 0; i < firstChild.getChildren().size(); i++) {
	// Node next = firstChild.getChildren().get(i);
	// if (i == 0 && firstChild.getChildren().size() == 1) {
	// if (next instanceof org.thymeleaf.dom.Text) {
	// org.thymeleaf.dom.Text nextText = (org.thymeleaf.dom.Text) next;
	// nextText.setContent(nextText.getContent().trim());
	// }
	// }
	// theElement.addChild(next);
	// }
	//
	// }
	//
	//
	// return ProcessorResult.ok();
	// }
	//
	// }

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

	private final class ProfileResourceResolver extends DefaultTemplateResolver {

		@Override
		protected boolean computeResolvable(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			String template = myNameToNarrativeTemplate.get(theTemplate);
			return template != null;
		}

		@Override
		protected TemplateMode computeTemplateMode(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return TemplateMode.XML;
		}

		@Override
		protected ITemplateResource computeTemplateResource(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			String template = myNameToNarrativeTemplate.get(theTemplate);
			return new StringTemplateResource(template);
		}

		@Override
		protected ICacheEntryValidity computeValidity(IEngineConfiguration theConfiguration, String theOwnerTemplate, String theTemplate, Map<String, Object> theTemplateResolutionAttributes) {
			return AlwaysValidCacheEntryValidity.INSTANCE;
		}

	}

}
