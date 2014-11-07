package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.io.EscapingWriterFactory;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.stax.WstxOutputFactory;

/**
 * Utility methods for working with the StAX API.
 * 
 * This class contains code adapted from the Apache Axiom project.
 */
public class XmlUtil {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlUtil.class);
	private static volatile XMLOutputFactory ourOutputFactory;
	private static volatile XMLInputFactory ourInputFactory;
	private static volatile boolean ourHaveLoggedStaxImplementation;
	private static final Map<String, Integer> VALID_ENTITY_NAMES;
	private static final ExtendedEntityReplacingXmlResolver XML_RESOLVER = new ExtendedEntityReplacingXmlResolver();

	private static final Attributes.Name IMPLEMENTATION_TITLE = new Attributes.Name("Implementation-Title");

	private static final Attributes.Name IMPLEMENTATION_VENDOR = new Attributes.Name("Implementation-Vendor");

	private static final Attributes.Name IMPLEMENTATION_VERSION = new Attributes.Name("Implementation-Version");

	private static final Attributes.Name BUNDLE_SYMBOLIC_NAME = new Attributes.Name("Bundle-SymbolicName");

	private static final Attributes.Name BUNDLE_VENDOR = new Attributes.Name("Bundle-Vendor");

	private static final Attributes.Name BUNDLE_VERSION = new Attributes.Name("Bundle-Version");

	static {
		HashMap<String, Integer> validEntityNames = new HashMap<String, Integer>();
		validEntityNames.put("nbsp", 160); // no-break space = non-breaking space, U+00A0 ISOnum -->
		validEntityNames.put("iexcl", 161); // inverted exclamation mark, U+00A1 ISOnum -->
		validEntityNames.put("cent", 162); // cent sign, U+00A2 ISOnum -->
		validEntityNames.put("pound", 163); // pound sign, U+00A3 ISOnum -->
		validEntityNames.put("curren", 164); // currency sign, U+00A4 ISOnum -->
		validEntityNames.put("yen", 165); // yen sign = yuan sign, U+00A5 ISOnum -->
		validEntityNames.put("brvbar", 166); // broken bar = broken vertical bar, U+00A6 ISOnum -->
		validEntityNames.put("sect", 167); // section sign, U+00A7 ISOnum -->
		validEntityNames.put("uml", 168); // diaeresis = spacing diaeresis, U+00A8 ISOdia -->
		validEntityNames.put("copy", 169); // copyright sign, U+00A9 ISOnum -->
		validEntityNames.put("ordf", 170); // feminine ordinal indicator, U+00AA ISOnum -->
		validEntityNames.put("laquo", 171); // left-pointing double angle quotation mark = left pointing guillemet,
											// U+00AB ISOnum -->
		validEntityNames.put("not", 172); // not sign = angled dash, U+00AC ISOnum -->
		validEntityNames.put("shy", 173); // soft hyphen = discretionary hyphen, U+00AD ISOnum -->
		validEntityNames.put("reg", 174); // registered sign = registered trade mark sign, U+00AE ISOnum -->
		validEntityNames.put("macr", 175); // macron = spacing macron = overline = APL overbar, U+00AF ISOdia -->
		validEntityNames.put("deg", 176); // degree sign, U+00B0 ISOnum -->
		validEntityNames.put("plusmn", 177); // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum -->
		validEntityNames.put("sup2", 178); // superscript two = superscript digit two = squared, U+00B2 ISOnum -->
		validEntityNames.put("sup3", 179); // superscript three = superscript digit three = cubed, U+00B3 ISOnum -->
		validEntityNames.put("acute", 180); // acute accent = spacing acute, U+00B4 ISOdia -->
		validEntityNames.put("micro", 181); // micro sign, U+00B5 ISOnum -->
		validEntityNames.put("para", 182); // pilcrow sign = paragraph sign, U+00B6 ISOnum -->
		validEntityNames.put("middot", 183); // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum -->
		validEntityNames.put("cedil", 184); // cedilla = spacing cedilla, U+00B8 ISOdia -->
		validEntityNames.put("sup1", 185); // superscript one = superscript digit one, U+00B9 ISOnum -->
		validEntityNames.put("ordm", 186); // masculine ordinal indicator, U+00BA ISOnum -->
		validEntityNames.put("raquo", 187); // right-pointing double angle quotation mark = right pointing guillemet,
											// U+00BB ISOnum -->
		validEntityNames.put("frac14", 188); // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum -->
		validEntityNames.put("frac12", 189); // vulgar fraction one half = fraction one half, U+00BD ISOnum -->
		validEntityNames.put("frac34", 190); // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
												// -->
		validEntityNames.put("iquest", 191); // inverted question mark = turned question mark, U+00BF ISOnum -->
		validEntityNames.put("Agrave", 192); // latin capital letter A with grave = latin capital letter A grave, U+00C0
												// ISOlat1 -->
		validEntityNames.put("Aacute", 193); // latin capital letter A with acute, U+00C1 ISOlat1 -->
		validEntityNames.put("Acirc", 194); // latin capital letter A with circumflex, U+00C2 ISOlat1 -->
		validEntityNames.put("Atilde", 195); // latin capital letter A with tilde, U+00C3 ISOlat1 -->
		validEntityNames.put("Auml", 196); // latin capital letter A with diaeresis, U+00C4 ISOlat1 -->
		validEntityNames.put("Aring", 197); // latin capital letter A with ring above = latin capital letter A ring,
											// U+00C5 ISOlat1 -->
		validEntityNames.put("AElig", 198); // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1 -->
		validEntityNames.put("Ccedil", 199); // latin capital letter C with cedilla, U+00C7 ISOlat1 -->
		validEntityNames.put("Egrave", 200); // latin capital letter E with grave, U+00C8 ISOlat1 -->
		validEntityNames.put("Eacute", 201); // latin capital letter E with acute, U+00C9 ISOlat1 -->
		validEntityNames.put("Ecirc", 202); // latin capital letter E with circumflex, U+00CA ISOlat1 -->
		validEntityNames.put("Euml", 203); // latin capital letter E with diaeresis, U+00CB ISOlat1 -->
		validEntityNames.put("Igrave", 204); // latin capital letter I with grave, U+00CC ISOlat1 -->
		validEntityNames.put("Iacute", 205); // latin capital letter I with acute, U+00CD ISOlat1 -->
		validEntityNames.put("Icirc", 206); // latin capital letter I with circumflex, U+00CE ISOlat1 -->
		validEntityNames.put("Iuml", 207); // latin capital letter I with diaeresis, U+00CF ISOlat1 -->
		validEntityNames.put("ETH", 208); // latin capital letter ETH, U+00D0 ISOlat1 -->
		validEntityNames.put("Ntilde", 209); // latin capital letter N with tilde, U+00D1 ISOlat1 -->
		validEntityNames.put("Ograve", 210); // latin capital letter O with grave, U+00D2 ISOlat1 -->
		validEntityNames.put("Oacute", 211); // latin capital letter O with acute, U+00D3 ISOlat1 -->
		validEntityNames.put("Ocirc", 212); // latin capital letter O with circumflex, U+00D4 ISOlat1 -->
		validEntityNames.put("Otilde", 213); // latin capital letter O with tilde, U+00D5 ISOlat1 -->
		validEntityNames.put("Ouml", 214); // latin capital letter O with diaeresis, U+00D6 ISOlat1 -->
		validEntityNames.put("times", 215); // multiplication sign, U+00D7 ISOnum -->
		validEntityNames.put("Oslash", 216); // latin capital letter O with stroke = latin capital letter O slash,
												// U+00D8 ISOlat1 -->
		validEntityNames.put("Ugrave", 217); // latin capital letter U with grave, U+00D9 ISOlat1 -->
		validEntityNames.put("Uacute", 218); // latin capital letter U with acute, U+00DA ISOlat1 -->
		validEntityNames.put("Ucirc", 219); // latin capital letter U with circumflex, U+00DB ISOlat1 -->
		validEntityNames.put("Uuml", 220); // latin capital letter U with diaeresis, U+00DC ISOlat1 -->
		validEntityNames.put("Yacute", 221); // latin capital letter Y with acute, U+00DD ISOlat1 -->
		validEntityNames.put("THORN", 222); // latin capital letter THORN, U+00DE ISOlat1 -->
		validEntityNames.put("szlig", 223); // latin small letter sharp s = ess-zed, U+00DF ISOlat1 -->
		validEntityNames.put("agrave", 224); // latin small letter a with grave = latin small letter a grave, U+00E0
												// ISOlat1 -->
		validEntityNames.put("aacute", 225); // latin small letter a with acute, U+00E1 ISOlat1 -->
		validEntityNames.put("acirc", 226); // latin small letter a with circumflex, U+00E2 ISOlat1 -->
		validEntityNames.put("atilde", 227); // latin small letter a with tilde, U+00E3 ISOlat1 -->
		validEntityNames.put("auml", 228); // latin small letter a with diaeresis, U+00E4 ISOlat1 -->
		validEntityNames.put("aring", 229); // latin small letter a with ring above = latin small letter a ring, U+00E5
											// ISOlat1 -->
		validEntityNames.put("aelig", 230); // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1 -->
		validEntityNames.put("ccedil", 231); // latin small letter c with cedilla, U+00E7 ISOlat1 -->
		validEntityNames.put("egrave", 232); // latin small letter e with grave, U+00E8 ISOlat1 -->
		validEntityNames.put("eacute", 233); // latin small letter e with acute, U+00E9 ISOlat1 -->
		validEntityNames.put("ecirc", 234); // latin small letter e with circumflex, U+00EA ISOlat1 -->
		validEntityNames.put("euml", 235); // latin small letter e with diaeresis, U+00EB ISOlat1 -->
		validEntityNames.put("igrave", 236); // latin small letter i with grave, U+00EC ISOlat1 -->
		validEntityNames.put("iacute", 237); // latin small letter i with acute, U+00ED ISOlat1 -->
		validEntityNames.put("icirc", 238); // latin small letter i with circumflex, U+00EE ISOlat1 -->
		validEntityNames.put("iuml", 239); // latin small letter i with diaeresis, U+00EF ISOlat1 -->
		validEntityNames.put("eth", 240); // latin small letter eth, U+00F0 ISOlat1 -->
		validEntityNames.put("ntilde", 241); // latin small letter n with tilde, U+00F1 ISOlat1 -->
		validEntityNames.put("ograve", 242); // latin small letter o with grave, U+00F2 ISOlat1 -->
		validEntityNames.put("oacute", 243); // latin small letter o with acute, U+00F3 ISOlat1 -->
		validEntityNames.put("ocirc", 244); // latin small letter o with circumflex, U+00F4 ISOlat1 -->
		validEntityNames.put("otilde", 245); // latin small letter o with tilde, U+00F5 ISOlat1 -->
		validEntityNames.put("ouml", 246); // latin small letter o with diaeresis, U+00F6 ISOlat1 -->
		validEntityNames.put("divide", 247); // division sign, U+00F7 ISOnum -->
		validEntityNames.put("oslash", 248); // latin small letter o with stroke, = latin small letter o slash, U+00F8
												// ISOlat1 -->
		validEntityNames.put("ugrave", 249); // latin small letter u with grave, U+00F9 ISOlat1 -->
		validEntityNames.put("uacute", 250); // latin small letter u with acute, U+00FA ISOlat1 -->
		validEntityNames.put("ucirc", 251); // latin small letter u with circumflex, U+00FB ISOlat1 -->
		validEntityNames.put("uuml", 252); // latin small letter u with diaeresis, U+00FC ISOlat1 -->
		validEntityNames.put("yacute", 253); // latin small letter y with acute, U+00FD ISOlat1 -->
		validEntityNames.put("thorn", 254); // latin small letter thorn, U+00FE ISOlat1 -->
		validEntityNames.put("yuml", 255); // latin small letter y with diaeresis, U+00FF ISOlat1 -->

		VALID_ENTITY_NAMES = Collections.unmodifiableMap(validEntityNames);
	}

	public static XMLEventReader createXmlReader(Reader reader) throws FactoryConfigurationError, XMLStreamException {
		XMLInputFactory inputFactory = getOrCreateInputFactory();

		// Now.. create the reader and return it
		XMLEventReader er = inputFactory.createXMLEventReader(reader);
		return er;
	}

	private static XMLInputFactory getOrCreateInputFactory() throws FactoryConfigurationError {
		if (ourInputFactory == null) {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			if (!ourHaveLoggedStaxImplementation) {
				logStaxImplementation(inputFactory.getClass());
			}

			/*
			 * In the following few lines, you can uncomment the first and comment the second to disable automatic
			 * parsing of extended entities, e.g. &sect;
			 * 
			 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
			 * being used (e.g. glassfish) so we don't set them there.
			 */

			if (inputFactory instanceof com.ctc.wstx.stax.WstxInputFactory) {
				// inputFactory.setProperty(WstxInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
				inputFactory.setProperty(WstxInputProperties.P_UNDECLARED_ENTITY_RESOLVER, XML_RESOLVER);
			}
			ourInputFactory = inputFactory;
		}
		return ourInputFactory;
	}

	private static void logStaxImplementation(Class<?> theClass) {
		try {
			URL rootUrl = getRootUrlForClass(theClass);
			if (rootUrl == null) {
				ourLog.info("Unable to determine location of StAX implementation containing class");
			} else {
				Manifest manifest;
				URL metaInfUrl = new URL(rootUrl, "META-INF/MANIFEST.MF");
				InputStream is = metaInfUrl.openStream();
				try {
					manifest = new Manifest(is);
				} finally {
					is.close();
				}
				Attributes attrs = manifest.getMainAttributes();
				String title = attrs.getValue(IMPLEMENTATION_TITLE);
				String symbolicName = attrs.getValue(BUNDLE_SYMBOLIC_NAME);
				if (symbolicName != null) {
					int i = symbolicName.indexOf(';');
					if (i != -1) {
						symbolicName = symbolicName.substring(0, i);
					}
				}
				String vendor = attrs.getValue(IMPLEMENTATION_VENDOR);
				if (vendor == null) {
					vendor = attrs.getValue(BUNDLE_VENDOR);
				}
				String version = attrs.getValue(IMPLEMENTATION_VERSION);
				if (version == null) {
					version = attrs.getValue(BUNDLE_VERSION);
				}
				if (ourLog.isDebugEnabled()) {
					ourLog.debug("FHIR XML procesing will use StAX implementation at {}\n  Title:         {}\n  Symbolic name: {}\n  Vendor:        {}\n  Version:       {}", rootUrl, title, symbolicName, vendor, version);
				} else {
					ourLog.info("FHIR XML procesing will use StAX implementation '{}' version '{}'", title, version);
				}
			}
		} catch (Throwable e) {
			ourLog.info("Unable to determine StAX implementation: " + e.getMessage());
		} finally {
			ourHaveLoggedStaxImplementation = true;
		}
	}

	private static URL getRootUrlForClass(Class<?> cls) {
		ClassLoader classLoader = cls.getClassLoader();
		String resource = cls.getName().replace('.', '/') + ".class";
		if (classLoader == null) {
			// A null class loader means the bootstrap class loader. In this case we use the
			// system class loader. This is safe since we can assume that the system class
			// loader uses parent first as delegation policy.
			classLoader = ClassLoader.getSystemClassLoader();
		}
		URL url = classLoader.getResource(resource);
		if (url == null) {
			return null;
		}
		String file = url.getFile();
		if (file.endsWith(resource)) {
			try {
				return new URL(url.getProtocol(), url.getHost(), url.getPort(), file.substring(0, file.length() - resource.length()));
			} catch (MalformedURLException ex) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static XMLStreamWriter createXmlStreamWriter(Writer theWriter) throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		XMLStreamWriter retVal = outputFactory.createXMLStreamWriter(theWriter);
		return retVal;
	}

	public static XMLEventWriter createXmlWriter(Writer theWriter) throws FactoryConfigurationError, XMLStreamException {
		XMLOutputFactory outputFactory = getOrCreateOutputFactory();
		XMLEventWriter retVal = outputFactory.createXMLEventWriter(theWriter);
		return retVal;
	}

	public static void main(String[] args) {
		System.out.println(Character.toString((char) 167));
	}

	private static XMLOutputFactory getOrCreateOutputFactory() throws FactoryConfigurationError {
		if (ourOutputFactory == null) {
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

			if (!ourHaveLoggedStaxImplementation) {
				logStaxImplementation(outputFactory.getClass());
			}

			/*
			 * Note that these properties are Woodstox specific and they cause a crash in environments where SJSXP is
			 * being used (e.g. glassfish) so we don't set them there.
			 */
			if (outputFactory instanceof WstxOutputFactory) {
				outputFactory.setProperty(XMLOutputFactory2.P_TEXT_ESCAPER, new MyEscaper());
			}
			ourOutputFactory = outputFactory;
		}
		return ourOutputFactory;
	}

	public static class MyEscaper implements EscapingWriterFactory {

		@Override
		public Writer createEscapingWriterFor(OutputStream theOut, String theEnc) throws UnsupportedEncodingException {
			return createEscapingWriterFor(new OutputStreamWriter(theOut), theEnc);
		}

		@Override
		public Writer createEscapingWriterFor(final Writer theW, String theEnc) throws UnsupportedEncodingException {
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

	private static final class ExtendedEntityReplacingXmlResolver implements XMLResolver {
		@Override
		public Object resolveEntity(String thePublicID, String theSystemID, String theBaseURI, String theNamespace) throws XMLStreamException {
			if (thePublicID == null && theSystemID == null) {
				if (theNamespace != null && VALID_ENTITY_NAMES.containsKey(theNamespace)) {
					return new String(Character.toChars(VALID_ENTITY_NAMES.get(theNamespace)));
				}
			}

			return null;
		}
	}

}
