package ca.uhn.fhir.parser;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeDeclaredChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildExtension;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.NonPrettyPrintWriterWrapper;
import ca.uhn.fhir.util.PrettyPrintWriterWrapper;
import ca.uhn.fhir.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IBaseXhtml;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

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
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is the FHIR XML parser/encoder. Users should not interact with this class directly, but should use
 * {@link FhirContext#newXmlParser()} to get an instance.
 */
public class XmlParser extends BaseParser {

	static final String FHIR_NS = "http://hl7.org/fhir";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParser.class);
	private boolean myPrettyPrint;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the XML parser is to invoke
	 * {@link FhirContext#newXmlParser()}.
	 *
	 * @param theParserErrorHandler
	 */
	public XmlParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
	}

	private XMLEventReader createStreamReader(Reader theReader) {
		try {
			return XmlUtil.createXmlReader(theReader);
		} catch (FactoryConfigurationError e1) {
			throw new ConfigurationException(Msg.code(1848) + "Failed to initialize STaX event factory", e1);
		} catch (XMLStreamException e1) {
			throw new DataFormatException(Msg.code(1849) + e1);
		}
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
		}
		NonPrettyPrintWriterWrapper retVal = new NonPrettyPrintWriterWrapper(eventWriter);
		return retVal;
	}

	@Override
	public void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext) throws DataFormatException {
		XMLStreamWriter eventWriter;
		try {
			eventWriter = createXmlWriter(theWriter);

			encodeResourceToXmlStreamWriter(theResource, eventWriter, false, theEncodeContext);
			eventWriter.flush();
		} catch (XMLStreamException e) {
			throw new ConfigurationException(Msg.code(1850) + "Failed to initialize STaX event factory", e);
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
			List<String> heldComments = new ArrayList<>(1);

			while (streamReader.hasNext()) {
				XMLEvent nextEvent = streamReader.nextEvent();
				try {

					switch (nextEvent.getEventType()) {
						case XMLStreamConstants.START_ELEMENT: {
							StartElement elem = nextEvent.asStartElement();

							String namespaceURI = elem.getName().getNamespaceURI();

							if ("extension".equals(elem.getName().getLocalPart())) {
								Attribute urlAttr = elem.getAttributeByName(new QName("url"));
								String url;
								if (urlAttr == null || isBlank(urlAttr.getValue())) {
									getErrorHandler().missingRequiredElement(new ParseLocation().setParentElementName("extension"), "url");
									url = null;
								} else {
									url = urlAttr.getValue();
								}
								parserState.enteringNewElementExtension(elem, url, false, getServerBaseUrl());
							} else if ("modifierExtension".equals(elem.getName().getLocalPart())) {
								Attribute urlAttr = elem.getAttributeByName(new QName("url"));
								String url;
								if (urlAttr == null || isBlank(urlAttr.getValue())) {
									getErrorHandler().missingRequiredElement(new ParseLocation().setParentElementName("modifierExtension"), "url");
									url = null;
								} else {
									url = urlAttr.getValue();
								}
								parserState.enteringNewElementExtension(elem, url, true, getServerBaseUrl());
							} else {
								String elementName = elem.getName().getLocalPart();
								parserState.enteringNewElement(namespaceURI, elementName);
							}

							if (!heldComments.isEmpty()) {
								for (String next : heldComments) {
									parserState.commentPre(next);
								}
								heldComments.clear();
							}

							for (Iterator<Attribute> attributes = elem.getAttributes(); attributes.hasNext(); ) {
								Attribute next = attributes.next();
								parserState.attributeValue(next.getName().getLocalPart(), next.getValue());
							}

							break;
						}
						case XMLStreamConstants.END_DOCUMENT:
						case XMLStreamConstants.END_ELEMENT: {
							if (!heldComments.isEmpty()) {
								for (String next : heldComments) {
									parserState.commentPost(next);
								}
								heldComments.clear();
							}
							parserState.endingElement();
							break;
						}
						case XMLStreamConstants.CHARACTERS: {
							parserState.string(nextEvent.asCharacters().getData());
							break;
						}
						case XMLStreamConstants.COMMENT: {
							Comment comment = (Comment) nextEvent;
							String commentText = comment.getText();
							heldComments.add(commentText);
							break;
						}
					}

					parserState.xmlEvent(nextEvent);

				} catch (DataFormatException e) {
					throw new DataFormatException(Msg.code(1851) + "DataFormatException at [" + nextEvent.getLocation().toString() + "]: " + e.getMessage(), e);
				}
			}
			return parserState.getObject();
		} catch (XMLStreamException e) {
			throw new DataFormatException(Msg.code(1852) + e);
		}
	}

	private void encodeChildElementToStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, BaseRuntimeChildDefinition theChildDefinition, IBase theElement, String theChildName, BaseRuntimeElementDefinition<?> childDef,
																 String theExtensionUrl, boolean theIncludedResource, CompositeChildElement theParent, EncodeContext theEncodeContext) throws XMLStreamException, DataFormatException {

		/*
		 * Often the two values below will be the same thing. There are cases though
		 * where they will not be. An example would be Observation.value, which is
		 * a choice type. If the value contains a Quantity, then:
		 * childGenericName = "value"
		 * theChildName = "valueQuantity"
		 */
		String childGenericName = theChildDefinition.getElementName();

		theEncodeContext.pushPath(childGenericName, false);
		try {

			if (theElement == null || theElement.isEmpty()) {
				if (isChildContained(childDef, theIncludedResource)) {
					// We still want to go in..
				} else {
					return;
				}
			}

			writeCommentsPre(theEventWriter, theElement);

			switch (childDef.getChildType()) {
				case ID_DATATYPE: {
					IIdType value = IIdType.class.cast(theElement);
					String encodedValue = "id".equals(theChildName) ? value.getIdPart() : value.getValue();
					if (StringUtils.isNotBlank(encodedValue) || !super.hasNoExtensions(value)) {
						theEventWriter.writeStartElement(theChildName);
						if (StringUtils.isNotBlank(encodedValue)) {
							theEventWriter.writeAttribute("value", encodedValue);
						}
						encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource, theEncodeContext);
						theEventWriter.writeEndElement();
					}
					break;
				}
				case PRIMITIVE_DATATYPE: {
					IPrimitiveType<?> pd = IPrimitiveType.class.cast(theElement);
					String value = pd.getValueAsString();
					if (value != null || !super.hasNoExtensions(pd)) {
						theEventWriter.writeStartElement(theChildName);
						String elementId = getCompositeElementId(theElement);
						if (isNotBlank(elementId)) {
							theEventWriter.writeAttribute("id", elementId);
						}
						if (value != null) {
							theEventWriter.writeAttribute("value", value);
						}
						encodeExtensionsIfPresent(theResource, theEventWriter, theElement, theIncludedResource, theEncodeContext);
						theEventWriter.writeEndElement();
					}
					break;
				}
				case RESOURCE_BLOCK:
				case COMPOSITE_DATATYPE: {
					theEventWriter.writeStartElement(theChildName);
					String elementId = getCompositeElementId(theElement);
					if (isNotBlank(elementId)) {
						theEventWriter.writeAttribute("id", elementId);
					}
					if (isNotBlank(theExtensionUrl)) {
						theEventWriter.writeAttribute("url", theExtensionUrl);
					}
					encodeCompositeElementToStreamWriter(theResource, theElement, theEventWriter, theIncludedResource, theParent, theEncodeContext);
					theEventWriter.writeEndElement();
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
						String value = resourceId.getValue();
						encodeResourceToXmlStreamWriter(next, theEventWriter, true, fixContainedResourceId(value), theEncodeContext);
						theEventWriter.writeEndElement();
					}
					break;
				}
				case RESOURCE: {
					IBaseResource resource = (IBaseResource) theElement;
					String resourceName = getContext().getResourceType(resource);
					if (!super.shouldEncodeResource(resourceName)) {
						break;
					}
					theEventWriter.writeStartElement(theChildName);
					theEncodeContext.pushPath(resourceName, true);
					encodeResourceToXmlStreamWriter(resource, theEventWriter, theIncludedResource, theEncodeContext);
					theEncodeContext.popPath();
					theEventWriter.writeEndElement();
					break;
				}
				case PRIMITIVE_XHTML: {
					XhtmlDt dt = XhtmlDt.class.cast(theElement);
					if (dt.hasContent()) {
						encodeXhtml(dt, theEventWriter);
					}
					break;
				}
				case PRIMITIVE_XHTML_HL7ORG: {
					IBaseXhtml dt = IBaseXhtml.class.cast(theElement);
					if (!dt.isEmpty()) {
						// TODO: this is probably not as efficient as it could be
						XhtmlDt hdt = new XhtmlDt();
						hdt.setValueAsString(dt.getValueAsString());
						encodeXhtml(hdt, theEventWriter);
					}
					break;
				}
				case EXTENSION_DECLARED:
				case UNDECL_EXT: {
					throw new IllegalStateException(Msg.code(1853) + "state should not happen: " + childDef.getName());
				}
			}

			writeCommentsPost(theEventWriter, theElement);

		} finally {
			theEncodeContext.popPath();
		}

	}

	private void encodeCompositeElementToStreamWriter(IBaseResource theResource, IBase theElement, XMLStreamWriter theEventWriter, boolean theContainedResource, CompositeChildElement theParent, EncodeContext theEncodeContext)
		throws XMLStreamException, DataFormatException {

		for (CompositeChildElement nextChildElem : super.compositeChildIterator(theElement, theContainedResource, theParent, theEncodeContext)) {

			BaseRuntimeChildDefinition nextChild = nextChildElem.getDef();

			if (nextChild.getElementName().equals("url") && theElement instanceof IBaseExtension) {
				/*
				 * XML encoding is a one-off for extensions. The URL element goes in an attribute
				 * instead of being encoded as a normal element, only for XML encoding
				 */
				continue;
			}

			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				Optional<IBase> narr = nextChild.getAccessor().getFirstValueOrNull(theElement);
				INarrativeGenerator gen = getContext().getNarrativeGenerator();
				if (gen != null && narr.isPresent() == false) {
					gen.populateResourceNarrative(getContext(), theResource);
				}

				narr = nextChild.getAccessor().getFirstValueOrNull(theElement);
				if (narr.isPresent()) {
					RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
					String childName = nextChild.getChildNameByDatatype(child.getDatatype());
					BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
					encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, narr.get(), childName, type, null, theContainedResource, nextChildElem, theEncodeContext);
					continue;
				}
			}

			if (nextChild instanceof RuntimeChildContainedResources) {
				encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, null, nextChild.getChildNameByDatatype(null), nextChild.getChildElementDefinitionByDatatype(null), null, theContainedResource, nextChildElem, theEncodeContext);
			} else {

				List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
				values = preProcessValues(nextChild, theResource, values, nextChildElem, theEncodeContext);

				if (values == null || values.isEmpty()) {
					continue;
				}
				for (IBase nextValue : values) {
					if ((nextValue == null || nextValue.isEmpty())) {
						continue;
					}

					BaseParser.ChildNameAndDef childNameAndDef = super.getChildNameAndDef(nextChild, nextValue);
					if (childNameAndDef == null) {
						continue;
					}

					String childName = childNameAndDef.getChildName();
					BaseRuntimeElementDefinition<?> childDef = childNameAndDef.getChildDef();
					String extensionUrl = getExtensionUrl(nextChild.getExtensionUrl());

					boolean isExtension = childName.equals("extension") || childName.equals("modifierExtension");
					if (isExtension && nextValue instanceof IBaseExtension) {
						IBaseExtension<?, ?> ext = (IBaseExtension<?, ?>) nextValue;
						if (isBlank(ext.getUrl())) {
							ParseLocation loc = new ParseLocation(theEncodeContext.toString() + "." + childName);
							getErrorHandler().missingRequiredElement(loc, "url");
						}
						if (ext.getValue() != null && ext.getExtension().size() > 0) {
							ParseLocation loc = new ParseLocation(theEncodeContext.toString() + "." + childName);
							getErrorHandler().extensionContainsValueAndNestedExtensions(loc);
						}
					}

					if (extensionUrl != null && isExtension == false) {
						encodeExtension(theResource, theEventWriter, theContainedResource, nextChildElem, nextChild, nextValue, childName, extensionUrl, childDef, theEncodeContext);
					} else if (nextChild instanceof RuntimeChildExtension) {
						IBaseExtension<?, ?> extension = (IBaseExtension<?, ?>) nextValue;
						if ((extension.getValue() == null || extension.getValue().isEmpty())) {
							if (extension.getExtension().isEmpty()) {
								continue;
							}
						}
						encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, nextValue, childName, childDef, getExtensionUrl(extension.getUrl()), theContainedResource, nextChildElem, theEncodeContext);
					} else {
						encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, nextValue, childName, childDef, extensionUrl, theContainedResource, nextChildElem, theEncodeContext);
					}

				}
			}
		}
	}

	private void encodeExtension(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theContainedResource, CompositeChildElement nextChildElem, BaseRuntimeChildDefinition nextChild, IBase nextValue, String childName, String extensionUrl, BaseRuntimeElementDefinition<?> childDef, EncodeContext theEncodeContext)
		throws XMLStreamException {
		BaseRuntimeDeclaredChildDefinition extDef = (BaseRuntimeDeclaredChildDefinition) nextChild;
		if (extDef.isModifier()) {
			theEventWriter.writeStartElement("modifierExtension");
		} else {
			theEventWriter.writeStartElement("extension");
		}

		String elementId = getCompositeElementId(nextValue);
		if (isNotBlank(elementId)) {
			theEventWriter.writeAttribute("id", elementId);
		}

		if (isBlank(extensionUrl)) {
			ParseLocation loc = new ParseLocation(theEncodeContext.toString());
			getErrorHandler().missingRequiredElement(loc, "url");
		} else {
			theEventWriter.writeAttribute("url", extensionUrl);
		}

		encodeChildElementToStreamWriter(theResource, theEventWriter, nextChild, nextValue, childName, childDef, null, theContainedResource, nextChildElem, theEncodeContext);
		theEventWriter.writeEndElement();
	}

	private void encodeExtensionsIfPresent(IBaseResource theResource, XMLStreamWriter theWriter, IBase theElement, boolean theIncludedResource, EncodeContext theEncodeContext) throws XMLStreamException, DataFormatException {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, toBaseExtensionList(res.getUndeclaredExtensions()), "extension", theIncludedResource, theEncodeContext);
			encodeUndeclaredExtensions(theResource, theWriter, toBaseExtensionList(res.getUndeclaredModifierExtensions()), "modifierExtension", theIncludedResource, theEncodeContext);
		}
		if (theElement instanceof IBaseHasExtensions) {
			IBaseHasExtensions res = (IBaseHasExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, res.getExtension(), "extension", theIncludedResource, theEncodeContext);
		}
		if (theElement instanceof IBaseHasModifierExtensions) {
			IBaseHasModifierExtensions res = (IBaseHasModifierExtensions) theElement;
			encodeUndeclaredExtensions(theResource, theWriter, res.getModifierExtension(), "modifierExtension", theIncludedResource, theEncodeContext);
		}
	}

	private void encodeResourceToXmlStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theIncludedResource, EncodeContext theEncodeContext) throws XMLStreamException, DataFormatException {
		IIdType resourceId = null;

		if (StringUtils.isNotBlank(theResource.getIdElement().getIdPart())) {
			resourceId = theResource.getIdElement();
			if (theResource.getIdElement().getValue().startsWith("urn:")) {
				resourceId = null;
			}
		}

		if (!theIncludedResource) {
			if (super.shouldEncodeResourceId(theResource, theEncodeContext) == false) {
				resourceId = null;
			} else if (theEncodeContext.getResourcePath().size() == 1 && getEncodeForceResourceId() != null) {
				resourceId = getEncodeForceResourceId();
			}
		}

		encodeResourceToXmlStreamWriter(theResource, theEventWriter, theIncludedResource, resourceId, theEncodeContext);
	}

	private void encodeResourceToXmlStreamWriter(IBaseResource theResource, XMLStreamWriter theEventWriter, boolean theContainedResource, IIdType theResourceId, EncodeContext theEncodeContext) throws XMLStreamException {
		RuntimeResourceDefinition resDef = getContext().getResourceDefinition(theResource);
		if (resDef == null) {
			throw new ConfigurationException(Msg.code(1854) + "Unknown resource type: " + theResource.getClass());
		}

		if (!theContainedResource) {
			setContainedResources(getContext().newTerser().containResources(theResource));
		}

		theEventWriter.writeStartElement(resDef.getName());
		theEventWriter.writeDefaultNamespace(FHIR_NS);

		if (theResource instanceof IAnyResource) {
			// HL7.org Structures
			if (theResourceId != null) {
				writeCommentsPre(theEventWriter, theResourceId);
				theEventWriter.writeStartElement("id");
				theEventWriter.writeAttribute("value", theResourceId.getIdPart());
				encodeExtensionsIfPresent(theResource, theEventWriter, theResourceId, false, theEncodeContext);
				theEventWriter.writeEndElement();
				writeCommentsPost(theEventWriter, theResourceId);
			}

			encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, theContainedResource, new CompositeChildElement(resDef, theEncodeContext), theEncodeContext);

		} else {

			// DSTU2+

			IResource resource = (IResource) theResource;
			if (theResourceId != null) {
          /*	writeCommentsPre(theEventWriter, theResourceId);
              writeOptionalTagWithValue(theEventWriter, "id", theResourceId.getIdPart());
					    writeCommentsPost(theEventWriter, theResourceId);*/
				theEventWriter.writeStartElement("id");
				theEventWriter.writeAttribute("value", theResourceId.getIdPart());
				encodeExtensionsIfPresent(theResource, theEventWriter, theResourceId, false, theEncodeContext);
				theEventWriter.writeEndElement();
				writeCommentsPost(theEventWriter, theResourceId);
			}

			InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			IdDt resourceId = resource.getId();
			String versionIdPart = resourceId.getVersionIdPart();
			if (isBlank(versionIdPart)) {
				versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
			}
			List<BaseCodingDt> securityLabels = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.SECURITY_LABELS);
			List<? extends IIdType> profiles = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.PROFILES);
			profiles = super.getProfileTagsForEncoding(resource, profiles);

			TagList tags = getMetaTagsForEncoding((resource), theEncodeContext);

			if (super.shouldEncodeResourceMeta(resource) && ElementUtil.isEmpty(versionIdPart, updated, securityLabels, tags, profiles) == false) {
				theEventWriter.writeStartElement("meta");
				if (shouldEncodePath(resource, "meta.versionId")) {
					writeOptionalTagWithValue(theEventWriter, "versionId", versionIdPart);
				}
				if (updated != null) {
					if (shouldEncodePath(resource, "meta.lastUpdated")) {
						writeOptionalTagWithValue(theEventWriter, "lastUpdated", updated.getValueAsString());
					}
				}

				for (IIdType profile : profiles) {
					theEventWriter.writeStartElement("profile");
					theEventWriter.writeAttribute("value", profile.getValue());
					theEventWriter.writeEndElement();
				}
				for (BaseCodingDt securityLabel : securityLabels) {
					theEventWriter.writeStartElement("security");
					encodeCompositeElementToStreamWriter(resource, securityLabel, theEventWriter, theContainedResource, null, theEncodeContext);
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
				encodeCompositeElementToStreamWriter(theResource, theResource, theEventWriter, theContainedResource, new CompositeChildElement(resDef, theEncodeContext), theEncodeContext);
			}

		}

		theEventWriter.writeEndElement();
	}

	private void encodeUndeclaredExtensions(IBaseResource theResource, XMLStreamWriter theEventWriter, List<? extends IBaseExtension<?, ?>> theExtensions, String tagName, boolean theIncludedResource, EncodeContext theEncodeContext)
		throws XMLStreamException, DataFormatException {
		for (IBaseExtension<?, ?> next : theExtensions) {
			if (next == null || (ElementUtil.isEmpty(next.getValue()) && next.getExtension().isEmpty())) {
				continue;
			}

			writeCommentsPre(theEventWriter, next);

			theEventWriter.writeStartElement(tagName);

			String elementId = getCompositeElementId(next);
			if (isNotBlank(elementId)) {
				theEventWriter.writeAttribute("id", elementId);
			}

			String url = getExtensionUrl(next.getUrl());
			if (isNotBlank(url)) {
				theEventWriter.writeAttribute("url", url);
			}

			if (next.getValue() != null) {
				IBaseDatatype value = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = getContext().getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				BaseRuntimeElementDefinition<?> childDef;
				if (childName == null) {
					childDef = getContext().getElementDefinition(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException(Msg.code(1855) + "Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
					childName = RuntimeChildUndeclaredExtensionDefinition.createExtensionChildName(childDef);
				} else {
					childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException(Msg.code(1856) + "Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
				}
				encodeChildElementToStreamWriter(theResource, theEventWriter, extDef, value, childName, childDef, null, theIncludedResource, null, theEncodeContext);
			}

			// child extensions
			encodeExtensionsIfPresent(theResource, theEventWriter, next, theIncludedResource, theEncodeContext);

			theEventWriter.writeEndElement();

			writeCommentsPost(theEventWriter, next);

		}
	}


	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
		if (theDt == null || theDt.getValue() == null) {
			return;
		}

		List<XMLEvent> events = XmlUtil.parse(theDt.getValue());
		boolean firstElement = true;

		for (XMLEvent event : events) {
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
					}
					for (Iterator<?> attrIter = se.getAttributes(); attrIter.hasNext(); ) {
						Attribute next = (Attribute) attrIter.next();
						if (isBlank(next.getName().getNamespaceURI())) {
							theEventWriter.writeAttribute(next.getName().getLocalPart(), next.getValue());
						} else {
							theEventWriter.writeAttribute(next.getName().getPrefix(), next.getName().getNamespaceURI(), next.getName().getLocalPart(), next.getValue());
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

	private <T extends IBaseResource> T parseResource(Class<T> theResourceType, XMLEventReader theStreamReader) {
		ParserState<T> parserState = ParserState.getPreResourceInstance(this, theResourceType, getContext(), false, getErrorHandler());
		return doXmlLoop(theStreamReader, parserState);
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	/**
	 * This is just to work around the fact that casting java.util.List<ca.uhn.fhir.model.api.ExtensionDt> to
	 * java.util.List<? extends org.hl7.fhir.instance.model.api.IBaseExtension<?, ?>> seems to be
	 * rejected by the compiler some of the time.
	 */
	private <Q extends IBaseExtension<?, ?>> List<IBaseExtension<?, ?>> toBaseExtensionList(final List<Q> theList) {
		List<IBaseExtension<?, ?>> retVal = new ArrayList<IBaseExtension<?, ?>>(theList.size());
		retVal.addAll(theList);
		return retVal;
	}

	private void writeCommentsPost(XMLStreamWriter theEventWriter, IBase theElement) throws XMLStreamException {
		if (theElement != null && theElement.hasFormatComment()) {
			for (String next : theElement.getFormatCommentsPost()) {
				if (isNotBlank(next)) {
					theEventWriter.writeComment(next);
				}
			}
		}
	}

	private void writeCommentsPre(XMLStreamWriter theEventWriter, IBase theElement) throws XMLStreamException {
		if (theElement != null && theElement.hasFormatComment()) {
			for (String next : theElement.getFormatCommentsPre()) {
				if (isNotBlank(next)) {
					theEventWriter.writeComment(next);
				}
			}
		}
	}

	private void writeOptionalTagWithValue(XMLStreamWriter theEventWriter, String theName, String theValue) throws XMLStreamException {
		if (StringUtils.isNotBlank(theValue)) {
			theEventWriter.writeStartElement(theName);
			theEventWriter.writeAttribute("value", theValue);
			theEventWriter.writeEndElement();
		}
	}

}
