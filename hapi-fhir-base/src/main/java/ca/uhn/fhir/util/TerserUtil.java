package ca.uhn.fhir.util;

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
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeChildChoiceDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Triple;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.slf4j.LoggerFactory.getLogger;

public final class TerserUtil {

	public static final String FIELD_NAME_IDENTIFIER = "identifier";
	/**
	 * Exclude for id, identifier and meta fields of a resource.
	 */
	public static final Collection<String> IDS_AND_META_EXCLUDES =
		Collections.unmodifiableSet(Stream.of("id", "identifier", "meta").collect(Collectors.toSet()));
	/**
	 * Exclusion predicate for id, identifier, meta fields.
	 */
	public static final Predicate<String> EXCLUDE_IDS_AND_META = new Predicate<String>() {
		@Override
		public boolean test(String s) {
			return !IDS_AND_META_EXCLUDES.contains(s);
		}
	};
	/**
	 * Exclusion predicate for id/identifier, meta and fields with empty values. This ensures that source / target resources,
	 * empty source fields will not results in erasure of target fields.
	 */
	public static final Predicate<Triple<BaseRuntimeChildDefinition, IBase, IBase>> EXCLUDE_IDS_META_AND_EMPTY = new Predicate<Triple<BaseRuntimeChildDefinition, IBase, IBase>>() {
		@Override
		public boolean test(Triple<BaseRuntimeChildDefinition, IBase, IBase> theTriple) {
			if (!EXCLUDE_IDS_AND_META.test(theTriple.getLeft().getElementName())) {
				return false;
			}
			BaseRuntimeChildDefinition childDefinition = theTriple.getLeft();
			boolean isSourceFieldEmpty = childDefinition.getAccessor().getValues(theTriple.getMiddle()).isEmpty();
			return !isSourceFieldEmpty;
		}
	};
	/**
	 * Exclusion predicate for keeping all fields.
	 */
	public static final Predicate<String> INCLUDE_ALL = new Predicate<String>() {
		@Override
		public boolean test(String s) {
			return true;
		}
	};
	private static final Logger ourLog = getLogger(TerserUtil.class);
	private static final String EQUALS_DEEP = "equalsDeep";

	private TerserUtil() {
	}

	/**
	 * Given an Child Definition of `identifier`, a R4/DSTU3 EID Identifier, and a new resource, clone the EID into that resources' identifier list.
	 */
	public static void cloneEidIntoResource(FhirContext theFhirContext, BaseRuntimeChildDefinition theIdentifierDefinition, IBase theEid, IBase theResourceToCloneEidInto) {
		// FHIR choice types - fields within fhir where we have a choice of ids
		BaseRuntimeElementCompositeDefinition<?> childIdentifier = (BaseRuntimeElementCompositeDefinition<?>) theIdentifierDefinition.getChildByName(FIELD_NAME_IDENTIFIER);
		IBase resourceNewIdentifier = childIdentifier.newInstance();

		FhirTerser terser = theFhirContext.newTerser();
		terser.cloneInto(theEid, resourceNewIdentifier, true);
		theIdentifierDefinition.getMutator().addValue(theResourceToCloneEidInto, resourceNewIdentifier);
	}

	/**
	 * Checks if the specified fields has any values
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource    Resource to check if the specified field is set
	 * @param theFieldName   name of the field to check
	 * @return Returns true if field exists and has any values set, and false otherwise
	 */
	public static boolean hasValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResource);
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(theFieldName);
		if (resourceIdentifier == null) {
			return false;
		}
		return !(resourceIdentifier.getAccessor().getValues(theResource).isEmpty());
	}

	/**
	 * Gets all values of the specified field.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource    Resource to check if the specified field is set
	 * @param theFieldName   name of the field to check
	 * @return Returns all values for the specified field or null if field with the provided name doesn't exist
	 */
	public static List<IBase> getValues(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResource);
		BaseRuntimeChildDefinition resourceIdentifier = resourceDefinition.getChildByName(theFieldName);
		if (resourceIdentifier == null) {
			ourLog.info("There is no field named {} in Resource {}", theFieldName, resourceDefinition.getName());
			return null;
		}
		return resourceIdentifier.getAccessor().getValues(theResource);
	}

	/**
	 * Gets the first available value for the specified field.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource    Resource to check if the specified field is set
	 * @param theFieldName   name of the field to check
	 * @return Returns the first value for the specified field or null if field with the provided name doesn't exist or
	 * has no values
	 */
	public static IBase getValueFirstRep(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		List<IBase> values = getValues(theFhirContext, theResource, theFieldName);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	/**
	 * Clones specified composite field (collection). Composite field values must conform to the collections
	 * contract.
	 *
	 * @param theFrom  Resource to clone the specified field from
	 * @param theTo    Resource to clone the specified field to
	 * @param theField Field name to be copied
	 */
	public static void cloneCompositeField(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, String theField) {
		FhirTerser terser = theFhirContext.newTerser();

		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theField);
		Validate.notNull(childDefinition);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		for (IBase theFromFieldValue : theFromFieldValues) {
			if (containsPrimitiveValue(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			IBase newFieldValue = newElement(terser, childDefinition, theFromFieldValue, null);
			terser.cloneInto(theFromFieldValue, newFieldValue, true);

			try {
				theToFieldValues.add(newFieldValue);
			} catch (Exception e) {
				childDefinition.getMutator().setValue(theTo, newFieldValue);
			}
		}
	}

	private static boolean containsPrimitiveValue(IBase theItem, List<IBase> theItems) {
		PrimitiveTypeEqualsPredicate predicate = new PrimitiveTypeEqualsPredicate();
		return theItems.stream().anyMatch(i -> {
			return predicate.test(i, theItem);
		});
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

	/**
	 * Checks if two items are equal via {@link #EQUALS_DEEP} method
	 *
	 * @param theItem1 First item to compare
	 * @param theItem2 Second item to compare
	 * @return Returns true if they are equal and false otherwise
	 */
	public static boolean equals(IBase theItem1, IBase theItem2) {
		if (theItem1 == null) {
			return theItem2 == null;
		}

		final Method method = getMethod(theItem1, EQUALS_DEEP);
		Validate.notNull(method);
		return equals(theItem1, theItem2, method);
	}

	private static boolean equals(IBase theItem1, IBase theItem2, Method theMethod) {
		if (theMethod != null) {
			try {
				return (Boolean) theMethod.invoke(theItem1, theItem2);
			} catch (Exception e) {
				throw new RuntimeException(Msg.code(1746) + String.format("Unable to compare equality via %s", EQUALS_DEEP), e);
			}
		}
		return theItem1.equals(theItem2);
	}

	private static boolean contains(IBase theItem, List<IBase> theItems) {
		final Method method = getMethod(theItem, EQUALS_DEEP);
		return theItems.stream().anyMatch(i -> equals(i, theItem, method));
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
	 * Replaces all fields that have matching field names by the given inclusion strategy. <code>theTo</code> will contain a copy of the
	 * values from <code>theFrom</code> instance.
	 *
	 * @param theFhirContext        Context holding resource definition
	 * @param theFrom               The resource to merge the fields from
	 * @param theTo                 The resource to merge the fields into
	 * @param theFieldNameInclusion Inclusion strategy that checks if a given field should be replaced
	 */
	public static void replaceFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> theFieldNameInclusion) {
		Predicate<Triple<BaseRuntimeChildDefinition, IBase, IBase>> predicate
			= (t) -> theFieldNameInclusion.test(t.getLeft().getElementName());
		replaceFieldsByPredicate(theFhirContext, theFrom, theTo, predicate);
	}

	/**
	 * Replaces fields on theTo resource that test positive by the given predicate. <code>theTo</code> will contain a copy of the
	 * values from <code>theFrom</code> for which predicate tests positive. Please note that composite fields will be replaced fully.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFrom        The resource to merge the fields from
	 * @param theTo          The resource to merge the fields into
	 * @param thePredicate   Predicate that checks if a given field should be replaced
	 */
	public static void replaceFieldsByPredicate(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<Triple<BaseRuntimeChildDefinition, IBase, IBase>> thePredicate) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		FhirTerser terser = theFhirContext.newTerser();
		for (BaseRuntimeChildDefinition childDefinition : definition.getChildrenAndExtension()) {
			if (thePredicate.test(Triple.of(childDefinition, theFrom, theTo))) {
				replaceField(terser, theFrom, theTo, childDefinition);
			}
		}
	}

	/**
	 * Checks if the field exists on the resource
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFieldName   Name of the field to check
	 * @param theInstance    Resource instance to check
	 * @return Returns true if resource definition has a child with the specified name and false otherwise
	 */
	public static boolean fieldExists(FhirContext theFhirContext, String theFieldName, IBaseResource theInstance) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theInstance);
		return definition.getChildByName(theFieldName) != null;
	}

	/**
	 * Replaces the specified fields on <code>theTo</code> resource with the value from <code>theFrom</code> resource.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFrom        The resource to replace the field from
	 * @param theTo          The resource to replace the field on
	 */
	public static void replaceField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		Validate.notNull(definition);
		replaceField(theFhirContext.newTerser(), theFrom, theTo, theFhirContext.getResourceDefinition(theFrom).getChildByName(theFieldName));
	}

	/**
	 * Clears the specified field on the resource provided
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theResource
	 * @param theFieldName
	 */
	public static void clearField(FhirContext theFhirContext, IBaseResource theResource, String theFieldName) {
		BaseRuntimeChildDefinition childDefinition = getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theResource);
		clear(childDefinition.getAccessor().getValues(theResource));
	}

	/**
	 * Clears the specified field on the element provided
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFieldName   Name of the field to clear values for
	 * @param theBase        The element definition to clear values on
	 */
	public static void clearField(FhirContext theFhirContext, String theFieldName, IBase theBase) {
		BaseRuntimeElementDefinition definition = theFhirContext.getElementDefinition(theBase.getClass());
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theFieldName);
		Validate.notNull(childDefinition);
		clear(childDefinition.getAccessor().getValues(theBase));
	}

	/**
	 * Sets the provided field with the given values. This method will add to the collection of existing field values
	 * in case of multiple cardinality. Use {@link #clearField(FhirContext, IBaseResource, String)}
	 * to remove values before setting
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFieldName   Child field name of the resource to set
	 * @param theResource    The resource to set the values on
	 * @param theValues      The values to set on the resource child field name
	 */
	public static void setField(FhirContext theFhirContext, String theFieldName, IBaseResource theResource, IBase... theValues) {
		setField(theFhirContext, theFhirContext.newTerser(), theFieldName, theResource, theValues);
	}

	/**
	 * Sets the provided field with the given values. This method will add to the collection of existing field values
	 * in case of multiple cardinality. Use {@link #clearField(FhirContext, IBaseResource, String)}
	 * to remove values before setting
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theTerser      Terser to be used when cloning field values
	 * @param theFieldName   Child field name of the resource to set
	 * @param theResource    The resource to set the values on
	 * @param theValues      The values to set on the resource child field name
	 */
	public static void setField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theResource, IBase... theValues) {
		BaseRuntimeChildDefinition childDefinition = getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theResource);
		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theResource);
		if (theFromFieldValues.isEmpty()) {
			for (IBase value : theValues) {
				try {
					childDefinition.getMutator().addValue(theResource, value);
				} catch (UnsupportedOperationException e) {
					ourLog.warn("Resource {} does not support multiple values, but an attempt to set {} was made. Setting the first item only", theResource, theValues);
					childDefinition.getMutator().setValue(theResource, value);
					break;
				}
			}
			return;
		}
		List<IBase> theToFieldValues = Arrays.asList(theValues);
		mergeFields(theTerser, theResource, childDefinition, theFromFieldValues, theToFieldValues);
	}

	/**
	 * Sets the specified value at the FHIR path provided.
	 *
	 * @param theTerser   The terser that should be used for cloning the field value.
	 * @param theFhirPath The FHIR path to set the field at
	 * @param theResource The resource on which the value should be set
	 * @param theValue    The value to set
	 */
	public static void setFieldByFhirPath(FhirTerser theTerser, String theFhirPath, IBaseResource theResource, IBase theValue) {
		List<IBase> theFromFieldValues = theTerser.getValues(theResource, theFhirPath, true, false);
		for (IBase theFromFieldValue : theFromFieldValues) {
			theTerser.cloneInto(theValue, theFromFieldValue, true);
		}
	}

	/**
	 * Sets the specified value at the FHIR path provided.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFhirPath    The FHIR path to set the field at
	 * @param theResource    The resource on which the value should be set
	 * @param theValue       The value to set
	 */
	public static void setFieldByFhirPath(FhirContext theFhirContext, String theFhirPath, IBaseResource theResource, IBase theValue) {
		setFieldByFhirPath(theFhirContext.newTerser(), theFhirPath, theResource, theValue);
	}

	/**
	 * Returns field values ant the specified FHIR path from the resource.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFhirPath    The FHIR path to get the field from
	 * @param theResource    The resource from which the value should be retrieved
	 * @return Returns the list of field values at the given FHIR path
	 */
	public static List<IBase> getFieldByFhirPath(FhirContext theFhirContext, String theFhirPath, IBase theResource) {
		return theFhirContext.newTerser().getValues(theResource, theFhirPath, false, false);
	}

	/**
	 * Returns the first available field value at the specified FHIR path from the resource.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFhirPath    The FHIR path to get the field from
	 * @param theResource    The resource from which the value should be retrieved
	 * @return Returns the first available value or null if no values can be retrieved
	 */
	public static IBase getFirstFieldByFhirPath(FhirContext theFhirContext, String theFhirPath, IBase theResource) {
		List<IBase> values = getFieldByFhirPath(theFhirContext, theFhirPath, theResource);
		if (values == null || values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	private static void replaceField(FhirTerser theTerser, IBaseResource theFrom, IBaseResource theTo, BaseRuntimeChildDefinition childDefinition) {
		List<IBase> fromValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> toValues = childDefinition.getAccessor().getValues(theTo);
		if (fromValues != toValues) {
			clear(toValues);

			mergeFields(theTerser, theTo, childDefinition, fromValues, toValues);
		}
	}

	/**
	 * Merges values of all fields except for "identifier" and "meta" from <code>theFrom</code> resource to
	 * <code>theTo</code> resource. Fields values are compared via the equalsDeep method, or via object identity if this
	 * method is not available.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theFrom        Resource to merge the specified field from
	 * @param theTo          Resource to merge the specified field into
	 */
	public static void mergeFieldsExceptIdAndMeta(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo) {
		mergeFields(theFhirContext, theFrom, theTo, EXCLUDE_IDS_AND_META);
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
	public static void mergeFields(FhirContext theFhirContext, IBaseResource theFrom, IBaseResource theTo, Predicate<String> inclusionStrategy) {
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
	public static void mergeField(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
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
	public static void mergeField(FhirContext theFhirContext, FhirTerser theTerser, String theFieldName, IBaseResource theFrom, IBaseResource theTo) {
		BaseRuntimeChildDefinition childDefinition = getBaseRuntimeChildDefinition(theFhirContext, theFieldName, theFrom);

		List<IBase> theFromFieldValues = childDefinition.getAccessor().getValues(theFrom);
		List<IBase> theToFieldValues = childDefinition.getAccessor().getValues(theTo);

		mergeFields(theTerser, theTo, childDefinition, theFromFieldValues, theToFieldValues);
	}

	private static BaseRuntimeChildDefinition getBaseRuntimeChildDefinition(FhirContext theFhirContext, String theFieldName, IBaseResource theFrom) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theFrom);
		BaseRuntimeChildDefinition childDefinition = definition.getChildByName(theFieldName);
		Validate.notNull(childDefinition);
		return childDefinition;
	}

	/**
	 * Creates a new element taking into consideration elements with choice that are not directly retrievable by element
	 * name
	 *
	 *
	 * @param theFhirTerser
	 * @param theChildDefinition  Child to create a new instance for
	 * @param theFromFieldValue   The base parent field
	 * @param theConstructorParam Optional constructor param
	 * @return Returns the new element with the given value if configured
	 */
	private static IBase newElement(FhirTerser theFhirTerser, BaseRuntimeChildDefinition theChildDefinition, IBase theFromFieldValue, Object theConstructorParam) {
		BaseRuntimeElementDefinition runtimeElementDefinition;
		if (theChildDefinition instanceof RuntimeChildChoiceDefinition) {
			runtimeElementDefinition = theChildDefinition.getChildElementDefinitionByDatatype(theFromFieldValue.getClass());
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

	private static void mergeFields(FhirTerser theTerser, IBaseResource theTo, BaseRuntimeChildDefinition childDefinition, List<IBase> theFromFieldValues, List<IBase> theToFieldValues) {
		for (IBase theFromFieldValue : theFromFieldValues) {
			if (contains(theFromFieldValue, theToFieldValues)) {
				continue;
			}

			IBase newFieldValue = newElement(theTerser, childDefinition, theFromFieldValue, null);
			if (theFromFieldValue instanceof IPrimitiveType) {
				try {
					Method copyMethod = getMethod(theFromFieldValue, "copy");
					if (copyMethod != null) {
						newFieldValue = (IBase) copyMethod.invoke(theFromFieldValue, new Object[]{});
					}
				} catch (Throwable t) {
					((IPrimitiveType) newFieldValue).setValueAsString(((IPrimitiveType) theFromFieldValue).getValueAsString());
				}
			} else {
				theTerser.cloneInto(theFromFieldValue, newFieldValue, true);
			}

			try {
				theToFieldValues.add(newFieldValue);
			} catch (UnsupportedOperationException e) {
				childDefinition.getMutator().setValue(theTo, newFieldValue);
				theToFieldValues = childDefinition.getAccessor().getValues(theTo);
			}
		}
	}

	/**
	 * Clones the specified resource.
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theInstance    The instance to be cloned
	 * @param <T>            Base resource type
	 * @return Returns a cloned instance
	 */
	public static <T extends IBaseResource> T clone(FhirContext theFhirContext, T theInstance) {
		RuntimeResourceDefinition definition = theFhirContext.getResourceDefinition(theInstance.getClass());
		T retVal = (T) definition.newInstance();

		FhirTerser terser = theFhirContext.newTerser();
		terser.cloneInto(theInstance, retVal, true);
		return retVal;
	}

	/**
	 * Creates a new element instance
	 *
	 * @param theFhirContext Context holding resource definition
	 * @param theElementType Element type name
	 * @param <T>            Base element type
	 * @return Returns a new instance of the element
	 */
	public static <T extends IBase> T newElement(FhirContext theFhirContext, String theElementType) {
		BaseRuntimeElementDefinition def = theFhirContext.getElementDefinition(theElementType);
		return (T) def.newInstance();
	}

	/**
	 * Creates a new element instance
	 *
	 * @param theFhirContext      Context holding resource definition
	 * @param theElementType      Element type name
	 * @param theConstructorParam Initialization parameter for the element
	 * @param <T>                 Base element type
	 * @return Returns a new instance of the element with the specified initial value
	 */
	public static <T extends IBase> T newElement(FhirContext theFhirContext, String theElementType, Object theConstructorParam) {
		BaseRuntimeElementDefinition def = theFhirContext.getElementDefinition(theElementType);
		Validate.notNull(def);
		return (T) def.newInstance(theConstructorParam);
	}

	/**
	 * Creates a new resource definition.
	 *
	 * @param theFhirContext  Context holding resource definition
	 * @param theResourceName Name of the resource in the context
	 * @param <T>             Type of the resource
	 * @return Returns a new instance of the resource
	 */
	public static <T extends IBase> T newResource(FhirContext theFhirContext, String theResourceName) {
		RuntimeResourceDefinition def = theFhirContext.getResourceDefinition(theResourceName);
		return (T) def.newInstance();
	}

	/**
	 * Creates a new resource definition.
	 *
	 * @param theFhirContext      Context holding resource definition
	 * @param theResourceName     Name of the resource in the context
	 * @param theConstructorParam Initialization parameter for the new instance
	 * @param <T>                 Type of the resource
	 * @return Returns a new instance of the resource
	 */
	public static <T extends IBase> T newResource(FhirContext theFhirContext, String theResourceName, Object theConstructorParam) {
		RuntimeResourceDefinition def = theFhirContext.getResourceDefinition(theResourceName);
		return (T) def.newInstance(theConstructorParam);
	}

	private static void clear(List<IBase> values) {
		if (values == null) {
			return;
		}

		try {
			values.clear();
		} catch (Throwable t) {
			ourLog.debug("Unable to clear values " + String.valueOf(values), t);
		}
	}

}
