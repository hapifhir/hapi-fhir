package ca.uhn.fhir.model.primitive;

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
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.stax2.io.EscapingWriterFactory;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.XmlUtil;

@DatatypeDef(name = "xhtml")
public class XhtmlDt extends BasePrimitive<List<XMLEvent>> {

	private List<XMLEvent> myValue;

	/**
	 * Constructor
	 */
	public XhtmlDt() {
		// nothing
	}

	/**
	 * Constructor which accepts a string code
	 * 
	 * @see #setValueAsString(String) for a description of how this value is applied
	 */
	@SimpleSetter()
	public XhtmlDt(@SimpleSetter.Parameter(name = "theTextDiv") String theTextDiv) {
		setValueAsString(theTextDiv);
	}

	/**
	 * Accepts a textual DIV and parses it into XHTML events which are stored internally.
	 * <p>
	 * <b>Formatting note:</b> The text will be trimmed {@link String#trim()}. If the text does not start with an HTML tag (generally this would be a div tag), a div tag will be automatically placed
	 * surrounding the text.
	 * </p>
	 * <p>
	 * Also note that if the parsed text contains any entities (&foo;) which are not a part of the entities defined in core XML (e.g. &sect; which
	 * is valid in XHTML 1.0 but not in XML 1.0) they will be parsed and converted to their equivalent unicode character.
	 * </p>
	 */
	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
			return;
		}

		String val = theValue.trim();
		if (!val.startsWith("<")) {
			val = "<div>" + val + "</div>";
		}
		if (val.startsWith("<?") && val.endsWith("?>")) {
			myValue = null;
			return;
		}

		try {
			ArrayList<XMLEvent> value = new ArrayList<XMLEvent>();
			StringReader reader = new StringReader(val);
			XMLEventReader er = XmlUtil.createXmlReader(reader);
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
			setValue(value);

		} catch (XMLStreamException e) {
			throw new DataFormatException("String does not appear to be valid XML/XHTML (error is \"" + e.getMessage() + "\"): " + theValue, e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	public String getValueAsString() throws DataFormatException {
		if (myValue == null) {
			return null;
		}
		try {
			StringWriter w = new StringWriter();
			XMLEventWriter ew = XmlUtil.createXmlWriter(w);
			for (XMLEvent next : myValue) {
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

	@Override
	public List<XMLEvent> getValue() {
		return myValue;
	}

	@Override
	public void setValue(List<XMLEvent> theValue) throws DataFormatException {
		myValue = theValue;
	}

	public boolean hasContent() {
		return myValue != null && myValue.size() > 0;
	}

}
