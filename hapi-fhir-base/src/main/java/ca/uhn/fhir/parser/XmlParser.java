package ca.uhn.fhir.parser;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;

public class XmlParser implements IParser {
	static final String ATOM_NS = "http://www.w3.org/2005/Atom";
	static final String FHIR_NS = "http://hl7.org/fhir";
	static final String OPENSEARCH_NS = "http://a9.com/-/spec/opensearch/1.1/";
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	// private static final Set<String> RESOURCE_NAMESPACES;

	private FhirContext myContext;
	private XMLInputFactory myXmlInputFactory;
	private XMLOutputFactory myXmlOutputFactory;
	private boolean myPrettyPrint;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
		myXmlOutputFactory = XMLOutputFactory.newInstance();
	}

	private XMLStreamWriter decorateStreamWriter(XMLStreamWriter eventWriter) {
		if (myPrettyPrint) {
			PrettyPrintWriterWrapper retVal = new PrettyPrintWriterWrapper(eventWriter);
			return retVal;
		} else {
			return eventWriter;
		}
	}

	private <T extends IElement> T doXmlLoop(XMLEventReader streamReader, ParserState<T> parserState) {
		try {
			while (streamReader.hasNext()) {
				XMLEvent nextEvent = streamReader.nextEvent();
				try {
					if (nextEvent.isStartElement()) {
						StartElement elem = nextEvent.asStartElement();

						String namespaceURI = elem.getName().getNamespaceURI();

						if ("extension".equals(elem.getName().getLocalPart())) {
							Attribute urlAttr = elem.getAttributeByName(new QName("url"));
							if (urlAttr == null || isBlank(urlAttr.getValue())) {
								throw new DataFormatException("Extension element has no 'url' attribute");
							}
							parserState.enteringNewElementExtension(elem, urlAttr.getValue(), false);
						} else if ("modifierExtension".equals(elem.getName().getLocalPart())) {
							Attribute urlAttr = elem.getAttributeByName(new QName("url"));
							if (urlAttr == null || isBlank(urlAttr.getValue())) {
								throw new DataFormatException("Extension element has no 'url' attribute");
							}
							parserState.enteringNewElementExtension(elem, urlAttr.getValue(), true);

						} else {

							String elementName = elem.getName().getLocalPart();
							parserState.enteringNewElement(namespaceURI, elementName);

						}

						for (@SuppressWarnings("unchecked")
						Iterator<Attribute> iter = elem.getAttributes(); iter.hasNext();) {
							Attribute next = iter.next();
							// if
							// (next.getName().getLocalPart().equals("value")) {
							parserState.attributeValue(next.getName().getLocalPart(), next.getValue());
							// }
						}

					} else if (nextEvent.isAttribute()) {
						Attribute elem = (Attribute) nextEvent;
						String name = (elem.getName().getLocalPart());
						parserState.attributeValue(name, elem.getValue());
					} else if (nextEvent.isEndElement()) {
						EndElement elem = nextEvent.asEndElement();

						String name = elem.getName().getLocalPart();
						String namespaceURI = elem.getName().getNamespaceURI();
						// if (!FHIR_NS.equals(namespaceURI) &&
						// !XHTML_NS.equals(namespaceURI)) {
						// continue;
						// }

						parserState.endingElement(elem);
						if (parserState.isComplete()) {
							return parserState.getObject();
						}
					} else if (nextEvent.isCharacters()) {
						parserState.string(nextEvent.asCharacters().getData());
					}

					parserState.xmlEvent(nextEvent);

				} catch (DataFormatException e) {
					throw new DataFormatException("DataFormatException at [" + nextEvent.getLocation().toString() + "]: " + e.getMessage(), e);
				}
			}
			return null;
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		}
	}

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		StringWriter stringWriter = new StringWriter();
		encodeBundleToWriter(theBundle, stringWriter);

		return stringWriter.toString();
	}

	@Override
	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) {
		try {
			XMLStreamWriter eventWriter;
			eventWriter = myXmlOutputFactory.createXMLStreamWriter(theWriter);
			eventWriter = decorateStreamWriter(eventWriter);

			eventWriter.writeStartElement("feed");
			eventWriter.writeDefaultNamespace(ATOM_NS);

			writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
			writeTagWithTextNode(eventWriter, "id", theBundle.getBundleId());

			writeAtomLink(eventWriter, "self", theBundle.getLinkSelf());
			writeAtomLink(eventWriter, "first", theBundle.getLinkFirst());
			writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious());
			writeAtomLink(eventWriter, "next", theBundle.getLinkNext());
			writeAtomLink(eventWriter, "last", theBundle.getLinkLast());
			writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase());

			if (theBundle.getTotalResults().getValue() != null) {
				eventWriter.writeStartElement("os", "totalResults",OPENSEARCH_NS);
				eventWriter.writeNamespace("os", OPENSEARCH_NS);
				eventWriter.writeCharacters(theBundle.getTotalResults().getValue().toString());
				eventWriter.writeEndElement();
			}

			writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
			writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());

			if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
				eventWriter.writeStartElement("author");
				writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
				writeOptionalTagWithTextNode(eventWriter, "uri", theBundle.getAuthorUri());
				eventWriter.writeEndElement();
			}

			for (BundleEntry nextEntry : theBundle.getEntries()) {
				eventWriter.writeStartElement("entry");

				eventWriter.writeStartElement("content");
				eventWriter.writeAttribute("type", "text/xml");

				IResource resource = nextEntry.getResource();
				encodeResourceToXmlStreamWriter(resource, eventWriter);

				eventWriter.writeEndElement(); // content
				eventWriter.writeEndElement(); // entry
			}

			eventWriter.writeEndElement();
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	private void encodeChildElementToStreamWriter(XMLStreamWriter theEventWriter, IElement nextValue, String childName, BaseRuntimeElementDefinition<?> childDef, String theExtensionUrl) throws XMLStreamException, DataFormatException {
		if (nextValue.isEmpty()) {
			return;
		}

		switch (childDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			IPrimitiveDatatype<?> pd = (IPrimitiveDatatype<?>) nextValue;
			String value = pd.getValueAsString();
			if (value != null) {
				theEventWriter.writeStartElement(childName);
				theEventWriter.writeAttribute("value", value);
				encodeExtensionsIfPresent(theEventWriter, nextValue);
				theEventWriter.writeEndElement();
			}
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
			ResourceReferenceDt ref = (ResourceReferenceDt) nextValue;
			if (!ref.isEmpty()) {
				theEventWriter.writeStartElement(childName);
				encodeResourceReferenceToStreamWriter(theEventWriter, ref);
				theEventWriter.writeEndElement();
			}
			break;
		}
		case RESOURCE: {
			throw new IllegalStateException(); // should not happen
		}
		case PRIMITIVE_XHTML: {
			XhtmlDt dt = (XhtmlDt) nextValue;
			if (dt.hasContent()) {
				encodeXhtml(dt, theEventWriter);
			}
			break;
		}
		case UNDECL_EXT: {
			throw new IllegalStateException("should not happen");
		}
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(IElement theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> children) throws XMLStreamException, DataFormatException {
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
					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
					if (extDef.isModifier()) {
						theEventWriter.writeStartElement("modifierExtension");
					} else {
						theEventWriter.writeStartElement("extension");
					}

					theEventWriter.writeAttribute("url", extensionUrl);
					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, null);
					theEventWriter.writeEndElement();
				} else {
					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, extensionUrl);
				}
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(IElement theElement, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws XMLStreamException, DataFormatException {
		encodeExtensionsIfPresent(theEventWriter, theElement);
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getExtensions());
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getChildren());
	}

	private void encodeExtensionsIfPresent(XMLStreamWriter theWriter, IElement theResource) throws XMLStreamException, DataFormatException {
		if (theResource instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theResource;
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredExtensions(), "extension");
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredModifierExtensions(), "modifierExtension");
		}
	}

	private void encodeUndeclaredExtensions(XMLStreamWriter theWriter, List<UndeclaredExtension> extensions, String tagName) throws XMLStreamException {
		for (UndeclaredExtension next : extensions) {
			theWriter.writeStartElement(tagName);
			theWriter.writeAttribute("url", next.getUrl());

			if (next.getValue() != null) {
				IElement nextValue = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(nextValue.getClass());
				if (childName==null) {
					throw new ConfigurationException("Unable to encode extension, unregognized child element type: " + nextValue.getClass().getCanonicalName());
				}
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
				encodeChildElementToStreamWriter(theWriter, nextValue, childName, childDef, null);
			}

			// child extensions
			encodeExtensionsIfPresent(theWriter, next);

			theWriter.writeEndElement();
		}
	}

	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, ResourceReferenceDt theRef) throws XMLStreamException {
		if (!(theRef.getDisplay().isEmpty())) {
			theEventWriter.writeStartElement("display");
			theEventWriter.writeAttribute("value", theRef.getDisplay().getValue());
			theEventWriter.writeEndElement();
		}
		if (!(theRef.getReference().isEmpty())) {
			theEventWriter.writeStartElement("reference");
			theEventWriter.writeAttribute("value", theRef.getReference().getValue());
			theEventWriter.writeEndElement();
		}
	}

	@Override
	public String encodeResourceToString(IResource theResource) throws DataFormatException {
		Writer stringWriter = new StringWriter();
		encodeResourceToWriter(theResource, stringWriter);
		return stringWriter.toString();
	}

	@Override
	public void encodeResourceToWriter(IResource theResource, Writer stringWriter) {
		XMLStreamWriter eventWriter;
		try {
			eventWriter = myXmlOutputFactory.createXMLStreamWriter(stringWriter);
			eventWriter = decorateStreamWriter(eventWriter);

			encodeResourceToXmlStreamWriter(theResource, eventWriter);
			eventWriter.flush();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	private void encodeResourceToXmlStreamWriter(IResource theResource, XMLStreamWriter eventWriter) throws XMLStreamException, DataFormatException {
		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		eventWriter.writeStartElement(resDef.getName());
		eventWriter.writeDefaultNamespace(FHIR_NS);

		encodeCompositeElementToStreamWriter(theResource, eventWriter, resDef);

		eventWriter.writeEndElement();
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
					if (StringUtils.isBlank(se.getName().getPrefix())) {
						theEventWriter.writeDefaultNamespace(se.getName().getNamespaceURI());
					} else {
						theEventWriter.writeNamespace(se.getName().getPrefix(), se.getName().getNamespaceURI());
					}
				} else {
					if (isBlank(se.getName().getPrefix())) {
						if (isBlank(se.getName().getNamespaceURI())) {
							theEventWriter.writeStartElement(se.getName().getLocalPart());
						} else {
							if (StringUtils.isBlank(se.getName().getPrefix())) {
								theEventWriter.writeStartElement(se.getName().getLocalPart());
								theEventWriter.writeDefaultNamespace(se.getName().getNamespaceURI());
							} else {
								theEventWriter.writeStartElement(se.getName().getNamespaceURI(), se.getName().getLocalPart());
							}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.parser.IParser#parseBundle(java.lang.String)
	 */
	@Override
	public Bundle parseBundle(String theXml) throws ConfigurationException, DataFormatException {
		StringReader reader = new StringReader(theXml);
		return parseBundle(reader);
	}

	private Bundle parseBundle(XMLEventReader theStreamReader) {
		ParserState<Bundle> parserState = ParserState.getPreAtomInstance(myContext);
		return doXmlLoop(theStreamReader, parserState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.uhn.fhir.parser.IParser#parseResource(java.lang.String)
	 */
	@Override
	public IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
		return parseResource(null, theMessageString);
	}

	private IResource parseResource(XMLEventReader theStreamReader) {
		return parseResource(null, theStreamReader);
	}

	private IResource parseResource(Class<? extends IResource> theResourceType, XMLEventReader theStreamReader) {
		ParserState<IResource> parserState = ParserState.getPreResourceInstance(theResourceType, myContext);
		return doXmlLoop(theStreamReader, parserState);
	}

	private void writeAtomLink(XMLStreamWriter theEventWriter, String theRel, StringDt theStringDt) throws XMLStreamException {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.writeStartElement("link");
			theEventWriter.writeAttribute("rel", theRel);
			theEventWriter.writeAttribute("href", theStringDt.getValue());
		}
	}

	private void writeOptionalTagWithTextNode(XMLStreamWriter theEventWriter, String theTagName, InstantDt theInstantDt) throws XMLStreamException {
		if (theInstantDt.getValue() != null) {
			theEventWriter.writeStartElement(theTagName);
			theEventWriter.writeCharacters(theInstantDt.getValueAsString());
			theEventWriter.writeEndElement();
		}
	}

	private void writeOptionalTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, StringDt theTextValue) throws XMLStreamException {
		if (StringUtils.isNotBlank(theTextValue.getValue())) {
			theEventWriter.writeStartElement(theElementName);
			theEventWriter.writeCharacters(theTextValue.getValue());
			theEventWriter.writeEndElement();
		}
	}

	private void writeTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, StringDt theStringDt) throws XMLStreamException {
		theEventWriter.writeStartElement(theElementName);
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.writeCharacters(theStringDt.getValue());
		}
		theEventWriter.writeEndElement();
	}

	@Override
	public Bundle parseBundle(Reader theReader) {
		XMLEventReader streamReader;
		try {
			streamReader = myXmlInputFactory.createXMLEventReader(theReader);
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		return parseBundle(streamReader);
	}

	@Override
	public IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
		return parseResource(null, theReader);
	}

	@Override
	public IResource parseResource(Class<? extends IResource> theResourceType, Reader theReader) {
		XMLEventReader streamReader;
		try {
			streamReader = myXmlInputFactory.createXMLEventReader(theReader);
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}

		return parseResource(theResourceType, streamReader);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		StringReader reader = new StringReader(theMessageString);
		return (T) parseResource(theResourceType, reader);
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}
}
