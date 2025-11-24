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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

public class ResourceUtil {

	private static final String ENCODING = "ENCODING_TYPE";
	private static final String RAW_ = "RAW_";
	private static final String EQUALS_DEEP = "equalsDeep";
	public static final String DATA_ABSENT_REASON_EXTENSION_URI =
			"http://hl7.org/fhir/StructureDefinition/data-absent-reason";

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
			@Nonnull IBaseResource theResource, @Nonnull EncodingEnum theEncodingType, String theSerializedData)
			throws IOException {
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
		BaseRuntimeChildDefinition childDefinition =
				getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theFrom);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		mergeFields(theTerser, theTo, childDefinition, theFromFieldValues, theToFieldValues);
	}

	private static void mergeFields(
			FhirTerser theTerser,
			IBaseResource theTo,
			BaseRuntimeChildDefinition childDefinition,
			List<IBase> theFromFieldValues,
			List<IBase> theToFieldValues) {
		if (!theFromFieldValues.isEmpty() && theToFieldValues.stream().anyMatch(ResourceUtil::hasDataAbsentReason)) {
			// If the to resource has a data absent reason, and there is potentially real data incoming
			// in the from resource, we should clear the data absent reason because it won't be absent anymore.
			theToFieldValues = removeDataAbsentReason(theTo, childDefinition, theToFieldValues);
		}

		for (IBase fromFieldValue : theFromFieldValues) {
			if (contains(fromFieldValue, theToFieldValues)) {
				continue;
			}

			if (hasDataAbsentReason(fromFieldValue) && !theToFieldValues.isEmpty()) {
				// if the from field value asserts a reason the field isn't populated, but the to field is populated,
				// we don't want to overwrite real data with the extension
				continue;
			}

			IBase newFieldValue = newElement(theTerser, childDefinition, fromFieldValue, null);
			if (fromFieldValue instanceof IPrimitiveType) {
				try {
					Method copyMethod = getMethod(fromFieldValue, "copy");
					if (copyMethod != null) {
						newFieldValue = (IBase) copyMethod.invoke(fromFieldValue, new Object[] {});
					}
				} catch (Throwable t) {
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
		Validate.notNull(childDefinition);
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

	private static boolean equals(IBase theItem1, IBase theItem2, Method theMethod) {
		if (theMethod != null) {
			try {
				return (Boolean) theMethod.invoke(theItem1, theItem2);
			} catch (Exception e) {
				throw new RuntimeException(
						Msg.code(1746) + String.format("Unable to compare equality via %s", EQUALS_DEEP), e);
			}
		}
		return theItem1.equals(theItem2);
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		final Method method = getMethod(theItem, EQUALS_DEEP);
		return theItems.stream().anyMatch(i -> equals(i, theItem, method));
	}

	private static boolean hasDataAbsentReason(IBase theItem) {
		if (theItem instanceof IBaseHasExtensions) {
			IBaseHasExtensions hasExtensions = (IBaseHasExtensions) theItem;
			return hasExtensions.getExtension().stream()
					.anyMatch(t -> StringUtils.equals(t.getUrl(), DATA_ABSENT_REASON_EXTENSION_URI));
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
	 * @param theFhirTerser
	 * @param theChildDefinition  Child to create a new instance for
	 * @param theFromFieldValue   The base parent field
	 * @param theConstructorParam Optional constructor param
	 * @return Returns the new element with the given value if configured
	 */
	private static IBase newElement(
			FhirTerser theFhirTerser,
			BaseRuntimeChildDefinition theChildDefinition,
			IBase theFromFieldValue,
			Object theConstructorParam) {
		BaseRuntimeElementDefinition runtimeElementDefinition;
		if (theChildDefinition instanceof RuntimeChildChoiceDefinition) {
			runtimeElementDefinition =
					theChildDefinition.getChildElementDefinitionByDatatype(theFromFieldValue.getClass());
		} else {
			runtimeElementDefinition = theChildDefinition.getChildByName(theChildDefinition.getElementName());
		}
		if ("contained".equals(runtimeElementDefinition.getName())) {
			IBaseResource sourceResource = (IBaseResource) theFromFieldValue;
			return theFhirTerser.clone(sourceResource);
		} else if (theConstructorParam == null) {
			return runtimeElementDefinition.newInstance();
		} else {
			return runtimeElementDefinition.newInstance(theConstructorParam);
		}
	}
}
