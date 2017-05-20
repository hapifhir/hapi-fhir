package ca.uhn.fhir.parser;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBinary;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition.ChildTypeEnum;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeChildContainedResources;
import ca.uhn.fhir.context.RuntimeChildDeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeChildNarrativeDefinition;
import ca.uhn.fhir.context.RuntimeChildUndeclaredExtensionDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.BaseBundle;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
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
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.json.GsonStructure;
import ca.uhn.fhir.parser.json.JsonLikeArray;
import ca.uhn.fhir.parser.json.JsonLikeObject;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.util.ElementUtil;

/**
 * This class is the FHIR JSON parser/encoder. Users should not interact with this class directly, but should use
 * {@link FhirContext#newJsonParser()} to get an instance.
 */
public class JsonParser extends BaseParser implements IJsonLikeParser {

	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN_DSTU1;
	private static final Set<String> BUNDLE_TEXTNODE_CHILDREN_DSTU2;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParser.HeldExtension.class);

	static {
		HashSet<String> hashSetDstu1 = new HashSet<String>();
		hashSetDstu1.add("title");
		hashSetDstu1.add("id");
		hashSetDstu1.add("updated");
		hashSetDstu1.add("published");
		hashSetDstu1.add("totalResults");
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
	 * Do not use this constructor, the recommended way to obtain a new instance of the JSON parser is to invoke
	 * {@link FhirContext#newJsonParser()}.
	 *
	 * @param theParserErrorHandler
	 */
	public JsonParser(FhirContext theContext, IParserErrorHandler theParserErrorHandler) {
		super(theContext, theParserErrorHandler);
		myContext = theContext;
	}

	private boolean addToHeldComments(int valueIdx, List<String> theCommentsToAdd, ArrayList<ArrayList<String>> theListToAddTo) {
		if (theCommentsToAdd.size() > 0) {
			theListToAddTo.ensureCapacity(valueIdx);
			while (theListToAddTo.size() <= valueIdx) {
				theListToAddTo.add(null);
			}
			if (theListToAddTo.get(valueIdx) == null) {
				theListToAddTo.set(valueIdx, new ArrayList<String>());
			}
			theListToAddTo.get(valueIdx).addAll(theCommentsToAdd);
			return true;
		}
		return false;
	}

	private boolean addToHeldExtensions(int valueIdx, List<? extends IBaseExtension<?, ?>> ext, ArrayList<ArrayList<HeldExtension>> list, boolean theIsModifier, CompositeChildElement theChildElem, CompositeChildElement theParent) {
		if (ext.size() > 0) {
			list.ensureCapacity(valueIdx);
			while (list.size() <= valueIdx) {
				list.add(null);
			}
			if (list.get(valueIdx) == null) {
				list.set(valueIdx, new ArrayList<JsonParser.HeldExtension>());
			}
			for (IBaseExtension<?, ?> next : ext) {
				list.get(valueIdx).add(new HeldExtension(next, theIsModifier, theChildElem, theParent));
			}
			return true;
		}
		return false;
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

//	private void assertObjectOfType(JsonLikeValue theResourceTypeObj, Object theValueType, String thePosition) {
//		if (theResourceTypeObj == null) {
//			throw new DataFormatException("Invalid JSON content detected, missing required element: '" + thePosition + "'");
//		}
//
//		if (theResourceTypeObj.getValueType() != theValueType) {
//			throw new DataFormatException("Invalid content of element " + thePosition + ", expected " + theValueType);
//		}
//	}

	private void beginArray(JsonLikeWriter theEventWriter, String arrayName) throws IOException {
		theEventWriter.beginArray(arrayName);
	}

	private void beginObject(JsonLikeWriter theEventWriter, String arrayName) throws IOException {
		theEventWriter.beginObject(arrayName);
	}

	private JsonLikeWriter createJsonWriter(Writer theWriter) {
		JsonLikeStructure jsonStructure = new GsonStructure();
		JsonLikeWriter retVal = jsonStructure.getJsonLikeWriter(theWriter);
		return retVal;
	}

	@Override
	public void doEncodeBundleToWriter(Bundle theBundle, Writer theWriter) throws IOException {
		JsonLikeWriter eventWriter = createJsonWriter(theWriter);
		doEncodeBundleToJsonLikeWriter(theBundle, eventWriter);
	}
	
	public void doEncodeBundleToJsonLikeWriter(Bundle theBundle, JsonLikeWriter theEventWriter) throws IOException { 
		if (myPrettyPrint) {
			theEventWriter.setPrettyPrint(myPrettyPrint);
		}
		theEventWriter.init();

		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
			encodeBundleToWriterInDstu2Format(theBundle, theEventWriter);
		} else {
			encodeBundleToWriterInDstu1Format(theBundle, theEventWriter);
		}
		theEventWriter.flush();
	}

	@Override
	protected void doEncodeResourceToWriter(IBaseResource theResource, Writer theWriter) throws IOException {
		JsonLikeWriter eventWriter = createJsonWriter(theWriter);
		doEncodeResourceToJsonLikeWriter(theResource, eventWriter);
	}
	
	public void doEncodeResourceToJsonLikeWriter(IBaseResource theResource, JsonLikeWriter theEventWriter) throws IOException { 
		if (myPrettyPrint) {
			theEventWriter.setPrettyPrint(myPrettyPrint);
		}
		theEventWriter.init();

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);
		encodeResourceToJsonStreamWriter(resDef, theResource, theEventWriter, null, false, false);
		theEventWriter.flush();
	}

	@Override
	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, Reader theReader) {
		JsonLikeStructure jsonStructure = new GsonStructure();
		jsonStructure.load(theReader);
		
		T retVal = doParseResource(theResourceType, jsonStructure);
		
		return retVal;
	}

	public <T extends IBaseResource> T doParseResource(Class<T> theResourceType, JsonLikeStructure theJsonStructure) {
			JsonLikeObject object = theJsonStructure.getRootObject();
		
			JsonLikeValue resourceTypeObj = object.get("resourceType");
			if (resourceTypeObj == null || !resourceTypeObj.isString() || isBlank(resourceTypeObj.getAsString())) {
				throw new DataFormatException("Invalid JSON content detected, missing required element: 'resourceType'");
			}
			
			String resourceType = resourceTypeObj.getAsString();

			ParserState<? extends IBaseResource> state = ParserState.getPreResourceInstance(this, theResourceType, myContext, true, getErrorHandler());
			state.enteringNewElement(null, resourceType);

			parseChildren(object, state);

			state.endingElement();
			state.endingElement();

			@SuppressWarnings("unchecked")
			T retVal = (T) state.getObject();

			return retVal;
	}

	@Override
	public void encodeBundleToJsonLikeWriter(Bundle theBundle, JsonLikeWriter theJsonLikeWriter) throws IOException, DataFormatException {
		Validate.notNull(theBundle, "theBundle must not be null");
		Validate.notNull(theJsonLikeWriter, "theJsonLikeWriter must not be null");

		doEncodeBundleToJsonLikeWriter(theBundle, theJsonLikeWriter);
	}

	private void encodeBundleToWriterInDstu1Format(Bundle theBundle, JsonLikeWriter theEventWriter) throws IOException {
		theEventWriter.beginObject();

		write(theEventWriter, "resourceType", "Bundle");

		writeTagWithTextNode(theEventWriter, "title", theBundle.getTitle());
		writeTagWithTextNode(theEventWriter, "id", theBundle.getBundleId());
		writeOptionalTagWithTextNode(theEventWriter, "updated", theBundle.getUpdated());

		boolean linkStarted = false;
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "self", theBundle.getLinkSelf(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "first", theBundle.getLinkFirst(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "previous", theBundle.getLinkPrevious(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "next", theBundle.getLinkNext(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "last", theBundle.getLinkLast(), linkStarted);
		linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "fhir-base", theBundle.getLinkBase(), linkStarted);
		if (linkStarted) {
			theEventWriter.endArray();
		}

		writeCategories(theEventWriter, theBundle.getCategories());

		writeOptionalTagWithTextNode(theEventWriter, "totalResults", theBundle.getTotalResults());

		writeAuthor(theBundle, theEventWriter);

		beginArray(theEventWriter, "entry");
		for (BundleEntry nextEntry : theBundle.getEntries()) {
			theEventWriter.beginObject();

			boolean deleted = nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false;
			if (deleted) {
				writeTagWithTextNode(theEventWriter, "deleted", nextEntry.getDeletedAt());
			}
			writeTagWithTextNode(theEventWriter, "title", nextEntry.getTitle());
			//TODO: Use of a deprecated method should be resolved.
			writeTagWithTextNode(theEventWriter, "id", nextEntry.getId());

			linkStarted = false;
			linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "self", nextEntry.getLinkSelf(), linkStarted);
			linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "alternate", nextEntry.getLinkAlternate(), linkStarted);
			linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "search", nextEntry.getLinkSearch(), linkStarted);
			if (linkStarted) {
				theEventWriter.endArray();
			}

			//TODO: Use of a deprecated method should be resolved.
			writeOptionalTagWithTextNode(theEventWriter, "updated", nextEntry.getUpdated());
			writeOptionalTagWithTextNode(theEventWriter, "published", nextEntry.getPublished());

			writeCategories(theEventWriter, nextEntry.getCategories());

			writeAuthor(nextEntry, theEventWriter);

			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(resource);
				encodeResourceToJsonStreamWriter(resDef, resource, theEventWriter, "content", false, true);
			}

			if (nextEntry.getSummary().isEmpty() == false) {
				write(theEventWriter, "summary", nextEntry.getSummary().getValueAsString());
			}

			theEventWriter.endObject(); // entry object
		}
		theEventWriter.endArray(); // entry array

		theEventWriter.endObject(); // resource object
	}

	private void encodeBundleToWriterInDstu2Format(Bundle theBundle, JsonLikeWriter theEventWriter) throws IOException {
		theEventWriter.beginObject();

		write(theEventWriter, "resourceType", "Bundle");

		writeOptionalTagWithTextNode(theEventWriter, "id", theBundle.getId().getIdPart());

		if (!ElementUtil.isEmpty(theBundle.getId().getVersionIdPart(), theBundle.getUpdated())) {
			beginObject(theEventWriter, "meta");
			writeOptionalTagWithTextNode(theEventWriter, "versionId", theBundle.getId().getVersionIdPart());
			writeOptionalTagWithTextNode(theEventWriter, "lastUpdated", theBundle.getUpdated());
			theEventWriter.endObject();
		}

		writeOptionalTagWithTextNode(theEventWriter, "type", theBundle.getType());

		writeOptionalTagWithNumberNode(theEventWriter, "total", theBundle.getTotalResults());

		boolean linkStarted = false;
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "next", theBundle.getLinkNext(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "self", theBundle.getLinkSelf(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "first", theBundle.getLinkFirst(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "previous", theBundle.getLinkPrevious(), linkStarted);
		linkStarted = writeAtomLinkInDstu2Format(theEventWriter, "last", theBundle.getLinkLast(), linkStarted);
		if (linkStarted) {
			theEventWriter.endArray();
		}

		beginArray(theEventWriter, "entry");
		for (BundleEntry nextEntry : theBundle.getEntries()) {
			theEventWriter.beginObject();

			if (nextEntry.getResource() != null && isNotBlank(nextEntry.getResource().getIdElement().getValue()) && (nextEntry.getResource().getId().getBaseUrl() != null || nextEntry.getResource().getId().getValueAsString().startsWith("urn:"))) {
				writeOptionalTagWithTextNode(theEventWriter, "fullUrl", nextEntry.getResource().getId().getValue());
			}

			boolean deleted = nextEntry.getDeletedAt() != null && nextEntry.getDeletedAt().isEmpty() == false;
			IResource resource = nextEntry.getResource();
			if (resource != null && !resource.isEmpty() && !deleted) {
				RuntimeResourceDefinition resDef = myContext.getResourceDefinition(resource);
				encodeResourceToJsonStreamWriter(resDef, resource, theEventWriter, "resource", false, true);
			}

			if (nextEntry.getSearchMode().isEmpty() == false || nextEntry.getScore().isEmpty() == false) {
				beginObject(theEventWriter, "search");
				writeOptionalTagWithTextNode(theEventWriter, "mode", nextEntry.getSearchMode().getValueAsString());
				writeOptionalTagWithDecimalNode(theEventWriter, "score", nextEntry.getScore());
				theEventWriter.endObject();
				// IResource nextResource = nextEntry.getResource();
			}

			if (nextEntry.getTransactionMethod().isEmpty() == false || nextEntry.getLinkSearch().isEmpty() == false) {
				beginObject(theEventWriter, "request");
				writeOptionalTagWithTextNode(theEventWriter, "method", nextEntry.getTransactionMethod().getValue());
				writeOptionalTagWithTextNode(theEventWriter, "url", nextEntry.getLinkSearch().getValue());
				theEventWriter.endObject();
			}

			if (deleted) {
				beginObject(theEventWriter, "deleted");
				if (nextEntry.getResource() != null) {
					write(theEventWriter, "type", myContext.getResourceDefinition(nextEntry.getResource()).getName());
					writeOptionalTagWithTextNode(theEventWriter, "resourceId", nextEntry.getResource().getId().getIdPart());
					writeOptionalTagWithTextNode(theEventWriter, "versionId", nextEntry.getResource().getId().getVersionIdPart());
				}
				writeTagWithTextNode(theEventWriter, "instant", nextEntry.getDeletedAt());
				theEventWriter.endObject();
			}

			// linkStarted = false;
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "self", nextEntry.getLinkSelf(), linkStarted);
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "alternate", nextEntry.getLinkAlternate(),
			// linkStarted);
			// linkStarted = writeAtomLinkInDstu1Format(theEventWriter, "search", nextEntry.getLinkSearch(),
			// linkStarted);
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
				write(theEventWriter, "summary", nextEntry.getSummary().getValueAsString());
			}

			theEventWriter.endObject(); // entry object
		}
		theEventWriter.endArray(); // entry array

		theEventWriter.endObject(); // resource object
	}

	private void encodeChildElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, IBase theNextValue, BaseRuntimeElementDefinition<?> theChildDef, String theChildName, boolean theContainedResource, CompositeChildElement theChildElem,
			boolean theForceEmpty) throws IOException {

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
			if (isBlank(value.getValueAsString())) {
				if (theForceEmpty) {
					theEventWriter.writeNull();
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
				String valueStr = value.getValueAsString();
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
			encodeCompositeElementToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, theContainedResource, theChildElem);
			theEventWriter.endObject();
			break;
		}
		case CONTAINED_RESOURCE_LIST:
		case CONTAINED_RESOURCES: {
			/*
			 * Disabled per #103 ContainedDt value = (ContainedDt) theNextValue; for (IResource next :
			 * value.getContainedResources()) { if (getContainedResources().getResourceId(next) != null) { continue; }
			 * encodeResourceToJsonStreamWriter(theResDef, next, theWriter, null, true,
			 * fixContainedResourceId(next.getId().getValue())); }
			 */
			List<IBaseResource> containedResources = getContainedResources().getContainedResources();
			if (containedResources.size() > 0) {
				beginArray(theEventWriter, theChildName);

				for (IBaseResource next : containedResources) {
					IIdType resourceId = getContainedResources().getResourceId(next);
					encodeResourceToJsonStreamWriter(theResDef, next, theEventWriter, null, true, fixContainedResourceId(resourceId.getValue()));
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
			RuntimeResourceDefinition def = myContext.getResourceDefinition(resource);
			encodeResourceToJsonStreamWriter(def, resource, theEventWriter, theChildName, false, true);
			break;
		case UNDECL_EXT:
		default:
			throw new IllegalStateException("Should not have this state here: " + theChildDef.getChildType().name());
		}

	}

	private void encodeCompositeElementChildrenToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theElement, JsonLikeWriter theEventWriter, boolean theContainedResource, CompositeChildElement theParent) throws IOException {

		{
			String elementId = getCompositeElementId(theElement);
			if (isNotBlank(elementId)) {
				write(theEventWriter, "id", elementId);
			}
		}

		boolean haveWrittenExtensions = false;
		for (CompositeChildElement nextChildElem : super.compositeChildIterator(theElement, theContainedResource, theParent)) {

			BaseRuntimeChildDefinition nextChild = nextChildElem.getDef();

			if (nextChildElem.getDef().getElementName().equals("extension") || nextChildElem.getDef().getElementName().equals("modifierExtension") || nextChild instanceof RuntimeChildDeclaredExtensionDefinition) {
				if (!haveWrittenExtensions) {
					extractAndWriteExtensionsAsDirectChild(theElement, theEventWriter, myContext.getElementDefinition(theElement.getClass()), theResDef, theResource, nextChildElem, theParent);
					haveWrittenExtensions = true;
				}
				continue;
			}

			if (nextChild instanceof RuntimeChildNarrativeDefinition) {
				INarrativeGenerator gen = myContext.getNarrativeGenerator();
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
						gen.generateNarrative(myContext, theResource, narr);
						if (!narr.isEmpty()) {
							RuntimeChildNarrativeDefinition child = (RuntimeChildNarrativeDefinition) nextChild;
							String childName = nextChild.getChildNameByDatatype(child.getDatatype());
							BaseRuntimeElementDefinition<?> type = child.getChildByName(childName);
							encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, narr, type, childName, theContainedResource, nextChildElem, false);
							continue;
						}
					}
				}
			} else if (nextChild instanceof RuntimeChildContainedResources) {
				String childName = nextChild.getValidChildNames().iterator().next();
				BaseRuntimeElementDefinition<?> child = nextChild.getChildByName(childName);
				encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, null, child, childName, theContainedResource, nextChildElem, false);
				continue;
			}

			List<? extends IBase> values = nextChild.getAccessor().getValues(theElement);
			values = super.preProcessValues(nextChild, theResource, values, nextChildElem);

			if (values == null || values.isEmpty()) {
				continue;
			}

			String currentChildName = null;
			boolean inArray = false;

			ArrayList<ArrayList<HeldExtension>> extensions = new ArrayList<ArrayList<HeldExtension>>(0);
			ArrayList<ArrayList<HeldExtension>> modifierExtensions = new ArrayList<ArrayList<HeldExtension>>(0);
			ArrayList<ArrayList<String>> comments = new ArrayList<ArrayList<String>>(0);
			ArrayList<String> ids = new ArrayList<String>(0);

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
				
				String childName = childNameAndDef.getChildName();
				BaseRuntimeElementDefinition<?> childDef = childNameAndDef.getChildDef();
				boolean primitive = childDef.getChildType() == ChildTypeEnum.PRIMITIVE_DATATYPE;

				if ((childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCES || childDef.getChildType() == ChildTypeEnum.CONTAINED_RESOURCE_LIST) && theContainedResource) {
					continue;
				}

				boolean force = false;
				if (primitive) {
					if (nextValue instanceof ISupportsUndeclaredExtensions) {
						List<ExtensionDt> ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredExtensions();
						force |= addToHeldExtensions(valueIdx, ext, extensions, false, nextChildElem, theParent);

						ext = ((ISupportsUndeclaredExtensions) nextValue).getUndeclaredModifierExtensions();
						force |= addToHeldExtensions(valueIdx, ext, modifierExtensions, true, nextChildElem, theParent);
					} else {
						if (nextValue instanceof IBaseHasExtensions) {
							IBaseHasExtensions element = (IBaseHasExtensions) nextValue;
							List<? extends IBaseExtension<?, ?>> ext = element.getExtension();
							force |= addToHeldExtensions(valueIdx, ext, extensions, false, nextChildElem, theParent);
						}
						if (nextValue instanceof IBaseHasModifierExtensions) {
							IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) nextValue;
							List<? extends IBaseExtension<?, ?>> ext = element.getModifierExtension();
							force |= addToHeldExtensions(valueIdx, ext, extensions, true, nextChildElem, theParent);
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

				if (currentChildName == null || !currentChildName.equals(childName)) {
					if (inArray) {
						theEventWriter.endArray();
					}
					if (nextChild.getMax() > 1 || nextChild.getMax() == Child.MAX_UNLIMITED) {
						beginArray(theEventWriter, childName);
						inArray = true;
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theContainedResource, nextChildElem, force);
					} else if (nextChild instanceof RuntimeChildNarrativeDefinition && theContainedResource) {
						// suppress narratives from contained resources
					} else {
						encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, childName, theContainedResource, nextChildElem, false);
					}
					currentChildName = childName;
				} else {
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, nextValue, childDef, null, theContainedResource, nextChildElem, force);
				}

				valueIdx++;
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
						writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, heldExts, heldModExts);
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

	private void encodeCompositeElementToStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, IBase theNextValue, JsonLikeWriter theEventWriter, boolean theContainedResource, CompositeChildElement theParent) throws IOException, DataFormatException {

		writeCommentsPreAndPost(theNextValue, theEventWriter);
		encodeCompositeElementChildrenToStreamWriter(theResDef, theResource, theNextValue, theEventWriter, theContainedResource, theParent);
	}

	@Override
	public void encodeResourceToJsonLikeWriter(IBaseResource theResource, JsonLikeWriter theJsonLikeWriter) throws IOException, DataFormatException {
		Validate.notNull(theResource, "theResource can not be null");
		Validate.notNull(theJsonLikeWriter, "theJsonLikeWriter can not be null");

		if (theResource.getStructureFhirVersionEnum() != myContext.getVersion().getVersion()) {
			throw new IllegalArgumentException("This parser is for FHIR version " + myContext.getVersion().getVersion() + " - Can not encode a structure for version " + theResource.getStructureFhirVersionEnum());
		}

		doEncodeResourceToJsonLikeWriter(theResource, theJsonLikeWriter);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, String theObjectNameOrNull, boolean theContainedResource, boolean theSubResource) throws IOException {
		IIdType resourceId = null;
		//		if (theResource instanceof IResource) {
		//			IResource res = (IResource) theResource;
		//			if (StringUtils.isNotBlank(res.getId().getIdPart())) {
		//				if (theContainedResource) {
		//					resourceId = res.getId().getIdPart();
		//				} else if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
		//					resourceId = res.getId().getIdPart();
		//				}
		//			}
		//		} else if (theResource instanceof IAnyResource) {
		//			IAnyResource res = (IAnyResource) theResource;
		//			if (/* theContainedResource && */StringUtils.isNotBlank(res.getIdElement().getIdPart())) {
		//				resourceId = res.getIdElement().getIdPart();
		//			}
		//		}

		if (StringUtils.isNotBlank(theResource.getIdElement().getIdPart())) {
			resourceId = theResource.getIdElement();
			if (theResource.getIdElement().getValue().startsWith("urn:")) {
				resourceId = null;
			}
			if (myContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU1)) {
				resourceId = null;
			}
		}

		if (!theContainedResource) {
			if (super.shouldEncodeResourceId(theResource) == false) {
				resourceId = null;
			} else if (!theSubResource && getEncodeForceResourceId() != null) {
				resourceId = getEncodeForceResourceId();
			}
		}

		encodeResourceToJsonStreamWriter(theResDef, theResource, theEventWriter, theObjectNameOrNull, theContainedResource, resourceId);
	}

	private void encodeResourceToJsonStreamWriter(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, String theObjectNameOrNull, boolean theContainedResource, IIdType theResourceId) throws IOException {
		if (!theContainedResource) {
			super.containResourcesForEncoding(theResource);
		}

		RuntimeResourceDefinition resDef = myContext.getResourceDefinition(theResource);

		if (theObjectNameOrNull == null) {
			theEventWriter.beginObject();
		} else {
			beginObject(theEventWriter, theObjectNameOrNull);
		}

		write(theEventWriter, "resourceType", resDef.getName());
		if (theResourceId != null && theResourceId.hasIdPart()) {
			write(theEventWriter, "id", theResourceId.getIdPart());
			if (theResourceId.hasFormatComment()) {
				beginObject(theEventWriter, "_id");
				writeCommentsPreAndPost(theResourceId, theEventWriter);
				theEventWriter.endObject();
			}
		}

		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1) && theResource instanceof IResource) {
			IResource resource = (IResource) theResource;
			// Object securityLabelRawObj =

			List<BaseCodingDt> securityLabels = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.SECURITY_LABELS);
			List<? extends IIdType> profiles = extractMetadataListNotNull(resource, ResourceMetadataKeyEnum.PROFILES);
			profiles = super.getProfileTagsForEncoding(resource, profiles);

			TagList tags = getMetaTagsForEncoding(resource);
			InstantDt updated = (InstantDt) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED);
			IdDt resourceId = resource.getId();
			String versionIdPart = resourceId.getVersionIdPart();
			if (isBlank(versionIdPart)) {
				versionIdPart = ResourceMetadataKeyEnum.VERSION.get(resource);
			}

			if (super.shouldEncodeResourceMeta(resource) && ElementUtil.isEmpty(versionIdPart, updated, securityLabels, tags, profiles) == false) {
				beginObject(theEventWriter, "meta");
				writeOptionalTagWithTextNode(theEventWriter, "versionId", versionIdPart);
				writeOptionalTagWithTextNode(theEventWriter, "lastUpdated", updated);

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
						encodeCompositeElementChildrenToStreamWriter(resDef, resource, securityLabel, theEventWriter, theContainedResource, null);
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

				theEventWriter.endObject(); // end meta
			}
		}

		if (theResource instanceof IBaseBinary) {
			IBaseBinary bin = (IBaseBinary) theResource;
			String contentType = bin.getContentType();
			if (isNotBlank(contentType)) {
				write(theEventWriter, "contentType", contentType);
			}
			String contentAsBase64 = bin.getContentAsBase64();
			if (isNotBlank(contentAsBase64)) {
				write(theEventWriter, "content", contentAsBase64);
			}
		} else {
			encodeCompositeElementToStreamWriter(theResDef, theResource, theResource, theEventWriter, theContainedResource, new CompositeChildElement(resDef));
		}

		theEventWriter.endObject();
	}

	@Override
	public void encodeTagListToWriter(TagList theTagList, Writer theWriter) throws IOException {
		JsonLikeWriter theEventWriter = createJsonWriter(theWriter);
		encodeTagListToJsonLikeWriter(theTagList, theEventWriter);
	}

	@Override
	public void encodeTagListToJsonLikeWriter(TagList theTagList, JsonLikeWriter theEventWriter) throws IOException {
		if (myPrettyPrint) {
			theEventWriter.setPrettyPrint(myPrettyPrint);
		}
		theEventWriter.init();

		theEventWriter.beginObject();

		write(theEventWriter, "resourceType", TagList.ELEMENT_NAME);

		beginArray(theEventWriter, TagList.ATTR_CATEGORY);
		for (Tag next : theTagList) {
			theEventWriter.beginObject();

			if (isNotBlank(next.getTerm())) {
				write(theEventWriter, Tag.ATTR_TERM, next.getTerm());
			}
			if (isNotBlank(next.getLabel())) {
				write(theEventWriter, Tag.ATTR_LABEL, next.getLabel());
			}
			if (isNotBlank(next.getScheme())) {
				write(theEventWriter, Tag.ATTR_SCHEME, next.getScheme());
			}

			theEventWriter.endObject();
		}
		theEventWriter.endArray();

		theEventWriter.endObject();
		theEventWriter.flush();
	}

	/**
	 * This is useful only for the two cases where extensions are encoded as direct children (e.g. not in some object
	 * called _name): resource extensions, and extension extensions
	 * @param theChildElem 
	 * @param theParent 
	 */
	private void extractAndWriteExtensionsAsDirectChild(IBase theElement, JsonLikeWriter theEventWriter, BaseRuntimeElementDefinition<?> theElementDef, RuntimeResourceDefinition theResDef, IBaseResource theResource, CompositeChildElement theChildElem, CompositeChildElement theParent) throws IOException {
		List<HeldExtension> extensions = new ArrayList<HeldExtension>(0);
		List<HeldExtension> modifierExtensions = new ArrayList<HeldExtension>(0);

		// Undeclared extensions
		extractUndeclaredExtensions(theElement, extensions, modifierExtensions, theChildElem, theParent);

		// Declared extensions
		if (theElementDef != null) {
			extractDeclaredExtensions(theElement, theElementDef, extensions, modifierExtensions, theChildElem);
		}

		// Write the extensions
		writeExtensionsAsDirectChild(theResource, theEventWriter, theResDef, extensions, modifierExtensions);
	}

	private void extractDeclaredExtensions(IBase theResource, BaseRuntimeElementDefinition<?> resDef, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions, CompositeChildElement theChildElem) {
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

	private void extractUndeclaredExtensions(IBase theElement, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions, CompositeChildElement theChildElem, CompositeChildElement theParent) {
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
				for (IBaseExtension<?, ?> next : ext) {
					if (next == null || (ElementUtil.isEmpty(next.getValue()) && next.getExtension().isEmpty())) {
						continue;
					}
					extensions.add(new HeldExtension(next, false, theChildElem, theParent));
				}
			}
			if (theElement instanceof IBaseHasModifierExtensions) {
				IBaseHasModifierExtensions element = (IBaseHasModifierExtensions) theElement;
				List<? extends IBaseExtension<?, ?>> ext = element.getModifierExtension();
				for (IBaseExtension<?, ?> next : ext) {
					if (next == null || next.isEmpty()) {
						continue;
					}
					modifierExtensions.add(new HeldExtension(next, true, theChildElem, theParent));
				}
			}
		}
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
			throw new DataFormatException("Syntax error parsing JSON FHIR structure: Expected ARRAY at element '" + thePosition + "', found '" + object.getJsonType() + "'");
		}
		return object.getAsArray();
	}

//	private JsonObject parse(Reader theReader) {
//
//		PushbackReader pbr = new PushbackReader(theReader);
//		JsonObject object;
//		try {
//			while(true) {
//				int nextInt;
//					nextInt = pbr.read();
//				if (nextInt == -1) {
//					throw new DataFormatException("Did not find any content to parse");
//				}
//				if (nextInt == '{') {
//					pbr.unread('{');
//					break;
//				}
//				if (Character.isWhitespace(nextInt)) {
//					continue;
//				}
//				throw new DataFormatException("Content does not appear to be FHIR JSON, first non-whitespace character was: '" + (char)nextInt + "' (must be '{')");
//			}
//		
//			Gson gson = newGson();
//		
//			object = gson.fromJson(pbr, JsonObject.class);
//		} catch (Exception e) {
//			throw new DataFormatException("Failed to parse JSON content, error was: " + e.getMessage(), e);
//		}
//		
//		return object;
//	}

	private void parseAlternates(JsonLikeValue theAlternateVal, ParserState<?> theState, String theElementName, String theAlternateName) {
		if (theAlternateVal == null || theAlternateVal.isNull()) {
			return;
		}

		if (theAlternateVal.isArray()) {
			JsonLikeArray array = theAlternateVal.getAsArray();
			if (array.size() > 1) {
				throw new DataFormatException("Unexpected array of length " + array.size() + " (expected 0 or 1) for element: " + theElementName);
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
		for (String nextKey : alternate.keySet()) {
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

	@Override
	public <T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, Reader theReader) {
		JsonLikeStructure jsonStructure = new GsonStructure();
		jsonStructure.load(theReader);
		
		Bundle retVal = parseBundle(theResourceType, jsonStructure);
		
		return retVal;
	}

	@Override
	public Bundle parseBundle(JsonLikeStructure theJsonLikeStructure) throws DataFormatException {
		return parseBundle(null, theJsonLikeStructure);
	}

	@Override
	public <T extends IBaseResource> Bundle parseBundle(Class<T> theResourceType, JsonLikeStructure theJsonStructure) {
		JsonLikeObject object = theJsonStructure.getRootObject();
		
		JsonLikeValue resourceTypeObj = object.get("resourceType");
		if (resourceTypeObj == null || !resourceTypeObj.isString()) {
			throw new DataFormatException("Invalid JSON content detected, missing required element: 'resourceType'");
		}
		String resourceType = resourceTypeObj.getAsString();
		if (!"Bundle".equals(resourceType)) {
			throw new DataFormatException("Trying to parse bundle but found resourceType other than 'Bundle'. Found: '" + resourceType + "'");
		}

		ParserState<Bundle> state = ParserState.getPreAtomInstance(this, myContext, theResourceType, true, getErrorHandler());
		if (myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU1)) {
			state.enteringNewElement(null, "Bundle");
		} else {
			state.enteringNewElement(null, "feed");
		}

		parseBundleChildren(object, state);

		state.endingElement();
		state.endingElement();

		Bundle retVal = state.getObject();

		return retVal;
	}

	private void parseBundleChildren(JsonLikeObject theObject, ParserState<?> theState) {
		for (String nextName : theObject.keySet()) {
			if ("resourceType".equals(nextName)) {
				continue;
			} else if ("entry".equals(nextName)) {
				JsonLikeArray entries = grabJsonArray(theObject, nextName, "entry");
				for (int i = 0; i < entries.size(); i++) {
					JsonLikeValue jsonValue = entries.get(i);
					theState.enteringNewElement(null, "entry");
					parseBundleChildren(jsonValue.getAsObject(), theState);
					theState.endingElement();
				}
				continue;
			} else if (myContext.getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				if ("link".equals(nextName)) {
					JsonLikeArray entries = grabJsonArray(theObject, nextName, "link");
					for (int i = 0; i < entries.size(); i++) {
						JsonLikeValue jsonValue = entries.get(i);
						theState.enteringNewElement(null, "link");
						JsonLikeObject linkObj = jsonValue.getAsObject();
						String rel = linkObj.getString("rel", null);
						String href = linkObj.getString("href", null);
						theState.attributeValue("rel", rel);
						theState.attributeValue("href", href);
						theState.endingElement();
					}
					continue;
				} else if (BUNDLE_TEXTNODE_CHILDREN_DSTU1.contains(nextName)) {
					theState.enteringNewElement(null, nextName);
					JsonLikeValue jsonElement = theObject.get(nextName);
					if (jsonElement.isScalar()) {
						theState.string(jsonElement.getAsString());
					}
					theState.endingElement();
					continue;
				}
			} else {
				if ("link".equals(nextName)) {
					JsonLikeArray entries = grabJsonArray(theObject, nextName, "link");
					for (int i = 0; i < entries.size(); i++) {
						JsonLikeValue jsonValue = entries.get(i);
						theState.enteringNewElement(null, "link");
						JsonLikeObject linkObj = jsonValue.getAsObject();
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

					JsonLikeValue obj = theObject.get(nextName);
					if (obj == null || obj.isNull()) {
						theState.attributeValue("value", null);
					} else if (obj.isScalar()) {
						theState.attributeValue("value", obj.getAsString());
					} else {
						throw new DataFormatException("Unexpected JSON object for entry '" + nextName + "'");
					}

					theState.endingElement();
					continue;
				}
			}

			JsonLikeValue nextVal = theObject.get(nextName);
			parseChildren(theState, nextName, nextVal, null, null, false);

		}
	}

	private void parseChildren(JsonLikeObject theObject, ParserState<?> theState) {
		Set<String> keySet = theObject.keySet();

		int allUnderscoreNames = 0;
		int handledUnderscoreNames = 0;

		for (String nextName : keySet) {
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

//		if (elementId != null) {
//			IBase object = (IBase) theState.getObject();
//			if (object instanceof IIdentifiableElement) {
//				((IIdentifiableElement) object).setElementSpecificId(elementId);
//			} else if (object instanceof IBaseResource) {
//				((IBaseResource) object).getIdElement().setValue(elementId);
//			}
//		}

		/*
		 * This happens if an element has an extension but no actual value. I.e.
		 * if a resource has a "_status" element but no corresponding "status"
		 * element. This could be used to handle a null value with an extension
		 * for example.
		 */
		if (allUnderscoreNames > handledUnderscoreNames) {
			for (String alternateName : keySet) {
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
				if (nextAlternateArray != null) {
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
					throw new DataFormatException("Missing required element 'resourceType' from JSON resource object, unable to parse");
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
			theState.attributeValue("value", theJsonVal.getAsString());
			parseAlternates(theAlternateVal, theState, theAlternateName, theAlternateName);
			theState.endingElement();
		}
	}

	private void parseExtension(ParserState<?> theState, JsonLikeArray theValues, boolean theIsModifier) {
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
				getErrorHandler().missingRequiredElement(new ParseLocation(parentElementName), "url");
				url = null;
			} else {
				url = getExtensionUrl(jsonElement.getAsString());
			}
			theState.enteringNewElementExtension(null, url, theIsModifier, getServerBaseUrl());
			for (String next : nextExtObj.keySet()) {
				if ("url".equals(next)) {
					continue;
				} else if ("extension".equals(next)) {
					JsonLikeArray jsonVal = JsonLikeValue.asArray(nextExtObj.get(next));
					parseExtension(theState, jsonVal, false);
				} else if ("modifierExtension".equals(next)) {
					JsonLikeArray jsonVal = JsonLikeValue.asArray(nextExtObj.get(next));
					parseExtension(theState, jsonVal, true);
				} else {
					JsonLikeValue jsonVal = nextExtObj.get(next);
					parseChildren(theState, next, jsonVal, null, null, false);
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
		 * ** NOTE: this duplicates most of the code in   ** *
		 * ** BaseParser.parseResource(Class<T>, Reader). ** *
		 * ** Unfortunately, there is no way to avoid     ** *   
		 * ** this without doing some refactoring of the  ** *
		 * ** BaseParser class.                           ** *
		 * ************************************************* *
		 *****************************************************/
		
		/*
		 * We do this so that the context can verify that the structure is for
		 * the correct FHIR version
		 */
		if (theResourceType != null) {
			myContext.getResourceDefinition(theResourceType);
		}

		// Actually do the parse
		T retVal = doParseResource(theResourceType, theJsonLikeStructure);

		RuntimeResourceDefinition def = myContext.getResourceDefinition(retVal);
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
	public TagList parseTagList(Reader theReader) {
		JsonLikeStructure jsonStructure = new GsonStructure();
		jsonStructure.load(theReader);
		
		TagList retVal = parseTagList(jsonStructure);
		
		return retVal;
	}
	
	@Override
	public TagList parseTagList(JsonLikeStructure theJsonStructure) {
		JsonLikeObject object = theJsonStructure.getRootObject();

		JsonLikeValue resourceTypeObj = object.get("resourceType");
		String resourceType = resourceTypeObj.getAsString();

		ParserState<TagList> state = ParserState.getPreTagListInstance(this, myContext, true, getErrorHandler());
		state.enteringNewElement(null, resourceType);

		parseChildren(object, state);

		state.endingElement();
		state.endingElement();

		return state.getObject();
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

	private boolean writeAtomLinkInDstu1Format(JsonLikeWriter theEventWriter, String theRel, StringDt theLink, boolean theStarted) throws IOException {
		boolean retVal = theStarted;
		if (isNotBlank(theLink.getValue())) {
			if (theStarted == false) {
				theEventWriter.beginArray("link");
				retVal = true;
			}

			theEventWriter.beginObject();
			write(theEventWriter, "rel", theRel);
			write(theEventWriter, "href", theLink.getValue());
			theEventWriter.endObject();
		}
		return retVal;
	}

	private boolean writeAtomLinkInDstu2Format(JsonLikeWriter theEventWriter, String theRel, StringDt theLink, boolean theStarted) throws IOException {
		boolean retVal = theStarted;
		if (isNotBlank(theLink.getValue())) {
			if (theStarted == false) {
				theEventWriter.beginArray("link");
				retVal = true;
			}

			theEventWriter.beginObject();
			write(theEventWriter, "relation", theRel);
			write(theEventWriter, "url", theLink.getValue());
			theEventWriter.endObject();
		}
		return retVal;
	}

	private void writeAuthor(BaseBundle theBundle, JsonLikeWriter theEventWriter) throws IOException {
		if (StringUtils.isNotBlank(theBundle.getAuthorName().getValue())) {
			beginArray(theEventWriter, "author");
			theEventWriter.beginObject();
			writeTagWithTextNode(theEventWriter, "name", theBundle.getAuthorName());
			writeOptionalTagWithTextNode(theEventWriter, "uri", theBundle.getAuthorUri());
			theEventWriter.endObject();
			theEventWriter.endArray();
		}
	}

	private void writeCategories(JsonLikeWriter theEventWriter, TagList categories) throws IOException {
		if (categories != null && categories.size() > 0) {
			theEventWriter.beginArray("category");
			for (Tag next : categories) {
				theEventWriter.beginObject();
				write(theEventWriter, "term", defaultString(next.getTerm()));
				write(theEventWriter, "label", defaultString(next.getLabel()));
				write(theEventWriter, "scheme", defaultString(next.getScheme()));
				theEventWriter.endObject();
			}
			theEventWriter.endArray();
		}
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

	private void writeExtensionsAsDirectChild(IBaseResource theResource, JsonLikeWriter theEventWriter, RuntimeResourceDefinition resDef, List<HeldExtension> extensions, List<HeldExtension> modifierExtensions) throws IOException {
		if (extensions.isEmpty() == false) {
			beginArray(theEventWriter, "extension");
			for (HeldExtension next : extensions) {
				next.write(resDef, theResource, theEventWriter);
			}
			theEventWriter.endArray();
		}
		if (modifierExtensions.isEmpty() == false) {
			beginArray(theEventWriter, "modifierExtension");
			for (HeldExtension next : modifierExtensions) {
				next.write(resDef, theResource, theEventWriter);
			}
			theEventWriter.endArray();
		}
	}

	private void writeOptionalTagWithDecimalNode(JsonLikeWriter theEventWriter, String theElementName, DecimalDt theValue) throws IOException {
		if (theValue != null && theValue.isEmpty() == false) {
			write(theEventWriter, theElementName, theValue.getValue());
		}
	}

	private void writeOptionalTagWithNumberNode(JsonLikeWriter theEventWriter, String theElementName, IntegerDt theValue) throws IOException {
		if (theValue != null && theValue.isEmpty() == false) {
			write(theEventWriter, theElementName, theValue.getValue().intValue());
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

	private void writeTagWithTextNode(JsonLikeWriter theEventWriter, String theElementName, IPrimitiveDatatype<?> theIdDt) throws IOException {
		if (theIdDt != null && !theIdDt.isEmpty()) {
			write(theEventWriter, theElementName, theIdDt.getValueAsString());
		} else {
			theEventWriter.writeNull(theElementName);
		}
	}

	private void writeTagWithTextNode(JsonLikeWriter theEventWriter, String theElementName, StringDt theStringDt) throws IOException {
		if (StringUtils.isNotBlank(theStringDt.getValue())) {
			write(theEventWriter, theElementName, theStringDt.getValue());
		}
		// else {
		// theEventWriter.writeNull(theElementName);
		// }
	}

	public static Gson newGson() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson;
	}
	
	private static void write(JsonLikeWriter theWriter, String theName, String theValue) throws IOException {
		theWriter.write(theName, theValue);
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

		public void write(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter) throws IOException {
			if (myUndeclaredExtension != null) {
				writeUndeclaredExtension(theResDef, theResource, theEventWriter, myUndeclaredExtension);
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
				List<? extends IBase> preProcessedValue = preProcessValues(myDef, theResource, Collections.singletonList(myValue), myChildElem);

//				// Check for undeclared extensions on the declared extension
//				// (grrrrrr....)
//				if (myValue instanceof ISupportsUndeclaredExtensions) {
//					ISupportsUndeclaredExtensions value = (ISupportsUndeclaredExtensions)myValue;
//					List<ExtensionDt> exts = value.getUndeclaredExtensions();
//					if (exts.size() > 0) {
//						ArrayList<IBase> newValueList = new ArrayList<IBase>();
//						newValueList.addAll(preProcessedValue);
//						newValueList.addAll(exts);
//						preProcessedValue = newValueList;
//					}
//				}
				
				myValue = preProcessedValue.get(0);
				
				BaseRuntimeElementDefinition<?> def = myDef.getChildElementDefinitionByDatatype(myValue.getClass());
				if (def.getChildType() == ChildTypeEnum.RESOURCE_BLOCK) {
					extractAndWriteExtensionsAsDirectChild(myValue, theEventWriter, def, theResDef, theResource, myChildElem, null);
				} else {
					String childName = myDef.getChildNameByDatatype(myValue.getClass());
					encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, myValue, def, childName, false, myParent, false);
				}

				theEventWriter.endObject();
			}
		}

		
		private void writeUndeclaredExtension(RuntimeResourceDefinition theResDef, IBaseResource theResource, JsonLikeWriter theEventWriter, IBaseExtension<?, ?> ext) throws IOException {
			IBase value = ext.getValue();
			final String extensionUrl = getExtensionUrl(ext.getUrl());

			theEventWriter.beginObject();

			writeCommentsPreAndPost(myUndeclaredExtension, theEventWriter);

			String elementId = getCompositeElementId(ext);
			if (isNotBlank(elementId)) {
				JsonParser.write(theEventWriter, "id", getCompositeElementId(ext));
			}

			JsonParser.write(theEventWriter, "url", extensionUrl);

			boolean noValue = value == null || value.isEmpty();
			if (noValue && ext.getExtension().isEmpty()) {
				ourLog.debug("Extension with URL[{}] has no value", extensionUrl);
			} else if (noValue) {

				if (myModifier) {
					beginArray(theEventWriter, "modifierExtension");
				} else {
					beginArray(theEventWriter, "extension");
				}

				for (Object next : ext.getExtension()) {
					writeUndeclaredExtension(theResDef, theResource, theEventWriter, (IBaseExtension<?, ?>) next);
				}
				theEventWriter.endArray();
			} else {

				/*
				 * Pre-process value - This is called in case the value is a reference
				 * since we might modify the text
				 */
				value = JsonParser.super.preProcessValues(myDef, theResource, Collections.singletonList(value), myChildElem).get(0);

				RuntimeChildUndeclaredExtensionDefinition extDef = myContext.getRuntimeChildUndeclaredExtensionDefinition();
				String childName = extDef.getChildNameByDatatype(value.getClass());
				if (childName == null) {
					childName = "value" + WordUtils.capitalize(myContext.getElementDefinition(value.getClass()).getName());
				}
				BaseRuntimeElementDefinition<?> childDef = extDef.getChildElementDefinitionByDatatype(value.getClass());
				if (childDef == null) {
					throw new ConfigurationException("Unable to encode extension, unregognized child element type: " + value.getClass().getCanonicalName());
				}
				encodeChildElementToStreamWriter(theResDef, theResource, theEventWriter, value, childDef, childName, true, myParent, false);
			}

			// theEventWriter.name(myUndeclaredExtension.get);

			theEventWriter.endObject();
		}

	}

}
