package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;
import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.stax.WstxOutputFactory;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.io.EscapingWriterFactory;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Utility methods for working with the StAX API.
 * <p>
 * This class contains code adapted from the Apache Axiom project.
 */
public class RDFUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RDFUtil.class);
	private static final Map<String, Integer> VALID_ENTITY_NAMES;
	private static final ExtendedEntityReplacingXmlResolver XML_RESOLVER = new ExtendedEntityReplacingXmlResolver();
	private static XMLOutputFactory ourFragmentOutputFactory;
	private static volatile boolean ourHaveLoggedStaxImplementation;
	private static volatile XMLInputFactory ourInputFactory;
	private static Throwable ourNextException;
	private static volatile XMLOutputFactory ourOutputFactory;

	static {
		HashMap<String, Integer> validEntityNames = new HashMap<>(1448);
		VALID_ENTITY_NAMES = Collections.unmodifiableMap(validEntityNames);
	}

	private static XMLOutputFactory createOutputFactory() throws FactoryConfigurationError {
		try {
			// Detect if we're running with the Android lib, and force repackaged Woodstox to be used
			Class.forName("ca.uhn.fhir.repackage.javax.xml.stream.XMLOutputFactory");
			System.setProperty("javax.xml.stream.XMLOutputFactory", "com.ctc.wstx.stax.WstxOutputFactory");
		} catch (ClassNotFoundException e) {
			// ok
		}

		XMLOutputFactory outputFactory = newOutputFactory();

		if (!ourHaveLoggedStaxImplementation) {
			logStaxImplementation(outputFactory.getClass());
		}

		/*
		 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
		 * being used (e.g. glassfish) so we don't set them there.
		 */
		try {
			Class.forName("com.ctc.wstx.stax.WstxOutputFactory");
			if (outputFactory instanceof WstxOutputFactory) {
//				((WstxOutputFactory)outputFactory).getConfig().setAttrValueEscaperFactory(new MyEscaper());
				outputFactory.setProperty(XMLOutputFactory2.P_TEXT_ESCAPER, new MyEscaper());
			}
		} catch (ClassNotFoundException e) {
			ourLog.debug("WstxOutputFactory (Woodstox) not found on classpath");
		}
		return outputFactory;
	}

	private static XMLEventWriter createXmlFragmentWriter(final Writer theWriter)
		throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateFragmentOutputFactory();
		return outputFactory.createXMLEventWriter(theWriter);
	}

	public static XMLEventReader createXmlReader(final Reader reader)
		throws FactoryConfigurationError, XMLStreamException {
		throwUnitTestExceptionIfConfiguredToDoSo();

		XMLInputFactory inputFactory = getOrCreateInputFactory();

		// Now.. create the reader and return it
		return inputFactory.createXMLEventReader(reader);
	}

	public static XMLStreamWriter createXmlStreamWriter(final Writer theWriter)
		throws FactoryConfigurationError, XMLStreamException {
		throwUnitTestExceptionIfConfiguredToDoSo();

		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		return outputFactory.createXMLStreamWriter(theWriter);
	}

	public static XMLEventWriter createXmlWriter(final Writer theWriter)
		throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		return outputFactory.createXMLEventWriter(theWriter);
	}

	/**
	 * Encode a set of StAX events into a String
	 */
	public static String encode(final List<XMLEvent> theEvents) {
		try {
			StringWriter w = new StringWriter();
			XMLEventWriter ew = RDFUtil.createXmlFragmentWriter(w);

			for (XMLEvent next : theEvents) {
				if (next.isCharacters()) {
					ew.add(next);
				} else {
					ew.add(next);
				}
			}
			ew.close();
			return w.toString();
		} catch (XMLStreamException e) {
			throw new DataFormatException("Problem with the contained XML events", e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException(e);
		}
	}

	private static XMLOutputFactory getOrCreateFragmentOutputFactory() throws FactoryConfigurationError {
		XMLOutputFactory retVal = ourFragmentOutputFactory;
		if (retVal == null) {
			retVal = createOutputFactory();
			retVal.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.TRUE);
			ourFragmentOutputFactory = retVal;
			return retVal;
		}
		return retVal;
	}

	private static XMLInputFactory getOrCreateInputFactory() throws FactoryConfigurationError {
		if (ourInputFactory == null) {

			try {
				// Detect if we're running with the Android lib, and force repackaged Woodstox to be used
				Class.forName("ca.uhn.fhir.repackage.javax.xml.stream.XMLInputFactory");
				System.setProperty("javax.xml.stream.XMLInputFactory", "com.ctc.wstx.stax.WstxInputFactory");
			} catch (ClassNotFoundException e) {
				// ok
			}

			XMLInputFactory inputFactory = newInputFactory();

			if (!ourHaveLoggedStaxImplementation) {
				logStaxImplementation(inputFactory.getClass());
			}

			/*
			 * These two properties disable external entity processing, which can
			 * be a security vulnerability.
			 *
			 * See https://github.com/jamesagnew/hapi-fhir/issues/339
			 * https://www.owasp.org/index.php/XML_External_Entity_%28XXE%29_Processing
			 */
			inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false); // This disables DTDs entirely for that factory
			inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", false); // disable external entities


			/*
			 * In the following few lines, you can uncomment the first and comment the second to disable automatic
			 * parsing of extended entities, e.g. &sect;
			 *
			 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
			 * being used (e.g. glassfish) so we don't set them there.
			 */
			try {
				Class.forName("com.ctc.wstx.stax.WstxInputFactory");
				boolean isWoodstox = inputFactory instanceof com.ctc.wstx.stax.WstxInputFactory;
				if (!isWoodstox) {
					// Check if implementation is woodstox by property since instanceof check does not work if running in JBoss
					try {
						isWoodstox = inputFactory.getProperty("org.codehaus.stax2.implVersion") != null;
					} catch (Exception e) {
						// ignore
					}
				}
				if (isWoodstox) {
					// inputFactory.setProperty(WstxInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
					inputFactory.setProperty(WstxInputProperties.P_UNDECLARED_ENTITY_RESOLVER, XML_RESOLVER);
					try {
						inputFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, "100000000");
					} catch (IllegalArgumentException e) {
						// ignore
					}
				}
			} catch (ClassNotFoundException e) {
				ourLog.debug("WstxOutputFactory (Woodstox) not found on classpath");
			}
			ourInputFactory = inputFactory;
		}
		return ourInputFactory;
	}

	private static XMLOutputFactory getOrCreateOutputFactory() throws FactoryConfigurationError {
		if (ourOutputFactory == null) {
			ourOutputFactory = createOutputFactory();
		}
		return ourOutputFactory;
	}

	private static void logStaxImplementation(final Class<?> theClass) {
		IDependencyLog logger = DependencyLogFactory.createJarLogger();
		if (logger != null) {
			logger.logStaxImplementation(theClass);
		}
		ourHaveLoggedStaxImplementation = true;
	}

	static XMLInputFactory newInputFactory() throws FactoryConfigurationError {
		XMLInputFactory inputFactory;
		try {
			inputFactory = XMLInputFactory.newInstance();
			if (inputFactory.isPropertySupported(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES)) {
				inputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
			}
			throwUnitTestExceptionIfConfiguredToDoSo();
		} catch (Throwable e) {
			throw new ConfigurationException("Unable to initialize StAX - XML processing is disabled", e);
		}
		return inputFactory;
	}

	static XMLOutputFactory newOutputFactory() throws FactoryConfigurationError {
		XMLOutputFactory outputFactory;
		try {
			outputFactory = XMLOutputFactory.newInstance();
			throwUnitTestExceptionIfConfiguredToDoSo();
		} catch (Throwable e) {
			throw new ConfigurationException("Unable to initialize StAX - XML processing is disabled", e);
		}
		return outputFactory;
	}

	/**
	 * Parses an XML string into a set of StAX events
	 */
	public static List<XMLEvent> parse(final String theValue) {
		if (isBlank(theValue)) {
			return Collections.emptyList();
		}

		String val = theValue.trim();
		if (!val.startsWith("<")) {
			val = XhtmlDt.DIV_OPEN_FIRST + val + "</div>";
		}
		boolean hasProcessingInstruction = val.startsWith("<?");
		if (hasProcessingInstruction && val.endsWith("?>")) {
			return null;
		}

		try {
			ArrayList<XMLEvent> value = new ArrayList<>();
			StringReader reader = new StringReader(val);
			XMLEventReader er = RDFUtil.createXmlReader(reader);
			boolean first = true;
			while (er.hasNext()) {
				XMLEvent next = er.nextEvent();
				if (first) {
					first = false;
					continue;
				}
				if (er.hasNext()) {
					// don't add the last event
					value.add(next);
				}
			}
			return value;

		} catch (XMLStreamException e) {
			throw new DataFormatException("String does not appear to be valid XML/XHTML (error is \"" + e.getMessage() + "\"): " + theValue, e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * FOR UNIT TESTS ONLY - Throw this exception for the next operation
	 */
	static void setThrowExceptionForUnitTest(final Throwable theException) {
		ourNextException = theException;
	}

	private static void throwUnitTestExceptionIfConfiguredToDoSo() throws FactoryConfigurationError, XMLStreamException {
		if (ourNextException != null) {
			if (ourNextException instanceof FactoryConfigurationError) {
				throw ((FactoryConfigurationError) ourNextException);
			}
			throw (XMLStreamException) ourNextException;
		}
	}

	private static final class ExtendedEntityReplacingXmlResolver implements XMLResolver {
		@Override
		public Object resolveEntity(final String thePublicID, final String theSystemID, final String theBaseURI, final String theNamespace) {
			if (thePublicID == null && theSystemID == null) {
				if (theNamespace != null && VALID_ENTITY_NAMES.containsKey(theNamespace)) {
					return new String(Character.toChars(VALID_ENTITY_NAMES.get(theNamespace)));
				}
			}

			return null;
		}
	}

	public static class MyEscaper implements EscapingWriterFactory {

		@Override
		public Writer createEscapingWriterFor(final OutputStream theOut, final String theEnc) throws UnsupportedEncodingException {
			return createEscapingWriterFor(new OutputStreamWriter(theOut, theEnc), theEnc);
		}

		@Override
		public Writer createEscapingWriterFor(final Writer theW, final String theEnc) {
			return new Writer() {

				@Override
				public void close() throws IOException {
					theW.close();
				}

				@Override
				public void flush() throws IOException {
					theW.flush();
				}

				@Override
				public void write(char[] theCbuf, int theOff, int theLen) throws IOException {
					boolean hasEscapable = false;
					for (int i = 0; i < theLen && !hasEscapable; i++) {
						char nextChar = theCbuf[i + theOff];
						switch (nextChar) {
							case '<':
							case '>':
							case '"':
							case '&':
								hasEscapable = true;
								break;
							default:
								break;
						}
					}

					if (!hasEscapable) {
						theW.write(theCbuf, theOff, theLen);
						return;
					}

					String escaped = StringEscapeUtils.escapeXml10(new String(theCbuf, theOff, theLen));
					theW.write(escaped.toCharArray());
				}
			};
		}

	}

}
