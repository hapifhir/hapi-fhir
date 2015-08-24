package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
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
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.NonPrettyPrintWriterWrapper;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;
import ca.uhn.fhir.util.XmlUtil;

/**
 * This class is the FHIR XML parser/encoder. Users should not interact with this class directly, but should use {@link FhirContext#newXmlParser()} to get an instance.
 */
public class XmlParser extends BaseParser implements IParser {

	static final String ATOM_NS = "http://www.w3.org/2005/Atom";
	static final String FHIR_NS = "http://hl7.org/fhir";
	static final String OPENSEARCH_NS = "http://a9.com/-/spec/opensearch/1.1/";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	static final String RESREF_DISPLAY = "display";
	static final String RESREF_REFERENCE = "reference";
	static final String TOMBSTONES_NS = "http://purl.org/atompub/tombstones/1.0";
	static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

	// private static final Set<String> RESOURCE_NAMESPACES;

	private FhirContext myContext;
	private boolean myPrettyPrint;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the XML parser is to invoke {@link FhirContext#newXmlParser()}.
	 * 
	 * @param theParserErrorHandler
	 */
	public XmlParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
		myContext = theContext;
	}

	private XMLEventReader createStreamReader(Reader theReader) {
		try {
			return XmlUtil.createXmlReader(theReader);
		} catch (FactoryConfigurationError e1) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e1);
		} catch (XMLStreamException e1) {
			throw new DataFormatException(e1);
		}

		// XMLEventReader streamReader;
		// try {
		// streamReader = myXmlInputFactory.createXMLEventReader(theReader);
		// } catch (XMLStreamException e) {
		// throw new DataFormatException(e);
		// } catch (FactoryConfigurationError e) {
		// throw new ConfigurationException("Failed to initialize STaX event factory", e);
		// }
		// return streamReader;
	}

	private XMLStreamWriter createXmlWriter(Writer theWriter) throws XMLStreamException {
		XMLStreamWriter eventWriter;
		eventWriter = XmlUtil.createXmlStreamWriter(theWriter);
		eventWriter = decorateStreamWriter(eventWriter);
		return eventWriter;
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

	@Override
	public void doEncodeBundleToWriter(Bundle theBundle, Writer theWriter) throws DataFormatException {
		try {
			XMLStreamWriter eventWriter = createXmlWriter(theWriter);
			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				encodeBundleToWriterDstu2(theBundle, eventWriter);
			} else {
				encodeBundleToWriterDstu1(theBundle, eventWriter);
			}
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	@Override
	public void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws DataFormatException {
		XMLStreamWriter eventWriter;
		try {
			eventWriter = createXmlWriter(theWriter);

			encodeResourceToXmlStreamWriter(theResource, eventWriter, false);
			eventWriter.flush();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);
		return parseResource(theResourceType, streamReader);
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

						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = elem.getAttributes();
						for (Iterator<Attribute> iter = attributes; iter.hasNext();) {
							Attribute next = iter.next();
							parserState.attributeValue(next.getName().getLocalPart(), next.getValue());
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

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		StringWriter stringWriter = new StringWriter();
		try {
			encodeBundleToWriter(theBundle, stringWriter);
		} catch (IOException e) {
			throw new InternalErrorException("IOException writing to StringWriter - Should not happen", e);
		}

		return stringWriter.toString();
	}

	private void encodeBundleToWriterDstu1(Bundle theBundle, XMLStreamWriter eventWriter) throws XMLStreamException {
		eventWriter.writeStartElement("feed");
		eventWriter.writeDefaultNamespace(ATOM_NS);

		writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
		writeTagWithTextNode(eventWriter, "id", theBundle.getBundleId());

		writeAtomLink(eventWriter, Constants.LINK_SELF, theBundle.getLinkSelf());
		writeAtomLink(eventWriter, Constants.LINK_FIRST, theBundle.getLinkFirst());
		writeAtomLink(eventWriter, Constants.LINK_PREVIOUS, theBundle.getLinkPrevious());
		writeAtomLink(eventWriter, Constants.LINK_NEXT, theBundle.getLinkNext());
		writeAtomLink(eventWriter, Constants.LINK_LAST, theBundle.getLinkLast());
		writeAtomLink(eventWriter, Constants.LINK_FHIR_BASE, theBundle.getLinkBase());

		if (theBundle.getTotalResults().getValue() != null) {
			eventWriter.writeStartElement("os", "totalResults", OPENSEARCH_NS);
			eventWriter.writeNamespace("os", OPENSEARCH_NS);
			eventWriter.writeCharacters(theBundle.getTotalResults().getValue().toString());
			eventWriter.writeEndElement();
		}

		writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());

		if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
			eventWriter.writeStartElement("author");
			writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
			writeOptionalTagWithTextNode(eventWriter, "uri", theBundle.getAuthorUri());
			eventWriter.writeEndElement();
		}

		writeCategories(eventWriter, theBundle.getCategories());

		for (BundleEntry nextEntry : theBundle.getEntries()) {
			boolean deleted = false;
			if (nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false) {
				deleted = true;
				eventWriter.writeStartElement("at", "deleted-entry", TOMBSTONES_NS);
				eventWriter.writeNamespace("at", TOMBSTONES_NS);

				if (nextEntry.getDeletedResourceId().isEmpty()) {
					writeOptionalAttribute(eventWriter, "ref", nextEntry.getId().getValueAsString());
				} else {
					writeOptionalAttribute(eventWriter, "ref", nextEntry.getDeletedResourceId().getValueAsString());
				}

				writeOptionalAttribute(eventWriter, "when", nextEntry.getDeletedAt().getValueAsString());
				if (nextEntry.getDeletedByEmail().isEmpty() == false || nextEntry.getDeletedByName().isEmpty() == false) {
					eventWriter.writeStartElement(TOMBSTONES_NS, "by");
					if (nextEntry.getDeletedByName().isEmpty() == false) {
						eventWriter.writeStartElement(TOMBSTONES_NS, "name");
						eventWriter.writeCharacters(nextEntry.getDeletedByName().getValue());
						eventWriter.writeEndElement();
					}
					if (nextEntry.getDeletedByEmail().isEmpty() == false) {
						eventWriter.writeStartElement(TOMBSTONES_NS, "email");
						eventWriter.writeCharacters(nextEntry.getDeletedByEmail().getValue());
						eventWriter.writeEndElement();
					}
					eventWriter.writeEndElement();
				}
				if (nextEntry.getDeletedComment().isEmpty() == false) {
					eventWriter.writeStartElement(TOMBSTONES_NS, "comment");
					eventWriter.writeCharacters(nextEntry.getDeletedComment().getValue());
					eventWriter.writeEndElement();
				}
			} else {
				eventWriter.writeStartElement("entry");
			}

			writeOptionalTagWithTextNode(eventWriter, "title", nextEntry.getTitle());
			if (!deleted) {
				if (nextEntry.getId().isEmpty() == false) {
					writeTagWithTextNode(eventWriter, "id", nextEntry.getId());
				} else {
					writeTagWithTextNode(eventWriter, "id", nextEntry.getResource().getId());
				}
			}
			writeOptionalTagWithTextNode(eventWriter, "updated", nextEntry.getUpdated());
			writeOptionalTagWithTextNode(eventWriter, "published", nextEntry.getPublished());

			writeCategories(eventWriter, nextEntry.getCategories());

			if (!nextEntry.getLinkSelf().isEmpty()) {
				writeAtomLink(eventWriter, "self", nextEntry.getLinkSelf());
			}

			if (!nextEntry.getLinkAlternate().isEmpty()) {
				writeAtomLink(eventWriter, "alternate", nextEntry.getLinkAlternate());
			}

			if (!nextEntry.getLinkSearch().isEmpty()) {
				writeAtomLink(eventWriter, "search", nextEntry.getLinkSearch());
			}

			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				eventWriter.writeStartElement("content");
				eventWriter.writeAttribute("type", "text/xml");
				encodeResourceToXmlStreamWriter(resource, eventWriter, false);
				eventWriter.writeEndElement(); // content
			} else {
				ourLog.debug("Bundle entry contains null resource");
			}

			if (!nextEntry.getSummary().isEmpty()) {
				eventWriter.writeStartElement("summary");
				eventWriter.writeAttribute("type", "xhtml");
				encodeXhtml(nextEntry.getSummary(), eventWriter);
				eventWriter.writeEndElement();
			}

			eventWriter.writeEndElement(); // entry
		}

		eventWriter.writeEndElement();
		eventWriter.close();
	}

	private void encodeBundleToWriterDstu2(Bundle theBundle, XMLStreamWriter theEventWriter) throws XMLStreamException {
		theEventWriter.writeStartElement("Bundle");
		theEventWriter.writeDefaultNamespace(FHIR_NS);

		writeOptionalTagWithValue(theEventWriter, "id", theBundle.getId().getIdPart());

		InstantDt updated = (InstantDt) theBundle.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
		IdDt bundleId = theBundle.getId();
		if (bundleId != null && isNotBlank(bundleId.getVersionIdPart()) || (updated != null && !updated.isEmpty())) {
			theEventWriter.writeStartElement("meta");
			writeOptionalTagWithValue(theEventWriter, "versionId", bundleId.getVersionIdPart());
			if (updated != null) {
				writeOptionalTagWithValue(theEventWriter, "lastUpdated", updated.getValueAsString());
			}
			theEventWriter.writeEndElement();
		}

		String bundleBaseUrl = theBundle.getLinkBase().getValue();

		writeOptionalTagWithValue(theEventWriter, "type", theBundle.getType().getValue());
		writeOptionalTagWithValue(theEventWriter, "total", theBundle.getTotalResults().getValueAsString());

		writeBundleResourceLink(theEventWriter, "first", theBundle.getLinkFirst());
		writeBundleResourceLink(theEventWriter, "previous", theBundle.getLinkPrevious());
		writeBundleResourceLink(theEventWriter, "next", theBundle.getLinkNext());
		writeBundleResourceLink(theEventWriter, "last", theBundle.getLinkLast());
		writeBundleResourceLink(theEventWriter, "self", theBundle.getLinkSelf());

		for (BundleEntry nextEntry : theBundle.getEntries()) {
			theEventWriter.writeStartElement("entry");

			boolean deleted = false;
			if (nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false) {
				deleted = true;
			}

			writeBundleResourceLink(theEventWriter, "alternate", nextEntry.getLinkAlternate());

			if (nextEntry.getResource() != null && nextEntry.getResource().getId().getBaseUrl() != null) {
				writeOptionalTagWithValue(theEventWriter, "fullUrl", nextEntry.getResource().getId().getValue());
			}

			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				theEventWriter.writeStartElement("resource");
				encodeResourceToXmlStreamWriter(resource, theEventWriter, false);
				theEventWriter.writeEndElement(); // content
			} else {
				ourLog.debug("Bundle entry contains null resource");
			}

			if (nextEntry.getSearchMode().isEmpty() == false || nextEntry.getScore().isEmpty() == false) {
				theEventWriter.writeStartElement("search");
				writeOptionalTagWithValue(theEventWriter, "mode", nextEntry.getSearchMode().getValueAsString());
				writeOptionalTagWithValue(theEventWriter, "score", nextEntry.getScore().getValueAsString());
				theEventWriter.writeEndElement();
				// IResource nextResource = nextEntry.getResource();
			}

			if (nextEntry.getTransactionMethod().isEmpty() == false || nextEntry.getLinkSearch().isEmpty() == false) {
				theEventWriter.writeStartElement("request");
				writeOptionalTagWithValue(theEventWriter, "method", nextEntry.getTransactionMethod().getValue());
				writeOptionalTagWithValue(theEventWriter, "url", nextEntry.getLinkSearch().getValue());
				theEventWriter.writeEndElement();
			}

			if (deleted) {
				theEventWriter.writeStartElement("deleted");
				writeOptionalTagWithValue(theEventWriter, "type", nextEntry.getId().getResourceType());
				writeOptionalTagWithValue(theEventWriter, "id", nextEntry.getId().getIdPart());
				writeOptionalTagWithValue(theEventWriter, "versionId", nextEntry.getId().getVersionIdPart());
				writeOptionalTagWithValue(theEventWriter, "instant", nextEntry.getDeletedAt().getValueAsString());
				theEventWriter.writeEndElement();
			}

			theEventWriter.writeEndElement(); // entry
		}

		theEventWriter.writeEndElement();
		theEventWriter.close();
	}

	private void encodeChildElementToStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, IBase nextValue, String childName, BaseRuntimeElementDefinition<?> childDef,
			String theExtensionUrl, boolean theIncludedResource, CompositeChildElement theParent) throws XMLStreamException, DataFormatException {
		if (nextValue == null || nextValue.isEmpty()) {
			if (isChildContained(childDef, theIncludedResource)) {
				// We still want to go in..
			} else {
				return;
			}
		}

		switch (childDef.getChildType()) {
		case ID_DATATYPE: {
			IIdType value = (IIdType) nextValue;
			String encodedValue = "id".equals(childName) ? value.getIdPart() : value.getValue();
			if (value != null) {
				theEventWriter.writeStartElement(childName);
				theEventWriter.writeAttribute("value", encodedValue);
				encodeExtensionsIfPresent(theResource, theEventWriter, nextValue, theIncludedResource);
				theEventWriter.writeEndElement();
			}
			break;
		}
		case PRIMITIVE_DATATYPE: {
			IPrimitiveType<?> pd = (IPrimitiveType<?>) nextValue;
			String value = pd.getValueAsString();
			if (value != null) {
				theEventWriter.writeStartElement(childName);
				theEventWriter.writeAttribute("value", value);
				encodeExtensionsIfPresent(theResource, theEventWriter, nextValue, theIncludedResource);
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
			encodeCompositeElementToStreamWriter(theResource, nextValue, theEventWriter, childCompositeDef, theIncludedResource, theParent);
			theEventWriter.writeEndElement();
			break;
		}
		case RESOURCE_REF: {
			IBaseReference ref = (IBaseReference) nextValue;
			if (!ref.isEmpty()) {
				theEventWriter.writeStartElement(childName);
				encodeResourceReferenceToStreamWriter(theEventWriter, ref, theResource, theIncludedResource);
				theEventWriter.writeEndElement();
			}
			break;
		}
		case CONTAINED_RESOURCE_LIST:
		case CONTAINED_RESOURCES: {
			/*
			 * Disable per #103 for (IResource next : value.getContainedResources()) { if (getContainedResources().getResourceId(next) != null) { continue; }
			 * theEventWriter.writeStartElement("contained"); encodeResourceToXmlStreamWriter(next, theEventWriter, true, fixContainedResourceId(next.getId().getValue()));
			 * theEventWriter.writeEndElement(); }
			 */
			for (IBaseResource next : getContainedResources().getContainedResources()) {
				IIdType resourceId = getContainedResources().getResourceId(next);
				theEventWriter.writeStartElement("contained");
				encodeResourceToXmlStreamWriter(next, theEventWriter, true, fixContainedResourceId(resourceId.getValue()));
				theEventWriter.writeEndElement();
			}
			break;
		}
		case RESOURCE: {
			theEventWriter.writeStartElement(childName);
			IBaseResource resource = (IBaseResource) nextValue;
			encodeResourceToXmlStreamWriter(resource, theEventWriter, false);
			theEventWriter.writeEndElement();
			break;
		}
		case PRIMITIVE_XHTML: {
			XhtmlDt dt = (XhtmlDt) nextValue;
			if (dt.hasContent()) {
				encodeXhtml(dt, theEventWriter);
			}
			break;
		}
		case PRIMITIVE_XHTML_HL7ORG: {
			IBaseXhtml dt = (IBaseXhtml) nextValue;
			if (dt.isEmpty()) {
				break;
			} else {
				// TODO: this is probably not as efficient as it could be
				XhtmlDt hdt = new XhtmlDt();
				hdt.setValueAsString(dt.getValueAsString());
				encodeXhtml(hdt, theEventWriter);
				break;
			}
		}
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + childDef.getName());
		}
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> theChildren,
			boolean theContainedResource, CompositeChildElement theParent) throws XMLStreamException, DataFormatException {
		for (CompositeChildElement nextChildElem : super.compositeChildIterator(theChildren, theContainedResource, theParent)) {

			BaseRuntimeChildDefinition nextChild = nextChildElem.getDef();

			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				INarrativeGenerator gen = myContext.getNarrativeGenerator();
				if (theResource instanceof IResource) {
					BaseNarrativeDt<?> narr = ((IResource) theResource).getText();
					if (gen != null && narr.isEmpty()) {
						String resourceProfile = myContext.getResourceDefinition(theResource).getResourceProfile();
						gen.generateNarrative(resourceProfile, theResource, narr);
					}
					if (narr.isEmpty() == false) {
						RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
						String childName = nextChild.getChildNameByDatatype(child.getDatatype());
						BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
						encodeChildElementToStreamWriter(theResource, theEventWriter, narr, childName, type, null, theContainedResource, nextChildElem);
						continue;
					}
				} else {
					// Narrative generation not currently supported for HL7org structures
				}
			}

			if (nextChild instanceof RuntimeChildContainedResources) {
				encodeChildElementToStreamWriter(theResource, theEventWriter, null, nextChild.getChildNameByDatatype(null), nextChild.getChildElementDefinitionByDatatype(null), null, theContainedResource,
						nextChildElem);
			} else {

				List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
				values = super.preProcessValues(nextChild, values);

				if (values == null || values.isEmpty()) {
					continue;
				}
				for (IBase nextValue : values) {
					if ((nextValue == null || nextValue.isEmpty())) {
						continue;
					}
					Class<? extends IBase> type = nextValue.getClass();
					String childName = nextChild.getChildNameByDatatype(type);
					String extensionUrl = nextChild.getExtensionUrl();
					BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
					if (childDef == null) {
						super.throwExceptionForUnknownChildType(nextChild, type);
					}

					if (nextValue instanceof IBaseExtension && myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
						// This is called for the Query resource in DSTU1 only
						extensionUrl = ((IBaseExtension<?, ?>) nextValue).getUrl();
						encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, extensionUrl, theContainedResource, nextChildElem);

					} else if (extensionUrl != null && childName.equals("extension") == false) {
						RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
						if (extDef.isModifier()) {
							theEventWriter.writeStartElement("modifierExtension");
						} else {
							theEventWriter.writeStartElement("extension");
						}

						theEventWriter.writeAttribute("url", extensionUrl);
						encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, null, theContainedResource, nextChildElem);
						theEventWriter.writeEndElement();
					} else if (nextChild instanceof RuntimeChildNarrativeDefinition && theContainedResource) {
						// suppress narratives from contained resources
					} else {
						encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, extensionUrl, theContainedResource, nextChildElem);
					}
				}
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> theElementDefinition,
			boolean theIncludedResource, CompositeChildElement theParent) throws XMLStreamException, DataFormatException {
		encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, theElementDefinition.getExtensions(), theIncludedResource, theParent);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, theElementDefinition.getChildren(), theIncludedResource, theParent);
	}

	private void encodeExtensionsIfPresent(IBaseResource theResource, XMLStreamWriter theWriter, IBase theElement, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, toBaseExtensionList(res.getUndeclaredExtensions()), "extension", theIncludedResource);
			encodeUndeclaredExtensions(theResource, theWriter, toBaseExtensionList(res.getUndeclaredModifierExtensions()), "modifierExtension", theIncludedResource);
		}
		if (theElement instanceof IBaseHasExtensions) {
			IBaseHasExtensions res = (IBaseHasExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, res.getExtension(), "extension", theIncludedResource);
		}
		if (theElement instanceof IBaseHasModifierExtensions) {
			IBaseHasModifierExtensions res = (IBaseHasModifierExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, res.getModifierExtension(), "modifierExtension", theIncludedResource);
		}
	}

	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, IBaseReference theRef, IBaseResource theResource, boolean theIncludedResource) throws XMLStreamException {
		String reference = determineReferenceText(theRef);

		encodeExtensionsIfPresent(theResource, theEventWriter, theRef, theIncludedResource);

		if (StringUtils.isNotBlank(reference)) {
			theEventWriter.writeStartElement(RESREF_REFERENCE);
			theEventWriter.writeAttribute("value", reference);
			theEventWriter.writeEndElement();
		}
		if (!(theRef.getDisplayElement().isEmpty())) {
			theEventWriter.writeStartElement(RESREF_DISPLAY);
			theEventWriter.writeAttribute("value", theRef.getDisplayElement().getValue());
			theEventWriter.writeEndElement();
		}
	}

	private void encodeResourceToStreamWriterInDstu2Format(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter,
			BaseRuntimeElementCompositeDefinition<?> resDef, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		/*
		 * DSTU2 requires extensions to come in a specific spot within the encoded content - This is a bit of a messy way to make that happen, but hopefully this won't matter as much once we use the HL7
		 * structures
		 */

		List<BaseRuntimeChildDefinition> preExtensionChildren = new ArrayList<BaseRuntimeChildDefinition>();
		List<BaseRuntimeChildDefinition> postExtensionChildren = new ArrayList<BaseRuntimeChildDefinition>();
		List<BaseRuntimeChildDefinition> children = resDef.getChildren();
		for (BaseRuntimeChildDefinition next : children) {
			if (next.getElementName().equals("text")) {
				preExtensionChildren.add(next);
			} else if (next.getElementName().equals("contained")) {
				preExtensionChildren.add(next);
			} else {
				postExtensionChildren.add(next);
			}
		}

		CompositeChildElement parent = new CompositeChildElement(theResDef);

		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, preExtensionChildren, theIncludedResource, parent);

		encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, resDef.getExtensions(), theIncludedResource, parent);

		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, postExtensionChildren, theIncludedResource, parent);

	}

	private void encodeResourceToXmlStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		String resourceId = null;
		if (theResource instanceof IResource) {
			// HAPI structs
			IResource iResource = (IResource) theResource;
			if (StringUtils.isNotBlank(iResource.getId().getIdPart())) {
				resourceId = iResource.getId().getIdPart();
			}
		} else {
			// HL7 structs
			IAnyResource resource = (IAnyResource) theResource;
			if (StringUtils.isNotBlank(resource.getIdElement().getIdPart())) {
				resourceId = resource.getIdElement().getIdPart();
			}
		}

		if (isOmitResourceId() && !theIncludedResource) {
			resourceId = null;
		}

		encodeResourceToXmlStreamWriter(theResource, theEventWriter, theIncludedResource, resourceId);
	}

	private void encodeResourceToXmlStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theContainedResource, String theResourceId) throws XMLStreamException {
		if (!theContainedResource) {
			super.containResourcesForEncoding(theResource);
		}

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		if (resDef == null) {
			throw new ConfigurationException("Unknown resource type: " + theResource.getClass());
		}

		theEventWriter.writeStartElement(resDef.getName());
		theEventWriter.writeDefaultNamespace(FHIR_NS);

		if (theResource instanceof IAnyResource) {

			// HL7.org Structures
			writeOptionalTagWithValue(theEventWriter, "id", theResourceId);
			encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, resDef, theContainedResource, new CompositeChildElement(resDef));

		} else {

			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {

				// DSTU2+

				IResource resource = (IResource) theResource;
				writeOptionalTagWithValue(theEventWriter, "id", theResourceId);

				InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
				IdDt resourceId = resource.getId();
				String versionIdPart = resourceId.getVersionIdPart();
				if (isBlank(versionIdPart)) {
					versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
				}
				List<BaseCodingDt> securityLabels = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.SECURITY_LABELS);
				List<IdDt> profiles = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.PROFILES);
				TagList tags = getMetaTagsForEncoding((resource));

				if (ElementUtil.isEmpty(versionIdPart, updated, securityLabels, tags, profiles) == false) {
					theEventWriter.writeStartElement("meta");
					writeOptionalTagWithValue(theEventWriter, "versionId", versionIdPart);
					if (updated != null) {
						writeOptionalTagWithValue(theEventWriter, "lastUpdated", updated.getValueAsString());
					}

					for (IdDt profile : profiles) {
						theEventWriter.writeStartElement("profile");
						theEventWriter.writeAttribute("value", profile.getValue());
						theEventWriter.writeEndElement();
					}
					for (BaseCodingDt securityLabel : securityLabels) {
						theEventWriter.writeStartElement("security");
						BaseRuntimeElementCompositeDefinition<?> def = (BaseRuntimeElementCompositeDefinition<?>) myContext.getElementDefinition(securityLabel.getClass());
						encodeCompositeElementChildrenToStreamWriter(resource, securityLabel, theEventWriter, def.getChildren(), theContainedResource, null);
						theEventWriter.writeEndElement();
					}
					if (tags != null) {
						for (Tag tag : tags) {
							if (tag.isEmpty()) {
								continue;
							}
							theEventWriter.writeStartElement("tag");
							writeOptionalTagWithValue(theEventWriter, "system", tag.getScheme());
							writeOptionalTagWithValue(theEventWriter, "code", tag.getTerm());
							writeOptionalTagWithValue(theEventWriter, "display", tag.getLabel());
							theEventWriter.writeEndElement();
						}
					}
					theEventWriter.writeEndElement();
				}

				if (theResource instanceof IBaseBinary) {
					IBaseBinary bin = (IBaseBinary) theResource;
					writeOptionalTagWithValue(theEventWriter, "contentType", bin.getContentType());
					writeOptionalTagWithValue(theEventWriter, "content", bin.getContentAsBase64());
				} else {
					encodeResourceToStreamWriterInDstu2Format(resDef, theResource, theResource, theEventWriter, resDef, theContainedResource);
				}

			} else {

				// DSTU1
				if (theResourceId != null && theContainedResource) {
					theEventWriter.writeAttribute("id", theResourceId);
				}

				if (theResource instanceof IBaseBinary) {
					IBaseBinary bin = (IBaseBinary) theResource;
					if (bin.getContentType() != null) {
						theEventWriter.writeAttribute("contentType", bin.getContentType());
					}
					theEventWriter.writeCharacters(bin.getContentAsBase64());
				} else {
					encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, resDef, theContainedResource, new CompositeChildElement(resDef));
				}

			}

		}

		theEventWriter.writeEndElement();
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

	private void encodeUndeclaredExtensions(IBaseResource theResource, XMLStreamWriter theWriter, List<? extends IBaseExtension<?, ?>> theExtensions, String tagName, boolean theIncludedResource)
			throws XMLStreamException, DataFormatException {
		for (IBaseExtension<?, ?> next : theExtensions) {
			if (next == null || (ElementUtil.isEmpty(next.getValue()) && next.getExtension().isEmpty())) {
				continue;
			}

			theWriter.writeStartElement(tagName);

			String url = next.getUrl();
			theWriter.writeAttribute("url", url);

			if (next.getValue() != null) {
				IBaseDatatype value = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				BaseRuntimeElementDefinition<?> childDef;
				if (childName == null) {
					childDef = myContext.getElementDefinition(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException("Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					} else {
						childName = RuntimeChildUndeclaredExtensionDefinition.createExtensionChildName(childDef);
					}
				} else {
					childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException("Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
				}
				encodeChildElementToStreamWriter(theResource, theWriter, value, childName, childDef, null, theIncludedResource, null);
			}

			// child extensions
			encodeExtensionsIfPresent(theResource, theWriter, next, theIncludedResource);

			theWriter.writeEndElement();
		}
	}

	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
		if (theDt == null || theDt.getValue() == null) {
			return;
		}

		boolean firstElement = true;
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
				String data = ((Characters) event).getData();
				theEventWriter.writeCharacters(data);
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
				if (firstElement) {
					if (StringUtils.isBlank(se.getName().getPrefix())) {
						String namespaceURI = se.getName().getNamespaceURI();
						if (StringUtils.isBlank(namespaceURI)) {
							namespaceURI = "http://www.w3.org/1999/xhtml";
						}
						theEventWriter.writeStartElement(se.getName().getLocalPart());
						theEventWriter.writeDefaultNamespace(namespaceURI);
					} else {
						String prefix = se.getName().getPrefix();
						String namespaceURI = se.getName().getNamespaceURI();
						theEventWriter.writeStartElement(prefix, se.getName().getLocalPart(), namespaceURI);
						theEventWriter.writeNamespace(prefix, namespaceURI);
					}
					firstElement = false;
				} else {
					if (isBlank(se.getName().getPrefix())) {
						if (isBlank(se.getName().getNamespaceURI())) {
							theEventWriter.writeStartElement(se.getName().getLocalPart());
						} else {
							if (StringUtils.isBlank(se.getName().getPrefix())) {
								theEventWriter.writeStartElement(se.getName().getLocalPart());
								// theEventWriter.writeDefaultNamespace(se.getName().getNamespaceURI());
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

		}
	}

	@Override
	public EncodingEnum getEncoding() {
		return EncodingEnum.XML;
	}

	@Override
	public <T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		return parseBundle(streamReader, theResourceType);
	}

	private Bundle parseBundle(XMLEventReader theStreamReader, Class<? extends IBaseResource> theResourceType) {
		ParserState<Bundle> parserState = ParserState.getPreAtomInstance(myContext, theResourceType, false, getErrorHandler());
		return doXmlLoop(theStreamReader, parserState);
	}

	private <T extends IBaseResource> T parseResource(Class<T> theResourceType, XMLEventReader theStreamReader) {
		ParserState<T> parserState = ParserState.getPreResourceInstance(theResourceType, myContext, false, getErrorHandler());
		return doXmlLoop(theStreamReader, parserState);
	}

	@Override
	public TagList parseTagList(Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		ParserState<TagList> parserState = ParserState.getPreTagListInstance(myContext, false, getErrorHandler());
		return doXmlLoop(streamReader, parserState);
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	/**
	 * This is just to work around the fact that casting java.util.List<ca.uhn.fhir.model.api.ExtensionDt> to java.util.List<? extends org.hl7.fhir.instance.model.api.IBaseExtension<?, ?>> seems to be
	 * rejected by the compiler some of the time.
	 */
	private <Q extends IBaseExtension<?, ?>> List<IBaseExtension<?, ?>> toBaseExtensionList(final List<Q> theList) {
		List<IBaseExtension<?, ?>> retVal = new ArrayList<IBaseExtension<?, ?>>(theList.size());
		retVal.addAll(theList);
		return retVal;
	}

	private void writeAtomLink(XMLStreamWriter theEventWriter, String theRel, StringDt theStringDt) throws XMLStreamException {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.writeStartElement("link");
			theEventWriter.writeAttribute("rel", theRel);
			theEventWriter.writeAttribute("href", theStringDt.getValue());
			theEventWriter.writeEndElement();
		}
	}

	private void writeBundleResourceLink(XMLStreamWriter theEventWriter, String theRel, StringDt theUrl) throws XMLStreamException {
		if (theUrl.isEmpty() == false) {
			theEventWriter.writeStartElement("link");
			theEventWriter.writeStartElement("relation");
			theEventWriter.writeAttribute("value", theRel);
			theEventWriter.writeEndElement();
			theEventWriter.writeStartElement("url");
			theEventWriter.writeAttribute("value", theUrl.getValue());
			theEventWriter.writeEndElement();
			theEventWriter.writeEndElement();
		}
	}

	private void writeCategories(XMLStreamWriter eventWriter, TagList categories) throws XMLStreamException {
		if (categories != null) {
			for (Tag next : categories) {
				eventWriter.writeStartElement("category");
				eventWriter.writeAttribute("term", defaultString(next.getTerm()));
				eventWriter.writeAttribute("label", defaultString(next.getLabel()));
				eventWriter.writeAttribute("scheme", defaultString(next.getScheme()));
				eventWriter.writeEndElement();
			}
		}
	}

	private void writeOptionalAttribute(XMLStreamWriter theEventWriter, String theName, String theValue) throws XMLStreamException {
		if (StringUtils.isNotBlank(theValue)) {
			theEventWriter.writeAttribute(theName, theValue);
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

	private void writeOptionalTagWithValue(XMLStreamWriter theEventWriter, String theName, String theValue) throws XMLStreamException {
		if (StringUtils.isNotBlank(theValue)) {
			theEventWriter.writeStartElement(theName);
			theEventWriter.writeAttribute("value", theValue);
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
