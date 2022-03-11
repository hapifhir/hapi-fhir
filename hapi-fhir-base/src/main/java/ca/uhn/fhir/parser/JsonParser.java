package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ISupportsUndeclaredExtensions;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.base.composite.BaseContainedDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.util.ElementUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBooleanDatatype;
import org.hl7.fhir.instance.model.api.IBaseDecimalDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseHasModifierExtensions;
import org.hl7.fhir.instance.model.api.IBaseIntegerDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.INarrative;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum.ID_DATATYPE;
import static ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum.PRIMITIVE_DATATYPE;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is the FHIR JSON parser/encoder. Users should not interact with this class directly, but should use
 * {@link FhirContext#newJsonParser()} to get an instance.
 */
public class JsonParser extends BaseParser implements IJsonLikeParser {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParser.HeldExtension.class);

	private boolean myPrettyPrint;

	/**
	 * Do not use this constructor, the recommended way to obtain a new instance of the JSON parser is to invoke
	 * {@link FhirContext#newJsonParser()}.
	 *
	 * @param theParserErrorHandler
	 */
	public JsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
	}

	private boolean addToHeldComments(int valueIdx, List<String> theCommentsToAdd, ArrayList<ArrayList<String>> theListToAddTo) {
		if (theCommentsToAdd.size() > 0) {
			theListToAddTo.ensureCapacity(valueIdx);
			while (theListToAddTo.size() <= valueIdx) {
				theListToAddTo.add(null);
			}
			if (theListToAddTo.get(valueIdx) == null) {
				theListToAddTo.set(valueIdx, new ArrayList<>());
			}
			theListToAddTo.get(valueIdx).addAll(theCommentsToAdd);
			return true;
		}
		return false;
	}

	private boolean addToHeldExtensions(int valueIdx, List<? extends IBaseExtension<?, ?>> ext, ArrayList<ArrayList<HeldExtension>> list, boolean theIsModifier, CompositeChildElement theChildElem,
                                        CompositeChildElement theParent, EncodeContext theEncodeContext, boolean theContainedResource, IBase theContainingElement) {
		boolean retVal = false;
		if (ext.size() > 0) {
			Boolean encodeExtension = null;
			for (IBaseExtension<?, ?> next : ext) {

				if (next.isEmpty()) {
					continue;
				}

				// Make sure we respect _summary and _elements
				if (encodeExtension == null) {
					encodeExtension = isEncodeExtension(theParent, theEncodeContext, theContainedResource, theContainingElement);
				}

				if (encodeExtension) {
					HeldExtension extension = new HeldExtension(next, theIsModifier, theChildElem, theParent);
					list.ensureCapacity(valueIdx);
					while (list.size() <= valueIdx) {
						list.add(null);
					}
					ArrayList<HeldExtension> extensionList = list.get(valueIdx);
					if (extensionList == null) {
						extensionList = new ArrayList<>();
						list.set(valueIdx, extensionList);
					}
					extensionList.add(extension);
					retVal = true;
				}
			}
		}
		return retVal;
	}

	private void addToHeldIds(int theValueIdx, ArrayList<String> theListToAddTo, String theId) {
		theListToAddTo.ensureCapacity(theValueIdx);
		while (theListToAddTo.size() <= theValueIdx) {
			theListToAddTo.add(null);
		}
		if (theListToAddTo.get(theValueIdx) == null) {
			theListToAddTo.set(theValueIdx, theId);
		}
	}

	// private void assertObjectOfType(JsonLikeValue theResourceTypeObj, Object theValueType, String thePosition) {
	// if (theResourceTypeObj == null) {
	// throw new DataFormatException(Msg.code(1836) + "Invalid JSON content detected, missing required element: '" + thePosition + "'");
	// }
	//
	// if (theResourceTypeObj.getValueType() != theValueType) {
	// throw new DataFormatException(Msg.code(1837) + "Invalid content of element " + thePosition + ", expected " + theValueType);
	// }
	// }

	private void beginArray(JsonLikeWriter theEventWriter, String arrayName) throws IOException {
		theEventWriter.beginArray(arrayName);
	}

	private void beginObject(JsonLikeWriter theEventWriter, String arrayName) throws IOException {
		theEventWriter.beginObject(arrayName);
	}

	private JsonLikeWriter createJsonWriter(Writer theWriter) throws IOException {
		JsonLikeStructure jsonStructure = new JacksonStructure();
		return jsonStructure.getJsonLikeWriter(theWriter);
	}

	public void doEncodeResourceToJsonLikeWriter(IBaseResource theResource, JsonLikeWriter theEventWriter, EncodeContext theEncodeContext) throws IOException {
		if (myPrettyPrint) {
			theEventWriter.setPrettyPrint(myPrettyPrint);
		}
		theEventWriter.init();

		RuntimeResourceDefinition resDef = getContext().getResourceDefinition(theResource);
		encodeResourceToJsonStreamWriter(resDef, theResource, theEventWriter, null, false, theEncodeContext);
		theEventWriter.flush();
	}

	@Override
	protected void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter, EncodeContext theEncodeContext) throws IOException {
		JsonLikeWriter eventWriter = createJsonWriter(theWriter);
		doEncodeResourceToJsonLikeWriter(theResource, eventWriter, theEncodeContext);
		eventWriter.close();
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) {
		JsonLikeStructure jsonStructure = new JacksonStructure();
		jsonStructure.load(theReader);

		T retVal = doParseResource(theResourceType, jsonStructure);

		return retVal;
	}

	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, JsonLikeStructure theJsonStructure) {
		JsonLikeObject object = theJsonStructure.getRootObject();

		JsonLikeValue resourceTypeObj = object.get("resourceType");
		if (resourceTypeObj == null || !resourceTypeObj.isString() || isBlank(resourceTypeObj.getAsString())) {
			throw new DataFormatException(Msg.code(1838) + "Invalid JSON content detected, missing required element: 'resourceType'");
		}

		String resourceType = resourceTypeObj.getAsString();

		ParserState<? extends IBaseResource> state = ParserState.getPreResourceInstance(this, theResourceType, getContext(), true, getErrorHandler());
		state.enteringNewElement(null, resourceType);

		parseChildren(object, state);

		state.endingElement();
		state.endingElement();

		@SuppressWarnings("unchecked")
		T retVal = (T) state.getObject();

		return retVal;
	}

	private void encodeChildElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, IBase theNextValue,
																 BaseRuntimeElementDefinition<?> theChildDef, String theChildName, boolean theContainedResource, CompositeChildElement theChildElem,
																 boolean theForceEmpty, EncodeContext theEncodeContext) throws IOException {

		switch (theChildDef.getChildType()) {
			case ID_DATATYPE: {
				IIdType value = (IIdType) theNextValue;
				String encodedValue = "id".equals(theChildName) ? value.getIdPart() : value.getValue();
				if (isBlank(encodedValue)) {
					break;
				}
				if (theChildName != null) {
					write(theEventWriter, theChildName, encodedValue);
				} else {
					theEventWriter.write(encodedValue);
				}
				break;
			}
			case PRIMITIVE_DATATYPE: {
				final IPrimitiveType<?> value = (IPrimitiveType<?>) theNextValue;
				final String valueStr = value.getValueAsString();
				if (isBlank(valueStr)) {
					if (theForceEmpty) {
						theEventWriter.writeNull();
					}
					break;
				}

				// check for the common case first - String value types
				Object valueObj = value.getValue();
				if (valueObj instanceof String) {
					if (theChildName != null) {
						theEventWriter.write(theChildName, valueStr);
					} else {
						theEventWriter.write(valueStr);
					}
					break;
				} else if (valueObj instanceof Long) {
					if (theChildName != null) {
						theEventWriter.write(theChildName, (long) valueObj);
					} else {
						theEventWriter.write((long) valueObj);
					}
					break;
				}

				if (value instanceof IBaseIntegerDatatype) {
					if (theChildName != null) {
						write(theEventWriter, theChildName, ((IBaseIntegerDatatype) value).getValue());
					} else {
						theEventWriter.write(((IBaseIntegerDatatype) value).getValue());
					}
				} else if (value instanceof IBaseDecimalDatatype) {
					BigDecimal decimalValue = ((IBaseDecimalDatatype) value).getValue();
					decimalValue = new BigDecimal(decimalValue.toString()) {
						private static final long serialVersionUID = 1L;

						@Override
						public String toString() {
							return value.getValueAsString();
						}
					};
					if (theChildName != null) {
						write(theEventWriter, theChildName, decimalValue);
					} else {
						theEventWriter.write(decimalValue);
					}
				} else if (value instanceof IBaseBooleanDatatype) {
					if (theChildName != null) {
						write(theEventWriter, theChildName, ((IBaseBooleanDatatype) value).getValue());
					} else {
						Boolean booleanValue = ((IBaseBooleanDatatype) value).getValue();
						if (booleanValue != null) {
							theEventWriter.write(booleanValue.booleanValue());
						}
					}
				} else {
					if (theChildName != null) {
						write(theEventWriter, theChildName, valueStr);
					} else {
						theEventWriter.write(valueStr);
					}
				}
				break;
			}
			case RESOURCE_BLOCK:
			case COMPOSITE_DATATYPE: {
				if (theChildName != null) {
					theEventWriter.beginObject(theChildName);
				} else {
					theEventWriter.beginObject();
				}
				encodeCompositeElementToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, theContainedResource, theChildElem, theEncodeContext);
				theEventWriter.endObject();
				break;
			}
			case CONTAINED_RESOURCE_LIST:
			case CONTAINED_RESOURCES: {
				List<IBaseResource> containedResources = getContainedResources().getContainedResources();
				if (containedResources.size() > 0) {
					beginArray(theEventWriter, theChildName);

					for (IBaseResource next : containedResources) {
						IIdType resourceId = getContainedResources().getResourceId(next);
						String value = resourceId.getValue();
						encodeResourceToJsonStreamWriter(theResDef, next, theEventWriter, null, true, fixContainedResourceId(value), theEncodeContext);
					}

					theEventWriter.endArray();
				}
				break;
			}
			case PRIMITIVE_XHTML_HL7ORG:
			case PRIMITIVE_XHTML: {
				if (!isSuppressNarratives()) {
					IPrimitiveType<?> dt = (IPrimitiveType<?>) theNextValue;
					if (theChildName != null) {
						write(theEventWriter, theChildName, dt.getValueAsString());
					} else {
						theEventWriter.write(dt.getValueAsString());
					}
				} else {
					if (theChildName != null) {
						// do nothing
					} else {
						theEventWriter.writeNull();
					}
				}
				break;
			}
			case RESOURCE:
				IBaseResource resource = (IBaseResource) theNextValue;
				RuntimeResourceDefinition def = getContext().getResourceDefinition(resource);

				theEncodeContext.pushPath(def.getName(), true);
				encodeResourceToJsonStreamWriter(def, resource, theEventWriter, theChildName, theContainedResource, theEncodeContext);
				theEncodeContext.popPath();

				break;
			case UNDECL_EXT:
			default:
				throw new IllegalStateException(Msg.code(1839) + "Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theElement, JsonLikeWriter theEventWriter,
																				 boolean theContainedResource, CompositeChildElement theParent, EncodeContext theEncodeContext) throws IOException {

		{
			String elementId = getCompositeElementId(theElement);
			if (isNotBlank(elementId)) {
				write(theEventWriter, "id", elementId);
			}
		}

		boolean haveWrittenExtensions = false;
		Iterable<CompositeChildElement> compositeChildElements = super.compositeChildIterator(theElement, theContainedResource, theParent, theEncodeContext);
		for (CompositeChildElement nextChildElem : compositeChildElements) {

			BaseRuntimeChildDefinition nextChild = nextChildElem.getDef();

			if (nextChildElem.getDef().getElementName().equals("extension") || nextChildElem.getDef().getElementName().equals("modifierExtension")
				|| nextChild instanceof RuntimeChildDeclaredExtensionDefinition) {
				if (!haveWrittenExtensions) {
					extractAndWriteExtensionsAsDirectChild(theElement, theEventWriter, getContext().getElementDefinition(theElement.getClass()), theResDef, theResource, nextChildElem, theParent, theEncodeContext, theContainedResource);
					haveWrittenExtensions = true;
				}
				continue;
			}

			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				INarrativeGenerator gen = getContext().getNarrativeGenerator();
				if (gen != null) {
					INarrative narr;
					if (theResource instanceof IResource) {
						narr = ((IResource) theResource).getText();
					} else if (theResource instanceof IDomainResource) {
						narr = ((IDomainResource) theResource).getText();
					} else {
						narr = null;
					}
					if (narr != null && narr.isEmpty()) {
						gen.populateResourceNarrative(getContext(), theResource);
						if (!narr.isEmpty()) {
							RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
							String childName = nextChild.getChildNameByDatatype(child.getDatatype());
							BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
							encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, narr, type, childName, theContainedResource, nextChildElem, false, theEncodeContext);
							continue;
						}
					}
				}
			} else if (nextChild instanceof RuntimeChildContainedResources) {
				String childName = nextChild.getValidChildNames().iterator().next();
				BaseRuntimeElementDefinition<?> child = nextChild.getChildByName(childName);
				encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, null, child, childName, theContainedResource, nextChildElem, false, theEncodeContext);
				continue;
			}

			List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
			values = preProcessValues(nextChild, theResource, values, nextChildElem, theEncodeContext);

			if (values == null || values.isEmpty()) {
				continue;
			}

			String currentChildName = null;
			boolean inArray = false;

			ArrayList<ArrayList<HeldExtension>> extensions = new ArrayList<>(0);
			ArrayList<ArrayList<HeldExtension>> modifierExtensions = new ArrayList<>(0);
			ArrayList<ArrayList<String>> comments = new ArrayList<>(0);
			ArrayList<String> ids = new ArrayList<>(0);

			int valueIdx = 0;
			for (IBase nextValue : values) {

				if (nextValue == null || nextValue.isEmpty()) {
					if (nextValue instanceof BaseContainedDt) {
						if (theContainedResource || getContainedResources().isEmpty()) {
							continue;
						}
					} else {
						continue;
					}
				}

				BaseParser.ChildNameAndDef childNameAndDef = super.getChildNameAndDef(nextChild, nextValue);
				if (childNameAndDef == null) {
					continue;
				}

				/*
				 * Often the two values below will be the same thing. There are cases though
				 * where they will not be. An example would be Observation.value, which is
				 * a choice type. If the value contains a Quantity, then:
				 * nextChildGenericName = "value"
				 * nextChildSpecificName = "valueQuantity"
				 */
				String nextChildSpecificName = childNameAndDef.getChildName();
				String nextChildGenericName = nextChild.getElementName();

				theEncodeContext.pushPath(nextChildGenericName, false);

				BaseRuntimeElementDefinition<?> childDef = childNameAndDef.getChildDef();
				boolean primitive = childDef.getChildType() == ChildTypeEnum.PRIMITIVE_DATATYPE;

				if ((childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES || childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) && theContainedResource) {
					continue;
				}

				boolean force = false;
				if (primitive) {
					if (nextValue instanceof ISupportsUndeclaredExtensions) {
						List<ExtensionDt> ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredExtensions();
						force |= addToHeldExtensions(valueIdx, ext, extensions, false, nextChildElem, theParent, theEncodeContext, theContainedResource, theElement);

						ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredModifierExtensions();
						force |= addToHeldExtensions(valueIdx, ext, modifierExtensions, true, nextChildElem, theParent, theEncodeContext, theContainedResource, theElement);
					} else {
						if (nextValue instanceof IBaseHasExtensions) {
							IBaseHasExtensions element = (IBaseHasExtensions) nextValue;
							List<? extends IBaseExtension<?, ?>> ext = element.getExtension();
							force |= addToHeldExtensions(valueIdx, ext, extensions, false, nextChildElem, theParent, theEncodeContext, theContainedResource, theElement);
						}
						if (nextValue instanceof IBaseHasModifierExtensions) {
							IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) nextValue;
							List<? extends IBaseExtension<?, ?>> ext = element.getModifierExtension();
							force |= addToHeldExtensions(valueIdx, ext, modifierExtensions, true, nextChildElem, theParent, theEncodeContext, theContainedResource, theElement);
						}
					}
					if (nextValue.hasFormatComment()) {
						force |= addToHeldComments(valueIdx, nextValue.getFormatCommentsPre(), comments);
						force |= addToHeldComments(valueIdx, nextValue.getFormatCommentsPost(), comments);
					}
					String elementId = getCompositeElementId(nextValue);
					if (isNotBlank(elementId)) {
						force = true;
						addToHeldIds(valueIdx, ids, elementId);
					}
				}

				if (currentChildName == null || !currentChildName.equals(nextChildSpecificName)) {
					if (inArray) {
						theEventWriter.endArray();
					}
					BaseRuntimeChildDefinition replacedParentDefinition = nextChild.getReplacedParentDefinition();
					if (isMultipleCardinality(nextChild.getMax()) || (replacedParentDefinition != null && isMultipleCardinality(replacedParentDefinition.getMax()))) {
						beginArray(theEventWriter, nextChildSpecificName);
						inArray = true;
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theContainedResource, nextChildElem, force, theEncodeContext);
					} else {
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, nextChildSpecificName, theContainedResource, nextChildElem, false, theEncodeContext);
					}
					currentChildName = nextChildSpecificName;
				} else {
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theContainedResource, nextChildElem, force, theEncodeContext);
				}

				valueIdx++;
				theEncodeContext.popPath();
			}

			if (inArray) {
				theEventWriter.endArray();
			}


			if (!extensions.isEmpty() || !modifierExtensions.isEmpty() || !comments.isEmpty()) {
				if (inArray) {
					// If this is a repeatable field, the extensions go in an array too
					beginArray(theEventWriter, '_' + currentChildName);
				} else {
					beginObject(theEventWriter, '_' + currentChildName);
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

					ArrayList<String> nextComments;
					if (comments.size() > i) {
						nextComments = comments.get(i);
					} else {
						nextComments = null;
					}
					if (nextComments != null && nextComments.isEmpty() == false) {
						haveContent = true;
					}

					String elementId = null;
					if (ids.size() > i) {
						elementId = ids.get(i);
						haveContent |= isNotBlank(elementId);
					}

					if (!haveContent) {
						theEventWriter.writeNull();
					} else {
						if (inArray) {
							theEventWriter.beginObject();
						}
						if (isNotBlank(elementId)) {
							write(theEventWriter, "id", elementId);
						}
						if (nextComments != null && !nextComments.isEmpty()) {
							beginArray(theEventWriter, "fhir_comments");
							for (String next : nextComments) {
								theEventWriter.write(next);
							}
							theEventWriter.endArray();
						}
						writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, heldExts, heldModExts, theEncodeContext, theContainedResource);
						if (inArray) {
							theEventWriter.endObject();
						}
					}
				}

				if (inArray) {
					theEventWriter.endArray();
				} else {
					theEventWriter.endObject();
				}
			}
		}
	}

	private boolean isMultipleCardinality(int maxCardinality) {
		return maxCardinality > 1 || maxCardinality == Child.MAX_UNLIMITED;
	}

	private void encodeCompositeElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theNextValue, JsonLikeWriter theEventWriter, boolean theContainedResource, CompositeChildElement theParent, EncodeContext theEncodeContext) throws IOException, DataFormatException {

		writeCommentsPreAndPost(theNextValue, theEventWriter);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, theContainedResource, theParent, theEncodeContext);
	}

	@Override
	public void encodeResourceToJsonLikeWriter(IBaseResource theResource, JsonLikeWriter theJsonLikeWriter) throws IOException, DataFormatException {
		Validate.notNull(theResource, "theResource can not be null");
		Validate.notNull(theJsonLikeWriter, "theJsonLikeWriter can not be null");

		if (theResource.getStructureFhirVersionEnum() != getContext().getVersion().getVersion()) {
			throw new IllegalArgumentException(Msg.code(1840) + "This parser is for FHIR version " + getContext().getVersion().getVersion() + " - Can not encode a structure for version " + theResource.getStructureFhirVersionEnum());
		}

		EncodeContext encodeContext = new EncodeContext();
		String resourceName = getContext().getResourceType(theResource);
		encodeContext.pushPath(resourceName, true);
		doEncodeResourceToJsonLikeWriter(theResource, theJsonLikeWriter, encodeContext);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, String theObjectNameOrNull,
																 boolean theContainedResource, EncodeContext theEncodeContext) throws IOException {
		IIdType resourceId = null;

		if (StringUtils.isNotBlank(theResource.getIdElement().getIdPart())) {
			resourceId = theResource.getIdElement();
			if (theResource.getIdElement().getValue().startsWith("urn:")) {
				resourceId = null;
			}
		}

		if (!theContainedResource) {
			if (!super.shouldEncodeResourceId(theResource, theEncodeContext)) {
				resourceId = null;
			} else if (theEncodeContext.getResourcePath().size() == 1 && getEncodeForceResourceId() != null) {
				resourceId = getEncodeForceResourceId();
			}
		}

		encodeResourceToJsonStreamWriter(theResDef, theResource, theEventWriter, theObjectNameOrNull, theContainedResource, resourceId, theEncodeContext);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, String theObjectNameOrNull,
																 boolean theContainedResource, IIdType theResourceId, EncodeContext theEncodeContext) throws IOException {

		if (!super.shouldEncodeResource(theResDef.getName())) {
			return;
		}

		if (!theContainedResource) {
			setContainedResources(getContext().newTerser().containResources(theResource));
		}

		RuntimeResourceDefinition resDef = getContext().getResourceDefinition(theResource);

		if (theObjectNameOrNull == null) {
			theEventWriter.beginObject();
		} else {
			beginObject(theEventWriter, theObjectNameOrNull);
		}

		write(theEventWriter, "resourceType", resDef.getName());
		if (theResourceId != null && theResourceId.hasIdPart()) {
			write(theEventWriter, "id", theResourceId.getIdPart());
			final List<HeldExtension> extensions = new ArrayList<>(0);
			final List<HeldExtension> modifierExtensions = new ArrayList<>(0);
			// Undeclared extensions
			extractUndeclaredExtensions(theResourceId, extensions, modifierExtensions, null, null, theEncodeContext, theContainedResource);
			boolean haveExtension = false;
			if (!extensions.isEmpty()) {
				haveExtension = true;
			}

			if (theResourceId.hasFormatComment() || haveExtension) {
				beginObject(theEventWriter, "_id");
				if (theResourceId.hasFormatComment()) {
					writeCommentsPreAndPost(theResourceId, theEventWriter);
				}
				if (haveExtension) {
					writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions, theEncodeContext, theContainedResource);
				}
				theEventWriter.endObject();
			}
		}

		if (theResource instanceof IResource) {
			IResource resource = (IResource) theResource;
			// Object securityLabelRawObj =

			List<BaseCodingDt> securityLabels = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.SECURITY_LABELS);
			List<? extends IIdType> profiles = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.PROFILES);
			profiles = super.getProfileTagsForEncoding(resource, profiles);

			TagList tags = getMetaTagsForEncoding(resource, theEncodeContext);
			InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			IdDt resourceId = resource.getId();
			String versionIdPart = resourceId.getVersionIdPart();
			if (isBlank(versionIdPart)) {
				versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
			}
			List<Map.Entry<ResourceMetadataKeyEnum<?>, Object>> extensionMetadataKeys = getExtensionMetadataKeys(resource);

			if (super.shouldEncodeResourceMeta(resource) && (ElementUtil.isEmpty(versionIdPart, updated, securityLabels, tags, profiles) == false) || !extensionMetadataKeys.isEmpty()) {
				beginObject(theEventWriter, "meta");

				if (shouldEncodePath(resource, "meta.versionId")) {
					writeOptionalTagWithTextNode(theEventWriter, "versionId", versionIdPart);
				}
				if (shouldEncodePath(resource, "meta.lastUpdated")) {
					writeOptionalTagWithTextNode(theEventWriter, "lastUpdated", updated);
				}

				if (profiles != null && profiles.isEmpty() == false) {
					beginArray(theEventWriter, "profile");
					for (IIdType profile : profiles) {
						if (profile != null && isNotBlank(profile.getValue())) {
							theEventWriter.write(profile.getValue());
						}
					}
					theEventWriter.endArray();
				}

				if (securityLabels.isEmpty() == false) {
					beginArray(theEventWriter, "security");
					for (BaseCodingDt securityLabel : securityLabels) {
						theEventWriter.beginObject();
						theEncodeContext.pushPath("security", false);
						encodeCompositeElementChildrenToStreamWriter(resDef, resource, securityLabel, theEventWriter, theContainedResource, null, theEncodeContext);
						theEncodeContext.popPath();
						theEventWriter.endObject();
					}
					theEventWriter.endArray();
				}

				if (tags != null && tags.isEmpty() == false) {
					beginArray(theEventWriter, "tag");
					for (Tag tag : tags) {
						if (tag.isEmpty()) {
							continue;
						}
						theEventWriter.beginObject();
						writeOptionalTagWithTextNode(theEventWriter, "system", tag.getScheme());
						writeOptionalTagWithTextNode(theEventWriter, "code", tag.getTerm());
						writeOptionalTagWithTextNode(theEventWriter, "display", tag.getLabel());
						theEventWriter.endObject();
					}
					theEventWriter.endArray();
				}

				addExtensionMetadata(theResDef, theResource, theContainedResource, extensionMetadataKeys, resDef, theEventWriter, theEncodeContext);

				theEventWriter.endObject(); // end meta
			}
		}

		encodeCompositeElementToStreamWriter(theResDef, theResource, theResource, theEventWriter, theContainedResource, new CompositeChildElement(resDef, theEncodeContext), theEncodeContext);

		theEventWriter.endObject();
	}


	private void addExtensionMetadata(RuntimeResourceDefinition theResDef, IBaseResource theResource,
												 boolean theContainedResource,
												 List<Map.Entry<ResourceMetadataKeyEnum<?>, Object>> extensionMetadataKeys,
												 RuntimeResourceDefinition resDef,
												 JsonLikeWriter theEventWriter, EncodeContext theEncodeContext) throws IOException {
		if (extensionMetadataKeys.isEmpty()) {
			return;
		}

		ExtensionDt metaResource = new ExtensionDt();
		for (Map.Entry<ResourceMetadataKeyEnum<?>, Object> entry : extensionMetadataKeys) {
			metaResource.addUndeclaredExtension((ExtensionDt) entry.getValue());
		}
		encodeCompositeElementToStreamWriter(theResDef, theResource, metaResource, theEventWriter, theContainedResource, new CompositeChildElement(resDef, theEncodeContext), theEncodeContext);
	}

	/**
	 * This is useful only for the two cases where extensions are encoded as direct children (e.g. not in some object
	 * called _name): resource extensions, and extension extensions
	 */
	private void extractAndWriteExtensionsAsDirectChild(IBase theElement, JsonLikeWriter theEventWriter, BaseRuntimeElementDefinition<?> theElementDef, RuntimeResourceDefinition theResDef,
																		 IBaseResource theResource, CompositeChildElement theChildElem, CompositeChildElement theParent, EncodeContext theEncodeContext, boolean theContainedResource) throws IOException {
		List<HeldExtension> extensions = new ArrayList<>(0);
		List<HeldExtension> modifierExtensions = new ArrayList<>(0);

		// Undeclared extensions
		extractUndeclaredExtensions(theElement, extensions, modifierExtensions, theChildElem, theParent, theEncodeContext, theContainedResource);

		// Declared extensions
		if (theElementDef != null) {
			extractDeclaredExtensions(theElement, theElementDef, extensions, modifierExtensions, theChildElem);
		}

		// Write the extensions
		writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions, theEncodeContext, theContainedResource);
	}

	private void extractDeclaredExtensions(IBase theResource, BaseRuntimeElementDefinition<?> resDef, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions,
														CompositeChildElement theChildElem) {
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsNonModifier()) {
			for (IBase nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue.isEmpty()) {
						continue;
					}
					extensions.add(new HeldExtension(nextDef, nextValue, theChildElem));
				}
			}
		}
		for (RuntimeChildDeclaredExtensionDefinition nextDef : resDef.getExtensionsModifier()) {
			for (IBase nextValue : nextDef.getAccessor().getValues(theResource)) {
				if (nextValue != null) {
					if (nextValue.isEmpty()) {
						continue;
					}
					modifierExtensions.add(new HeldExtension(nextDef, nextValue, theChildElem));
				}
			}
		}
	}

	private void extractUndeclaredExtensions(IBase theElement, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions, CompositeChildElement theChildElem,
														  CompositeChildElement theParent, EncodeContext theEncodeContext, boolean theContainedResource) {
		if (theElement instanceof ISupportsUndeclaredExtensions) {
			ISupportsUndeclaredExtensions element = (ISupportsUndeclaredExtensions) theElement;
			List<ExtensionDt> ext = element.getUndeclaredExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				extensions.add(new HeldExtension(next, false, theChildElem, theParent));
			}

			ext = element.getUndeclaredModifierExtensions();
			for (ExtensionDt next : ext) {
				if (next == null || next.isEmpty()) {
					continue;
				}
				modifierExtensions.add(new HeldExtension(next, true, theChildElem, theParent));
			}
		} else {
			if (theElement instanceof IBaseHasExtensions) {
				IBaseHasExtensions element = (IBaseHasExtensions) theElement;
				List<? extends IBaseExtension<?, ?>> ext = element.getExtension();
				Boolean encodeExtension = null;
				for (IBaseExtension<?, ?> next : ext) {
					if (next == null || (ElementUtil.isEmpty(next.getValue()) && next.getExtension().isEmpty())) {
						continue;
					}

					// Make sure we respect _elements and _summary
					if (encodeExtension == null) {
						encodeExtension = isEncodeExtension(theParent, theEncodeContext, theContainedResource, element);
					}
					if (encodeExtension) {
						HeldExtension extension = new HeldExtension(next, false, theChildElem, theParent);
						extensions.add(extension);
					}

				}
			}
			if (theElement instanceof IBaseHasModifierExtensions) {
				IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) theElement;
				List<? extends IBaseExtension<?, ?>> ext = element.getModifierExtension();
				for (IBaseExtension<?, ?> next : ext) {
					if (next == null || next.isEmpty()) {
						continue;
					}

					HeldExtension extension = new HeldExtension(next, true, theChildElem, theParent);
					modifierExtensions.add(extension);
				}
			}
		}
	}

	private boolean isEncodeExtension(CompositeChildElement theParent, EncodeContext theEncodeContext, boolean theContainedResource, IBase theElement) {
		BaseRuntimeElementDefinition<?> runtimeElementDefinition = getContext().getElementDefinition(theElement.getClass());
		boolean retVal = true;
		if (runtimeElementDefinition instanceof BaseRuntimeElementCompositeDefinition) {
			BaseRuntimeElementCompositeDefinition definition = (BaseRuntimeElementCompositeDefinition) runtimeElementDefinition;
			BaseRuntimeChildDefinition childDef = definition.getChildByName("extension");
			CompositeChildElement c = new CompositeChildElement(theParent, childDef, theEncodeContext);
			retVal = c.shouldBeEncoded(theContainedResource);
		}
		return retVal;
	}

	@Override
	public EncodingEnum getEncoding() {
		return EncodingEnum.JSON;
	}

	private JsonLikeArray grabJsonArray(JsonLikeObject theObject, String nextName, String thePosition) {
		JsonLikeValue object = theObject.get(nextName);
		if (object == null || object.isNull()) {
			return null;
		}
		if (!object.isArray()) {
			throw new DataFormatException(Msg.code(1841) + "Syntax error parsing JSON FHIR structure: Expected ARRAY at element '" + thePosition + "', found '" + object.getJsonType() + "'");
		}
		return object.getAsArray();
	}

	private void parseAlternates(JsonLikeValue theAlternateVal, ParserState<?> theState, String theElementName, String theAlternateName) {
		if (theAlternateVal == null || theAlternateVal.isNull()) {
			return;
		}

		if (theAlternateVal.isArray()) {
			JsonLikeArray array = theAlternateVal.getAsArray();
			if (array.size() > 1) {
				throw new DataFormatException(Msg.code(1842) + "Unexpected array of length " + array.size() + " (expected 0 or 1) for element: " + theElementName);
			}
			if (array.size() == 0) {
				return;
			}
			parseAlternates(array.get(0), theState, theElementName, theAlternateName);
			return;
		}

		JsonLikeValue alternateVal = theAlternateVal;
		if (alternateVal.isObject() == false) {
			getErrorHandler().incorrectJsonType(null, theAlternateName, ValueType.OBJECT, null, alternateVal.getJsonType(), null);
			return;
		}

		JsonLikeObject alternate = alternateVal.getAsObject();

		for (Iterator<String> keyIter = alternate.keyIterator(); keyIter.hasNext(); ) {
			String nextKey = keyIter.next();
			JsonLikeValue nextVal = alternate.get(nextKey);
			if ("extension".equals(nextKey)) {
				boolean isModifier = false;
				JsonLikeArray array = nextVal.getAsArray();
				parseExtension(theState, array, isModifier);
			} else if ("modifierExtension".equals(nextKey)) {
				boolean isModifier = true;
				JsonLikeArray array = nextVal.getAsArray();
				parseExtension(theState, array, isModifier);
			} else if ("id".equals(nextKey)) {
				if (nextVal.isString()) {
					theState.attributeValue("id", nextVal.getAsString());
				} else {
					getErrorHandler().incorrectJsonType(null, "id", ValueType.SCALAR, ScalarType.STRING, nextVal.getJsonType(), nextVal.getDataType());
				}
			} else if ("fhir_comments".equals(nextKey)) {
				parseFhirComments(nextVal, theState);
			}
		}
	}

	private void parseChildren(JsonLikeObject theObject, ParserState<?> theState) {
		int allUnderscoreNames = 0;
		int handledUnderscoreNames = 0;

		for (Iterator<String> keyIter = theObject.keyIterator(); keyIter.hasNext(); ) {
			String nextName = keyIter.next();
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("extension".equals(nextName)) {
				JsonLikeArray array = grabJsonArray(theObject, nextName, "extension");
				parseExtension(theState, array, false);
				continue;
			} else if ("modifierExtension".equals(nextName)) {
				JsonLikeArray array = grabJsonArray(theObject, nextName, "modifierExtension");
				parseExtension(theState, array, true);
				continue;
			} else if (nextName.equals("fhir_comments")) {
				parseFhirComments(theObject.get(nextName), theState);
				continue;
			} else if (nextName.charAt(0) == '_') {
				allUnderscoreNames++;
				continue;
			}

			JsonLikeValue nextVal = theObject.get(nextName);
			String alternateName = '_' + nextName;
			JsonLikeValue alternateVal = theObject.get(alternateName);
			if (alternateVal != null) {
				handledUnderscoreNames++;
			}

			parseChildren(theState, nextName, nextVal, alternateVal, alternateName, false);

		}

		// if (elementId != null) {
		// IBase object = (IBase) theState.getObject();
		// if (object instanceof IIdentifiableElement) {
		// ((IIdentifiableElement) object).setElementSpecificId(elementId);
		// } else if (object instanceof IBaseResource) {
		// ((IBaseResource) object).getIdElement().setValue(elementId);
		// }
		// }

		/*
		 * This happens if an element has an extension but no actual value. I.e.
		 * if a resource has a "_status" element but no corresponding "status"
		 * element. This could be used to handle a null value with an extension
		 * for example.
		 */
		if (allUnderscoreNames > handledUnderscoreNames) {
			for (Iterator<String> keyIter = theObject.keyIterator(); keyIter.hasNext(); ) {
				String alternateName = keyIter.next();
				if (alternateName.startsWith("_") && alternateName.length() > 1) {
					JsonLikeValue nextValue = theObject.get(alternateName);
					if (nextValue != null) {
						if (nextValue.isObject()) {
							String nextName = alternateName.substring(1);
							if (theObject.get(nextName) == null) {
								theState.enteringNewElement(null, nextName);
								parseAlternates(nextValue, theState, alternateName, alternateName);
								theState.endingElement();
							}
						} else {
							getErrorHandler().incorrectJsonType(null, alternateName, ValueType.OBJECT, null, nextValue.getJsonType(), null);
						}
					}
				}
			}
		}

	}

	private void parseChildren(ParserState<?> theState, String theName, JsonLikeValue theJsonVal, JsonLikeValue theAlternateVal, String theAlternateName, boolean theInArray) {
		if (theName.equals("id")) {
			if (!theJsonVal.isString()) {
				getErrorHandler().incorrectJsonType(null, "id", ValueType.SCALAR, ScalarType.STRING, theJsonVal.getJsonType(), theJsonVal.getDataType());
			}
		}

		if (theJsonVal.isArray()) {
			JsonLikeArray nextArray = theJsonVal.getAsArray();

			JsonLikeValue alternateVal = theAlternateVal;
			if (alternateVal != null && alternateVal.isArray() == false) {
				getErrorHandler().incorrectJsonType(null, theAlternateName, ValueType.ARRAY, null, alternateVal.getJsonType(), null);
				alternateVal = null;
			}

			JsonLikeArray nextAlternateArray = JsonLikeValue.asArray(alternateVal); // could be null
			for (int i = 0; i < nextArray.size(); i++) {
				JsonLikeValue nextObject = nextArray.get(i);
				JsonLikeValue nextAlternate = null;
				if (nextAlternateArray != null && nextAlternateArray.size() >= (i + 1)) {
					nextAlternate = nextAlternateArray.get(i);
				}
				parseChildren(theState, theName, nextObject, nextAlternate, theAlternateName, true);
			}
		} else if (theJsonVal.isObject()) {
			if (!theInArray && theState.elementIsRepeating(theName)) {
				getErrorHandler().incorrectJsonType(null, theName, ValueType.ARRAY, null, ValueType.OBJECT, null);
			}

			theState.enteringNewElement(null, theName);
			parseAlternates(theAlternateVal, theState, theAlternateName, theAlternateName);
			JsonLikeObject nextObject = theJsonVal.getAsObject();
			boolean preResource = false;
			if (theState.isPreResource()) {
				JsonLikeValue resType = nextObject.get("resourceType");
				if (resType == null || !resType.isString()) {
					throw new DataFormatException(Msg.code(1843) + "Missing required element 'resourceType' from JSON resource object, unable to parse");
				}
				theState.enteringNewElement(null, resType.getAsString());
				preResource = true;
			}
			parseChildren(nextObject, theState);
			if (preResource) {
				theState.endingElement();
			}
			theState.endingElement();
		} else if (theJsonVal.isNull()) {
			theState.enteringNewElement(null, theName);
			parseAlternates(theAlternateVal, theState, theAlternateName, theAlternateName);
			theState.endingElement();
		} else {
			// must be a SCALAR
			theState.enteringNewElement(null, theName);
			String asString = theJsonVal.getAsString();
			theState.attributeValue("value", asString);
			parseAlternates(theAlternateVal, theState, theAlternateName, theAlternateName);
			theState.endingElement();
		}
	}

	private void parseExtension(ParserState<?> theState, JsonLikeArray theValues, boolean theIsModifier) {
		int allUnderscoreNames = 0;
		int handledUnderscoreNames = 0;

		for (int i = 0; i < theValues.size(); i++) {
			JsonLikeObject nextExtObj = JsonLikeValue.asObject(theValues.get(i));
			JsonLikeValue jsonElement = nextExtObj.get("url");
			String url;
			if (null == jsonElement || !(jsonElement.isScalar())) {
				String parentElementName;
				if (theIsModifier) {
					parentElementName = "modifierExtension";
				} else {
					parentElementName = "extension";
				}
				getErrorHandler().missingRequiredElement(new ParseLocation().setParentElementName(parentElementName), "url");
				url = null;
			} else {
				url = getExtensionUrl(jsonElement.getAsString());
			}
			theState.enteringNewElementExtension(null, url, theIsModifier, getServerBaseUrl());
			for (Iterator<String> keyIter = nextExtObj.keyIterator(); keyIter.hasNext(); ) {
				String next = keyIter.next();
				if ("url".equals(next)) {
					continue;
				} else if ("extension".equals(next)) {
					JsonLikeArray jsonVal = JsonLikeValue.asArray(nextExtObj.get(next));
					parseExtension(theState, jsonVal, false);
				} else if ("modifierExtension".equals(next)) {
					JsonLikeArray jsonVal = JsonLikeValue.asArray(nextExtObj.get(next));
					parseExtension(theState, jsonVal, true);
				} else if (next.charAt(0) == '_') {
					allUnderscoreNames++;
					continue;
				} else {
					JsonLikeValue jsonVal = nextExtObj.get(next);
					String alternateName = '_' + next;
					JsonLikeValue alternateVal = nextExtObj.get(alternateName);
					if (alternateVal != null) {
						handledUnderscoreNames++;
					}
					parseChildren(theState, next, jsonVal, alternateVal, alternateName, false);
				}
			}

			/*
			 * This happens if an element has an extension but no actual value. I.e.
			 * if a resource has a "_status" element but no corresponding "status"
			 * element. This could be used to handle a null value with an extension
			 * for example.
			 */
			if (allUnderscoreNames > handledUnderscoreNames) {
				for (Iterator<String> keyIter = nextExtObj.keyIterator(); keyIter.hasNext(); ) {
					String alternateName = keyIter.next();
					if (alternateName.startsWith("_") && alternateName.length() > 1) {
						JsonLikeValue nextValue = nextExtObj.get(alternateName);
						if (nextValue != null) {
							if (nextValue.isObject()) {
								String nextName = alternateName.substring(1);
								if (nextExtObj.get(nextName) == null) {
									theState.enteringNewElement(null, nextName);
									parseAlternates(nextValue, theState, alternateName, alternateName);
									theState.endingElement();
								}
							} else {
								getErrorHandler().incorrectJsonType(null, alternateName, ValueType.OBJECT, null, nextValue.getJsonType(), null);
							}
						}
					}
				}
			}
			theState.endingElement();
		}
	}

	private void parseFhirComments(JsonLikeValue theObject, ParserState<?> theState) {
		if (theObject.isArray()) {
			JsonLikeArray comments = theObject.getAsArray();
			for (int i = 0; i < comments.size(); i++) {
				JsonLikeValue nextComment = comments.get(i);
				if (nextComment.isString()) {
					String commentText = nextComment.getAsString();
					if (commentText != null) {
						theState.commentPre(commentText);
					}
				}
			}
		}
	}

	@Override
	public <T extends IBaseResource> T parseResource(Class<T> theResourceType, JsonLikeStructure theJsonLikeStructure) throws DataFormatException {

		/*****************************************************
		 * ************************************************* *
		 * ** NOTE: this duplicates most of the code in ** *
		 * ** BaseParser.parseResource(Class<T>, Reader). ** *
		 * ** Unfortunately, there is no way to avoid ** *
		 * ** this without doing some refactoring of the ** *
		 * ** BaseParser class. ** *
		 * ************************************************* *
		 *****************************************************/

		/*
		 * We do this so that the context can verify that the structure is for
		 * the correct FHIR version
		 */
		if (theResourceType != null) {
			getContext().getResourceDefinition(theResourceType);
		}

		// Actually do the parse
		T retVal = doParseResource(theResourceType, theJsonLikeStructure);

		RuntimeResourceDefinition def = getContext().getResourceDefinition(retVal);
		if ("Bundle".equals(def.getName())) {

			BaseRuntimeChildDefinition entryChild = def.getChildByName("entry");
			BaseRuntimeElementCompositeDefinition<?> entryDef = (BaseRuntimeElementCompositeDefinition<?>) entryChild.getChildByName("entry");
			List<IBase> entries = entryChild.getAccessor().getValues(retVal);
			if (entries != null) {
				for (IBase nextEntry : entries) {

					/**
					 * If Bundle.entry.fullUrl is populated, set the resource ID to that
					 */
					// TODO: should emit a warning and maybe notify the error handler if the resource ID doesn't match the
					// fullUrl idPart
					BaseRuntimeChildDefinition fullUrlChild = entryDef.getChildByName("fullUrl");
					if (fullUrlChild == null) {
						continue; // TODO: remove this once the data model in tinder plugin catches up to 1.2
					}
					List<IBase> fullUrl = fullUrlChild.getAccessor().getValues(nextEntry);
					if (fullUrl != null && !fullUrl.isEmpty()) {
						IPrimitiveType<?> value = (IPrimitiveType<?>) fullUrl.get(0);
						if (value.isEmpty() == false) {
							List<IBase> entryResources = entryDef.getChildByName("resource").getAccessor().getValues(nextEntry);
							if (entryResources != null && entryResources.size() > 0) {
								IBaseResource res = (IBaseResource) entryResources.get(0);
								String versionId = res.getIdElement().getVersionIdPart();
								res.setId(value.getValueAsString());
								if (isNotBlank(versionId) && res.getIdElement().hasVersionIdPart() == false) {
									res.setId(res.getIdElement().withVersion(versionId));
								}
							}
						}
					}

				}
			}

		}

		return retVal;
	}

	@Override
	public IBaseResource parseResource(JsonLikeStructure theJsonLikeStructure) throws DataFormatException {
		return parseResource(null, theJsonLikeStructure);
	}

	@Override
	public IParser setPrettyPrint(boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	private void write(JsonLikeWriter theEventWriter, String theChildName, Boolean theValue) throws IOException {
		if (theValue != null) {
			theEventWriter.write(theChildName, theValue.booleanValue());
		}
	}

	// private void parseExtensionInDstu2Style(boolean theModifier, ParserState<?> theState, String
	// theParentExtensionUrl, String theExtensionUrl, JsonArray theValues) {
	// String extUrl = UrlUtil.constructAbsoluteUrl(theParentExtensionUrl, theExtensionUrl);
	// theState.enteringNewElementExtension(null, extUrl, theModifier);
	//
	// for (int extIdx = 0; extIdx < theValues.size(); extIdx++) {
	// JsonObject nextExt = theValues.getJsonObject(extIdx);
	// for (String nextKey : nextExt.keySet()) {
	// // if (nextKey.startsWith("value") && nextKey.length() > 5 &&
	// // myContext.getRuntimeChildUndeclaredExtensionDefinition().getChildByName(nextKey) != null) {
	// JsonElement jsonVal = nextExt.get(nextKey);
	// if (jsonVal.getValueType() == ValueType.ARRAY) {
	// /*
	// * Extension children which are arrays are sub-extensions. Any other value type should be treated as a value.
	// */
	// JsonArray arrayValue = (JsonArray) jsonVal;
	// parseExtensionInDstu2Style(theModifier, theState, extUrl, nextKey, arrayValue);
	// } else {
	// parseChildren(theState, nextKey, jsonVal, null, null);
	// }
	// }
	// }
	//
	// theState.endingElement();
	// }

	private void write(JsonLikeWriter theEventWriter, String theChildName, BigDecimal theDecimalValue) throws IOException {
		theEventWriter.write(theChildName, theDecimalValue);
	}

	private void write(JsonLikeWriter theEventWriter, String theChildName, Integer theValue) throws IOException {
		theEventWriter.write(theChildName, theValue);
	}

	private void writeCommentsPreAndPost(IBase theNextValue, JsonLikeWriter theEventWriter) throws IOException {
		if (theNextValue.hasFormatComment()) {
			beginArray(theEventWriter, "fhir_comments");
			List<String> pre = theNextValue.getFormatCommentsPre();
			if (pre.isEmpty() == false) {
				for (String next : pre) {
					theEventWriter.write(next);
				}
			}
			List<String> post = theNextValue.getFormatCommentsPost();
			if (post.isEmpty() == false) {
				for (String next : post) {
					theEventWriter.write(next);
				}
			}
			theEventWriter.endArray();
		}
	}

	private void writeExtensionsAsDirectChild(IBaseResource theResource, JsonLikeWriter theEventWriter, RuntimeResourceDefinition resDef, List<HeldExtension> extensions,
															List<HeldExtension> modifierExtensions, EncodeContext theEncodeContext, boolean theContainedResource) throws IOException {
		// Write Extensions
		if (extensions.isEmpty() == false) {
			theEncodeContext.pushPath("extension", false);
			beginArray(theEventWriter, "extension");
			for (HeldExtension next : extensions) {
				next.write(resDef, theResource, theEventWriter, theEncodeContext, theContainedResource);
			}
			theEventWriter.endArray();
			theEncodeContext.popPath();
		}

		// Write ModifierExtensions
		if (modifierExtensions.isEmpty() == false) {
			theEncodeContext.pushPath("modifierExtension", false);
			beginArray(theEventWriter, "modifierExtension");
			for (HeldExtension next : modifierExtensions) {
				next.write(resDef, theResource, theEventWriter, theEncodeContext, theContainedResource);
			}
			theEventWriter.endArray();
			theEncodeContext.popPath();
		}
	}

	private void writeOptionalTagWithTextNode(JsonLikeWriter theEventWriter, String theElementName, IPrimitiveDatatype<?> thePrimitive) throws IOException {
		if (thePrimitive == null) {
			return;
		}
		String str = thePrimitive.getValueAsString();
		writeOptionalTagWithTextNode(theEventWriter, theElementName, str);
	}

	private void writeOptionalTagWithTextNode(JsonLikeWriter theEventWriter, String theElementName, String theValue) throws IOException {
		if (StringUtils.isNotBlank(theValue)) {
			write(theEventWriter, theElementName, theValue);
		}
	}

	private class HeldExtension implements Comparable<HeldExtension> {

		private CompositeChildElement myChildElem;
		private RuntimeChildDeclaredExtensionDefinition myDef;
		private boolean myModifier;
		private IBaseExtension<?, ?> myUndeclaredExtension;
		private IBase myValue;
		private CompositeChildElement myParent;

		public HeldExtension(IBaseExtension<?, ?> theUndeclaredExtension, boolean theModifier, CompositeChildElement theChildElem, CompositeChildElement theParent) {
			assert theUndeclaredExtension != null;
			myUndeclaredExtension = theUndeclaredExtension;
			myModifier = theModifier;
			myChildElem = theChildElem;
			myParent = theParent;
		}

		public HeldExtension(RuntimeChildDeclaredExtensionDefinition theDef, IBase theValue, CompositeChildElement theChildElem) {
			assert theDef != null;
			assert theValue != null;
			myDef = theDef;
			myValue = theValue;
			myChildElem = theChildElem;
		}

		@Override
		public int compareTo(HeldExtension theArg0) {
			String url1 = myDef != null ? myDef.getExtensionUrl() : myUndeclaredExtension.getUrl();
			String url2 = theArg0.myDef != null ? theArg0.myDef.getExtensionUrl() : theArg0.myUndeclaredExtension.getUrl();
			url1 = defaultString(getExtensionUrl(url1));
			url2 = defaultString(getExtensionUrl(url2));
			return url1.compareTo(url2);
		}

		private void managePrimitiveExtension(final IBase theValue, final RuntimeResourceDefinition theResDef, final IBaseResource theResource, final JsonLikeWriter theEventWriter, final BaseRuntimeElementDefinition<?> def, final String childName, EncodeContext theEncodeContext, boolean theContainedResource) throws IOException {
			if (def.getChildType().equals(ID_DATATYPE) || def.getChildType().equals(PRIMITIVE_DATATYPE)) {
				final List<HeldExtension> extensions = new ArrayList<HeldExtension>(0);
				final List<HeldExtension> modifierExtensions = new ArrayList<HeldExtension>(0);
				// Undeclared extensions
				extractUndeclaredExtensions(theValue, extensions, modifierExtensions, myParent, null, theEncodeContext, theContainedResource);
				// Declared extensions
				if (def != null) {
					extractDeclaredExtensions(theValue, def, extensions, modifierExtensions, myParent);
				}
				boolean haveContent = false;
				if (!extensions.isEmpty() || !modifierExtensions.isEmpty()) {
					haveContent = true;
				}
				if (haveContent) {
					beginObject(theEventWriter, '_' + childName);
					writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions, theEncodeContext, theContainedResource);
					theEventWriter.endObject();
				}
			}
		}

		public void write(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, EncodeContext theEncodeContext, boolean theContainedResource) throws IOException {
			if (myUndeclaredExtension != null) {
				writeUndeclaredExtension(theResDef, theResource, theEventWriter, myUndeclaredExtension, theEncodeContext, theContainedResource);
			} else {
				theEventWriter.beginObject();

				writeCommentsPreAndPost(myValue, theEventWriter);

				JsonParser.write(theEventWriter, "url", getExtensionUrl(myDef.getExtensionUrl()));

				/*
				 * This makes sure that even if the extension contains a reference to a contained
				 * resource which has a HAPI-assigned ID we'll still encode that ID.
				 *
				 * See #327
				 */
				List<? extends IBase> preProcessedValue = preProcessValues(myDef, theResource, Collections.singletonList(myValue), myChildElem, theEncodeContext);

				// // Check for undeclared extensions on the declared extension
				// // (grrrrrr....)
				// if (myValue instanceof ISupportsUndeclaredExtensions) {
				// ISupportsUndeclaredExtensions value = (ISupportsUndeclaredExtensions)myValue;
				// List<ExtensionDt> exts = value.getUndeclaredExtensions();
				// if (exts.size() > 0) {
				// ArrayList<IBase> newValueList = new ArrayList<IBase>();
				// newValueList.addAll(preProcessedValue);
				// newValueList.addAll(exts);
				// preProcessedValue = newValueList;
				// }
				// }

				myValue = preProcessedValue.get(0);

				BaseRuntimeElementDefinition<?> def = myDef.getChildElementDefinitionByDatatype(myValue.getClass());
				if (def.getChildType() == ChildTypeEnum.RESOURCE_BLOCK) {
					extractAndWriteExtensionsAsDirectChild(myValue, theEventWriter, def, theResDef, theResource, myChildElem, null, theEncodeContext, theContainedResource);
				} else {
					String childName = myDef.getChildNameByDatatype(myValue.getClass());
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, myValue, def, childName, false, myParent, false, theEncodeContext);
					managePrimitiveExtension(myValue, theResDef, theResource, theEventWriter, def, childName, theEncodeContext, theContainedResource);
				}

				theEventWriter.endObject();
			}
		}

		private void writeUndeclaredExtension(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, IBaseExtension<?, ?> ext, EncodeContext theEncodeContext, boolean theContainedResource) throws IOException {
			IBase value = ext.getValue();
			final String extensionUrl = getExtensionUrl(ext.getUrl());

			theEventWriter.beginObject();

			writeCommentsPreAndPost(myUndeclaredExtension, theEventWriter);

			String elementId = getCompositeElementId(ext);
			if (isNotBlank(elementId)) {
				JsonParser.write(theEventWriter, "id", getCompositeElementId(ext));
			}

			if (isBlank(extensionUrl)) {
				ParseLocation loc = new ParseLocation(theEncodeContext.toString());
				getErrorHandler().missingRequiredElement(loc, "url");
			}

			JsonParser.write(theEventWriter, "url", extensionUrl);

			boolean noValue = value == null || value.isEmpty();
			if (noValue && ext.getExtension().isEmpty()) {

				ParseLocation loc = new ParseLocation(theEncodeContext.toString());
				getErrorHandler().missingRequiredElement(loc, "value");
				ourLog.debug("Extension with URL[{}] has no value", extensionUrl);

			} else {

				if (!noValue && !ext.getExtension().isEmpty()) {
					ParseLocation loc = new ParseLocation(theEncodeContext.toString());
					getErrorHandler().extensionContainsValueAndNestedExtensions(loc);
				}

				// Write child extensions
				if (!ext.getExtension().isEmpty()) {

					if (myModifier) {
						beginArray(theEventWriter, "modifierExtension");
					} else {
						beginArray(theEventWriter, "extension");
					}

					for (Object next : ext.getExtension()) {
						writeUndeclaredExtension(theResDef, theResource, theEventWriter, (IBaseExtension<?, ?>) next, theEncodeContext, theContainedResource);
					}
					theEventWriter.endArray();

				}

				// Write value
				if (!noValue) {
					theEncodeContext.pushPath("value", false);

					/*
					 * Pre-process value - This is called in case the value is a reference
					 * since we might modify the text
					 */
					value = preProcessValues(myDef, theResource, Collections.singletonList(value), myChildElem, theEncodeContext).get(0);

					RuntimeChildUndeclaredExtensionDefinition extDef = getContext().getRuntimeChildUndeclaredExtensionDefinition();
					String childName = extDef.getChildNameByDatatype(value.getClass());
					if (childName == null) {
						childName = "value" + WordUtils.capitalize(getContext().getElementDefinition(value.getClass()).getName());
					}
					BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
					if (childDef == null) {
						throw new ConfigurationException(Msg.code(1844) + "Unable to encode extension, unrecognized child element type: " + value.getClass().getCanonicalName());
					}
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, value, childDef, childName, false, myParent, false, theEncodeContext);
					managePrimitiveExtension(value, theResDef, theResource, theEventWriter, childDef, childName, theEncodeContext, theContainedResource);

					theEncodeContext.popPath();
				}
			}

			// theEventWriter.name(myUndeclaredExtension.get);

			theEventWriter.endObject();
		}
	}

	private static void write(JsonLikeWriter theWriter, String theName, String theValue) throws IOException {
		theWriter.write(theName, theValue);
	}
}
