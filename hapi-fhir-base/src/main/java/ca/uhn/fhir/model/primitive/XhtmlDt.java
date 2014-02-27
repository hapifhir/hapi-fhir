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
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "xhtml")
public class XhtmlDt implements IPrimitiveDatatype<List<XMLEvent>> {

	private List<XMLEvent> myValue;

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
			return;
		}
		String val = "<a>" + theValue + "</a>";
		try {
			ArrayList<XMLEvent> value = new ArrayList<XMLEvent>();
			XMLEventReader er = XMLInputFactory.newInstance().createXMLEventReader(new StringReader(val));
			boolean first = true;
			while (er.hasNext()) {
				if (first) {
					first = false;
					continue;
				}
				XMLEvent next = er.nextEvent();
				if (er.hasNext()) {
					// don't add the last event
					value.add(next);
				}
			}

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
