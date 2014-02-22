package ca.uhn.fhir.parser;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;

public class XmlParser {
	private static final String FHIR_NS = "http://hl7.org/fhir";
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	private FhirContext myContext;
	private XMLInputFactory myXmlInputFactory;
	private XMLOutputFactory myXmlOutputFactory;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
		myXmlOutputFactory = XMLOutputFactory.newInstance();
	}

	public String encodeResourceToString(IResource theResource) {
		XMLStreamWriter eventWriter;
		StringWriter stringWriter = new StringWriter();
		try {
			eventWriter = myXmlOutputFactory.createXMLStreamWriter(stringWriter);
			eventWriter = new PrettyPrintWriterWrapper(eventWriter);
			
			RuntimeResourceDefinition resDef = (RuntimeResourceDefinition) myContext.getClassToElementDefinition().get(theResource.getClass());
			eventWriter.writeStartElement(resDef.getName());
			eventWriter.writeDefaultNamespace(FHIR_NS);

			encodeCompositeElementToStreamWriter(theResource, eventWriter, resDef);

			eventWriter.writeEndElement();
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		return stringWriter.toString();
	}

	private void encodeCompositeElementToStreamWriter(IElement theResource, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws XMLStreamException {
		for (BaseRuntimeChildDefinition nextChild : resDef.getChildren()) {
			List<IElement> values = nextChild.getAccessor().getValues(theResource);
			if (values == null || values.isEmpty()) {
				continue;
			}

			for (IElement nextValue : values) {
				if (nextValue == null) {
					continue;
				}
				Class<? extends IElement> type = nextValue.getClass();
				String childName = nextChild.getChildNameByDatatype(type);
				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
				theEventWriter.writeStartElement(childName);

				switch (childDef.getChildType()) {
				case PRIMITIVE_DATATYPE: {
					IPrimitiveDatatype<?> pd = (IPrimitiveDatatype<?>) nextValue;
					theEventWriter.writeAttribute("value", pd.getValueAsString());
					break;
				}
				case RESOURCE_BLOCK:
				case COMPOSITE_DATATYPE: {
					BaseRuntimeElementCompositeDefinition<?> childCompositeDef = (BaseRuntimeElementCompositeDefinition<?>) childDef;
					encodeCompositeElementToStreamWriter(nextValue, theEventWriter, childCompositeDef);
					break;
				}
				case RESOURCE_REF: {
					ResourceReference ref = (ResourceReference) nextValue;
					encodeResourceReferenceToStreamWriter(theEventWriter, ref);
					break;
				}
				case RESOURCE:
					throw new IllegalStateException(); // should not happen
				}

				theEventWriter.writeEndElement();
			}
		}
	}

	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, ResourceReference theRef) throws XMLStreamException {
		if (StringUtils.isNotBlank(theRef.getDisplay())) {
			theEventWriter.writeStartElement("display");
			theEventWriter.writeAttribute("value", theRef.getDisplay());
			theEventWriter.writeEndElement();
		}
		if (StringUtils.isNotBlank(theRef.getReference())) {
			theEventWriter.writeStartElement("reference");
			theEventWriter.writeAttribute("value", theRef.getReference());
			theEventWriter.writeEndElement();
		}
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
					parserState.endingElement(elem);
					if (parserState.isComplete()) {
						return (IResource) parserState.getObject();
					}
				} else {
					parserState.otherEvent(nextEvent);
				}

			}

			return null;
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		}
	}
}
