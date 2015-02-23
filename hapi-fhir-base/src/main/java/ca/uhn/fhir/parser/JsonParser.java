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
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParsingException;

import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.primitive.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.IBase;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseDecimalDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseIntegerDatatype;

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
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IIdentifiableElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import ca.uhn.fhir.model.base.resource.BaseBinary;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.util.ElementUtil;
import ca.uhn.fhir.util.UrlUtil;

public class JsonParser extends BaseParser implements IParser {

	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN_DSTU1;
	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN_DSTU2;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParser.HeldExtension.class);

	static {
		HashSet<String> hashSetDstu1 = new HashSet<String>();
		hashSetDstu1.add("title");
		hashSetDstu1.add("id");
		hashSetDstu1.add("updated");
		hashSetDstu1.add("published");
		BUNDLE_TEXTNODE_CHILDREN_DSTU1 = Collections.unmodifiableSet(hashSetDstu1);

		HashSet<String> hashSetDstu2 = new HashSet<String>();
		hashSetDstu2.add("type");
		hashSetDstu2.add("base");
		hashSetDstu2.add("total");
		BUNDLE_TEXTNODE_CHILDREN_DSTU2 = Collections.unmodifiableSet(hashSetDstu2);
	}

	private FhirContext myContext;
	private boolean myPrettyPrint;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the JSON parser is to invoke {@link FhirContext#newJsonParser()}.
	 */
	public JsonParser(FhirContext theContext) {
		super(theContext);
		myContext = theContext;
	}

	private void addToHeldExtensions(int valueIdx, List<? extends IBaseExtension<?>> ext, ArrayList<ArrayList<HeldExtension>> list, boolean theIsModifier) {
		if (ext.size() > 0) {
			list.ensureCapacity(valueIdx);
			while (list.size() <= valueIdx) {
				list.add(null);
			}
			if (list.get(valueIdx) == null) {
				list.set(valueIdx, new ArrayList<JsonParser.HeldExtension>());
			}
			for (IBaseExtension<?> next : ext) {
				list.get(valueIdx).add(new HeldExtension(next, theIsModifier));
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
		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
			encodeBundleToWriterInDstu2Format(theBundle, eventWriter);
		} else {
			encodeBundleToWriterInDstu1Format(theBundle, eventWriter);
		}
		eventWriter.flush();
	}

	private void encodeBundleToWriterInDstu1Format(Bundle theBundle, JsonGenerator eventWriter) throws IOException {
		eventWriter.writeStartObject();

		eventWriter.write("resourceType", "Bundle");

		writeTagWithTextNode(eventWriter, "title", theBundle.getTitle());
		writeTagWithTextNode(eventWriter, "id", theBundle.getBundleId());
		writeOptionalTagWithTextNode(eventWriter, "updated", theBundle.getUpdated());
		writeOptionalTagWithTextNode(eventWriter, "published", theBundle.getPublished());

		boolean linkStarted = false;
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "self", theBundle.getLinkSelf(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "first", theBundle.getLinkFirst(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "previous", theBundle.getLinkPrevious(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "next", theBundle.getLinkNext(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "last", theBundle.getLinkLast(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(eventWriter, "fhir-base", theBundle.getLinkBase(), linkStarted);
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
			linkStarted = writeAtomLinkInDstu1Format(eventWriter, "self", nextEntry.getLinkSelf(), linkStarted);
			linkStarted = writeAtomLinkInDstu1Format(eventWriter, "alternate", nextEntry.getLinkAlternate(), linkStarted);
			linkStarted = writeAtomLinkInDstu1Format(eventWriter, "search", nextEntry.getLinkSearch(), linkStarted);
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

			if (nextEntry.getSummary().isEmpty() == false) {
				eventWriter.write("summary", nextEntry.getSummary().getValueAsString());
			}

			eventWriter.writeEnd(); // entry object
		}
		eventWriter.writeEnd(); // entry array

		eventWriter.writeEnd();
	}

	private void encodeBundleToWriterInDstu2Format(Bundle theBundle, JsonGenerator theEventWriter) throws IOException {
		theEventWriter.writeStartObject();

		theEventWriter.write("resourceType", "Bundle");

		writeOptionalTagWithTextNode(theEventWriter, "id", theBundle.getId().getIdPart());

		if (!ElementUtil.isEmpty(theBundle.getId().getVersionIdPart(), theBundle.getUpdated())) {
			theEventWriter.writeStartObject("meta");
			writeOptionalTagWithTextNode(theEventWriter, "versionId", theBundle.getId().getVersionIdPart());
			writeOptionalTagWithTextNode(theEventWriter, "lastUpdated", theBundle.getUpdated());
			theEventWriter.writeEnd();
		}

		writeOptionalTagWithTextNode(theEventWriter, "type", theBundle.getType());
		writeOptionalTagWithTextNode(theEventWriter, "base", theBundle.getLinkBase());
		writeOptionalTagWithNumberNode(theEventWriter, "total", theBundle.getTotalResults());

		boolean linkStarted = false;
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "next", theBundle.getLinkNext(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "self", theBundle.getLinkSelf(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "first", theBundle.getLinkFirst(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "previous", theBundle.getLinkPrevious(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "last", theBundle.getLinkLast(), linkStarted);
		if (linkStarted) {
			theEventWriter.writeEnd();
		}

		theEventWriter.writeStartArray("entry");
		for (BundleEntry nextEntry : theBundle.getEntries()) {
			theEventWriter.writeStartObject();

			writeOptionalTagWithTextNode(theEventWriter, "base", determineResourceBaseUrl(theBundle.getLinkBase().getValue(), nextEntry));

			boolean deleted = nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false;
			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(resource);
				encodeResourceToJsonStreamWriter(resDef, resource, theEventWriter, "resource", false);
			}

			if (nextEntry.getSearchMode().isEmpty() == false || nextEntry.getScore().isEmpty() == false) {
				theEventWriter.writeStartObject("search");
				writeOptionalTagWithTextNode(theEventWriter, "mode", nextEntry.getSearchMode().getValueAsString());
				writeOptionalTagWithDecimalNode(theEventWriter, "score", nextEntry.getScore());
				theEventWriter.writeEnd();
				// IResource nextResource = nextEntry.getResource();
			}

			if (nextEntry.getTransactionOperation().isEmpty() == false || nextEntry.getLinkSearch().isEmpty() == false) {
				theEventWriter.writeStartObject("transaction");
				writeOptionalTagWithTextNode(theEventWriter, "operation", nextEntry.getTransactionOperation().getValue());
				writeOptionalTagWithTextNode(theEventWriter, "url", nextEntry.getLinkSearch().getValue());
				theEventWriter.writeEnd();
			}

			if (deleted) {
				theEventWriter.writeStartObject("deleted");
				if (nextEntry.getResource() != null) {
					theEventWriter.write("type", myContext.getResourceDefinition(nextEntry.getResource()).getName());
					writeOptionalTagWithTextNode(theEventWriter, "resourceId", nextEntry.getResource().getId().getIdPart());
					writeOptionalTagWithTextNode(theEventWriter, "versionId", nextEntry.getResource().getId().getVersionIdPart());
				}
				writeTagWithTextNode(theEventWriter, "instant", nextEntry.getDeletedAt());
				theEventWriter.writeEnd();
			}

			// linkStarted = false;
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "self", nextEntry.getLinkSelf(), linkStarted);
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "alternate", nextEntry.getLinkAlternate(), linkStarted);
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "search", nextEntry.getLinkSearch(), linkStarted);
			// if (linkStarted) {
			// theEventWriter.writeEnd();
			// }
			//
			// writeOptionalTagWithTextNode(theEventWriter, "updated", nextEntry.getUpdated());
			// writeOptionalTagWithTextNode(theEventWriter, "published", nextEntry.getPublished());
			//
			// writeCategories(theEventWriter, nextEntry.getCategories());
			//
			// writeAuthor(nextEntry, theEventWriter);

			if (nextEntry.getSummary().isEmpty() == false) {
				theEventWriter.write("summary", nextEntry.getSummary().getValueAsString());
			}

			theEventWriter.writeEnd(); // entry object
		}
		theEventWriter.writeEnd(); // entry array

		theEventWriter.writeEnd();
	}

	private void encodeChildElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonGenerator theWriter, IBase theNextValue,
			BaseRuntimeElementDefinition<?> theChildDef, String theChildName, boolean theIsSubElementWithinResource) throws IOException {

		switch (theChildDef.getChildType()) {
		case PRIMITIVE_DATATYPE: {
			IPrimitiveType<?> value = (IPrimitiveType<?>) theNextValue;
			if (isBlank(value.getValueAsString())) {
				break;
			}

			if (value instanceof IBaseIntegerDatatype) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((IBaseIntegerDatatype) value).getValue());
				} else {
					theWriter.write(((IBaseIntegerDatatype) value).getValue());
				}
			} else if (value instanceof IBaseDecimalDatatype) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((IBaseDecimalDatatype) value).getValue());
				} else {
					theWriter.write(((IBaseDecimalDatatype) value).getValue());
				}
			} else if (value instanceof IBaseBooleanDatatype) {
				if (theChildName != null) {
					theWriter.write(theChildName, ((IBaseBooleanDatatype) value).getValue());
				} else {
					theWriter.write(((IBaseBooleanDatatype) value).getValue());
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
			if (theNextValue instanceof ExtensionDt) {
				theWriter.write("url", ((ExtensionDt) theNextValue).getUrlAsString());
			}
			encodeCompositeElementToStreamWriter(theResDef, theResource, theNextValue, theWriter, childCompositeDef, theIsSubElementWithinResource);
			theWriter.writeEnd();
			break;
		}
		case RESOURCE_REF: {
			BaseResourceReferenceDt referenceDt = (BaseResourceReferenceDt) theNextValue;
			if (theChildName != null) {
				theWriter.writeStartObject(theChildName);
			} else {
				theWriter.writeStartObject();
			}

			String reference = determineReferenceText(referenceDt);

			if (StringUtils.isNotBlank(reference)) {
				theWriter.write(XmlParser.RESREF_REFERENCE, reference);
			}
			if (referenceDt.getDisplayElement().isEmpty() == false) {
				theWriter.write(XmlParser.RESREF_DISPLAY, referenceDt.getDisplayElement().getValueAsString());
			}
			theWriter.writeEnd();
			break;
		}
		case CONTAINED_RESOURCES: {
			/*
			 * Disabled per #103 ContainedDt value = (ContainedDt) theNextValue; for (IResource next : value.getContainedResources()) { if (getContainedResources().getResourceId(next) != null) {
			 * continue; } encodeResourceToJsonStreamWriter(theResDef, next, theWriter, null, true, fixContainedResourceId(next.getId().getValue())); }
			 */
			List<IBaseResource> containedResources = getContainedResources().getContainedResources();
			if (containedResources.size() > 0) {
				theWriter.writeStartArray(theChildName);

				for (IBaseResource next : containedResources) {
					IdDt resourceId = getContainedResources().getResourceId(next);
					encodeResourceToJsonStreamWriter(theResDef, next, theWriter, null, true, fixContainedResourceId(resourceId.getValue()));
				}

				theWriter.writeEnd();
			}
			break;
		}
		case PRIMITIVE_XHTML: {
			if (!getSuppressNarratives()) {
				XhtmlDt dt = (XhtmlDt) theNextValue;
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
		case RESOURCE:
			IBaseResource resource = (IBaseResource) theNextValue;
			RuntimeResourceDefinition def = myContext.getResourceDefinition(resource);
			encodeResourceToJsonStreamWriter(def, resource, theWriter, theChildName, true);
			break;
		case UNDECL_EXT:
		default:
			throw new IllegalStateException("Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theNextValue, JsonGenerator theEventWriter,
			List<? extends BaseRuntimeChildDefinition> theChildren, boolean theIsSubElementWithinResource) throws IOException {
		for (BaseRuntimeChildDefinition nextChild : theChildren) {
			if (nextChild.getElementName().equals("extension") || nextChild.getElementName().equals("modifierExtension")) {
				continue;
			}
			
			if (nextChild instanceof RuntimeChildNarrativeDefinition) {

				INarrativeGenerator gen = myContext.getNarrativeGenerator();
				if (gen != null) {
					BaseNarrativeDt<?> narr = ((IResource) theResource).getText();
					gen.generateNarrative(theResDef.getResourceProfile(), theResource, narr);
					if (narr != null) {
						RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
						String childName = nextChild.getChildNameByDatatype(child.getDatatype());
						BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, narr, type, childName, theIsSubElementWithinResource);
						continue;
					}
				}
			}

			List<? extends IBase> values = nextChild.getAccessor().getValues(theNextValue);
			if (values == null || values.isEmpty()) {
				continue;
			}

			String currentChildName = null;
			boolean inArray = false;

			ArrayList<ArrayList<HeldExtension>> extensions = new ArrayList<ArrayList<HeldExtension>>(0);
			ArrayList<ArrayList<HeldExtension>> modifierExtensions = new ArrayList<ArrayList<HeldExtension>>(0);

			int valueIdx = 0;
			for (IBase nextValue : values) {
				if (nextValue == null || nextValue.isEmpty()) {
					if (nextValue instanceof BaseContainedDt) {
						if (theIsSubElementWithinResource || getContainedResources().isEmpty()) {
							continue;
						}
					} else {
						continue;
					}
				}

				Class<? extends IBase> type = nextValue.getClass();
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
					// RuntimeChildDeclaredExtensionDefinition extDef = (RuntimeChildDeclaredExtensionDefinition)
					// nextChild;
					// if (extDef.isModifier()) {
					// addToHeldExtensions(valueIdx, modifierExtensions, extDef, nextValue);
					// } else {
					// addToHeldExtensions(valueIdx, extensions, extDef, nextValue);
					// }
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

					if (primitive) {
						if (nextValue instanceof ISupportsUndeclaredExtensions) {
							List<ExtensionDt> ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredExtensions();
							addToHeldExtensions(valueIdx, ext, extensions, false);

							ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredModifierExtensions();
							addToHeldExtensions(valueIdx, ext, modifierExtensions, true);
						} else {
							if (nextValue instanceof IBaseHasExtensions) {
								IBaseHasExtensions element = (IBaseHasExtensions) nextValue;
								List<? extends IBaseExtension<?>> ext = element.getExtension();
								addToHeldExtensions(valueIdx, ext, extensions, false);
							}
							if (nextValue instanceof IBaseHasModifierExtensions) {
								IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) nextValue;
								List<? extends IBaseExtension<?>> ext = element.getModifierExtension();
								addToHeldExtensions(valueIdx, ext, extensions, true);
							}
						}
					}

				}

				valueIdx++;
			}

			if (inArray) {
				theEventWriter.writeEnd();
			}

			if (extensions.size() > 0 || modifierExtensions.size() > 0) {
				if (inArray) {
					// If this is a repeatable field, the extensions go in an array too
					theEventWriter.writeStartArray('_' + currentChildName);
				} else {
					theEventWriter.writeStartObject('_' + currentChildName);
				}

				for (int i = 0; i < valueIdx; i++) {
					boolean haveContent = false;

					List<HeldExtension> heldExts = Collections.emptyList();
					List<HeldExtension> heldModExts = Collections.emptyList();
					if (extensions.size() > i && extensions.get(i) != null && extensions.get(i).isEmpty() == false) {
						haveContent = true;
						heldExts = extensions.get(i);
					}

					if (modifierExtensions.size() > i && modifierExtensions.get(i) != null && modifierExtensions.get(i).isEmpty() == false) {
						haveContent = true;
						heldModExts = modifierExtensions.get(i);
					}

					if (!haveContent) {
						theEventWriter.writeNull();
					} else {
						if (inArray) {
							theEventWriter.writeStartObject();
						}
						writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, heldExts, heldModExts, null);
						if (inArray) {
							theEventWriter.writeEnd();
						}
					}
				}

				theEventWriter.writeEnd();
			}
		}
	}

	private void encodeCompositeElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theNextValue, JsonGenerator theEventWriter,
			BaseRuntimeElementCompositeDefinition<?> resDef, boolean theIsSubElementWithinResource) throws IOException, DataFormatException {
		extractAndWriteExtensionsAsDirectChild(theNextValue, theEventWriter, resDef, theResDef, theResource, null);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, resDef.getExtensions(), theIsSubElementWithinResource);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, resDef.getChildren(), theIsSubElementWithinResource);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonGenerator theEventWriter, String theObjectNameOrNull,
			boolean theIsSubElementWithinResource) throws IOException {
		String resourceId = null;
		if (theResource instanceof IResource) {
			IResource res = (IResource) theResource;
			if (StringUtils.isNotBlank(res.getId().getIdPart())) {
				if (theIsSubElementWithinResource) {
					resourceId = res.getId().getIdPart();
				} else if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
					resourceId = res.getId().getIdPart();
				}
			}
		} else if (theResource instanceof IAnyResource) {
			IAnyResource res = (IAnyResource) theResource;
			if (theIsSubElementWithinResource && StringUtils.isNotBlank(res.getId())) {
				resourceId = res.getId();
			}
		}

		encodeResourceToJsonStreamWriter(theResDef, theResource, theEventWriter, theObjectNameOrNull, theIsSubElementWithinResource, resourceId);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonGenerator theEventWriter, String theObjectNameOrNull,
			boolean theIsSubElementWithinResource, String theResourceId) throws IOException {
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

		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1) && theResource instanceof IResource) {
			IResource resource = (IResource) theResource;
			if (!ElementUtil.isEmpty(resource.getId().getVersionIdPart(), ResourceMetadataKeyEnum.UPDATED.get(resource))) {
				theEventWriter.writeStartObject("meta");
				writeOptionalTagWithTextNode(theEventWriter, "versionId", resource.getId().getVersionIdPart());
				writeOptionalTagWithTextNode(theEventWriter, "lastUpdated", ResourceMetadataKeyEnum.UPDATED.get(resource));

				Object securityLabelRawObj = resource.getResourceMetadata().get(ResourceMetadataKeyEnum.SECURITY_LABELS);
				if (securityLabelRawObj != null) {
					List<BaseCodingDt> securityLabels = (List<BaseCodingDt>) securityLabelRawObj;
					if (!securityLabels.isEmpty()) {
						theEventWriter.writeStartArray("security");

						for (BaseCodingDt securityLabel : securityLabels) {
							theEventWriter.writeStartObject();

							UriDt system = securityLabel.getSystemElement();
							if (system != null && !system.isEmpty())
								writeOptionalTagWithTextNode(theEventWriter, "system", system.getValueAsString());

							CodeDt code = securityLabel.getCodeElement();

							if (code != null && !code.isEmpty())
								writeOptionalTagWithTextNode(theEventWriter, "code", code.getValueAsString());

							StringDt display = securityLabel.getDisplayElement();
							if (display != null && !display.isEmpty())
								writeOptionalTagWithTextNode(theEventWriter, "display", display.getValueAsString());

							/*todo: handle version
							StringDt version = securityLabel.getVersion();
							if (version != null && ! version.isEmpty())
								writeOptionalTagWithTextNode(theEventWriter, "version", version.getValueAsString());
                            */
							theEventWriter.writeEnd(); //end the individual security label
						}
						theEventWriter.writeEnd(); //end security labels array
					}
				}

				theEventWriter.writeEnd(); //end meta
			}
		}

		if (theResource instanceof BaseBinary) {
			BaseBinary bin = (BaseBinary) theResource;
			theEventWriter.write("contentType", bin.getContentType());
			theEventWriter.write("content", bin.getContentAsBase64());
		} else {
			encodeCompositeElementToStreamWriter(theResDef, theResource, theResource, theEventWriter, resDef, theIsSubElementWithinResource);
		}

		theEventWriter.writeEnd();
	}

	@Override
	public void encodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException {
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
	private void extractAndWriteExtensionsAsDirectChild(IBase theElement, JsonGenerator theEventWriter, BaseRuntimeElementDefinition<?> theElementDef, RuntimeResourceDefinition theResDef,
			IBaseResource theResource, String theParentExtensionUrl) throws IOException {
		List<HeldExtension> extensions = new ArrayList<HeldExtension>(0);
		List<HeldExtension> modifierExtensions = new ArrayList<HeldExtension>(0);

		// Undeclared extensions
		extractUndeclaredExtensions(theElement, extensions, modifierExtensions);

		// Declared extensions
		if (theElementDef != null) {
			extractDeclaredExtensions(theElement, theElementDef, extensions, modifierExtensions);
		}

		// Write the extensions
		writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions, theParentExtensionUrl);
	}

	private void extractDeclaredExtensions(IBase theResource, BaseRuntimeElementDefinition<?> resDef, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions) {
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsNonModifier()) {
			for (IBase nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue == null || nextValue.isEmpty()) {
						continue;
					}
					extensions.add(new HeldExtension(nextDef, nextValue));
				}
			}
		}
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsModifier()) {
			for (IBase nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue == null || nextValue.isEmpty()) {
						continue;
					}
					modifierExtensions.add(new HeldExtension(nextDef, nextValue));
				}
			}
		}
	}

	private void extractUndeclaredExtensions(IBase theElement, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions element = (ISupportsUndeclaredExtensions) theElement;
			List<ExtensionDt> ext = element.getUndeclaredExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				extensions.add(new HeldExtension(next, false));
			}

			ext = element.getUndeclaredModifierExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				modifierExtensions.add(new HeldExtension(next, true));
			}
		} else {
			if (theElement instanceof IBaseHasExtensions) {
				IBaseHasExtensions element = (IBaseHasExtensions) theElement;
				List<? extends IBaseExtension<?>> ext = element.getExtension();
				for (IBaseExtension<?> next : ext) {
					if (next == null || next.isEmpty()) {
						continue;
					}
					extensions.add(new HeldExtension(next, false));
				}
			}
			if (theElement instanceof IBaseHasModifierExtensions) {
				IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) theElement;
				List<? extends IBaseExtension<?>> ext = element.getModifierExtension();
				for (IBaseExtension<?> next : ext) {
					if (next == null || next.isEmpty()) {
						continue;
					}
					modifierExtensions.add(new HeldExtension(next, true));
				}
			}
		}
	}

	private void parseAlternates(JsonValue theAlternateVal, ParserState<?> theState, String theElementName) {
		if (theAlternateVal == null || theAlternateVal.getValueType() == ValueType.NULL) {
			return;
		}

		if (theAlternateVal instanceof JsonArray) {
			JsonArray array = (JsonArray) theAlternateVal;
			if (array.size() > 1) {
				throw new DataFormatException("Unexpected array of length " + array.size() + " (expected 0 or 1) for element: " + theElementName);
			}
			if (array.size() == 0) {
				return;
			}
			parseAlternates(array.getJsonObject(0), theState, theElementName);
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
	public <T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		JsonReader reader;
		JsonObject object;

		try {
			reader = Json.createReader(theReader);
			object = reader.readObject();
		} catch (JsonParsingException e) {
			if (e.getMessage().startsWith("Unexpected char 39")) {
				throw new DataFormatException("Failed to parse JSON encoded FHIR content: " + e.getMessage()
						+ " - This may indicate that single quotes are being used as JSON escapes where double quotes are required", e);
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
		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
			state.enteringNewElement(null, "Bundle");
		} else {
			state.enteringNewElement(null, "feed");
		}

		parseBundleChildren(object, state);

		state.endingElement();

		Bundle retVal = state.getObject();

		return retVal;
	}

	private void parseBundleChildren(JsonObject theObject, ParserState<?> theState) {
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("entry".equals(nextName)) {
				JsonArray entries = theObject.getJsonArray(nextName);
				for (JsonValue jsonValue : entries) {
					theState.enteringNewElement(null, "entry");
					parseBundleChildren((JsonObject) jsonValue, theState);
					theState.endingElement();
				}
				continue;
			} else if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				if ("link".equals(nextName)) {
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
				} else if (BUNDLE_TEXTNODE_CHILDREN_DSTU1.contains(nextName)) {
					theState.enteringNewElement(null, nextName);
					theState.string(theObject.getString(nextName, null));
					theState.endingElement();
					continue;
				}
			} else {
				if ("link".equals(nextName)) {
					JsonArray entries = theObject.getJsonArray(nextName);
					for (JsonValue jsonValue : entries) {
						theState.enteringNewElement(null, "link");
						JsonObject linkObj = (JsonObject) jsonValue;
						String rel = linkObj.getString("relation", null);
						String href = linkObj.getString("url", null);
						theState.enteringNewElement(null, "relation");
						theState.attributeValue("value", rel);
						theState.endingElement();
						theState.enteringNewElement(null, "url");
						theState.attributeValue("value", href);
						theState.endingElement();
						theState.endingElement();
					}
					continue;
				} else if (BUNDLE_TEXTNODE_CHILDREN_DSTU2.contains(nextName)) {
					theState.enteringNewElement(null, nextName);
					// String obj = theObject.getString(nextName, null);

					JsonValue obj = theObject.get(nextName);
					if (obj == null) {
						theState.attributeValue("value", null);
					} else if (obj instanceof JsonString) {
						theState.attributeValue("value", theObject.getString(nextName, null));
					} else if (obj instanceof JsonNumber) {
						theState.attributeValue("value", obj.toString());
					} else {
						throw new DataFormatException("Unexpected JSON object for entry '" + nextName + "'");
					}

					theState.endingElement();
					continue;
				}
			}

			JsonValue nextVal = theObject.get(nextName);
			parseChildren(theState, nextName, nextVal, null, null);

		}
	}

	private void parseChildren(JsonObject theObject, ParserState<?> theState) {
		String elementId = null;
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("id".equals(nextName)) {
				elementId = theObject.getString(nextName);
				if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
					continue;
				}
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
			String alternateName = '_' + nextName;
			JsonValue alternateVal = theObject.get(alternateName);

			parseChildren(theState, nextName, nextVal, alternateVal, alternateName);

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

	private void parseChildren(ParserState<?> theState, String theName, JsonValue theJsonVal, JsonValue theAlternateVal, String theAlternateName) {
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
				parseChildren(theState, theName, nextObject, nextAlternate, theAlternateName);
			}
			break;
		}
		case OBJECT: {
			theState.enteringNewElement(null, theName);
			parseAlternates(theAlternateVal, theState, theAlternateName);
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
			parseAlternates(theAlternateVal, theState, theAlternateName);
			theState.endingElement();
			break;
		}
		case NUMBER:
		case FALSE:
		case TRUE:
			theState.enteringNewElement(null, theName);
			theState.attributeValue("value", theJsonVal.toString());
			parseAlternates(theAlternateVal, theState, theAlternateName);
			theState.endingElement();
			break;
		case NULL:
			break;
		}
	}

	private void parseExtension(ParserState<?> theState, JsonArray theValues, boolean theIsModifier) {
		for (int i = 0; i < theValues.size(); i++) {
			JsonObject nextExtObj = theValues.getJsonObject(i);
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
					parseChildren(theState, next, jsonVal, null, null);
				}
			}
			theState.endingElement();
		}
	}

	private void parseExtensionInDstu2Style(boolean theModifier, ParserState<?> theState, String theParentExtensionUrl, String theExtensionUrl, JsonArray theValues) {
		String extUrl = UrlUtil.constructAbsoluteUrl(theParentExtensionUrl, theExtensionUrl);
		theState.enteringNewElementExtension(null, extUrl, theModifier);

		for (int extIdx = 0; extIdx < theValues.size(); extIdx++) {
			JsonObject nextExt = theValues.getJsonObject(extIdx);
			for (String nextKey : nextExt.keySet()) {
				// if (nextKey.startsWith("value") && nextKey.length() > 5 &&
				// myContext.getRuntimeChildUndeclaredExtensionDefinition().getChildByName(nextKey) != null) {
				JsonValue jsonVal = nextExt.get(nextKey);
				if (jsonVal.getValueType() == ValueType.ARRAY) {
					/*
					 * Extension children which are arrays are sub-extensions. Any other value type should be treated as a value.
					 */
					JsonArray arrayValue = (JsonArray) jsonVal;
					parseExtensionInDstu2Style(theModifier, theState, extUrl, nextKey, arrayValue);
				} else {
					parseChildren(theState, nextKey, jsonVal, null, null);
				}
			}
		}

		theState.endingElement();
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) {
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

		ParserState<? extends IBaseResource> state = ParserState.getPreResourceInstance(def.getImplementingClass(), myContext, true);
		state.enteringNewElement(null, def.getName());

		parseChildren(object, state);

		state.endingElement();

		@SuppressWarnings("unchecked")
		T retVal = (T) state.getObject();

		return retVal;
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

	private boolean writeAtomLinkInDstu1Format(JsonGenerator theEventWriter, String theRel, StringDt theLink, boolean theStarted) {
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

	private boolean writeAtomLinkInDstu2Format(JsonGenerator theEventWriter, String theRel, StringDt theLink, boolean theStarted) {
		boolean retVal = theStarted;
		if (isNotBlank(theLink.getValue())) {
			if (theStarted == false) {
				theEventWriter.writeStartArray("link");
				retVal = true;
			}

			theEventWriter.writeStartObject();
			theEventWriter.write("relation", theRel);
			theEventWriter.write("url", theLink.getValue());
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

	private void writeExtensionsAsDirectChild(IBaseResource theResource, JsonGenerator theEventWriter, RuntimeResourceDefinition resDef, List<HeldExtension> extensions,
			List<HeldExtension> modifierExtensions, String theParentExtensionUrl) throws IOException {
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

	private void writeOptionalTagWithNumberNode(JsonGenerator theEventWriter, String theElementName, IntegerDt theValue) {
		if (theValue != null && theValue.isEmpty() == false) {
			theEventWriter.write(theElementName, theValue.getValue().intValue());
		}
	}

	private void writeOptionalTagWithDecimalNode(JsonGenerator theEventWriter, String theElementName, DecimalDt theValue) {
		if (theValue != null && theValue.isEmpty() == false) {
			theEventWriter.write(theElementName, theValue.getValue());
		}
	}

	private void writeOptionalTagWithTextNode(JsonGenerator theEventWriter, String theElementName, IPrimitiveDatatype<?> thePrimitive) {
		if (thePrimitive == null) {
			return;
		}
		String str = thePrimitive.getValueAsString();
		writeOptionalTagWithTextNode(theEventWriter, theElementName, str);
	}

	private void writeOptionalTagWithTextNode(JsonGenerator theEventWriter, String theElementName, String theValue) {
		if (StringUtils.isNotBlank(theValue)) {
			theEventWriter.write(theElementName, theValue);
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

	private class HeldExtension implements Comparable<HeldExtension> {

		private RuntimeChildDeclaredExtensionDefinition myDef;
		private IBaseExtension<?> myUndeclaredExtension;
		private IBase myValue;
		private boolean myModifier;

		public HeldExtension(IBaseExtension<?> theUndeclaredExtension, boolean theModifier) {
			assert theUndeclaredExtension != null;
			myUndeclaredExtension = theUndeclaredExtension;
			myModifier = theModifier;
		}

		public HeldExtension(RuntimeChildDeclaredExtensionDefinition theDef, IBase theValue) {
			assert theDef != null;
			assert theValue != null;
			myDef = theDef;
			myValue = theValue;
		}

		public void write(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonGenerator theEventWriter) throws IOException {
			if (myUndeclaredExtension != null) {
				writeUndeclaredExtInDstu1Format(theResDef, theResource, theEventWriter, myUndeclaredExtension);
			} else {
				theEventWriter.writeStartObject();
				theEventWriter.write("url", myDef.getExtensionUrl());

				BaseRuntimeElementDefinition<?> def = myDef.getChildElementDefinitionByDatatype(myValue.getClass());
				if (def.getChildType() == ChildTypeEnum.RESOURCE_BLOCK) {
					extractAndWriteExtensionsAsDirectChild(myValue, theEventWriter, def, theResDef, theResource, myDef.getExtensionUrl());
				} else {
					String childName = myDef.getChildNameByDatatype(myValue.getClass());
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, myValue, def, childName, false);
				}

				theEventWriter.writeEnd();
			}
		}

		private void writeUndeclaredExtInDstu1Format(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonGenerator theEventWriter, IBaseExtension<?> ext) throws IOException {
			IBaseDatatype value = ext.getValue();
			String extensionUrl = ext.getUrl();

			theEventWriter.writeStartObject();
			theEventWriter.write("url", extensionUrl);

			boolean noValue = value == null || value.isEmpty();
			if (noValue && ext.getExtension().isEmpty()) {
				ourLog.debug("Extension with URL[{}] has no value", extensionUrl);
			} else if (noValue) {

				if (myModifier) {
					theEventWriter.writeStartArray("modifierExtension");
				} else {
					theEventWriter.writeStartArray("extension");
				}

				for (Object next : ext.getExtension()) {
					writeUndeclaredExtInDstu1Format(theResDef, theResource, theEventWriter, (IBaseExtension<?>) next);
				}
				theEventWriter.writeEnd();
			} else {
				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				if (childName == null) {
					childName = "value" + myContext.getElementDefinition(value.getClass()).getName();
				}
				BaseRuntimeElementDefinition<?> childDef = myContext.getElementDefinition(value.getClass());
				if (childDef == null) {
					throw new ConfigurationException("Unable to encode extension, unregognized child element type: " + value.getClass().getCanonicalName());
				}
				encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, value, childDef, childName, true);
			}

			// theEventWriter.name(myUndeclaredExtension.get);

			theEventWriter.writeEnd();
		}

		@Override
		public int compareTo(HeldExtension theArg0) {
			String url1 = myDef != null ? myDef.getExtensionUrl() : myUndeclaredExtension.getUrl();
			String url2 = theArg0.myDef != null ? theArg0.myDef.getExtensionUrl() : theArg0.myUndeclaredExtension.getUrl();
			url1 = defaultString(url1);
			url2 = defaultString(url2);
			return url1.compareTo(url2);
		}

	}
}
