package ca.uhn.fhir.parser;

import java.io.StringReader;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;

public class XmlParser {
	private static final String FHIR_NS = "http://hl7.org/fhir";

	private FhirContext myContext;
	private XMLInputFactory myXmlInputFactory;
	private XMLEventFactory myEventFactory;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
		myEventFactory = XMLEventFactory.newInstance();
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
				}
				

			}
			
			return null;
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		}
	}
}
