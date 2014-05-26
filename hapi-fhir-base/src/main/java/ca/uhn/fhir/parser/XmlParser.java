package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR Library
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

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
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
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.util.NonPrettyPrintWriterWrapper;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;

public class XmlParser extends BaseParser implements IParser {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	static final String ATOM_NS = "http://www.w3.org/2005/Atom";
	static final String FHIR_NS = "http://hl7.org/fhir";
	static final String OPENSEARCH_NS = "http://a9.com/-/spec/opensearch/1.1/";
	static final String TOMBSTONES_NS = "http://purl.org/atompub/tombstones/1.0";
	static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	// private static final Set<String> RESOURCE_NAMESPACES;

	private FhirContext myContext;
	private boolean myPrettyPrint;
	private XMLInputFactory myXmlInputFactory;
	private XMLOutputFactory myXmlOutputFactory;

	public XmlParser(FhirContext theContext) {
		myContext = theContext;
		myXmlInputFactory = XMLInputFactory.newInstance();
		myXmlOutputFactory = XMLOutputFactory.newInstance();
	}

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		StringWriter stringWriter = new StringWriter();
		encodeBundleToWriter(theBundle, stringWriter);

		return stringWriter.toString();
	}

	@Override
	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws DataFormatException {
		try {
			XMLStreamWriter eventWriter = createXmlWriter(theWriter);

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
				eventWriter.writeStartElement("os", "totalResults", OPENSEARCH_NS);
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
				boolean deleted=false;
				if (nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty()==false) {
					deleted=true;
					eventWriter.writeStartElement("at","deleted-entry",TOMBSTONES_NS);
					eventWriter.writeNamespace("at", TOMBSTONES_NS);
					eventWriter.writeAttribute("ref", nextEntry.getId().getValueAsString());
					eventWriter.writeAttribute("when", nextEntry.getDeletedAt().getValueAsString());
				} else {
					eventWriter.writeStartElement("entry");
				}

				writeOptionalTagWithTextNode(eventWriter, "title", nextEntry.getTitle());
				if (!deleted) {
					writeTagWithTextNode(eventWriter, "id", nextEntry.getId());
				}
				writeOptionalTagWithTextNode(eventWriter, "updated", nextEntry.getUpdated());
				writeOptionalTagWithTextNode(eventWriter, "published", nextEntry.getPublished());

				if (nextEntry.getCategories() != null) {
					for (Tag next : nextEntry.getCategories()) {
						eventWriter.writeStartElement("category");
						eventWriter.writeAttribute("term", defaultString(next.getTerm()));
						eventWriter.writeAttribute("label", defaultString(next.getLabel()));
						eventWriter.writeAttribute("scheme", defaultString(next.getScheme()));
						eventWriter.writeEndElement();
					}
				}

				if (!nextEntry.getLinkSelf().isEmpty()) {
					writeAtomLink(eventWriter, "self", nextEntry.getLinkSelf());
				}

				if (!nextEntry.getLinkAlternate().isEmpty()) {
					writeAtomLink(eventWriter, "alternate", nextEntry.getLinkAlternate());
				}

				IResource resource = nextEntry.getResource();
				if (resource != null && !resource.isEmpty()) {
					eventWriter.writeStartElement("content");
					eventWriter.writeAttribute("type", "text/xml");
					encodeResourceToXmlStreamWriter(resource, eventWriter, false);
					eventWriter.writeEndElement(); // content
				} else {
					ourLog.debug("Bundle entry contains null resource");
				}

				eventWriter.writeEndElement(); // entry
			}

			eventWriter.writeEndElement();
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	@Override
	public String encodeResourceToString(IResource theResource) throws DataFormatException {
		if (theResource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		Writer stringWriter = new StringWriter();
		encodeResourceToWriter(theResource, stringWriter);
		return stringWriter.toString();
	}

	@Override
	public void encodeResourceToWriter(IResource theResource, Writer theWriter) throws DataFormatException {
		XMLStreamWriter eventWriter;
		try {
			eventWriter = createXmlWriter(theWriter);

			encodeResourceToXmlStreamWriter(theResource, eventWriter, false);
			eventWriter.flush();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	private XMLStreamWriter createXmlWriter(Writer theWriter) throws XMLStreamException {
		XMLStreamWriter eventWriter;
		eventWriter = myXmlOutputFactory.createXMLStreamWriter(theWriter);
		eventWriter = decorateStreamWriter(eventWriter);
		return eventWriter;
	}

	@Override
	public void encodeTagListToWriter(TagList theTagList, Writer theWriter) throws IOException {
		try {
			XMLStreamWriter eventWriter = createXmlWriter(theWriter);

			eventWriter.writeStartElement(TagList.ELEMENT_NAME_LC);
			eventWriter.writeDefaultNamespace(FHIR_NS);

			for (Tag next : theTagList) {
				eventWriter.writeStartElement(TagList.ATTR_CATEGORY);

				if (isNotBlank(next.getTerm())) {
					eventWriter.writeAttribute(Tag.ATTR_TERM, next.getTerm());
				}
				if (isNotBlank(next.getLabel())) {
					eventWriter.writeAttribute(Tag.ATTR_LABEL, next.getLabel());
				}
				if (isNotBlank(next.getScheme())) {
					eventWriter.writeAttribute(Tag.ATTR_SCHEME, next.getScheme());
				}

				eventWriter.writeEndElement();
			}

			eventWriter.writeEndElement();
			eventWriter.close();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	@Override
	public <T extends IResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		return parseBundle(streamReader, theResourceType);
	}

	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		return parseResource(theResourceType, streamReader);
	}

	@Override
	public TagList parseTagList(Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		ParserState<TagList> parserState = ParserState.getPreTagListInstance(myContext, false);
		return doXmlLoop(streamReader, parserState);
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	private XMLEventReader createStreamReader(Reader theReader) {
		XMLEventReader streamReader;
		try {
			streamReader = myXmlInputFactory.createXMLEventReader(theReader);
		} catch (XMLStreamException e) {
			throw new DataFormatException(e);
		} catch (FactoryConfigurationError e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
		return streamReader;
	}

	private XMLStreamWriter decorateStreamWriter(XMLStreamWriter eventWriter) {
		if (myPrettyPrint) {
			PrettyPrintWriterWrapper retVal = new PrettyPrintWriterWrapper(eventWriter);
			return retVal;
		} else {
			NonPrettyPrintWriterWrapper retVal = new NonPrettyPrintWriterWrapper(eventWriter);
			return retVal;
		}
	}

	private <T> T doXmlLoop(XMLEventReader streamReader, ParserState<T> parserState) {
		ourLog.trace("Entering XML parsing loop with state: {}", parserState);

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
						parserState.endingElement();
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

	private void encodeChildElementToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, XMLStreamWriter theEventWriter, IElement nextValue, String childName, BaseRuntimeElementDefinition<?> childDef, String theExtensionUrl, boolean theIncludedResource)
			throws XMLStreamException, DataFormatException {
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
				encodeExtensionsIfPresent(theResDef, theResource, theEventWriter, nextValue, theIncludedResource);
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
			encodeCompositeElementToStreamWriter(theResDef, theResource, nextValue, theEventWriter, childCompositeDef, theIncludedResource);
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
		case CONTAINED_RESOURCES: {
			ContainedDt value = (ContainedDt) nextValue;
			theEventWriter.writeStartElement("contained");
			for (IResource next : value.getContainedResources()) {
				encodeResourceToXmlStreamWriter(next, theEventWriter, true);
			}
			theEventWriter.writeEndElement();
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
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + childDef.getName());
		}
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, IElement theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> children, boolean theIncludedResource)
			throws XMLStreamException, DataFormatException {
		for (BaseRuntimeChildDefinition nextChild : children) {
			if (nextChild instanceof RuntimeChildNarrativeDefinition && !theIncludedResource) {
				INarrativeGenerator gen = myContext.getNarrativeGenerator();
				if (gen != null) {
					NarrativeDt narr = gen.generateNarrative(theResDef.getResourceProfile(), theResource);
					if (narr != null) {
						RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
						String childName = nextChild.getChildNameByDatatype(child.getDatatype());
						BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, narr, childName, type, null, theIncludedResource);
						continue;
					}
				}
			}

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
					super.throwExceptionForUnknownChildType(nextChild, type);
				}

				if (extensionUrl != null && childName.equals("extension") == false) {
					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
					if (extDef.isModifier()) {
						theEventWriter.writeStartElement("modifierExtension");
					} else {
						theEventWriter.writeStartElement("extension");
					}

					theEventWriter.writeAttribute("url", extensionUrl);
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childName, childDef, null, theIncludedResource);
					theEventWriter.writeEndElement();
				} else {
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childName, childDef, extensionUrl, theIncludedResource);
				}
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, IElement theElement, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef, boolean theIncludedResource) throws XMLStreamException,
			DataFormatException {
		encodeExtensionsIfPresent(theResDef, theResource, theEventWriter, theElement, theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theElement, theEventWriter, resDef.getExtensions(), theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theElement, theEventWriter, resDef.getChildren(), theIncludedResource);
	}

	private void encodeExtensionsIfPresent(RuntimeResourceDefinition theResDef, IResource theResource, XMLStreamWriter theWriter, IElement theElement, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theElement;
			encodeUndeclaredExtensions(theResDef, theResource, theWriter, res.getUndeclaredExtensions(), "extension", theIncludedResource);
			encodeUndeclaredExtensions(theResDef, theResource, theWriter, res.getUndeclaredModifierExtensions(), "modifierExtension", theIncludedResource);
		}
	}

	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, ResourceReferenceDt theRef) throws XMLStreamException {
		String reference = theRef.getReference().getValue();
//		if (StringUtils.isBlank(reference)) {
//			if (theRef.getResourceType() != null && StringUtils.isNotBlank(theRef.getResourceId())) {
//				reference = myContext.getResourceDefinition(theRef.getResourceType()).getName() + '/' + theRef.getResourceId();
//			}
//		}

		if (!(theRef.getDisplay().isEmpty())) {
			theEventWriter.writeStartElement("display");
			theEventWriter.writeAttribute("value", theRef.getDisplay().getValue());
			theEventWriter.writeEndElement();
		}
		if (StringUtils.isNotBlank(reference)) {
			theEventWriter.writeStartElement("reference");
			theEventWriter.writeAttribute("value", reference);
			theEventWriter.writeEndElement();
		}
	}

	/**
	 * @param theIncludedResource
	 *            Set to true only if this resource is an "included" resource,
	 *            as opposed to a "root level" resource by itself or in a bundle
	 *            entry
	 * 
	 */
	private void encodeResourceToXmlStreamWriter(IResource theResource, XMLStreamWriter theEventWriter, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		super.containResourcesForEncoding(theResource);

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		if (resDef == null) {
			throw new ConfigurationException("Unknown resource type: " + theResource.getClass());
		}

		theEventWriter.writeStartElement(resDef.getName());
		theEventWriter.writeDefaultNamespace(FHIR_NS);

		if (theIncludedResource && StringUtils.isNotBlank(theResource.getId().getValue())) {
			theEventWriter.writeAttribute("id", theResource.getId().getValue());
		}

		encodeCompositeElementToStreamWriter(resDef, theResource, theResource, theEventWriter, resDef, theIncludedResource);

		theEventWriter.writeEndElement();
	}

	private void encodeUndeclaredExtensions(RuntimeResourceDefinition theResDef, IResource theResource, XMLStreamWriter theWriter, List<ExtensionDt> extensions, String tagName, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		for (ExtensionDt next : extensions) {
			theWriter.writeStartElement(tagName);
			theWriter.writeAttribute("url", next.getUrl().getValue());

			if (next.getValue() != null) {
				IElement nextValue = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(nextValue.getClass());
				if (childName == null) {
					throw new ConfigurationException("Unable to encode extension, unregognized child element type: " + nextValue.getClass().getCanonicalName());
				}
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
				encodeChildElementToStreamWriter(theResDef, theResource, theWriter, nextValue, childName, childDef, null, theIncludedResource);
			}

			// child extensions
			encodeExtensionsIfPresent(theResDef, theResource, theWriter, next, theIncludedResource);

			theWriter.writeEndElement();
		}
	}

	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
		if (theDt == null || theDt.getValue() == null || getSuppressNarratives()) {
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
						String namespaceURI = se.getName().getNamespaceURI();
						if (StringUtils.isBlank(namespaceURI)) {
							namespaceURI = "http://www.w3.org/1999/xhtml";
						}
						theEventWriter.writeDefaultNamespace(namespaceURI);
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
					for (Iterator<?> attrIter = se.getAttributes(); attrIter.hasNext();) {
						Attribute next = (Attribute) attrIter.next();
						theEventWriter.writeAttribute(next.getName().getLocalPart(), next.getValue());
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

	private Bundle parseBundle(XMLEventReader theStreamReader, Class<? extends IResource> theResourceType) {
		ParserState<Bundle> parserState = ParserState.getPreAtomInstance(myContext, theResourceType, false);
		return doXmlLoop(theStreamReader, parserState);
	}

	private <T extends IResource> T parseResource(Class<T> theResourceType, XMLEventReader theStreamReader) {
		ParserState<T> parserState = ParserState.getPreResourceInstance(theResourceType, myContext, false);
		return doXmlLoop(theStreamReader, parserState);
	}

	private void writeAtomLink(XMLStreamWriter theEventWriter, String theRel, StringDt theStringDt) throws XMLStreamException {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.writeStartElement("link");
			theEventWriter.writeAttribute("rel", theRel);
			theEventWriter.writeAttribute("href", theStringDt.getValue());
			theEventWriter.writeEndElement();
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

	private void writeTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, IdDt theIdDt) throws XMLStreamException {
		theEventWriter.writeStartElement(theElementName);
		if (StringUtils.isNotBlank(theIdDt.getValue())) {
			theEventWriter.writeCharacters(theIdDt.getValue());
		}
		theEventWriter.writeEndElement();
	}

	private void writeTagWithTextNode(XMLStreamWriter theEventWriter, String theElementName, StringDt theStringDt) throws XMLStreamException {
		theEventWriter.writeStartElement(theElementName);
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.writeCharacters(theStringDt.getValue());
		}
		theEventWriter.writeEndElement();
	}
}
