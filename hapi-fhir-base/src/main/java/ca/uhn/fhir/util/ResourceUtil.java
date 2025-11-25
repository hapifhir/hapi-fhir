/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.EncodingEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Strings;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class ResourceUtil {

	private static final String ENCODING = "ENCODING_TYPE";
	private static final String RAW_ = "RAW_";
	private static final String EQUALS_DEEP = "equalsDeep";
	public static final String DATA_ABSENT_REASON_EXTENSION_URI =
			"http://hl7.org/fhir/StructureDefinition/data-absent-reason";

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceUtil.class);

	public static class MergeControlParameters {
		private boolean ignoreCodeableConceptCodingOrder;

		public boolean isIgnoreCodeableConceptCodingOrder() {
			return ignoreCodeableConceptCodingOrder;
		}

		public void setIgnoreCodeableConceptCodingOrder(boolean theIgnoreCodeableConceptCodingOrder) {
			ignoreCodeableConceptCodingOrder = theIgnoreCodeableConceptCodingOrder;
		}
	}

	private ResourceUtil() {}

	/**
	 * Exclusion predicate for keeping all fields.
	 */
	public static final Predicate<String> INCLUDE_ALL = s -> true;

	/**
	 * This method removes the narrative from the resource, or if the resource is a bundle, removes the narrative from
	 * all of the resources in the bundle
	 *
	 * @param theContext The fhir context
	 * @param theInput   The resource to remove the narrative from
	 */
	public static void removeNarrative(FhirContext theContext, IBaseResource theInput) {
		if (theInput instanceof IBaseBundle) {
			for (IBaseResource next : BundleUtil.toListOfResources(theContext, (IBaseBundle) theInput)) {
				removeNarrative(theContext, next);
			}
		}

		BaseRuntimeElementCompositeDefinition<?> element = theContext.getResourceDefinition(theInput.getClass());
		BaseRuntimeChildDefinition textElement = element.getChildByName("text");
		if (textElement != null) {
			textElement.getMutator().setValue(theInput, null);
		}
	}

	public static void addRawDataToResource(
			@Nonnull IBaseResource theResource, @Nonnull EncodingEnum theEncodingType, String theSerializedData) {
		theResource.setUserData(getRawUserDataKey(theEncodingType), theSerializedData);
		theResource.setUserData(ENCODING, theEncodingType);
	}

	public static EncodingEnum getEncodingTypeFromUserData(@Nonnull IBaseResource theResource) {
		return (EncodingEnum) theResource.getUserData(ENCODING);
	}

	public static String getRawStringFromResourceOrNull(@Nonnull IBaseResource theResource) {
		EncodingEnum type = (EncodingEnum) theResource.getUserData(ENCODING);
		if (type != null) {
			return (String) theResource.getUserData(getRawUserDataKey(type));
		}
		return null;
	}

	private static String getRawUserDataKey(EncodingEnum theEncodingEnum) {
		return RAW_ + theEncodingEnum.name();
	}

	/**
	 * Merges all fields on the provided instance. <code>theTo</code> will contain a union of all values from <code>theFrom</code>
	 * instance and <code>theTo</code> instance.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFrom        The resource to merge the fields from
	 * @param theTo          The resource to merge the fields into
	 */
	public static void mergeAllFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		mergeFields(theFhirContext, theFrom, theTo, INCLUDE_ALL);
	}

	/**
	 * Merges values of all field from <code>theFrom</code> resource to <code>theTo</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext    Context holding resource definition
	 * @param theFrom           Resource to merge the specified field from
	 * @param theTo             Resource to merge the specified field into
	 * @param inclusionStrategy Predicate to test which fields should be merged
	 */
	public static void mergeFields(
			FhirContext theFhirContext,
			IBaseResource theFrom,
			IBaseResource theTo,
			Predicate<String> inclusionStrategy) {
		FhirTerser terser = theFhirContext.newTerser();

		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		for (BaseRuntimeChildDefinition childDefinition : definition.getChildrenAndExtension()) {
			if (!inclusionStrategy.test(childDefinition.getElementName())) {
				continue;
			}

			List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
			List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

			mergeFields(terser, theTo, childDefinition, theFromFieldValues, theToFieldValues);
		}
	}

	/**
	 * Merges value of the specified field from <code>theFrom</code> resource to <code>theTo</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFieldName   Name of the child filed to merge
	 * @param theFrom        Resource to merge the specified field from
	 * @param theTo          Resource to merge the specified field into
	 */
	public static void mergeField(
			FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		mergeField(theFhirContext, theFhirContext.newTerser(), theFieldName, theFrom, theTo);
	}

	/**
	 * Merges value of the specified field from <code>theFrom</code> resource to <code>theTo</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext            Context holding resource definition
	 * @param theFieldName              Name of the child filed to merge
	 * @param theFrom                   Resource to merge the specified field from
	 * @param theTo                     Resource to merge the specified field into
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 */
	public static void mergeField(
			FhirContext theFhirContext,
			String theFieldName,
			IBaseResource theFrom,
			IBaseResource theTo,
			MergeControlParameters theMergeControlParameters) {
		mergeField(theFhirContext, theFhirContext.newTerser(), theFieldName, theFrom, theTo, theMergeControlParameters);
	}

	/**
	 * Merges value of the specified field from <code>theFrom</code> resource to <code>theTo</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theTerser      Terser to be used when cloning the field values
	 * @param theFieldName   Name of the child filed to merge
	 * @param theFrom        Resource to merge the specified field from
	 * @param theTo          Resource to merge the specified field into
	 */
	public static void mergeField(
			FhirContext theFhirContext,
			FhirTerser theTerser,
			String theFieldName,
			IBaseResource theFrom,
			IBaseResource theTo) {
		mergeField(theFhirContext, theTerser, theFieldName, theFrom, theTo, new MergeControlParameters());
	}

	/**
	 * Merges value of the specified field from <code>theFrom</code> resource to <code>theTo</code> resource. Fields
	 * values are compared via the equalsDeep method, or via object identity if this method is not available.
	 *
	 * @param theFhirContext            Context holding resource definition
	 * @param theTerser                 Terser to be used when cloning the field values
	 * @param theFieldName              Name of the child filed to merge
	 * @param theFrom                   Resource to merge the specified field from
	 * @param theTo                     Resource to merge the specified field into
	 * @param theMergeControlParameters Parameters to provide fine-grained control over the behaviour of the merge
	 */
	public static void mergeField(
			FhirContext theFhirContext,
			FhirTerser theTerser,
			String theFieldName,
			IBaseResource theFrom,
			IBaseResource theTo,
			MergeControlParameters theMergeControlParameters) {
		BaseRuntimeChildDefinition childDefinition =
				getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theFrom);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		mergeFields(theTerser, theTo, childDefinition, theFromFieldValues, theToFieldValues, theMergeControlParameters);
	}

	private static void mergeFields(
			FhirTerser theTerser,
			IBaseResource theTo,
			BaseRuntimeChildDefinition childDefinition,
			List<IBase> theFromFieldValues,
			List<IBase> theToFieldValues) {
		mergeFields(
				theTerser, theTo, childDefinition, theFromFieldValues, theToFieldValues, new MergeControlParameters());
	}

	private static void mergeFields(
			FhirTerser theTerser,
			IBaseResource theTo,
			BaseRuntimeChildDefinition childDefinition,
			List<IBase> theFromFieldValues,
			List<IBase> theToFieldValues,
			MergeControlParameters theMergeControlParameters) {
		if (!theFromFieldValues.isEmpty() && theToFieldValues.stream().anyMatch(ResourceUtil::hasDataAbsentReason)) {
			// If the to resource has a data absent reason, and there is potentially real data incoming
			// in the from resource, we should clear the data absent reason because it won't be absent anymore.
			theToFieldValues = removeDataAbsentReason(theTo, childDefinition, theToFieldValues);
		}

		List<IBase> filteredFromFieldValues = new ArrayList<>();
		for (IBase fromFieldValue : theFromFieldValues) {
			if (theToFieldValues.isEmpty()) {
				// if the target field is unpopulated, accept any value from the source field
				filteredFromFieldValues.add(fromFieldValue);
			} else if (!hasDataAbsentReason(fromFieldValue)) {
				// if the value from the source field does not have a data absent reason extension,
				// evaluate its suitability for inclusion
				if (Strings.CI.equals(fromFieldValue.fhirType(), "codeableConcept")) {
					if (!containsCodeableConcept(
							fromFieldValue, theToFieldValues, theTerser, theMergeControlParameters)) {
						filteredFromFieldValues.add(fromFieldValue);
					}
				} else if (!contains(fromFieldValue, theToFieldValues)) {
					// include it if the target list doesn't already contain an exact match
					filteredFromFieldValues.add(fromFieldValue);
				}
			}
		}

		for (IBase fromFieldValue : filteredFromFieldValues) {
			IBase newFieldValue = newElement(theTerser, childDefinition, fromFieldValue);
			if (fromFieldValue instanceof IPrimitiveType) {
				try {
					Method copyMethod = getMethod(fromFieldValue, "copy");
					if (copyMethod != null) {
						newFieldValue = (IBase) copyMethod.invoke(fromFieldValue);
					}
				} catch (Exception t) {
					((IPrimitiveType<?>) newFieldValue)
							.setValueAsString(((IPrimitiveType<?>) fromFieldValue).getValueAsString());
				}
			} else {
				theTerser.cloneInto(fromFieldValue, newFieldValue, true);
			}

			try {
				theToFieldValues.add(newFieldValue);
			} catch (UnsupportedOperationException e) {
				childDefinition.getMutator().setValue(theTo, newFieldValue);
				theToFieldValues = childDefinition.getAccessor().getValues(theTo);
			}
		}
	}

	private static BaseRuntimeChildDefinition getBaseRuntimeChildDefinition(
			FhirContext theFhirContext, String theFieldName, IBaseResource theFrom) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theFieldName);
		Objects.requireNonNull(childDefinition);
		return childDefinition;
	}

	private static Method getMethod(IBase theBase, String theMethodName) {
		Method method = null;
		for (Method m : theBase.getClass().getDeclaredMethods()) {
			if (m.getName().equals(theMethodName)) {
				method = m;
				break;
			}
		}
		return method;
	}

	private static boolean evaluateEquality(IBase theItem1, IBase theItem2, Method theMethod) {
		if (theMethod != null) {
			try {
				return (Boolean) theMethod.invoke(theItem1, theItem2);
			} catch (Exception e) {
				ourLog.debug("{} Unable to compare equality via {}", Msg.code(2821), theMethod.getName(), e);
			}
		}
		return theItem1.equals(theItem2);
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		final Method method = getMethod(theItem, EQUALS_DEEP);
		return theItems.stream().anyMatch(i -> evaluateEquality(i, theItem, method));
	}


	private static boolean containsCodeableConcept(
			IBase theSourceItem,
			List<IBase> theTargetItems,
			FhirTerser theTerser,
			MergeControlParameters theMergeControlParameters) {
		Method shallowEquals = getMethod(theSourceItem, "equalsShallow");
		List<IBase> shallowMatches = theTargetItems.stream()
				.filter(targetItem -> evaluateEquality(targetItem, theSourceItem, shallowEquals))
				.toList();

		if (theMergeControlParameters.isIgnoreCodeableConceptCodingOrder()) {
			return shallowMatches.stream().anyMatch(targetItem -> {
				List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
				List<IBase> targetCodings = theTerser.getValues(targetItem, "coding");
				return sourceCodings.stream().allMatch(sourceCoding -> {
					Method deepEquals = getMethod(sourceCoding, EQUALS_DEEP);
					return targetCodings.stream()
							.anyMatch(targetCoding -> evaluateEquality(sourceCoding, targetCoding, deepEquals));
				});
			});
		} else {
			return shallowMatches.stream().anyMatch(targetItem -> {
				boolean match = true;
				List<IBase> sourceCodings = theTerser.getValues(theSourceItem, "coding");
				List<IBase> targetCodings = theTerser.getValues(targetItem, "coding");
				if (sourceCodings.size() == targetCodings.size()) {
					for (int i = 0; i < sourceCodings.size(); i++) {
						Method deepEquals = getMethod(sourceCodings.get(i), EQUALS_DEEP);
						match &= evaluateEquality(sourceCodings.get(i), targetCodings.get(i), deepEquals);
					}
				} else {
					match = false;
				}
				return match;
			});
		}
	}

	private static boolean hasDataAbsentReason(IBase theItem) {
		if (theItem instanceof IBaseHasExtensions hasExtensions) {
			return hasExtensions.getExtension().stream()
					.anyMatch(t -> Strings.CS.equals(t.getUrl(), DATA_ABSENT_REASON_EXTENSION_URI));
		}
		return false;
	}

	private static List<IBase> removeDataAbsentReason(
			IBaseResource theResource, BaseRuntimeChildDefinition theFieldDefinition, List<IBase> theFieldValues) {
		for (int i = 0; i < theFieldValues.size(); i++) {
			if (hasDataAbsentReason(theFieldValues.get(i))) {
				try {
					theFieldDefinition.getMutator().remove(theResource, i);
				} catch (UnsupportedOperationException e) {
					// the field must be single-valued, just clear it
					theFieldDefinition.getMutator().setValue(theResource, null);
				}
			}
		}
		return theFieldDefinition.getAccessor().getValues(theResource);
	}

	/**
	 * Creates a new element taking into consideration elements with choice that are not directly retrievable by element
	 * name
	 *
	 * @param theFhirTerser      A terser instance for the FHIR release
	 * @param theChildDefinition Child to create a new instance for
	 * @param theFromFieldValue  The base parent field
	 * @return Returns the new element with the given value if configured
	 */
	private static IBase newElement(
			FhirTerser theFhirTerser, BaseRuntimeChildDefinition theChildDefinition, IBase theFromFieldValue) {
		BaseRuntimeElementDefinition<?> runtimeElementDefinition;
		if (theChildDefinition instanceof RuntimeChildChoiceDefinition) {
			runtimeElementDefinition =
					theChildDefinition.getChildElementDefinitionByDatatype(theFromFieldValue.getClass());
		} else {
			runtimeElementDefinition = theChildDefinition.getChildByName(theChildDefinition.getElementName());
		}
		if ("contained".equals(runtimeElementDefinition.getName())) {
			IBaseResource sourceResource = (IBaseResource) theFromFieldValue;
			return theFhirTerser.clone(sourceResource);
		} else {
			return runtimeElementDefinition.newInstance();
		}
	}
}
