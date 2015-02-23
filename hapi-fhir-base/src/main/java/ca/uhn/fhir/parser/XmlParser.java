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

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.*;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.IBase;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.INarrative;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
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
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseBinary;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.method.BaseMethodBinding;
import ca.uhn.fhir.util.NonPrettyPrintWriterWrapper;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;
import ca.uhn.fhir.util.XmlUtil;

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
	 */
	public XmlParser(FhirContext theContext) {
		super(theContext);
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
			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
				encodeBundleToWriterDstu2(theBundle, eventWriter);
			} else {
				encodeBundleToWriterDstu1(theBundle, eventWriter);
			}
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	private void encodeBundleToWriterDstu1(Bundle theBundle, XMLStreamWriter eventWriter) throws XMLStreamException {
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
		writeOptionalTagWithValue(theEventWriter, "base", bundleBaseUrl);
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

			writeOptionalTagWithValue(theEventWriter, "base", determineResourceBaseUrl(bundleBaseUrl, nextEntry));

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

			if (nextEntry.getTransactionOperation().isEmpty() == false || nextEntry.getLinkSearch().isEmpty() == false) {
				theEventWriter.writeStartElement("transaction");
				writeOptionalTagWithValue(theEventWriter, "operation", nextEntry.getTransactionOperation().getValue());
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
												  String theExtensionUrl, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		if (nextValue.isEmpty()) {
			if (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES && getContainedResources().isEmpty() == false && theIncludedResource == false) {
				// We still want to go in..
			} else {
				return;
			}
		}

		switch (childDef.getChildType()) {
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
			encodeCompositeElementToStreamWriter(theResource, nextValue, theEventWriter, childCompositeDef, theIncludedResource);
			theEventWriter.writeEndElement();
			break;
		}
		case RESOURCE_REF: {
			BaseResourceReferenceDt ref = (BaseResourceReferenceDt) nextValue;
			if (!ref.isEmpty()) {
				theEventWriter.writeStartElement(childName);
				encodeResourceReferenceToStreamWriter(theEventWriter, ref);
				theEventWriter.writeEndElement();
			}
			break;
		}
		case CONTAINED_RESOURCES: {
			BaseContainedDt value = (BaseContainedDt) nextValue;
			/*
			 * Disable per #103 for (IResource next : value.getContainedResources()) { if (getContainedResources().getResourceId(next) != null) { continue; }
			 * theEventWriter.writeStartElement("contained"); encodeResourceToXmlStreamWriter(next, theEventWriter, true, fixContainedResourceId(next.getId().getValue()));
			 * theEventWriter.writeEndElement(); }
			 */
			for (IBaseResource next : getContainedResources().getContainedResources()) {
				IdDt resourceId = getContainedResources().getResourceId(next);
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
		case EXTENSION_DECLARED:
		case UNDECL_EXT: {
			throw new IllegalStateException("state should not happen: " + childDef.getName());
		}
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> children,
															  boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		for (BaseRuntimeChildDefinition nextChild : children) {
			if (nextChild.getElementName().equals("extension") || nextChild.getElementName().equals("modifierExtension")) {
				continue;
			}
			
			if (nextChild instanceof RuntimeChildNarrativeDefinition && !theIncludedResource) {
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
						encodeChildElementToStreamWriter(theResource, theEventWriter, narr, childName, type, null, theIncludedResource);
						continue;
					}
				} else {
					INarrative narr1 = ((IDomainResource) theResource).getText();
					BaseNarrativeDt<?> narr2 = null;
					if (gen != null && narr1.isEmpty()) {
						// TODO: need to implement this
						String resourceProfile = myContext.getResourceDefinition(theResource).getResourceProfile();
						gen.generateNarrative(resourceProfile, theResource, null);
					}
					if (narr2 != null) {
						RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
						String childName = nextChild.getChildNameByDatatype(child.getDatatype());
						BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
						encodeChildElementToStreamWriter(theResource, theEventWriter, narr2, childName, type, null, theIncludedResource);
						continue;
					}
				}
			}

			List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
			if (values == null || values.isEmpty()) {
				continue;
			}

			for (IBase nextValue : values) {
				if ((nextValue == null || nextValue.isEmpty()) && !(nextValue instanceof BaseContainedDt)) {
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
					extensionUrl = ((IBaseExtension<?>) nextValue).getUrl();
					encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, extensionUrl, theIncludedResource);
					
				} else if (extensionUrl != null && childName.equals("extension") == false) {
					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
					if (extDef.isModifier()) {
						theEventWriter.writeStartElement("modifierExtension");
					} else {
						theEventWriter.writeStartElement("extension");
					}

					theEventWriter.writeAttribute("url", extensionUrl);
					encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, null, theIncludedResource);
					theEventWriter.writeEndElement();
				} else if (nextChild instanceof RuntimeChildNarrativeDefinition && theIncludedResource) {
					// suppress narratives from contained resources
				} else {
					encodeChildElementToStreamWriter(theResource, theEventWriter, nextValue, childName, childDef, extensionUrl, theIncludedResource);
				}
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> theElementDefinition,
													  boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, theElementDefinition.getExtensions(), theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, theElementDefinition.getChildren(), theIncludedResource);
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

	/**
	 * This is just to work around the fact that casting java.util.List<ca.uhn.fhir.model.api.ExtensionDt> to java.util.List<? extends org.hl7.fhir.instance.model.api.IBaseExtension<?>> seems to be
	 * rejected by the compiler some of the time.
	 */
	private <Q extends IBaseExtension<?>> List<IBaseExtension<?>> toBaseExtensionList(final List<Q> theList) {
		List<IBaseExtension<?>> retVal = new ArrayList<IBaseExtension<?>>(theList.size());
		retVal.addAll(theList);
		return retVal;
	}

	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, BaseResourceReferenceDt theRef) throws XMLStreamException {
		String reference = determineReferenceText(theRef);

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
		 * DSTU2 requires extensions to come in a specific spot within the encoded content - This is a bit of a messy way to make that happen, but hopefully this won't matter as much once we use the
		 * HL7 structures
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
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, preExtensionChildren, theIncludedResource);

		encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource);
		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, resDef.getExtensions(), theIncludedResource);

		encodeCompositeElementChildrenToStreamWriter(theResource, theElement, theEventWriter, postExtensionChildren, theIncludedResource);

	}

	@Override
	public String encodeResourceToString(IBaseResource theResource) throws DataFormatException {
		if (theResource == null) {
			throw new NullPointerException("Resource can not be null");
		}

		Writer stringWriter = new StringWriter();
		encodeResourceToWriter(theResource, stringWriter);
		return stringWriter.toString();
	}

	@Override
	public void encodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws DataFormatException {
		XMLStreamWriter eventWriter;
		try {
			eventWriter = createXmlWriter(theWriter);

			encodeResourceToXmlStreamWriter(theResource, eventWriter, false);
			eventWriter.flush();
		} catch (XMLStreamException e) {
			throw new ConfigurationException("Failed to initialize STaX event factory", e);
		}
	}

	private void encodeResourceToXmlStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theIncludedResource) throws XMLStreamException, DataFormatException {
		String resourceId = null;
		if (theResource instanceof IResource) {
			// HAPI structs
			IResource iResource = (IResource) theResource;
			if (StringUtils.isNotBlank(iResource.getId().getValue())) {
				resourceId = iResource.getId().getIdPart();
			}
		} else {
			// HL7 structs
			IAnyResource resource = (IAnyResource) theResource;
			if (StringUtils.isNotBlank(resource.getId())) {
				resourceId = resource.getId();
			}
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
			encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, resDef, theContainedResource);


		} else {

			if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {

				// DSTU2+


				IResource resource = (IResource) theResource;
				writeOptionalTagWithValue(theEventWriter, "id", theResourceId);


				InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
				IdDt resourceId = resource.getId();
				if (resourceId != null && isNotBlank(resourceId.getVersionIdPart()) || (updated != null && !updated.isEmpty())) {
					theEventWriter.writeStartElement("meta");
					String versionIdPart = resourceId.getVersionIdPart();
					if (isBlank(versionIdPart)) {
						versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
					}
					writeOptionalTagWithValue(theEventWriter, "versionId", versionIdPart);
					if (updated != null) {
						writeOptionalTagWithValue(theEventWriter, "lastUpdated", updated.getValueAsString());
					}
					Object securityLabelRawObj = resource.getResourceMetadata().get(ResourceMetadataKeyEnum.SECURITY_LABELS);
					if (securityLabelRawObj != null) {
						List<BaseCodingDt> securityLabels = (List<BaseCodingDt>) securityLabelRawObj;
						if (!securityLabels.isEmpty()) {

							for (BaseCodingDt securityLabel : securityLabels) {
								theEventWriter.writeStartElement("security");

								UriDt system = securityLabel.getSystemElement();
								if (system != null && !system.isEmpty())
									writeOptionalTagWithValue(theEventWriter, "system", system.getValueAsString());

								CodeDt code = securityLabel.getCodeElement();
								if (code != null && !code.isEmpty())
									writeOptionalTagWithValue(theEventWriter, "code", code.getValueAsString());

								StringDt display = securityLabel.getDisplayElement();
								if (display != null && !display.isEmpty())
									writeOptionalTagWithValue(theEventWriter, "display", display.getValueAsString());

							/*todo: handle version
							StringDt version = securityLabel.getVersion();
							if (version != null && ! version.isEmpty())
								writeOptionalTagWithValue(theEventWriter, "version", version.getValueAsString());
                            */
								theEventWriter.writeEndElement();
							}

						}
					}
					theEventWriter.writeEndElement();
				}

				if (theResource instanceof BaseBinary) {
					BaseBinary bin = (BaseBinary) theResource;
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

				if (theResource instanceof BaseBinary) {
					BaseBinary bin = (BaseBinary) theResource;
					if (bin.getContentType() != null) {
						theEventWriter.writeAttribute("contentType", bin.getContentType());
					}
					theEventWriter.writeCharacters(bin.getContentAsBase64());
				} else {
					encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, resDef, theContainedResource);
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

	private void encodeUndeclaredExtensions(IBaseResource theResource, XMLStreamWriter theWriter, List<? extends IBaseExtension<?>> theExtensions, String tagName, boolean theIncludedResource)
			throws XMLStreamException, DataFormatException {
		for (IBaseExtension<?> next : theExtensions) {
			if (next == null) {
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
				}
				encodeChildElementToStreamWriter(theResource, theWriter, value, childName, childDef, null, theIncludedResource);
			}

			// child extensions
			encodeExtensionsIfPresent(theResource, theWriter, next, theIncludedResource);

			theWriter.writeEndElement();
		}
	}

	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
		if (theDt == null || theDt.getValue() == null || getSuppressNarratives()) {
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
	public <T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);

		return parseBundle(streamReader, theResourceType);
	}

	private Bundle parseBundle(XMLEventReader theStreamReader, Class<? extends IBaseResource> theResourceType) {
		ParserState<Bundle> parserState = ParserState.getPreAtomInstance(myContext, theResourceType, false);
		return doXmlLoop(theStreamReader, parserState);
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) {
		XMLEventReader streamReader = createStreamReader(theReader);
		return parseResource(theResourceType, streamReader);
	}

	private <T extends IBaseResource> T parseResource(Class<T> theResourceType, XMLEventReader theStreamReader) {
		ParserState<T> parserState = ParserState.getPreResourceInstance(theResourceType, myContext, false);
		return doXmlLoop(theStreamReader, parserState);
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
