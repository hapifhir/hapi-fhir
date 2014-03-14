package ca.uhn.fhir.parser;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

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
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class JsonParser implements IParser {

	private FhirContext myContext;

	public JsonParser(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException {
		StringWriter stringWriter = new StringWriter();
		encodeBundleToWriter(theBundle, stringWriter);

		return stringWriter.toString();
	}

	@Override
	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) {
		// try {
		// XMLStreamWriter eventWriter;
		// eventWriter = myXmlOutputFactory.createXMLStreamWriter(theWriter);
		// eventWriter = decorateStreamWriter(eventWriter);
		//
		// eventWriter.writeStartElement("feed");
		// eventWriter.writeDefaultNamespace(ATOM_NS);
		//
		// writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
		// writeTagWithTextNode(eventWriter, "id", theBundle.getId());
		//
		// writeAtomLink(eventWriter, "self", theBundle.getLinkSelf());
		// writeAtomLink(eventWriter, "first", theBundle.getLinkFirst());
		// writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious());
		// writeAtomLink(eventWriter, "next", theBundle.getLinkNext());
		// writeAtomLink(eventWriter, "last", theBundle.getLinkLast());
		// writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase());
		//
		// if (theBundle.getTotalResults().getValue() != null) {
		// eventWriter.writeStartElement("os", OPENSEARCH_NS, "totalResults");
		// eventWriter.writeNamespace("os", OPENSEARCH_NS);
		// eventWriter.writeCharacters(theBundle.getTotalResults().getValue().toString());
		// eventWriter.writeEndElement();
		// }
		//
		// writeOptionalTagWithTextNode(eventWriter, "updated",
		// theBundle.getUpdated());
		// writeOptionalTagWithTextNode(eventWriter, "published",
		// theBundle.getPublished());
		//
		// if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
		// eventWriter.writeStartElement("author");
		// writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
		// writeOptionalTagWithTextNode(eventWriter, "uri",
		// theBundle.getAuthorUri());
		// eventWriter.writeEndElement();
		// }
		//
		// for (BundleEntry nextEntry : theBundle.getEntries()) {
		// eventWriter.writeStartElement("entry");
		//
		// eventWriter.writeStartElement("content");
		// eventWriter.writeAttribute("type", "text/xml");
		//
		// IResource resource = nextEntry.getResource();
		// encodeResourceToJsonStreamWriter(resource, eventWriter);
		//
		// eventWriter.writeEndElement(); // content
		// eventWriter.writeEndElement(); // entry
		// }
		//
		// eventWriter.writeEndElement();
		// eventWriter.close();
		// } catch (XMLStreamException e) {
		// throw new
		// ConfigurationException("Failed to initialize STaX event factory", e);
		// }
	}

	private void encodeCompositeElementToStreamWriter(IElement theElement, JsonWriter theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws IOException, DataFormatException {
		encodeExtensionsIfPresent(theEventWriter, theElement);
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getExtensions());
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getChildren());
	}

	private class HeldExtension {

		public HeldExtension(UndeclaredExtension theUndeclaredExtension) {

		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(IElement theElement, JsonWriter theEventWriter, List<? extends BaseRuntimeChildDefinition> theChildren) throws IOException {
		for (BaseRuntimeChildDefinition nextChild : theChildren) {
			List<? extends IElement> values = nextChild.getAccessor().getValues(theElement);
			if (values == null || values.isEmpty()) {
				continue;
			}

			String currentChildName = null;
			boolean inArray = false;

			ArrayList<ArrayList<HeldExtension>> extensions = new ArrayList<ArrayList<HeldExtension>>(0);
			ArrayList<ArrayList<HeldExtension>> modifierExtensions = new ArrayList<ArrayList<HeldExtension>>(0);

			int valueIdx = 0;
			for (IElement nextValue : values) {
				if (nextValue == null || nextValue.isEmpty()) {
					continue;
				}

				Class<? extends IElement> type = nextValue.getClass();
				String childName = nextChild.getChildNameByDatatype(type);
				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
				if (childDef == null) {
					throw new IllegalStateException(nextChild + " has no child of type " + type);
				}

				if (nextChild instanceof RuntimeChildDeclaredExtensionDefinition) {

					// TODO: hold and return
					 RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
					 if (extDef.isModifier()) {
					 theEventWriter.name("modifierExtension");
					 } else {
					 theEventWriter.name("extension");
					 }
					
					 theEventWriter.beginObject();
					 theEventWriter.name("url");
					 String extensionUrl = nextChild.getExtensionUrl();
					 theEventWriter.value(extensionUrl);
					 theEventWriter.name(childName);
					 encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef);
					 theEventWriter.endObject();

				} else {

					if (currentChildName == null || !currentChildName.equals(childName)) {
						if (inArray) {
							theEventWriter.endArray();
						}
						theEventWriter.name(childName);
						if (nextChild.getMax() > 1 || nextChild.getMax() == Child.MAX_UNLIMITED) {
							theEventWriter.beginArray();
							inArray = true;
						}
						currentChildName = childName;
					}

					encodeChildElementToStreamWriter(theEventWriter, nextValue, childName, childDef);

					if (nextValue instanceof ISupportsUndeclaredExtensions) {
						List<UndeclaredExtension> ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredExtensions();
						addToHeldExtensions(valueIdx, ext, extensions);

						ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredModifierExtensions();
						addToHeldExtensions(valueIdx, ext, modifierExtensions);
					}

				}

				valueIdx++;
			}

			if (inArray) {
				theEventWriter.endArray();
			}
			
			String extType = "extension";
			if (extensions.size() > 0 || modifierExtensions.size() > 0) {
				theEventWriter.name('_' + currentChildName);
				theEventWriter.beginObject();

				if (extensions.size() > 0) {
					
					theEventWriter.name(extType);
					theEventWriter.beginArray();
					for (ArrayList<HeldExtension> next : extensions) {
						if (next == null || next.isEmpty()) {
							theEventWriter.nullValue();
						}else {
							theEventWriter.beginArray();
//							next.write(theEventWriter);
							theEventWriter.endArray();
						}
					}
					for (int i = extensions.size(); i < valueIdx; i++) {
						theEventWriter.nullValue();
					}
					theEventWriter.endArray();
				}
				
				theEventWriter.endObject();
			}

		}
	}

	private void addToHeldExtensions(int valueIdx, List<UndeclaredExtension> ext, ArrayList<ArrayList<HeldExtension>> list) {
		if (ext.size() > 0) {
			list.ensureCapacity(valueIdx);
			while (list.size() <= valueIdx) {
				list.add(null);
			}
			if (list.get(valueIdx) == null) {
				list.set(valueIdx, new ArrayList<JsonParser.HeldExtension>());
			}
			for (UndeclaredExtension next : ext) {
				list.get(valueIdx).add(new HeldExtension(next));
			}
		}
	}

	private void encodeExtensionsIfPresent(JsonWriter theWriter, IElement theResource) throws IOException {
		if (theResource instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theResource;
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredExtensions(), "extension");
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredModifierExtensions(), "modifierExtension");
		}
	}

	private void encodeUndeclaredExtensions(JsonWriter theWriter, List<UndeclaredExtension> extensions, String theTagName) throws IOException {
		if (extensions.isEmpty()) {
			return;
		}

		theWriter.name(theTagName);
		theWriter.beginArray();

		for (UndeclaredExtension next : extensions) {

			theWriter.beginObject();
			theWriter.name("url");
			theWriter.value(next.getUrl());

			if (next.getValue() != null) {
				IElement nextValue = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(nextValue.getClass());
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
				encodeChildElementToStreamWriter(theWriter, nextValue, childName, childDef);
			}

			encodeUndeclaredExtensions(theWriter, next.getUndeclaredExtensions(), "extension");
			encodeUndeclaredExtensions(theWriter, next.getUndeclaredModifierExtensions(), "modifierExtension");

			theWriter.endObject();
		}

		theWriter.endArray();
	}

	private void encodeChildElementToStreamWriter(JsonWriter theWriter, IElement theValue, String theChildName, BaseRuntimeElementDefinition<?> theChildDef) throws IOException {

		switch (theChildDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			IPrimitiveDatatype<?> value = (IPrimitiveDatatype<?>) theValue;
			if (value instanceof IntegerDt) {
				theWriter.value(((IntegerDt) value).getValue());
			} else if (value instanceof DecimalDt) {
				theWriter.value(((DecimalDt) value).getValue());
			} else if (value instanceof BooleanDt) {
				theWriter.value(((BooleanDt) value).getValue());
			} else {
				String valueStr = value.getValueAsString();
				theWriter.value(valueStr);
			}
			break;
		}
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			BaseRuntimeElementCompositeDefinition<?> childCompositeDef = (BaseRuntimeElementCompositeDefinition<?>) theChildDef;
			theWriter.beginObject();
			encodeCompositeElementToStreamWriter(theValue, theWriter, childCompositeDef);
			theWriter.endObject();
			break;
		}
		case RESOURCE_REF: {
			ResourceReferenceDt value = (ResourceReferenceDt) theValue;
			theWriter.beginObject();
			if (value.getReference().isEmpty() == false) {
				theWriter.name("resource");
				theWriter.value(value.getReference().getValueAsString());
			}
			if (value.getDisplay().isEmpty() == false) {
				theWriter.name("display");
				theWriter.value(value.getDisplay().getValueAsString());
			}
			theWriter.endObject();
			break;
		}
		case PRIMITIVE_XHTML: {
			XhtmlDt dt = (XhtmlDt) theValue;
			theWriter.value(dt.getValueAsString());
			break;
		}
		case UNDECL_EXT:
		default:
			throw new IllegalStateException("Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	@Override
	public String encodeResourceToString(IResource theResource) throws DataFormatException, IOException {
		Writer stringWriter = new StringWriter();
		encodeResourceToWriter(theResource, stringWriter);
		return stringWriter.toString();
	}

	@Override
	public void encodeResourceToWriter(IResource theResource, Writer theWriter) throws IOException {
		JsonWriter eventWriter = new JsonWriter(theWriter);
		eventWriter.setIndent("  ");
		// try {
		encodeResourceToJsonStreamWriter(theResource, eventWriter);
		eventWriter.flush();
		// } catch (XMLStreamException e) {
		// throw new
		// ConfigurationException("Failed to initialize STaX event factory", e);
		// }
	}

	private void encodeResourceToJsonStreamWriter(IResource theResource, JsonWriter theEventWriter) throws IOException {
		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);

		theEventWriter.beginObject();

		theEventWriter.name("resourceType");
		theEventWriter.value(resDef.getName());

		encodeCompositeElementToStreamWriter(theResource, theEventWriter, resDef);

		theEventWriter.endObject();
	}

	@Override
	public Bundle parseBundle(Reader theReader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle parseBundle(String theMessageString) throws ConfigurationException, DataFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource parseResource(String theMessageString) throws ConfigurationException, DataFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource parseResource(Reader theReader) throws ConfigurationException, DataFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IResource parseResource(Class<? extends IResource> theResourceType, Reader theReader) {
		// TODO Auto-generated method stub
		return null;
	}

}
