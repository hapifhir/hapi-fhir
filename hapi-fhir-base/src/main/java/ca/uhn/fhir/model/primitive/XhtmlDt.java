package ca.uhn.fhir.model.primitive;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

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
	 * <b>Formatting note:</b> The text will be trimmed {@link String#trim()}. If the text does not start with
	 * an HTML tag (generally this would be a div tag), a div tag will be automatically
	 * placed surrounding the text. 
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
		
		try {
			ArrayList<XMLEvent> value = new ArrayList<XMLEvent>();
			XMLEventReader er = XMLInputFactory.newInstance().createXMLEventReader(new StringReader(val));
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
			throw new DataFormatException("String does not appear to be valid XML/XHTML", e);
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
			XMLEventWriter ew = XMLOutputFactory.newInstance().createXMLEventWriter(w);
			for (XMLEvent next : myValue) {
				ew.add(next);
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
