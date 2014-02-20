package ca.uhn.fhir.parser;

import java.io.StringReader;
import java.util.Iterator;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;

public class XmlParser {
	private static final String FHIR_NS = "http://hl7.org/fhir";
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	private FhirContext myContext;
	private XMLInputFactory myXmlInputFactory;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
	}

	public IResource parseResource(String theXml) throws ConfigurationException, DataFormatException {
		XMLEventReader streamReader;
		try {
			streamReader = myXmlInputFactory.createXMLEventReader(new StringReader(theXml));
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		try {
			ParserState parserState = null;
			while (streamReader.hasNext()) {
				XMLEvent nextEvent = streamReader.nextEvent();
				if (nextEvent.isStartElement()) {
					StartElement elem = nextEvent.asStartElement();
					if (!FHIR_NS.equals(elem.getName().getNamespaceURI())) {
						continue;
					}

					if (parserState == null) {
						parserState = ParserState.getResourceInstance(myContext, elem.getName().getLocalPart());
					} else {
						parserState.enteringNewElement(elem.getName().getLocalPart());
					}

					for (@SuppressWarnings("unchecked")
					Iterator<Attribute> iter = elem.getAttributes(); iter.hasNext();) {
						Attribute next = iter.next();
						if (next.getName().getLocalPart().equals("value")) {
							parserState.attributeValue(next.getValue());
						}
					}

				} else if (nextEvent.isAttribute()) {
					Attribute elem = (Attribute) nextEvent;
					if (!FHIR_NS.equals(elem.getName().getNamespaceURI())) {
						continue;
					}
					if (!"value".equals(elem.getName().getLocalPart())) {
						continue;
					}
					if (parserState == null) {
						throw new DataFormatException("Detected attribute before element");
					}
					parserState.attributeValue(elem.getValue());
				} else if (nextEvent.isEndElement()) {
					EndElement elem = nextEvent.asEndElement();
					if (!FHIR_NS.equals(elem.getName().getNamespaceURI())) {
						continue;
					}
					if (parserState == null) {
						throw new DataFormatException("Detected unexpected end-element");
					}
					parserState.endingElement(elem.getName().getLocalPart());
					if (parserState.isComplete()) {
						return (IResource) parserState.getObject();
					}
				}

			}

			return null;
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		}
	}
}
