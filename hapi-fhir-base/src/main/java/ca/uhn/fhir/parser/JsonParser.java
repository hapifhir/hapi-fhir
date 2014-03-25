package ca.uhn.fhir.parser;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.UndeclaredExtension;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;

public class JsonParser extends BaseParser implements IParser {

	private FhirContext myContext;
	private boolean myPrettyPrint;

	public JsonParser(FhirContext theContext) {
		myContext = theContext;
	}

	@Override
	public String encodeBundleToString(Bundle theBundle) throws DataFormatException, IOException {
		if (theBundle == null) {
			throw new NullPointerException("Bundle can not be null");
		}
		StringWriter stringWriter = new StringWriter();
		encodeBundleToWriter(theBundle, stringWriter);

		return stringWriter.toString();
	}

	@Override
	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException {
		JsonGenerator eventWriter = createJsonGenerator(theWriter);
		eventWriter.writeStartObject();

		eventWriter.write("resourceType", "Bundle");

		writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
		writeTagWithTextNode(eventWriter, "id", theBundle.getBundleId());
		writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
		writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());

		eventWriter.writeStartArray("link");
		writeAtomLink(eventWriter, "self", theBundle.getLinkSelf());
		writeAtomLink(eventWriter, "first", theBundle.getLinkFirst());
		writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious());
		writeAtomLink(eventWriter, "next", theBundle.getLinkNext());
		writeAtomLink(eventWriter, "last", theBundle.getLinkLast());
		writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase());
		eventWriter.writeEnd();

		writeOptionalTagWithTextNode(eventWriter, "totalResults", theBundle.getTotalResults());

		writeAuthor(theBundle, eventWriter);

		eventWriter.writeStartArray("entry");
		for (BundleEntry nextEntry : theBundle.getEntries()) {
			eventWriter.writeStartObject();

			writeTagWithTextNode(eventWriter, "title", nextEntry.getTitle());
			writeTagWithTextNode(eventWriter, "id", nextEntry.getEntryId());

			eventWriter.writeStartArray("link");
			writeAtomLink(eventWriter, "self", nextEntry.getLinkSelf());
			eventWriter.writeEnd();

			writeOptionalTagWithTextNode(eventWriter, "updated", nextEntry.getUpdated());
			writeOptionalTagWithTextNode(eventWriter, "published", nextEntry.getPublished());

			writeAuthor(nextEntry, eventWriter);

			IResource resource = nextEntry.getResource();
			encodeResourceToJsonStreamWriter(resource, eventWriter, "content");

			eventWriter.writeEnd(); // entry object
		}
		eventWriter.writeEnd(); // entry array

		eventWriter.writeEnd();
		eventWriter.close();
	}

	@Override
	public String encodeResourceToString(IResource theResource) throws DataFormatException, IOException {
		Writer stringWriter = new StringWriter();
		encodeResourceToWriter(theResource, stringWriter);
		return stringWriter.toString();
	}

	@Override
	public void encodeResourceToWriter(IResource theResource, Writer theWriter) throws IOException {
		JsonGenerator eventWriter = createJsonGenerator(theWriter);

		encodeResourceToJsonStreamWriter(theResource, eventWriter, null);
		eventWriter.flush();
	}

	@Override
	public Bundle parseBundle(Reader theReader) {
		JsonReader reader = Json.createReader(theReader);
		JsonObject object = reader.readObject();

		JsonValue resourceTypeObj = object.get("resourceType");
		assertObjectOfType(resourceTypeObj, JsonValue.ValueType.STRING, "resourceType");
		String resourceType = ((JsonString) resourceTypeObj).getString();
		if (!"Bundle".equals(resourceType)) {
			throw new DataFormatException("Trying to parse bundle but found resourceType other than 'Bundle'. Found: '" + resourceType + "'");
		}

		ParserState<Bundle> state = ParserState.getPreAtomInstance(myContext, true);
		state.enteringNewElement(null, "feed");

		parseBundleChildren(object, state);

		state.endingElement();

		Bundle retVal = state.getObject();

		return retVal;
	}

	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		return parseResource(theResourceType, new StringReader(theMessageString));
	}

	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, Reader theReader) {
		JsonReader reader = Json.createReader(theReader);
		JsonObject object = reader.readObject();

		JsonValue resourceTypeObj = object.get("resourceType");
		assertObjectOfType(resourceTypeObj, JsonValue.ValueType.STRING, "resourceType");
		String resourceType = ((JsonString) resourceTypeObj).getString();

		RuntimeResourceDefinition def;
		if (theResourceType != null) {
			def = myContext.getResourceDefinition(theResourceType);
		} else {
			def = myContext.getResourceDefinition(resourceType);
		}

		ParserState<? extends IResource> state = ParserState.getPreResourceInstance(def.getImplementingClass(), myContext, true);
		state.enteringNewElement(null, def.getName());

		parseChildren(object, state);

		state.endingElement();

		@SuppressWarnings("unchecked")
		T retVal = (T) state.getObject();

		return retVal;
	}

	private void parseChildren(JsonObject theObject, ParserState<?> theState) {
		String elementId = null;
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("id".equals(nextName)) {
				elementId = theObject.getString(nextName);
				continue;
			} else if ("extension".equals(nextName)) {
				JsonArray array = theObject.getJsonArray(nextName);
				parseExtension(theState, array, false);
				continue;
			} else if ("modifierExtension".equals(nextName)) {
				JsonArray array = theObject.getJsonArray(nextName);
				parseExtension(theState, array, true);
				continue;
			} else if (nextName.charAt(0) == '_') {
				continue;
			}

			JsonValue nextVal = theObject.get(nextName);
			JsonValue alternateVal = theObject.get('_' + nextName);

			parseChildren(theState, nextName, nextVal, alternateVal);

		}

		if (elementId != null) {
			IElement object = theState.getObject();
			if (object instanceof IIdentifiableElement) {
				((IIdentifiableElement) object).setId(new IdDt(elementId));
			}
		}
	}

	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN;
	
	static {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add("title");
		hashSet.add("id");
		hashSet.add("updated");
		hashSet.add("published");
		BUNDLE_TEXTNODE_CHILDREN=Collections.unmodifiableSet(hashSet);
	}
	
	private void parseBundleChildren(JsonObject theObject, ParserState<?> theState) {
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("link".equals(nextName)) {
				JsonArray entries = theObject.getJsonArray(nextName);
				for (JsonValue jsonValue : entries) {
					theState.enteringNewElement(null, "link");
					JsonObject linkObj = (JsonObject) jsonValue;
					String rel = linkObj.getString("rel", null);
					String href = linkObj.getString("href", null);
					theState.attributeValue("rel", rel);
					theState.attributeValue("href", href);
					theState.endingElement();
				}
				continue;
			} else if ("entry".equals(nextName)) {
				JsonArray entries = theObject.getJsonArray(nextName);
				for (JsonValue jsonValue : entries) {
					theState.enteringNewElement(null, "entry");
					parseBundleChildren((JsonObject) jsonValue, theState);
					theState.endingElement();
				}
				continue;
			} else if (BUNDLE_TEXTNODE_CHILDREN.contains(nextName)) {
				theState.enteringNewElement(null, nextName);
				theState.string(theObject.getString(nextName, null));
				theState.endingElement();
				continue;
			}

			JsonValue nextVal = theObject.get(nextName);
			parseChildren(theState, nextName, nextVal, null);

		}
	}

	private void parseChildren(ParserState<?> theState, String theName, JsonValue theJsonVal, JsonValue theAlternateVal) {
		switch (theJsonVal.getValueType()) {
		case ARRAY: {
			JsonArray nextArray = (JsonArray) theJsonVal;
			JsonArray nextAlternateArray = (JsonArray) theAlternateVal;
			for (int i = 0; i < nextArray.size(); i++) {
				JsonValue nextObject = nextArray.get(i);
				JsonValue nextAlternate = null;
				if (nextAlternateArray != null) {
					nextAlternate = nextAlternateArray.get(i);
				}
				parseChildren(theState, theName, nextObject, nextAlternate);
			}
			break;
		}
		case OBJECT: {
			theState.enteringNewElement(null, theName);
			JsonObject nextObject = (JsonObject) theJsonVal;
			boolean preResource = false;
			if (theState.isPreResource()) {
				String resType = nextObject.getString("resourceType");
				if (isBlank(resType)) {
					throw new DataFormatException("Missing 'resourceType' from resource");
				}
				theState.enteringNewElement(null, resType);
				preResource = true;
			}
			parseChildren(nextObject, theState);
			if (preResource) {
				theState.endingElement();
			}
			theState.endingElement();
			break;
		}
		case STRING: {
			JsonString nextValStr = (JsonString) theJsonVal;
			theState.enteringNewElement(null, theName);
			theState.attributeValue("value", nextValStr.getString());
			parseAlternates(theAlternateVal, theState);
			theState.endingElement();
			break;
		}
		case NUMBER:
		case FALSE:
		case TRUE:
			theState.enteringNewElement(null, theName);
			theState.attributeValue("value", theJsonVal.toString());
			parseAlternates(theAlternateVal, theState);
			theState.endingElement();
			break;
		case NULL:
			break;
		}
	}

	private void parseAlternates(JsonValue theAlternateVal, ParserState<?> theState) {
		if (theAlternateVal == null || theAlternateVal.getValueType() == ValueType.NULL) {
			return;
		}
		JsonObject alternate = (JsonObject) theAlternateVal;
		for (Entry<String, JsonValue> nextEntry : alternate.entrySet()) {
			String nextKey = nextEntry.getKey();
			JsonValue nextVal = nextEntry.getValue();
			if ("extension".equals(nextKey)) {
				boolean isModifier = false;
				JsonArray array = (JsonArray) nextEntry.getValue();
				parseExtension(theState, array, isModifier);
			} else if ("modifierExtension".equals(nextKey)) {
				boolean isModifier = true;
				JsonArray array = (JsonArray) nextEntry.getValue();
				parseExtension(theState, array, isModifier);
			} else if ("id".equals(nextKey)) {
				switch (nextVal.getValueType()) {
				case STRING:
					theState.attributeValue("id", ((JsonString) nextVal).getString());
					break;
				case NULL:
					break;
				default:
					break;
				}
			}
		}
	}

	private void parseExtension(ParserState<?> theState, JsonArray array, boolean theIsModifier) {
		// TODO: use theIsModifier
		for (int i = 0; i < array.size(); i++) {
			JsonObject nextExtObj = array.getJsonObject(i);
			String url = nextExtObj.getString("url");
			theState.enteringNewElementExtension(null, url, theIsModifier);
			for (Iterator<String> iter = nextExtObj.keySet().iterator(); iter.hasNext();) {
				String next = iter.next();
				if ("url".equals(next)) {
					continue;
				} else if ("extension".equals(next)) {
					JsonArray jsonVal = (JsonArray) nextExtObj.get(next);
					parseExtension(theState, jsonVal, false);
				} else if ("modifierExtension".equals(next)) {
					JsonArray jsonVal = (JsonArray) nextExtObj.get(next);
					parseExtension(theState, jsonVal, true);
				} else {
					JsonValue jsonVal = nextExtObj.get(next);
					parseChildren(theState, next, jsonVal, null);
				}
			}
			theState.endingElement();
		}
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
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

	private void assertObjectOfType(JsonValue theResourceTypeObj, ValueType theValueType, String thePosition) {
		if (theResourceTypeObj.getValueType() != theValueType) {
			throw new DataFormatException("Invalid content of element " + thePosition + ", expected " + theValueType);
		}
	}

	private JsonGenerator createJsonGenerator(Writer theWriter) {
		Map<String, Object> properties = new HashMap<String, Object>(1);
		if (myPrettyPrint) {
			properties.put(JsonGenerator.PRETTY_PRINTING, myPrettyPrint);
		}
		JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
		JsonGenerator eventWriter = jgf.createGenerator(theWriter);
		return eventWriter;
	}

	private void encodeChildElementToStreamWriter(JsonGenerator theWriter, IElement theValue, BaseRuntimeElementDefinition<?> theChildDef, String theChildName) throws IOException {

		switch (theChildDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			IPrimitiveDatatype<?> value = (IPrimitiveDatatype<?>) theValue;
			if (value instanceof IntegerDt) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((IntegerDt) value).getValue());
				} else {
					theWriter.write(((IntegerDt) value).getValue());
				}
			} else if (value instanceof DecimalDt) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((DecimalDt) value).getValue());
				} else {
					theWriter.write(((DecimalDt) value).getValue());
				}
			} else if (value instanceof BooleanDt) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((BooleanDt) value).getValue());
				} else {
					theWriter.write(((BooleanDt) value).getValue());
				}
			} else {
				String valueStr = value.getValueAsString();
				if (theChildName != null) {
					theWriter.write(theChildName, valueStr);
				} else {
					theWriter.write(valueStr);
				}
			}
			break;
		}
		case RESOURCE_BLOCK:
		case COMPOSITE_DATATYPE: {
			BaseRuntimeElementCompositeDefinition<?> childCompositeDef = (BaseRuntimeElementCompositeDefinition<?>) theChildDef;
			if (theChildName != null) {
				theWriter.writeStartObject(theChildName);
			} else {
				theWriter.flush();// TODO: remove
				theWriter.writeStartObject();
			}
			encodeCompositeElementToStreamWriter(theValue, theWriter, childCompositeDef);
			theWriter.writeEnd();
			break;
		}
		case RESOURCE_REF: {
			ResourceReferenceDt value = (ResourceReferenceDt) theValue;
			if (theChildName != null) {
				theWriter.writeStartObject(theChildName);
			} else {
				theWriter.writeStartObject();
			}

			String reference = value.getReference().getValue();
			if (StringUtils.isBlank(reference)) {
				if (value.getResourceType() != null && StringUtils.isNotBlank(value.getResourceId())) {
					reference = myContext.getResourceDefinition(value.getResourceType()).getName() + '/' + value.getResourceId();
				}
			}

			if (StringUtils.isNotBlank(reference)) {
				theWriter.write("resource", reference);
			}
			if (value.getDisplay().isEmpty() == false) {
				theWriter.write("display", value.getDisplay().getValueAsString());
			}
			theWriter.writeEnd();
			break;
		}
		case CONTAINED_RESOURCES: {
			theWriter.writeStartArray(theChildName);
			ContainedDt value = (ContainedDt) theValue;
			for (IResource next : value.getContainedResources()) {
				encodeResourceToJsonStreamWriter(next, theWriter, null);
			}
			theWriter.writeEnd();
			break;
		}
		case PRIMITIVE_XHTML: {
			XhtmlDt dt = (XhtmlDt) theValue;
			if (theChildName != null) {
				theWriter.write(theChildName, dt.getValueAsString());
			} else {
				theWriter.write(dt.getValueAsString());
			}
			break;
		}
		case UNDECL_EXT:
		default:
			throw new IllegalStateException("Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(IElement theElement, JsonGenerator theEventWriter, List<? extends BaseRuntimeChildDefinition> theChildren) throws IOException {
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
					super.throwExceptionForUnknownChildType(nextChild, type);
				}

				if (nextChild instanceof RuntimeChildDeclaredExtensionDefinition) {

					// TODO: hold and return
					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
					if (extDef.isModifier()) {
						theEventWriter.writeStartObject("modifierExtension");
					} else {
						theEventWriter.writeStartObject("extension");
					}

					String extensionUrl = nextChild.getExtensionUrl();
					theEventWriter.write("url", extensionUrl);
					// theEventWriter.writeName(childName);
					encodeChildElementToStreamWriter(theEventWriter, nextValue, childDef, childName);

					theEventWriter.writeEnd();

				} else {

					if (currentChildName == null || !currentChildName.equals(childName)) {
						if (inArray) {
							theEventWriter.writeEnd();
						}
						if (nextChild.getMax() > 1 || nextChild.getMax() == Child.MAX_UNLIMITED) {
							theEventWriter.writeStartArray(childName);
							inArray = true;
							encodeChildElementToStreamWriter(theEventWriter, nextValue, childDef, null);
						} else {
							encodeChildElementToStreamWriter(theEventWriter, nextValue, childDef, childName);
						}
						currentChildName = childName;
					} else {
						theEventWriter.flush();// TODO: remove
						encodeChildElementToStreamWriter(theEventWriter, nextValue, childDef, null);
					}

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
				theEventWriter.writeEnd();
			}

			if (extensions.size() > 0 || modifierExtensions.size() > 0) {
				theEventWriter.writeStartArray('_' + currentChildName);

				for (int i = 0; i < valueIdx; i++) {
					boolean haveContent = false;
					if (extensions.size() > i && extensions.get(i) != null && extensions.get(i).isEmpty() == false) {
						haveContent = true;
						theEventWriter.writeStartObject();
						theEventWriter.writeStartArray("extension");
						for (HeldExtension nextExt : extensions.get(i)) {
							nextExt.write(theEventWriter);
						}
						theEventWriter.writeEnd();
						theEventWriter.writeEnd();
					}

					if (!haveContent) {
						// theEventWriter.writeEnd();
						theEventWriter.flush(); // TODO: remove
						theEventWriter.writeNull();
					}
				}

				// if (extensions.size() > 0) {
				//
				// theEventWriter.name(extType);
				// theEventWriter.beginArray();
				// for (ArrayList<HeldExtension> next : extensions) {
				// if (next == null || next.isEmpty()) {
				// theEventWriter.nullValue();
				// } else {
				// theEventWriter.beginArray();
				// // next.write(theEventWriter);
				// theEventWriter.endArray();
				// }
				// }
				// for (int i = extensions.size(); i < valueIdx; i++) {
				// theEventWriter.nullValue();
				// }
				// theEventWriter.endArray();
				// }

				theEventWriter.writeEnd();
				theEventWriter.flush(); // TODO: remove
			}

		}
	}

	private void encodeCompositeElementToStreamWriter(IElement theElement, JsonGenerator theEventWriter, BaseRuntimeElementCompositeDefinition<?> resDef) throws IOException, DataFormatException {
		encodeExtensionsIfPresent(theEventWriter, theElement);
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getExtensions());
		encodeCompositeElementChildrenToStreamWriter(theElement, theEventWriter, resDef.getChildren());
	}

	private void encodeExtensionsIfPresent(JsonGenerator theWriter, IElement theResource) throws IOException {
		if (theResource instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions res = (ISupportsUndeclaredExtensions) theResource;
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredExtensions(), "extension");
			encodeUndeclaredExtensions(theWriter, res.getUndeclaredModifierExtensions(), "modifierExtension");
		}
	}

	private void encodeResourceToJsonStreamWriter(IResource theResource, JsonGenerator theEventWriter, String theObjectNameOrNull) throws IOException {
		super.containResourcesForEncoding(theResource);

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);

		if (theObjectNameOrNull == null) {
			theEventWriter.writeStartObject();
		} else {
			theEventWriter.writeStartObject(theObjectNameOrNull);
		}

		theEventWriter.write("resourceType", resDef.getName());
		if (theResource.getId() != null && isNotBlank(theResource.getId().getValue())) {
			theEventWriter.write("id", theResource.getId().getValue());
		}

		encodeCompositeElementToStreamWriter(theResource, theEventWriter, resDef);

		theEventWriter.writeEnd();
	}

	private void encodeUndeclaredExtensions(JsonGenerator theWriter, List<UndeclaredExtension> extensions, String theTagName) throws IOException {
		if (extensions.isEmpty()) {
			return;
		}

		theWriter.writeStartArray(theTagName);

		for (UndeclaredExtension next : extensions) {

			theWriter.writeStartObject();

			theWriter.write("url", next.getUrl());

			if (next.getValue() != null) {
				IElement nextValue = next.getValue();
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(nextValue.getClass());
				// theWriter.writeName("value" + childDef.getName());
				encodeChildElementToStreamWriter(theWriter, nextValue, childDef, "value" + childDef.getName());
			}

			encodeUndeclaredExtensions(theWriter, next.getUndeclaredExtensions(), "extension");
			encodeUndeclaredExtensions(theWriter, next.getUndeclaredModifierExtensions(), "modifierExtension");

			theWriter.writeEnd();
		}

		theWriter.writeEnd();
	}

	private void writeAtomLink(JsonGenerator theEventWriter, String theRel, StringDt theLink) {
		if (isNotBlank(theLink.getValue())) {
			theEventWriter.writeStartObject();
			theEventWriter.write("rel", theRel);
			theEventWriter.write("href", theLink.getValue());
			theEventWriter.writeEnd();
		}
	}

	private void writeAuthor(BaseBundle theBundle, JsonGenerator eventWriter) {
		if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
			eventWriter.writeStartArray("author");
			eventWriter.writeStartObject();
			writeTagWithTextNode(eventWriter, "name", theBundle.getAuthorName());
			writeOptionalTagWithTextNode(eventWriter, "uri", theBundle.getAuthorUri());
			eventWriter.writeEnd();
			eventWriter.writeEnd();
		}
	}

	private void writeOptionalTagWithTextNode(JsonGenerator theEventWriter, String theElementName, IPrimitiveDatatype<?> theInstantDt) {
		String str = theInstantDt.getValueAsString();
		if (StringUtils.isNotBlank(str)) {
			theEventWriter.write(theElementName, theInstantDt.getValueAsString());
		}
	}

	private void writeTagWithTextNode(JsonGenerator theEventWriter, String theElementName, StringDt theStringDt) {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.write(theElementName, theStringDt.getValue());
		} else {
			theEventWriter.writeNull(theElementName);
		}
	}

	private class HeldExtension {

		private UndeclaredExtension myUndeclaredExtension;

		public HeldExtension(UndeclaredExtension theUndeclaredExtension) {
			myUndeclaredExtension = theUndeclaredExtension;
		}

		public void write(JsonGenerator theEventWriter) throws IOException {
			if (myUndeclaredExtension != null) {
				writeUndeclaredExt(theEventWriter, myUndeclaredExtension);
			}
		}

		private void writeUndeclaredExt(JsonGenerator theEventWriter, UndeclaredExtension ext) throws IOException {
			theEventWriter.writeStartObject();
			theEventWriter.write("url", ext.getUrl());

			IElement value = ext.getValue();
			if (value == null && ext.getAllUndeclaredExtensions().isEmpty()) {
				theEventWriter.writeNull();
			} else if (value == null) {
				theEventWriter.writeStartArray("extension");
				for (UndeclaredExtension next : ext.getUndeclaredExtensions()) {
					writeUndeclaredExt(theEventWriter, next);
				}
				theEventWriter.writeEnd();
			} else {
				BaseRuntimeElementDefinition<?> def = myContext.getElementDefinition(value.getClass());
				// theEventWriter.writeName("value" + def.getName());
				encodeChildElementToStreamWriter(theEventWriter, value, def, "value" + def.getName());
			}

			// theEventWriter.name(myUndeclaredExtension.get);

			theEventWriter.writeEnd();
		}

	}

}
