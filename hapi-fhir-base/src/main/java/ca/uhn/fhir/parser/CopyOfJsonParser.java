package ca.uhn.fhir.parser;


public class CopyOfJsonParser  {

//	private FhirContext myContext;
//
//	public CopyOfJsonParser(FhirContext theContext) {
//		myContext = theContext;
//	}
//
//	@Override
//	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
//		StringWriter stringWriter = new StringWriter();
//		encodeBundleToWriter(theBundle, stringWriter);
//
//		return stringWriter.toString();
//	}
//
//	@Override
//	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) {
//		try {
//			XMLStreamWriter eventWriter;
//			eventWriter = myXmlOutputFactory.createXMLStreamWriter(theWriter);
//			eventWriter = decorateStreamWriter(eventWriter);
//
//			eventWriter.writeStartElement("feed");
//			eventWriter.writeDefaultNamespace(ATOM_NS);
//
//			writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
//			writeTagWithTextNode(eventWriter, "id", theBundle.getId());
//
//			writeAtomLink(eventWriter, "self", theBundle.getLinkSelf());
//			writeAtomLink(eventWriter, "first", theBundle.getLinkFirst());
//			writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious());
//			writeAtomLink(eventWriter, "next", theBundle.getLinkNext());
//			writeAtomLink(eventWriter, "last", theBundle.getLinkLast());
//			writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase());
//
//			if (theBundle.getTotalResults().getValue() != null) {
//				eventWriter.writeStartElement("os", OPENSEARCH_NS, "totalResults");
//				eventWriter.writeNamespace("os", OPENSEARCH_NS);
//				eventWriter.writeCharacters(theBundle.getTotalResults().getValue().toString());
//				eventWriter.writeEndElement();
//			}
//
//			writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
//			writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());
//
//			if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
//				eventWriter.writeStartElement("author");
//				writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
//				writeOptionalTagWithTextNode(eventWriter, "uri", theBundle.getAuthorUri());
//				eventWriter.writeEndElement();
//			}
//
//			for (BundleEntry nextEntry : theBundle.getEntries()) {
//				eventWriter.writeStartElement("entry");
//
//				eventWriter.writeStartElement("content");
//				eventWriter.writeAttribute("type", "text/xml");
//
//				IResource resource = nextEntry.getResource();
//				encodeResourceToXmlStreamWriter(resource, eventWriter);
//
//				eventWriter.writeEndElement(); // content
//				eventWriter.writeEndElement(); // entry
//			}
//
//			eventWriter.writeEndElement();
//			eventWriter.close();
//		} catch (XMLStreamException e) {
//			throw new ConfigurationException("Failed to initialize STaX event factory", e);
//		}
//	}
//
//	private void encodeChildElementToStreamWriter(JsonWriter theEventWriter, IElement nextValue, String childName, BaseRuntimeElementDefinition<?> childDef, String theExtensionUrl) {
//		if (nextValue.isEmpty()) {
//			return;
//		}
//
//		switch (childDef.getChildType()) {
//		case PRIMITIVE_DATATYPE: {
//			IPrimitiveDatatype<?> pd = (IPrimitiveDatatype<?>) nextValue;
//			if (pd.getValue() != null) {
//				theEventWriter.name(childName);
//				if (pd instanceof IntegerDt) {
//					theEventWriter.value(((IntegerDt)pd).getValue());
//				} else if (pd instanceof DecimalDt) {
//					theEventWriter.value(((DecimalDt)pd).getValue());
//				} else if (pd instanceof BooleanDt) {
//					theEventWriter.value(((BooleanDt)pd).getValue());
//				} else {
//					String value = pd.getValueAsString();
//					theEventWriter.value(value);
//				}
//				theEventWriter.writeAttribute("value", value);
//				encodeExtensionsIfPresent(theEventWriter, nextValue);
//				theEventWriter.writeEndElement();
//			}
//			break;
//		}
//		case RESOURCE_BLOCK:
//		case COMPOSITE_DATATYPE: {
//			theEventWriter.writeStartElement(childName);
//			if (isNotBlank(theExtensionUrl)) {
//				theEventWriter.writeAttribute("url", theExtensionUrl);
//			}
//			BaseRuntimeElementCompositeDefinition<?> childCompositeDef = (BaseRuntimeElementCompositeDefinition<?>) childDef;
//			encodeCompositeElementToStreamWriter(nextValue, theEventWriter, childCompositeDef);
//			encodeExtensionsIfPresent(theEventWriter, nextValue);
//			theEventWriter.writeEndElement();
//			break;
//		}
//		case RESOURCE_REF: {
//			ResourceReferenceDt ref = (ResourceReferenceDt) nextValue;
//			if (!ref.isEmpty()) {
//				theEventWriter.writeStartElement(childName);
//				encodeResourceReferenceToStreamWriter(theEventWriter, ref);
//				theEventWriter.writeEndElement();
//			}
//			break;
//		}
//		case RESOURCE: {
//			throw new IllegalStateException(); // should not happen
//		}
//		case PRIMITIVE_XHTML: {
//			XhtmlDt dt = (XhtmlDt) nextValue;
//			if (dt.hasContent()) {
//				encodeXhtml(dt, theEventWriter);
//			}
//			break;
//		}
//		case UNDECL_EXT: {
//			throw new IllegalStateException("should not happen");
//		}
//		}
//
//	}
//
//	private void encodeCompositeElementChildrenToStreamWriter(IElement theElement, XMLStreamWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> children) throws XMLStreamException,
//			DataFormatException {
//		for (BaseRuntimeChildDefinition nextChild : children) {
//			List<? extends IElement> values = nextChild.getAccessor().getValues(theElement);
//			if (values == null || values.isEmpty()) {
//				continue;
//			}
//
//			for (IElement nextValue : values) {
//				if (nextValue == null) {
//					continue;
//				}
//				Class<? extends IElement> type = nextValue.getClass();
//				String childName = nextChild.getChildNameByDatatype(type);
//				String extensionUrl = nextChild.getExtensionUrl();
//				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
//				if (childDef == null) {
//					throw new IllegalStateException(nextChild + " has no child of type " + type);
//				}
//
//				if (extensionUrl != null && childName.equals("extension") == false) {
//					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
//					if (extDef.isModifier()) {
//						theEventWriter.writeStartElement("modifierExtension");
//					}else {
//						theEventWriter.writeStartElement("extension");
//					}
//					
//					theEventWriter.writeAttribute("url", extensionUrl);
//					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, null);
//					theEventWriter.writeEndElement();
//				} else {
//					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef, extensionUrl);
//				}
//			}
//		}
//	}
//
//	private void encodeCompositeElementToStreamWriter(IElement theElement, JsonWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws XMLStreamException,
//			DataFormatException {
//		encodeExtensionsIfPresent(theEventWriter, theElement);
//		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getExtensions());
//		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getChildren());
//	}
//
//	private void encodeExtensionsIfPresent(JsonWriter theWriter, IElement theResource)  {
//		if (theResource instanceof ISupportsUndeclaredExtensions) {
//			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theResource;
//			encodeUndeclaredExtensions(theWriter, res.getUndeclaredExtensions(), "extension");
//			encodeUndeclaredExtensions(theWriter, res.getUndeclaredModifierExtensions(), "modifierExtension");
//		}
//	}
//
//	private void encodeUndeclaredExtensions(JsonWriter theWriter, List<UndeclaredExtension> extensions, String theTagName) throws XMLStreamException {
//		if (extensions.isEmpty()) {
//			return;
//		}
//		
//		theWriter.name(theTagName);
//		theWriter.beginArray();
//		
//		for (UndeclaredExtension next : extensions) {
//			theWriter.name("url");
//			theWriter.value(next.getUrl());
//
//			if (next.getValue() != null) {
//				IElement nextValue = next.getValue();
//				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
//				String childName = extDef.getChildNameByDatatype(nextValue.getClass());
//				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
//				encodeChildElementToStreamWriter(theWriter, nextValue, childName, childDef, null);
//			}
//
//			// child extensions
//			encodeExtensionsIfPresent(theWriter, next);
//
//		}
//		
//		theWriter.endArray();
//	}
//
//	private void encodeResourceReferenceToStreamWriter(XMLStreamWriter theEventWriter, ResourceReferenceDt theRef) throws XMLStreamException {
//		if (!(theRef.getDisplay().isEmpty())) {
//			theEventWriter.writeStartElement("display");
//			theEventWriter.writeAttribute("value", theRef.getDisplay().getValue());
//			theEventWriter.writeEndElement();
//		}
//		if (!(theRef.getReference().isEmpty())) {
//			theEventWriter.writeStartElement("reference");
//			theEventWriter.writeAttribute("value", theRef.getReference().getValue());
//			theEventWriter.writeEndElement();
//		}
//	}
//
//
//	@Override
//	public String encodeResourceToString(IResource theResource) throws DataFormatException {
//		Writer stringWriter = new StringWriter();
//		encodeResourceToWriter(theResource, stringWriter);
//		return stringWriter.toString();
//	}
//
//	@Override
//	public void encodeResourceToWriter(IResource theResource, Writer theWriter) {
//		JsonWriter eventWriter = new JsonWriter(theWriter);
////		try {
//			encodeResourceToXmlStreamWriter(theResource, eventWriter);
//			eventWriter.flush();
////		} catch (XMLStreamException e) {
////			throw new ConfigurationException("Failed to initialize STaX event factory", e);
////		}
//	}
//
//
//	private void encodeResourceToXmlStreamWriter(IResource theResource, JsonWriter theEventWriter) throws XMLStreamException, DataFormatException {
//		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
//		
//		theEventWriter.beginObject();
//		
//		theEventWriter.name("resourceType");
//		theEventWriter.value(resDef.getName());
//		
//		encodeCompositeElementToStreamWriter(theResource, theEventWriter, resDef);
//
//		theEventWriter.writeEndElement();
//	}
//
//	private void encodeXhtml(XhtmlDt theDt, XMLStreamWriter theEventWriter) throws XMLStreamException {
//		if (theDt == null || theDt.getValue() == null) {
//			return;
//		}
//
//		boolean firstEvent = true;
//		for (XMLEvent event : theDt.getValue()) {
//			switch (event.getEventType()) {
//			case XMLStreamConstants.ATTRIBUTE:
//				Attribute attr = (Attribute) event;
//				if (isBlank(attr.getName().getPrefix())) {
//					if (isBlank(attr.getName().getNamespaceURI())) {
//						theEventWriter.writeAttribute(attr.getName().getLocalPart(), attr.getValue());
//					} else {
//						theEventWriter.writeAttribute(attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
//					}
//				} else {
//					theEventWriter.writeAttribute(attr.getName().getPrefix(), attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
//				}
//
//				break;
//			case XMLStreamConstants.CDATA:
//				theEventWriter.writeCData(((Characters) event).getData());
//				break;
//			case XMLStreamConstants.CHARACTERS:
//			case XMLStreamConstants.SPACE:
//				theEventWriter.writeCharacters(((Characters) event).getData());
//				break;
//			case XMLStreamConstants.COMMENT:
//				theEventWriter.writeComment(((Comment) event).getText());
//				break;
//			case XMLStreamConstants.END_ELEMENT:
//				theEventWriter.writeEndElement();
//				break;
//			case XMLStreamConstants.ENTITY_REFERENCE:
//				EntityReference er = (EntityReference) event;
//				theEventWriter.writeEntityRef(er.getName());
//				break;
//			case XMLStreamConstants.NAMESPACE:
//				Namespace ns = (Namespace) event;
//				theEventWriter.writeNamespace(ns.getPrefix(), ns.getNamespaceURI());
//				break;
//			case XMLStreamConstants.START_ELEMENT:
//				StartElement se = event.asStartElement();
//				if (firstEvent) {
//					theEventWriter.writeStartElement(se.getName().getLocalPart());
//					if (StringUtils.isBlank(se.getName().getPrefix())) {
//						theEventWriter.writeDefaultNamespace(se.getName().getNamespaceURI());
//					} else {
//						theEventWriter.writeNamespace(se.getName().getPrefix(), se.getName().getNamespaceURI());
//					}
//				} else {
//					if (isBlank(se.getName().getPrefix())) {
//						if (isBlank(se.getName().getNamespaceURI())) {
//							theEventWriter.writeStartElement(se.getName().getLocalPart());
//						} else {
//							if (StringUtils.isBlank(se.getName().getPrefix())) {
//								theEventWriter.writeStartElement(se.getName().getLocalPart());
//								theEventWriter.writeDefaultNamespace(se.getName().getNamespaceURI());
//							} else {
//								theEventWriter.writeStartElement(se.getName().getNamespaceURI(), se.getName().getLocalPart());
//							}
//						}
//					} else {
//						theEventWriter.writeStartElement(se.getName().getPrefix(), se.getName().getLocalPart(), se.getName().getNamespaceURI());
//					}
//				}
//				break;
//			case XMLStreamConstants.DTD:
//			case XMLStreamConstants.END_DOCUMENT:
//			case XMLStreamConstants.ENTITY_DECLARATION:
//			case XMLStreamConstants.NOTATION_DECLARATION:
//			case XMLStreamConstants.PROCESSING_INSTRUCTION:
//			case XMLStreamConstants.START_DOCUMENT:
//				break;
//			}
//
//			firstEvent = false;
//		}
//	}
//	
//	@Override
//	public Bundle parseBundle(Reader theReader) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public IResource parseResource(XMLEventReader theStreamReader) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public IResource parseResource(Class<? extends IResource> theResourceType, Reader theReader) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
