package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import static org.apache.commons.lang3.StringUtils.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
import javax.json.stream.JsonParsingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.dstu.composite.ContainedDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;

public class JsonParser extends BaseParser implements IParser {

	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParser.HeldExtension.class);

	static {
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.add("title");
		hashSet.add("id");
		hashSet.add("updated");
		hashSet.add("published");
		BUNDLE_TEXTNODE_CHILDREN = Collections.unmodifiableSet(hashSet);
	}

	private FhirContext myContext;

	private boolean myPrettyPrint;

	public JsonParser(FhirContext theContext) {
		super(theContext);
		myContext = theContext;
	}

	private void addToHeldExtensions(int valueIdx, List<ExtensionDt> ext, ArrayList<ArrayList<HeldExtension>> list) {
		if (ext.size() > 0) {
			list.ensureCapacity(valueIdx);
			while (list.size() <= valueIdx) {
				list.add(null);
			}
			if (list.get(valueIdx) == null) {
				list.set(valueIdx, new ArrayList<JsonParser.HeldExtension>());
			}
			for (ExtensionDt next : ext) {
				list.get(valueIdx).add(new HeldExtension(next));
			}
		}
	}

	private void assertObjectOfType(JsonValue theResourceTypeObj, ValueType theValueType, String thePosition) {
		if (theResourceTypeObj == null) {
			throw new DataFormatException("Invalid JSON content detected, missing required element: '" + thePosition + "'");
		}
		
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

	@Override
	public void encodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException {
		JsonGenerator eventWriter = createJsonGenerator(theWriter);
		eventWriter.writeStartObject();

		eventWriter.write("resourceType", "Bundle");

		writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
		writeTagWithTextNode(eventWriter, "id", theBundle.getBundleId());
		writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
		writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());

		boolean linkStarted = false;
		linkStarted = writeAtomLink(eventWriter, "self", theBundle.getLinkSelf(), linkStarted);
		linkStarted = writeAtomLink(eventWriter, "first", theBundle.getLinkFirst(), linkStarted);
		linkStarted = writeAtomLink(eventWriter, "previous", theBundle.getLinkPrevious(), linkStarted);
		linkStarted = writeAtomLink(eventWriter, "next", theBundle.getLinkNext(), linkStarted);
		linkStarted = writeAtomLink(eventWriter, "last", theBundle.getLinkLast(), linkStarted);
		linkStarted = writeAtomLink(eventWriter, "fhir-base", theBundle.getLinkBase(), linkStarted);
		if (linkStarted) {
			eventWriter.writeEnd();
		}
		
		writeCategories(eventWriter, theBundle.getCategories());

		writeOptionalTagWithTextNode(eventWriter, "totalResults", theBundle.getTotalResults());

		writeAuthor(theBundle, eventWriter);

		eventWriter.writeStartArray("entry");
		for (BundleEntry nextEntry : theBundle.getEntries()) {
			eventWriter.writeStartObject();

			boolean deleted = nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false;
			if (deleted) {
				writeTagWithTextNode(eventWriter, "deleted", nextEntry.getDeletedAt());
			}
			writeTagWithTextNode(eventWriter, "title", nextEntry.getTitle());
			writeTagWithTextNode(eventWriter, "id", nextEntry.getId());

			linkStarted = false;
			linkStarted = writeAtomLink(eventWriter, "self", nextEntry.getLinkSelf(), linkStarted);
			linkStarted = writeAtomLink(eventWriter, "alternate", nextEntry.getLinkAlternate(), linkStarted);
			linkStarted = writeAtomLink(eventWriter, "search", nextEntry.getLinkSearch(), linkStarted);
			if (linkStarted) {
				eventWriter.writeEnd();
			}

			writeOptionalTagWithTextNode(eventWriter, "updated", nextEntry.getUpdated());
			writeOptionalTagWithTextNode(eventWriter, "published", nextEntry.getPublished());

			writeCategories(eventWriter, nextEntry.getCategories());

			writeAuthor(nextEntry, eventWriter);

			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(resource);
				encodeResourceToJsonStreamWriter(resDef, resource, eventWriter, "content", false);
			}

			if (nextEntry.getSummary().isEmpty()==false) {
				eventWriter.write("summary", nextEntry.getSummary().getValueAsString());
			}
			
			eventWriter.writeEnd(); // entry object
		}
		eventWriter.writeEnd(); // entry array

		eventWriter.writeEnd();
		eventWriter.flush();
	}

	private void writeCategories(JsonGenerator eventWriter, TagList categories) {
		if (categories != null && categories.size() > 0) {
			eventWriter.writeStartArray("category");
			for (Tag next : categories) {
				eventWriter.writeStartObject();
				eventWriter.write("term", defaultString(next.getTerm()));
				eventWriter.write("label", defaultString(next.getLabel()));
				eventWriter.write("scheme", defaultString(next.getScheme()));
				eventWriter.writeEnd();
			}
			eventWriter.writeEnd();
		}
	}

	private void encodeChildElementToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, JsonGenerator theWriter, IElement theValue, BaseRuntimeElementDefinition<?> theChildDef,
			String theChildName, boolean theIsSubElementWithinResource) throws IOException {

		switch (theChildDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			IPrimitiveDatatype<?> value = (IPrimitiveDatatype<?>) theValue;
			if (isBlank(value.getValueAsString())) {
				break;
			}
			
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
				theWriter.writeStartObject();
			}
			if (theValue instanceof ExtensionDt) {
				theWriter.write("url", ((ExtensionDt) theValue).getUrlAsString());
			}
			encodeCompositeElementToStreamWriter(theResDef, theResource, theValue, theWriter, childCompositeDef, theIsSubElementWithinResource);
			theWriter.writeEnd();
			break;
		}
		case RESOURCE_REF: {
			ResourceReferenceDt referenceDt = (ResourceReferenceDt) theValue;
			if (theChildName != null) {
				theWriter.writeStartObject(theChildName);
			} else {
				theWriter.writeStartObject();
			}

			String reference = determineReferenceText(referenceDt); 
					
			if (StringUtils.isNotBlank(reference)) {
				theWriter.write(XmlParser.RESREF_REFERENCE, reference);
			}
			if (referenceDt.getDisplay().isEmpty() == false) {
				theWriter.write(XmlParser.RESREF_DISPLAY, referenceDt.getDisplay().getValueAsString());
			}
			theWriter.writeEnd();
			break;
		}
		case CONTAINED_RESOURCES: {
			theWriter.writeStartArray(theChildName);
			ContainedDt value = (ContainedDt) theValue;
			for (IResource next : value.getContainedResources()) {
				if (getContainedResources().getResourceId(next)!=null) {
					continue;
				}
				encodeResourceToJsonStreamWriter(theResDef, next, theWriter, null, true, fixContainedResourceId(next.getId().getValue()));
			}
			for (IResource next : getContainedResources().getContainedResources()) {
				IdDt resourceId = getContainedResources().getResourceId(next);
				encodeResourceToJsonStreamWriter(theResDef, next, theWriter, null, true, fixContainedResourceId(resourceId.getValue()));
			}
			theWriter.writeEnd();
			break;
		}
		case PRIMITIVE_XHTML: {
			if (!getSuppressNarratives()) {
				XhtmlDt dt = (XhtmlDt) theValue;
				if (theChildName != null) {
					theWriter.write(theChildName, dt.getValueAsString());
				} else {
					theWriter.write(dt.getValueAsString());
				}
			} else {
				if (theChildName != null) {
					// do nothing
				} else {
					theWriter.writeNull();
				}
			}
			break;
		}
		case UNDECL_EXT:
		default:
			throw new IllegalStateException("Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, IElement theElement, JsonGenerator theEventWriter,
			List<? extends BaseRuntimeChildDefinition> theChildren, boolean theIsSubElementWithinResource) throws IOException {
		for (BaseRuntimeChildDefinition nextChild : theChildren) {
			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				INarrativeGenerator gen = myContext.getNarrativeGenerator();
				if (gen != null) {
					NarrativeDt narr = gen.generateNarrative(theResDef.getResourceProfile(), theResource);
					if (narr != null) {
						RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
						String childName = nextChild.getChildNameByDatatype(child.getDatatype());
						BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, narr, type, childName, theIsSubElementWithinResource);
						continue;
					}
				}
			}

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
					if (nextValue instanceof ContainedDt) {
						if (theIsSubElementWithinResource || getContainedResources().isEmpty()) {
							continue;
						}
					} else {
						continue;
					}
				}

				Class<? extends IElement> type = nextValue.getClass();
				String childName = nextChild.getChildNameByDatatype(type);
				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildElementDefinitionByDatatype(type);
				if (childDef == null) {
					super.throwExceptionForUnknownChildType(nextChild, type);
				}
				boolean primitive = childDef.getChildType() == ChildTypeEnum.PRIMITIVE_DATATYPE;
				
				if (childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES && theIsSubElementWithinResource) {
					continue;
				}
				
				if (nextChild instanceof RuntimeChildDeclaredExtensionDefinition) {
					// Don't encode extensions
//					RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition) nextChild;
//					if (extDef.isModifier()) {
//						addToHeldExtensions(valueIdx, modifierExtensions, extDef, nextValue);
//					} else {
//						addToHeldExtensions(valueIdx, extensions, extDef, nextValue);
//					}
				} else {

					if (currentChildName == null || !currentChildName.equals(childName)) {
						if (inArray) {
							theEventWriter.writeEnd();
						}
						if (nextChild.getMax() > 1 || nextChild.getMax() == Child.MAX_UNLIMITED) {
							theEventWriter.writeStartArray(childName);
							inArray = true;
							encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theIsSubElementWithinResource);
						} else if (nextChild instanceof RuntimeChildNarrativeDefinition && theIsSubElementWithinResource) {
							// suppress narratives from contained resources
						} else {
							encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, childName, theIsSubElementWithinResource);
						}
						currentChildName = childName;
					} else {
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theIsSubElementWithinResource);
					}

					if (nextValue instanceof ISupportsUndeclaredExtensions && primitive) {
						List<ExtensionDt> ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredExtensions();
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
							nextExt.write(theResDef, theResource, theEventWriter);
						}
						theEventWriter.writeEnd();
						theEventWriter.writeEnd();
					}

					if (!haveContent) {
						// theEventWriter.writeEnd();
						theEventWriter.writeNull();
					}
				}

				theEventWriter.writeEnd();
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, IElement theElement, JsonGenerator theEventWriter,
			BaseRuntimeElementCompositeDefinition<?> resDef, boolean theIsSubElementWithinResource) throws IOException, DataFormatException {
		extractAndWriteExtensionsAsDirectChild(theElement, theEventWriter, resDef, theResDef, theResource);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theElement, theEventWriter, resDef.getExtensions(), theIsSubElementWithinResource);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theElement, theEventWriter, resDef.getChildren(),theIsSubElementWithinResource);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, JsonGenerator theEventWriter, String theObjectNameOrNull,
			boolean theIsSubElementWithinResource) throws IOException {
		String resourceId = null;
		if (theIsSubElementWithinResource && StringUtils.isNotBlank(theResource.getId().getValue())) {
			resourceId = theResource.getId().getValue();
		}

		encodeResourceToJsonStreamWriter(theResDef, theResource, theEventWriter, theObjectNameOrNull, theIsSubElementWithinResource, resourceId);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IResource theResource, JsonGenerator theEventWriter, String theObjectNameOrNull, boolean theIsSubElementWithinResource,
			String theResourceId) throws IOException {
		if (!theIsSubElementWithinResource) {
			super.containResourcesForEncoding(theResource);
		}

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);

		if (theObjectNameOrNull == null) {
			theEventWriter.writeStartObject();
		} else {
			theEventWriter.writeStartObject(theObjectNameOrNull);
		}

		theEventWriter.write("resourceType", resDef.getName());
		if (theResourceId != null) {
			theEventWriter.write("id", theResourceId);
		}

		if (theResource instanceof Binary) {
			Binary bin = (Binary) theResource;
			theEventWriter.write("contentType", bin.getContentType());
			theEventWriter.write("content", bin.getContentAsBase64());
		} else {
			encodeCompositeElementToStreamWriter(theResDef, theResource, theResource, theEventWriter, resDef, theIsSubElementWithinResource);
		}
		theEventWriter.writeEnd();
	}

	@Override
	public void encodeResourceToWriter(IResource theResource, Writer theWriter) throws IOException {
		Validate.notNull(theResource, "Resource can not be null");

		JsonGenerator eventWriter = createJsonGenerator(theWriter);

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		encodeResourceToJsonStreamWriter(resDef, theResource, eventWriter, null, false);
		eventWriter.flush();
	}

	@Override
	public void encodeTagListToWriter(TagList theTagList, Writer theWriter) throws IOException {
		JsonGenerator eventWriter = createJsonGenerator(theWriter);

		eventWriter.writeStartObject();

		eventWriter.write("resourceType", TagList.ELEMENT_NAME);

		eventWriter.writeStartArray(TagList.ATTR_CATEGORY);
		for (Tag next : theTagList) {
			eventWriter.writeStartObject();

			if (isNotBlank(next.getTerm())) {
				eventWriter.write(Tag.ATTR_TERM, next.getTerm());
			}
			if (isNotBlank(next.getLabel())) {
				eventWriter.write(Tag.ATTR_LABEL, next.getLabel());
			}
			if (isNotBlank(next.getScheme())) {
				eventWriter.write(Tag.ATTR_SCHEME, next.getScheme());
			}

			eventWriter.writeEnd();
		}
		eventWriter.writeEnd();

		eventWriter.writeEnd();
		eventWriter.flush();
	}

	/**
	 * This is useful only for the two cases where extensions are encoded as direct children (e.g. not in some object called _name): resource extensions, and extension extensions
	 */
	private void extractAndWriteExtensionsAsDirectChild(IElement theElement, JsonGenerator theEventWriter, BaseRuntimeElementDefinition<?> theElementDef, RuntimeResourceDefinition theResDef,
			IResource theResource) throws IOException {
		List<HeldExtension> extensions = new ArrayList<HeldExtension>(0);
		List<HeldExtension> modifierExtensions = new ArrayList<HeldExtension>(0);

		// Undeclared extensions
		extractUndeclaredExtensions(theElement, extensions, modifierExtensions);

		// Declared extensions
		extractDeclaredExtensions(theElement, theElementDef, extensions, modifierExtensions);

		// Write the extensions
		writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions);
	}

	private void extractDeclaredExtensions(IElement theResource, BaseRuntimeElementDefinition<?> resDef, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions) {
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsNonModifier()) {
			for (IElement nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue == null || nextValue.isEmpty()) {
						continue;
					}
					extensions.add(new HeldExtension(nextDef, nextValue));
				}
			}
		}
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsModifier()) {
			for (IElement nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue == null || nextValue.isEmpty()) {
						continue;
					}
					modifierExtensions.add(new HeldExtension(nextDef, nextValue));
				}
			}
		}
	}

	private void extractUndeclaredExtensions(IElement theResource, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions) {
		if (theResource instanceof ISupportsUndeclaredExtensions) {
			List<ExtensionDt> ext = ((ISupportsUndeclaredExtensions) theResource).getUndeclaredExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				extensions.add(new HeldExtension(next));
			}

			ext = ((ISupportsUndeclaredExtensions) theResource).getUndeclaredModifierExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				modifierExtensions.add(new HeldExtension(next));
			}
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

	@Override
	public <T extends IResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		JsonReader reader;		
		JsonObject object;
		
		try {
			reader = Json.createReader(theReader);
			object = reader.readObject();
		} catch (JsonParsingException e) {
			if (e.getMessage().startsWith("Unexpected char 39")) {
				throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage() + " - This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
			}
			throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage(), e);
		}
		JsonValue resourceTypeObj = object.get("resourceType");
		assertObjectOfType(resourceTypeObj, JsonValue.ValueType.STRING, "resourceType");
		String resourceType = ((JsonString) resourceTypeObj).getString();
		if (!"Bundle".equals(resourceType)) {
			throw new DataFormatException("Trying to parse bundle but found resourceType other than 'Bundle'. Found: '" + resourceType + "'");
		}

		ParserState<Bundle> state = ParserState.getPreAtomInstance(myContext, theResourceType, true);
		state.enteringNewElement(null, "feed");

		parseBundleChildren(object, state);

		state.endingElement();

		Bundle retVal = state.getObject();

		return retVal;
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

	private void parseChildren(JsonObject theObject, ParserState<?> theState) {
		String elementId = null;
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("id".equals(nextName)) {
				elementId = theObject.getString(nextName);
				continue;
			} else if ("_id".equals(nextName)) {
				// _id is incorrect, but some early examples in the FHIR spec used it
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
			IElement object = (IElement) theState.getObject();
			if (object instanceof IIdentifiableElement) {
				((IIdentifiableElement) object).setElementSpecificId(elementId);
			} else if (object instanceof IResource) {
				((IResource) object).setId(new IdDt(elementId));
			}
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
			parseAlternates(theAlternateVal, theState);
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

	private void parseExtension(ParserState<?> theState, JsonArray array, boolean theIsModifier) {
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

	@Override
	public <T extends IResource> T parseResource(Class<T> theResourceType, String theMessageString) {
		return parseResource(theResourceType, new StringReader(theMessageString));
	}

	@Override
	public TagList parseTagList(Reader theReader) {
		JsonReader reader = Json.createReader(theReader);
		JsonObject object = reader.readObject();

		JsonValue resourceTypeObj = object.get("resourceType");
		assertObjectOfType(resourceTypeObj, JsonValue.ValueType.STRING, "resourceType");
		String resourceType = ((JsonString) resourceTypeObj).getString();

		ParserState<TagList> state = ParserState.getPreTagListInstance(myContext, true);
		state.enteringNewElement(null, resourceType);

		parseChildren(object, state);

		state.endingElement();

		return state.getObject();
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	private boolean writeAtomLink(JsonGenerator theEventWriter, String theRel, StringDt theLink, boolean theStarted) {
		boolean retVal = theStarted;
		if (isNotBlank(theLink.getValue())) {
			if (theStarted == false) {
				theEventWriter.writeStartArray("link");
				retVal = true;
			}

			theEventWriter.writeStartObject();
			theEventWriter.write("rel", theRel);
			theEventWriter.write("href", theLink.getValue());
			theEventWriter.writeEnd();
		}
		return retVal;
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

	private void writeExtensionsAsDirectChild(IResource theResource, JsonGenerator theEventWriter, RuntimeResourceDefinition resDef, List<HeldExtension> extensions,
			List<HeldExtension> modifierExtensions) throws IOException {
		if (extensions.isEmpty() == false) {
			theEventWriter.writeStartArray("extension");
			for (HeldExtension next : extensions) {
				next.write(resDef, theResource, theEventWriter);
			}
			theEventWriter.writeEnd();
		}
		if (modifierExtensions.isEmpty() == false) {
			theEventWriter.writeStartArray("modifierExtension");
			for (HeldExtension next : modifierExtensions) {
				next.write(resDef, theResource, theEventWriter);
			}
			theEventWriter.writeEnd();
		}
	}

	private void writeOptionalTagWithTextNode(JsonGenerator theEventWriter, String theElementName, IPrimitiveDatatype<?> theInstantDt) {
		String str = theInstantDt.getValueAsString();
		if (StringUtils.isNotBlank(str)) {
			theEventWriter.write(theElementName, theInstantDt.getValueAsString());
		}
	}

	private void writeTagWithTextNode(JsonGenerator theEventWriter, String theElementName, IPrimitiveDatatype<?> theIdDt) {
		if (theIdDt != null && !theIdDt.isEmpty()) {
			theEventWriter.write(theElementName, theIdDt.getValueAsString());
		} else {
			theEventWriter.writeNull(theElementName);
		}
	}

	private void writeTagWithTextNode(JsonGenerator theEventWriter, String theElementName, StringDt theStringDt) {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			theEventWriter.write(theElementName, theStringDt.getValue());
		}
		// else {
		// theEventWriter.writeNull(theElementName);
		// }
	}

	private class HeldExtension {

		private RuntimeChildDeclaredExtensionDefinition myDef;
		private ExtensionDt myUndeclaredExtension;
		private IElement myValue;

		public HeldExtension(ExtensionDt theUndeclaredExtension) {
			assert theUndeclaredExtension != null;
			myUndeclaredExtension = theUndeclaredExtension;
		}

		public HeldExtension(RuntimeChildDeclaredExtensionDefinition theDef, IElement theValue) {
			assert theDef != null;
			assert theValue != null;
			myDef = theDef;
			myValue = theValue;
		}

		public void write(RuntimeResourceDefinition theResDef, IResource theResource, JsonGenerator theEventWriter) throws IOException {
			if (myUndeclaredExtension != null) {
				writeUndeclaredExt(theResDef, theResource, theEventWriter, myUndeclaredExtension);
			} else {
				theEventWriter.writeStartObject();
				theEventWriter.write("url", myDef.getExtensionUrl());

				BaseRuntimeElementDefinition<?> def = myDef.getChildElementDefinitionByDatatype(myValue.getClass());
				if (def.getChildType() == ChildTypeEnum.RESOURCE_BLOCK) {
					extractAndWriteExtensionsAsDirectChild(myValue, theEventWriter, def, theResDef, theResource);
				} else {
					// encodeChildElementToStreamWriter(theResDef, theResource,
					// theEventWriter, myValue, def, "value" +
					// WordUtils.capitalize(def.getName()));
					String childName = myDef.getChildNameByDatatype(myValue.getClass());
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, myValue, def, childName,false);
				}

				// theEventWriter.name(myUndeclaredExtension.get);

				theEventWriter.writeEnd();
			}
		}

		private void writeUndeclaredExt(RuntimeResourceDefinition theResDef, IResource theResource, JsonGenerator theEventWriter, ExtensionDt ext) throws IOException {
			IElement value = ext.getValue();

			theEventWriter.writeStartObject();
			theEventWriter.write("url", ext.getUrl().getValue());

			boolean noValue = value == null || value.isEmpty();
			if (noValue && ext.getAllUndeclaredExtensions().isEmpty()) {
				ourLog.debug("Extension with URL[{}] has no value", ext.getUrl().getValue());
			} else if (noValue) {
				theEventWriter.writeStartArray("extension");
				for (ExtensionDt next : ext.getUndeclaredExtensions()) {
					writeUndeclaredExt(theResDef, theResource, theEventWriter, next);
				}
				theEventWriter.writeEnd();
			} else {
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				if (childName == null) {
					throw new ConfigurationException("Unable to encode extension, unregognized child element type: " + value.getClass().getCanonicalName());
				}
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
				encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, value, childDef, childName,true);
			}

			// theEventWriter.name(myUndeclaredExtension.get);

			theEventWriter.writeEnd();
		}

	}
}
