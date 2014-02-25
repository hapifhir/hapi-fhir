package ca.uhn.fhir.parser;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceReference;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;

public class XmlParser {
	private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
	private static final String FHIR_NS = "http://hl7.org/fhir";
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	private static final String ATOM_NS = "http://www.w3.org/2005/Atom";
	private static final String OPENSEARCH_NS = "http://a9.com/-/spec/opensearch/1.1/";

	private FhirContext myContext;
	private XMLInputFactory myXmlInputFactory;
	private XMLOutputFactory myXmlOutputFactory;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
		myXmlOutputFactory = XMLOutputFactory.newInstance();
	}

	public String encodeResourceToString(IResource theResource) throws DataFormatException {
		XMLStreamWriter eventWriter;
		StringWriter stringWriter = new StringWriter();
		try {
			eventWriter = myXmlOutputFactory.createXMLStreamWriter(stringWriter);
			eventWriter = decorateStreamWriter(eventWriter);

			encodeResourceToStreamWriter(theResource, eventWriter);
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		return stringWriter.toString();
	}

	public void encodeResourceToStreamWriter(IResource theResource, XMLStreamWriter eventWriter) throws XMLStreamException, DataFormatException {
		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		eventWriter.writeStartElement(resDef.getName());
		eventWriter.writeDefaultNamespace(FHIR_NS);

		encodeCompositeElementToStreamWriter(theResource, eventWriter, resDef);

		eventWriter.writeEndElement();
		eventWriter.close();
	}

	private XMLStreamWriter decorateStreamWriter(XMLStreamWriter eventWriter) {
		PrettyPrintWriterWrapper retVal = new PrettyPrintWriterWrapper(eventWriter);
		return retVal;
	}

	private void encodeCompositeElementToStreamWriter(IElement theElement, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws XMLStreamException,
			DataFormatException {
		encodeExtensionsIfPresent(theEventWriter, theElement);
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getExtensions());
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getChildren());
	}

	private void encodeCompositeElementChildrenToStreamWriter(IElement theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> children) throws XMLStreamException,
			DataFormatException {
		for (BaseRuntimeChildDefinition nextChild : children) {
			List<? extends IElement> values = nextChild.getAccessor().getValues(theElement);
			if (values == null || values.isEmpty()) {
				continue;
			}

			for (IElement nextValue : values) {
				if (nextValue == null) {
					continue;
				}
				Class<? extends IElement> type = nextValue.getClass();
				String childName = nextChild.getChildNameByDatatype(type);
				String extensionUrl = nextChild.getExtensionUrl();
				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
				if (childDef == null) {
					throw new IllegalStateException(nextChild + " has no child of type " + type);
				}

				if (extensionUrl != null && childName.equals("extension") == false) {
					theEventWriter.writeStartElement("extension");
					theEventWriter.writeAttribute("url", extensionUrl);
					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, null);
					theEventWriter.writeEndElement();
				} else {
					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, extensionUrl);
				}
			}
		}
	}

	private void encodeChildElementToStreamWriter(XMLStreamWriter theEventWriter, IElement nextValue, String childName, BaseRuntimeElementDefinition<?> childDef, String theExtensionUrl)
			throws XMLStreamException, DataFormatException {
		switch (childDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			theEventWriter.writeStartElement(childName);
			IPrimitiveDatatype<?> pd = (IPrimitiveDatatype<?>) nextValue;
			theEventWriter.writeAttribute("value", pd.getValueAsString());
			encodeExtensionsIfPresent(theEventWriter, nextValue);
			theEventWriter.writeEndElement();
			break;
		}
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			theEventWriter.writeStartElement(childName);
			if (isNotBlank(theExtensionUrl)) {
				theEventWriter.writeAttribute("url", theExtensionUrl);
			}
			BaseRuntimeElementCompositeDefinition<?> childCompositeDef = (BaseRuntimeElementCompositeDefinition<?>) childDef;
			encodeCompositeElementToStreamWriter(nextValue, theEventWriter, childCompositeDef);
			encodeExtensionsIfPresent(theEventWriter, nextValue);
			theEventWriter.writeEndElement();
			break;
		}
		case RESOURCE_REF: {
			theEventWriter.writeStartElement(childName);
			ResourceReference ref = (ResourceReference) nextValue;
			encodeResourceReferenceToStreamWriter(theEventWriter, ref);
			theEventWriter.writeEndElement();
			break;
		}
		case RESOURCE: {
			throw new IllegalStateException(); // should not happen
		}
		case PRIMITIVE_XHTML: {
			XhtmlDt dt = (XhtmlDt) nextValue;
			encodeXhtml(dt, theEventWriter);
			break;
		}
		case UNDECL_EXT: {
			throw new IllegalStateException("should not happen");
		}
		}
	}

	private void encodeExtensionsIfPresent(XMLStreamWriter theWriter, IElement theResource) throws XMLStreamException, DataFormatException {
		if (theResource instanceof ISupportsUndeclaredExtensions) {
			for (UndeclaredExtension next : ((ISupportsUndeclaredExtensions) theResource).getUndeclaredExtensions()) {
				theWriter.writeStartElement("extension");
				theWriter.writeAttribute("url", next.getUrl());

				if (next.getValue() != null) {
					IElement nextValue = next.getValue();
					RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
					String childName = extDef.getChildNameByDatatype(nextValue.getClass());
					BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
					encodeChildElementToStreamWriter(theWriter, nextValue, childName, childDef, null);
				}

				// child extensions
				encodeExtensionsIfPresent(theWriter, next);

				theWriter.writeEndElement();
			}
		}
	}

	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		XMLStreamWriter eventWriter;
		StringWriter stringWriter = new StringWriter();
		try {
			eventWriter = myXmlOutputFactory.createXMLStreamWriter(stringWriter);
			eventWriter = decorateStreamWriter(eventWriter);

			eventWriter.writeStartElement("feed");
			eventWriter.writeDefaultNamespace(ATOM_NS);

			writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
			writeTagWithTextNode(eventWriter, "id", theBundle.getId());

			writeAtomLink(eventWriter, "self", theBundle.getLinkSelf());
			writeAtomLink(eventWriter, "first", theBundle.getLinkFirst());
			writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious());
			writeAtomLink(eventWriter, "next", theBundle.getLinkNext());
			writeAtomLink(eventWriter, "last", theBundle.getLinkLast());
			writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase());

			if (theBundle.getTotalResults() != null) {
				eventWriter.writeNamespace("os", OPENSEARCH_NS);
				eventWriter.writeStartElement(OPENSEARCH_NS, "totalResults");
				eventWriter.writeCharacters(theBundle.getTotalResults().toString());
				eventWriter.writeEndElement();
			}

			writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
			writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());

			if (StringUtils.isNotBlank(theBundle.getAuthorName())) {
				eventWriter.writeStartElement("author");
				writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
				writeOptionalTagWithTextNode(eventWriter, "device", theBundle.getAuthorDevice());
			}

			for (BundleEntry nextEntry : theBundle.getEntries()) {
				eventWriter.writeStartElement("entry");

				eventWriter.writeStartElement("content");
				eventWriter.writeAttribute("type", "text/xml");
				
				IResource resource = nextEntry.getResource();
				encodeResourceToStreamWriter(resource, eventWriter);
				
				eventWriter.writeEndElement(); // content
				
				eventWriter.writeEndElement(); // entry
			}
			
			eventWriter.writeEndElement();
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		return stringWriter.toString();
	}

	private void writeOptionalTagWithTextNode(XMLStreamWriter theEventWriter, String theTagName, Date theNodeValue) throws XMLStreamException {
		if (theNodeValue != null) {
			theEventWriter.writeStartElement(theTagName);
			theEventWriter.writeCharacters(DatatypeConverter.printDateTime(DateUtils.toCalendar(theNodeValue)));
			theEventWriter.writeEndElement();
		}
	}

	private void writeAtomLink(XMLStreamWriter theEventWriter, String theRel, String theLink) throws XMLStreamException {
		if (StringUtils.isNotBlank(theLink)) {
			theEventWriter.writeStartElement("link");
			theEventWriter.writeAttribute("rel", theRel);
			theEventWriter.writeAttribute("href", theLink);
		}
	}

	private void writeTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, String theTextValue) throws XMLStreamException {
		theEventWriter.writeStartElement(theElementName);
		if (StringUtils.isNotBlank(theTextValue)) {
			theEventWriter.writeCharacters(theTextValue);
		}
		theEventWriter.writeEndElement();
	}

	private void writeOptionalTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, String theTextValue) throws XMLStreamException {
		if (StringUtils.isNotBlank(theTextValue)) {
			theEventWriter.writeStartElement(theElementName);
			theEventWriter.writeCharacters(theTextValue);
			theEventWriter.writeEndElement();
		}
	}

	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
		if (theDt == null || theDt.getValue() == null) {
			return;
		}

		boolean firstEvent = true;
		for (XMLEvent event : theDt.getValue()) {
			switch (event.getEventType()) {
			case XMLStreamConstants.ATTRIBUTE:
				Attribute attr = (Attribute) event;
				if (isBlank(attr.getName().getPrefix())) {
					if (isBlank(attr.getName().getNamespaceURI())) {
						theEventWriter.writeAttribute(attr.getName().getLocalPart(), attr.getValue());
					} else {
						theEventWriter.writeAttribute(attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
					}
				} else {
					theEventWriter.writeAttribute(attr.getName().getPrefix(), attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
				}

				break;
			case XMLStreamConstants.CDATA:
				theEventWriter.writeCData(((Characters) event).getData());
				break;
			case XMLStreamConstants.CHARACTERS:
			case XMLStreamConstants.SPACE:
				theEventWriter.writeCharacters(((Characters) event).getData());
				break;
			case XMLStreamConstants.COMMENT:
				theEventWriter.writeComment(((Comment) event).getText());
				break;
			case XMLStreamConstants.END_ELEMENT:
				theEventWriter.writeEndElement();
				break;
			case XMLStreamConstants.ENTITY_REFERENCE:
				EntityReference er = (EntityReference) event;
				theEventWriter.writeEntityRef(er.getName());
				break;
			case XMLStreamConstants.NAMESPACE:
				Namespace ns = (Namespace) event;
				theEventWriter.writeNamespace(ns.getPrefix(), ns.getNamespaceURI());
				break;
			case XMLStreamConstants.START_ELEMENT:
				StartElement se = event.asStartElement();
				if (firstEvent) {
					theEventWriter.writeStartElement(se.getName().getLocalPart());
					theEventWriter.writeNamespace(se.getName().getPrefix(), se.getName().getNamespaceURI());
				} else {
					if (isBlank(se.getName().getPrefix())) {
						if (isBlank(se.getName().getNamespaceURI())) {
							theEventWriter.writeStartElement(se.getName().getLocalPart());
						} else {
							theEventWriter.writeStartElement(se.getName().getNamespaceURI(), se.getName().getLocalPart());
						}
					} else {
						theEventWriter.writeStartElement(se.getName().getPrefix(), se.getName().getLocalPart(), se.getName().getNamespaceURI());
					}
				}
				break;
			case XMLStreamConstants.DTD:
			case XMLStreamConstants.END_DOCUMENT:
			case XMLStreamConstants.ENTITY_DECLARATION:
			case XMLStreamConstants.NOTATION_DECLARATION:
			case XMLStreamConstants.PROCESSING_INSTRUCTION:
			case XMLStreamConstants.START_DOCUMENT:
				break;
			}

			firstEvent = false;
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

					String namespaceURI = elem.getName().getNamespaceURI();
					if (!FHIR_NS.equals(namespaceURI) && !XHTML_NS.equals(namespaceURI)) {
						continue;
					}

					if (parserState == null) {
						parserState = ParserState.getResourceInstance(myContext, elem.getName().getLocalPart());
					} else if ("extension".equals(elem.getName().getLocalPart())) {
						Attribute urlAttr = elem.getAttributeByName(new QName("url"));
						if (urlAttr == null || isBlank(urlAttr.getValue())) {
							throw new DataFormatException("Extension element has no 'url' attribute");
						}
						parserState.enteringNewElementExtension(elem, urlAttr.getValue());
					} else {
						parserState.enteringNewElement(elem, elem.getName().getLocalPart());
					}

					for (@SuppressWarnings("unchecked")
					Iterator<Attribute> iter = elem.getAttributes(); iter.hasNext();) {
						Attribute next = iter.next();
						if (next.getName().getLocalPart().equals("value")) {
							parserState.attributeValue(next, next.getValue());
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
					parserState.attributeValue(elem, elem.getValue());
				} else if (nextEvent.isEndElement()) {
					EndElement elem = nextEvent.asEndElement();
					String namespaceURI = elem.getName().getNamespaceURI();
					if (!FHIR_NS.equals(namespaceURI) && !XHTML_NS.equals(namespaceURI)) {
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
					if (parserState != null) {
						parserState.otherEvent(nextEvent);
					}
				}

			}

			return null;
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		}
	}
}
